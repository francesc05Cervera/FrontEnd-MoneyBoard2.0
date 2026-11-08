package com.moneyboard;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.moneyboard.style.Style;
import com.moneyboard.View.LogInArea;

public class MainApp extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Style.installModernTheme();
            JFrame frame = new LogInArea();
            frame.setVisible(true);
        });
    }
}
