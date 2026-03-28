package com.moneyboard.View;

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

    public AddTransazioneArea() {
        setTitle("MoneyBoard2.0 - Aggiungi Transazione");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        __initComponent();

        setVisible(true);
    }

    private void __initComponent() {

        // Pannello principale
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Titolo
        titleLabel = new JLabel("Aggiungi una nuova transazione", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form
        formPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        // Titolo
        titoloLabel = new JLabel("Titolo:");
        titoloLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtTitolo = new JTextField();
        txtTitolo.setFont(new Font("Arial", Font.PLAIN, 16));
        txtTitolo.setPreferredSize(new Dimension(300, 35));

        // Descrizione
        descrizioneLabel = new JLabel("Descrizione:");
        descrizioneLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtDescrizione = new JTextArea(3, 20);
        txtDescrizione.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDescrizione.setLineWrap(true);
        txtDescrizione.setWrapStyleWord(true);
        JScrollPane descrizioneScroll = new JScrollPane(txtDescrizione);

        // Importo
        importoLabel = new JLabel("Importo:");
        importoLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtImporto = new JTextField();
        txtImporto.setFont(new Font("Arial", Font.PLAIN, 16));
        txtImporto.setPreferredSize(new Dimension(300, 35));

        // Flag entrata
        entrataLabel = new JLabel("È un'entrata?");
        entrataLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        entrataCheck = new JCheckBox("Sì");
        entrataCheck.setBackground(Color.WHITE);
        entrataCheck.setFont(new Font("Arial", Font.PLAIN, 16));

        // Data (default oggi)
        dataLabel = new JLabel("Data:");
        dataLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtData = new JTextField(LocalDate.now().toString());
        txtData.setFont(new Font("Arial", Font.PLAIN, 16));
        txtData.setEditable(false);

        // Aggiunta componenti al form
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

        // Pulsanti
        buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));

        aggiungiBtn = new JButton("Aggiungi");
        aggiungiBtn.setFont(new Font("Arial", Font.BOLD, 18));
        aggiungiBtn.setBackground(new Color(40, 167, 69)); // verde
        aggiungiBtn.setForeground(Color.WHITE);
        aggiungiBtn.setEnabled(false); // disattivato finché non selezioni una sezione

        annullaBtn = new JButton("Annulla");
        annullaBtn.setFont(new Font("Arial", Font.BOLD, 18));
        annullaBtn.setBackground(new Color(220, 53, 69)); // rosso
        annullaBtn.setForeground(Color.WHITE);

        buttonPanel.add(aggiungiBtn);
        buttonPanel.add(annullaBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Metodo per attivare il pulsante dall'esterno (controller)
    public void enableAggiungiButton() {
        aggiungiBtn.setEnabled(true);
    }
}