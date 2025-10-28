package functions.meta;

import functions.Function;

public class Composition implements Function {
    private final Function outer;
    private final Function inner;

    public Composition(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }

    public double getLeftDomainBorder() {
        return inner.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return inner.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        double innerValue = inner.getFunctionValue(x);
        if (Double.isNaN(innerValue)) {
            return Double.NaN;
        }
        return outer.getFunctionValue(innerValue);
    }
}
