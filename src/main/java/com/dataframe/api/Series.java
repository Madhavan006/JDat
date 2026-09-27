package com.jdat.api;

import com.dataframe.core.Series;

public class Series {
    private final com.dataframe.core.Series internal;

    public Series(com.dataframe.core.Series internal) {
        this.internal = internal;
    }

    public int size() {
        return internal.size();
    }

    public double getDouble(int index) {
        return internal.getDouble(index);
    }

    public boolean isNa(int index) {
        return internal.isNa(index);
    }

    public long count() {
        return internal.count();
    }

    public double mean() {
        return internal.mean();
    }

    public double sum() {
        return internal.sum();
    }

    public double min() {
        return internal.min();
    }

    public double max() {
        return internal.max();
    }

    public double stdDev() {
        return internal.stdDev();
    }

    public double var() {
        return internal.var();
    }

    public OptionalDouble minOptional() {
        return internal.minOptional();
    }

    public OptionalDouble maxOptional() {
        return internal.maxOptional();
    }

    public long naCount() {
        return internal.naCount();
    }

    public String name() {
        return internal.name();
    }

    @Override
    public String toString() {
        return internal.toString();
    }
}