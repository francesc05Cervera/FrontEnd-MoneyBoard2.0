package com.moneyboard.View;

import com.moneyboard.Style.Style;

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
        setSize(1120, 760);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 650));
        setResizable(true);

        __initComponent();
        Style.applyTheme(this);
        loadTransazioni();

        setVisible(true);
    }

    private void __initComponent() {
        mainPanel=Style.backgroundPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(34,38,30,38));
        JPanel top=new JPanel(new BorderLayout()); top.setOpaque(false);
        JPanel titles=new JPanel(); titles.setOpaque(false); titles.setLayout(new BoxLayout(titles,BoxLayout.Y_AXIS));
        JLabel eyebrow=Style.subtitle("AREA OPERATIVA  /  MOVIMENTI");
        JLabel title=Style.heading("Transazioni");
        JLabel sub=Style.subtitle("Movimenti registrati nella sezione: "+sezioneSelezionata.getNome());
        titles.add(eyebrow); titles.add(Box.createVerticalStrut(6)); titles.add(title); titles.add(Box.createVerticalStrut(5)); titles.add(sub);
        top.add(titles,BorderLayout.WEST); mainPanel.add(top,BorderLayout.NORTH);

        String[] colonne={"TITOLO","DESCRIZIONE","IMPORTO","DATA","TIPO"};
        tableModel=new DefaultTableModel(colonne,0){@Override public boolean isCellEditable(int row,int column){return false;}};
        transTable=new JTable(tableModel); transTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Style.styleTable(transTable);
        transTable.getColumnModel().getColumn(0).setPreferredWidth(170);
        transTable.getColumnModel().getColumn(1).setPreferredWidth(320);
        transTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        transTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        transTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        JScrollPane scroll=new JScrollPane(transTable); scroll.setBorder(BorderFactory.createEmptyBorder()); scroll.getViewport().setBackground(Style.SURFACE);
        tablePanel=Style.cardPanel(new BorderLayout()); tablePanel.add(scroll,BorderLayout.CENTER);
        mainPanel.add(tablePanel,BorderLayout.CENTER);

        buttonPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0)); buttonPanel.setOpaque(false);
        eliminaBtn=Style.newStyledButton("Elimina movimento",Color.WHITE,Style.DANGER,13);
        eliminaBtn.setEnabled(false); eliminaBtn.addActionListener(e->actionEliminaTransazione());
        indietroBtn=Style.newStyledButton("←  Torna alle sezioni",Color.WHITE,new Color(49,60,86),13);
        indietroBtn.addActionListener(e->actionIndietro());
        buttonPanel.add(indietroBtn); buttonPanel.add(eliminaBtn);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);
        add(mainPanel);
        transTable.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting()) eliminaBtn.setEnabled(transTable.getSelectedRow()!=-1);});
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

        new Thread(() -> executeLoadTransazioni(sezioneSelezionata.getId())).start();
    }

    private void executeLoadTransazioni(Long sezioneId) {
        try {
            Transazione[] transazioni = transazioneAPI.getBySezioneId(sezioneId);

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