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
        setSize(1040, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 620));
        setResizable(true);
        setUndecorated(false);

        __initComponent();
        Style.applyTheme(this);

        setVisible(true);
    }

    private void __initComponent() {
        mainPanel = Style.backgroundPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(34, 54, 34, 54));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("💰  MONEYBOARD");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        logo.setForeground(Style.PRIMARY);
        JLabel headline = Style.heading("<html>Il controllo del tuo denaro,<br>in una nuova prospettiva.</html>");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 34));
        JLabel description = Style.subtitle("<html>Organizza i tuoi obiettivi, monitora le spese<br>e costruisci il tuo futuro finanziario.</html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        brand.add(logo);
        brand.add(Box.createVerticalStrut(72));
        brand.add(headline);
        brand.add(Box.createVerticalStrut(18));
        brand.add(description);
        brand.add(Box.createVerticalStrut(36));
        JLabel features = new JLabel("<html><font color='#30C48D'>●</font>  Obiettivi sempre sotto controllo<br><br><font color='#7C6CFF'>●</font>  Entrate e uscite in un unico posto<br><br><font color='#F5B54C'>●</font>  Una visione più chiara dei tuoi risparmi</html>");
        features.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        features.setForeground(Style.MUTED);
        brand.add(features);

        JPanel formCard = Style.cardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER, 1, true),
                BorderFactory.createEmptyBorder(38, 38, 38, 38)));
        JLabel formTitle = Style.heading("Bentornato");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 27));
        JLabel formSubtitle = Style.subtitle("Accedi al tuo spazio finanziario");
        mailLabel = Style.fieldLabel("INDIRIZZO EMAIL");
        passwordLabel = Style.fieldLabel("PASSWORD");
        txtMail = new JTextField();
        txtPassword = new JPasswordField();
        Style.styleField(txtMail);
        Style.styleField(txtPassword);
        txtMail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        entraBtn = Style.newStyledButton("Accedi al tuo account  →", Color.WHITE, Style.PRIMARY, 15);
        entraBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        entraBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        entraBtn.addActionListener(e -> actionButtonLogIn());
        registratiBtn = Style.newStyledButton("Crea un account", Color.WHITE, new Color(49, 60, 86), 14);
        registratiBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registratiBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        registratiBtn.addActionListener(e -> actionButtonRegistrati());

        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(formSubtitle);
        formCard.add(Box.createVerticalStrut(38));
        formCard.add(mailLabel);
        formCard.add(Box.createVerticalStrut(9));
        formCard.add(txtMail);
        formCard.add(Box.createVerticalStrut(22));
        formCard.add(passwordLabel);
        formCard.add(Box.createVerticalStrut(9));
        formCard.add(txtPassword);
        formCard.add(Box.createVerticalStrut(30));
        formCard.add(entraBtn);
        formCard.add(Box.createVerticalStrut(12));
        formCard.add(registratiBtn);

        JPanel layout = new JPanel(new GridBagLayout());
        layout.setOpaque(false);
        GridBagConstraints left = new GridBagConstraints();
        left.gridx=0; left.gridy=0; left.weightx=1; left.weighty=1;
        left.fill=GridBagConstraints.BOTH; left.anchor=GridBagConstraints.CENTER;
        left.insets=new Insets(0,0,0,55);
        layout.add(brand,left);
        GridBagConstraints right = new GridBagConstraints();
        right.gridx=1; right.gridy=0; right.weightx=0.85; right.weighty=0;
        right.fill=GridBagConstraints.HORIZONTAL; right.anchor=GridBagConstraints.CENTER;
        right.insets=new Insets(0,15,0,0);
        layout.add(formCard,right);
        mainPanel.add(layout,BorderLayout.CENTER);
        add(mainPanel);
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