package com.moneyboard.View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class SezioniArea extends JFrame {

    private JTable sezioniTable;
    private DefaultTableModel tableModel;
    private JPanel mainPanel, tablePanel, buttonPanel;
    private JButton logoutBtn, modificaBtn, aggiungiTransBtn, eliminaBtn, vediTransBtn, creaSezioneBtn;

    public SezioniArea() {
        setTitle("MoneyBoard2.0 - Sezioni");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
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
        JLabel title = new JLabel("Le tue sezioni", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Tabella
        String[] colonne = {"Nome Sezione", "Importo"};
        tableModel = new DefaultTableModel(colonne, 0);

        sezioniTable = new JTable(tableModel);
        sezioniTable.setFont(new Font("Arial", Font.PLAIN, 16));
        sezioniTable.setRowHeight(28);

        // Celle trasparenti
        sezioniTable.setOpaque(false);
        ((DefaultTableCellRenderer) sezioniTable.getDefaultRenderer(Object.class)).setOpaque(false);

        // Header trasparente
        JTableHeader header = sezioniTable.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(255, 255, 255, 0));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setOpaque(false);

        // ScrollPane trasparente
        JScrollPane scrollPane = new JScrollPane(sezioniTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Pannello pulsanti (2 righe, max 3 per riga)
        buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);

        modificaBtn = new JButton("Modifica Sezione");
        modificaBtn.setFont(new Font("Arial", Font.BOLD, 16));
        modificaBtn.setBackground(new Color(0, 102, 204));
        modificaBtn.setForeground(Color.WHITE);

        aggiungiTransBtn = new JButton("Aggiungi Transazione");
        aggiungiTransBtn.setFont(new Font("Arial", Font.BOLD, 16));
        aggiungiTransBtn.setBackground(new Color(40, 167, 69));
        aggiungiTransBtn.setForeground(Color.WHITE);

        aggiungiTransBtn.addActionListener(e -> { new AddTransazioneArea(); dispose(); });
        
        eliminaBtn = new JButton("Elimina Sezione");
        eliminaBtn.setFont(new Font("Arial", Font.BOLD, 16));
        eliminaBtn.setBackground(new Color(255, 140, 0));
        eliminaBtn.setForeground(Color.WHITE);

        vediTransBtn = new JButton("Vedi Transazioni");
        vediTransBtn.setFont(new Font("Arial", Font.BOLD, 16));
        vediTransBtn.setBackground(new Color(255, 193, 7));
        vediTransBtn.setForeground(Color.BLACK);
        
        vediTransBtn.addActionListener(e -> {new TransazioniArea(); dispose(); });

        // Nuovo bottone: CREA SEZIONE (viola)
        creaSezioneBtn = new JButton("Crea Sezione");
        creaSezioneBtn.setFont(new Font("Arial", Font.BOLD, 16));
        creaSezioneBtn.setBackground(new Color(138, 43, 226)); // viola elegante
        creaSezioneBtn.setForeground(Color.WHITE);
        creaSezioneBtn.addActionListener(e -> { new CreateSezioneArea(); dispose(); });
        
        // Aggiunta bottoni (2 righe)
        buttonPanel.add(logoutBtn);
        //buttonPanel.add(modificaBtn);
        buttonPanel.add(aggiungiTransBtn);
        buttonPanel.add(eliminaBtn);
        buttonPanel.add(vediTransBtn);
        buttonPanel.add(creaSezioneBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}