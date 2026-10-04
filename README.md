# JDat - Lightweight Pandas-inspired DataFrame Library in Java 17+

![JDat Banner](https://raw.githubusercontent.com/Madhavan006/JDat/main/docs/banner.png)

[![GitHub](https://img.shields.io/github/stars/Madhavan006/JDat?style=for-the-badge)](https://github.com/Madhavan006/JDat)
[![GitHub forks](https://img.shields.io/github/forks/Madhavan006/JDat?style=for-the-badge)](https://github.com/Madhavan006/JDat)
[![GitHub license](https://img.shields.io/github/license/Madhavan006/JDat?style=for-the-badge)](https://github.com/Madhavan006/JDat)
[![GitHub issues](https://img.shields.io/github/issues/Madhavan006/JDat?style=for-the-badge)](https://github.com/Madhavan006/JDat)
[![GitHub stars](https://img.shields.io/github/stars/Madhavan006/JDat?style=for-the-badge)](https://github.com/Madhavan006/JDat)

## 📖 Overview

**JDat** is a lightweight, pure-Java DataFrame library inspired by Pandas, built from scratch using Java 17+. It provides a clean, expressive API for tabular data manipulation without depending on existing DataFrame libraries like Tablesaw, Smile, ND4J, or any Python-based wrappers.

The library is designed to be:
- **Lightweight** - Only Apache Commons CSV and Google Guava as dependencies
- **Educational** - Good for learning Java collections, streams, and OOP design
- **Practical** - Suitable for data processing, cleaning, and analysis tasks
- **Pure Java** - No Python wrappers, no native dependencies

## 🚀 Quick Start

```java
import com.jdat.api.DataFrame;

// Read data from CSV
DataFrame df = DataFrame.readCSV("sales.csv");

// Basic operations
df.head(5);           // First 5 rows
df.info();            // Summary info
df.describe();        // Descriptive statistics

// Filtering
DataFrame filtered = df.filter(row -> row.getDouble("sales") > 1000);

// Grouping and aggregation
DataFrame grouped = df.groupBy("category").mean("sales");

// Sorting
DataFrame sorted = df.sortBy("sales");
```

## 📦 Installation

### Maven

Add the JDat library to your Maven `pom.xml`:

```xml
<dependencies>
    <!-- JDat DataFrame Library -->
    <dependency>
        <groupId>com.jdat</groupId>
        <artifactId>JDat</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Apache Commons CSV -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-csv</artifactId>
        <version>1.10.0</version>
    </dependency>

    <!-- Google Guava -->
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>31.1-jre</version>
    </dependency>
</dependencies>
```

### Manual Build

```bash
mvn clean package
```

The JAR will be at `target/JDat-1.0.0.jar`.

## 🔧 API Reference

### Creating and Loading Data

| Method | Description |
|--------|-------------|
| `DataFrame.readCSV("sales.csv")` | Load data from a CSV file |
| `DataFrame head(int n)` | Return first n rows |
| `DataFrame info()` | Print summary information |
| `DataFrame describe()` | Print descriptive statistics |

### Data Manipulation

| Method | Description |
|--------|-------------|
| `DataFrame filter(Predicate<Row> predicate)` | Filter rows based on predicate |
| `DataFrame sortBy(String columnName)` | Sort by column (ascending) |
| `DataFrame sortBy(String columnName, boolean ascending)` | Sort by column (direction) |
| `DataFrame groupBy(String columnName)` | Group by column |
| `DataFrame groupBy(String columnName, String... aggregateColumns)` | Group by column with aggregations |
| `DataFrame mean(String columnName)` | Compute mean of a column |

### Row Access

```java
DataFrame df = DataFrame.readCSV("data.csv");
Row row = df.getRow(0);
double sales = row.getDouble("sales");      // Get value by column name
boolean isNa = row.isNa("sales");           // Check if value is NA
```

### Series Operations

```java
DataFrame df = DataFrame.readCSV("data.csv");
Series series = df.series("sales");
double mean = series.mean();
double sum = series.sum();
double min = series.min();
double max = series.max();
double std = series.stdDev();
double var = series.var();
```

## 📝 Usage Examples

### Basic Operations

```java
import com.jdat.api.DataFrame;

public class Main {
    public static void main(String[] args) {
        // Load data from CSV
        DataFrame df = DataFrame.readCSV("sales.csv");
        
        // View first 5 rows
        DataFrame head = df.head(5);
        
        // Summary info
        df.info();
        
        // Descriptive statistics
        df.describe();
        
        // Filter rows where sales > 1000
        DataFrame filtered = df.filter(row -> row.getDouble("sales") > 1000);
        
        // Group by category and compute mean sales
        DataFrame grouped = df.groupBy("category").mean("sales");
        
        // Sort by sales column
        DataFrame sorted = df.sortBy("sales");
    }
}
```

### Filtering Example

```java
DataFrame df = DataFrame.readCSV("sales.csv");
DataFrame filtered = df.filter(row -> row.getDouble("sales") > 1000);
System.out.println("Rows with sales > 1000: " + filtered.rows());
```

### Grouping Example

```java
DataFrame df = DataFrame.readCSV("sales.csv");
DataFrame grouped = df.groupBy("category").mean("sales");
System.out.println("Grouped data:");
grouped.describe();
```

### Sorting Example

```java
DataFrame df = DataFrame.readCSV("sales.csv");
DataFrame sortedDesc = df.sortBy("sales", false);  // Descending
DataFrame sortedAsc = df.sortBy("sales");           // Ascending (default)
```

## 📁 Project Structure

```
JDat/
├── pom.xml                          # Maven project configuration
├── LICENSE                          # MIT license
├── build.bat / compile.bat          # Build scripts
├── src/main/java/com/jdat/core/     # Core implementation
│   ├── Series.java                  # Statistical series with mean, std, correlation, etc.
│   ├── Row.java                     # Row accessor with column name lookup
│   └── DataFrame.java               # Main DataFrame implementation
├── src/main/java/com/jdat/api/      # Public API (matches Pandas-like API)
│   ├── Series.java                  # Public Series API
│   ├── Row.java                     # Public Row API  
│   └── DataFrame.java               # Public DataFrame API
├── src/test/java/com/jdat/test/     # Comprehensive test suite
│   ├── SeriesTest.java              # Series statistics tests
│   ├── DataFrameTest.java         # DataFrame basic tests
│   ├── ApiTest.java                 # API examples from requirements
│   ├── StatisticsTest.java        # Correlation, covariance, outlier tests
│   └── JoinTest.java                # Join operations tests
└── sales.csv                        # Sample dataset for testing
```

## 🛠️ Features

### Data Loading
- `readCSV()` - Load data from CSV files with header parsing
- Supports quoted fields and commas within values

### Selection and Filtering
- `filter()` - Predicate-based row filtering using `Row` API
- `sortBy()` - Ascending/descending sort by column

### Aggregation and Grouping
- `groupBy()` - Group rows by column value
- `mean()` - Compute mean of a column within groups
- Additional aggregations can be implemented

### Descriptive Statistics

**Series Methods:**
- `mean()`, `sum()`, `min()`, `max()`
- `stdDev()`, `var()` - Sample standard deviation and variance
- `correlation(Series)` - Pearson correlation coefficient
- `covariance(Series)` - Covariance
- `detectOutliers(double zScore)` - Z-score based outlier detection

**DataFrame Methods:**
- `info()` - Summary of rows, columns, and non-null counts
- `describe()` - Mean, std, min, max, quartiles

### Joins

- `join(DataFrame other, String leftKey, String rightKey, String how)` 
- Supports inner join configuration via `how` parameter

### Profiling

- `profile()` - Comprehensive output including:
  - Row and column counts
  - Per-column statistics (mean, std, min, max)
  - Correlation matrix for all numeric columns

## 🧪 Test Coverage

The library includes **5 comprehensive test classes**:

| Test Class | Focus |
|------------|-------|
| `SeriesTest.java` | Series statistics (mean, std, min, max, count, NA handling) |
| `DataFrameTest.java` | DataFrame basics (CSV, head, info, describe, filter, sort, groupBy) |
| `ApiTest.java` | API examples from the requirements |
| `StatisticsTest.java` | Correlation, covariance, outlier detection |
| `JoinTest.java` | Join operations |

Run tests with:
```bash
mvn test
```

## 🤝 Contributing

Contributions are welcome! Please feel free to:
1. Fork the repository
2. Create a feature branch
3. Submit a pull request

### Development Setup

```bash
# Clone the repository
git clone https://github.com/Madhavan006/JDat.git
cd JDat

# Build the project
mvn clean compile

# Run tests
mvn test

# Generate Javadocs
mvn javadoc:javadoc
```

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```



---

**JDat** - DataFrame functionality for Java, without the Python overhead.


