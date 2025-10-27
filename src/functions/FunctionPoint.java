package functions;

import java.io.*;

public class FunctionPoint implements Serializable {

    private double x;
    private double y;

    // Конструкторы
    public FunctionPoint() { x = 0; y = 0; }

    public FunctionPoint(double x, double y) { this.x = x; this.y = y; }

    public FunctionPoint(FunctionPoint point) { x = point.x; y = point.y; }

    // Геттеры и сеттеры
    public double getX() { return x; }

    public void setX(double x) { this.x = x; }

    public double getY() { return y; }

    public void setY(double y) { this.y = y; }

}