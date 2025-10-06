package functions;

import com.asmodeus.utils.LinkedList;

/**
 * Класс табулированной функции... неясно, для чего
 */
public class LinkedListTabulatedFunction implements TabulatedFunction {
    private LinkedList<FunctionPoint> points;
    /**
     * Конструктор табулированной функции по самим точкам
     * @param points точки данной функции
     */
    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2) throw new IllegalArgumentException("Points count must be greater than 2");
        for (int i = points.length - 1; i > 0; i--) {
            if (points[i].getX() <= points[i-1].getX()) throw new IllegalArgumentException("Illegal points(x coordinate)");
        }
        this.points = new LinkedList<>();
        for (FunctionPoint point : points) {
            this.points.addToEnd(new FunctionPoint(point));
        }
    }
    /**
     * Конструктор табулированной функции по кол-ву точек
     * @param leftX левая граница <s>страданий</s> по значению X
     * @param rightX правая граница <s>страданий</s> по значению X
     * @param pointsCount кол-во точек данной табулированной функции
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (pointsCount < 2) throw new IllegalArgumentException("Points count must be greater than 2");
        if (leftX >= rightX) throw new IllegalArgumentException("LeftX border must be lower than RightX border");
        this.points = new LinkedList<>();
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i <= pointsCount - 1; i++) {
            this.points.addToEnd(new FunctionPoint(leftX + i * step, 0));
        }
    }
    /**
     * Конструктор табулированной функции по заданным значениям Y(массив)
     * @param leftX левая граница <s>страданий</s> по значению X
     * @param rightX правая граница <s>страданий</s> по значению X
     * @param values точки данной табулированной функции
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        this(leftX, rightX, values.length);
        for (int i = 0; i <= values.length - 1; i++) {
            FunctionPoint p = points.get(i);
            p.setY(values[i]);
            this.points.set(i, p);
        }
    }
    @Override
    public double getLeftDomainBorder() {
        return (this.points.getSize() > 0) ? (this.points.get(0).getX()) : (Double.NaN);
    }
    @Override
    public double getRightDomainBorder() {
        return (this.points.getSize() > 0) ? (this.points.get(this.points.getSize() - 1).getX()) : (Double.NaN);
    }
    @Override
    public double getFunctionValue(double x) {
        if (x < this.getLeftDomainBorder() || this.getRightDomainBorder() < x) return Double.NaN;
        int index;
        for (index = this.points.getSize() - 2; index >= 0; index--) { // нет смысла проверять правую точку, скипаем автоматом
            if (this.points.get(index).getX() < x) break;
            if (this.points.get(index).getX() == x) return this.points.get(index).getY(); // нет смысла интерполировать
        } // здесь используется функция (y1-y0)/(x1-x0)*(x-x0) + y0
        return (this.points.get(index+1).getY() - this.points.get(index).getY()) /
                (this.points.get(index+1).getX() - this.points.get(index).getX()) *
                (x - this.points.get(index).getX()) + this.points.get(index).getY();
    }
    @Override
    public int getPointsCount() { return this.points.getSize(); }
    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        return new FunctionPoint(this.points.get(index));
    }
    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        double leftX = (index == 0) ? (Double.NEGATIVE_INFINITY) : (this.points.get(index-1).getX());
        double rightX = (index == this.points.getSize() - 1) ? (Double.POSITIVE_INFINITY) : (this.points.get(index+1).getX());
        if (leftX >= point.getX() || point.getX() >= rightX) throw new InappropriateFunctionPointException();
        this.points.set(index, new FunctionPoint(point));
    }
    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        return this.points.get(index).getX();
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        this.points.get(index).setX(x);
    }
    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        return this.points.get(index).getY();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        this.points.get(index).setY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        if (this.points.getSize() < 3) throw new IllegalStateException("Points count must be greater than 2 for deletion");
        this.points.remove(index);
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();
        int index = 0;
        for (; index < this.points.getSize(); index++) {
            if (this.points.get(index).getX() == x) throw new InappropriateFunctionPointException();
            if (this.points.get(index).getX() > x) break;
        }
        this.points.insert(index - 1, new FunctionPoint(point));
    }
}
