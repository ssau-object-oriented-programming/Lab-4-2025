package functions;
import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x;
    private double y;
    private static final long serialVersionUID = 1L;
    public FunctionPoint(double x, double y) { //Конструктор с заданными координатами
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point) { //Конструктор копирования
        this.x = point.x;
        this.y = point.y;
    }
    public FunctionPoint() { //Конструктор по умолчанию
        this(0.0, 0.0);
    }
    public double getX() {  //Геттеры и сеттеры
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
