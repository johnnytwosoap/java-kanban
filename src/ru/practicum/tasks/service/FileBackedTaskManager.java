package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private Path fileTasks;
    private Boolean created;

    public FileBackedTaskManager() {
        fileTasks = Path.of("static/tasks.csv");
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

    public File getFilePathForManager() {
        return fileTasks.toFile();
    }

    private void loadFromFile() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileTasks.toFile()))) {
            fileReader.readLine();
            while (fileReader.ready()) {
                String[] taskLine = fileReader.readLine().split(",");
                switch (taskLine[1]) {
                    case "Task": {
                        super.reloadTask(new Task(taskLine[2], taskLine[3], TaskStatus.valueOf(taskLine[4])), Integer.parseInt(taskLine[0]));
                        break;
                    }
                    case "Epic": {
                        super.reloadEpic(new Epic(taskLine[2] , taskLine[2]), Integer.parseInt(taskLine[0]), TaskStatus.valueOf(taskLine[4]));
                        break;
                    }
                    case "SubTask": {
                        super.reloadSubTask(new SubTask(taskLine[2], taskLine[3], TaskStatus.valueOf(taskLine[4]), Integer.parseInt(taskLine[5])), Integer.parseInt(taskLine[0]));
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
        try (Writer fileWriter = new FileWriter(fileTasks.toFile())) {
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
