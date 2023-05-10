package part2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.PI;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        double a = 0;
        double b = PI;
        int n = 1_000_000_000;
        DoubleUnaryOperator f = Math::sin;
        int nThreads = 2000;

        long start = System.currentTimeMillis();
        double delta = (b - a) / nThreads;
        ExecutorService es = Executors.newFixedThreadPool(100);
//        ExecutorService es = Executors.newSingleThreadExecutor();
        List<Future<Double>> futures = new ArrayList<>(nThreads);
        for (int i = 0; i < nThreads; i++) {
            double ai = a + i * delta;
            double bi = ai + delta;
            CallableIntegralCalculator calculator = new CallableIntegralCalculator(ai, bi, n / nThreads, f);
            Future<Double> future = es.submit(calculator);
            futures.add(future);
        }
        es.shutdown();

        double total = 0;
        try {
            for (Future<Double> future : futures) {
                total += future.get();
            }
            long finish = System.currentTimeMillis();
            System.out.println("result = " + total);
            System.out.println(finish - start);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error in thread");
        }

    }
}
