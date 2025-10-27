package functions.meta;

import functions.Function;
import functions.InappropriateFunctionPointException;

public class Power implements Function {
    private Function a;
    double n;
    public Power(Function a,double n) { this.a = a; this.n = n; }

    public double getLeftDomainBorder() throws InappropriateFunctionPointException {
        return a.getLeftDomainBorder();
    }

    public double getRightDomainBorder() throws InappropriateFunctionPointException {
        return a.getRightDomainBorder();
    }

    public double getFunctionValue(double x) throws InappropriateFunctionPointException {
        return (Math.pow(a.getFunctionValue(x), n));
    }
}
