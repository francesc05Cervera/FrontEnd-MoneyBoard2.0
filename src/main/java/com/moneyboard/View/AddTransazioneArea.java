package com.moneyboard.View;

import com.moneyboard.API.TransazioneAPI;
import com.moneyboard.Model.CreateTransactionRequest;
import com.moneyboard.Model.SezioneRisparmio;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;
import com.moneyboard.style.Style;
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
        setSize(550, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(620, 620));
        setResizable(true);

        __initComponent();
        Style.applyTheme(this);

        setVisible(true);
    }

    private void __initComponent() {
        mainPanel=Style.backgroundPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30,38,28,38));
        JPanel header=new JPanel(); header.setOpaque(false); header.setLayout(new BoxLayout(header,BoxLayout.Y_AXIS));
        JLabel eyebrow=Style.subtitle("NUOVO MOVIMENTO  /  "+sezioneSelezionata.getNome().toUpperCase());
        titleLabel=Style.heading("Aggiungi transazione"); titleLabel.setFont(new Font("Segoe UI",Font.BOLD,27));
        header.add(eyebrow); header.add(Box.createVerticalStrut(6)); header.add(titleLabel);
        mainPanel.add(header,BorderLayout.NORTH);

        formPanel=Style.cardPanel(); formPanel.setLayout(new GridBagLayout());
        GridBagConstraints g=new GridBagConstraints(); g.gridx=0;g.weightx=1;g.fill=GridBagConstraints.HORIZONTAL;g.insets=new Insets(7,10,7,10);
        titoloLabel=Style.fieldLabel("TITOLO"); txtTitolo=new JTextField(); Style.styleField(txtTitolo);
        descrizioneLabel=Style.fieldLabel("DESCRIZIONE"); txtDescrizione=new JTextArea(3,20); Style.styleField(txtDescrizione); txtDescrizione.setLineWrap(true);txtDescrizione.setWrapStyleWord(true);
        JScrollPane descrizioneScroll=new JScrollPane(txtDescrizione); descrizioneScroll.setBorder(BorderFactory.createLineBorder(Style.BORDER, 1, true)); descrizioneScroll.getViewport().setBackground(Style.SURFACE_ALT);
        importoLabel=Style.fieldLabel("IMPORTO  (€)"); txtImporto=new JTextField(); Style.styleField(txtImporto);
        entrataLabel=Style.fieldLabel("TIPO DI MOVIMENTO"); entrataCheck=new JCheckBox("  È un'entrata"); entrataCheck.setForeground(Style.SUCCESS);entrataCheck.setBackground(Style.SURFACE);entrataCheck.setFont(Style.REGULAR);
        dataLabel=Style.fieldLabel("DATA"); txtData=new JTextField(LocalDate.now().toString()); Style.styleField(txtData); txtData.setEditable(false);txtData.setForeground(Style.MUTED);
        int y=0;
        g.gridy=y++;formPanel.add(titoloLabel,g);g.gridy=y++;formPanel.add(txtTitolo,g);
        g.gridy=y++;formPanel.add(descrizioneLabel,g);g.gridy=y++;formPanel.add(descrizioneScroll,g);
        g.gridy=y++;formPanel.add(importoLabel,g);g.gridy=y++;formPanel.add(txtImporto,g);
        g.gridy=y++;formPanel.add(entrataLabel,g);g.gridy=y++;formPanel.add(entrataCheck,g);
        g.gridy=y++;formPanel.add(dataLabel,g);g.gridy=y++;formPanel.add(txtData,g);
        JPanel center=new JPanel(new GridBagLayout());center.setOpaque(false);
        GridBagConstraints cg=new GridBagConstraints();cg.gridx=0;cg.gridy=0;cg.weightx=1;cg.fill=GridBagConstraints.HORIZONTAL;cg.insets=new Insets(15,0,15,0);
        center.add(formPanel,cg);mainPanel.add(center,BorderLayout.CENTER);

        buttonPanel=new JPanel(new GridLayout(1,2,12,0));buttonPanel.setOpaque(false);
        aggiungiBtn=Style.newStyledButton("Salva movimento  →",Color.WHITE,Style.SUCCESS,14);aggiungiBtn.addActionListener(e->actionAggiungi());
        annullaBtn=Style.newStyledButton("Annulla",Color.WHITE,new Color(49,60,86),14);annullaBtn.addActionListener(e->actionAnnulla());
        buttonPanel.add(annullaBtn);buttonPanel.add(aggiungiBtn);mainPanel.add(buttonPanel,BorderLayout.SOUTH);
        add(mainPanel);
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