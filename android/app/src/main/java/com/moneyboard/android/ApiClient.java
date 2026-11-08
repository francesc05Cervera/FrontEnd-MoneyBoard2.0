package com.moneyboard.android;

import com.google.gson.Gson;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

final class ApiClient {
    static final String BASE_URL = "https://backend-moneyboard2-0.onrender.com/api2";
    static final Gson GSON = new Gson();

    String request(String method, String path, Object body, String token) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + path).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(90000);
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        if (token != null && !token.isBlank()) connection.setRequestProperty("Authorization", token);
        if (body != null) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            try (OutputStream out = connection.getOutputStream()) {
                out.write(GSON.toJson(body).getBytes(StandardCharsets.UTF_8));
            }
        }
        int status = connection.getResponseCode();
        InputStream stream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
        String response = "";
        if (stream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) result.append(line);
                response = result.toString();
            }
        }
        connection.disconnect();
        if (status == 401 || status == 403) throw new SessionExpiredException();
        if (status >= 400) throw new IOException("Errore HTTP " + status + (response.isBlank() ? "" : ": " + response));
        return response;
    }

    static final class SessionExpiredException extends IOException {
        SessionExpiredException() { super("Sessione scaduta. Effettua nuovamente l'accesso."); }
    }
}
