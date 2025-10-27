package functions.basic;

public class Tan extends TrigonometricFunction{

    public double getLeftDomainBorder() { return super.getLeftDomainBorder(); }

    public double getRightDomainBorder() { return super.getRightDomainBorder(); }

    public double getFunctionValue(double x) { return (Math.tan(x)); }
}