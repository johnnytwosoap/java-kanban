package ru.practicum.tasks.service;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
