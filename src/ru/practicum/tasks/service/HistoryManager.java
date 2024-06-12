package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);

}
