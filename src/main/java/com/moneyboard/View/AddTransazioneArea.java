package com.moneyboard.View;

import com.moneyboard.API.TransazioneAPI;
import com.moneyboard.Model.CreateTransactionRequest;
import com.moneyboard.Model.SezioneRisparmio;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;
import com.moneyboard.Style.Style;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddTransazioneArea extends JFrame {

    private JLabel titleLabel, titoloLabel, descrizioneLabel, importoLabel, entrataLabel, dataLabel;
    private JTextField txtTitolo, txtImporto, txtData;
    private JTextArea txtDescrizione;
    private JCheckBox entrataCheck;
    private JButton aggiungiBtn, annullaBtn;
    private JPanel mainPanel, formPanel, buttonPanel;

    private final SezioneRisparmio sezioneSelezionata;
    private final TransazioneAPI transazioneAPI;

    public AddTransazioneArea(SezioneRisparmio sezioneSelezionata) {
        this.sezioneSelezionata = sezioneSelezionata;
        this.transazioneAPI = new TransazioneAPI();

        setTitle("MoneyBoard2.0 - Aggiungi Transazione");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        __initComponent();

        setVisible(true);
    }

    private void __initComponent() {

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        titleLabel = new JLabel(
                "Aggiungi una nuova transazione - " + sezioneSelezionata.getNome(),
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        titoloLabel = new JLabel("Titolo:");
        titoloLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtTitolo = new JTextField();
        txtTitolo.setFont(new Font("Arial", Font.PLAIN, 16));
        txtTitolo.setPreferredSize(new Dimension(300, 35));

        descrizioneLabel = new JLabel("Descrizione:");
        descrizioneLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtDescrizione = new JTextArea(3, 20);
        txtDescrizione.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDescrizione.setLineWrap(true);
        txtDescrizione.setWrapStyleWord(true);
        JScrollPane descrizioneScroll = new JScrollPane(txtDescrizione);

        importoLabel = new JLabel("Importo:");
        importoLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtImporto = new JTextField();
        txtImporto.setFont(new Font("Arial", Font.PLAIN, 16));
        txtImporto.setPreferredSize(new Dimension(300, 35));

        entrataLabel = new JLabel("È un'entrata?");
        entrataLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        entrataCheck = new JCheckBox("Sì");
        entrataCheck.setBackground(Color.WHITE);
        entrataCheck.setFont(new Font("Arial", Font.PLAIN, 16));

        dataLabel = new JLabel("Data:");
        dataLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtData = new JTextField(LocalDate.now().toString());
        txtData.setFont(new Font("Arial", Font.PLAIN, 16));
        txtData.setEditable(false);

        formPanel.add(titoloLabel);
        formPanel.add(txtTitolo);
        formPanel.add(descrizioneLabel);
        formPanel.add(descrizioneScroll);
        formPanel.add(importoLabel);
        formPanel.add(txtImporto);
        formPanel.add(entrataLabel);
        formPanel.add(entrataCheck);
        formPanel.add(dataLabel);
        formPanel.add(txtData);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));

        aggiungiBtn = Style.newStyledButton("Aggiungi", Color.WHITE, new Color(40, 167, 69), 18);
        aggiungiBtn.addActionListener(e -> actionAggiungi());

        annullaBtn = Style.newStyledButton("Annulla", Color.WHITE, new Color(220,53,69), 18);
        annullaBtn.addActionListener(e -> actionAnnulla());

        buttonPanel.add(aggiungiBtn);
        buttonPanel.add(annullaBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void actionAggiungi() {
        String titolo = txtTitolo.getText().trim();
        String descrizione = txtDescrizione.getText().trim();
        String importoText = txtImporto.getText().trim();
        String dataText = txtData.getText().trim();
        boolean isEntrata = entrataCheck.isSelected();

        if (titolo.isBlank() || importoText.isBlank() || dataText.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Compila tutti i campi obbligatori.",
                    "Campi mancanti",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Double importo;
        LocalDate data;

        try {
            importo = Double.parseDouble(importoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "L'importo deve essere un numero valido.",
                    "Valore non valido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (importo <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "L'importo deve essere maggiore di zero.",
                    "Valore non valido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            data = LocalDate.parse(dataText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "La data non è valida.",
                    "Data non valida",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Utente utenteLoggato = UserSession.getInstance().getUtente();

        if (utenteLoggato == null || utenteLoggato.getId() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Utente non loggato.",
                    "Errore sessione",
                    JOptionPane.ERROR_MESSAGE
            );
            new LogInArea();
            dispose();
            return;
        }

        if (sezioneSelezionata == null || sezioneSelezionata.getId() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sezione non valida.",
                    "Errore sezione",
                    JOptionPane.ERROR_MESSAGE
            );
            new SezioniArea();
            dispose();
            return;
        }

        setButtonsEnabled(false);

        new Thread(() -> executeCreateTransazione(
                titolo,
                descrizione,
                importo,
                data.toString(),
                isEntrata,
                sezioneSelezionata
        )).start();
    }

    private void executeCreateTransazione(String titolo,
                                          String descrizione,
                                          Double importo,
                                          String data,
                                          boolean isEntrata,
                                          SezioneRisparmio sezione) {
        try {
            CreateTransactionRequest request = new CreateTransactionRequest(
                    titolo,
                    descrizione,
                    data,
                    importo,
                    isEntrata,
                    sezione.getNome()
            );

            transazioneAPI.create(request);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        this,
                        "Transazione aggiunta con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new TransazioniArea(sezioneSelezionata);
                dispose();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Errore durante l'aggiunta della transazione.\n" + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void actionAnnulla() {
        dispose();
    }

    private void setButtonsEnabled(boolean enabled) {
        aggiungiBtn.setEnabled(enabled);
        annullaBtn.setEnabled(enabled);
    }
}