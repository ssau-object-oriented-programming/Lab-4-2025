package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() {}
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX) throw new IllegalArgumentException("leftX must be less than rightX");
        if (leftX < function.getLeftDomainBorder()) throw new IllegalArgumentException("leftX too low");
        if (rightX > function.getRightDomainBorder()) throw new IllegalArgumentException("rightX too high");
        if (pointsCount < 2) throw new IllegalArgumentException("pointsCount must be greater than 1");
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        double x;
        for (int i = 0; i < pointsCount; i++) {
            x = leftX + step * i;
            points[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return new LinkedListTabulatedFunction(points);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out); // оборачиваем поток в нужный нам класс
        int amount = function.getPointsCount();
        dos.writeInt(amount);
        for (int i = 0; i < amount; i++) {
            dos.writeDouble(function.getPointX(i));
            dos.writeDouble(function.getPointY(i));
        }
        dos.flush(); // фиксируем изменения
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in); // оборачиваем поток в нужный нам класс
        int amount = dis.readInt();
        FunctionPoint[] points = new FunctionPoint[amount];
        for (int i = 0; i < amount; i++) {
            points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
        }
        return new LinkedListTabulatedFunction(points); // пока возьмем данную реализацию
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        int amount = function.getPointsCount();
        BufferedWriter bw = new BufferedWriter(out);
        bw.write(String.valueOf(amount));
        bw.write(':');
        for (int i = 0; i < amount; i++) {
            bw.write('(');
            bw.write(String.valueOf(function.getPointX(i)));
            bw.write(',');
            bw.write(String.valueOf(function.getPointY(i)));
            bw.write(')');
            if (i < amount - 1) bw.write(';');
        }
        bw.flush();
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in); // честно, это не облегчает работу...
        st.resetSyntax();

        st.whitespaceChars(':', ':');
        st.whitespaceChars('(','(');
        st.whitespaceChars(')',')');
        st.whitespaceChars(',',',');
        st.whitespaceChars(';',';');

        st.wordChars('0', '9');
        st.wordChars('-', '-');
        st.wordChars('e', 'e');
        st.wordChars('E', 'E');
        st.wordChars('.', '.');

        st.nextToken();
        int amount = Integer.parseInt(st.sval);

        double x, y;
        FunctionPoint[] points = new FunctionPoint[amount];
        for (int i = 0; i < amount; i++) {
            st.nextToken();
            x = Double.parseDouble(st.sval);
            st.nextToken();
            y = Double.parseDouble(st.sval);
            points[i] = new FunctionPoint(x, y);
        }

        return new LinkedListTabulatedFunction(points);
    }
}
