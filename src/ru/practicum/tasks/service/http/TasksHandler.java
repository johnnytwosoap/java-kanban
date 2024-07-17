package ru.practicum.tasks.service.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;


public class TasksHandler implements HttpHandler {
    public HttpTaskManager httpTaskManager;
    TasksHandler(HttpTaskManager httpTaskManager) {
        this.httpTaskManager = httpTaskManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskId = httpTaskManager.getId(exchange);
        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                if (taskId.isEmpty()) {
                    httpTaskManager.getAllTasks(exchange);
                } else {
                    httpTaskManager.getTask(exchange, taskId.get());
                }
            }
            case "POST" -> httpTaskManager.postTask(exchange);
            case "DELETE" -> httpTaskManager.deleteTask(exchange, taskId);
            default -> httpTaskManager.errorMethod(exchange);
        }
    }
}
