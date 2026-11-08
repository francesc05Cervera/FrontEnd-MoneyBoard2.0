# MoneyBoard Android

App Android nativa in Java per MoneyBoard 2.0, separata dal client desktop Swing.

## Requisiti
- Android Studio recente
- JDK 17
- Android SDK 35

## Avvio
1. Apri Android Studio.
2. Seleziona **Open** e scegli la cartella `android/` di questo repository.
3. Attendi il Gradle Sync e installa Android SDK 35 se richiesto.
4. Avvia l'app su un emulatore o dispositivo Android (minimo Android 7.0 / API 24).

## Funzionalità
- Registrazione e login con il backend MoneyBoard su Render.
- Token di sessione salvato con EncryptedSharedPreferences.
- Dashboard con sezioni, saldo e avanzamento obiettivo.
- Creazione ed eliminazione delle sezioni.
- Lista, creazione ed eliminazione delle transazioni.
- UI dark responsive con componenti Material.

## Backend
Usa gli endpoint REST già utilizzati dal client desktop:
`https://backend-moneyboard2-0.onrender.com/api2`

Il backend deve essere raggiungibile e mantenere attivi gli endpoint descritti nel client Swing.
