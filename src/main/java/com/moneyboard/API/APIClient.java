package com.moneyboard.API;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class APIClient {

    private static final String BASE_URL = "https://backend-moneyboard2-0.onrender.com";
    private final HttpClient client;
    private final Gson gson;

    public APIClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }

    public String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleError(response);
        return response.body();
    }

    public String post(String endpoint, Object body) throws IOException, InterruptedException {
        String json = gson.toJson(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleError(response);
        return response.body();
    }

    public String delete(String endpoint, Object body) throws IOException, InterruptedException {
        String json = gson.toJson(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleError(response);
        return response.body();
    }

    private void handleError(HttpResponse<String> response) {
        int status = response.statusCode();

        if (status >= 400) {
            String body = response.body();
            if (body == null || body.isBlank()) {
                throw new RuntimeException("Errore HTTP: " + status);
            }
            throw new RuntimeException("Errore HTTP " + status + ": " + body);
        }
    }
}