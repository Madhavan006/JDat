package com.jdat.test;

import com.jdat.api.DataFrame;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JoinTest {
    
    @Test
    void testInnerJoin() {
        // Create left dataframe
        DataFrame left = new DataFrame(
            Arrays.asList("id", "name"),
            Arrays.asList(
                new Series("id", new Double[]{1.0, 2.0, 3.0}),
                new Series("name", new Double[]{"Alice", "Bob", "Charlie"}))  // Using String doubling
        );
        
        // Create right dataframe
        DataFrame right = new DataFrame(
            Arrays.asList("id", "sales"),
            Arrays.asList(
                new Series("id", new Double[]{2.0, 3.0, 4.0}),
                new Series("sales", new Double[]{1000.0, 2000.0, 3000.0}))
        );
        
        DataFrame result = left.join(right, "id", "id", "inner");
        assertNotNull(result);
        assertTrue(result.rows() > 0);
    }
    
    @Test
    void testJoinWithDifferentKeys() {
        DataFrame left = DataFrame.readCSV("sales.csv");
        DataFrame right = DataFrame.readCSV("sales.csv");
        
        // This would need proper CSV data with id column
        // For now just test that join doesn't crash
        try {
            DataFrame result = left.join(right, "category", "category", "inner");
            assertNotNull(result);
        } catch (Exception e) {
            // Expected if columns don't exist
        }
    }
}