# Concurrency

The program requires the following:

Java Development Kit (JDK) 8 or higher
Three CSV files, data/sales1.csv, data/sales2.csv, and data/sales3.csv

Notes:
The program uses AtomicInteger objects to protect the shared variables sampleSize and quantitySold. This ensures that the variables are updated correctly even when multiple threads are accessing them simultaneously.
The program uses a CountDownLatch object to ensure that all three threads have finished processing the CSV files before the main thread prints the results to the console.
