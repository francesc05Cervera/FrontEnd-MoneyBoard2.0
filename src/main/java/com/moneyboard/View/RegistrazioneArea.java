package com.moneyboard.View;

import com.moneyboard.API.UtenteAPI;
import com.moneyboard.Model.UserCreateRequest;
import com.moneyboard.style.Style;

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
        setSize(1040, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 620));
        setResizable(true);

        initComponent();
        Style.applyTheme(this);

        setVisible(true);
    }

    private void initComponent() {
        mainPanel = Style.backgroundPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(34, 54, 34, 54));
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand,BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("💰  MONEYBOARD");
        logo.setFont(new Font("Segoe UI Emoji",Font.PLAIN,20));
        logo.setForeground(Style.PRIMARY);
        JLabel headline = Style.heading("<html>Inizia a costruire<br>il tuo futuro.</html>");
        headline.setFont(new Font("Segoe UI",Font.BOLD,34));
        brand.add(logo);
        brand.add(Box.createVerticalStrut(80));
        brand.add(headline);
        brand.add(Box.createVerticalStrut(18));
        brand.add(Style.subtitle("Un solo spazio per dare forma ai tuoi obiettivi finanziari."));

        JPanel card=Style.cardPanel();
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(32,36,32,36));
        JLabel title=Style.heading("Crea il tuo account");
        title.setFont(new Font("Segoe UI",Font.BOLD,26));
        JLabel sub=Style.subtitle("Bastano pochi dati per iniziare");
        usernameLabel=Style.fieldLabel("NOME UTENTE");
        mailLabel=Style.fieldLabel("INDIRIZZO EMAIL");
        passwordLabel=Style.fieldLabel("PASSWORD");
        txtUsername=new JTextField(); txtMail=new JTextField(); txtPassword=new JPasswordField();
        Style.styleField(txtUsername); Style.styleField(txtMail); Style.styleField(txtPassword);
        Dimension fieldSize=new Dimension(380,46);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));
        txtMail.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));
        registraBtn=Style.newStyledButton("Crea account  →",Color.WHITE,Style.PRIMARY,15);
        registraBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registraBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        registraBtn.addActionListener(e->actionButtonRegistrati());
        tornaLoginBtn=Style.newStyledButton("Ho già un account",Color.WHITE,new Color(49,60,86),14);
        tornaLoginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        tornaLoginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        tornaLoginBtn.addActionListener(e->actionButtonTornaLogin());
        card.add(title); card.add(Box.createVerticalStrut(7)); card.add(sub);
        card.add(Box.createVerticalStrut(24)); card.add(usernameLabel); card.add(Box.createVerticalStrut(7)); card.add(txtUsername);
        card.add(Box.createVerticalStrut(16)); card.add(mailLabel); card.add(Box.createVerticalStrut(7)); card.add(txtMail);
        card.add(Box.createVerticalStrut(16)); card.add(passwordLabel); card.add(Box.createVerticalStrut(7)); card.add(txtPassword);
        card.add(Box.createVerticalStrut(24)); card.add(registraBtn); card.add(Box.createVerticalStrut(10)); card.add(tornaLoginBtn);
        JPanel layout=new JPanel(new GridBagLayout()); layout.setOpaque(false);
        GridBagConstraints a=new GridBagConstraints(); a.gridx=0;a.gridy=0;a.weightx=1;a.weighty=1;a.fill=GridBagConstraints.BOTH;a.insets=new Insets(0,0,0,50);layout.add(brand,a);
        GridBagConstraints b=new GridBagConstraints(); b.gridx=1;b.gridy=0;b.weightx=.85;b.fill=GridBagConstraints.HORIZONTAL;b.insets=new Insets(0,15,0,0);layout.add(card,b);
        mainPanel.add(layout,BorderLayout.CENTER); add(mainPanel);
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