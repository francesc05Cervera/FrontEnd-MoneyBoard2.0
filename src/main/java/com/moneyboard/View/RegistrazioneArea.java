package com.moneyboard.View;

import javax.swing.*;
import java.awt.*;

public class RegistrazioneArea extends JFrame {

    private JLabel titleLabel, usernameLabel, mailLabel, passwordLabel;
    private JTextField txtUsername, txtMail;
    private JPasswordField txtPassword;
    private JButton registraBtn, tornaLoginBtn;
    private JPanel mainPanel, formPanel, buttonPanel;

    public RegistrazioneArea() {
        setTitle("MoneyBoard2.0 - Registrazione Utente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        __initComponent();

        setVisible(true);
    }

    private void __initComponent() {

        // Pannello principale
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        // Titolo
        titleLabel = new JLabel("Crea il tuo account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Pannello form
        formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridLayout(6, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Nome utente
        usernameLabel = new JLabel("Nome utente:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsername.setPreferredSize(new Dimension(300, 35));

        // Email
        mailLabel = new JLabel("E-mail:");
        mailLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtMail = new JTextField();
        txtMail.setFont(new Font("Arial", Font.PLAIN, 16));
        txtMail.setPreferredSize(new Dimension(300, 35));

        // Password
        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setPreferredSize(new Dimension(300, 35));

        // Aggiunta componenti al form
        formPanel.add(usernameLabel);
        formPanel.add(txtUsername);
        formPanel.add(mailLabel);
        formPanel.add(txtMail);
        formPanel.add(passwordLabel);
        formPanel.add(txtPassword);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Pannello pulsanti
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 50, 150));

        registraBtn = new JButton("Registrati");
        registraBtn.setFont(new Font("Arial", Font.BOLD, 18));
        registraBtn.setBackground(new Color(51, 102, 255));
        registraBtn.setForeground(Color.WHITE);

        tornaLoginBtn = new JButton("Torna al Login");
        tornaLoginBtn.setFont(new Font("Arial", Font.BOLD, 18));
        tornaLoginBtn.setBackground(new Color(0, 153, 76));
        tornaLoginBtn.setForeground(Color.WHITE);

        buttonPanel.add(registraBtn);
        buttonPanel.add(tornaLoginBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}