package com.jdat.test;

import com.jdat.api.DataFrame;
import com.jdat.api.Series;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DataFrameTest {
    
    @Test
    void testReadCSV() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        assertNotNull(df);
    }
    
    @Test
    void testColumns() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        assertNotNull(df.columns());
    }
    
    @Test
    void testRows() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        assertTrue(df.rows() > 0);
    }
    
    @Test
    void testHead() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        DataFrame head = df.head(5);
        assertNotNull(head);
        assertTrue(head.rows() <= 5);
    }
    
    @Test
    void testInfo() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        df.info();
    }
    
    @Test
    void testDescribe() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        df.describe();
    }
    
    @Test
    void testFilter() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        DataFrame filtered = df.filter(row -> row.getDouble("sales") > 1000);
        assertTrue(filtered.rows() <= df.rows());
    }
    
    @Test
    void testSortBy() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        DataFrame sorted = df.sortBy("sales");
        assertNotNull(sorted);
    }
    
    @Test
    void testGroupBy() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        DataFrame grouped = df.groupBy("category");
        assertNotNull(grouped);
    }
    
    @Test
    void testMeanAggregation() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        DataFrame mean = df.mean("sales");
        assertNotNull(mean);
    }
}