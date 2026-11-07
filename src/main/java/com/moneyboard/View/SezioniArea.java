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
        setSize(1180, 820);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(980, 700));
        setResizable(true);

        __initComponent();
        Style.applyTheme(this);
        loadSezioni();

        setVisible(true);
    }

    private void __initComponent() {
        mainPanel = Style.backgroundPanel();
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(14,19,32));
        sidebar.setLayout(new BoxLayout(sidebar,BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(28,20,24,20));
        sidebar.setPreferredSize(new Dimension(225,0));
        JLabel logo=new JLabel("◈  MONEYBOARD");
        logo.setFont(new Font("Segoe UI",Font.BOLD,17)); logo.setForeground(Style.PRIMARY);
        JLabel menu=Style.subtitle("WORKSPACE");
        menu.setBorder(BorderFactory.createEmptyBorder(48,8,14,0));
        JLabel active=new JLabel("   ▦   Panoramica");
        active.setOpaque(true); active.setBackground(new Color(47,48,82)); active.setForeground(Color.WHITE);
        active.setFont(new Font("Segoe UI",Font.BOLD,14));
        active.setBorder(BorderFactory.createEmptyBorder(14,10,14,10));
        active.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));
        sidebar.add(logo); sidebar.add(menu); sidebar.add(active);
        sidebar.add(Box.createVerticalGlue());
        JLabel footer=Style.subtitle("PERSONAL FINANCE");
        sidebar.add(footer);
        logoutBtn=Style.newStyledButton("Esci",Color.WHITE,new Color(75,45,63),13);
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT); logoutBtn.addActionListener(e->actionLogout());
        sidebar.add(Box.createVerticalStrut(12)); sidebar.add(logoutBtn);
        mainPanel.add(sidebar,BorderLayout.WEST);

        JPanel content=new JPanel(new BorderLayout(0,22)); content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(34,36,30,36));
        JPanel top=new JPanel(new BorderLayout()); top.setOpaque(false);
        JPanel titleBox=new JPanel(); titleBox.setOpaque(false); titleBox.setLayout(new BoxLayout(titleBox,BoxLayout.Y_AXIS));
        JLabel eyebrow=Style.subtitle("IL TUO SPAZIO FINANZIARIO  /  OVERVIEW");
        JLabel title=Style.heading("Panoramica");
        titleBox.add(eyebrow); titleBox.add(Box.createVerticalStrut(7)); titleBox.add(title);
        refreshBtn=Style.newStyledButton("↻  Aggiorna",Color.WHITE,new Color(49,60,86),13);
        refreshBtn.addActionListener(e->actionRefreshBtn());
        top.add(titleBox,BorderLayout.WEST); top.add(refreshBtn,BorderLayout.EAST);
        content.add(top,BorderLayout.NORTH);

        JPanel body=new JPanel(new BorderLayout(0,18)); body.setOpaque(false);
        JPanel welcome=Style.cardPanel(new BorderLayout(10,5));
        JLabel welcomeTitle=Style.heading("I tuoi obiettivi, a portata di mano");
        welcomeTitle.setFont(new Font("Segoe UI",Font.BOLD,20));
        JLabel welcomeSub=Style.subtitle("Seleziona una sezione per gestire le transazioni o crea un nuovo obiettivo di risparmio.");
        welcome.add(welcomeTitle,BorderLayout.NORTH); welcome.add(welcomeSub,BorderLayout.CENTER);
        body.add(welcome,BorderLayout.NORTH);

        String[] colonne={"SEZIONE","SALDO"};
        tableModel=new DefaultTableModel(colonne,0){@Override public boolean isCellEditable(int row,int column){return false;}};
        sezioniTable=new JTable(tableModel); sezioniTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Style.styleTable(sezioniTable);
        JScrollPane scroll=new JScrollPane(sezioniTable); scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Style.SURFACE);
        tablePanel=Style.cardPanel(new BorderLayout(0,12));
        JLabel tableTitle=new JLabel("Le tue sezioni di risparmio"); tableTitle.setFont(new Font("Segoe UI",Font.BOLD,17)); tableTitle.setForeground(Style.TEXT);
        tablePanel.add(tableTitle,BorderLayout.NORTH); tablePanel.add(scroll,BorderLayout.CENTER);
        body.add(tablePanel,BorderLayout.CENTER);

        buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); buttonPanel.setOpaque(false);
        creaSezioneBtn=Style.newStyledButton("+  Nuova sezione",Color.WHITE,Style.PRIMARY,13);
        creaSezioneBtn.addActionListener(e->actionButtonCreaSezione());
        aggiungiTransBtn=Style.newStyledButton("+  Transazione",Color.WHITE,Style.SUCCESS,13);
        aggiungiTransBtn.addActionListener(e->actionAggiungiTransazione());
        vediTransBtn=Style.newStyledButton("↗  Movimenti",Color.WHITE,new Color(49,60,86),13);
        vediTransBtn.addActionListener(e->actionVediTransazioni());
        eliminaBtn=Style.newStyledButton("Elimina sezione",Color.WHITE,Style.DANGER,13);
        eliminaBtn.addActionListener(e->actionEliminaSezione());
        buttonPanel.add(creaSezioneBtn); buttonPanel.add(aggiungiTransBtn); buttonPanel.add(vediTransBtn); buttonPanel.add(eliminaBtn);
        body.add(buttonPanel,BorderLayout.SOUTH);
        content.add(body,BorderLayout.CENTER);
        mainPanel.add(content,BorderLayout.CENTER);
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