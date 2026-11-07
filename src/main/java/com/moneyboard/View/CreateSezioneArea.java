package com.moneyboard.View;

import com.moneyboard.API.SezioneAPI;
import com.moneyboard.Model.SectionCreateRequest;
import com.moneyboard.Model.UserSession;
import com.moneyboard.Model.Utente;
import com.moneyboard.Style.Style;
import javax.swing.*;
import java.awt.*;

public class CreateSezioneArea extends JFrame {

    private JLabel titleLabel, nomeLabel, obiettivoLabel;
    private JTextField txtNome, txtObiettivo;
    private JButton creaBtn, annullaBtn;
    private JPanel mainPanel, formPanel, buttonPanel;

    private final SezioneAPI sezioneAPI;

    public CreateSezioneArea() {
        this.sezioneAPI = new SezioneAPI();

        setTitle("MoneyBoard2.0 - Crea Sezione");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(620, 620));
        setResizable(true);

        __initComponent();
        Style.applyTheme(this);

        setVisible(true);
    }

    private void __initComponent() {
        mainPanel=Style.backgroundPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(34,42,32,42));
        JPanel header=new JPanel(); header.setOpaque(false); header.setLayout(new BoxLayout(header,BoxLayout.Y_AXIS));
        JLabel eyebrow=Style.subtitle("NUOVO OBIETTIVO");
        titleLabel=Style.heading("Crea una sezione");
        titleLabel.setFont(new Font("Segoe UI",Font.BOLD,27));
        header.add(eyebrow); header.add(Box.createVerticalStrut(7)); header.add(titleLabel);
        mainPanel.add(header,BorderLayout.NORTH);

        formPanel=Style.cardPanel(); formPanel.setLayout(new BoxLayout(formPanel,BoxLayout.Y_AXIS));
        nomeLabel=Style.fieldLabel("NOME DELLA SEZIONE");
        txtNome=new JTextField(); Style.styleField(txtNome); txtNome.setMaximumSize(new Dimension(Integer.MAX_VALUE,48));
        obiettivoLabel=Style.fieldLabel("OBIETTIVO DI RISPARMIO  (€)");
        txtObiettivo=new JTextField(); Style.styleField(txtObiettivo); txtObiettivo.setMaximumSize(new Dimension(Integer.MAX_VALUE,48));
        formPanel.add(nomeLabel); formPanel.add(Box.createVerticalStrut(9)); formPanel.add(txtNome);
        formPanel.add(Box.createVerticalStrut(24)); formPanel.add(obiettivoLabel); formPanel.add(Box.createVerticalStrut(9)); formPanel.add(txtObiettivo);
        JPanel center=new JPanel(new GridBagLayout()); center.setOpaque(false);
        GridBagConstraints gbc=new GridBagConstraints(); gbc.gridx=0;gbc.gridy=0;gbc.weightx=1;gbc.fill=GridBagConstraints.HORIZONTAL;gbc.insets=new Insets(25,0,25,0);
        center.add(formPanel,gbc); mainPanel.add(center,BorderLayout.CENTER);

        buttonPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0)); buttonPanel.setOpaque(false);
        creaBtn=Style.newStyledButton("Crea obiettivo  →",Color.WHITE,Style.PRIMARY,14); creaBtn.addActionListener(e->actionCreaSezione());
        annullaBtn=Style.newStyledButton("Annulla",Color.WHITE,new Color(49,60,86),14); annullaBtn.addActionListener(e->actionAnnulla());
        buttonPanel.add(annullaBtn); buttonPanel.add(creaBtn); mainPanel.add(buttonPanel,BorderLayout.SOUTH);
        add(mainPanel);
    }

	private void __createButtonPanel() 
	{
		buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80));

        creaBtn = Style.newStyledButton("Crea", Color.WHITE, new Color(40, 167, 69), 18);
        creaBtn.addActionListener(e -> actionCreaSezione());

        annullaBtn = Style.newStyledButton("Annulla", Color.WHITE, new Color(220, 53, 69), 18);
        annullaBtn.addActionListener(e -> actionAnnulla());

        buttonPanel.add(creaBtn);
        buttonPanel.add(annullaBtn);
	}

    private void actionCreaSezione() {
        String nome = txtNome.getText().trim();
        String obiettivoText = txtObiettivo.getText().trim();

        if (nome.isBlank() || obiettivoText.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Compila tutti i campi.",
                    "Campi mancanti",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Double obiettivo;

        try {
            obiettivo = Double.parseDouble(obiettivoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "L'obiettivo deve essere un numero valido.",
                    "Valore non valido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (obiettivo <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "L'obiettivo deve essere maggiore di zero.",
                    "Valore non valido",
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

        setButtonsEnabled(false);

        Double finalObiettivo = obiettivo;
        new Thread(() -> executeCreateSezione(nome, finalObiettivo)).start();
    }

    private void executeCreateSezione(String nome, Double obiettivo) {
        try {
            SectionCreateRequest request = new SectionCreateRequest(nome, obiettivo);
            sezioneAPI.create(request);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        this,
                        "Sezione creata con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                
                dispose();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Errore nella creazione della sezione.\n" + ex.getMessage(),
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
        creaBtn.setEnabled(enabled);
        annullaBtn.setEnabled(enabled);
    }
}