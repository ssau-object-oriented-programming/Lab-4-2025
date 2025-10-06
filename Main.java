import functions.Function;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import functions.basic.Cos;
import functions.basic.Exp;
import functions.basic.Log;
import functions.basic.Sin;
import functions.meta.Power;
import functions.meta.Sum;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        Sin sin = new Sin();
        Cos cos = new Cos();

        display(sin);
        System.out.println();
        display(cos);
        System.out.println();

        System.out.println("=========================");

        TabulatedFunction tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 1000);
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 1000);

        display(tabSin);
        System.out.println();
        display(tabCos);
        System.out.println();

        System.out.println("=========================");

        Sum sum = new Sum(new Power(tabSin, 2), new Power(tabCos, 2));
        display(sum);
        System.out.println();

        System.out.println("=========================");

        Exp exp = new Exp();
        TabulatedFunction tabExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);
        display(tabExp);
        System.out.println();

        TabulatedFunction newTabExp = null;
        try {
            File f = new File("exp.txt");
            FileWriter fw = new FileWriter(f);
            TabulatedFunctions.writeTabulatedFunction(tabExp, fw);
            fw.close();
            FileReader fr = new FileReader(f);
            newTabExp = TabulatedFunctions.readTabulatedFunction(fr);
        } catch (Exception err) { err.printStackTrace(); }
        display(newTabExp);
        System.out.println();

        System.out.println("=========================");

        Log log = new Log(Math.E);
        TabulatedFunction tabLog = TabulatedFunctions.tabulate(log, 0, 10, 11);
        display(tabLog);
        System.out.println();

        TabulatedFunction newTabLog = null;
        try {
            File f = new File("log.txt");
            FileOutputStream fos = new FileOutputStream(f);
            TabulatedFunctions.outputTabulatedFunction(tabLog, fos);
            fos.close();
            FileInputStream fis = new FileInputStream(f);
            newTabLog = TabulatedFunctions.inputTabulatedFunction(fis);
        } catch (Exception err) { err.printStackTrace(); }
        display(newTabLog);
        System.out.println();

        System.out.println("= = = = = = = = = = = = =");

        try {
            File f = new File("logSERIALIZE.txt");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(newTabLog);
            oos.flush();
            fos.flush();
            oos.close();
            fos.flush();

            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            newTabLog = (TabulatedFunction) ois.readObject();
        } catch (Exception err) { err.printStackTrace(); }
        display(newTabLog);
    }
    public static void display(Function f) {
        if (f == null) return;

        double x = 0;
        double maxX = Math.PI;
        double step = 0.1;

        while (x <= maxX) {
            System.out.print(Math.round(f.getFunctionValue(x) * 1000) / 1000d);
            System.out.print(" ");

            x += step;
        }
    }
}
