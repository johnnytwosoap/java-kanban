package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Node;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList<T>{

    private Node<T> head;
    private Node<T> tail;

    private int size = 0;

    public List<T> getTasks() {
        List<T> tasks = new ArrayList<>();
        if (head != null) {
            Node<T> listHead = head;
            while (listHead.getNext() != null) {
                tasks.add(listHead.getData());
                listHead = listHead.getNext();
            }
        }
        if (tail != null) {
            tasks.add(tail.getData());
        }
        return tasks;
    }
    public Node<T> linkLast(T element) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, element,null);
        if (head == null) {
            head = newNode;
        } else {
            if (head.getNext() == null) {
                head.setNext(newNode);
            }
        }
        if (oldTail != null) {
            oldTail.setNext(newNode);
        }
        tail = newNode;
        size++;
        return newNode;
    }

    public void removeNode(Node<T> task) {
        Node<T> taskHead = task.getPrev();
        Node<T> taskTail = task.getNext();

        if (taskHead == null) {
            head = taskTail;
            taskTail.setPrev(null);
        } else {
            taskHead.setNext(taskTail);
        }
        if (taskTail == null) {
            tail = taskHead;
            taskHead.setNext(null);
        } else {
            taskTail.setPrev(taskHead);
        }
        task.setData(null);
        size--;
    }

}
