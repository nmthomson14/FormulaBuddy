package com.example.FormulaBuddy.GUI;

import com.example.FormulaBuddy.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;

public class AddFormulaMenu {
    private static JFrame frame;
    private JPanel panel1;
    private JLabel output;
    private JPanel functionPanel;
    private JButton confirmButton;
    private JLabel addFormulaLabel;
    private JTextField nameField;
    private JTextField tagsField;
    private JTextField inputFieldLHS;
    private JTextField inputFieldRHS;
    private JCheckBox validCheckbox;
    private JTextField currentSelectedField;

    public static void createMenu(FormulaRecord record, Runnable onConfirm) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Formula Buddy");
            AddFormulaMenu menu = new AddFormulaMenu(record, onConfirm);

            frame.setContentPane(menu.panel1); // Set the UI panel
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack(); // Auto-size based on UI components
            frame.setLocationRelativeTo(null);
            frame.setVisible(true); // Show window
        });
    }

    private AddFormulaMenu(FormulaRecord formulaToEdit, Runnable onConfirm) {
        setupUI();

        validCheckbox.setSelected(false);
        validCheckbox.setEnabled(false);
        validCheckbox.setText("Please Enter a formula!");
        confirmButton.setEnabled(false);
        currentSelectedField = inputFieldRHS;
        functionPanel.setLayout(new GridLayout(0, 3, 5, 5));
        functionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (formulaToEdit != null) {
            nameField.setText(formulaToEdit.name());
            tagsField.setText(String.join(", ", formulaToEdit.tags()));
            addFormulaLabel.setText("Edit Formula");
            String[] expressionParts = formulaToEdit.expression().split("=");
            inputFieldLHS.setText(expressionParts[0]);
            inputFieldRHS.setText(expressionParts[1]);
            renderFormula();
        } else {
            addFormulaLabel.setText("Add Formula");
        }

        for (var record : FunctionDictionary.COMMON_FUNCTIONS) {
            JButton btn = createCalculatorSymbolButton(record);
            if (btn == null) continue;
            functionPanel.add(btn);
        }

        inputFieldLHS.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                renderFormula();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                renderFormula();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                renderFormula();
            }
        });

        inputFieldLHS.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                currentSelectedField = inputFieldLHS;
            }
        });

        inputFieldRHS.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                renderFormula();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                renderFormula();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                renderFormula();
            }
        });

        inputFieldRHS.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                currentSelectedField = inputFieldRHS;
            }
        });

        confirmButton.addActionListener(e -> {
            try {
                String[] tags = Arrays.stream(tagsField.getText().split(",")).map(String::trim).filter(tag -> !tag.isEmpty()).toArray(String[]::new);
                String formula = inputFieldLHS.getText() + " = " + inputFieldRHS.getText();
                FormulaRecord record = FormulaProcessor.processFormula(nameField.getText(), formula, tags);

                if (formulaToEdit != null) {
                    FileHandler.replaceFormulaRecord(formulaToEdit, record);
                } else {
                    FileHandler.addFormulaRecord(record);
                }

                onConfirm.run();
                frame.dispose();
            } catch (IllegalArgumentException ex) {
                output.setText("Invalid formula!");
            }
        });
    }

    private void renderFormula() {
        String formula = inputFieldLHS.getText() + " = " + inputFieldRHS.getText();
        Icon icon = FormulaProcessor.generateLatexIcon(formula, 30);
        if (icon == null) {
            validCheckbox.setSelected(false);
            validCheckbox.setText("Invalid");
            confirmButton.setEnabled(false);
            confirmButton.setText("Formula Invalid!");
            return;
        }
        validCheckbox.setSelected(true);
        validCheckbox.setText("Valid");
        confirmButton.setEnabled(true);
        confirmButton.setText("Confirm");
        output.setIcon(icon);
    }

    private JButton createCalculatorSymbolButton(FunctionRecord record) {
        try {
            Icon icon = FormulaProcessor.generateLatexIcon(record.symjaFunction(), 15);
            JButton button = new JButton(icon);

            String insertTemplate = record.insertFunction(); // e.g., "sqrt()"
            int cursorPos = insertTemplate.indexOf('(') + 1; // position right after '('

            button.addActionListener(e -> {
                if (currentSelectedField == null) return;
                insertFunctionTemplate(currentSelectedField, insertTemplate, cursorPos);
            });

            return button;
        } catch (Exception ex) {
            return null;
        }
    }

    private void insertFunctionTemplate(JTextComponent inputField, String template, int cursorOffset) {
        int pos = inputField.getCaretPosition();
        String currentText = inputField.getText();

        String newText = currentText.substring(0, pos) + template + currentText.substring(pos);
        inputField.setText(newText);

        inputField.requestFocusInWindow();
        inputField.setCaretPosition(pos + cursorOffset);
    }

    private void setupUI() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormulaLabel = new JLabel("Add Formula");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(addFormulaLabel, gbc);

        JLabel label4 = new JLabel("Name");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);

        nameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel1.add(nameField, gbc);

        JLabel label5 = new JLabel("Tags (comma separated)");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel1.add(label5, gbc);

        tagsField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        panel1.add(tagsField, gbc);

        JLabel label2 = new JLabel("Input");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel1.add(label2, gbc);

        inputFieldLHS = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel1.add(inputFieldLHS, gbc);

        JLabel label6 = new JLabel("=");
        gbc.gridx = 2;
        gbc.gridy = 3;
        panel1.add(label6, gbc);

        inputFieldRHS = new JTextField();
        gbc.gridx = 3;
        gbc.gridy = 3;
        panel1.add(inputFieldRHS, gbc);

        JLabel label8 = new JLabel("Please note that some letters like e (regardless of capitalization) are regarded as constants.");
        label8.setFont(new Font(label8.getFont().getName(), Font.PLAIN, 10));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label8, gbc);

        JLabel label3 = new JLabel("Functions");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel1.add(label3, gbc);

        functionPanel = new JPanel();
        functionPanel.setLayout(new BorderLayout());
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(functionPanel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label1 = new JLabel("Formula");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panel1.add(label1, gbc);

        output = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        panel1.add(output, gbc);

        JLabel label7 = new JLabel("Formula Valid:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel1.add(label7, gbc);

        validCheckbox = new JCheckBox("CheckBox");
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel1.add(validCheckbox, gbc);

        confirmButton = new JButton("Confirm");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(confirmButton, gbc);
    }

}
