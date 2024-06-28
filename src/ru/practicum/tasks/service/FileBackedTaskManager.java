package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private Path fileTasks;
    private Boolean created;

    public FileBackedTaskManager(String fiePath) {
        fileTasks = Path.of(fiePath);
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
                LocalDateTime dateTime = null;
                if (!taskLine[5].equals("null")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    dateTime = LocalDateTime.parse(taskLine[5], formatter);
                }
                switch (taskLine[1]) {
                    case "Task": {
                        super.reloadTask(new Task(taskLine[2], taskLine[3], TaskStatus.valueOf(taskLine[4]), dateTime, Integer.parseInt(taskLine[6])), Integer.parseInt(taskLine[0]));
                        break;
                    }
                    case "Epic": {
                        super.reloadEpic(new Epic(taskLine[2], taskLine[2]), Integer.parseInt(taskLine[0]), TaskStatus.valueOf(taskLine[4]), dateTime, Integer.parseInt(taskLine[6]));
                        break;
                    }
                    case "SubTask": {
                        super.reloadSubTask(new SubTask(taskLine[2], taskLine[3], TaskStatus.valueOf(taskLine[4]), Integer.parseInt(taskLine[7]), dateTime, Integer.parseInt(taskLine[6])), Integer.parseInt(taskLine[0]));
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
        if (savedTask.getId() != 0 ||  savedTask.getId() != null) {
            save();
        }
        return savedTask;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public Task updateTask(Task task) {
        Task savedTask = super.updateTask(task);
        if (savedTask.getId() != 0 ||  savedTask.getId() != null) {
            save();
        }
        return savedTask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask savedTask = super.createSubTask(subTask);
        if (savedTask.getId() != 0 ||  savedTask.getId() != null) {
            save();
        }
        return savedTask;
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
    public SubTask updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
        return subTask;
    }


    @Override
    public Epic createEpic(Epic epic) {
        Epic savedTask = super.createEpic(epic);
        if (savedTask.getId() != 0 ||  savedTask.getId() != null) {
            save();
        }
        return savedTask;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic savedTask = super.updateEpic(epic);
        if (savedTask.getId() != 0 ||  savedTask.getId() != null) {
            save();
        }
        return savedTask;
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(fileTasks.toFile())) {
            fileWriter.write("id,type,name,status,description,epic\n");
            super.getAllTasks().stream().map(task -> task.toFile() + "\n").forEach(taskToFile -> saveLine(fileWriter, taskToFile));
            super.getAllEpic().stream().map(epic -> epic.toFile() + "\n").forEach(epicToFile -> saveLine(fileWriter, epicToFile));
            super.getAllSubTasks().stream().map(subTask -> subTask.toFile() + "\n").forEach(subTaskToFile -> saveLine(fileWriter, subTaskToFile));
        } catch (IOException exc) {
            throw new ManagerSaveException("Ошибка сохранения информации");
        }
    }

    private void saveLine(Writer fileWriter, String taskToFile) {
        try {
            fileWriter.write(taskToFile);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка сохранения информации");
        }
    }
}
