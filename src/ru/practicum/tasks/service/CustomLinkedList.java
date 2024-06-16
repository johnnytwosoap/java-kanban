package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Node;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
public class CustomLinkedList<T> {
=======
public class CustomLinkedList<T>{
>>>>>>> 8f501025a9c53cb4a5477cf7b9b91b3cbfade556

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
<<<<<<< HEAD

=======
>>>>>>> 8f501025a9c53cb4a5477cf7b9b91b3cbfade556
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
