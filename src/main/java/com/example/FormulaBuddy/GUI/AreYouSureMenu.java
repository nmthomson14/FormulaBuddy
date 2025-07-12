package com.example.FormulaBuddy.GUI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class AreYouSureMenu {
    private JPanel panel1;
    private JButton yesButton;
    private JButton cancelButton;
    private JLabel label1;

    public static void CreateMenu(String message, Runnable subscriberMethod) {

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
        setupUI();
        label1.setText(message);

        yesButton.addActionListener(e -> {
            subscriberMethod.run();
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            frame.dispose();
        });
    }

    private void setupUI() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel1.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel label2 = new JLabel("Are you sure?");
        label2.setFont(getFont(null, Font.BOLD, 20, label2.getFont()));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel1.add(label2, gbc);

        label1 = new JLabel("Label");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel1.add(label1, gbc);

//        yesButton = new JButton("Yes");
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridwidth = 1;
//        gbc.anchor = GridBagConstraints.NORTHEAST;
//        gbc.insets = new Insets(0, 0, 0, 10);
//        panel1.add(yesButton, gbc);
//
//        cancelButton = new JButton("Cancel");
//        cancelButton.setForeground(new Color(0xE50100));
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.NORTHWEST;
//        gbc.insets = new Insets(0, 10, 0, 0);
//        panel1.add(cancelButton, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 20px gap between buttons
        yesButton = new JButton("Yes");
        cancelButton = new JButton("Cancel");
        cancelButton.setForeground(new Color(0xE50100));

        buttonPanel.add(yesButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel1.add(buttonPanel, gbc);
    }


    /**
     * @noinspection ALL
     */
    private Font getFont(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

}
