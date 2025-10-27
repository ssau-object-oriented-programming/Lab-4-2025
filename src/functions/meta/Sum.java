package functions.meta;

import functions.Function;
import functions.InappropriateFunctionPointException;

public class Sum implements Function {

    private Function a, b;
    public Sum(Function a,Function b) { this.a = a; this.b = b; }

    public double getLeftDomainBorder() throws InappropriateFunctionPointException {
        return (Math.max(a.getLeftDomainBorder(), b.getLeftDomainBorder()));
    }

    public double getRightDomainBorder() throws InappropriateFunctionPointException {
        return (Math.min(a.getRightDomainBorder(), b.getRightDomainBorder()));
    }

    public double getFunctionValue(double x) throws InappropriateFunctionPointException {
        return (a.getFunctionValue(x) + b.getFunctionValue(x));
    }
}
