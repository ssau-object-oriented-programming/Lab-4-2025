package functions;

import java.io.*;
import java.util.StringTokenizer;

public class TabulatedFunctions {
    private TabulatedFunctions() {} // Приватный конструктор для предотвращения создания объекта

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // Проверки
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount]; // Создание массива точек
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(points);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException { //Метод вывода табулированной функции в байтовый поток
        DataOutputStream dataOut = new DataOutputStream(out);

        // Записываем количество точек
        dataOut.writeInt(function.getPointsCount());

        // Записываем координаты точек
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        dataOut.flush(); // Не закрываем поток, чтобы пользователь мог продолжать с ним работать
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException { //Метод ввода табулированной функции из байтового потока
        DataInputStream dataIn = new DataInputStream(in);

        // Читаем количество точек
        int pointsCount = dataIn.readInt();

        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException { //Метод записи табулированной функции в символьный поток
        PrintWriter writer = new PrintWriter(out);
        // Записываем количество точек
        writer.print(function.getPointsCount());
        writer.print(" ");
        // Записываем координаты точек через пробел
        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(function.getPointX(i));
            writer.print(" ");
            writer.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1) {
                writer.print(" ");
            }
        }
        writer.flush(); // Не закрываем поток
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException { //Метод чтения табулированной функции из символьного потока
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        // Читаем количество точек
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;
        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;

            tokenizer.nextToken();
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        return new ArrayTabulatedFunction(points);
    }
}

