package functions;

import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable, Externalizable {

    private FunctionPoint[] points;
    private int size;

    // Конструкторы
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {

        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException();
        }

        points = new FunctionPoint[pointsCount + 10];
        size = pointsCount;
        double interval = (Math.abs(rightX - leftX)) / (size - 1);

        for (int i = 0; i < size; ++i) {
            points[i] = new FunctionPoint((leftX + i * interval), 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){

        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException();
        }

        points = new FunctionPoint[values.length + 10];
        size = values.length;
        double interval = (Math.abs(rightX - leftX)) / (values.length - 1);

        for (int i = 0; i < values.length; ++i){
            points[i] = new FunctionPoint((leftX + i * interval), values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] array) {

        if (array.length < 2) {
            throw new IllegalArgumentException();
        }

        size = array.length;

        for (int i = 1; i < array.length; ++i) {
            if (array[i-1].getX() >= array[i].getX()) {
                throw new IllegalArgumentException();
            }
        }

        points = new FunctionPoint[array.length + 10];
        System.arraycopy(array, 0, points, 0, array.length);
    }

    // Публичный конструктор без параметров
    public ArrayTabulatedFunction() {
    }

    // Метод, возвращающий левую границу области определения
    public double getLeftDomainBorder() throws InappropriateFunctionPointException {

        if (size == 0){
            throw new InappropriateFunctionPointException();
        }
        return points[0].getX();
    }
    // Метод, возвращающий правую границу области определения
    public double getRightDomainBorder() throws InappropriateFunctionPointException {

        if (size == 0){
            throw new InappropriateFunctionPointException();
        }
        return points[size - 1].getX();
    }

    // Метод, возвращающий значение функции в точке x с использованием линейной интерполяции
    public double getFunctionValue(double x) throws InappropriateFunctionPointException {

        if ((size == 0) || ((x < getLeftDomainBorder()) || x > getRightDomainBorder())){
            throw new InappropriateFunctionPointException();
        }

        for (int i = 0; i < size - 1; ++i) {
            if (x >= points[i].getX() && x <= points[i + 1].getX()) {

                double x1 = points[i].getX();
                double y1 = points[i].getY();
                double x2 = points[i + 1].getX();
                double y2 = points[i + 1].getY();

                // Линейная интерполяция
                return (y1 + (x - x1) * (y2 - y1) / (x2 - x1));
            }
        }
        // Если всё-таки не удалось найти точку
        throw new InappropriateFunctionPointException();
    }

    // Метод, возвращающий количество точек
    public int getPointsCount() {
        return size;
    }

    //Метод, возвращающий реальный размер массива (с учётом запаса)
    public int getRealLength() {
        return points.length;
    }

    // Метод, возвращающий ссылку на объект, описывающий точку, имеющую указанный номер
    public FunctionPoint getPoint(int index) {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            // Возвращаем копию точки, чтобы предотвратить несанкционированный доступ к точке
            return new FunctionPoint(points[index]);
        }
    }

    // Метод, заменяющий точку по индексу на заданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            double x = point.getX();

            for (int i = 0; i < size; ++i){
                if(points[i].getX() == x){
                    throw new InappropriateFunctionPointException();
                }
            }

            if (size > 1)
                // Обрабатываем отдельно первый элемент
                if (index == 0 && x <= points[1].getX())
                    points[0] = point;
                    // Обрабатываем отдельно последний элемент
                else if (index == size - 1 && x >= points[size - 2].getX())
                    points[size - 1] = point;
                    // Обрабатываем остальные случаи
                else if (x >= points[index - 1].getX() && x <= points[index + 1].getX())
                    points[index] = point;
                else {
                    throw new InappropriateFunctionPointException();
                }
        }
    }

    // Метод, возвращающий абсциссу точки по индексу
    public double getPointX(int index) {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            return points[index].getX();
        }
    }

    // Метод, изменяющий абсциссу точки по индексу
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            if (size > 1)
                if (index == 0 && x <= points[1].getX())
                    points[0].setX(x);
                else if (index == size - 1 && x >= points[size - 2].getX())
                    points[size - 1].setX(x);
                else if (x >= points[index - 1].getX() && x <= points[index + 1].getX())
                    points[index].setX(x);
                else {
                    throw new InappropriateFunctionPointException();
                }
        }
    }

    // Метод, возвращающий ординату точки по индексу
    public double getPointY(int index) {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            return points[index].getY();
        }
    }

    // Метод, изменяющий ординату точки по индексу
    public void setPointY(int index, double y) {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            points[index].setY(y);
        }
    }

    // Метод, удаляющий точку по индексу
    public void deletePoint(int index) {

        if (size < 3) {
            throw new IllegalStateException();
        }
        else if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            --size;
            System.arraycopy(points, index + 1, points, index, size - index);
        }
    }

    // Метод, добавляющий новую точку
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{

        double x = point.getX();

        for (int i = 0; i < size; ++i){
            if(points[i].getX() == x){
                throw new InappropriateFunctionPointException();
            }
        }

        if(x < getLeftDomainBorder()){
            if (size < points.length) {
                System.arraycopy(points, 0, points, 1, size);
                points[0] = point;
            }
            else{
                FunctionPoint[] newPoints = new FunctionPoint[size + 10];
                System.arraycopy(points, 0, newPoints, 1, points.length);
                newPoints[0] = point;
                points = newPoints;
            }
            ++size;
        }
        else if(x > getRightDomainBorder()){
            if (size < points.length) {
                points[size] = point;
            }
            else{
                FunctionPoint[] newPoints = new FunctionPoint[size + 10];
                System.arraycopy(points, 0, newPoints, 0, points.length);
                newPoints[size] = point;
                points = newPoints;
            }
            ++size;
        }
        else {
            if (size < points.length) {
                int index = 0;
                while (index < size && x > points[index].getX()) {
                    ++index;
                }
                System.arraycopy(points, index, points, index + 1, size - index);
                points[index] = point;
            } else {
                FunctionPoint[] newPoints = new FunctionPoint[size + 10];
                int index = 0;
                while (index < size && x > points[index].getX()) {
                    ++index;
                }
                System.arraycopy(points, 0, newPoints, 0, index);
                newPoints[index] = point;
                System.arraycopy(points, index, newPoints, index + 1, size - index);
                points = newPoints;
            }
            ++size;
        }
    }

    //Метод вывода
    public void printTabulatedFunction() {

        int pointsCount = getPointsCount();
        for (int i = 0; i < pointsCount; i++) {
            double x = getPointX(i);
            double y = getPointY(i);
            System.out.println("x = " + x + ", y = " + y);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // Реализация записи полей внешнего объекта
        out.writeObject(points);
        out.writeInt(size);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Реализация чтения полей из внешнего объекта
        points = (FunctionPoint[]) in.readObject();
        size = in.readInt();
    }

}