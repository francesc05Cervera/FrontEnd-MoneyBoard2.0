package com.moneyboard.Model;

/**
 * Body di POST /api2/transazione/create.
 * Il backend legge "entrata" (non isEntrata) e "sezione.nome"; l'utente è ricavato dal token JWT.
 */
public class CreateTransactionRequest {
    private String titolo;
    private String descrizione;
    private String data;
    private Double importo;
    private boolean entrata;
    private SezioneRef sezione;

    /** Riferimento alla sezione tramite nome. */
    public static class SezioneRef {
        private String nome;

        public SezioneRef(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }

    public CreateTransactionRequest() {
    }

    public CreateTransactionRequest(String titolo, String descrizione, String data,
                                    Double importo, boolean entrata, String nomeSezione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.importo = importo;
        this.entrata = entrata;
        this.sezione = new SezioneRef(nomeSezione);
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getData() {
        return data;
    }

    public Double getImporto() {
        return importo;
    }

    public boolean isEntrata() {
        return entrata;
    }

    public SezioneRef getSezione() {
        return sezione;
    }
}
