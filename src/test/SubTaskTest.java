package test;

import org.junit.jupiter.api.Test;
import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.InMemoryTaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewSubTask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.createEpic(epic).getId();
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, epicId);
        final int subTaskId = taskManager.createSubTask(subTask).getId();

        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewSubTaskWithWrongEpic() {
        new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, 4);

        final List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertEquals(0, subTasks.size(), "Неверное количество задач.");
    }

    @Test
    void checkRemovingSubTask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.createEpic(epic).getId();
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, epicId);
        final int subTaskId = taskManager.createSubTask(subTask).getId();

        final List<SubTask> subTasks = taskManager.getAllSubTasksByEpic(epicId);
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        taskManager.deleteSubTask(subTaskId);
        final List<SubTask> tasksAfterRemoving = taskManager.getAllSubTasksByEpic(epicId);
        assertEquals(0, tasksAfterRemoving.size(), "Неверное количество задач.");
    }


    @Test
    void checkClearAllSubTask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.createEpic(epic).getId();
        SubTask subTaskFirst = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, epicId);
        taskManager.createSubTask(subTaskFirst);
        SubTask subTaskSecond= new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, epicId);
        taskManager.createSubTask(subTaskSecond);

        final List<SubTask> subTasks = taskManager.getAllSubTasksByEpic(epicId);
        assertEquals(2, subTasks.size(), "Неверное количество задач.");
        taskManager.deleteAllSubTasks();
        final List<SubTask> tasksAfterRemoving = taskManager.getAllSubTasksByEpic(epicId);
        assertEquals(0, tasksAfterRemoving.size(), "Неверное количество задач.");
    }


}