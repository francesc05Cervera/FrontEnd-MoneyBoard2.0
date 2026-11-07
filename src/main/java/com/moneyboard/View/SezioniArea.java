package com.moneyboard.View;

import com.moneyboard.API.SezioneAPI;
import com.moneyboard.Model.DeleteSezioneRequest;
import com.moneyboard.Model.SezioneRisparmio;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;
import com.moneyboard.Style.Style;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class SezioniArea extends JFrame {

    private JTable sezioniTable;
    private DefaultTableModel tableModel;
    private JPanel mainPanel, tablePanel, buttonPanel;
    private JButton logoutBtn, refreshBtn, aggiungiTransBtn, eliminaBtn, vediTransBtn, creaSezioneBtn;

    private final SezioneAPI sezioneAPI;
    private SezioneRisparmio[] sezioniCorrenti;

    public SezioniArea() {
        this.sezioneAPI = new SezioneAPI();

        setTitle("MoneyBoard2.0 - Sezioni");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        __initComponent();
        loadSezioni();

        setVisible(true);
    }

    private void __initComponent() {

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Le tue sezioni", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        String[] colonne = {"Nome Sezione", "Importo"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        sezioniTable = new JTable(tableModel);
        sezioniTable.setFont(new Font("Arial", Font.PLAIN, 16));
        sezioniTable.setRowHeight(28);
        sezioniTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sezioniTable.setOpaque(false);
        ((DefaultTableCellRenderer) sezioniTable.getDefaultRenderer(Object.class)).setOpaque(false);

        JTableHeader header = sezioniTable.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(255, 255, 255, 0));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(sezioniTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        logoutBtn = Style.newStyledButton("Logout", Color.WHITE, new Color(220, 53, 69) );
        logoutBtn.addActionListener(e -> actionLogout());

        refreshBtn = Style.newStyledButton("Aggiorna Tabella", Color.WHITE, new Color(0, 102, 204));
        refreshBtn.addActionListener(e -> actionRefreshBtn());
        
        aggiungiTransBtn = Style.newStyledButton("Aggiungi Transazione", Color.WHITE, new Color(40, 167, 69));
        aggiungiTransBtn.addActionListener(e -> actionAggiungiTransazione());

        eliminaBtn = Style.newStyledButton("Elimina Sezione", Color.WHITE, new Color(255, 140, 0));
        eliminaBtn.addActionListener(e -> actionEliminaSezione());

        vediTransBtn = Style.newStyledButton("Vedi Transazioni", Color.BLACK, new Color(255,193,7));
        vediTransBtn.addActionListener(e -> actionVediTransazioni());

        creaSezioneBtn = Style.newStyledButton("Crea Sezione", Color.WHITE, new Color(138,43,226));
        creaSezioneBtn.addActionListener(e -> actionButtonCreaSezione());
        
        buttonPanel.add(logoutBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(aggiungiTransBtn);
        buttonPanel.add(eliminaBtn);
        buttonPanel.add(vediTransBtn);
        buttonPanel.add(creaSezioneBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private void actionButtonCreaSezione()
    {
    	new CreateSezioneArea();
    }
    
    private void actionRefreshBtn()
    {
    	loadSezioni();
    }

    private void loadSezioni() {
        Utente utente = UserSession.getInstance().getUtente();

        if (utente == null || utente.getId() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Utente non loggato.",
                    "Errore sessione",
                    JOptionPane.ERROR_MESSAGE
            );
            new LogInArea();
            dispose();
            return;
        }

        setButtonsEnabled(false);

        new Thread(this::executeLoadSezioni).start();
    }

    private void executeLoadSezioni() {
        try {
            SezioneRisparmio[] sezioni = sezioneAPI.getSezioni();

            SwingUtilities.invokeLater(() -> updateTable(sezioni));

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Errore caricamento sezioni:\n" + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void updateTable(SezioneRisparmio[] sezioni) {
        this.sezioniCorrenti = sezioni;
        tableModel.setRowCount(0);

        if (sezioni == null || sezioni.length == 0) {
            return;
        }

        for (SezioneRisparmio sezione : sezioni) {
            Object[] row = {
                    sezione.getNome(),
                    sezione.getSaldo() + " €"
            };
            tableModel.addRow(row);
        }
    }

    private void actionAggiungiTransazione() {
        int selectedRow = sezioniTable.getSelectedRow();

        if (selectedRow == -1 || sezioniCorrenti == null || selectedRow >= sezioniCorrenti.length) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleziona una sezione.",
                    "Nessuna selezione",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        SezioneRisparmio sezioneSelezionata = sezioniCorrenti[selectedRow];
        new AddTransazioneArea(sezioneSelezionata);
        
    }

    private void actionVediTransazioni() {
        int selectedRow = sezioniTable.getSelectedRow();

        if (selectedRow == -1 || sezioniCorrenti == null || selectedRow >= sezioniCorrenti.length) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleziona una sezione.",
                    "Nessuna selezione",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        SezioneRisparmio sezioneSelezionata = sezioniCorrenti[selectedRow];
        new TransazioniArea(sezioneSelezionata);
        
    }

    private void actionEliminaSezione() {
        int selectedRow = sezioniTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleziona una sezione da eliminare.",
                    "Nessuna selezione",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String nomeSezione = tableModel.getValueAt(selectedRow, 0).toString().trim();

        int conferma = JOptionPane.showConfirmDialog(
                this,
                "Vuoi davvero eliminare la sezione \"" + nomeSezione + "\"?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (conferma != JOptionPane.YES_OPTION) {
            return;
        }

        setButtonsEnabled(false);

        new Thread(() -> executeDeleteSezione(nomeSezione)).start();
    }

    private void executeDeleteSezione(String nomeSezione) {
        try {
            DeleteSezioneRequest request = new DeleteSezioneRequest(nomeSezione);
            sezioneAPI.delete(request);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        this,
                        "Sezione eliminata con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadSezioni();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Errore durante l'eliminazione della sezione \"" + nomeSezione + "\".\n" + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void actionLogout() {
        UserSession.getInstance().clear();
        new LogInArea();
        dispose();
    }

    private void setButtonsEnabled(boolean enabled) {
        logoutBtn.setEnabled(enabled);
        aggiungiTransBtn.setEnabled(enabled);
        eliminaBtn.setEnabled(enabled);
        vediTransBtn.setEnabled(enabled);
        creaSezioneBtn.setEnabled(enabled);
    }
}