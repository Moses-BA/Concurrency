import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
 
    static String[] files = new String[] { "data/sales1.csv", "data/sales2.csv", "data/sales3.csv"};
   
    static AtomicInteger sampleSize = new AtomicInteger(0);
    static AtomicInteger quantitySold = new AtomicInteger(0);


    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(3);
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        executor.submit(() -> increment(files[0], latch));
        executor.submit(() -> increment(files[1], latch));
        executor.submit(() -> increment(files[2], latch));

        Scanner scan = new Scanner(System.in);
        System.out.print("Please enter your name to access the Global Superstore data: ");
        String name = scan.nextLine();
        System.out.println("\nThank you " + name + ".\n");
        scan.close(); 

        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            executor.shutdownNow();
        }
        
        System.out.println("The sample size is: " + sampleSize);
        System.out.println("The quantity sold is: " + quantitySold);
    }

    public static void increment(String file, CountDownLatch latch) {
        try {
            Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(file).toURI());
            try {
                Files.lines(path)
                .skip(1)
                .mapToInt((line) -> Integer.parseInt(line.split(",")[2]))
                .forEachOrdered((quantity) -> {
                    sampleSize.addAndGet(1);
                    quantitySold.addAndGet(quantity);
                });
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                latch.countDown();
            }
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
        }
    }
}
