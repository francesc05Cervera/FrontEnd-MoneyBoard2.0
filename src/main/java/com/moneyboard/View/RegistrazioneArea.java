package com.moneyboard.View;

import com.moneyboard.API.UtenteAPI;
import com.moneyboard.Model.UserCreateRequest;

import javax.swing.*;
import java.awt.*;

public class RegistrazioneArea extends JFrame {

    private JLabel usernameLabel;
    private JLabel mailLabel;
    private JLabel passwordLabel;

    private JTextField txtUsername;
    private JTextField txtMail;
    private JPasswordField txtPassword;

    private JButton registraBtn;
    private JButton tornaLoginBtn;

    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;

    private final UtenteAPI utenteAPI;

    public RegistrazioneArea() {
        this.utenteAPI = new UtenteAPI();

        setTitle("MoneyBoard2.0 - Registrazione");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponent();

        setVisible(true);
    }

    private void initComponent() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Crea un nuovo account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridLayout(6, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));

        mailLabel = new JLabel("E-mail:");
        mailLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtMail = new JTextField();
        txtMail.setFont(new Font("Arial", Font.PLAIN, 16));

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));

        formPanel.add(usernameLabel);
        formPanel.add(txtUsername);
        formPanel.add(mailLabel);
        formPanel.add(txtMail);
        formPanel.add(passwordLabel);
        formPanel.add(txtPassword);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 50, 150));

        registraBtn = new JButton("Registrati");
        registraBtn.setFont(new Font("Arial", Font.BOLD, 18));
        registraBtn.setBackground(new Color(0, 153, 76));
        registraBtn.setForeground(Color.WHITE);
        registraBtn.addActionListener(e -> actionButtonRegistrati());

        tornaLoginBtn = new JButton("Torna al login");
        tornaLoginBtn.setFont(new Font("Arial", Font.BOLD, 18));
        tornaLoginBtn.setBackground(new Color(51, 102, 255));
        tornaLoginBtn.setForeground(Color.WHITE);
        tornaLoginBtn.addActionListener(e -> actionButtonTornaLogin());

        buttonPanel.add(registraBtn);
        buttonPanel.add(tornaLoginBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void actionButtonRegistrati() {
        String username = txtUsername.getText().trim();
        String email = txtMail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Compila tutti i campi.",
                    "Campi mancanti",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        setButtonsEnabled(false);

        new Thread(() -> executeRegistrazione(username, email, password)).start();
    }

    private void executeRegistrazione(String username, String email, String password) {
        try {
            UserCreateRequest request = new UserCreateRequest(username, email, password);
            utenteAPI.register(request);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        this,
                        "Registrazione completata con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new LogInArea();
                dispose();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Registrazione fallita.\n" + ex.getMessage(),
                    "Errore registrazione",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        registraBtn.setEnabled(enabled);
        tornaLoginBtn.setEnabled(enabled);
    }

    private void actionButtonTornaLogin() {
        new LogInArea();
        dispose();
    }
}