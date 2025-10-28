import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.FunctionPoint;
import functions.Functions;
import functions.TabulatedFunctions;
import functions.Function;
import functions.basic.Sin;
import functions.basic.Cos;
import functions.basic.Exp;
import functions.basic.Log;
import java.io.*;

public class Main {
    public static void main(String[] args){
        TabulatedFunction function = new ArrayTabulatedFunction(0, 4, 5);
        
        function.setPointY(0, 0);
        function.setPointY(1, 1);
        function.setPointY(2, 4);
        function.setPointY(3, 9);
        function.setPointY(4, 16);
        
        System.out.println("Функция x^2:");
        System.out.println("Область определения: [" + function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]");
        System.out.println("Количество точек: " + function.getPointsCount());
        
        System.out.println("\nЗначения функции:");
        double[] testPoints = {-1, 0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 5};
        
        for (double x : testPoints) {
            double y = function.getFunctionValue(x);
            if (Double.isNaN(y)) {
                System.out.println("f(" + x + ") = не определено");
            } else {
                System.out.println("f(" + x + ") = " + y);
            }
        }
        
        System.out.println("\nДобавляем точку (1.5, 2.25):");
        function.addPoint(new FunctionPoint(1.5, 2.25));
        System.out.println("Количество точек после добавления: " + function.getPointsCount());
        
        System.out.println("\nУдаляем точку с индексом 2:");
        function.deletePoint(2);
        System.out.println("Количество точек после удаления: " + function.getPointsCount());
        
        System.out.println("\nЗначения функции после изменений:");
        for (double x : testPoints) {
            double y = function.getFunctionValue(x);
            if (Double.isNaN(y)) {
                System.out.println("f(" + x + ") = не определено");
            } else {
                System.out.println("f(" + x + ") = " + y);
            }
        }
        
        System.out.println("\nИнформация о точках:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.println("Точка " + i + ": (" + function.getPointX(i) + ", " + function.getPointY(i) + ")");
        }

        Function s = new Sin();
        Function c = new Cos();
        System.out.println("\nСинус/Косинус 0..π шаг 0.1:");
        for (double x = 0; x <= Math.PI + 1e-9; x += 0.1) {
            System.out.println("x=" + x + ": sin=" + s.getFunctionValue(x) + ", cos=" + c.getFunctionValue(x));
        }

        TabulatedFunction ts = TabulatedFunctions.tabulate(s, 0, Math.PI, 10);
        TabulatedFunction tc = TabulatedFunctions.tabulate(c, 0, Math.PI, 10);
        System.out.println("\nТабулированные sin/cos 0..π шаг 0.1:");
        for (double x = 0; x <= Math.PI + 1e-9; x += 0.1) {
            double ys = ts.getFunctionValue(x);
            double yc = tc.getFunctionValue(x);
            System.out.println("x=" + x + ": tsin=" + ys + ", tcos=" + yc);
        }

        Function sumSquares = Functions.sum(Functions.power(ts, 2), Functions.power(tc, 2));
        System.out.println("\nСумма квадратов tab(sin)^2+tab(cos)^2 0..π шаг 0.1:");
        for (double x = 0; x <= Math.PI + 1e-9; x += 0.1) {
            System.out.println("x=" + x + ": " + sumSquares.getFunctionValue(x));
        }

        Function e = new Exp();
        TabulatedFunction te = TabulatedFunctions.tabulate(e, 0, 10, 11);
        try {
            FileWriter fw = new FileWriter("exp_txt.txt");
            TabulatedFunctions.writeTabulatedFunction(te, fw);
            fw.close();
            FileReader fr = new FileReader("exp_txt.txt");
            TabulatedFunction teIn = TabulatedFunctions.readTabulatedFunction(fr);
            fr.close();
            System.out.println("\nexp 0..10 шаг 1 (текстовый ввод/вывод):");
            for (int i = 0; i <= 10; i++) {
                double x = i;
                System.out.println("x=" + x + ": src=" + te.getFunctionValue(x) + ", in=" + teIn.getFunctionValue(x));
            }
        } catch (IOException ioe) {
            System.out.println("Ошибка ввода-вывода");
        }

        Function ln = new Log();
        TabulatedFunction tln = TabulatedFunctions.tabulate(ln, 0, 10, 11);
        try {
            FileOutputStream fos = new FileOutputStream("log_bin.dat");
            TabulatedFunctions.outputTabulatedFunction(tln, fos);
            fos.close();
            FileInputStream fis = new FileInputStream("log_bin.dat");
            TabulatedFunction tlnIn = TabulatedFunctions.inputTabulatedFunction(fis);
            fis.close();
            System.out.println("\nln 0..10 шаг 1 (бинарный ввод/вывод):");
            for (int i = 0; i <= 10; i++) {
                double x = i;
                System.out.println("x=" + x + ": src=" + tln.getFunctionValue(x) + ", in=" + tlnIn.getFunctionValue(x));
            }
        } catch (IOException ioe) {
            System.out.println("Ошибка ввода-вывода");
        }

        try {
            TabulatedFunction composed = TabulatedFunctions.tabulate(Functions.composition(new Log(), new Exp()), 0, 10, 11);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("obj_ser.bin"));
            oos.writeObject(composed);
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("obj_ser.bin"));
            TabulatedFunction restored = (TabulatedFunction) ois.readObject();
            ois.close();
            System.out.println("\nln(exp(x)) 0..10 шаг 1 (сериализация):");
            for (int i = 0; i <= 10; i++) {
                double x = i;
                System.out.println("x=" + x + ": src=" + composed.getFunctionValue(x) + ", in=" + restored.getFunctionValue(x));
            }
        } catch (Exception e2) {
            System.out.println("Ошибка сериализации");
        }
    }
}
