package ru.practicum.tasks.service.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;

public class EpicsHandler implements HttpHandler {
    public HttpTaskManager httpTaskManager;
    EpicsHandler(HttpTaskManager httpTaskManager) {
        this.httpTaskManager = httpTaskManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskId = httpTaskManager.getId(exchange);
        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                if (taskId.isEmpty()) {
                    httpTaskManager.getAllEpic(exchange);
                } else {
                    Optional<String> subtasks = getSubTask(exchange);
                    if (subtasks.isEmpty()) {
                        httpTaskManager.getEpic(exchange, taskId.get());
                    } else if (subtasks.get().equals("subtasks")) {
                        httpTaskManager.getAllSubTasksByEpic(exchange, taskId.get());
                    }
                }
            }
            case "POST" -> httpTaskManager.postEpic(exchange);
            case "DELETE" -> httpTaskManager.deleteEpic(exchange, taskId);
            default -> httpTaskManager.errorMethod(exchange);
        }
    }

    private Optional<String> getSubTask(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(pathParts[3]);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}