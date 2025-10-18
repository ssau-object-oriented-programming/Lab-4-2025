package functions;
import functions.meta.*;

public class Functions {
    private Functions() {} // Приватный конструктор для предотвращения создания объекта
    public static Function shift(Function f, double shiftX, double shiftY){ //возвращает объект функции, полученной из исходной сдвигом вдоль осей
        return new Shift(f, shiftX, shiftY);
    };
    public static Function scale(Function f, double kX, double kY){ //возвращает объект функции, полученной из исходной масштабированием вдоль осей
        return new Scale(f, kX, kY);
    };
    public static Function power(Function f, double pow) { //возвращает объект функции, являющейся заданной степенью исходной
        return new Power(f, pow);
    };
    public static Function sum(Function f1, Function f2) { //возвращает объект функции, являющейся суммой двух исходных
        return new Sum(f1, f2);
    };
    public static Function mult(Function f1, Function f2) { //возвращает объект функции, являющейся произведением двух исходных
        return new Mult(f1, f2);
    };
    public static Function composition(Function f1, Function f2) { //возвращает объект функции, являющейся композицией двух исходных
        return new Composition(f1, f2);
    };
}
