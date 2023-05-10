package part3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.PI;

public class Main {

    double total;
    int finished;

    Lock lock;
    private Condition condition;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        double a = 0;
        double b = PI;
        int n = 1_000_000_000;
        DoubleUnaryOperator f = Math::sin;
        int nThreads = 100;
        double delta = (b - a) / nThreads;
        total = 0;
        finished = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < nThreads; i++) {
            double ai = a + i * delta;
            double bi = ai + delta;
            new Thread(new RunnableIntegralCalculator(ai, bi, n / nThreads, f, this)).start();
        }

        lock = new ReentrantLock();
        condition = lock.newCondition();

        lock.lock();
        try {
            while (finished < nThreads) {
                condition.await();
            }
        } catch (InterruptedException e) {
            System.err.println("Exception in thread");
        } finally {
            lock.unlock();
        }
        long finish = System.currentTimeMillis();
        System.out.println("result = " + total);
        System.out.println(finish - start);
    }

    public void sendResult(double v) {
        try {
            lock.lock();
            total += v;
            finished++;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
