package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Node;
import ru.practicum.tasks.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList customLinkedList = new CustomLinkedList();
    private HashMap<Integer, Node<Task>> history = new HashMap<>();

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        history.put(task.getId(), customLinkedList.linkLast(task));
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            customLinkedList.removeNode(history.get(id));
            history.remove(id);
        }
    }
}
