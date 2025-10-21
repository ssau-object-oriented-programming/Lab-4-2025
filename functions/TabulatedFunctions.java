package functions;

import java.io.*;


public class TabulatedFunctions {
    // Приватный конструктор для запрета создания объектов
    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создать объект класса TabulatedFunctions");
    }

    /**
     * Создает табулированную функцию на основе аналитической функции
     * @param function исходная аналитическая функция
     * @param leftX левая граница табулирования
     * @param rightX правая граница табулирования
     * @param pointsCount количество точек табулирования
     * @return табулированная функция
     * @throws IllegalArgumentException если границы выходят за область определения функции
     */
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException {

        // Проверка корректности границ табулирования
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                    "Границы табулирования [" + leftX + ", " + rightX + "] " +
                            "выходят за область определения функции [" +
                            function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]"
            );
        }

        // Проверка минимального количества точек
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        // Проверка корректности отрезка
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        // Создаем массив значений функции в точках табулирования
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        // Возвращаем табулированную функцию (пока используем ArrayTabulatedFunction)
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }

    /**
     * Выводит табулированную функцию в байтовый поток
     * @param function табулированная функция
     * @param out выходной поток
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);

        // Записываем количество точек
        dataOut.writeInt(function.getPointsCount());

        // Записываем координаты точек
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        // Не закрываем поток, чтобы пользователь мог продолжать с ним работать
        dataOut.flush();
    }

    /**
     * Вводит табулированную функцию из байтового потока
     * @param in входной поток
     * @return табулированная функция
     * @throws IOException если произошла ошибка ввода-вывода или данные некорректны
     */
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        // Читаем количество точек
        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Создаем и возвращаем табулированную функцию
        return new ArrayTabulatedFunction(points);
    }

    /**
     * Записывает табулированную функцию в символьный поток
     * @param function табулированная функция
     * @param out выходной поток
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);

        // Записываем количество точек
        writer.print(function.getPointsCount());
        writer.print(' ');

        // Записываем координаты точек через пробел
        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(function.getPointX(i));
            writer.print(' ');
            writer.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1) {
                writer.print(' ');
            }
        }

        // Не закрываем поток, чтобы пользователь мог продолжать с ним работать
        writer.flush();
    }

    /**
     * Читает табулированную функцию из символьного потока
     * @param in входной поток
     * @return табулированная функция
     * @throws IOException если произошла ошибка ввода-вывода или данные некорректны
     */
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        // Читаем количество точек
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось количество точек");
        }
        int pointsCount = (int) tokenizer.nval;

        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата X точки " + (i + 1));
            }
            double x = tokenizer.nval;

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата Y точки " + (i + 1));
            }
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        // Создаем и возвращаем табулированную функцию
        return new ArrayTabulatedFunction(points);
    }
}