package com.moneyboard.API;

import com.moneyboard.Model.CreateTransactionRequest;
import com.moneyboard.Model.Transazione;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TransazioneAPI {

    private final APIClient apiClient;

    public TransazioneAPI() {
        this.apiClient = new APIClient();
    }

    public Transazione[] getByUtenteId(Long utenteId) throws Exception {
        String json = apiClient.get("/api/transazioni/utente/" + utenteId);
        return apiClient.getGson().fromJson(json, Transazione[].class);
    }

    public Transazione[] getByUtenteIdAndSezioneId(Long utenteId, Long sezioneId) throws Exception {
        String json = apiClient.get("/api/transazioni/utente/" + utenteId + "/sezione/" + sezioneId);
        return apiClient.getGson().fromJson(json, Transazione[].class);
    }

    public Transazione create(CreateTransactionRequest request) throws Exception {
        String json = apiClient.post("/api/transazioni", request);
        return apiClient.getGson().fromJson(json, Transazione.class);
    }

    public void deleteById(Long id) throws Exception {
        SimpleDeleteClient.delete("/api/transazioni/" + id);
    }
}