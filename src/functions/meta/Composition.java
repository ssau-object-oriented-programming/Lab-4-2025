package functions.meta;

import functions.Function;
import functions.InappropriateFunctionPointException;

public class Composition implements Function {
    Function a, b;

    public Composition(Function a,Function b) { this.a = a; this.b = b; }

    public double getLeftDomainBorder() throws InappropriateFunctionPointException {
        return a.getLeftDomainBorder();
    }

    public double getRightDomainBorder() throws InappropriateFunctionPointException {
        return a.getRightDomainBorder();
    }

    public double getFunctionValue(double x) throws InappropriateFunctionPointException {
        return (a.getFunctionValue(b.getFunctionValue(x)));
    }
}
