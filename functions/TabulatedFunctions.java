package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() {}
    
    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) {
        if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder()) {
            throw new IllegalArgumentException();
        }
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = f.getFunctionValue(x);
        }
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }

    public static void writeTabulatedFunction(TabulatedFunction f, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.println(f.getPointsCount());
        for (int i = 0; i < f.getPointsCount(); i++) {
            pw.println(f.getPointX(i) + " " + f.getPointY(i));
        }
        pw.flush();
    }

    public static void outputTabulatedFunction(TabulatedFunction f, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(f.getPointsCount());
        for (int i = 0; i < f.getPointsCount(); i++) {
            dos.writeDouble(f.getPointX(i));
            dos.writeDouble(f.getPointY(i));
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int n = dis.readInt();
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            pts[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(pts);
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        BufferedReader br = new BufferedReader(in);
        int n = Integer.parseInt(br.readLine().trim());
        FunctionPoint[] pts = br.lines()
                .limit(n)
                .map(line -> line.trim().split("\\s+"))
                .map(parts -> new FunctionPoint(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])))
                .toArray(FunctionPoint[]::new);
        return new ArrayTabulatedFunction(pts);
    }
}