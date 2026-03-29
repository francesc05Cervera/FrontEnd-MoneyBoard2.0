package com.moneyboard.View;

import com.moneyboard.API.SezioneAPI;
import com.moneyboard.Model.SectionCreateRequest;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;

import javax.swing.*;
import java.awt.*;

public class CreateSezioneArea extends JFrame {

    private JLabel titleLabel, nomeLabel, obiettivoLabel;
    private JTextField txtNome, txtObiettivo;
    private JButton creaBtn, annullaBtn;
    private JPanel mainPanel, formPanel, buttonPanel;

    private final SezioneAPI sezioneAPI;

    public CreateSezioneArea() {
        this.sezioneAPI = new SezioneAPI();

        setTitle("MoneyBoard2.0 - Crea Sezione");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        __initComponent();

        setVisible(true);
    }

    private void __initComponent() {

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        titleLabel = new JLabel("Crea una nuova sezione", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        nomeLabel = new JLabel("Nome Sezione:");
        nomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtNome = new JTextField();
        txtNome.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNome.setPreferredSize(new Dimension(300, 35));

        obiettivoLabel = new JLabel("Obiettivo (importo da raggiungere):");
        obiettivoLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtObiettivo = new JTextField();
        txtObiettivo.setFont(new Font("Arial", Font.PLAIN, 16));
        txtObiettivo.setPreferredSize(new Dimension(300, 35));

        formPanel.add(nomeLabel);
        formPanel.add(txtNome);
        formPanel.add(obiettivoLabel);
        formPanel.add(txtObiettivo);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80));

        creaBtn = new JButton("Crea");
        creaBtn.setFont(new Font("Arial", Font.BOLD, 18));
        creaBtn.setBackground(new Color(40, 167, 69));
        creaBtn.setForeground(Color.WHITE);
        creaBtn.addActionListener(e -> actionCreaSezione());

        annullaBtn = new JButton("Annulla");
        annullaBtn.setFont(new Font("Arial", Font.BOLD, 18));
        annullaBtn.setBackground(new Color(220, 53, 69));
        annullaBtn.setForeground(Color.WHITE);
        annullaBtn.addActionListener(e -> actionAnnulla());

        buttonPanel.add(creaBtn);
        buttonPanel.add(annullaBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void actionCreaSezione() {
        String nome = txtNome.getText().trim();
        String obiettivoText = txtObiettivo.getText().trim();

        if (nome.isBlank() || obiettivoText.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Compila tutti i campi.",
                    "Campi mancanti",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Double obiettivo;

        try {
            obiettivo = Double.parseDouble(obiettivoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "L'obiettivo deve essere un numero valido.",
                    "Valore non valido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (obiettivo <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "L'obiettivo deve essere maggiore di zero.",
                    "Valore non valido",
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

        setButtonsEnabled(false);

        Double finalObiettivo = obiettivo;
        new Thread(() -> executeCreateSezione(nome, finalObiettivo, utenteLoggato)).start();
    }

    private void executeCreateSezione(String nome, Double obiettivo, Utente utenteLoggato) {
        try {
            SectionCreateRequest request = new SectionCreateRequest(nome, obiettivo, utenteLoggato);
            sezioneAPI.create(request);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        this,
                        "Sezione creata con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new SezioniArea();
                dispose();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Errore nella creazione della sezione.\n" + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void actionAnnulla() {
        new SezioniArea();
        dispose();
    }

    private void setButtonsEnabled(boolean enabled) {
        creaBtn.setEnabled(enabled);
        annullaBtn.setEnabled(enabled);
    }
}