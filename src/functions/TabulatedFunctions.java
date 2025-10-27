package functions;

import java.io.*;

public class TabulatedFunctions {

    // Приватный конструктор для предотвращения создания объекта
    private TabulatedFunctions() {};

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {

        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException();
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double interval = (Math.abs(rightX - leftX)) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; ++i){
            points[i] = new FunctionPoint((leftX + i * interval), function.getFunctionValue(leftX + i * interval));
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

        // Считываем значения координат точек
        for (int i = 0; i < pointsCount; ++i) {
            points[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
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

        // Считываем значения координат точек
        for(int i = 0; i < pointsCount; ++i){
            tokenizer.nextToken();
            x = tokenizer.nval;
            tokenizer.nextToken();
            y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        // Создаем и возвращаем объект табулированной функции
        return new ArrayTabulatedFunction(points);
    }
}
