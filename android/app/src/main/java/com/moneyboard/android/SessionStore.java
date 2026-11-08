package com.moneyboard.android;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

final class SessionStore {
    private final SharedPreferences preferences;

    SessionStore(Context context) throws Exception {
        MasterKey key = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
        preferences = EncryptedSharedPreferences.create(context, "moneyboard_session", key,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
    }

    String token() { return preferences.getString("token", null); }
    void save(String token, String email) {
        preferences.edit().putString("token", token).putString("email", email).apply();
    }
    String email() { return preferences.getString("email", ""); }
    void clear() { preferences.edit().clear().apply(); }
}
