package test;

import org.junit.jupiter.api.Test;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.InMemoryHistoryManager;
import ru.practicum.tasks.service.InMemoryTaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewTaskInHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void checkHistoryLengthForRepeatedTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        final int taskId = taskManager.createTask(task).getId();
        final Task savedTask = taskManager.getTask(taskId);
        historyManager.add(savedTask);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

        Task taskSecond = new Task("Test addNewTask2", "Test addNewTask2 description", TaskStatus.NEW);
        final int taskIdSecond = taskManager.createTask(taskSecond).getId();
        final Task savedTaskSecond = taskManager.getTask(taskIdSecond);
        historyManager.add(savedTaskSecond);
        Task taskThird = new Task("Test addNewTask3", "Test addNewTask3 description", TaskStatus.NEW);
        final int taskIdThird = taskManager.createTask(taskThird).getId();
        final Task savedTaskThird = taskManager.getTask(taskIdThird);
        historyManager.add(savedTaskThird);
        historyManager.add(savedTask);

        final List<Task> historySecond = historyManager.getHistory();
        assertNotNull(historySecond, "История не пустая.");
        assertEquals(3, historySecond.size(), "История не пустая.");
        assertEquals(2, historySecond.get(0).getId(), "История не пустая.");
        assertEquals(3, historySecond.get(1).getId(), "История не пустая.");
        assertEquals(1, historySecond.get(2).getId(), "История не пустая.");
    }

}