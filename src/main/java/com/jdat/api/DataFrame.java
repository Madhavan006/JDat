package com.jdat.api;

import com.dataframe.core.DataFrame;
import com.dataframe.core.Series;

public class DataFrame {
    private final com.dataframe.core.DataFrame internal;

    public DataFrame(com.dataframe.core.DataFrame internal) {
        this.internal = internal;
    }

    public static DataFrame readCSV(String filePath) {
        return new DataFrame(com.dataframe.core.DataFrame.readCSV(filePath));
    }

    public List<String> columns() {
        return internal.columns();
    }

    public int rows() {
        return internal.rows();
    }

    public Series getSeries(String columnName) {
        return new Series(internal.getSeries(columnName));
    }

    public Row getRow(int index) {
        return new Row(internal.getRow(index));
    }

    public DataFrame head(int n) {
        return new DataFrame(internal.head(n));
    }

    public DataFrame info() {
        internal.info();
        return this;
    }

    public DataFrame describe() {
        internal.describe();
        return this;
    }

    public DataFrame filter(java.util.function.Predicate<Row> predicate) {
        return new DataFrame(internal.filter(predicate));
    }

    public DataFrame sortBy(String columnName) {
        return new DataFrame(internal.sortBy(columnName));
    }

    public DataFrame sortBy(String columnName, boolean ascending) {
        return new DataFrame(internal.sortBy(columnName, ascending));
    }

    public DataFrame groupBy(String columnName) {
        return new DataFrame(internal.groupBy(columnName));
    }

    public DataFrame groupBy(String columnName, String... aggregateColumns) {
        return new DataFrame(internal.groupBy(columnName, Arrays.asList(aggregateColumns)));
    }

    public DataFrame mean(String columnName) {
        return new DataFrame(internal.mean(columnName));
    }

    public DataFrame join(DataFrame other, String leftKey, String rightKey, String how) {
        return new DataFrame(internal.join(other.internal, leftKey, rightKey, how));
    }

    public DataFrame profile() {
        return new DataFrame(internal.profile());
    }

    public Series series(String columnName) {
        return new Series(internal.getSeries(columnName));
    }

    @Override
    public String toString() {
        return internal.toString();
    }
}