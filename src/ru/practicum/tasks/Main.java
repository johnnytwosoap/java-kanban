package ru.practicum.tasks;

import com.google.gson.*;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final int PORT = 8080;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) throws IOException {


        Managers managers = new Managers();
        TaskManager taskManager = managers.getHttpTaskManager();
        HttpTaskClient httpTaskClient = new HttpTaskClient();

        HttpTaskServer httpTaskServer = new HttpTaskServer((HttpTaskManager) taskManager, PORT);

        System.out.println("Поехали!");

        createTasks(httpTaskClient);
        System.out.println(" ");

        createEpicsAndSubTasks(httpTaskClient);
        System.out.println(" ");

        deleteTasks(httpTaskClient);
        System.out.println(" ");

        deleteSubTasksAndEpics(httpTaskClient);
        System.out.println(" ");

        createEpicsAndSubTasks(httpTaskClient);
        System.out.println(" ");

        deleteEpicsAndSubTasks(httpTaskClient);
        System.out.println(" ");

        createTasks(httpTaskClient);
        System.out.println(" ");

        createEpicsAndSubTasks(httpTaskClient);
        System.out.println(" ");

        updateStatuses(httpTaskClient);
        System.out.println(" ");

        checkHistory(httpTaskClient);
        System.out.println(" ");

        getResult(httpTaskClient);
        System.out.println("Окончание проверки");
        httpTaskServer.stop();

    }

    public static void createTasks(HttpTaskClient httpTaskClient) {
        System.out.println("Создаем две задачи");
        Task task1 = new Task(
                "first task",
                "firstTask description",
                TaskStatus.NEW,
                LocalDateTime.of(2024,06,29,03,04,05),
                30);
        Task task2 = new Task("second task","secondTask description", TaskStatus.DONE, null, 0);
        String firstTask = httpTaskClient.client("tasks", task1);
        String secondTask = httpTaskClient.client("tasks", task2);
        System.out.println("Первая задача " + firstTask);
        System.out.println("Вторая задача " + secondTask);
    }


    public static void createEpicsAndSubTasks(HttpTaskClient httpTaskClient) {
        System.out.println("Создаем два эпика с подзадачами");
        Epic epic1 = new Epic("first epic","firstEpic description");
        String firstEpic = httpTaskClient.client("epics", epic1).replace("\\","");
        System.out.println("Первый эпик без подзадач " + firstEpic);
        JsonElement jsonElement = JsonParser.parseString(firstEpic.substring(1, firstEpic.length() - 1));
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Integer firstEpicId = jsonObject.get("id").getAsInt();
        SubTask subTask1 = new SubTask(
                "first subtask",
                "firstSubTask description",
                TaskStatus.DONE, firstEpicId,
                LocalDateTime.parse("28-06-2024 03:04:05", formatter),
                10);

        String firstSubTask = httpTaskClient.client("subtasks", subTask1);
        System.out.println("Первая подзадача " + firstSubTask);

        Epic epic2 = new Epic("second epic","secondEpic description");

        String secondEpic = httpTaskClient.client("epics", epic2).replace("\\","");
        JsonElement jsonElements = JsonParser.parseString(secondEpic.substring(1, secondEpic.length() - 1));
        JsonObject jsonObjects = jsonElements.getAsJsonObject();
        Integer secondEpicId = jsonObjects.get("id").getAsInt();
        SubTask subTask2 = new SubTask(
                "second subtask",
                "secondSubTask description",
                TaskStatus.NEW, secondEpicId,
                LocalDateTime.parse("28-06-2024 02:03:15", formatter),
                5);
        String secondSubTask = httpTaskClient.client("subtasks", subTask2);
        System.out.println("Вторая подзадача " + secondSubTask);

        SubTask subTask3 = new SubTask("third subtask", "thirdSubTask description", TaskStatus.NEW, secondEpicId, null, 0);

        String thirdSubTask = httpTaskClient.client("subtasks", subTask3);
        System.out.println("Третья подзадача " + thirdSubTask);
        System.out.println("Первый эпик c подзадачами " + firstEpic);
        String allSubTasks = httpTaskClient.clientGetDelete("subtasks","GET");
        System.out.println("Все подзадачи " + allSubTasks);
        String allSubTasksByEpic = httpTaskClient.clientGetDelete("epics/" + secondEpicId + "/subtasks","GET");
        System.out.println("Подзадачи второго эпика " + allSubTasksByEpic);
    }

    public static void deleteTasks(HttpTaskClient httpTaskClient) {
        System.out.println("Удаляем задания по очереди");
        //System.out.println("Список задач до удаления" + taskManager.getAllTasks());
        String response = httpTaskClient.clientGetDelete("tasks","GET");
        List<String> allTasks = Arrays.stream(
                        response
                        .replace("[","")
                        .replace("]","")
                        .replace("}\",","};")
                        .replace("}\"","}")
                        .split(";"))
                        .map(String::trim)
                        .map(task -> task.substring(1).replace("\\",""))
                        .toList();
        System.out.println("Список задач до удаления" + allTasks);
        List<JsonObject> jsonObjects = allTasks.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        for (JsonObject jsonObject: jsonObjects) {
            httpTaskClient.clientGetDelete("tasks/" + jsonObject.get("id").getAsString(),"DELETE");
        }
        String allTasksAfter = httpTaskClient.clientGetDelete("tasks/","GET");
        System.out.println("Список задач после удаления" + allTasksAfter);
    }

    public static void clearTasks(TaskManager taskManager) {
        System.out.println("Удаляем все задания");
        System.out.println("Список задач до удаления" + taskManager.getAllTasks());
        taskManager.deleteAllTasks();
        System.out.println("Список задач после удаления" + taskManager.getAllTasks());
    }

    public static void deleteSubTasksAndEpics(HttpTaskClient httpTaskClient) {
        System.out.println("Удаляем подзадания по очереди");
        List<String> allTasks = listStringResponse(httpTaskClient.clientGetDelete("subtasks","GET"));
        System.out.println("Список подзадач до удаления" + allTasks);
        List<JsonObject> jsonObjects = allTasks.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        for (JsonObject jsonObject: jsonObjects) {
            httpTaskClient.clientGetDelete("subtasks/" + jsonObject.get("id").getAsString(),"DELETE");
        }

        List<String> allTasksAfter = listStringResponse(httpTaskClient.clientGetDelete("subtasks","GET"));
        System.out.println("Список задач после удаления" + allTasksAfter);

        List<String> allEpics = listStringResponse(httpTaskClient.clientGetDelete("epics","GET"));
        System.out.println("Список эпиков после удаления подзадач " + allEpics);
        System.out.println("Удаляем эпики по очереди");
        List<JsonObject> jsonObjectsEpics = allEpics.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        for (JsonObject jsonObject: jsonObjectsEpics) {
            httpTaskClient.clientGetDelete("epics/" + jsonObject.get("id").getAsString(),"DELETE");
        }
        List<String> allEpicsafter = listStringResponse(httpTaskClient.clientGetDelete("epics","GET"));
        System.out.println("Список эпиков после удаления " + allEpicsafter);
    }

    public static List<String> listStringResponse(String response) {
        return Arrays.stream(response
                                .replace("[","")
                                .replace("]","")
                                .replace("}\",","};")
                                .replace("}\"","}")
                                .split(";"))
                .map(String::trim)
                .map(task -> task.substring(1).replace("\\",""))
                .toList();
    }

    public static void deleteEpicsAndSubTasks(HttpTaskClient httpTaskClient) {
        System.out.println("Удаляем эпики по очереди");
        List<String> allEpics = listStringResponse(httpTaskClient.clientGetDelete("epics","GET"));
        System.out.println("Список эпиков до удаления " + allEpics);
        List<JsonObject> jsonObjectsEpics = allEpics.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        for (JsonObject jsonObject: jsonObjectsEpics) {
            httpTaskClient.clientGetDelete("epics/" + jsonObject.get("id").getAsString(),"DELETE");
        }
        System.out.println("Список эпиков после удаления " + listStringResponse(httpTaskClient.clientGetDelete("epics","GET")));
        System.out.println("Список подзадач после удаления эпиков " + listStringResponse(httpTaskClient.clientGetDelete("subtasks","GET")));
    }

    public static void clearSubTasks(TaskManager taskManager) {
        System.out.println("Удаляем все подзадания");
        System.out.println("Список подзадач до удаления" + taskManager.getAllSubTasks());
        taskManager.deleteAllSubTasks();
        System.out.println("Список подзадач после удаления" + taskManager.getAllSubTasks());
        System.out.println("Список эпиков после удаления подзадач " + taskManager.getAllEpic());
    }

    public static void clearEpics(TaskManager taskManager) {
        System.out.println("Удаляем все эпики");
        System.out.println("Список эпиков до удаления " + taskManager.getAllEpic());
        taskManager.deleteAllEpics();
        System.out.println("Список эпиков после удаления " + taskManager.getAllEpic());
        System.out.println("Список подзадач после удаления эпиков " + taskManager.getAllSubTasks());
    }

    public static void updateStatuses(HttpTaskClient httpTaskClient) {
        System.out.println("Изменяем статусы заданий");
        List<String> listStringsTasks = listStringResponse(httpTaskClient.clientGetDelete("tasks","GET"));
        List<JsonObject> jsonObjectsTasks = listStringsTasks.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        Task task = new Task(jsonObjectsTasks.get(0));
        task.setId(jsonObjectsTasks.get(0).get("id").getAsInt());
        System.out.println(listStringResponse(httpTaskClient.clientGetDelete("tasks","GET")));
        task.setStatus(TaskStatus.DONE);
        httpTaskClient.client("tasks", task);
        System.out.println(listStringResponse(httpTaskClient.clientGetDelete("tasks","GET")));
        task.setStatus(TaskStatus.IN_PROGRESS);
        httpTaskClient.client("tasks", task);
        System.out.println(listStringResponse(httpTaskClient.clientGetDelete("tasks","GET")));


        System.out.println("Изменяем статусы подзаданий");
        List<String> listStringsEpics = listStringResponse(httpTaskClient.clientGetDelete("epics","GET"));
        List<JsonObject> jsonObjectsEpics = listStringsEpics.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        Epic epic = new Epic(jsonObjectsEpics.get(0));
        epic.setId(jsonObjectsEpics.get(0).get("id").getAsInt());
        System.out.println("Начальное состояние " + epic);
        List<String> listStringsSubTasksByEpic = listStringResponse(httpTaskClient.clientGetDelete("epics/" + epic.getId() + "/subtasks","GET"));
        List<JsonObject> jsonObjectsSubTasksByEpic = listStringsSubTasksByEpic.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        SubTask subTask = new SubTask(jsonObjectsSubTasksByEpic.get(0));
        SubTask subTask1 = new SubTask(jsonObjectsSubTasksByEpic.get(1));
        subTask.setId(jsonObjectsSubTasksByEpic.get(0).get("id").getAsInt());
        subTask1.setId(jsonObjectsSubTasksByEpic.get(1).get("id").getAsInt());
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        httpTaskClient.client("subtasks", subTask);
        System.out.println("Одно подзадание статус в процессе " + httpTaskClient.clientGetDelete("epics/"+epic.getId(),"GET"));
        subTask.setStatus(TaskStatus.DONE);
        httpTaskClient.client("subtasks", subTask);
        System.out.println("Одно подзадание статус сделано " +  httpTaskClient.clientGetDelete("epics/"+epic.getId(),"GET"));
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        httpTaskClient.client("subtasks", subTask1);
        System.out.println("Сделано + в процессе " +  httpTaskClient.clientGetDelete("epics/"+epic.getId(),"GET"));
        subTask1.setStatus(TaskStatus.DONE);
        httpTaskClient.client("subtasks", subTask1);
        System.out.println("Два подзадания статус сделано " +  httpTaskClient.clientGetDelete("epics/"+epic.getId(),"GET"));
    }

    public static void checkHistory(HttpTaskClient httpTaskClient) {
        System.out.println("Проверяем историю обращений");
        List<String> listStringsTasks = listStringResponse(httpTaskClient.clientGetDelete("tasks","GET"));
        List<JsonObject> jsonObjectsTasks = listStringsTasks.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        Integer taskId = jsonObjectsTasks.get(0).get("id").getAsInt();
        List<String> listStringsEpics = listStringResponse(httpTaskClient.clientGetDelete("epics","GET"));
        List<JsonObject> jsonObjectsEpics = listStringsEpics.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        Integer epicId = jsonObjectsEpics.get(0).get("id").getAsInt();
        List<String> listStringsSubTasksByEpic = listStringResponse(httpTaskClient.clientGetDelete("epics/" + epicId + "/subtasks","GET"));
        List<JsonObject> jsonObjectsSubTasksByEpic = listStringsSubTasksByEpic.stream().map(JsonParser::parseString).map(JsonElement::getAsJsonObject).toList();
        Integer subtaskId = jsonObjectsSubTasksByEpic.get(0).get("id").getAsInt();
        httpTaskClient.clientGetDelete("tasks/" + taskId,"GET");
        httpTaskClient.clientGetDelete("subtasks/" + subtaskId,"GET");
        httpTaskClient.clientGetDelete("epics/" + epicId,"GET");
        httpTaskClient.clientGetDelete("tasks/" + taskId,"GET");
        httpTaskClient.clientGetDelete("subtasks/" + subtaskId,"GET");
        httpTaskClient.clientGetDelete("epics/" + epicId,"GET");
        httpTaskClient.clientGetDelete("tasks/" + taskId,"GET");
        httpTaskClient.clientGetDelete("subtasks/" + subtaskId,"GET");
        httpTaskClient.clientGetDelete("epics/" + epicId,"GET");
        httpTaskClient.clientGetDelete("tasks/" + taskId,"GET");
        httpTaskClient.clientGetDelete("subtasks/" + subtaskId,"GET");
        httpTaskClient.clientGetDelete("epics/" + epicId,"GET");
        System.out.println(listStringResponse(httpTaskClient.clientGetDelete("history","GET")));
    }

    public static void getResult(HttpTaskClient httpTaskClient) {
        System.out.println("Задания после всех проверок");
        for (String task : listStringResponse(httpTaskClient.clientGetDelete("tasks","GET"))) {
            System.out.println("Task " + task);
        }
        for (String task : listStringResponse(httpTaskClient.clientGetDelete("epics","GET"))) {
            System.out.println("Epic " + task.toString());
        }
        for (String task : listStringResponse(httpTaskClient.clientGetDelete("subtasks","GET"))) {
            System.out.println("SubTask " + task.toString());
        }
        System.out.println("Задания после всех проверок по приоритету");
        for (Object task : listStringResponse(httpTaskClient.clientGetDelete("prioritized","GET"))) {
            System.out.println(task.toString());
        }
    }

}
