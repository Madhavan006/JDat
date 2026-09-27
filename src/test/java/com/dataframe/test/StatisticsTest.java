package com.jdat.test;

import com.jdat.api.Series;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class StatisticsTest {
    
    @Test
    void testCorrelation() {
        Series s1 = new Series("height", new Double[]{160.0, 170.0, 180.0, 175.0, 165.0});
        Series s2 = new Series("weight", new Double[]{60.0, 70.0, 80.0, 75.0, 65.0});
        
        double corr = s1.correlation(s2);
        assertTrue(corr > 0.9 && corr <= 1.0, "Expected high positive correlation, got " + corr);
    }
    
    @Test
    void testCorrelationNegative() {
        Series s1 = new Series("x", new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Series s2 = new Series("y", new Double[]{5.0, 4.0, 3.0, 2.0, 1.0});
        
        double corr = s1.correlation(s2);
        assertTrue(corr < -0.9 && corr >= -1.0, "Expected high negative correlation, got " + corr);
    }
    
    @Test
    void testCovariance() {
        Series s1 = new Series("x", new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Series s2 = new Series("y", new Double[]{2.0, 4.0, 6.0, 8.0, 10.0});
        
        double cov = s1.covariance(s2);
        assertEquals(2.5, cov, 0.001);
    }
    
    @Test
    void testOutlierDetection() {
        Series s = new Series("values", new Double[]{1.0, 2.0, 3.0, 100.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0});
        
        List<Integer> outliers = s.detectOutliers(2.0);
        assertTrue(outliers.contains(3), "Value 100.0 at index 3 should be an outlier");
    }
    
    @Test
    void testOutlierNoOutliers() {
        Series s = new Series("values", new Double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0});
        
        List<Integer> outliers = s.detectOutliers(2.0);
        assertTrue(outliers.isEmpty(), "Expected no outliers, got " + outliers);
    }
}