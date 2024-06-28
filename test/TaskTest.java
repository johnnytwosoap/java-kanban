import org.junit.jupiter.api.Test;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, LocalDateTime.now(), 20);
        final int taskId = taskManager.createTask(task).getId();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");

    }

    @Test
    void checkRemovingTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, LocalDateTime.now(), 20);
        final int taskId = taskManager.createTask(task).getId();
        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        taskManager.deleteTask(taskId);
        final List<Task> tasksAfterRemoving = taskManager.getAllTasks();
        assertEquals(0, tasksAfterRemoving.size(), "Неверное количество задач.");
    }


    @Test
    void checkClearAllTask() {
        Task taskFirst = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, LocalDateTime.now(), 20);
        taskManager.createTask(taskFirst);
        Task taskSecond = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, LocalDateTime.now(), 20);
        taskManager.createTask(taskSecond);
        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        taskManager.deleteAllTasks();
        final List<Task> tasksAfterRemoving = taskManager.getAllTasks();
        assertEquals(0, tasksAfterRemoving.size(), "Неверное количество задач.");
    }

}