package com.moneyboard.API;

import com.moneyboard.Model.DeleteSezioneRequest;
import com.moneyboard.Model.SectionCreateRequest;
import com.moneyboard.Model.SezioneRisparmio;

public class SezioneAPI {

    private final APIClient apiClient;

    public SezioneAPI() {
        this.apiClient = new APIClient();
    }

    /** GET /api2/sezione → sezioni dell'utente autenticato (id ricavato dal token). */
    public SezioneRisparmio[] getSezioni() throws Exception {
        String json = apiClient.get("/sezione");
        return apiClient.getGson().fromJson(json, SezioneRisparmio[].class);
    }

    /** POST /api2/sezione/create → risposta in testo semplice. */
    public void create(SectionCreateRequest request) throws Exception {
        apiClient.post("/sezione/create", request);
    }

    /** DELETE /api2/sezione con body { "nome": "..." } → risposta in testo semplice. */
    public void delete(DeleteSezioneRequest request) throws Exception {
        apiClient.delete("/sezione", request);
    }
}
