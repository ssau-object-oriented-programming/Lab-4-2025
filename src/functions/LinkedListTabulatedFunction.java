package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {

    private class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev, next;
        private int index;
        private FunctionNode() {
            prev = null;
            next = null;
            index = 0;
        }
    }

    private FunctionNode head;
    private int size;

    //Инициализатор экземпляра, который выполняется при создании объекта класса и инициализирует его поля
    {
        head = new FunctionNode();
        head.prev = head;
        head.point = null;
        head.next = head;
        size = 0;
        head.index = -1;
    }

    // Конструкторы
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {

        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException();
        }

        double interval = (Math.abs(rightX - leftX)) / (pointsCount - 1);

        while (size < pointsCount) {
            head = addNodeToTail();
            head.point = new FunctionPoint(leftX, 0);
            leftX += interval;
        }
        head = head.next;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values){

        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException();
        }

        double interval = (Math.abs(rightX - leftX)) / (values.length - 1);

        for (int i = 0; size < values.length; ++i){
            head = addNodeToTail();
            head.point = new FunctionPoint(leftX, values[i]);
            leftX += interval;
        }
        head = head.next;
    }

    public LinkedListTabulatedFunction(FunctionPoint[] array) {
        if (array.length < 2)
            throw new IllegalArgumentException();

        for (int i = 0; i < array.length - 1; ++i)
            if (array[i].getX() >= array[i + 1].getX())
                throw new IllegalArgumentException();


        for (int i = 0; size < array.length; ++i) {
            head = addNodeToTail();
            head.point = new FunctionPoint(array[i]);
        }
        head = head.next;
    }

    // Метод, возвращающий левую границу области определения
    public double getLeftDomainBorder() throws InappropriateFunctionPointException {

        if (size == 0){
            throw new InappropriateFunctionPointException();
        }
        return getNodeByIndex(0).point.getX();
    }
    // Метод, возвращающий правую границу области определения
    public double getRightDomainBorder() throws InappropriateFunctionPointException {

        if (size == 0){
            throw new InappropriateFunctionPointException();
        }
        return getNodeByIndex(size - 1).point.getX();
    }

    // Метод, возвращающий значение функции в точке x с использованием линейной интерполяции
    public double getFunctionValue(double x) throws InappropriateFunctionPointException {

        if ((size == 0) || ((x < getLeftDomainBorder()) || x > getRightDomainBorder())){
            throw new InappropriateFunctionPointException();
        }

        for (int i = 0; i < size - 1; ++i) {
            if (x >= getNodeByIndex(i).point.getX() && x <= getNodeByIndex(i + 1).point.getX()) {

                double x1 = getNodeByIndex(i).point.getX();
                double y1 =  getNodeByIndex(i).point.getY();
                double x2 =  getNodeByIndex(i + 1).point.getX();
                double y2 =  getNodeByIndex(i + 1).point.getY();

                // Линейная интерполяция
                return (y1 + (x - x1) * (y2 - y1) / (x2 - x1));
            }
        }
        // Если всё-таки не удалось найти точку
        throw new InappropriateFunctionPointException();
    }

    // Метод, возвращающий количество точек
    public int getPointsCount() { return size; }

    // Метод, возвращающий ссылку на объект, описывающий точку, имеющую указанный номер
    public FunctionPoint getPoint(int index) {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            // Возвращаем копию точки, чтобы предотвратить несанкционированный доступ к точке
            return new FunctionPoint(getNodeByIndex(index).point);
        }
    }

    // Метод, заменяющий точку по индексу на заданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            double x = point.getX();
            for (int i = 0; i < size; ++i){
                if(getPointX(i) == x){
                    throw new InappropriateFunctionPointException();
                }
            }
            if (size > 1)
                // Обрабатываем отдельно первый элемент
                if (index == 0 && x <= getNodeByIndex(1).point.getX())
                    getNodeByIndex(0).point = point;
                    // Обрабатываем отдельно последний элемент
                else if (index == size - 1 && x >= getNodeByIndex(size - 2).point.getX())
                    getNodeByIndex(size - 1).point = point;
                    // Обрабатываем остальные случаи
                else if (x >= getNodeByIndex(index - 1).point.getX() && x <= getNodeByIndex(index + 1).point.getX())
                    getNodeByIndex(index).point = point;
                else {
                    throw new InappropriateFunctionPointException();
                }
        }
    }

    // Метод, возвращающий абсциссу точки по индексу
    public double getPointX(int index) { return getPoint(index).getX(); }

    // Метод, изменяющий абсциссу точки по индексу
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            for (int i = 0; i < size; ++i){
                if(getPointX(i) == x){
                    throw new InappropriateFunctionPointException();
                }
            }
            if (size > 1)
                if (index == 0 && x <= getNodeByIndex(1).point.getX())
                    getNodeByIndex(0).point.setX(x);
                else if (index == size - 1 && x >= getNodeByIndex(size - 2).point.getX())
                    getNodeByIndex(size - 1).point.setX(x);
                else if (x >= getNodeByIndex(index - 1).point.getX() && x <= getNodeByIndex(index + 1).point.getX())
                    getNodeByIndex(index).point.setX(x);
                else {
                    throw new InappropriateFunctionPointException();
                }
        }
    }

    // Метод, возвращающий ординату точки по индексу
    public double getPointY(int index) { return getPoint(index).getY(); }

    // Метод, изменяющий ординату точки по индексу
    public void setPointY(int index, double y) {

        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            getNodeByIndex(index).point.setY(y);
        }
    }

    // Метод, удаляющий точку по индексу
    public void deletePoint(int index) {

        if (size < 3) {
            throw new IllegalStateException();
        }
        else if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        else {
            deleteNodeByIndex(index);
        }
    }

    // Метод, добавляющий новую точку
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{

        double x = point.getX();

        for (int i = 0; i < size; ++i){
            if(getPointX(i) == x){
                throw new InappropriateFunctionPointException();
            }
        }

        if(x < getLeftDomainBorder()){
            addNodeByIndex(0).point = point;
        }
        else if(x > getRightDomainBorder()){
            addNodeToTail().point = point;
        }
        else {
            FunctionNode current = new FunctionNode();
            current = head.next;
            while (x > current.point.getX()) {
                current = current.next;
            }
            addNodeByIndex(current.index).point = point;
        }
    }

    //возвращает ссылку на объект элемента списка по его номеру
    public FunctionNode getNodeByIndex(int index) {

        if (size > 0 && index >= 0) {
            FunctionNode current = new FunctionNode();
            current = head.next;
            if (index - current.index <= size - index)
                while (current.index != index)
                    current = current.next;
            else
                while (current.index != index)
                    current = current.prev;
            return current;
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    //добавляет новый элемент в конец списка и возвращает ссылку на объект этого элемента
    public FunctionNode addNodeToTail() {

        if (size > 0){
            head = getNodeByIndex(size - 1);
        }
        FunctionNode temp = new FunctionNode();
        temp.next = head.next;
        head.next.prev = temp;
        head.next = temp;
        temp.prev = head;
        temp.index = size;

        ++size;

        head = head.next.next;

        return temp;
    }

    //добавляет новый элемент в указанную позицию списка и возвращает ссылку на объект этого элемента
    public FunctionNode addNodeByIndex(int index) {

        FunctionNode current = new FunctionNode();
        current = getNodeByIndex(index);
        FunctionNode temp = new FunctionNode();
        temp.index = current.index;
        current.prev.next = temp;
        temp.prev = current.prev;
        current.prev = temp;
        temp.next = current;

        ++size;

        while (current.index != -1) {
            ++current.index;
            current = current.next;
        }

        return temp;
    }

    //удаляет элемент списка по номеру и возвращает ссылку на объект удаленного элемента
    public FunctionNode deleteNodeByIndex(int index) {

        FunctionNode current = new FunctionNode();
        current = getNodeByIndex(index);
        current.prev.next = current.next;
        current.next.prev = current.prev;

        --size;

        while (current.index != -1) {
            --current.index;
            current = current.next;
        }

        return current;
    }

    //Метод вывода
    public void printTabulatedFunction() {

        for (int i = 0; i < size; i++) {
            double x = getPointX(i);
            double y = getPointY(i);
            System.out.println("x = " + x + ", y = " + y);
        }
    }

}
