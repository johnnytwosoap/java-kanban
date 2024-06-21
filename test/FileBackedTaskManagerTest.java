import org.junit.jupiter.api.Test;

import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.FileBackedTaskManager;
import ru.practicum.tasks.service.ManagerSaveException;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

    @Test
    void checkTaskInFile() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        final int taskId = fileBackedTaskManager.createTask(task).getId();
        final Task savedTask = fileBackedTaskManager.getTask(taskId);

        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileBackedTaskManager.getFilePathForManager()))) {
            String[] taskLine = fileReader.lines().filter(line -> line.contains("Task")).findFirst().get().split(",");

            assertEquals(savedTask.getId(), Integer.parseInt(taskLine[0]), "Задачи не совпадают.");
            assertEquals(savedTask.getTaskName(), taskLine[2], "Задачи не совпадают.");
            assertEquals(savedTask.getDescription(), taskLine[3], "Задачи не совпадают.");
            assertEquals(savedTask.getStatus().name(), taskLine[4], "Задачи не совпадают.");
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка восстановления информации");
        }
    }
}
