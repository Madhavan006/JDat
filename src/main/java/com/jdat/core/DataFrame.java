package com.jdat.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataFrame {
    private final List<String> columnNames;
    private final List<Series> columns;
    private final int rowCount;

    public DataFrame(List<String> columnNames, List<Series> columns) {
        this.columnNames = new ArrayList<>(columnNames);
        this.columns = new ArrayList<>(columns);
        this.rowCount = columns.isEmpty() ? 0 : columns.get(0).size();
    }

    public static DataFrame readCSV(String filePath) {
        List<String> columnNames = new ArrayList<>();
        List<Series> seriesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line == null) return new DataFrame(columnNames, seriesList);

            // Parse header
            String[] headers = parseCSVLine(line);
            for (String header : headers) {
                columnNames.add(header.trim());
                seriesList.add(new Series(header, new Double[0], new ArrayList<>()));
            }

            // Parse data rows
            while ((line = br.readLine()) != null) {
                String[] values = parseCSVLine(line);
                if (values.length != columnNames.size()) continue;

                for (int i = 0; i < columnNames.size(); i++) {
                    String val = values[i].trim();
                    if (val.isEmpty() || val.equalsIgnoreCase("NaN") || val.equalsIgnoreCase("NA")) {
                        seriesList.get(i).getData().add(null);
                    } else {
                        try {
                            seriesList.get(i).getData().add(Double.parseDouble(val));
                        } catch (NumberFormatException e) {
                            seriesList.get(i).getData().add(null);
                        }
                    }
                }
            }

            // Now set up naMask properly based on null values
            for (Series s : seriesList) {
                List<Boolean> mask = new ArrayList<>();
                for (Object v : s.getData()) {
                    mask.add(v == null);
                }
                s.setNaMask(mask);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV: " + filePath, e);
        }

        return new DataFrame(columnNames, seriesList);
    }

    // Helper to parse CSV lines (simple version, handles quoted fields)
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    public List<String> columns() {
        return columnNames;
    }

    public int rows() {
        return rowCount;
    }

    public Series getSeries(String columnName) {
        int idx = columnNames.indexOf(columnName);
        if (idx == -1) throw new IllegalArgumentException("Column not found: " + columnName);
        return columns.get(idx);
    }

    public Row getRow(int index) {
        Map<String, Double> values = new HashMap<>();
        Map<String, Boolean> naMask = new HashMap<>();
        for (Series col : columns) {
            double v = col.getDouble(index);
            boolean isNa = col.isNa(index);
            values.put(col.name(), v);
            naMask.put(col.name(), isNa);
        }
        return new Row(values, naMask);
    }

    public DataFrame head(int n) {
        if (n <= 0) return new DataFrame(columnNames, Collections.<Series>emptyList());

        int limit = Math.min(n, rowCount);
        List<Series> subCols = new ArrayList<>();
        for (Series col : columns) {
            List<Double> sub = col.getData().subList(0, limit);
            List<Boolean> mask = col.getNaMask().subList(0, limit);
            subCols.add(new Series(col.name(), sub, mask));
        }
        return new DataFrame(columnNames, subCols);
    }

    public DataFrame info() {
        System.out.println("DataFrame Summary:");
        System.out.println("---------------");
        System.out.println("Rows: " + rowCount);
        System.out.println("Columns: " + columnNames.size());
        System.out.println();
        for (Series col : columns) {
            System.out.printf("Column '%s': %d non-null values%n", col.name(), col.count());
        }
        System.out.println("---------------");
        return this;
    }

    public DataFrame describe() {
        System.out.println("Description:");
        System.out.println("------------");
        for (Series col : columns) {
            System.out.printf("%s:%n", col.name());
            System.out.printf("  count: %.0f%n", col.count());
            System.out.printf("  mean: %.4f%n", col.mean());
            System.out.printf("  std: %.4f%n", col.stdDev());
            System.out.printf("  min: %.4f%n", col.min());
            System.out.printf("  25%%: %.4f%n", quantile(col, 0.25));
            System.out.printf("  50%%: %.4f%n", median(col));
            System.out.printf("  75%%: %.4f%n", quantile(col, 0.75));
            System.out.printf("  max: %.4f%n%n", col.max());
        }
        return this;
    }

    private double quantile(Series col, double q) {
        List<Double> nonNa = new ArrayList<>();
        for (int i = 0; i < col.size(); i++) {
            if (!col.isNa(i)) nonNa.add(col.getDouble(i));
        }
        if (nonNa.isEmpty()) return 0.0;
        Collections.sort(nonNa);
        int idx = (int) (q * (nonNa.size() - 1));
        return nonNa.get(idx);
    }

    private double median(Series col) {
        List<Double> nonNa = new ArrayList<>();
        for (int i = 0; i < col.size(); i++) {
            if (!col.isNa(i)) nonNa.add(col.getDouble(i));
        }
        if (nonNa.isEmpty()) return 0.0;
        Collections.sort(nonNa);
        int size = nonNa.size();
        if (size % 2 == 0) {
            return (nonNa.get(size / 2 - 1) + nonNa.get(size / 2)) / 2.0;
        } else {
            return nonNa.get(size / 2);
        }
    }

    public DataFrame filter(java.util.function.Predicate<Row> predicate) {
        List<Series> newCols = new ArrayList<>();
        for (Series col : columns) {
            List<Double> newData = new ArrayList<>();
            List<Boolean> newMask = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                Row row = getRow(i);
                if (predicate.test(row)) {
                    newData.add(col.getDouble(i));
                    newMask.add(col.isNa(i));
                } else {
                    // Keep NaN for filtered-out rows to maintain alignment
                    newData.add(Double.NaN);
                    newMask.add(true);
                }
            }
            newCols.add(new Series(col.name(), newData, newMask));
        }
        return new DataFrame(columnNames, newCols);
    }

    public DataFrame sortBy(String columnName, boolean ascending) {
        List<Integer> indices = IntStream.range(0, rowCount).boxed().collect(Collectors.toList());
        indices.sort((i1, i2) -> {
            double v1 = getRow(i1).getDouble(columnName);
            double v2 = getRow(i2).getDouble(columnName);
            return ascending ? Double.compare(v1, v2) : Double.compare(v2, v1);
        });

        List<Series> newCols = new ArrayList<>();
        for (Series col : columns) {
            List<Double> newData = new ArrayList<>();
            List<Boolean> newMask = new ArrayList<>();
            for (int idx : indices) {
                newData.add(col.getDouble(idx));
                newMask.add(col.isNa(idx));
            }
            newCols.add(new Series(col.name(), newData, newMask));
        }
        return new DataFrame(columnNames, newCols);
    }

    public DataFrame sortBy(String columnName) {
        return sortBy(columnName, true);
    }

    public DataFrame groupBy(String columnName) {
        return groupBy(columnName, Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    public DataFrame groupBy(String columnName, List<String> aggregateColumns) {
        Series groupCol = getSeries(columnName);
        Map<Object, List<Integer>> groups = new LinkedHashMap<>();

        for (int i = 0; i < rowCount; i++) {
            Object key = getRow(i).getDouble(columnName);
            groups.computeIfAbsent(k -> key, k1 -> new ArrayList<>()).add(i);
        }

        List<String> newColumnNames = new ArrayList<>();
        List<Series> newSeries = new ArrayList<>();

        // For each aggregate column, compute mean
        List<String> colsToAggregate = aggregateColumns.isEmpty() ? columnNames : aggregateColumns;
        for (String aggCol : colsToAggregate) {
            Series col = getSeries(aggCol);
            Map<Object, List<Double>> groupValues = new LinkedHashMap<>();
            for (Map.Entry<Object, List<Integer>> entry : groups.entrySet()) {
                List<Double> values = new ArrayList<>();
                for (int idx : entry.getValue()) {
                    values.add(col.getDouble(idx));
                }
                groupValues.put(entry.getKey(), values);
            }
            // Create a series of means for each group
            List<Double> means = new ArrayList<>();
            for (Map.Entry<Object, List<Double>> entry : groupValues.entrySet()) {
                double sum = 0.0;
                long count = 0;
                for (double v : entry.getValue()) {
                    if (!Double.isNaN(v)) {
                        sum += v;
                        count++;
                    }
                }
                means.add(count > 0 ? sum / count : Double.NaN);
            }
            for (Double m : means) {
                newSeries.add(new Series(aggCol + "_mean_" + m, new Double[]{m}));
                newColumnNames.add(aggCol + "_mean");
            }
        }

        // Also include the groupby column
        newColumnNames.add(columnName);
        List<Double> groupKeys = new ArrayList<>(groups.keySet()).stream()
                .map(k -> {
                    double v = Double.isNaN((Double) k) ? Double.NaN : k;
                    return v;
                })
                .collect(Collectors.toList());
        newSeries.add(new Series(columnName, groupKeys.toArray(new Double[0])));

        return new DataFrame(newColumnNames, newSeries);
    }

    public DataFrame mean(String columnName) {
        Series col = getSeries(columnName);
        List<Double> means = new ArrayList<>();
        means.add(col.mean());
        return new DataFrame(Collections.singletonList(columnName + "_mean"),
                Collections.singletonList(new Series(columnName + "_mean", means.toArray(new Double[0]))));
    }

    public DataFrame join(DataFrame other, String leftKey, String rightKey, String how) {
        Series leftKeyCol = getSeries(leftKey);
        Series rightKeyCol = other.getSeries(rightKey);

        Map<Object, List<Integer>> leftGroups = new LinkedHashMap<>();
        Map<Object, List<Integer>> rightGroups = new LinkedHashMap<>();

        for (int i = 0; i < rowCount; i++) {
            leftGroups.computeIfAbsent(leftKeyCol.getData().get(i), k -> new ArrayList<>()).add(i);
        }
        for (int i = 0; i < other.rowCount; i++) {
            rightGroups.computeIfAbsent(rightKeyCol.getData().get(i), k -> new ArrayList<>()).add(i);
        }

        List<String> newColumnNames = new ArrayList<>();
        List<Series> newSeries = new ArrayList<>();

        // Combine columns from both dataframes
        // Add all columns from left
        for (Series col : columns) {
            List<Double> combinedData = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                combinedData.add(col.getDouble(i));
            }
            newSeries.add(new Series(col.name(), combinedData, Collections.nCopies(rowCount, false)));
            newColumnNames.add(col.name());
        }

        // Add columns from right that aren't in left
        for (Series col : other.columns) {
            if (!columns.contains(col)) {
                List<Double> combinedData = new ArrayList<>();
                for (int i = 0; i < rowCount; i++) {
                    combinedData.add(Double.NaN);
                }
                newSeries.add(new Series(col.name(), combinedData, Collections.nCopies(rowCount, true)));
                newColumnNames.add(col.name());
            }
        }

        return new DataFrame(newColumnNames, newSeries);
    }

    public DataFrame mean(String columnName) {
        Series col = getSeries(columnName);
        List<Double> means = new ArrayList<>();
        means.add(col.mean());
        return new DataFrame(Collections.singletonList(columnName + "_mean"),
                Collections.singletonList(new Series(columnName + "_mean", means.toArray(new Double[0]))));
    }

    public DataFrame profile() {
        System.out.println("Profile:");
        System.out.println("=========");
        System.out.println("Rows: " + rowCount);
        System.out.println("Columns: " + columnNames.size());
        System.out.println();

        System.out.println("Column Details:");
        for (Series col : columns) {
            System.out.printf("  %s:%n", col.name());
            System.out.printf("    Type: numeric%n");
            System.out.printf("    Non-null count: %d%n", col.count());
            System.out.printf("    Null count: %d%n", col.naCount());
            System.out.printf("    Mean: %.2f%n", col.mean());
            System.out.printf("    Std Dev: %.2f%n", col.stdDev());
            System.out.printf("    Min: %.2f%n", col.min());
            System.out.printf("    Max: %.2f%n", col.max());
            System.out.printf("    Unique values: %d%n", col.size());
            System.out.println();
        }

        // Correlation matrix for numeric columns
        System.out.println("Correlation Matrix:");
        if (columnNames.size() >= 2) {
            for (String c1 : columnNames) {
                for (String c2 : columnNames) {
                    Series s1 = getSeries(c1);
                    Series s2 = getSeries(c2);
                    double corr = s1.correlation(s2);
                    System.out.printf("  %s vs %s: %.2f  ", c1, c2, corr);
                }
                System.out.println();
            }
        }

        System.out.println("=========");
        return this;
    }
}