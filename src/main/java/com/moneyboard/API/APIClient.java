package com.moneyboard.API;

import com.google.gson.Gson;
import com.moneyboard.Model.UserSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class APIClient {

    private static final String BASE_URL = "https://backend-moneyboard2-0.onrender.com/api2";
    private static final String ACCEPT = "application/json, text/plain, */*";

    private final HttpClient client;
    private final Gson gson;

    public APIClient() {
        // Timeout ampi: sul piano gratuito di Render la prima chiamata può richiedere circa un minuto
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }

    public String get(String endpoint) throws IOException, InterruptedException {
        return send(endpoint, newRequest(endpoint).GET());
    }

    public String post(String endpoint, Object body) throws IOException, InterruptedException {
        return send(endpoint, newRequest(endpoint)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body))));
    }

    public String delete(String endpoint) throws IOException, InterruptedException {
        return send(endpoint, newRequest(endpoint).DELETE());
    }

    public String delete(String endpoint, Object body) throws IOException, InterruptedException {
        return send(endpoint, newRequest(endpoint)
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(gson.toJson(body))));
    }

    /** Login e registrazione sono gli unici endpoint che non richiedono il token. */
    private boolean isPublic(String endpoint) {
        return endpoint.startsWith("/user/login") || endpoint.startsWith("/user/register");
    }

    private HttpRequest.Builder newRequest(String endpoint) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .timeout(Duration.ofSeconds(90))
                .header("Accept", ACCEPT);

        if (!isPublic(endpoint)) {
            // Il token viene riletto dalla sessione a ogni richiesta
            UserSession session = UserSession.getInstance();
            String token = session.getToken();
            if (token == null || JwtUtils.isExpired(token)) {
                session.clear();
                throw new SessionExpiredException();
            }
            // Il backend si aspetta il token "nudo", senza il prefisso "Bearer "
            builder.header("Authorization", token);
        }
        return builder;
    }

    private String send(String endpoint, HttpRequest.Builder builder) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleError(endpoint, response);
        return response.body();
    }

    private void handleError(String endpoint, HttpResponse<String> response) {
        int status = response.statusCode();
        if (status < 400) {
            return;
        }

        if (!isPublic(endpoint) && (status == 401 || status == 403)) {
            UserSession.getInstance().clear();
            throw new SessionExpiredException();
        }

        String body = response.body();
        if (body == null || body.isBlank()) {
            throw new RuntimeException(messaggioDaStatus(status));
        }
        throw new RuntimeException("Errore HTTP " + status + ": " + body);
    }

    private String messaggioDaStatus(int status) {
        switch (status) {
            case 400: return "Richiesta non valida o dati già esistenti (HTTP 400).";
            case 401: return "Credenziali non valide (HTTP 401).";
            case 404: return "Risorsa non trovata (HTTP 404).";
            default: return "Errore del server (HTTP " + status + ").";
        }
    }
}
