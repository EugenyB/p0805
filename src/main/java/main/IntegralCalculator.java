package main;

import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

public class IntegralCalculator {

    private double a;
    private double b;
    private double h;
    private int n;
    private DoubleUnaryOperator f;

    public IntegralCalculator(double a, double b, int n, DoubleUnaryOperator f) {
        this.a = a;
        this.b = b;
        this.n = n;
        this.f = f;
        h = (b - a) / n;
    }

    public double calculate() {
        return IntStream.range(0, n).mapToDouble(i -> a + i * h + h / 2).map(f).map(y -> y * h).sum();
    }
}
