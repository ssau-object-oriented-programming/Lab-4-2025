import functions.TabulatedFunction;
import functions.FunctionPoint;

public class Main {
    public static void main(String[] args) {
        TabulatedFunction function = new TabulatedFunction(0, 4, 5);
        
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
    }
}
