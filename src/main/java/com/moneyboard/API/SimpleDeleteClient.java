package com.moneyboard.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SimpleDeleteClient {

    private static final String BASE_URL = "https://backend-moneyboard2-0.onrender.com";

    public static void delete(String endpoint) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Errore HTTP " + response.statusCode() + ": " + response.body());
        }
    }
}