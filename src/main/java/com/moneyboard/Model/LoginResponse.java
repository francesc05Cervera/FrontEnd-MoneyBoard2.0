package com.moneyboard.Model;

/** Risposta di POST /api2/user/login. */
public class LoginResponse {
    private String token;

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
