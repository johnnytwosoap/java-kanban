import org.junit.jupiter.api.Test;
import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void checkStatus() {
        LocalDateTime firstTaskTime = LocalDateTime.parse("27-06-1999 15:30:45", formatter);
        LocalDateTime secondTaskTime = LocalDateTime.parse("27-06-2000 16:30:45", formatter);
        LocalDateTime thirdTaskTime = LocalDateTime.parse("29-06-2001 17:30:45", formatter);
        LocalDateTime fourthTaskTime = LocalDateTime.parse("29-06-2002 17:30:45", formatter);
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, firstTaskTime, 20);
        final int taskId = taskManager.createTask(task).getId();
        final Task savedTask = taskManager.getTask(taskId);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, secondTaskTime, 20);
        final int taskId1 = taskManager.createTask(task1).getId();
        final Task savedTask1 = taskManager.getTask(taskId1);
        savedTask.setStatus(TaskStatus.DONE);
        savedTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(savedTask);
        taskManager.updateTask(savedTask1);
        assertEquals(taskManager.getTask(taskId).getStatus(), TaskStatus.DONE, "Статусы не совпадают");
        assertEquals(taskManager.getTask(taskId1).getStatus(), TaskStatus.IN_PROGRESS, "Статусы не совпадают");


        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.createEpic(epic).getId();
        SubTask subTaskFirst = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, epicId, thirdTaskTime, 20);
        final int subTaskFirstId = taskManager.createSubTask(subTaskFirst).getId();
        final SubTask savedSubTaskFirst = taskManager.getSubTask(subTaskFirstId);
        SubTask subTaskSecond = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, epicId, fourthTaskTime, 20);
        final int subTaskSecondId = taskManager.createSubTask(subTaskSecond).getId();
        final SubTask savedSubTaskSecond = taskManager.getSubTask(subTaskSecondId);

        assertEquals(taskManager.getSubTask(subTaskFirstId).getStatus(), TaskStatus.NEW, "Статусы не совпадают");
        assertEquals(taskManager.getSubTask(subTaskSecondId).getStatus(), TaskStatus.NEW, "Статусы не совпадают");
        assertEquals(taskManager.getEpic(epicId).getStatus(), TaskStatus.NEW, "Статусы не совпадают");

        savedSubTaskFirst.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTaskFirst);
        assertEquals(taskManager.getEpic(epicId).getStatus(), TaskStatus.IN_PROGRESS, "Статусы не совпадают");

        savedSubTaskFirst.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTaskFirst);
        assertEquals(taskManager.getEpic(epicId).getStatus(), TaskStatus.IN_PROGRESS, "Статусы не совпадают");

        savedSubTaskSecond.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(savedSubTaskSecond);
        assertEquals(taskManager.getEpic(epicId).getStatus(), TaskStatus.IN_PROGRESS, "Статусы не совпадают");


        savedSubTaskSecond.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(savedSubTaskSecond);
        assertEquals(taskManager.getEpic(epicId).getStatus(), TaskStatus.DONE, "Статусы не совпадают");

    }

    @Test
    public void checkPrioritized() {
        LocalDateTime firstTaskTime = LocalDateTime.parse("27-06-1999 15:30:45", formatter);
        LocalDateTime secondTaskTime = LocalDateTime.parse("27-06-2000 16:30:45", formatter);
        LocalDateTime firstTaskTimeUpdated = LocalDateTime.parse("29-06-2001 17:30:45", formatter);
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, firstTaskTime, 20);
        final int taskId = taskManager.createTask(task).getId();
        final Task savedTask = taskManager.getTask(taskId);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, secondTaskTime, 20);
        final int taskId1 = taskManager.createTask(task1).getId();
        final Task savedTask1 = taskManager.getTask(taskId1);
        savedTask.setStartTime(firstTaskTimeUpdated);
        taskManager.updateTask(savedTask);
        assertEquals(taskManager.getPrioritizedTasks().getFirst(), savedTask1, "Задания не совпадают");
        assertEquals(taskManager.getPrioritizedTasks().getLast(), savedTask, "Задания не совпадают");
    }

}