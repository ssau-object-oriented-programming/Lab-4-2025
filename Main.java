import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Тестирование всего и вся\n");

        testBasicFunctions();
        testTabulatedFunctions();
        testMetaFunctions();
        testFileOperations();
        testSerialization();
        testExternalizable();

    }

    /** Тестирование базовых функций (пакет basic)
     */
    private static void testBasicFunctions() {
        System.out.println("1. Тест базовых функций");

        // Тест экспоненты
        Exp exp = new Exp();
        System.out.println("Экспонента:");
        System.out.printf("Область определения: [%.1f, %.1f]%n", exp.getLeftDomainBorder(), exp.getRightDomainBorder());
        for (double x = -1; x <= 1; x += 0.5) {
            System.out.printf("exp(%.1f) = %.3f%n", x, exp.getFunctionValue(x));
        }

        // Тест логарифма
        Log log = new Log(10); // Десятичный логарифм
        System.out.println("\nЛогарифм по основанию 10:");
        System.out.printf("Область определения: [%.1f, %.1f]%n", log.getLeftDomainBorder(), log.getRightDomainBorder());
        for (double x = 0.1; x <= 10; x *= 10) {
            System.out.printf("log10(%.1f) = %.3f%n", x, log.getFunctionValue(x));
        }

        // Тест тригонометрических функций
        Sin sin = new Sin();
        Cos cos = new Cos();

        System.out.println("\nТригонометрические функции на [0, π] с шагом 0.1:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("x=%.1f: sin=%.6f, cos=%.6f%n", x, sin.getFunctionValue(x), cos.getFunctionValue(x));
        }
        System.out.println();
    }

    /** Тестирование табулированной функции (2x+1)
     */
    private static void testTabulatedFunctions() {
        System.out.println("2. Тест табулированной функции");

        // Тест ArrayTabulatedFunction
        double[] values = {1, 3, 5, 7, 9, 11};
        ArrayTabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 5, values);

        System.out.println("ArrayTabulatedFunction (2x+1):");
        arrayFunc.printTabulatedFunction();

        // Тест интерполяции
        System.out.println("\nИнтерполяция ArrayTabulatedFunction:");
        for (double x = 0.5; x <= 4.5; x += 1) {
            System.out.printf("f(%.1f) = %.3f%n", x, arrayFunc.getFunctionValue(x));
        }

        // Тест LinkedListTabulatedFunction
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 5, values);

        System.out.println("\nLinkedListTabulatedFunction (2x+1):");
        listFunc.printTabulatedFunction();

        // Тест модификаций
        try {
            System.out.println("\nМодификация точек:");
            listFunc.setPointY(2, 10); // Меняем значение в точке x=2
            listFunc.addPoint(new FunctionPoint(2.5, 6.25));
            listFunc.deletePoint(1);
            listFunc.printTabulatedFunction();
        } catch (Exception e) {
            System.out.println("Ошибка при модификации: " + e.getMessage());
        }
        System.out.println();
    }

    /** Тестирование мета-функций (пакет meta)
     */
    private static void testMetaFunctions() {
        System.out.println("3. Тест мета-функций");

        Sin sin = new Sin();
        Cos cos = new Cos();

        // Сравнение оригинальных и табулированных функций
        System.out.println("Сравнение оригинальных и табулированных функций:");

        // Создаем табулированные аналоги с 10 точками
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);

        System.out.println("\nСравнение sin(x) и его табулированного аналога:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            double originalSin = sin.getFunctionValue(x);
            double tabulatedSinVal = tabulatedSin.getFunctionValue(x);
            System.out.printf("x=%.1f: orig=%.6f, tab=%.6f%n", x, originalSin, tabulatedSinVal);
        }

        System.out.println("\nСравнение cos(x) и его табулированного аналога:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            double originalCos = cos.getFunctionValue(x);
            double tabulatedCosVal = tabulatedCos.getFunctionValue(x);
            System.out.printf("x=%.1f: orig=%.6f, tab=%.6f%n", x, originalCos, tabulatedCosVal);
        }

        // Сумма квадратов табулированных аналогов
        System.out.println("\nСумма квадратов табулированных син и кос:");
        Function sumOfSquares = Functions.sum(Functions.power(tabulatedSin, 2), Functions.power(tabulatedCos, 2));

        System.out.println("sin²(x) + cos²(x) (табулированные, 10 точек):");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("x=%.1f: result=%.10f%n", x, sumOfSquares.getFunctionValue(x));
        }

        // Остальные тесты мета-функций
        Function scaledSin = new Scale(sin, 2, 3); // sin(2x) * 3
        System.out.println("\n3 * sin(2x):");
        for (double x = 0; x <= Math.PI; x += Math.PI/4) {
            System.out.printf("x=%.3f: result=%.3f%n", x, scaledSin.getFunctionValue(x));
        }

        Function shiftedCos = new Shift(cos, Math.PI/2, 1); // cos(x - π/2) + 1
        System.out.println("\ncos(x - π/2) + 1:");
        for (double x = 0; x <= Math.PI; x += Math.PI/4) {
            System.out.printf("x=%.3f: result=%.3f%n", x, shiftedCos.getFunctionValue(x));
        }

        Function mult = new Mult(sin, cos); // sin(x) * cos(x)
        System.out.println("\nsin(x) * cos(x):");
        for (double x = 0; x <= Math.PI; x += Math.PI/4) {
            System.out.printf("x=%.3f: result=%.3f%n", x, mult.getFunctionValue(x));
        }
        System.out.println();
    }

    /** Тестирование операций с файлами (текстовые, бинарные)
     */
    private static void testFileOperations() {
        System.out.println("4. Тест файловых операций");

        try {
            System.out.println("Экспонента - текстовый формат:");
            Exp exp = new Exp();
            TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);

            // Текстовый формат
            String textFile = "exp_text.txt";
            try (FileWriter writer = new FileWriter(textFile)) {
                TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
            }

            // Текстовое чтение
            TabulatedFunction readTextExp;
            try (FileReader reader = new FileReader(textFile)) {
                readTextExp = TabulatedFunctions.readTabulatedFunction(reader);
            }

            System.out.println("Экспонента (0 до 10, шаг 1):");
            System.out.println("Оригинал vs Прочитанный (текстовый формат):");
            for (double x = 0; x <= 10; x += 1) {
                System.out.printf("x=%.1f: orig=%.6f, read=%.6f%n", x, tabulatedExp.getFunctionValue(x), readTextExp.getFunctionValue(x));
            }

            System.out.println("\nЛогарифм - бинарный формат:");
            Log log = new Log(Math.E);
            TabulatedFunction tabulatedLog = TabulatedFunctions.tabulate(log, 0.1, 10, 11);

            // Бинарная запись
            String binFile = "log_binary.bin";
            try (FileOutputStream fos = new FileOutputStream(binFile)) {
                TabulatedFunctions.outputTabulatedFunction(tabulatedLog, fos);
            }

            // Бинарное чтение
            TabulatedFunction readBinLog;
            try (FileInputStream fis = new FileInputStream(binFile)) {
                readBinLog = TabulatedFunctions.inputTabulatedFunction(fis);
            }

            System.out.println("Логарифм (0.1 до 10, шаг 1):");
            System.out.println("Оригинал vs Прочитанный (бинарный формат):");
            for (double x = 0.1; x <= 10; x += 1) {
                if (x < 0.1) continue;
                System.out.printf("x=%.1f: orig=%.6f, read=%.6f%n", x, tabulatedLog.getFunctionValue(x), readBinLog.getFunctionValue(x));
            }

        } catch (IOException e) {
            System.out.println("Ошибка файловых операций: " + e.getMessage());
        }
        System.out.println();
    }

    /** Тестирование Serializable
     */
    private static void testSerialization() {
        System.out.println("5. Тест сериализации (Serializable)");

        try {
            Exp exp = new Exp();
            Log log = new Log(Math.E);
            Function composition = Functions.composition(exp, log);
            TabulatedFunction original = TabulatedFunctions.tabulate(composition, 0, 10, 11);

            System.out.println("Оригинальная функция ln(exp(x)) = x (0 до 10, 11 точек):");
            printFunctionTable(original, 0, 10, 1);

            // Сериализация
            String serFile = "serializable.ser";
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile))) {
                oos.writeObject(original);
            }

            // Десериализация
            TabulatedFunction deserialized;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile))) {
                deserialized = (TabulatedFunction) ois.readObject();
            }

            System.out.println("Десериализованная функция:");
            printFunctionTable(deserialized, 0, 10, 1);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка сериализации: " + e.getMessage());
        }
        System.out.println();
    }

    /** Тестирование Externalizable
     */
    private static void testExternalizable() {
        System.out.println("6. Тест сериализации (Externalizable)");

        try {
            // функция для сравнения
            Exp exp = new Exp();
            Log log = new Log(Math.E);
            Function composition = Functions.composition(exp, log);
            TabulatedFunction tabulated = TabulatedFunctions.tabulate(composition, 0, 10, 11);

            // Создаем Externalizable версию
            ArrayTabulatedFunction original = new ArrayTabulatedFunction(0, 10, 11);
            for (int i = 0; i < tabulated.getPointsCount(); i++) {
                original.setPointY(i, tabulated.getPointY(i));
            }

            System.out.println("Оригинальная функция (Externalizable):");
            printFunctionTable(original, 0, 10, 1);

            // Сериализация
            String extFile = "externalizable.ser";
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(extFile))) {
                oos.writeObject(original);
            }

            // Десериализация
            ArrayTabulatedFunction deserialized;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(extFile))) {
                deserialized = (ArrayTabulatedFunction) ois.readObject();
            }

            System.out.println("Десериализованная функция (Externalizable):");
            printFunctionTable(deserialized, 0, 10, 1);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка Externalizable: " + e.getMessage());
        }
        System.out.println();
    }

    /** Вспомогательный метод для красивого вывода значений функции
     * @param func - функция, значения которой мы хотим вывести
     * @param start - начальное значение x
     * @param end - конечное значение x
     * @param step - шаг изменения x
     *
     */
    private static void printFunctionTable(TabulatedFunction func, double start, double end, double step) {
        for (double x = start; x <= end; x += step) {
            System.out.printf("f(%.1f) = %.6f%n", x, func.getFunctionValue(x));
        }
    }
}