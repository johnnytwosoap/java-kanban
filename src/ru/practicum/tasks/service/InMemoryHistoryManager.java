package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.service.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size()==10){
            history.remove(0);
        }
        history.add(task);
    }


    @Override
    public List<Task> getHistory() {
        return history;
    }

}
