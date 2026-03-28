package com.moneyboard.View;

import javax.swing.*;
import java.awt.*;

public class LogInArea extends JFrame {

    private JLabel mailLabel, passwordLabel;
    private JTextField txtMail;
    private JPasswordField txtPassword;
    private JButton entraBtn, registratiBtn;
    private JPanel mainPanel, formPanel, buttonPanel;

    public LogInArea() {
        setTitle("MoneyBoard2.0 - Login Utente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);

        __initComponent();

        setVisible(true);
    }

    private void __initComponent() {

        // Pannello principale
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        // Titolo
        JLabel title = new JLabel("Accedi al tuo account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Pannello form
        formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridLayout(4, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Label e campi
        mailLabel = new JLabel("E-mail:");
        mailLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtMail = new JTextField();
        txtMail.setFont(new Font("Arial", Font.PLAIN, 16));

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));

        txtMail.setPreferredSize(new Dimension(300, 35));
        txtPassword.setPreferredSize(new Dimension(300, 35));
        
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

        entraBtn = new JButton("Entra");
        entraBtn.setFont(new Font("Arial", Font.BOLD, 18));
        entraBtn.setBackground(new Color(0, 153, 76));
        entraBtn.setForeground(Color.WHITE);
        entraBtn.addActionListener(e -> actionButtonLogIn());
        
        registratiBtn = new JButton("Registrati");
        registratiBtn.setFont(new Font("Arial", Font.BOLD, 18));
        registratiBtn.setBackground(new Color(51, 102, 255));
        registratiBtn.setForeground(Color.WHITE);
        registratiBtn.addActionListener(e -> actionButtonRegistrati());
        
        buttonPanel.add(entraBtn);
        buttonPanel.add(registratiBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private void actionButtonLogIn()
    {
    	//da implementare controller
    	new SezioniArea(); 
    	dispose();
    	
    }
    
    private void actionButtonRegistrati()
    {
    	new RegistrazioneArea(); 
    	dispose(); 
    }
}