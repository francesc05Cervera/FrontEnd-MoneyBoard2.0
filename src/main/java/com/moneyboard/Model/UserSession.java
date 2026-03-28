package com.moneyboard.Model;

import com.moneyboard.Model.Utente;

public class UserSession {

    private static UserSession instance;
    private Utente utente;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public boolean isLoggedIn() {
        return utente != null && utente.getId() != null;
    }

    public void clear() {
        utente = null;
    }
}