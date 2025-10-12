import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;


public class Main {
    public static void main(String[] args) {
        //создаем объекты функций
        Sin sinFunction = new Sin();
        Cos cosFunction = new Cos();

        System.out.println("Функция Sin определена на отрезке ["+sinFunction.getLeftDomainBorder()+", "+sinFunction.getRightDomainBorder()+"]");
        System.out.println("Функция Cos определена на отрезке ["+cosFunction.getLeftDomainBorder()+", "+cosFunction.getRightDomainBorder()+"]");

        System.out.println();

        //выводим значения с шагом 0.1 на отрезке [0, π]
        System.out.println("Значения функций из 10 точек на отрезке [0, π] с шагом 0.1:");
        System.out.println("x\t\tSin(x)\t\tCos(x)");
        System.out.println("--------------------------------------------");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            double sinValue = sinFunction.getFunctionValue(x);
            double cosValue = cosFunction.getFunctionValue(x);
            System.out.println(x+"\t\t"+sinValue+"\t\t"+cosValue);
        }

        System.out.println();

        TabulatedFunction sinTabulated = TabulatedFunctions.tabulate(sinFunction, 0, Math.PI, 10);
        TabulatedFunction cosTabulated = TabulatedFunctions.tabulate(cosFunction, 0, Math.PI, 10);

        System.out.println();

        //выводим значения с шагом 0.1 на отрезке [0, π]
        System.out.println("Значения табулированных функций из 10 точек на отрезке [0, π] с шагом 0.1:");
        System.out.println("x\t\tSin(x)\t\tCos(x)");
        System.out.println("--------------------------------------------");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            double sinValue = sinTabulated.getFunctionValue(x);
            double cosValue = cosTabulated.getFunctionValue(x);
            System.out.println(x+"\t\t"+sinValue+"\t\t"+cosValue);
        }

        System.out.println();

        Sum sumSquares = new Sum(new Power(sinTabulated, 2), new Power(cosTabulated, 2));
        System.out.println("Значения суммы квадратов функций из 10 точек на отрезке [0, π] с шагом 0.1:");
        System.out.println("x\t\t\t\ty");
        System.out.println("--------------------------------------------");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.println(x+"\t\t\t\t"+ sumSquares.getFunctionValue(x));
        }

        System.out.println();

        Exp exp = new Exp();
        TabulatedFunction expTabulated = TabulatedFunctions.tabulate(exp, 0, 10, 11);
        System.out.println("Значения табулированной функции экспоненты:");
        printTabulatedFunction(expTabulated);

        System.out.println();

        try {
            File textFile = new File("exp.txt");
            FileWriter fw = new FileWriter(textFile);
            TabulatedFunctions.writeTabulatedFunction(expTabulated, fw);
            fw.close();
            FileReader fr = new FileReader(textFile);
            TabulatedFunction newExpTabulated = TabulatedFunctions.readTabulatedFunction(fr);
            System.out.println("Значения табулированной функции экспоненты после записи и чтения из файла:");
            printTabulatedFunction(newExpTabulated);
        } catch (IOException ioe) {System.out.println("Ошибка: " + ioe);}

        System.out.println();

        Log log = new Log(Math.E);
        TabulatedFunction logTabulated = TabulatedFunctions.tabulate(log, 0, 10, 11);
        System.out.println("Значения табулированной функции логарифма:");
        printTabulatedFunction(logTabulated);

        System.out.println();

        try {
            File textFile = new File("log.txt");
            FileOutputStream fos = new FileOutputStream(textFile);
            TabulatedFunctions.outputTabulatedFunction(logTabulated, fos);
            fos.close();
            FileInputStream fis = new FileInputStream(textFile);
            TabulatedFunction newLogTabulated = TabulatedFunctions.inputTabulatedFunction(fis);
            System.out.println("Значения табулированной функции логарифма после записи и чтения из файла:");
            printTabulatedFunction(newLogTabulated);
        } catch (IOException ioe) {System.out.println("Ошибка: " + ioe);}

      /*  try {
            Function composition = new functions.meta.Composition(new Log(Math.E), new Exp());
            TabulatedFunction compositionTabulated = TabulatedFunctions.tabulate(composition, 0, 10, 11);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ln1.bin"));
            oos.writeObject(compositionTabulated);
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ln1.bin"));
            TabulatedFunction newCompositionTabulated = (TabulatedFunction) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Ошибка сериализации: "+e );
        }*/
        System.out.println("\n--- ArrayTabulatedFunction ---");
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, 5);
        try {
            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("array_func.bin"));
            oos1.writeObject(arrayFunc);
            oos1.close();

            ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("array_func.bin"));
            TabulatedFunction loadedArrayFunc = (TabulatedFunction) ois1.readObject();
            ois1.close();

            System.out.println("ArrayTabulatedFunction сериализация успешна");
            printTabulatedFunction(loadedArrayFunc);
        } catch (Exception e) {
            System.out.println("Ошибка ArrayTabulatedFunction: " + e);
        }
    }
    private static void printTabulatedFunction(TabulatedFunction function) {
        System.out.println("x\t\t\t\ty");
        System.out.println("--------------------------------------------");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.println(function.getPointX(i)+"\t\t\t\t"+function.getPointY(i));
        }
    }
}