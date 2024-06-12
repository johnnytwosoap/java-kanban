package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.service.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Task> history = new LinkedHashMap<>();

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())){
            remove(task.getId());
        }
        history.put(task.getId(),task);
    }


    @Override
    public List<Task> getHistory() {
        List<Task> taskHistory = new ArrayList<>();
        taskHistory.addAll(history.values());
        return taskHistory;
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

}
