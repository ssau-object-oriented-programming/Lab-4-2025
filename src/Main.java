import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws InappropriateFunctionPointException {

        // Создание объектов Sin и Cos
        Function sinFunction = new Sin();
        Function cosFunction = new Cos();

        // Вывод значений Sin и Cos на отрезке от 0 до π
        System.out.println("Вывод значений Sin и Cos на отрезке от 0 до π:");
        printFunctionValues(sinFunction, 0, Math.PI, Math.PI/9);
        printFunctionValues(cosFunction, 0, Math.PI, Math.PI/9);

        // Табулирование Sin и Cos на отрезке от 0 до π с 10 точками
        TabulatedFunction sinTabulated = TabulatedFunctions.tabulate(sinFunction, 0,  Math.PI, 10);
        TabulatedFunction cosTabulated = TabulatedFunctions.tabulate(cosFunction, 0,  Math.PI, 10);

        // Вывод значений табулированных Sin и Cos на отрезке от 0 до π
        System.out.println("Вывод значений табулированных Sin и Cos на отрезке от 0 до π:");
        printTabulatedFunctionValues(sinTabulated, 0,  Math.PI, Math.PI/9);
        printTabulatedFunctionValues(cosTabulated, 0,  Math.PI, Math.PI/9);

        // Создание функции, являющейся суммой квадратов табулированных Sin и Cos
        Function sumOfSquares = Functions.sum(Functions.power(sinTabulated, 2), Functions.power(cosTabulated, 2));

        // Вывод  значений суммы квадратов на отрезке от 0 до π
        System.out.println("Вывод значений суммы квадратов на отрезке от 0 до π:");
        printFunctionValues(sumOfSquares, 0,  Math.PI, Math.PI/9);



        // Создание объекта Exp
        Function expFunction = new Exp();

        // Табулирование экспоненты на отрезке от 0 до 10 с 11 точками
        TabulatedFunction expTabulated = TabulatedFunctions.tabulate(expFunction, 0, 10, 11);

        // Запись табулированной экспоненты в файл

        try {
            FileWriter fileWriter = new FileWriter("exp_tabulated.txt");
            TabulatedFunctions.writeTabulatedFunction(expTabulated, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Чтение табулированной функции из файла
        try {
            FileReader fileReader = new FileReader("exp_tabulated.txt");
            TabulatedFunction readExpTabulated = TabulatedFunctions.readTabulatedFunction(fileReader);
            fileReader.close();

            // Вывод и сравнение значений исходной и считанной функций на отрезке от 0 до 10 с шагом 1
            System.out.println("Вывод и сравнение значений исходной и считанной функций Exp на отрезке от 0 до 10 с шагом 1:");
            System.out.println("Исходная функция:");
            printTabulatedFunctionValues(expTabulated, 0, 10, 1);
            System.out.println("Считанная функция:");
            printTabulatedFunctionValues(readExpTabulated, 0, 10, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Создание объекта Log
        Function logFunction = new Log(Math.E);

        // Табулирование логарифма на отрезке от 0 до 10 с 11 точками
        TabulatedFunction logTabulated = TabulatedFunctions.tabulate(logFunction, 0.1, 10, 11);

        // Запись табулированного логарифма в файл
        try {
            FileOutputStream fileOut = new FileOutputStream("log_tabulated.txt");
            TabulatedFunctions.outputTabulatedFunction(logTabulated, fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Чтение табулированной функции из файла
        try {
            FileInputStream fileIn = new FileInputStream("log_tabulated.txt");
            TabulatedFunction readLogTabulated = TabulatedFunctions.inputTabulatedFunction(fileIn);
            fileIn.close();

            // Вывод и сравнение значений исходной и считанной функций на отрезке от 0 до 10 с шагом 1
            System.out.println("Вывод и сравнение значений исходной и считанной функций Log на отрезке от 0 до 10 с шагом 1:");
            System.out.println("Исходная функция:");
            printTabulatedFunctionValues(logTabulated, 0.1, 10, 1);
            System.out.println("Считанная функция:");
            printTabulatedFunctionValues(readLogTabulated, 0.1, 10, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Табулирование логарифма на отрезке от 0 до 10 с 11 точками
        TabulatedFunction logTabulatedSer = TabulatedFunctions.tabulate(logFunction, 0.1, 10, 11);

        // Сериализация
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("log_tabulated_serializable.txt"))) {
            oos.writeObject(logTabulatedSer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Десериализация
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("log_tabulated_serializable.txt"))) {
            TabulatedFunction readLogTabulated = (TabulatedFunction) ois.readObject();

            // Вывод и сравнение значений исходной и считанной функций на отрезке от 0 до 10 с шагом 1
            System.out.println("Вывод и сравнение значений исходной и считанной функций Log на отрезке от 0 до 10 с шагом 1:");
            System.out.println("Исходная функция:");
            printTabulatedFunctionValues(logTabulatedSer, 0.1, 10, 1);
            System.out.println("Считанная функция:");
            printTabulatedFunctionValues(readLogTabulated, 0.1, 10, 1);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        // Табулирование логарифма на отрезке от 0 до 10 с 11 точками
        TabulatedFunction logTabulatedEx = TabulatedFunctions.tabulate(logFunction, 0.1, 10, 11);

// Сериализация
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("log_tabulated_externalizable.txt"))) {
            oos.writeObject(logTabulatedEx);
        } catch (IOException e) {
            e.printStackTrace();
        }

// Десериализация
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("log_tabulated_externalizable.txt"))) {
            TabulatedFunction readLogTabulated = (TabulatedFunction) ois.readObject();

            // Вывод и сравнение значений исходной и считанной функций на отрезке от 0 до 10 с шагом 1
            System.out.println("Вывод и сравнение значений исходной и считанной функций Log на отрезке от 0 до 10 с шагом 1:");
            System.out.println("Исходная функция:");
            printTabulatedFunctionValues(logTabulatedEx, 0.1, 10, 1);
            System.out.println("Считанная функция:");
            printTabulatedFunctionValues(readLogTabulated, 0.1, 10, 1);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Вспомогательный метод для вывода значений функции на отрезке с заданным шагом
    private static void printFunctionValues(Function function, double from, double to, double step) throws InappropriateFunctionPointException {
        for (double x = from; x <= to; x += step) {
            System.out.println("Function value at x = " + x + ": " + function.getFunctionValue(x));
        }
    }

    // Вспомогательный метод для вывода значений табулированной функции на отрезке с заданным шагом
    private static void printTabulatedFunctionValues(TabulatedFunction tabulatedFunction, double from, double to, double step) throws InappropriateFunctionPointException {
        for (double x = from; x <= to; x += step) {
            System.out.println("Tabulated function value at x = " + x + ": " + tabulatedFunction.getFunctionValue(x));
        }
    }

}