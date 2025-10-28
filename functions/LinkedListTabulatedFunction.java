package functions;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable {
    private static class Node implements Serializable {
        FunctionPoint data;
        Node next;
        Node prev;
    }
    
    private Node head;
    private int pointsCount;
    private static final double EPS = Math.ulp(1.0);
    
    public LinkedListTabulatedFunction() {
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException();
        }
        if (!(rightX > leftX)) {
            throw new IllegalArgumentException();
        }
        this.pointsCount = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);
        head = new Node();
        head.data = new FunctionPoint(leftX, 0);
        Node current = head;
        for (int i = 1; i < pointsCount; i++) {
            Node next = new Node();
            next.data = new FunctionPoint(leftX + i * step, 0);
            current.next = next;
            next.prev = current;
            current = next;
        }
        current.next = head;
        head.prev = current;
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException();
        }
        if (!(rightX > leftX)) {
            throw new IllegalArgumentException();
        }
        this.pointsCount = values.length;
        double step = (rightX - leftX) / (pointsCount - 1);
        head = new Node();
        head.data = new FunctionPoint(leftX, values[0]);
        Node current = head;
        for (int i = 1; i < pointsCount; i++) {
            Node next = new Node();
            next.data = new FunctionPoint(leftX + i * step, values[i]);
            current.next = next;
            next.prev = current;
            current = next;
        }
        current.next = head;
        head.prev = current;
    }

    public LinkedListTabulatedFunction(FunctionPoint[] sourcePoints) {
        if (sourcePoints == null || sourcePoints.length < 2) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < sourcePoints.length; i++) {
            if (sourcePoints[i - 1].getX() >= sourcePoints[i].getX()) {
                throw new IllegalArgumentException();
            }
        }
        this.pointsCount = sourcePoints.length;
        head = new Node();
        head.data = new FunctionPoint(sourcePoints[0]);
        Node current = head;
        for (int i = 1; i < pointsCount; i++) {
            Node next = new Node();
            next.data = new FunctionPoint(sourcePoints[i]);
            current.next = next;
            next.prev = current;
            current = next;
        }
        current.next = head;
        head.prev = current;
    }
    
    private Node getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }
    
    public double getLeftDomainBorder() {
        return head.data.getX();
    }
    
    public double getRightDomainBorder() {
        return head.prev.data.getX();
    }
    
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        Node current = head;
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(x - current.data.getX()) <= EPS) {
                return current.data.getY();
            }
            current = current.next;
        }
        current = head;
        int count = 0;
        while (count < pointsCount - 1) {
            double x1 = current.data.getX();
            double x2 = current.next.data.getX();
            if (x >= x1 && x <= x2) {
                double y1 = current.data.getY();
                double y2 = current.next.data.getY();
                if (Double.isNaN(y1) || Double.isNaN(y2)) {
                    return Double.NaN;
                }
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
            count++;
        }
        return Double.NaN;
    }
    
    public int getPointsCount() {
        return pointsCount;
    }
    
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).data);
    }
    
    public void setPoint(int index, FunctionPoint point) {
        Node node = getNodeByIndex(index);
        double newX = point.getX();
        if (index > 0 && newX - EPS <= node.prev.data.getX()) {
            throw new IllegalArgumentException();
        }
        if (index < pointsCount - 1 && newX + EPS >= node.next.data.getX()) {
            throw new IllegalArgumentException();
        }
        node.data = new FunctionPoint(point);
    }
    
    public double getPointX(int index) {
        return getNodeByIndex(index).data.getX();
    }
    
    public void setPointX(int index, double x) {
        Node node = getNodeByIndex(index);
        if (index > 0 && x - EPS <= node.prev.data.getX()) {
            throw new IllegalArgumentException();
        }
        if (index < pointsCount - 1 && x + EPS >= node.next.data.getX()) {
            throw new IllegalArgumentException();
        }
        node.data.setX(x);
    }
    
    public double getPointY(int index) {
        return getNodeByIndex(index).data.getY();
    }
    
    public void setPointY(int index, double y) {
        getNodeByIndex(index).data.setY(y);
    }
    
    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException();
        }
        Node node = getNodeByIndex(index);
        if (node == head) {
            head = head.next;
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
        pointsCount--;
    }
    
    public void addPoint(FunctionPoint point) {
        Node currentCheck = head;
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(currentCheck.data.getX() - point.getX()) <= EPS) {
                throw new IllegalArgumentException();
            }
            currentCheck = currentCheck.next;
        }
        Node current = head;
        int count = 0;
        while (count < pointsCount && current.data.getX() < point.getX()) {
            current = current.next;
            count++;
        }
        
        Node newNode = new Node();
        newNode.data = new FunctionPoint(point);
        
        if (count == 0) {
            newNode.next = head;
            newNode.prev = head.prev;
            head.prev.next = newNode;
            head.prev = newNode;
            head = newNode;
        } else if (count == pointsCount) {
            newNode.next = head;
            newNode.prev = head.prev;
            head.prev.next = newNode;
            head.prev = newNode;
        } else {
            newNode.next = current;
            newNode.prev = current.prev;
            current.prev.next = newNode;
            current.prev = newNode;
        }
        pointsCount++;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(pointsCount);
        Node current = head;
        for (int i = 0; i < pointsCount; i++) {
            out.writeObject(current.data);
            current = current.next;
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        pointsCount = in.readInt();
        if (pointsCount < 2) {
            throw new IllegalArgumentException();
        }
        head = new Node();
        head.data = (FunctionPoint) in.readObject();
        Node current = head;
        for (int i = 1; i < pointsCount; i++) {
            Node next = new Node();
            next.data = (FunctionPoint) in.readObject();
            current.next = next;
            next.prev = current;
            current = next;
        }
        current.next = head;
        head.prev = current;
    }
}