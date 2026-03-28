package com.moneyboard.View;

import javax.swing.*;
import java.awt.*;

public class CreateSezioneArea extends JFrame {

    private JLabel titleLabel, nomeLabel, obiettivoLabel;
    private JTextField txtNome, txtObiettivo;
    private JButton creaBtn, annullaBtn;
    private JPanel mainPanel, formPanel, buttonPanel;

    public CreateSezioneArea() {
        setTitle("MoneyBoard2.0 - Crea Sezione");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
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
        titleLabel = new JLabel("Crea una nuova sezione", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form
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

        // Pulsanti
        buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80));

        creaBtn = new JButton("Crea");
        creaBtn.setFont(new Font("Arial", Font.BOLD, 18));
        creaBtn.setBackground(new Color(40, 167, 69)); // verde
        creaBtn.setForeground(Color.WHITE);

        annullaBtn = new JButton("Annulla");
        annullaBtn.setFont(new Font("Arial", Font.BOLD, 18));
        annullaBtn.setBackground(new Color(220, 53, 69)); // rosso
        annullaBtn.setForeground(Color.WHITE);

        buttonPanel.add(creaBtn);
        buttonPanel.add(annullaBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}