package com.moneyboard.View;

import com.moneyboard.API.UtenteAPI;
import com.moneyboard.Model.LoginRequest;
import com.moneyboard.Model.Utente;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Style.*;

import javax.swing.*;
import java.awt.*;

public class LogInArea extends JFrame {

    private JLabel mailLabel;
    private JLabel passwordLabel;
    private JTextField txtMail;
    private JPasswordField txtPassword;
    private JButton entraBtn;
    private JButton registratiBtn;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;

    private final UtenteAPI utenteAPI;

    public LogInArea() {
        this.utenteAPI = new UtenteAPI();

        setTitle("MoneyBoard2.0 - Login Utente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);

        __initComponent();
        Style.applyTheme(this);

        setVisible(true);
    }

    private void __initComponent() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Benvenuto! Effettua il login per iniziare", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        __CreateForm();

        __CreateButtonPanel();

        add(mainPanel);
    }

	private void __CreateButtonPanel() {
		buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 50, 150));

        entraBtn = Style.newStyledButton("Entra", Color.WHITE, new Color(0, 153, 76), 18);
        entraBtn.addActionListener(e -> actionButtonLogIn());

        registratiBtn = Style.newStyledButton("Sei Nuovo? Registrati qui!", Color.WHITE, new Color(51, 102, 255), 18);
        registratiBtn.addActionListener(e -> actionButtonRegistrati());

        buttonPanel.add(entraBtn);
        buttonPanel.add(registratiBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void __CreateForm() {
	    formPanel = new JPanel();
	    formPanel.setBackground(Color.WHITE);
	    formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
	    formPanel.setLayout(new GridBagLayout());

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 10, 10);
	    gbc.anchor = GridBagConstraints.WEST;

	    // LABEL EMAIL
	    mailLabel = new JLabel("E-mail:");
	    mailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    formPanel.add(mailLabel, gbc);

	    // TEXTFIELD EMAIL
	    txtMail = new JTextField();
	    txtMail.setFont(new Font("Arial", Font.PLAIN, 16));
	    txtMail.setPreferredSize(new Dimension(220, 30)); // più stretto
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    formPanel.add(txtMail, gbc);

	    // LABEL PASSWORD
	    passwordLabel = new JLabel("Password:");
	    passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    formPanel.add(passwordLabel, gbc);

	    // TEXTFIELD PASSWORD
	    txtPassword = new JPasswordField();
	    txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
	    txtPassword.setPreferredSize(new Dimension(220, 30)); // più stretto
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    formPanel.add(txtPassword, gbc);

	    mainPanel.add(formPanel, BorderLayout.CENTER);
	}

    private void actionButtonLogIn() {
        String email = txtMail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Inserisci email e password.",
                    "Campi mancanti",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        setButtonsEnabled(false);

        new Thread(() -> executeLogin(email, password)).start();
    }

    private void executeLogin(String email, String password) {
        try {
            LoginRequest request = new LoginRequest(email, password);
            Utente utente = utenteAPI.login(request);

            UserSession.getInstance().setUtente(utente);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        this,
                        "Login effettuato con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new SezioniArea();
                dispose();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Email o password errati.\n" + ex.getMessage(),
                    "Errore login",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        entraBtn.setEnabled(enabled);
        registratiBtn.setEnabled(enabled);
    }

    private void actionButtonRegistrati() {
        new RegistrazioneArea();
        dispose();
    }
}