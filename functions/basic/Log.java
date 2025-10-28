package functions.basic;
import functions.Function;

public class Log implements Function {
    private double a;

    public Log(double a) {
        this.a = a;
    }

    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x <= 0 )
            return Double.NaN;
        return Math.log(x) / Math.log(a);
    }
}