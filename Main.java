import functions.*;
import functions.basic.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Function f1 = new Cos();
        Function f2 = new Sin();
        f1 = TabulatedFunctions.tabulate(f1, 0, Math.PI, 10);
        f2 = TabulatedFunctions.tabulate(f2, 0, Math.PI, 10);
        for (double i = 0; i < Math.PI; i += 0.1)
            System.out.printf("cos %.2f = %.2f\n", i, f1.getFunctionValue(i));
        System.out.println();
        for (double i = 0; i < Math.PI; i += 0.1)
            System.out.printf("sin %.2f = %.2f\n", i, f2.getFunctionValue(i));
        System.out.println();
        Function f3 = Functions.sum(Functions.power(f1, 2), Functions.power(f2, 2));
        for (double i = 0; i < Math.PI; i += 0.1)
            System.out.printf("sin^2 %.2f + cos^2 %.2f = %.2f\n", i, i, f3.getFunctionValue(i));
        System.out.println();
        TabulatedFunction f4 = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);
        TabulatedFunction f5 = null;
        try {
            TabulatedFunctions.writeTabulatedFunction(f4, new FileWriter("Exp.txt"));
            f5 = TabulatedFunctions.readTabulatedFunction(new FileReader("Exp.txt"));
        } catch (IOException e) {
            System.out.println("IOException");
        }
        for (int i = 0; i < 11; ++i)
            System.out.printf("x = %d:\t\t%.2f\t\t%.2f\n", i, f4.getFunctionValue(i), f5.getFunctionValue(i));
        System.out.println();
        TabulatedFunction f6 = TabulatedFunctions.tabulate(new Log(Math.E), 0, 10, 11);
        TabulatedFunction f7 = null;
        try {
            TabulatedFunctions.outputTabulatedFunction(f6, new FileOutputStream("Log.txt"));
            f7 = TabulatedFunctions.inputTabulatedFunction(new FileInputStream("Log.txt"));
        } catch (IOException e) {
            System.out.println("IOException");
        }
        for (int i = 0; i < 11; ++i)
            System.out.printf("x = %d:\t\t%.2f\t\t%.2f\n", i, f6.getFunctionValue(i), f7.getFunctionValue(i));
        System.out.println();
        TabulatedFunction f8 = TabulatedFunctions.tabulate(Functions.composition(new Exp(), new Log(Math.E)), 0, 10, 11);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Serializable.ser"))) {
            out.writeObject(f8);
        } catch(IOException e) {
            System.out.println("IOException");
        }
        TabulatedFunction f9 = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("Serializable.ser"))) {
            f9 = (TabulatedFunction) in.readObject();
        } catch(IOException e) {
            System.out.println("IOException");
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
        }
        for (int i = 0; i < 11; ++i)
            System.out.printf("x = %d:\t\t%.2f\t\t%.2f\n", i, f8.getFunctionValue(i), f9.getFunctionValue(i));
    }
}