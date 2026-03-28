package com.moneyboard.Model;

public class SectionCreateRequest {
    private String nome;
    private Double obbiettivo;
    private Utente utente;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(String nome, Double obbiettivo, Utente utente) {
        this.nome = nome;
        this.obbiettivo = obbiettivo;
        this.utente = utente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getObbiettivo() {
        return obbiettivo;
    }

    public void setObbiettivo(Double obbiettivo) {
        this.obbiettivo = obbiettivo;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}