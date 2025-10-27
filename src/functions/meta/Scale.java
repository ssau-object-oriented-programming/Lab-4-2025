package functions.meta;

import functions.Function;
import functions.InappropriateFunctionPointException;

public class Scale implements Function {
    private Function a;
    private double x, y;

    public Scale(Function a, double x, double y) { this.a = a; this.x = x; this.y = y; }

    public double getLeftDomainBorder() throws InappropriateFunctionPointException {
        return (a.getLeftDomainBorder() * x);
    }

    public double getRightDomainBorder() throws InappropriateFunctionPointException {
        return (a.getRightDomainBorder() * x);
    }

    public double getFunctionValue(double x) throws InappropriateFunctionPointException {
        return (a.getFunctionValue(x / this.x) * this.y);
    }
}
