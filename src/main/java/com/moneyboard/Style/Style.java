package com.moneyboard.Style;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;

public class Style {
    public static final Color BACKGROUND = new Color(245, 247, 251);
    public static final Color PRIMARY = new Color(37, 99, 235);
    public static final Color TEXT = new Color(31, 41, 55);
    private static final Color BORDER = new Color(219, 226, 236);

    public static void installModernTheme() {
        Font regular = new Font("Segoe UI", Font.PLAIN, 14);
        Font bold = new Font("Segoe UI", Font.BOLD, 14);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("OptionPane.messageFont", regular);
        UIManager.put("OptionPane.buttonFont", bold);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Label.font", regular);
        UIManager.put("Button.font", bold);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        UIManager.put("TextField.font", regular);
        UIManager.put("PasswordField.font", regular);
        UIManager.put("TextArea.font", regular);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("PasswordField.foreground", TEXT);
        UIManager.put("TextArea.foreground", TEXT);
        UIManager.put("TextField.caretForeground", PRIMARY);
        UIManager.put("PasswordField.caretForeground", PRIMARY);
        UIManager.put("TextArea.caretForeground", PRIMARY);

        var fieldBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 11, 8, 11));
        UIManager.put("TextField.border", fieldBorder);
        UIManager.put("PasswordField.border", fieldBorder);
        UIManager.put("TextArea.border", fieldBorder);
        UIManager.put("Table.font", regular);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.selectionBackground", new Color(219, 234, 254));
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("Table.gridColor", new Color(235, 239, 245));
        UIManager.put("TableHeader.font", bold);
        UIManager.put("TableHeader.background", new Color(239, 243, 249));
        UIManager.put("TableHeader.foreground", TEXT);
    }

    public static JButton newStyledButton(String text, Color colorForeground, Color backGr) {
        return newStyledButton(text, colorForeground, backGr, 16);
    }

    public static JButton newStyledButton(String text, Color colorForeground, Color backGr, int dimText) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, dimText));
        button.setBackground(backGr);
        button.setForeground(colorForeground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        Color normal = backGr;
        Color hover = normal.darker();
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(hover);
            }
            @Override public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });
        return button;
    }
}
