package ru.practicum.tasks.service.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.tasks.model.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;


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