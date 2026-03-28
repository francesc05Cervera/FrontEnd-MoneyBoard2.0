package com.moneyboard.View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class TransazioniArea extends JFrame {

    private JTable transTable;
    private DefaultTableModel tableModel;
    private JPanel mainPanel, tablePanel, buttonPanel;
    private JButton eliminaBtn, indietroBtn;

    public TransazioniArea() {
        setTitle("MoneyBoard2.0 - Transazioni");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 750);
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
        JLabel title = new JLabel("Tutte le transazioni", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Tabella
        String[] colonne = {"Titolo", "Descrizione", "Importo"};
        tableModel = new DefaultTableModel(colonne, 0);

        transTable = new JTable(tableModel);
        transTable.setFont(new Font("Arial", Font.PLAIN, 16));
        transTable.setRowHeight(28);

        // Celle trasparenti
        transTable.setOpaque(false);
        ((DefaultTableCellRenderer) transTable.getDefaultRenderer(Object.class)).setOpaque(false);

        // Header trasparente
        JTableHeader header = transTable.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(255, 255, 255, 0));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setOpaque(false);

        // ScrollPane trasparente
        JScrollPane scrollPane = new JScrollPane(transTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Pannello pulsanti
        buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        eliminaBtn = new JButton("Elimina Transazione");
        eliminaBtn.setFont(new Font("Arial", Font.BOLD, 18));
        eliminaBtn.setBackground(new Color(220, 53, 69)); // rosso
        eliminaBtn.setForeground(Color.WHITE);
        eliminaBtn.setEnabled(false); // attivo solo se selezionata una riga

        indietroBtn = new JButton("Indietro");
        indietroBtn.setFont(new Font("Arial", Font.BOLD, 18));
        indietroBtn.setBackground(new Color(70, 70, 70)); // grigio scuro elegante
        indietroBtn.setForeground(Color.WHITE);

        buttonPanel.add(eliminaBtn);
        buttonPanel.add(indietroBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Listener per attivare il pulsante elimina
        transTable.getSelectionModel().addListSelectionListener(e -> {
            eliminaBtn.setEnabled(transTable.getSelectedRow() != -1);
        });
    }
}