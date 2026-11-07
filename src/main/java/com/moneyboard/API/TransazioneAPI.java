package com.moneyboard.API;

import com.moneyboard.Model.CreateTransactionRequest;
import com.moneyboard.Model.Transazione;

public class TransazioneAPI {

    private final APIClient apiClient;

    public TransazioneAPI() {
        this.apiClient = new APIClient();
    }

    /** GET /api2/transazione → tutte le transazioni dell'utente autenticato. */
    public Transazione[] getTransazioni() throws Exception {
        String json = apiClient.get("/transazione");
        return apiClient.getGson().fromJson(json, Transazione[].class);
    }

    /** GET /api2/transazione/sezione/{id} */
    public Transazione[] getBySezioneId(Long sezioneId) throws Exception {
        String json = apiClient.get("/transazione/sezione/" + sezioneId);
        return apiClient.getGson().fromJson(json, Transazione[].class);
    }

    /** POST /api2/transazione/create → risposta in testo semplice. */
    public void create(CreateTransactionRequest request) throws Exception {
        apiClient.post("/transazione/create", request);
    }

    /** DELETE /api2/transazione/{id} → risposta in testo semplice. */
    public void deleteById(Long id) throws Exception {
        apiClient.delete("/transazione/" + id);
    }
}
