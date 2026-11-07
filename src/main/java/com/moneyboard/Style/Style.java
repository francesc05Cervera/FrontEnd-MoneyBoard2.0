package com.moneyboard.Style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import javax.swing.table.JTableHeader;

public final class Style {
    public static final Color BACKGROUND = new Color(15, 18, 28);
    public static final Color SURFACE = new Color(24, 29, 43);
    public static final Color SURFACE_ALT = new Color(31, 38, 55);
    public static final Color PRIMARY = new Color(99, 102, 241);
    public static final Color TEXT = new Color(237, 241, 249);
    public static final Color MUTED = new Color(157, 167, 186);
    public static final Color BORDER = new Color(54, 63, 82);
    public static final Color SUCCESS = new Color(22, 163, 119);
    public static final Color DANGER = new Color(220, 75, 91);
    public static final Color WARNING = new Color(202, 145, 55);
    private static final Font REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD = new Font("Segoe UI", Font.BOLD, 14);

    private Style() {}

    public static void installModernTheme() {
        UIManager.put("Panel.background", SURFACE);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("OptionPane.messageFont", REGULAR);
        UIManager.put("OptionPane.buttonFont", BOLD);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Label.font", REGULAR);
        UIManager.put("Button.font", BOLD);
        UIManager.put("Button.foreground", TEXT);
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.focus", BACKGROUND);
        UIManager.put("TextField.font", REGULAR);
        UIManager.put("PasswordField.font", REGULAR);
        UIManager.put("TextArea.font", REGULAR);
        UIManager.put("TextField.background", SURFACE_ALT);
        UIManager.put("PasswordField.background", SURFACE_ALT);
        UIManager.put("TextArea.background", SURFACE_ALT);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("PasswordField.foreground", TEXT);
        UIManager.put("TextArea.foreground", TEXT);
        UIManager.put("TextField.caretForeground", PRIMARY);
        UIManager.put("PasswordField.caretForeground", PRIMARY);
        UIManager.put("TextArea.caretForeground", PRIMARY);
        UIManager.put("Table.font", REGULAR);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.selectionBackground", new Color(57, 65, 105));
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.font", BOLD);
        UIManager.put("TableHeader.background", SURFACE_ALT);
        UIManager.put("TableHeader.foreground", TEXT);
        UIManager.put("ComboBox.background", SURFACE_ALT);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("CheckBox.background", SURFACE);
        UIManager.put("CheckBox.foreground", TEXT);
        UIManager.put("ScrollPane.background", SURFACE);
        UIManager.put("Viewport.background", SURFACE);
    }

    public static void applyTheme(Window window) {
        window.getContentPane().setBackground(BACKGROUND);
        styleTree(window);
        window.setBackground(BACKGROUND);
    }

    private static void styleTree(Component component) {
        if (component instanceof JPanel panel) {
            panel.setBackground(SURFACE);
            panel.setOpaque(true);
        }
        if (component instanceof JLabel label) {
            label.setForeground(TEXT);
            label.setFont(label.getFont().getSize() >= 22
                    ? new Font("Segoe UI", Font.BOLD, label.getFont().getSize())
                    : REGULAR);
        }
        if (component instanceof JTextComponent field) {
            field.setBackground(SURFACE_ALT);
            field.setForeground(TEXT);
            field.setCaretColor(PRIMARY);
            field.setSelectionColor(new Color(57, 65, 105));
            field.setSelectedTextColor(Color.WHITE);
            field.setFont(REGULAR);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1, true),
                    BorderFactory.createEmptyBorder(9, 12, 9, 12)));
        }
        if (component instanceof JCheckBox check) {
            check.setBackground(SURFACE);
            check.setForeground(TEXT);
            check.setFont(REGULAR);
            check.setFocusPainted(false);
        }
        if (component instanceof JTable table) {
            table.setBackground(SURFACE);
            table.setForeground(TEXT);
            table.setSelectionBackground(new Color(57, 65, 105));
            table.setSelectionForeground(Color.WHITE);
            table.setGridColor(BORDER);
            table.setFont(REGULAR);
            table.setRowHeight(Math.max(38, table.getRowHeight()));
            table.setFillsViewportHeight(true);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(false);
            JTableHeader header = table.getTableHeader();
            header.setBackground(SURFACE_ALT);
            header.setForeground(TEXT);
            header.setFont(BOLD);
            header.setPreferredSize(new java.awt.Dimension(header.getPreferredSize().width, 42));
        }
        if (component instanceof JScrollPane scroll) {
            scroll.setBackground(SURFACE);
            scroll.getViewport().setBackground(SURFACE);
            scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        }
        if (component instanceof JComboBox<?> combo) {
            combo.setBackground(SURFACE_ALT);
            combo.setForeground(TEXT);
            combo.setFont(REGULAR);
        }
        if (component instanceof JButton button) {
            Color normal = button.getClientProperty("moneyboard.normal") instanceof Color c
                    ? c : PRIMARY;
            styleButton(button, normal);
        }
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                styleTree(child);
            }
        }
    }

    private static void styleButton(JButton button, Color normal) {
        button.putClientProperty("moneyboard.normal", normal);
        button.setFont(BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(normal);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        if (!Boolean.TRUE.equals(button.getClientProperty("moneyboard.hoverInstalled"))) {
            button.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    if (button.isEnabled()) button.setBackground(normal.brighter());
                }
                @Override public void mouseExited(MouseEvent e) {
                    button.setBackground(normal);
                }
            });
            button.putClientProperty("moneyboard.hoverInstalled", Boolean.TRUE);
        }
    }

    public static JButton newStyledButton(String text, Color colorForeground, Color backGr) {
        return newStyledButton(text, colorForeground, backGr, 16);
    }

    public static JButton newStyledButton(String text, Color colorForeground, Color backGr, int dimText) {
        JButton button = new JButton(text);
        Color semantic = backGr.getGreen() > backGr.getRed() * 1.25
                ? SUCCESS
                : backGr.getRed() > backGr.getGreen() * 1.35
                    ? DANGER
                    : backGr.getRed() > 180 && backGr.getGreen() > 130
                        ? WARNING : PRIMARY;
        styleButton(button, semantic);
        button.setFont(new Font("Segoe UI", Font.BOLD, dimText));
        return button;
    }
}
