package com.jdat.test;

import com.jdat.api.DataFrame;
import com.jdat.api.Series;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SeriesTest {
    
    @Test
    void testMean() {
        Series s = new Series("test", new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(3.0, s.mean(), 0.001);
    }
    
    @Test
    void testMeanWithNa() {
        Series s = new Series("test", new Double[]{1.0, null, 3.0, 4.0, 5.0});
        assertEquals(3.25, s.mean(), 0.001);
    }
    
    @Test
    void testSum() {
        Series s = new Series("test", new Double[]{1.0, 2.0, 3.0});
        assertEquals(6.0, s.sum(), 0.001);
    }
    
    @Test
    void testMin() {
        Series s = new Series("test", new Double[]{5.0, 3.0, 8.0, 1.0});
        assertEquals(1.0, s.min(), 0.001);
    }
    
    @Test
    void testMax() {
        Series s = new Series("test", new Double[]{5.0, 3.0, 8.0, 1.0});
        assertEquals(8.0, s.max(), 0.001);
    }
    
    @Test
    void testCount() {
        Series s = new Series("test", new Double[]{1.0, null, 3.0});
        assertEquals(2, s.count());
    }
    
    @Test
    void testNaCount() {
        Series s = new Series("test", new Double[]{1.0, null, 3.0});
        assertEquals(1, s.naCount());
    }
    
    @Test
    void testStdDev() {
        Series s = new Series("test", new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(1.5811, s.stdDev(), 0.001);
    }
    
    @Test
    void testVar() {
        Series s = new Series("test", new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(2.5, s.var(), 0.001);
    }
    
    @Test
    void testMinOptional() {
        Series s = new Series("test", new Double[]{5.0, 3.0, 8.0, 1.0});
        assertTrue(s.minOptional().isPresent());
        assertEquals(1.0, s.minOptional().getAsDouble(), 0.001);
    }
    
    @Test
    void testMaxOptional() {
        Series s = new Series("test", new Double[]{5.0, 3.0, 8.0, 1.0});
        assertTrue(s.maxOptional().isPresent());
        assertEquals(8.0, s.maxOptional().getAsDouble(), 0.001);
    }
    
    @Test
    void testSeriesWithNulls() {
        Series s = new Series("test", new Double[]{1.0, null, 3.0});
        assertTrue(s.isNa(1));
        assertEquals(1.0, s.getDouble(0), 0.001);
        assertEquals(3.0, s.getDouble(2), 0.001);
    }
}