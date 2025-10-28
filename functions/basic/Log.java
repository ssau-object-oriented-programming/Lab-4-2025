package functions.basic;

import functions.Function;

public class Log implements Function {
    private final double base;

    public Log() {
        this.base = Math.E;
    }

    public Log(double base) {
        this.base = base;
    }

    public double getLeftDomainBorder() {
        return 0;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x <= 0) return Double.NaN;
        return Math.log(x) / Math.log(base);
    }
}
