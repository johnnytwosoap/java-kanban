package ru.practicum.tasks.service.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.practicum.tasks.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class HttpTaskClient {

    GsonBuilder gsonBuilder = new GsonBuilder();

    public String clientGetDelete(String address, String method) {
        try {
            URI uri = URI.create("http://localhost:" + 8080 + "/" + address);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

            switch (method) {
                case "GET": {
                    request = HttpRequest.newBuilder()
                            .GET()
                            .uri(uri)
                            .version(HttpClient.Version.HTTP_1_1)
                            .header("Accept", "application/json")
                            .build();
                    break;
                }
                case "DELETE": {
                    request = HttpRequest.newBuilder()
                            .DELETE()
                            .uri(uri)
                            .version(HttpClient.Version.HTTP_1_1)
                            .header("Accept", "application/json")
                            .build();
                    break;
                }
            }
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            return response.body();
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception);
            return "";
        }

    }

    public String client(String address, Task toSend) {
        try {
            Gson gson = gsonBuilder.serializeNulls().setDateFormat("dd-MM-yyyy HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class,  new LocalDateTimeAdapter())
                .create();
            URI uri = URI.create("http://localhost:" + 8080 + "/" + address);
            String objectToSend = toSend.toJson();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(objectToSend))
                    .uri(uri)
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept",  "application/json")
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            return response.body();
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception);
            return "";
        }

    }

}
