import org.junit.jupiter.api.Test;

import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.FileBackedTaskManager;
import ru.practicum.tasks.service.ManagerSaveException;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("static/taskstest.csv");

    @Test
    void checkTaskInFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime taskStartTime = LocalDateTime.parse("27-06-2024 03:04:05", formatter);
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, taskStartTime, 20);
        final int taskId = fileBackedTaskManager.createTask(task).getId();
        final Task savedTask = fileBackedTaskManager.getTask(taskId);

        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileBackedTaskManager.getFilePathForManager()))) {
            String[] taskLine = fileReader.lines().filter(line -> line.contains("Task")).findFirst().get().split(",");

            assertEquals(savedTask.getId(), Integer.parseInt(taskLine[0]), "Задачи не совпадают.");
            assertEquals(savedTask.getTaskName(), taskLine[2], "Задачи не совпадают.");
            assertEquals(savedTask.getDescription(), taskLine[3], "Задачи не совпадают.");
            assertEquals(savedTask.getStatus().name(), taskLine[4], "Задачи не совпадают.");
            assertEquals(savedTask.getStartTime(), LocalDateTime.parse(taskLine[5], formatter), "Задачи не совпадают.");
            assertEquals(savedTask.getDuration(), Duration.ofMinutes(Integer.parseInt(taskLine[6])), "Задачи не совпадают.");

        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка восстановления информации");
        }
    }
}
