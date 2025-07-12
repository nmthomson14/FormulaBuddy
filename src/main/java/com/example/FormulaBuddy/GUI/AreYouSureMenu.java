package com.example.FormulaBuddy.GUI;

import javax.swing.*;

public class AreYouSureMenu {
    private JPanel panel1;
    private JButton yesButton;
    private JButton cancelButton;
    private JLabel label1;

    public static void CreateMenu( String message, Runnable subscriberMethod) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Formula Buddy");
            AreYouSureMenu menu = new AreYouSureMenu(subscriberMethod, frame, message);

            frame.setContentPane(menu.panel1); // Set the UI panel
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack(); // Auto-size based on UI components
            frame.setLocationRelativeTo(null);
            frame.setVisible(true); // Show window
        });
    }

    private AreYouSureMenu(Runnable subscriberMethod, JFrame frame, String message) {

        label1.setText(message);

        yesButton.addActionListener(e -> {
            subscriberMethod.run();
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            frame.dispose();
        });
    }
}
