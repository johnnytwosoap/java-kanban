import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.Managers;
import ru.practicum.tasks.service.TaskManager;
import ru.practicum.tasks.service.http.HttpTaskClient;
import ru.practicum.tasks.service.http.HttpTaskManager;
import ru.practicum.tasks.service.http.HttpTaskServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Test
    void checkTasks() throws IOException {
        Managers managers = new Managers();
        TaskManager defaultManager = managers.getHttpTaskManager();
        HttpTaskClient httpTaskClient = new HttpTaskClient();
        HttpTaskServer httpTaskServer = new HttpTaskServer((HttpTaskManager) defaultManager, 8080);
        defaultManager.deleteAllTasks();
        defaultManager.deleteAllEpics();

        assertTrue(defaultManager.getAllTasks().isEmpty(), "Задача найдена.");
        assertTrue(defaultManager.getAllEpic().isEmpty(), "Задача найдена.");
        assertTrue(defaultManager.getAllSubTasks().isEmpty(), "Задача найдена.");

        Task task = new Task("first task","firstTask description", TaskStatus.DONE, null, 0);
        String firstTask = httpTaskClient.client("tasks", task).replace("\\","");
        JsonElement jsonElementTask = JsonParser.parseString(firstTask.substring(1, firstTask.length() - 1));
        Integer firstTaskId = jsonElementTask.getAsJsonObject().get("id").getAsInt();
        assertEquals(1, defaultManager.getAllTasks().size(), "Задача не найдена.");


        Epic epic = new Epic("first epic","firstEpic description");
        String firstEpic = httpTaskClient.client("epics", epic).replace("\\","");

        assertEquals(1, defaultManager.getAllEpic().size(), "Задача не найдена.");


        JsonElement jsonElement = JsonParser.parseString(firstEpic.substring(1, firstEpic.length() - 1));
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Integer firstEpicId = jsonObject.get("id").getAsInt();
        SubTask subTask = new SubTask(
                "first subtask",
                "firstSubTask description",
                TaskStatus.DONE, firstEpicId,
                LocalDateTime.parse("28-06-2024 03:04:05", formatter),
                10);
        String firstSubTask = httpTaskClient.client("subtasks", subTask).replace("\\","");

        JsonElement jsonElementSubTask = JsonParser.parseString(firstSubTask.substring(1, firstSubTask.length() - 1));
        Integer firstSubTaskId = jsonElementSubTask.getAsJsonObject().get("id").getAsInt();

        assertEquals(1, defaultManager.getAllSubTasks().size(), "Задача не найдена.");



        httpTaskClient.clientGetDelete("tasks/" + firstTaskId,"DELETE");

        httpTaskClient.clientGetDelete("epics/" + firstEpicId,"DELETE");

        httpTaskClient.clientGetDelete("subtasks/" + firstSubTaskId,"DELETE");


        assertTrue(defaultManager.getAllTasks().isEmpty(), "Задача найдена.");
        assertTrue(defaultManager.getAllEpic().isEmpty(), "Задача найдена.");
        assertTrue(defaultManager.getAllSubTasks().isEmpty(), "Задача найдена.");

        httpTaskClient.client("subtasks", subTask);
        httpTaskServer.stop();
    }
}
