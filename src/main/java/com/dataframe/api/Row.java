package com.jdat.api;

import com.dataframe.core.Row;

public class Row {
    private final com.dataframe.core.Row internal;

    public Row(com.dataframe.core.Row internal) {
        this.internal = internal;
    }

    public double getDouble(String columnName) {
        return internal.getDouble(columnName);
    }

    public boolean isNa(String columnName) {
        return internal.isNa(columnName);
    }

    @Override
    public String toString() {
        return internal.toString();
    }
}