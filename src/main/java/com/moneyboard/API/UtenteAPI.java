package com.moneyboard.API;

import com.moneyboard.Model.LoginRequest;
import com.moneyboard.Model.UserCreateRequest;
import com.moneyboard.Model.Utente;

public class UtenteAPI {

    private final APIClient apiClient;

    public UtenteAPI() {
        this.apiClient = new APIClient();
    }

    public void register(UserCreateRequest request) throws Exception {
        apiClient.post("/api/utenti", request);
    }

    public Utente login(LoginRequest request) throws Exception {
        String json = apiClient.post("/api/utenti/login", request);
        return apiClient.getGson().fromJson(json, Utente.class);
    }

    public Utente[] getAll() throws Exception {
        String json = apiClient.get("/api/utenti");
        return apiClient.getGson().fromJson(json, Utente[].class);
    }
}