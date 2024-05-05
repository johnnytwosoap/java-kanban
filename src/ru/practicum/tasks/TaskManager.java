package ru.practicum.tasks;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<?> getHistory();

    Task createTask(Task task);

    void updateTask(Task task);

    void deleteAllTasks();

    void deleteTask(int id);

    Task getTask(int id);

    List<Task> getAllTasks();

    SubTask createSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void deleteSubTask(int id);

    SubTask getSubTask(int id);

    List<SubTask> getAllSubTasks();

    List<SubTask> getAllSubTasksByEpic(Integer epicId);

    void deleteAllSubTasks();

    Epic createEpic(Epic epic);

    void updateEpic(Epic task);

    void deleteEpic(int id);

    Epic getEpic(int id);

    List<Epic> getAllEpic();

    void deleteAllEpics();
}
