package ru.practicum.tasks.service;

import ru.practicum.tasks.service.http.HttpTaskManager;

import java.io.IOException;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public TaskManager getFileManager(String filePath) {
        return new FileBackedTaskManager(filePath);
    }

    public TaskManager getHttpTaskManager() throws IOException {
        return new HttpTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
