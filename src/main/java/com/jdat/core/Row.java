package com.jdat.core;

import java.util.HashMap;
import java.util.Map;

public class Row {
    private final Map<String, Double> values; // column name -> value
    private final Map<String, Boolean> naMask; // column name -> is NA

    public Row(Map<String, Double> values, Map<String, Boolean> naMask) {
        this.values = new HashMap<>(values);
        this.naMask = new HashMap<>(naMask);
    }

    public double getDouble(String columnName) {
        if (!values.containsKey(columnName)) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        if (naMask.get(columnName)) {
            throw new IllegalStateException("Value is NA: " + columnName);
        }
        return values.get(columnName);
    }

    public boolean isNa(String columnName) {
        return naMask.getOrDefault(columnName, false);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : values.entrySet()) {
            String col = entry.getKey();
            double v = entry.getValue();
            if (naMask.get(col)) {
                sb.append(col).append(": NaN ");
            } else {
                sb.append(col).append(":").append(v).append(" ");
            }
        }
        return sb.toString();
    }
}