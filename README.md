# 💰 MoneyBoard 2.0 — Desktop Client

> **Frontend desktop** per l'applicazione **MoneyBoard 2.0**, sviluppato in **Java Swing**.  
> Interfaccia grafica per la gestione del risparmio personale: sezioni, transazioni e profilo utente, il tutto collegato al backend REST su cloud.

---

## 📋 Indice

- [Panoramica](#-panoramica)
- [Funzionalità](#-funzionalità)
- [Tech Stack](#-tech-stack)
- [Struttura del Progetto](#-struttura-del-progetto)
- [Prerequisiti](#-prerequisiti)
- [Installazione & Avvio](#-installazione--avvio)
- [Connessione al Backend](#-connessione-al-backend)
- [Autore](#-autore)

---

## 🧭 Panoramica

**MoneyBoard 2.0 Desktop** è il client grafico del sistema MoneyBoard, un'applicazione per la gestione del risparmio personale. Permette all'utente di registrarsi, autenticarsi e gestire le proprie **sezioni di risparmio** e le relative **transazioni** tramite un'interfaccia Swing pulita e moderna.

Comunica con il backend Spring Boot deployato su **Render** tramite chiamate HTTP REST, utilizzando `HttpClient` nativo di Java 17 e **Gson** per la serializzazione/deserializzazione JSON.

---

## ✨ Funzionalità

- 🔐 **Registrazione & Login** — Autenticazione utente con gestione della sessione lato client
- 📂 **Gestione Sezioni** — Visualizza, crea ed elimina sezioni di risparmio personalizzate
- 💸 **Gestione Transazioni** — Aggiungi, visualizza ed elimina transazioni per ogni sezione
- 📊 **Riepilogo Saldi** — Visualizzazione del saldo aggiornato per ogni sezione di risparmio
- 🌐 **Integrazione REST API** — Comunicazione asincrona con il backend tramite HTTP (GET, POST, DELETE)

---

## 🛠️ Tech Stack

| Componente        | Tecnologia                        |
|-------------------|-----------------------------------|
| Linguaggio        | Java 17                           |
| GUI Framework     | Java Swing                        |
| HTTP Client       | `java.net.http.HttpClient` (JDK)  |
| Serializzazione   | Gson 2.10.1                       |
| Build Tool        | Maven                             |
| Backend (remoto)  | Spring Boot su Render             |
| HTTP aggiuntivo   | OkHttp 4.12.0                     |

---

## 📁 Struttura del Progetto

```
FrontEnd-MoneyBoard2.0/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/moneyboard/
                ├── MainApp.java              # Entry point dell'applicazione
                ├── API/
                │   ├── APIClient.java        # Client HTTP base (GET, POST, DELETE)
                │   ├── UtenteAPI.java        # Chiamate API per gli utenti
                │   ├── SezioneAPI.java       # Chiamate API per le sezioni
                │   ├── TransazioneAPI.java   # Chiamate API per le transazioni
                │   └── SimpleDeleteClient.java
                ├── Model/
                │   ├── Utente.java           # Modello utente
                │   ├── UserSession.java      # Sessione utente singleton
                │   ├── SezioneRisparmio.java # Modello sezione di risparmio
                │   ├── Transazione.java      # Modello transazione
                │   ├── LoginRequest.java
                │   ├── UserCreateRequest.java
                │   ├── SectionCreateRequest.java
                │   ├── CreateTransactionRequest.java
                │   └── DeleteSezioneRequest.java
                ├── View/
                │   ├── LogInArea.java        # Schermata di login
                │   ├── RegistrazioneArea.java# Schermata di registrazione
                │   ├── SezioniArea.java      # Lista e gestione sezioni
                │   ├── CreateSezioneArea.java# Form creazione sezione
                │   ├── TransazioniArea.java  # Lista e gestione transazioni
                │   └── AddTransazioneArea.java # Form aggiunta transazione
                └── Style/
                    └── ...                   # Classi per lo stile UI
```

---

## ✅ Prerequisiti

- **Java 17** o superiore installato
- **Maven 3.8+** installato
- Connessione Internet attiva (per comunicare con il backend su Render)

---

## 🚀 Installazione & Avvio

1. **Clona il repository:**
   ```bash
   git clone https://github.com/francesc05Cervera/FrontEnd-MoneyBoard2.0.git
   cd FrontEnd-MoneyBoard2.0
   ```

2. **Compila il progetto con Maven:**
   ```bash
   mvn clean package
   ```

3. **Avvia l'applicazione:**
   ```bash
   java -jar target/MoneyBoard2_Swing-1.0.jar
   ```
   
   Oppure esegui direttamente la classe principale dall'IDE:
   ```
   com.moneyboard.MainApp
   ```

---

## 🌐 Connessione al Backend

Il client si connette al backend MoneyBoard 2.0 deployato su **Render**:

```
Base URL: https://backend-moneyboard2-0.onrender.com
```

> ⚠️ **Nota:** Il backend è hostato su un piano gratuito di Render. La **prima richiesta** potrebbe richiedere qualche secondo in più a causa del cold start del server.

### Endpoint principali utilizzati

| Metodo | Endpoint               | Descrizione                     |
|--------|------------------------|---------------------------------|
| POST   | `/utenti/login`        | Login utente                    |
| POST   | `/utenti/register`     | Registrazione nuovo utente      |
| GET    | `/sezioni`             | Lista sezioni dell'utente       |
| POST   | `/sezioni`             | Crea una nuova sezione          |
| DELETE | `/sezioni`             | Elimina una sezione             |
| GET    | `/transazioni`         | Lista transazioni di una sezione|
| POST   | `/transazioni`         | Aggiunge una transazione        |
| DELETE | `/transazioni`         | Elimina una transazione         |

---

## 👤 Autore

**Francesco Cervera**  
🎓 Studente di Informatica — Università degli Studi di Napoli Federico II  
🐙 GitHub: [@francesc05Cervera](https://github.com/francesc05Cervera)

---

> *Progetto sviluppato come frontend desktop del sistema MoneyBoard 2.0, in abbinamento al backend Spring Boot.*
