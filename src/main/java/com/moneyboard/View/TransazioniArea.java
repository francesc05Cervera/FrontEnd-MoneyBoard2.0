package com.moneyboard.View;

import com.moneyboard.API.TransazioneAPI;
import com.moneyboard.Model.SezioneRisparmio;
import com.moneyboard.Model.Transazione;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;

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

    private final SezioneRisparmio sezioneSelezionata;
    private final TransazioneAPI transazioneAPI;
    private Transazione[] transazioniCorrenti;

    public TransazioniArea(SezioneRisparmio sezioneSelezionata) {
        this.sezioneSelezionata = sezioneSelezionata;
        this.transazioneAPI = new TransazioneAPI();

        setTitle("MoneyBoard2.0 - Transazioni");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        __initComponent();
        loadTransazioni();

        setVisible(true);
    }

    private void __initComponent() {

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Tutte le transazioni - " + sezioneSelezionata.getNome(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        String[] colonne = {"Titolo", "Descrizione", "Importo", "Data", "Tipo"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transTable = new JTable(tableModel);
        transTable.setFont(new Font("Arial", Font.PLAIN, 16));
        transTable.setRowHeight(28);
        transTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        transTable.setOpaque(false);
        ((DefaultTableCellRenderer) transTable.getDefaultRenderer(Object.class)).setOpaque(false);

        JTableHeader header = transTable.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(255, 255, 255, 0));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(transTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        eliminaBtn = new JButton("Elimina Transazione");
        eliminaBtn.setFont(new Font("Arial", Font.BOLD, 18));
        eliminaBtn.setBackground(new Color(220, 53, 69));
        eliminaBtn.setForeground(Color.WHITE);
        eliminaBtn.setEnabled(false);
        eliminaBtn.addActionListener(e -> actionEliminaTransazione());

        indietroBtn = new JButton("Indietro");
        indietroBtn.setFont(new Font("Arial", Font.BOLD, 18));
        indietroBtn.setBackground(new Color(70, 70, 70));
        indietroBtn.setForeground(Color.WHITE);
        indietroBtn.addActionListener(e -> actionIndietro());

        buttonPanel.add(eliminaBtn);
        buttonPanel.add(indietroBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        transTable.getSelectionModel().addListSelectionListener(e -> {
            eliminaBtn.setEnabled(transTable.getSelectedRow() != -1);
        });
    }

    private void loadTransazioni() {
        Utente utente = UserSession.getInstance().getUtente();

        if (utente == null || utente.getId() == null || sezioneSelezionata == null || sezioneSelezionata.getId() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Dati sessione o sezione non validi.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            new SezioniArea();
            dispose();
            return;
        }

        setButtonsEnabled(false);

        new Thread(() -> executeLoadTransazioni(utente.getId(), sezioneSelezionata.getId())).start();
    }

    private void executeLoadTransazioni(Long utenteId, Long sezioneId) {
        try {
            Transazione[] transazioni = transazioneAPI.getByUtenteIdAndSezioneId(utenteId, sezioneId);

            SwingUtilities.invokeLater(() -> updateTable(transazioni));

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Errore caricamento transazioni:\n" + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void updateTable(Transazione[] transazioni) {
        this.transazioniCorrenti = transazioni;
        tableModel.setRowCount(0);

        if (transazioni == null || transazioni.length == 0) {
            return;
        }

        for (Transazione transazione : transazioni) {
            Object[] row = {
                    transazione.getTitolo(),
                    transazione.getDescrizione(),
                    transazione.getImporto(),
                    transazione.getData(),
                    Boolean.TRUE.equals(transazione.getIsEntrata()) ? "Entrata" : "Uscita"
            };
            tableModel.addRow(row);
        }
    }

    private void actionEliminaTransazione() {
        int selectedRow = transTable.getSelectedRow();

        if (selectedRow == -1 || transazioniCorrenti == null || selectedRow >= transazioniCorrenti.length) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleziona una transazione da eliminare.",
                    "Nessuna selezione",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Transazione transazione = transazioniCorrenti[selectedRow];

        int conferma = JOptionPane.showConfirmDialog(
                this,
                "Vuoi davvero eliminare la transazione \"" + transazione.getTitolo() + "\"?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (conferma != JOptionPane.YES_OPTION) {
            return;
        }

        setButtonsEnabled(false);

        new Thread(() -> executeDeleteTransazione(transazione.getId())).start();
    }

    private void executeDeleteTransazione(Long id) {
        try {
            transazioneAPI.deleteById(id);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        this,
                        "Transazione eliminata con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadTransazioni();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Errore durante l'eliminazione della transazione.\n" + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void actionIndietro() {
        new SezioniArea();
        dispose();
    }

    private void setButtonsEnabled(boolean enabled) {
        indietroBtn.setEnabled(enabled);
        if (enabled) {
            eliminaBtn.setEnabled(transTable.getSelectedRow() != -1);
        } else {
            eliminaBtn.setEnabled(false);
        }
    }
}