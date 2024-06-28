import org.junit.jupiter.api.Test;
import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.createEpic(epic).getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getAllEpic();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void checkRemovingEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.createEpic(epic).getId();
        SubTask subTaskFirst = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, epicId, LocalDateTime.now(), 20);
        taskManager.createSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, epicId, LocalDateTime.now(), 20);
        taskManager.createSubTask(subTaskSecond);

        final List<SubTask> subTasks = taskManager.getAllSubTasksByEpic(epicId);
        assertEquals(2, subTasks.size(), "Неверное количество задач.");
        taskManager.deleteEpic(epicId);
        final List<SubTask> subTasksAfterRemoving = taskManager.getAllSubTasks();
        assertEquals(0, subTasksAfterRemoving.size(), "Неверное количество задач.");
        final List<Epic> epicsAfterRemoving = taskManager.getAllEpic();
        assertEquals(0, epicsAfterRemoving.size(), "Неверное количество задач.");
    }


    @Test
    void checkClearAllEpics() {
        Epic epicFirst = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createEpic(epicFirst);
        Epic epicSecond = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createEpic(epicSecond);

        taskManager.deleteAllEpics();
        final List<Epic> epicsAfterRemoving = taskManager.getAllEpic();
        assertEquals(0, epicsAfterRemoving.size(), "Неверное количество задач.");
    }


    @Test
    void checkDurationOfEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        Epic savedEpic = taskManager.createEpic(epic);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime firstSubTaskStartTime = LocalDateTime.parse("29-06-2024 03:04:05", formatter);
        LocalDateTime secondSubTaskStartTime = LocalDateTime.parse("27-06-2024 03:04:03", formatter);
        LocalDateTime thirdSubTaskStartTime = LocalDateTime.parse("28-06-2024 03:04:23", formatter);

        SubTask subTaskFirst = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, savedEpic.getId(), firstSubTaskStartTime, 30);
        taskManager.createSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, savedEpic.getId(), secondSubTaskStartTime, 10);
        taskManager.createSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask("Test addNewSubTask", "Test addNewSubTask description", TaskStatus.NEW, savedEpic.getId(), thirdSubTaskStartTime, 10);
        taskManager.createSubTask(subTaskThird);

        assertEquals(savedEpic.getStartTime(), secondSubTaskStartTime, "Время начала эпика не совпадает");
        assertEquals(savedEpic.getDuration(), Duration.between(secondSubTaskStartTime, firstSubTaskStartTime.plus(Duration.ofMinutes(30))), "Продолжительность эпика не совпадает");
        assertEquals(savedEpic.getEndTime(), firstSubTaskStartTime.plus(Duration.ofMinutes(30)), "Время окончания эпика не совпадает");
    }


}