package com.moneyboard.View;

import com.moneyboard.API.TransazioneAPI;
import com.moneyboard.Model.CreateTransactionRequest;
import com.moneyboard.Model.SezioneRisparmio;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;
import com.moneyboard.Style.Style;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddTransazioneArea extends JFrame {

    private JLabel titleLabel, titoloLabel, descrizioneLabel, importoLabel, entrataLabel, dataLabel;
    private JTextField txtTitolo, txtImporto, txtData;
    private JTextArea txtDescrizione;
    private JCheckBox entrataCheck;
    private JButton aggiungiBtn, annullaBtn;
    private JPanel mainPanel, formPanel, buttonPanel;

    private final SezioneRisparmio sezioneSelezionata;
    private final TransazioneAPI transazioneAPI;

    public AddTransazioneArea(SezioneRisparmio sezioneSelezionata) {
        this.sezioneSelezionata = sezioneSelezionata;
        this.transazioneAPI = new TransazioneAPI();

        setTitle("MoneyBoard2.0 - Aggiungi Transazione");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 820);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(680, 700));
        setResizable(true);

        __initComponent();
        Style.applyTheme(this);

        setVisible(true);
    }

    private void __initComponent() {
        mainPanel = Style.backgroundPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(28, 36, 24, 36));

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel eyebrow = Style.subtitle("NUOVO MOVIMENTO  /  " + sezioneSelezionata.getNome().toUpperCase());
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel = Style.heading("Aggiungi transazione");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        header.add(eyebrow);
        header.add(Box.createVerticalStrut(8));
        header.add(titleLabel);
        header.add(Box.createVerticalStrut(20));
        mainPanel.add(header, BorderLayout.NORTH);

        // Form card: ogni campo ha una propria area e dimensioni stabili.
        formPanel = Style.cardPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER, 1, true),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)));

        titoloLabel = Style.fieldLabel("TITOLO");
        txtTitolo = new JTextField();
        Style.styleField(txtTitolo);
        txtTitolo.setPreferredSize(new Dimension(500, 48));
        txtTitolo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        formPanel.add(createFieldGroup(titoloLabel, txtTitolo));

        descrizioneLabel = Style.fieldLabel("DESCRIZIONE");
        txtDescrizione = new JTextArea(4, 20);
        Style.styleField(txtDescrizione);
        txtDescrizione.setLineWrap(true);
        txtDescrizione.setWrapStyleWord(true);
        txtDescrizione.setMargin(new Insets(10, 12, 10, 12));
        JScrollPane descrizioneScroll = new JScrollPane(txtDescrizione);
        descrizioneScroll.setPreferredSize(new Dimension(500, 112));
        descrizioneScroll.setMinimumSize(new Dimension(250, 112));
        descrizioneScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));
        descrizioneScroll.setBorder(BorderFactory.createLineBorder(Style.BORDER, 1, true));
        descrizioneScroll.getViewport().setBackground(Style.SURFACE_ALT);
        descrizioneScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        formPanel.add(createFieldGroup(descrizioneLabel, descrizioneScroll));

        importoLabel = Style.fieldLabel("IMPORTO (€)");
        txtImporto = new JTextField();
        Style.styleField(txtImporto);
        txtImporto.setPreferredSize(new Dimension(500, 48));
        txtImporto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        formPanel.add(createFieldGroup(importoLabel, txtImporto));

        entrataLabel = Style.fieldLabel("TIPO DI MOVIMENTO");
        entrataCheck = new JCheckBox("È un'entrata");
        entrataCheck.setOpaque(false);
        entrataCheck.setForeground(Style.SUCCESS);
        entrataCheck.setFont(new Font("Segoe UI", Font.BOLD, 14));
        entrataCheck.setFocusPainted(false);
        entrataCheck.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        entrataCheck.setIconTextGap(12);
        entrataCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(createFieldGroup(entrataLabel, entrataCheck));

        dataLabel = Style.fieldLabel("DATA");
        txtData = new JTextField(LocalDate.now().toString());
        Style.styleField(txtData);
        txtData.setEditable(false);
        txtData.setForeground(Style.MUTED);
        txtData.setPreferredSize(new Dimension(500, 48));
        txtData.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        formPanel.add(createFieldGroup(dataLabel, txtData));

        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(BorderFactory.createEmptyBorder());
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formScroll.getVerticalScrollBar().setUnitIncrement(18);
        formScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(formScroll, BorderLayout.CENTER);

        // Footer sempre visibile, separato dall'area scorrevole.
        buttonPanel = new JPanel(new GridLayout(1, 2, 14, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        annullaBtn = Style.newStyledButton("Annulla", Color.WHITE, new Color(49, 60, 86), 14);
        aggiungiBtn = Style.newStyledButton("Salva movimento  →", Color.WHITE, Style.SUCCESS, 14);
        annullaBtn.setPreferredSize(new Dimension(0, 54));
        aggiungiBtn.setPreferredSize(new Dimension(0, 54));
        annullaBtn.addActionListener(e -> actionAnnulla());
        aggiungiBtn.addActionListener(e -> actionAggiungi());
        buttonPanel.add(annullaBtn);
        buttonPanel.add(aggiungiBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFieldGroup(JLabel label, JComponent field) {
        JPanel group = new JPanel();
        group.setOpaque(false);
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        group.add(label);
        group.add(Box.createVerticalStrut(9));
        group.add(field);
        group.add(Box.createVerticalStrut(19));
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        return group;
    }

    private void actionAggiungi() {
        String titolo = txtTitolo.getText().trim();
        String descrizione = txtDescrizione.getText().trim();
        String importoText = txtImporto.getText().trim();
        String dataText = txtData.getText().trim();
        boolean isEntrata = entrataCheck.isSelected();

        if (titolo.isBlank() || importoText.isBlank() || dataText.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Compila tutti i campi obbligatori.",
                    "Campi mancanti",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Double importo;
        LocalDate data;

        try {
            importo = Double.parseDouble(importoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "L'importo deve essere un numero valido.",
                    "Valore non valido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (importo <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "L'importo deve essere maggiore di zero.",
                    "Valore non valido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            data = LocalDate.parse(dataText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "La data non è valida.",
                    "Data non valida",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Utente utenteLoggato = UserSession.getInstance().getUtente();

        if (utenteLoggato == null || utenteLoggato.getId() == null) {
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

        if (sezioneSelezionata == null || sezioneSelezionata.getId() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sezione non valida.",
                    "Errore sezione",
                    JOptionPane.ERROR_MESSAGE
            );
            new SezioniArea();
            dispose();
            return;
        }

        setButtonsEnabled(false);

        new Thread(() -> executeCreateTransazione(
                titolo,
                descrizione,
                importo,
                data.toString(),
                isEntrata,
                sezioneSelezionata
        )).start();
    }

    private void executeCreateTransazione(String titolo,
                                          String descrizione,
                                          Double importo,
                                          String data,
                                          boolean isEntrata,
                                          SezioneRisparmio sezione) {
        try {
            CreateTransactionRequest request = new CreateTransactionRequest(
                    titolo,
                    descrizione,
                    data,
                    importo,
                    isEntrata,
                    sezione.getNome()
            );

            transazioneAPI.create(request);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        this,
                        "Transazione aggiunta con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new TransazioniArea(sezioneSelezionata);
                dispose();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Errore durante l'aggiunta della transazione.\n" + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            ));
        } finally {
            SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
        }
    }

    private void actionAnnulla() {
        dispose();
    }

    private void setButtonsEnabled(boolean enabled) {
        aggiungiBtn.setEnabled(enabled);
        annullaBtn.setEnabled(enabled);
    }
}