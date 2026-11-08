package com.moneyboard.Style;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

public final class Style {
    public static final Color BACKGROUND = new Color(10, 14, 25);
    public static final Color SURFACE = new Color(19, 26, 42);
    public static final Color SURFACE_ALT = new Color(27, 36, 56);
    public static final Color PRIMARY = new Color(124, 108, 255);
    public static final Color PRIMARY_HOVER = new Color(145, 132, 255);
    public static final Color TEXT = new Color(239, 242, 250);
    public static final Color MUTED = new Color(145, 157, 180);
    public static final Color BORDER = new Color(43, 55, 79);
    public static final Color SUCCESS = new Color(48, 196, 141);
    public static final Color DANGER = new Color(244, 98, 116);
    public static final Color WARNING = new Color(245, 181, 76);
    public static final Font REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font MEDIUM = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font HEADING = new Font("Segoe UI", Font.BOLD, 28);

    private Style() {}

    public static void installModernTheme() {
        UIManager.put("Panel.background", SURFACE);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Label.font", REGULAR);
        UIManager.put("Button.font", MEDIUM);
        UIManager.put("Button.foreground", TEXT);
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("TextField.font", REGULAR);
        UIManager.put("PasswordField.font", REGULAR);
        UIManager.put("TextArea.font", REGULAR);
        UIManager.put("TextField.background", SURFACE_ALT);
        UIManager.put("PasswordField.background", SURFACE_ALT);
        UIManager.put("TextArea.background", SURFACE_ALT);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("PasswordField.foreground", TEXT);
        UIManager.put("TextArea.foreground", TEXT);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("OptionPane.messageFont", REGULAR);
        UIManager.put("OptionPane.buttonFont", MEDIUM);
        UIManager.put("Table.font", REGULAR);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.selectionBackground", new Color(61, 60, 112));
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.font", MEDIUM);
        UIManager.put("TableHeader.background", SURFACE_ALT);
        UIManager.put("TableHeader.foreground", MUTED);
        UIManager.put("CheckBox.background", SURFACE);
        UIManager.put("CheckBox.foreground", TEXT);
        UIManager.put("ScrollPane.background", SURFACE);
        UIManager.put("Viewport.background", SURFACE);
    }

    public static JPanel backgroundPanel() {
        return new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setPaint(new GradientPaint(0, 0, new Color(13, 19, 35),
                        getWidth(), getHeight(), new Color(22, 25, 48)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }

    public static JPanel cardPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(24, 26, 24, 26)));
        return panel;
    }

    public static JPanel cardPanel() {
        return cardPanel(new BorderLayout(12, 12));
    }

    public static JLabel heading(String value) {
        JLabel label = new JLabel(value);
        label.setFont(HEADING);
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel subtitle(String value) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(MUTED);
        return label;
    }

    public static JLabel fieldLabel(String value) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(190, 199, 218));
        return label;
    }

    public static void styleField(JTextComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(TEXT);
        field.setCaretColor(PRIMARY_HOVER);
        field.setSelectionColor(new Color(75, 69, 135));
        field.setSelectedTextColor(Color.WHITE);
        field.setBackground(SURFACE_ALT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)));
    }

    public static void styleTable(JTable table) {
        table.setFont(REGULAR);
        table.setForeground(TEXT);
        table.setBackground(SURFACE);
        table.setSelectionBackground(new Color(61, 60, 112));
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(46);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(36, 46, 66));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setForeground(MUTED);
        header.setBackground(SURFACE_ALT);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 44));
        header.setReorderingAllowed(false);
    }

    public static JButton newStyledButton(String text, Color ignoredForeground, Color requestedColor) {
        return newStyledButton(text, ignoredForeground, requestedColor, 14);
    }

    public static JButton newStyledButton(String text, Color ignoredForeground, Color requestedColor, int size) {
        JButton button = new JButton(text);
        Color semantic = requestedColor.getRed() > 170 && requestedColor.getGreen() < 150
                ? DANGER
                : requestedColor.getGreen() > requestedColor.getRed() * 1.2
                    ? SUCCESS
                    : requestedColor.getRed() > 180 && requestedColor.getGreen() > 120
                        ? WARNING : PRIMARY;
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, size));
        button.setForeground(Color.WHITE);
        button.setBackground(semantic);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(13, 18, 13, 18));
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(semantic.brighter());
            }
            @Override public void mouseExited(MouseEvent e) {
                button.setBackground(semantic);
            }
        });
        return button;
    }

    public static void applyTheme(JFrame window) {
        window.getContentPane().setBackground(BACKGROUND);
        window.setBackground(BACKGROUND);
    }
}
