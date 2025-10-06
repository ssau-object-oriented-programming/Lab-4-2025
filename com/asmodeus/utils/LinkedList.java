package com.asmodeus.utils;

import java.io.*;

public class LinkedList<T> implements Serializable {
    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = 0;
    public void addToEnd(T data) {
        if (head == null) {
            head = new Node<>(data);
            tail = head;
            this.size++;
            return;
        }
        this.tail.setNext(new Node<>(data));
        Node<T> temp = this.tail;
        this.tail = this.tail.getNext();
        this.tail.setPrev(temp);
        this.size++;
    }
    private Node<T> getNode(int index) {
        if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException();
        if (this.size / 2 >= index) {
            Node<T> temp = head;
            while (index > 0) {
                temp = temp.getNext();
                index--;
            }
            return temp;
        }
        Node<T> temp = this.tail;
        index = this.size - index - 1;
        while (index > 0) {
            temp = temp.getPrev();
            index--;
        }
        return temp;
    }
    public void remove(int index) {
        if (index >= this.size || index < 0) throw new IndexOutOfBoundsException();
        if (this.size == 1) {
            this.head.onDelete();
            this.head = null;
            this.tail = null;
            this.size--;
            return;
        }
        if (index == 0) {
            this.head = this.head.getNext();
            this.head.getPrev().onDelete();
            this.head.setPrev(null);
            this.size--;
            return;
        }
        if (index == this.size - 1) {
            this.tail = this.tail.getPrev();
            this.tail.getNext().onDelete();
            this.tail.setNext(null);
            this.size--;
            return;
        }
        Node<T> temp = this.getNode(index);
        temp.getPrev().setNext(temp.getNext());
        temp.getNext().setPrev(temp.getPrev());
        temp.onDelete();
        this.size--;
    }
    public void set(int index, T data) {
        if (index >= this.size || index < 0) throw new IndexOutOfBoundsException();
        Node<T> temp = this.getNode(index);
        temp.set(data);
    }
    public void insert(int index, T data) {
        if (index >= this.size || index < -1) throw new IndexOutOfBoundsException();
        if (this.size == 0) { this.addToEnd(data); return; }
        if (index == -1) {
            Node<T> t = this.head;
            this.head = new Node<>(data);
            this.head.setNext(t);
            t.setPrev(this.head);
            this.size++;
            return;
        }
        if (index == this.size - 1) {
            Node<T> t = this.tail;
            this.tail = new Node<>(data);
            this.tail.setPrev(t);
            t.setNext(this.tail);
            this.size++;
            return;
        }
        Node<T> temp = this.getNode(index);
        Node<T> newNode = new Node<>(data);
        temp.getNext().setPrev(newNode);
        newNode.setNext(temp.getNext());
        temp.setNext(newNode);
        newNode.setPrev(temp);
        this.size++;
    }
    public T get(int index) {
        if (index >= this.size || index < 0) throw new IndexOutOfBoundsException();
        return this.getNode(index).get();
    }
    public int getSize() {
        return this.size;
    }
    @Override
    public String toString() {
        if (this.size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> temp = this.head;
        while (temp != null) {
            sb.append(temp.get());
            sb.append(",");
            temp = temp.getNext();
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    private static class Node<T> implements Serializable {
        private T data;
        private Node<T> next;
        private Node<T> prev;
        private Node(T data) {
            this.data = data;
        }
        private Node(T data, Node<T> prev) {
            this.data = data;
            this.prev = prev;
            this.next = this.prev.next;
            this.prev.next = this;
        }
        private T get() {
            return this.data;
        }
        private void set(T data) {
            this.data = data;
        }
        private Node<T> getNext() {
            return this.next;
        }
        private void setNext(Node<T> next) {
            this.next = next;
        }
        private Node<T> getPrev() {
            return this.prev;
        }
        private void setPrev(Node<T> prev) {
            this.prev = prev;
        }
        private void onDelete() {
            this.prev = null;
            this.next = null;
            this.data = null;
        }
    }
}
