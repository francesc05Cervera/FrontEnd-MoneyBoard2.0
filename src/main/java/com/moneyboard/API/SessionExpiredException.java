package com.moneyboard.API;

/** Lanciata quando il token JWT manca, è scaduto o viene rifiutato dal backend. */
public class SessionExpiredException extends RuntimeException {

    public SessionExpiredException() {
        super("Sessione scaduta, effettua di nuovo il login.");
    }
}
