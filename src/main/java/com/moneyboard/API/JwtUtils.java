package com.moneyboard.API;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/** Lettura (senza verifica della firma) del payload di un token JWT: id utente e scadenza. */
public final class JwtUtils {

    private static final Gson GSON = new Gson();

    private JwtUtils() {
    }

    /** L'id dell'utente è il campo "sub" del token. */
    public static Long getUserId(String token) {
        Map<String, Object> payload = payload(token);
        if (payload == null || payload.get("sub") == null) {
            return null;
        }
        try {
            return Long.valueOf(String.valueOf(payload.get("sub")));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** true se il token è illeggibile, senza scadenza o già scaduto. */
    public static boolean isExpired(String token) {
        Map<String, Object> payload = payload(token);
        if (payload == null || !(payload.get("exp") instanceof Number)) {
            return true;
        }
        long exp = ((Number) payload.get("exp")).longValue();
        return exp * 1000 <= System.currentTimeMillis();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> payload(String token) {
        try {
            String[] parts = token.split("\\.");
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            return GSON.fromJson(new String(decoded, StandardCharsets.UTF_8), Map.class);
        } catch (Exception e) {
            return null;
        }
    }
}
