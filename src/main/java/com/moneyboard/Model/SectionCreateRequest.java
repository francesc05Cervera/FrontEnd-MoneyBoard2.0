package com.moneyboard.Model;

/** Body di POST /api2/sezione/create. L'utente è ricavato dal token JWT. Nota: "obbiettivo" con due b. */
public class SectionCreateRequest {
    private String nome;
    private Double obbiettivo;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(String nome, Double obbiettivo) {
        this.nome = nome;
        this.obbiettivo = obbiettivo;
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
}
