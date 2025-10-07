package functions;

public class Functions {
    public static Function sum(final Function a, final Function b) {
        return new Function() {
            public double getLeftDomainBorder() {
                return Math.max(a.getLeftDomainBorder(), b.getLeftDomainBorder());
            }

            public double getRightDomainBorder() {
                return Math.min(a.getRightDomainBorder(), b.getRightDomainBorder());
            }

            public double getFunctionValue(double x) {
                return a.getFunctionValue(x) + b.getFunctionValue(x);
            }
        };
    }

    public static Function mult(final Function a, final Function b) {
        return new Function() {
            public double getLeftDomainBorder() {
                return Math.max(a.getLeftDomainBorder(), b.getLeftDomainBorder());
            }

            public double getRightDomainBorder() {
                return Math.min(a.getRightDomainBorder(), b.getRightDomainBorder());
            }

            public double getFunctionValue(double x) {
                return a.getFunctionValue(x) * b.getFunctionValue(x);
            }
        };
    }

    public static Function power(final Function f, final int p) {
        return new Function() {
            public double getLeftDomainBorder() {
                return f.getLeftDomainBorder();
            }

            public double getRightDomainBorder() {
                return f.getRightDomainBorder();
            }

            public double getFunctionValue(double x) {
                return Math.pow(f.getFunctionValue(x), p);
            }
        };
    }
}


