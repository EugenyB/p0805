package main;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.PI;

public class Main {

    private double totalResult;
    private int finished;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        double a = 0;
        double b = PI;
        int n = 1_000_000_000;
        DoubleUnaryOperator f = Math::sin;

//        IntegralCalculator calculator = new IntegralCalculator(a, b, n, f);
//        long start = System.currentTimeMillis();
//        double v = calculator.calculate();
//        long finish = System.currentTimeMillis();
//        System.out.println("v = " + v);
//        System.out.println(finish - start);

        int nThreads = 20;
        totalResult = 0;
        finished = 0;

        long start = System.currentTimeMillis();
        double delta = (b - a) / nThreads;
        for (int i = 0; i < nThreads; i++) {
            double at = a + delta * i;
            double bt = at + delta;
            new Thread(new RunnableIntegralCalculator(at, bt, n / nThreads, f, this)).start();
        }
        try {
            synchronized (this) {
                while (finished < nThreads) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long finish = System.currentTimeMillis();
        System.out.println("totalResult = " + totalResult);
        System.out.println(finish - start);
    }

    public synchronized void sendResult(double result) {
        totalResult += result;
        finished++;
//        finish();
        notify();
    }

//    private synchronized void finish() {
//        finished++;
//    }
}
