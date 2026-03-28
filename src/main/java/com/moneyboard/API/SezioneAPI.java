package com.moneyboard.API;

import com.moneyboard.Model.DeleteSezioneRequest;
import com.moneyboard.Model.SectionCreateRequest;
import com.moneyboard.Model.SezioneRisparmio;

public class SezioneAPI {

    private final APIClient apiClient;

    public SezioneAPI() {
        this.apiClient = new APIClient();
    }

    public SezioneRisparmio[] getByUtenteId(Long utenteId) throws Exception {
        String json = apiClient.get("/api/sezioni/utente/" + utenteId);
        return apiClient.getGson().fromJson(json, SezioneRisparmio[].class);
    }

    public SezioneRisparmio create(SectionCreateRequest request) throws Exception {
        String json = apiClient.post("/api/sezioni", request);
        return apiClient.getGson().fromJson(json, SezioneRisparmio.class);
    }

    public void delete(DeleteSezioneRequest request) throws Exception {
        apiClient.delete("/api/sezioni", request);
    }
}