package ru.practicum.tasks.service.http;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.service.InMemoryTaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.*;

public class HttpTaskManager extends InMemoryTaskManager {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private Gson gson;

    public HttpTaskManager() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gson = gsonBuilder.create();
    }

    public Optional<Integer> getId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public void getAllTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = super.getAllTasks();
            if (tasks.isEmpty()) {
                writeResponse(exchange,"\"result\":\"Список заданий пуст\"", 200);
            } else {
                writeResponse(exchange, tasks.stream().map(Task::toJson).collect(Collectors.toList()), 200);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка получения заданий\"", 406);
        }
    }

    public void getTask(HttpExchange exchange, int id) throws IOException {
        try {
            Task task = super.getTask(id);
            if (task.getId() > 0) {
                writeResponse(exchange, task.toJson(), 200);
            } else {
                writeResponse(exchange, "\"result\":\"Такой задачи не существует\"", 404);
            }
        }  catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Такой задачи не существует\"", 404);
        }

    }

    public void getSubTask(HttpExchange exchange, int id) throws IOException {
        try {
            SubTask task = super.getSubTask(id);
            if (task.getId() > 0) {
                writeResponse(exchange, task.toJson(), 200);
            } else {
                writeResponse(exchange, "\"result\":\"Такой задачи не существует\"", 404);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Такой задачи не существует\"", 404);
        }

    }

    public void getEpic(HttpExchange exchange, int id) throws IOException {
        try {
            Epic task = super.getEpic(id);
            if (task.getId() > 0) {
                writeResponse(exchange, task.toJson(), 200);
            } else {
                writeResponse(exchange, "\"result\":\"Такой задачи не существует\"", 404);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Такой задачи не существует\"", 404);
        }

    }

    public void postTask(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) {
                writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
                return;
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String id = jsonObject.get("id").getAsString();
            Task jsonTask = new Task(jsonObject);
            Task task;
            if (!id.equals("null")) {
                jsonTask.setId(Integer.parseInt(id));
                task = super.updateTask(jsonTask);
            } else {
                task = super.createTask(jsonTask);
            }
            if (task.getId() > 0) {
                writeResponse(exchange, task.toJson(), 201);
            } else {
                writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
        }
    }

    public void deleteTask(HttpExchange exchange, Optional<Integer> id) throws IOException {
        if (id.isEmpty()) {
            writeResponse(exchange, "\"result\":\"Ошибка удаления задания\"", 406);
        } else {
            super.deleteTask(id.get());
            writeResponse(exchange, "\"result\":\"Задание удалено\"", 200);
        }
    }

    public void postEpic(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) {
                writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
                return;
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String id = jsonObject.get("id").getAsString();
            Epic jsonTask = new Epic(jsonObject);
            Epic task;
            if (!id.equals("null")) {
                jsonTask.setId(Integer.parseInt(id));
                task = super.updateEpic(jsonTask);
            } else {
                task = super.createEpic(jsonTask);
            }
            if (task.getId() > 0) {
                writeResponse(exchange, task.toJson(), 201);
            } else {
                writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
        }
    }

    public void deleteEpic(HttpExchange exchange, Optional<Integer> id) throws IOException {
        if (id.isEmpty()) {
            writeResponse(exchange, "\"result\":\"Ошибка удаления задания\"", 406);
        } else {
            super.deleteEpic(id.get());
            writeResponse(exchange, "\"result\":\"Задание удалено\"", 200);
        }
    }

    public void getAllEpic(HttpExchange exchange) throws IOException {
        try {
            List<Epic> epics = super.getAllEpic();
            if (epics.isEmpty()) {
                writeResponse(exchange,"\"result\":\"Список заданий пуст\"", 200);
            } else {
                writeResponse(exchange, epics.stream().map(Epic::toJson).collect(Collectors.toList()), 200);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка получения заданий\"", 406);
        }
    }

    public void postSubTask(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) {
                writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
                return;
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String id = jsonObject.get("id").getAsString();
            SubTask jsonTask = new SubTask(jsonObject);
            SubTask task;
            if (!id.equals("null")) {
                jsonTask.setId(Integer.parseInt(id));
                task = super.updateSubTask(jsonTask);
                System.out.println(task);
            } else {
                task = super.createSubTask(jsonTask);
            }
            if (task.getId() > 0) {
                writeResponse(exchange, task.toJson(), 201);
            } else {
                writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка сохранения задания\"", 406);
        }
    }

    public void getAllSubTasks(HttpExchange exchange) throws IOException {
        try {
            List<SubTask> subtasks = super.getAllSubTasks();
            if (subtasks.isEmpty()) {
                writeResponse(exchange,"\"result\":\"Список заданий пуст\"", 200);
            } else {
                writeResponse(exchange, subtasks.stream().map(SubTask::toJson).collect(Collectors.toList()), 200);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка получения заданий\"", 406);
        }
    }

    public void getAllSubTasksByEpic(HttpExchange exchange, Integer taskId) throws IOException {
        try {
            List<SubTask> subtasks = super.getAllSubTasksByEpic(taskId);
            if (subtasks.isEmpty()) {
                writeResponse(exchange,"\"result\":\"Список заданий пуст\"", 200);
            } else {
                writeResponse(exchange, subtasks.stream().map(SubTask::toJson).collect(Collectors.toList()), 200);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка получения заданий\"", 406);
        }
    }

    public void deleteSubTask(HttpExchange exchange, Optional<Integer> id) throws IOException {
        if (id.isEmpty()) {
            writeResponse(exchange, "\"result\":\"Ошибка удаления задания\"", 406);
        } else {
            super.deleteSubTask(id.get());
            writeResponse(exchange, "\"result\":\"Задание удалено\"", 200);
        }
    }

    public void errorMethod(HttpExchange exchange) throws IOException {
        writeResponse(exchange, "\"result\":\"Вы использовали какой-то другой метод!\"", 404);
    }

    public void getHistory(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = super.getHistory();
            if (tasks.isEmpty()) {
                writeResponse(exchange,"\"result\":\"Список истории пуст\"", 200);
            } else {
                writeResponse(exchange, tasks.stream().map(Task::toJson).collect(Collectors.toList()), 200);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка получения заданий\"", 406);
        }
    }

    public void getPrioritizedTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = super.getPrioritizedTasks();
            if (tasks.isEmpty()) {
                writeResponse(exchange,"\"result\":\"Список приоритетных заданий пуст\"", 200);
            } else {
                writeResponse(exchange, tasks.stream().map(Task::toJson).collect(Collectors.toList()), 200);
            }
        } catch (Exception exception) {
            writeResponse(exchange, "\"result\":\"Ошибка получения заданий\"", 406);
        }
    }

    private void writeResponse(HttpExchange exchange,
                               Object response,
                               int responseCode) throws IOException {

        String responseString = gson.toJson(response);
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }
}
