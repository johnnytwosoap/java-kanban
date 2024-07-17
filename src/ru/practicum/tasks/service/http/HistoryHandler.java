package ru.practicum.tasks.service.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler  implements HttpHandler {

    public HttpTaskManager httpTaskManager;

    HistoryHandler(HttpTaskManager httpTaskManager) {
        this.httpTaskManager = httpTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET": httpTaskManager.getHistory(exchange);
            default: httpTaskManager.errorMethod(exchange);
        }
    }
}