package functions;

import java.io.*;

public interface TabulatedFunction extends Function {

    int getPointsCount();
    // Метод, возвращающий ссылку на объект, описывающий точку, имеющую указанный номер
    FunctionPoint getPoint(int index);
    // Метод, заменяющий точку по индексу на заданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    // Метод, возвращающий абсциссу точки по индексу
    double getPointX(int index);
    // Метод, изменяющий абсциссу точки по индексу
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    // Метод, возвращающий ординату точки по индексу
    double getPointY(int index);
    // Метод, изменяющий ординату точки по индексу
    void setPointY(int index, double y);
    // Метод, удаляющий точку по индексу
    void deletePoint(int index);
    // Метод, добавляющий новую точку
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    //Метод вывода
    void printTabulatedFunction();
}
