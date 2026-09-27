package com.jdat.test;

import com.jdat.api.DataFrame;
import com.jdat.api.Row;
import com.jdat.api.Series;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ApiTest {
    
    @Test
    void testApiExamples() {
        // Test the API examples from the requirements
        DataFrame df = DataFrame.readCSV("sales.csv");
        
        // Test head
        DataFrame head = df.head(5);
        assertNotNull(head);
        
        // Test info
        df.info();
        
        // Test describe
        df.describe();
        
        // Test filter
        DataFrame filtered = df.filter(row -> row.getDouble("sales") > 1000);
        assertTrue(filtered.rows() <= df.rows());
        
        // Test groupBy
        DataFrame grouped = df.groupBy("category");
        assertNotNull(grouped);
        
        // Test mean aggregation
        DataFrame meanResult = df.mean("sales");
        assertNotNull(meanResult);
    }
    
    @Test
    void testSeriesOperations() {
        Series s = new Series("sales", new Double[]{1000.0, 2000.0, 3000.0, null, 5000.0});
        
        assertEquals(2250.0, s.mean(), 0.001);
        assertEquals(11000.0, s.sum(), 0.001);
        assertEquals(500.0, s.min(), 0.001);
        assertEquals(5000.0, s.max(), 0.001);
        assertEquals(4, s.count());
        assertEquals(1, s.naCount());
    }
    
    @Test
    void testRowOperations() {
        Map<String, Double> values = new java.util.HashMap<>();
        values.put("sales", 1500.0);
        values.put("quantity", 20.0);
        Map<String, Boolean> naMask = new java.util.HashMap<>();
        naMask.put("sales", false);
        naMask.put("quantity", false);
        
        Row row = new Row(values, naMask);
        assertEquals(1500.0, row.getDouble("sales"), 0.001);
        assertFalse(row.isNa("sales"));
    }
    
    @Test
    void testSortBy() {
        DataFrame df = DataFrame.readCSV("sales.csv");
        DataFrame sorted = df.sortBy("sales", false);
        assertNotNull(sorted);
    }
}