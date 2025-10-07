package functions;

import java.io.*;

public class TabulatedFunctions {
    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) {
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = f.getFunctionValue(x);
        }
        return new TabulatedFunction(leftX, rightX, values);
    }

    public static void outputTabulatedFunction(TabulatedFunction f, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(f.getPointsCount());
        for (int i = 0; i < f.getPointsCount(); i++) {
            dos.writeDouble(f.getPointX(i));
            dos.writeDouble(f.getPointY(i));
        }
        dos.flush();
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
        return new TabulatedFunction(pts);
    }

    public static void writeTabulatedFunction(TabulatedFunction f, Writer out) throws IOException {
        BufferedWriter bw = new BufferedWriter(out);
        bw.write(Integer.toString(f.getPointsCount()));
        bw.newLine();
        for (int i = 0; i < f.getPointsCount(); i++) {
            bw.write(f.getPointX(i) + " " + f.getPointY(i));
            bw.newLine();
        }
        bw.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.parseNumbers();
        st.nextToken();
        int n = (int) st.nval;
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            pts[i] = new FunctionPoint(x, y);
        }
        return new TabulatedFunction(pts);
    }
}


