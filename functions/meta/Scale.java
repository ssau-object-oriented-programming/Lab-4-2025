package functions.meta;

import functions.Function;

public class Scale implements Function {
    private final Function source;
    private final double sx;
    private final double sy;

    public Scale(Function source, double sx, double sy) {
        this.source = source;
        this.sx = sx;
        this.sy = sy;
    }

    public double getLeftDomainBorder() {
        if (sx > 0) return source.getLeftDomainBorder() / sx;
        if (sx < 0) return source.getRightDomainBorder() / sx;
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        if (sx > 0) return source.getRightDomainBorder() / sx;
        if (sx < 0) return source.getLeftDomainBorder() / sx;
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        return sy * source.getFunctionValue(sx * x);
    }
}


