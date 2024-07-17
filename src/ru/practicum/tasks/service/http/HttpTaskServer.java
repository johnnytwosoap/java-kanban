package ru.practicum.tasks.service.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private HttpServer httpServer;
    private HttpTaskManager httpTaskManager;

    public HttpTaskServer(HttpTaskManager taskManager, int port) throws IOException {
        httpTaskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/tasks", new TasksHandler(httpTaskManager));
        httpServer.createContext("/subtasks", new SubTasksHandler(httpTaskManager));
        httpServer.createContext("/epics", new EpicsHandler(httpTaskManager));
        httpServer.createContext("/history", new HistoryHandler(httpTaskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(httpTaskManager));
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + port + " порту!");

    }


    public void stop() {
        System.out.println("HTTP-сервер выключен");
        httpServer.stop(1);
    }
}
