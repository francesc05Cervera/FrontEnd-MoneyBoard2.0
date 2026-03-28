package com.moneyboard.Model;

public class CreateTransactionRequest {
    private String titolo;
    private String descrizione;
    private String data;
    private Double importo;
    private boolean isEntrata;
    private Utente utente;
    private SezioneRisparmio sezione;

    public CreateTransactionRequest() {
    }

    public CreateTransactionRequest(String titolo, String descrizione, String data,
                                    Double importo, boolean isEntrata,
                                    Utente utente, SezioneRisparmio sezione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.importo = importo;
        this.isEntrata = isEntrata;
        this.utente = utente;
        this.sezione = sezione;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public boolean isEntrata() {
        return isEntrata;
    }

    public void setEntrata(boolean isEntrata) {
        this.isEntrata = isEntrata;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public SezioneRisparmio getSezione() {
        return sezione;
    }

    public void setSezione(SezioneRisparmio sezione) {
        this.sezione = sezione;
    }
}