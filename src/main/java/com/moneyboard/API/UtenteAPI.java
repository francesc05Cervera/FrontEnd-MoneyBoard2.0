package com.moneyboard.API;

import com.moneyboard.Model.LoginRequest;
import com.moneyboard.Model.LoginResponse;
import com.moneyboard.Model.UserCreateRequest;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;

public class UtenteAPI {

    private final APIClient apiClient;

    public UtenteAPI() {
        this.apiClient = new APIClient();
    }

    /** POST /api2/user/register → { "message": "..." } */
    public void register(UserCreateRequest request) throws Exception {
        apiClient.post("/user/register", request);
    }

    /**
     * POST /api2/user/login → { "token": "..." }.
     * Salva il token nella sessione e restituisce l'utente con l'id ricavato dal token.
     */
    public Utente login(LoginRequest request) throws Exception {
        String json = apiClient.post("/user/login", request);
        LoginResponse response = apiClient.getGson().fromJson(json, LoginResponse.class);

        if (response == null || response.getToken() == null || response.getToken().isBlank()) {
            throw new RuntimeException("Risposta di login non valida.");
        }

        Long id = JwtUtils.getUserId(response.getToken());
        if (id == null) {
            throw new RuntimeException("Token ricevuto non valido.");
        }

        UserSession.getInstance().setToken(response.getToken());
        return new Utente(id, null, request.getEmail(), null);
    }
}
