package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TabulatedFunction implements Function, Externalizable {
    private FunctionPoint[] points;
    private int pointsCount;
    
    public TabulatedFunction() {
    }
    
    public TabulatedFunction(double leftX, double rightX, int pointsCount) {
        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount * 2];
        
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }
    
    public TabulatedFunction(double leftX, double rightX, double[] values) {
        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount * 2];
        
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
    
    public double getLeftDomainBorder() {
        return points[0].getX();
    }
    
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }
    
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        
        if (x == getLeftDomainBorder()) {
            return points[0].getY();
        }
        
        if (x == getRightDomainBorder()) {
            return points[pointsCount - 1].getY();
        }
        
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            
            if (x >= x1 && x <= x2) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        
        return Double.NaN;
    }

    public TabulatedFunction(FunctionPoint[] sourcePoints) {
        if (sourcePoints == null || sourcePoints.length < 2) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < sourcePoints.length; i++) {
            if (sourcePoints[i - 1].getX() >= sourcePoints[i].getX()) {
                throw new IllegalArgumentException();
            }
        }
        this.pointsCount = sourcePoints.length;
        this.points = new FunctionPoint[Math.max(4, pointsCount * 2)];
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(sourcePoints[i]);
        }
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int n = in.readInt();
        this.pointsCount = n;
        this.points = new FunctionPoint[Math.max(4, n * 2)];
        for (int i = 0; i < n; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            this.points[i] = new FunctionPoint(x, y);
        }
    }
    
    public int getPointsCount() {
        return pointsCount;
    }
    
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(points[index]);
    }
    
    public void setPoint(int index, FunctionPoint point) {
        double newX = point.getX();
        
        if (index > 0 && newX <= points[index - 1].getX()) {
            return;
        }
        
        if (index < pointsCount - 1 && newX >= points[index + 1].getX()) {
            return;
        }
        
        points[index] = new FunctionPoint(point);
    }
    
    public double getPointX(int index) {
        return points[index].getX();
    }
    
    public void setPointX(int index, double x) {
        if (index > 0 && x <= points[index - 1].getX()) {
            return;
        }
        
        if (index < pointsCount - 1 && x >= points[index + 1].getX()) {
            return;
        }
        
        points[index].setX(x);
    }
    
    public double getPointY(int index) {
        return points[index].getY();
    }
    
    public void setPointY(int index, double y) {
        points[index].setY(y);
    }
    
    public void deletePoint(int index) {
        if (pointsCount <= 2) {
            return;
        }
        
        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
    }
    
    public void addPoint(FunctionPoint point) {
        if (pointsCount >= points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }
        
        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < point.getX()) {
            insertIndex++;
        }
        
        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }
}
