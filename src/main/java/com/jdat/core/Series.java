package com.jdat.core;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Series {
    private final String name;
    private final List<Double> data;
    private final List<Boolean> naMask; // true = is NA

    public Series(String name, List<Double> data, List<Boolean> naMask) {
        this.name = name;
        this.data = new ArrayList<>(data);
        this.naMask = new ArrayList<>(naMask);
    }

    public void setNaMask(List<Boolean> naMask) {
        this.naMask.clear();
        this.naMask.addAll(naMask);
    }

    public Series(String name, Double[] data) {
        this.name = name;
        this.data = new ArrayList<>();
        this.naMask = new ArrayList<>();
        for (Double v : data) {
            if (v == null) {
                this.data.add(0.0);
                this.naMask.add(true);
            } else {
                this.data.add(v);
                this.naMask.add(false);
            }
        }
    }

    public int size() {
        return data.size();
    }

    public double getDouble(int index) {
        return data.get(index);
    }

    public List<Double> getData() {
        return new ArrayList<>(data);
    }

    public List<Boolean> getNaMask() {
        return new ArrayList<>(naMask);
    }

    public boolean isNa(int index) {
        return naMask.get(index);
    }

    public long count() {
        long c = 0;
        for (boolean isNa : naMask) {
            if (!isNa) c++;
        }
        return c;
    }

    public double mean() {
        long c = count();
        if (c == 0) return 0.0;
        double sum = 0.0;
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i)) sum += data.get(i);
        }
        return sum / c;
    }

    public double sum() {
        double s = 0.0;
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i)) s += data.get(i);
        }
        return s;
    }

    public double min() {
        double min = Double.MAX_VALUE;
        boolean found = false;
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i)) {
                found = true;
                if (data.get(i) < min) min = data.get(i);
            }
        }
        if (!found) throw new IllegalStateException("No non-NA values");
        return min;
    }

    public double max() {
        double max = Double.MIN_VALUE;
        boolean found = false;
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i)) {
                found = true;
                if (data.get(i) > max) max = data.get(i);
            }
        }
        if (!found) throw new IllegalStateException("No non-NA values");
        return max;
    }

    public double stdDev() {
        double m = mean();
        long c = count();
        if (c <= 1) return 0.0;
        double sumSq = 0.0;
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i)) {
                double d = data.get(i) - m;
                sumSq += d * d;
            }
        }
        return Math.sqrt(sumSq / (c - 1));
    }

    public List<Integer> detectOutliers(double zScore) {
        double m = mean();
        double s = stdDev();
        if (s == 0) return Collections.emptyList();
        
        List<Integer> outliers = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i)) {
                double z = Math.abs(data.get(i) - m) / s;
                if (z > zScore) {
                    outliers.add(i);
                }
            }
        }
        return outliers;
    }

    public double var() {
        double m = mean();
        long c = count();
        if (c <= 1) return 0.0;
        double sumSq = 0.0;
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i)) {
                double d = data.get(i) - m;
                sumSq += d * d;
            }
        }
        return sumSq / (c - 1);
    }

    public OptionalDouble minOptional() {
        return data.stream()
                .filter(v -> !naMask.get(data.indexOf(v)))
                .mapToDouble(Double::doubleValue)
                .min();
    }

    public OptionalDouble maxOptional() {
        return data.stream()
                .filter(v -> !naMask.get(data.indexOf(v)))
                .mapToDouble(Double::doubleValue)
                .max();
    }

    public double correlation(Series other) {
        if (size() != other.size()) throw new IllegalArgumentException("Series must have same size");
        
        long n = count();
        double meanX = mean();
        double meanY = other.mean();
        
        double sumXY = 0.0, sumX2 = 0.0, sumY2 = 0.0;
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i) && !other.isNa(i)) {
                double x = data.get(i);
                double y = other.getDouble(i);
                sumXY += (x - meanX) * (y - meanY);
                sumX2 += Math.pow(x - meanX, 2);
                sumY2 += Math.pow(y - meanY, 2);
            }
        }
        
        double denominator = Math.sqrt(sumX2 * sumY2);
        if (denominator == 0) return 0.0;
        return sumXY / denominator;
    }

    public double covariance(Series other) {
        if (size() != other.size()) throw new IllegalArgumentException("Series must have same size");
        
        long n = count();
        double meanX = mean();
        double meanY = other.mean();
        
        double sum = 0.0;
        for (int i = 0; i < data.size(); i++) {
            if (!naMask.get(i) && !other.isNa(i)) {
                sum += (data.get(i) - meanX) * (other.getDouble(i) - meanY);
            }
        }
        return sum / (n - 1);
    }

    public long naCount() {
        long c = 0;
        for (boolean isNa : naMask) {
            if (isNa) c++;
        }
        return c;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (naMask.get(i)) sb.append("NaN ");
            else sb.append(data.get(i)).append(" ");
        }
        return sb.toString();
    }
}