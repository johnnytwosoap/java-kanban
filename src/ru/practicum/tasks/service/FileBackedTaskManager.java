package ru.practicum.tasks.service;

import ru.practicum.tasks.Main;
import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String filePathForManager;
    private Boolean created;

    public FileBackedTaskManager() {
        filePathForManager = Main.class.getResource("Main.class").getPath().substring(1).split("java-kanban")[0] + "java-kanban/tasks.csv";

        Path fileTasks = Paths.get(filePathForManager);
        try {
            if (!Files.exists(fileTasks)) {
                Files.createFile(fileTasks);
                created = true;
            } else {
                loadFromFile();
                created = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean getCreated() {
        return created;
    }

    public String getFilePathForManager() {
        return filePathForManager;
    }

    private void loadFromFile() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePathForManager))) {
            Map<String, Integer> oldEpicsId = new HashMap<>();
            while (fileReader.ready()) {
                String[] taskLine = fileReader.readLine().split(",");
                TaskStatus status = TaskStatus.NEW;
                if (taskLine[4] == TaskStatus.IN_PROGRESS.name()) {
                    status = TaskStatus.IN_PROGRESS;
                } else if (taskLine[4] == TaskStatus.DONE.name()) {
                    status = TaskStatus.DONE;
                }
                switch (taskLine[1]) {
                    case "Task": {
                        super.createTask(new Task(taskLine[2], taskLine[3], status));
                        break;
                    }
                    case "Epic": {
                        Epic savedEpic = super.createEpic(new Epic(taskLine[2], taskLine[2]));
                        oldEpicsId.put(taskLine[0], savedEpic.getId());
                        break;
                    }
                    case "SubTask": {
                        SubTask subTask = new SubTask(taskLine[2], taskLine[3], status, oldEpicsId.get(taskLine[5]));
                        super.createSubTask(subTask);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка восстановления информации");
        }
    }


    @Override
    public Task createTask(Task task) {
        Task savedTask = super.createTask(task);
        save();
        return savedTask;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask savedSubTask = super.createSubTask(subTask);
        save();
        return savedSubTask;
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }


    @Override
    public Epic createEpic(Epic epic) {
        Epic savedEpic = super.createEpic(epic);
        save();
        return savedEpic;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(filePathForManager)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : super.getAllTasks()) {
                fileWriter.write(task.toFile() + "\n");
            }
            for (Epic epic : super.getAllEpic()) {
                fileWriter.write(epic.toFile() + "\n");
            }
            for (SubTask subTask : super.getAllSubTasks()) {
                fileWriter.write(subTask.toFile() + "\n");
            }
        } catch (IOException exc) {
            throw new ManagerSaveException("Ошибка сохранения информации");
        }
    }
}
