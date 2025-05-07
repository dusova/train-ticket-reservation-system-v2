package com.trainticket;

import com.formdev.flatlaf.FlatLightLaf;
import com.trainticket.view.LoginFrame;
import com.trainticket.util.DatabaseUtil;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        DatabaseUtil.initializeDatabase();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseUtil.closeConnection();
        }));

        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                UIManager.put("Button.arc", 10);
                UIManager.put("Component.arc", 8);
                UIManager.put("ProgressBar.arc", 8);
                UIManager.put("TextComponent.arc", 8);
                UIManager.put("CheckBox.arc", 8);
                UIManager.put("Component.focusWidth", 1);
                UIManager.put("Button.borderWidth", 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            new LoginFrame().setVisible(true);
        });
    }
}