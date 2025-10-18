import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {

        // Объекты классов Sin и Cos
        Function sinFunction = new Sin();
        Function cosFunction = new Cos();

        // Значения этих функций на отрезке от 0 до π с шагом 0,1
        System.out.println("Sin от 0 до π с шагом 0.1:");
        printFunctionValues(sinFunction, 0, Math.PI, 0.1);

        System.out.println("\nCos от 0 до π с шагом 0.1:");
        printFunctionValues(cosFunction, 0, Math.PI, 0.1);

        // Табулированные аналоги этих функций на отрезке от 0 до π с 10 точками
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sinFunction, 0, Math.PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cosFunction, 0, Math.PI, 10);

        // Значения этих функций на отрезке от 0 до π с шагом 0,1
        System.out.println("\nТабулированный Sin (10 точек) от 0 до π с шагом 0.1:");
        printFunctionValues(tabulatedSin, 0, Math.PI, 0.1);

        System.out.println("\nТабулированный Cos (10 точек) от 0 до π с шагом 0.1:");
        printFunctionValues(tabulatedCos, 0, Math.PI, 0.1);

        // Объект функции, являющейся суммой квадратов табулированных аналогов синуса и косинуса
        Function sinSquared = Functions.power(tabulatedSin, 2);
        Function cosSquared = Functions.power(tabulatedCos, 2);
        Function sumOfSquares = Functions.sum(sinSquared, cosSquared);

        // Значения этой функций на отрезке от 0 до π с шагом 0,1
        System.out.println("\nСумма квадратов табулированных Sin и Cos (10 точек) от 0 до π с шагом 0.1:");
        printFunctionValues(sumOfSquares, 0, Math.PI, 0.1);

        // Тест с 5 точками
        System.out.println(("Тест с 5 точками"));
        TabulatedFunction sin5 = TabulatedFunctions.tabulate(new Sin(), 0, Math.PI, 5);
        TabulatedFunction cos5 = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, 5);
        Function sum5 = Functions.sum(Functions.power(sin5, 2), Functions.power(cos5, 2));
        printFunctionValues(sum5, 0, Math.PI, 0.1);

        // Тест с 20 точками
        System.out.println(("Тест с 20 точками"));
        TabulatedFunction sin20 = TabulatedFunctions.tabulate(new Sin(), 0, Math.PI, 20);
        TabulatedFunction cos20 = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, 20);
        Function sum20 = Functions.sum(Functions.power(sin20, 2), Functions.power(cos20, 2));
        printFunctionValues(sum20, 0, Math.PI, 0.1);

        // Работа с файлами (экспонента)
        TabulatedFunction expTabulated = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);
        // Запись экспоненты
        try (FileWriter writer = new FileWriter("exp_function.txt")) {
            TabulatedFunctions.writeTabulatedFunction(expTabulated, writer);
        }

        // Чтение
        TabulatedFunction readExp;
        try (FileReader reader = new FileReader("exp_function.txt")) {
            readExp = TabulatedFunctions.readTabulatedFunction(reader);
        }

        System.out.println("Сравнение табулированной и считанной из файла экспоненты:");
        compareFunctions(expTabulated, readExp, 0, 10, 1);

        // Работа с файлами (логарифм)
        TabulatedFunction logTabulated = TabulatedFunctions.tabulate(new Log(Math.E), 1, 10, 11);

        // Запись
        try (FileOutputStream out = new FileOutputStream("log_function.bin")) {
            TabulatedFunctions.outputTabulatedFunction(logTabulated, out);
        }

        // Чтение
        TabulatedFunction readLog;
        try (FileInputStream in = new FileInputStream("log_function.bin")) {
            readLog = TabulatedFunctions.inputTabulatedFunction(in);
        }

        System.out.println("Сравнение табулированного и считанного из файла логарифма:");
        compareFunctions(logTabulated, readLog, 1, 10, 1);
        //Композиция: ln(exp(x)) = x
        Function expFunction = new Exp();
        Function logFunction = new Log(Math.E);
        Function composition = Functions.composition(logFunction, expFunction); // ln(exp(x)) = x

        // Табулируем композицию на отрезке от 0 до 10 с 11 точками
        TabulatedFunction tabulatedComposition = TabulatedFunctions.tabulate(composition, 0, 10, 11);

        // Сериализация с использованием Serializable
        System.out.println("\nАвтоматическая сериализация (serializable)");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serializable_function.ser"))) {
            oos.writeObject(tabulatedComposition);
        }

        TabulatedFunction readSerializable;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serializable_function.ser"))) {
            readSerializable = (TabulatedFunction) ois.readObject();
        }

        System.out.println("Сравнение Serializable (ln(exp(x)) = x):");
        compareFunctions(tabulatedComposition, readSerializable, 0, 10, 1);
        // Сериализация с использованием externalizable
        System.out.println("\nРучная сериализация (externalizable)");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("externalizable_function.ser"))) {
            oos.writeObject(tabulatedComposition);
        }

        TabulatedFunction readExternalizable;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("externalizable_function.ser"))) {
            readExternalizable = (TabulatedFunction) ois.readObject();
        }

        System.out.println("Сравнение Externalizable (ln(exp(x)) = x):");
        compareFunctions(tabulatedComposition, readExternalizable, 0, 10, 1);

    }


    private static void printFunctionValues(Function function, double from, double to, double step) {
        for (double x = from; x <= to + 1e-10; x += step) {
            double value = function.getFunctionValue(x);
            if (!Double.isNaN(value)) {
                System.out.printf("x = " + x + " y = " + value + "\n");
            }
        }
    }

    private static void compareFunctions(Function f1, Function f2, double from, double to, double step) {
        for (double x = from; x <= to + 1e-10; x += step) {
            double v1 = f1.getFunctionValue(x);
            double v2 = f2.getFunctionValue(x);
            if (!Double.isNaN(v1) && !Double.isNaN(v2)) {
                System.out.printf("x = " + x + " первая = " + v1 + " вторая = " + v2 + "\n");
            }
        }
    }

    private static void printFileContent(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}