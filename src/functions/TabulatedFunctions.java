package functions;

import java.io.*;

public class TabulatedFunctions {
    private static final double EPSILON = 1e-10;

    // Приватный конструктор для предотвращения создания объекта
    private TabulatedFunctions() {};

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {

        if (leftX < function.getLeftDomainBorder() - EPSILON || rightX > function.getRightDomainBorder() + EPSILON)  {
            throw new IllegalArgumentException();
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double interval = (Math.abs(rightX - leftX)) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; ++i){
            double currentX = leftX + i * interval;
            if (currentX < function.getLeftDomainBorder() - EPSILON ||
                    currentX > function.getRightDomainBorder() + EPSILON) {
                throw new IllegalArgumentException();
            }
            points[i] = new FunctionPoint(currentX, function.getFunctionValue(currentX));
        }
        return new ArrayTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {

        DataOutputStream dataOut = new DataOutputStream(out);

        // Записываем количество точек
        dataOut.writeInt(function.getPointsCount());

        // Записываем значения координат точек
        for (int i = 0; i < function.getPointsCount(); ++i) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        // Закрываем поток
        dataOut.close();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {

        DataInputStream dataIn = new DataInputStream(in);

        // Считываем количество точек
        int pointsCount = dataIn.readInt();

        // Создаем массив точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double prevX = Double.NEGATIVE_INFINITY;

        // Считываем значения координат точек
        for (int i = 0; i < pointsCount; ++i) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();

            // Проверяем упорядоченность точек с использованием эпсилона
            if (i > 0 && x <= prevX + EPSILON) {
                throw new IOException();
            }

            points[i] = new FunctionPoint(x, y);
            prevX = x;
        }

        // Закрываем поток
        dataIn.close();

        // Создаем и возвращаем объект табулированной функции
        return new ArrayTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {

        BufferedWriter writer = new BufferedWriter(out);

        // Записываем количество точек
        writer.write(Integer.toString(function.getPointsCount()));

        // Записываем значения координат точек
        for (int i = 0; i < function.getPointsCount(); ++i) {
            writer.write(' ');
            writer.write(Double.toString(function.getPointX(i)));
            writer.write(' ');
            writer.write(Double.toString(function.getPointY(i)));
        }

        // Закрываем поток
        writer.close();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {

        StreamTokenizer tokenizer = new StreamTokenizer(in);

        // Переводим токен
        tokenizer.nextToken();

        // Считали кол-во точек
        int pointsCount = (int)tokenizer.nval;

        double x, y;
        FunctionPoint points[] = new FunctionPoint[pointsCount];

        double prevX = Double.NEGATIVE_INFINITY;

        // Считываем значения координат точек
        for(int i = 0; i < pointsCount; ++i){
            tokenizer.nextToken();
            x = tokenizer.nval;
            tokenizer.nextToken();
            y = tokenizer.nval;

            // Проверяем упорядоченность точек с использованием эпсилона
            if (i > 0 && x <= prevX + EPSILON) {
                throw new IOException("Точки не упорядочены по возрастанию X");
            }

            points[i] = new FunctionPoint(x, y);
            prevX = x;
        }
        // Создаем и возвращаем объект табулированной функции
        return new ArrayTabulatedFunction(points);
    }
}
