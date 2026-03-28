package com.moneyboard.Model;

public class SezioneRisparmio {
    private Long id;
    private String nome;
    private Double obiettivo;
    private Double saldo;
    private Utente utente;

    public SezioneRisparmio() {
    }

    public SezioneRisparmio(Long id, String nome, Double obiettivo, Double saldo, Utente utente) {
        this.id = id;
        this.nome = nome;
        this.obiettivo = obiettivo;
        this.saldo = saldo;
        this.utente = utente;
    }

    public SezioneRisparmio(String nome, Double obiettivo, Double saldo, Utente utente) {
        this.nome = nome;
        this.obiettivo = obiettivo;
        this.saldo = saldo;
        this.utente = utente;
    }

    public SezioneRisparmio(String nome, Double obiettivo, Utente utente) {
        this.nome = nome;
        this.obiettivo = obiettivo;
        this.utente = utente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getObiettivo() {
        return obiettivo;
    }

    public void setObiettivo(Double obiettivo) {
        this.obiettivo = obiettivo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    @Override
    public String toString() {
        return nome;
    }
}