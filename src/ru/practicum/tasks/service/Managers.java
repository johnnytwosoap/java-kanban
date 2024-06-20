package ru.practicum.tasks.service;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public TaskManager getFileManager() {
        return new FileBackedTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
