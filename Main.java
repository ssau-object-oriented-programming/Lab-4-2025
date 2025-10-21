import functions.*;
import functions.basic.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== ТЕСТИРОВАНИЕ БАЗОВЫХ ФУНКЦИЙ ===");
            testBasicFunctions();

            System.out.println("\n=== ТЕСТИРОВАНИЕ ТАБУЛИРОВАНИЯ ===");
            testTabulation();

            System.out.println("\n=== ТЕСТИРОВАНИЕ КОМБИНАЦИЙ ФУНКЦИЙ ===");
            testFunctionCombinations();

            System.out.println("\n=== ТЕСТИРОВАНИЕ ТЕКСТОВОЙ СЕРИАЛИЗАЦИИ ===");
            testTextSerialization();

            System.out.println("\n=== ТЕСТИРОВАНИЕ БИНАРНОЙ СЕРИАЛИЗАЦИИ ===");
            testBinarySerialization();

            // ЗАДАНИЕ 9 - СЕРИАЛИЗАЦИЯ
            System.out.println("\n=== ТЕСТИРОВАНИЕ JAVA СЕРИАЛИЗАЦИИ ===");
            testJavaSerialization();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testBasicFunctions() {
        Function sin = new Sin();
        Function cos = new Cos();

        System.out.println("Синус и косинус на [0, Pi] с шагом 0.1:");
        System.out.println("x\t\tSin(x)\t\tCos(x)");
        System.out.println("----------------------------------------");

        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("%.3f\t\t%.6f\t%.6f%n",
                    x, sin.getFunctionValue(x), cos.getFunctionValue(x));
        }
    }

    private static void testTabulation() {
        Function sin = new Sin();
        Function cos = new Cos();

        // Создаем табулированные аналоги
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);

        System.out.println("Сравнение аналитических и табулированных функций:");
        System.out.println("x\t\tSin(x)\t\tTabSin(x)\tCos(x)\t\tTabCos(x)");
        System.out.println("----------------------------------------------------------------");

        for (double x = 0; x <= Math.PI; x += 0.1) {
            double sinValue = sin.getFunctionValue(x);
            double cosValue = cos.getFunctionValue(x);
            double tabSinValue = tabulatedSin.getFunctionValue(x);
            double tabCosValue = tabulatedCos.getFunctionValue(x);

            System.out.printf("%.3f\t\t%.6f\t%.6f\t%.6f\t%.6f%n",
                    x, sinValue, tabSinValue, cosValue, tabCosValue);
        }
    }

    private static void testFunctionCombinations() {
        System.out.println("Сумма квадратов синуса и косинуса с разным количеством точек:");

        // Тестируем с разным количеством точек
        int[] pointsCounts = {5, 10, 20, 50};

        for (int pointsCount : pointsCounts) {
            System.out.println("\nКоличество точек: " + pointsCount);
            System.out.println("x\t\tSin²(x)+Cos²(x)");
            System.out.println("--------------------------");

            // Создаем табулированные аналоги
            TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(new Sin(), 0, Math.PI, pointsCount);
            TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, pointsCount);

            // Создаем квадраты функций
            Function sinSquared = Functions.power(tabulatedSin, 2);
            Function cosSquared = Functions.power(tabulatedCos, 2);

            // Создаем сумму квадратов
            Function sumOfSquares = Functions.sum(sinSquared, cosSquared);

            // Выводим значения
            for (double x = 0; x <= Math.PI; x += 0.1) {
                double value = sumOfSquares.getFunctionValue(x);
                System.out.printf("%.3f\t\t%.6f%n", x, value);
            }
        }
    }

    private static void testTextSerialization() throws IOException {
        System.out.println("Текстовая сериализация экспоненты:");

        // Создаем табулированную экспоненту
        TabulatedFunction expFunction = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);

        // Записываем в файл
        String textFileName = "exp_function.txt";
        try (FileWriter writer = new FileWriter(textFileName)) {
            TabulatedFunctions.writeTabulatedFunction(expFunction, writer);
        }

        // Читаем из файла
        TabulatedFunction readExpFunction;
        try (FileReader reader = new FileReader(textFileName)) {
            readExpFunction = TabulatedFunctions.readTabulatedFunction(reader);
        }

        // Сравниваем значения
        System.out.println("x\t\tИсходная\tПрочитанная");
        System.out.println("------------------------------------");
        for (double x = 0; x <= 10; x += 1.0) {
            double original = expFunction.getFunctionValue(x);
            double read = readExpFunction.getFunctionValue(x);
            System.out.printf("%.1f\t\t%.6f\t%.6f%n", x, original, read);
        }

        // Показываем содержимое файла
        System.out.println("\nСодержимое файла " + textFileName + ":");
        try (BufferedReader br = new BufferedReader(new FileReader(textFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private static void testBinarySerialization() throws IOException {
        System.out.println("Бинарная сериализация логарифма:");

        // Создаем табулированный логарифм (натуральный)
        TabulatedFunction logFunction = TabulatedFunctions.tabulate(new Log(Math.E), 1, 10, 10);

        // Записываем в файл
        String binaryFileName = "log_function.bin";
        try (FileOutputStream fos = new FileOutputStream(binaryFileName)) {
            TabulatedFunctions.outputTabulatedFunction(logFunction, fos);
        }

        // Читаем из файла
        TabulatedFunction readLogFunction;
        try (FileInputStream fis = new FileInputStream(binaryFileName)) {
            readLogFunction = TabulatedFunctions.inputTabulatedFunction(fis);
        }

        // Сравниваем значения
        System.out.println("x\t\tИсходная\tПрочитанная");
        System.out.println("------------------------------------");
        for (double x = 1; x <= 10; x += 1.0) {
            double original = logFunction.getFunctionValue(x);
            double read = readLogFunction.getFunctionValue(x);
            System.out.printf("%.1f\t\t%.6f\t%.6f%n", x, original, read);
        }

        // Показываем размер файла
        File binaryFile = new File(binaryFileName);
        System.out.println("\nРазмер бинарного файла: " + binaryFile.length() + " байт");
    }
    private static void testJavaSerialization() throws IOException, ClassNotFoundException {
        System.out.println("Java сериализация функции ln(exp(x)):");

        // Создаем функцию: ln(exp(x)) = x
        Function composition = Functions.composition(new Log(Math.E), new Exp());
        TabulatedFunction originalFunction = TabulatedFunctions.tabulate(composition, 0, 10, 11);

        System.out.println("Исходная функция:");
        printFunctionValues(originalFunction, "Исходная");

        // Тестируем Serializable
        testSerializable(originalFunction, "serializable_ln_exp.ser");

        // Тестируем Externalizable
        testExternalizable(originalFunction, "externalizable_ln_exp.ser");

        // Анализ файлов
        analyzeSerializationFiles();
    }

    private static void testSerializable(TabulatedFunction function, String filename)
            throws IOException, ClassNotFoundException {
        System.out.println("\n--- Serializable сериализация ---");

        // Сериализация
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(function);
        }

        // Десериализация
        TabulatedFunction deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            deserialized = (TabulatedFunction) ois.readObject();
        }

        System.out.println("После десериализации:");
        printFunctionValues(deserialized, "Serializable");

        // Проверка идентичности
        checkFunctionsEquality(function, deserialized, "Serializable");

        // Размер файла
        File file = new File(filename);
        System.out.println("Размер файла: " + file.length() + " байт");
    }

    private static void testExternalizable(TabulatedFunction originalFunction, String filename)
            throws IOException, ClassNotFoundException {
        System.out.println("\n--- Externalizable сериализация ---");

        // Создаем Externalizable версию
        ArrayTabulatedFunctionExternalizable externalizableFunction =
                convertToExternalizable(originalFunction);

        // Сериализация
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(externalizableFunction);
        }

        // Десериализация
        ArrayTabulatedFunctionExternalizable deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            deserialized = (ArrayTabulatedFunctionExternalizable) ois.readObject();
        }

        System.out.println("После десериализации:");
        printFunctionValues(deserialized, "Externalizable");

        // Проверка идентичности
        checkFunctionsEquality(originalFunction, deserialized, "Externalizable");

        // Размер файла
        File file = new File(filename);
        System.out.println("Размер файла: " + file.length() + " байт");
    }

    private static ArrayTabulatedFunctionExternalizable convertToExternalizable(
            TabulatedFunction function) {
        FunctionPoint[] points = new FunctionPoint[function.getPointsCount()];
        for (int i = 0; i < function.getPointsCount(); i++) {
            points[i] = function.getPoint(i);
        }
        return new ArrayTabulatedFunctionExternalizable(points);
    }

    private static void printFunctionValues(TabulatedFunction function, String label) {
        System.out.println("x\t" + label + " значение");
        System.out.println("----------------------");
        for (double x = 0; x <= 10; x += 1.0) {
            double value = function.getFunctionValue(x);
            System.out.printf("%.1f\t%.6f%n", x, value);
        }
    }

    private static void checkFunctionsEquality(TabulatedFunction f1, TabulatedFunction f2, String type) {
        boolean identical = true;
        for (double x = 0; x <= 10; x += 1.0) {
            double v1 = f1.getFunctionValue(x);
            double v2 = f2.getFunctionValue(x);
            if (Math.abs(v1 - v2) > 1e-10) {
                identical = false;
                break;
            }
        }
        if (identical) {
            System.out.println(type + ": функции идентичны!");
        } else {
            System.out.println(type + ": обнаружены расхождения!");
        }
    }

    private static void analyzeSerializationFiles() {
        System.out.println("\n--- СРАВНЕНИЕ ФАЙЛОВ СЕРИАЛИЗАЦИИ ---");

        File serializableFile = new File("serializable_ln_exp.ser");
        File externalizableFile = new File("externalizable_ln_exp.ser");

        System.out.println("Serializable размер: " + serializableFile.length() + " байт");
        System.out.println("Externalizable размер: " + externalizableFile.length() + " байт");

        long difference = serializableFile.length() - externalizableFile.length();
        double percentage = (double) difference / serializableFile.length() * 100;
        System.out.printf("Разница: %d байт (%.1f%%)%n", difference, percentage);

        // Анализ содержимого
        System.out.println("\nАнализ содержимого файлов:");
        analyzeFileContent(serializableFile, "Serializable");
        analyzeFileContent(externalizableFile, "Externalizable");

        System.out.println("\n--- ВЫВОДЫ ---");
        System.out.println("Serializable:");
        System.out.println("  + Простая реализация (implements Serializable)");
        System.out.println("  - Больший размер файла (метаданные классов)");
        System.out.println("  - Медленнее из-за рефлексии");

        System.out.println("Externalizable:");
        System.out.println("  + Минимальный размер файла");
        System.out.println("  + Высокая производительность");
        System.out.println("  + Полный контроль над процессом");
        System.out.println("  - Сложная реализация (нужно реализовать методы)");
        System.out.println("  - Требуется конструктор по умолчанию");
    }

    private static void analyzeFileContent(File file, String type) {
        System.out.println("\n" + type + " файл (" + file.length() + " байт):");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[Math.min(50, (int)file.length())];
            int bytesRead = fis.read(buffer);

            System.out.print("Hex: ");
            for (int i = 0; i < bytesRead; i++) {
                System.out.printf("%02x ", buffer[i]);
                if ((i + 1) % 16 == 0) System.out.println();
            }
            System.out.println();

            // Анализ заголовка
            if (bytesRead >= 2) {
                System.out.printf("Заголовок: %02x %02x", buffer[0], buffer[1]);
                if (buffer[0] == (byte)0xAC && buffer[1] == (byte)0xED) {
                    System.out.println(" (Java Serialization Stream)");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}
