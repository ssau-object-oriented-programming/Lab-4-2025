package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x;
    private double y;
    
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    
    public FunctionPoint() {
        this.x = 0;
        this.y = 0;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
}
