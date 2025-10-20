import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;

public class Main {

    public static void main(String[] s) throws InappropriateFunctionPointException {
        Function sin = new Sin();
        Function cos = new Cos();
        for (double i = 0; i <= Math.PI; i += 0.1) {
            System.out.println("Sin на отрезке (x = " + i + ") = " + sin.getFunctionValue(i));
            System.out.println("Cos на отрезке (x = " + i + ") = " + cos.getFunctionValue(i));
        }

        System.out.println();
        System.out.println("Табулированные функции");
        System.out.println();

        TabulatedFunction sinTab = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction cosTab = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);
        for (double i = 0; i < Math.PI; i += 0.1) {
            System.out.println("Sin на отрезке (x = " + i + ") = " + sinTab.getFunctionValue(i));
            System.out.println("Cos на отрезке (x = " + i + ") = " + cosTab.getFunctionValue(i));
        }

        System.out.println();
        System.out.println("Сумма квадратов синуса и косинуса(10 точек разбиения)");
        System.out.println();

        Function sum = Functions.sum(Functions.power(sinTab, 2), Functions.power(cosTab, 2));
        for (double i = 0; i <= Math.PI; i += 0.1) {
            System.out.println("Сумма на отрезке (x = " + i + ") = " + sum.getFunctionValue(i));
        }

        System.out.println();
        System.out.println("Сумма квадратов синуса и косинуса(100 точек разбиения)");
        System.out.println();

        TabulatedFunction sinTab1 = TabulatedFunctions.tabulate(sin, 0, Math.PI, 100);
        TabulatedFunction cosTab1 = TabulatedFunctions.tabulate(cos, 0, Math.PI, 100);
        Function sum1 = Functions.sum(Functions.power(sinTab1, 2), Functions.power(cosTab1, 2));
        for (double i = 0; i <= Math.PI; i += 0.1) {
            System.out.println("Сумма на отрезке (x = " + i + ") = " + sum1.getFunctionValue(i));
        }

        System.out.println();
        System.out.println("Экспонента");
        System.out.println();

        TabulatedFunction ex1 = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);
        try (FileWriter out = new FileWriter("exp.txt")) {
            TabulatedFunctions.writeTabulatedFunction(ex1, out);
        } catch (IOException e) {
            System.out.println("Исключение");
        }
        try (FileReader in = new FileReader("exp.txt")) {
            TabulatedFunction ex2 = TabulatedFunctions.readTabulatedFunction(in);
            for (int i = 0; i <= 10; i++) {
                System.out.println("Отезок (x = " + i + ") ex1 = " + ex1.getFunctionValue(i) + " ex2 = " + ex2.getFunctionValue(i));
            }
        } catch (IOException e) {
            System.out.println("Исключение");
        }

        System.out.println();
        System.out.println("Логарифм");
        System.out.println();

        TabulatedFunction log1 = TabulatedFunctions.tabulate(new Log(Math.E), 0, 10, 11);
        try (FileOutputStream out = new FileOutputStream("Log.txt")) {
            TabulatedFunctions.outputTabulatedFunction(log1, out);
        } catch (IOException e) {
            System.out.println("Исключение");
        }
        try (FileInputStream in = new FileInputStream("Log.txt")) {
            TabulatedFunction log2 = TabulatedFunctions.inputTabulatedFunction(in);
            for (int i = 0; i <= 10; i++) {
                System.out.println("Отезок (x = " + i + ") log1 = " + log1.getFunctionValue(i) + " log2 = " + log2.getFunctionValue(i));
            }
        } catch (IOException e) {
            System.out.println("Исключение");
        }

        System.out.println();
        System.out.println("Сериализация");
        System.out.println();

        Function log = new Log(Math.E);
        Function ex = new Exp();
        TabulatedFunction Fun = TabulatedFunctions.tabulate(Functions.composition(log, ex), 0, 10, 11);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("externa.ser"))) {
            out.writeObject(Fun);
        } catch (IOException e) {
            System.out.println("Исключение");
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("externa.ser"))) {
            TabulatedFunction Fun1 = (TabulatedFunction) in.readObject();
            for (int i = 0; i <= 10; i++) {
                System.out.println("Отезок (x = " + i + ") Fun = " + Fun.getFunctionValue(i) + " Fun1 = " + Fun1.getFunctionValue(i));
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Исключение");

        }


    }
}