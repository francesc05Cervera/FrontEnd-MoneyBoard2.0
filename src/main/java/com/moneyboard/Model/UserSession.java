package com.moneyboard.Model;

public class UserSession {

    private static UserSession instance;
    private Utente utente;
    private volatile String token;

    private UserSession() {
    }

    public static synchronized UserSession getInstance() {
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

    /** Token JWT della sessione, letto da APIClient a ogni richiesta. */
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLoggedIn() {
        return utente != null && utente.getId() != null && token != null;
    }

    public void clear() {
        utente = null;
        token = null;
    }
}
