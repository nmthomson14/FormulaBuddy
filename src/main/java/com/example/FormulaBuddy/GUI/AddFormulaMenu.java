package com.example.FormulaBuddy.GUI;

import com.example.FormulaBuddy.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
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

        inputFieldLHS.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { renderFormula(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { renderFormula(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { renderFormula(); }
        });

        inputFieldLHS.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                currentSelectedField = inputFieldLHS;
            }
        });

        inputFieldRHS.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { renderFormula(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { renderFormula(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { renderFormula(); }
        });

        inputFieldRHS.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                currentSelectedField = inputFieldRHS;
            }
        });

        confirmButton.addActionListener(e -> {
            try {
                String[] tags = Arrays.stream(tagsField.getText().split(","))
                        .map(String::trim)
                        .filter(tag -> !tag.isEmpty())
                        .toArray(String[]::new);
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
}
