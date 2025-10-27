package functions.basic;

public class Cos extends TrigonometricFunction{

    public double getLeftDomainBorder()  {
        return super.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return super.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return (Math.cos(x));
    }
}
