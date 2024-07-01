package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Node;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;

    public List<T> getTasks() {
        List<T> tasks = new ArrayList<>();
        Node<T> listHead = head;
        while (listHead != null) {
            tasks.add(listHead.getData());
            listHead = listHead.getNext();
        }
        return tasks;
    }

    public Node<T> linkLast(T element) {
        final Node<T> newNode = new Node<>(tail, element,null);
        if (head == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }
        tail = newNode;
        return newNode;
    }

    public void removeNode(Node<T> task) {
        Node<T> taskHead = task.getPrev();
        Node<T> taskTail = task.getNext();

        if (taskHead == null) {
            head = taskTail;
            if (taskTail != null) {
                taskTail.setPrev(null);
            }
        } else {
            taskHead.setNext(taskTail);
        }
        if (taskTail == null) {
            tail = taskHead;
            if (taskHead != null) {
                taskHead.setNext(null);
            }
        } else {
            taskTail.setPrev(taskHead);
        }
        task.setData(null);
    }

}
