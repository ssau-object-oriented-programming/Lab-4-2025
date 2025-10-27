package functions;

public interface Function {
    // Метод, возвращающий левую границу области определения
    double getLeftDomainBorder() throws InappropriateFunctionPointException;
    // Метод, возвращающий правую границу области определения
    double getRightDomainBorder() throws InappropriateFunctionPointException;
    // Метод, возвращающий значение функции в точке x с использованием линейной интерполяции
    double getFunctionValue(double x) throws InappropriateFunctionPointException;
}
