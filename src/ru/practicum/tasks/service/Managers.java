package ru.practicum.tasks.service;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public TaskManager getFileManager(String filePath) {
        return new FileBackedTaskManager(filePath);
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
