package functions.basic;

import functions.Function;

public class TrigonometricFunction implements Function {
    private final double a;
    private final double b;
    private final double c;

    public TrigonometricFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        return a * Math.sin(b * x) + c * Math.cos(b * x);
    }
}


