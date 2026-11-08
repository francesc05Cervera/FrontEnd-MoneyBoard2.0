package com.moneyboard.View;

import com.moneyboard.API.SezioneAPI;
import com.moneyboard.Model.DeleteSezioneRequest;
import com.moneyboard.Model.SezioneRisparmio;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;
import com.moneyboard.Style.Style;
import javax.swing.*;
import java.awt.*;

public class SezioniArea extends JFrame {

    private JPanel mainPanel, cardsPanel, cardsContainer, buttonPanel;
    private int selectedSezioneIndex = -1;
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
        JPanel content=new JPanel(new BorderLayout(0,22)); content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(34,36,30,36));
        JPanel top=new JPanel(new BorderLayout()); top.setOpaque(false);
        JPanel titleBox=new JPanel(); titleBox.setOpaque(false); titleBox.setLayout(new BoxLayout(titleBox,BoxLayout.Y_AXIS));
        JLabel eyebrow=Style.subtitle("IL TUO SPAZIO FINANZIARIO  /  OVERVIEW");
        JLabel title=Style.heading("Panoramica");
        titleBox.add(eyebrow); titleBox.add(Box.createVerticalStrut(7)); titleBox.add(title);
        refreshBtn=Style.newStyledButton("Aggiorna  ↻",Color.WHITE,new Color(36, 139, 190),13);
        refreshBtn.addActionListener(e->actionRefreshBtn());

        logoutBtn = Style.newStyledButton("Esci  ⏻", Color.WHITE, new Color(210, 55, 75), 13);
        logoutBtn.addActionListener(e -> actionLogout());
        JPanel topActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        topActions.setOpaque(false);
        topActions.add(refreshBtn);
        topActions.add(logoutBtn);
        top.add(titleBox,BorderLayout.WEST);
        top.add(topActions,BorderLayout.EAST);
        content.add(top,BorderLayout.NORTH);

        JPanel body=new JPanel(new BorderLayout(0,18)); body.setOpaque(false);
        JPanel welcome=Style.cardPanel(new BorderLayout(10,5));
        JLabel welcomeTitle=Style.heading("I tuoi obiettivi, a portata di mano");
        welcomeTitle.setFont(new Font("Segoe UI",Font.BOLD,20));
        JLabel welcomeSub=Style.subtitle("Seleziona una sezione per gestire le transazioni o crea un nuovo obiettivo di risparmio.");
        welcome.add(welcomeTitle,BorderLayout.NORTH); welcome.add(welcomeSub,BorderLayout.CENTER);
        body.add(welcome,BorderLayout.NORTH);

        cardsContainer = Style.cardPanel(new BorderLayout(0, 16));
        JPanel cardsHeader = new JPanel(new BorderLayout());
        cardsHeader.setOpaque(false);
        JPanel cardsHeading = new JPanel();
        cardsHeading.setOpaque(false);
        cardsHeading.setLayout(new BoxLayout(cardsHeading, BoxLayout.Y_AXIS));
        JLabel cardsTitle = Style.heading("Le tue sezioni");
        cardsTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
        JLabel cardsHint = Style.subtitle("Seleziona una card per gestire il tuo obiettivo");
        cardsHeading.add(cardsTitle);
        cardsHeading.add(Box.createVerticalStrut(5));
        cardsHeading.add(cardsHint);
        cardsHeader.add(cardsHeading, BorderLayout.WEST);
        cardsContainer.add(cardsHeader, BorderLayout.NORTH);
        cardsPanel = new JPanel(new GridLayout(0, 3, 16, 16));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 8, 2));
        JScrollPane cardsScroll = new JScrollPane(cardsPanel);
        cardsScroll.setBorder(BorderFactory.createEmptyBorder());
        cardsScroll.setOpaque(false);
        cardsScroll.getViewport().setOpaque(false);
        cardsScroll.getVerticalScrollBar().setUnitIncrement(18);
        cardsScroll.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0));
        cardsContainer.add(cardsScroll, BorderLayout.CENTER);
        body.add(cardsContainer, BorderLayout.CENTER);

        buttonPanel=new JPanel(new GridLayout(1,4,10,0)); buttonPanel.setOpaque(false);
        creaSezioneBtn=Style.newStyledButton("＋  Nuova sezione",Color.WHITE,new Color(124, 108, 255),13);
        creaSezioneBtn.addActionListener(e->actionButtonCreaSezione());
        aggiungiTransBtn=Style.newStyledButton("＋  Transazione",Color.WHITE,new Color(32, 166, 126),13);
        aggiungiTransBtn.addActionListener(e->actionAggiungiTransazione());
        vediTransBtn=Style.newStyledButton("▤  Movimenti",Color.WHITE,new Color(36, 139, 190),13);
        vediTransBtn.addActionListener(e->actionVediTransazioni());
        eliminaBtn=Style.newStyledButton("Elimina sezione  ×",Color.WHITE,new Color(190, 48, 67),13);
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

            SwingUtilities.invokeLater(() -> updateCards(sezioni));

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

    private void updateCards(SezioneRisparmio[] sezioni) {
        this.sezioniCorrenti = sezioni;
        selectedSezioneIndex = -1;
        cardsPanel.removeAll();
        if (sezioni == null || sezioni.length == 0) {
            cardsPanel.setLayout(new BorderLayout());
            JLabel empty = new JLabel("<html><div style='text-align:center;'>"
                    + "<span style='font-size:22px;'>Nessuna sezione ancora</span><br><br>"
                    + "Crea il tuo primo obiettivo di risparmio per iniziare."
                    + "</div></html>", SwingConstants.CENTER);
            empty.setForeground(Style.MUTED);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            cardsPanel.add(empty, BorderLayout.CENTER);
        } else {
            cardsPanel.setLayout(new GridLayout(0, 3, 16, 16));
            for (int i = 0; i < sezioni.length; i++) cardsPanel.add(createSezioneCard(sezioni[i], i));
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
        updateSelectionButtons();
    }

    private JPanel createSezioneCard(SezioneRisparmio sezione, int index) {
        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(Style.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 18, 20)));
        card.setPreferredSize(new Dimension(220, 165));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel topLine = new JPanel(new BorderLayout());
        topLine.setOpaque(false);
        JLabel symbol = new JLabel("€", SwingConstants.CENTER);
        symbol.setOpaque(true);
        symbol.setBackground(new Color(53, 48, 103));
        symbol.setForeground(new Color(177, 164, 255));
        symbol.setFont(new Font("Segoe UI", Font.BOLD, 19));
        symbol.setPreferredSize(new Dimension(42, 42));
        symbol.setBorder(BorderFactory.createLineBorder(new Color(82, 73, 145), 1, true));
        JLabel tag = new JLabel("OBIETTIVO");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tag.setForeground(Style.MUTED);
        topLine.add(symbol, BorderLayout.WEST);
        topLine.add(tag, BorderLayout.EAST);
        JPanel details = new JPanel(new BorderLayout());
        details.setOpaque(false);
        JLabel name = new JLabel(sezione.getNome());
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(Style.TEXT);
        name.setToolTipText(sezione.getNome());

        // Il saldo è nel footer della card: resta visibile anche quando
        // il layout della griglia riduce lo spazio verticale del contenuto.
        Double saldo = sezione.getSaldo();
        String saldoText = saldo == null ? "—" : String.format(java.util.Locale.ITALY, "%,.2f €", saldo);
        JLabel balance = new JLabel(saldoText);
        balance.setFont(new Font("Segoe UI", Font.BOLD, 22));
        balance.setForeground(Style.SUCCESS);

        JLabel hint = new JLabel("Seleziona  →");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setForeground(Style.MUTED);

        details.add(name, BorderLayout.NORTH);
        JPanel cardFooter = new JPanel(new BorderLayout(8, 0));
        cardFooter.setOpaque(false);
        cardFooter.add(balance, BorderLayout.WEST);
        cardFooter.add(hint, BorderLayout.EAST);
        card.add(topLine, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(cardFooter, BorderLayout.SOUTH);
        java.awt.event.MouseAdapter selector = new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { selectSezione(index); }
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (selectedSezioneIndex != index) card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Style.PRIMARY, 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 18, 20)));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (selectedSezioneIndex != index) card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Style.BORDER, 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 18, 20)));
            }
        };
        card.addMouseListener(selector);
        topLine.addMouseListener(selector); symbol.addMouseListener(selector); tag.addMouseListener(selector);
        details.addMouseListener(selector); name.addMouseListener(selector); balance.addMouseListener(selector);
        cardFooter.addMouseListener(selector); hint.addMouseListener(selector);
        return card;
    }

    private void selectSezione(int index) {
        selectedSezioneIndex = index;
        for (int i = 0; i < cardsPanel.getComponentCount(); i++) {
            if (cardsPanel.getComponent(i) instanceof JPanel) {
                JPanel card = (JPanel) cardsPanel.getComponent(i);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(i == index ? Style.PRIMARY : Style.BORDER,
                                i == index ? 2 : 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 18, 20)));
                card.setBackground(i == index ? new Color(25, 31, 53) : Style.SURFACE);
            }
        }
        updateSelectionButtons();
    }

    private void updateSelectionButtons() {
        boolean selected = selectedSezioneIndex >= 0 && sezioniCorrenti != null
                && selectedSezioneIndex < sezioniCorrenti.length;
        aggiungiTransBtn.setEnabled(selected);
        vediTransBtn.setEnabled(selected);
        eliminaBtn.setEnabled(selected);
    }

    private void actionAggiungiTransazione() {
        int selectedRow = selectedSezioneIndex;

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
        int selectedRow = selectedSezioneIndex;

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
        int selectedRow = selectedSezioneIndex;

        if (selectedRow == -1 || sezioniCorrenti == null || selectedRow >= sezioniCorrenti.length) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleziona una sezione da eliminare.",
                    "Nessuna selezione",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String nomeSezione = sezioniCorrenti[selectedRow].getNome();

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
        creaSezioneBtn.setEnabled(enabled);
        refreshBtn.setEnabled(enabled);
        if (!enabled) {
            aggiungiTransBtn.setEnabled(false);
            eliminaBtn.setEnabled(false);
            vediTransBtn.setEnabled(false);
        } else updateSelectionButtons();
    }
}