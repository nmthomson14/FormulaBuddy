package com.example.FormulaBuddy;

import org.apache.commons.lang3.NotImplementedException;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GUIBuilder implements MessageReceiver {

    // Constants
    private static final int UI_HEIGHT = 650;
    private static final int UI_WIDTH = 700;
    private static final String PROGRAM_NAME = "Formula Buddy";



    // Ui components
    private JTextArea systemOutput;
    private CardLayout cardLayout;
    private JPanel stackedPanel;
    private JFrame mainFrame;

    // Handlers

    // TODO: DECONSTRUCT AND REPLACE THIS WITH UI DESIGNER COMPONENTS

    public GUIBuilder() {

        initializeUI();
    }

    private void initializeUI() {

        // Prevent concurrency issues by running on the event dispatch thread (handles all GUI events)
        SwingUtilities.invokeLater(this::buildMainMenu);
    }

    private void buildMainMenu() {

        // Define JComponents
        systemOutput = new JTextArea(10, 20);
        cardLayout = new CardLayout();
        stackedPanel = new JPanel(cardLayout);
        mainFrame = new JFrame(PROGRAM_NAME);
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JScrollPane systemOutputScrollPane = new JScrollPane(systemOutput);
        JLabel logo;

        // Get Logo
        URL logoUrl = getClass().getResource("/logo.png");

        if (logoUrl != null) {

            ImageIcon originalIcon = new ImageIcon(logoUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            logo = new JLabel(scaledIcon);

        } else {

            receiveMessage("Could not load logo from resources.");
            logo = new JLabel("Logo not found");
        }

        // Handle the systemOutput messaging box
        systemOutput.setEditable(false);
        systemOutputScrollPane.setPreferredSize(new Dimension(500, 100));
        systemOutputScrollPane.setMaximumSize(new Dimension(500, 100));

        // Handle the top panel
        topPanel.add(Box.createVerticalStrut(25)); // spacer
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        addComponent(logo, topPanel, 10);

        // Handle the bottom panel
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // Respect vertical sizing
        addComponent(systemOutputScrollPane, bottomPanel, 50);

        // Handle main frame
        mainFrame.setSize(UI_WIDTH, UI_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(topPanel, BorderLayout.NORTH);
        mainFrame.add(stackedPanel, BorderLayout.CENTER);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Build the tabs
        buildFormulaInputScreen();
        mainFrame.setVisible(true);
        cardLayout.show(stackedPanel, "INPUTFORMULA");

        // Print welcome message after initialization
        receiveMessage("Welcome to Formula Buddy! Click help if you're stuck.");
    }

    private void buildFormulaInputScreen() {
        // Define JComponents
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a formula!");
        JTextField formulaInput = new JTextField();
        JButton functions = new JButton("Functions Dictionary");
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        JDialog dialog = createCalculatorDialog(mainFrame, formulaInput);
        JLabel latexLabel = new JLabel();
        JPanel formulaExecutorScreen = buildEmptyFormulaExecutorScreen();

        // Set panel defaults
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(10)); // spacer

        // Button handlers
        functions.addActionListener(e -> {
            dialog.setVisible(true);
        });
        save.addActionListener(e -> {
            populateFormulaExecutorScreen(formulaExecutorScreen, formulaInput.getText());
            cardLayout.show(stackedPanel, "FORMULAEXECUTOR");
            System.out.println("doing...");
        });
        cancel.addActionListener(e -> {
            throw new NotImplementedException();
        });
        formulaInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLatex();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLatex();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLatex();
            }
            private void updateLatex() {

                String input = formulaInput.getText().trim();

                if (input.isEmpty()) {
                    latexLabel.setIcon(null);
                    latexLabel.setText("Input formula");
                    return;
                }

                try {
                    Icon latexIcon = FormulaProcessor.generateLatexIcon(formulaInput.getText());
                    latexLabel.setIcon(latexIcon);
                    latexLabel.setText(null);
                } catch (Exception ex) {
                    latexLabel.setIcon(null);
                }
            }
        });

        // Add components to panel
        addComponent(label, panel, 10);
        addComponent(formulaInput, panel, 50);
        addComponent(functions, panel, 10);
        addComponent(latexLabel, panel, 10);
        addComponent(save, panel, 10);
        addComponent(cancel, panel, 10);

//        for (var entry : COMMON_FUNCTIONS.entrySet()) {
//            JButton button = createCalculatorSymbolButton(formulaInput, entry.getKey(), entry.getValue());
//            panel.add(button);
//        }

        // add the panel to the stacked panes
        stackedPanel.add(panel, "INPUTFORMULA");

    }

    private JPanel buildEmptyFormulaExecutorScreen() {
        JPanel panel = new JPanel();

        // Set panel defaults
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(10)); // spacer

        stackedPanel.add(panel, "FORMULAEXECUTOR");
        return panel;
    }

    private void populateFormulaExecutorScreen(JPanel panel, String formula) {
        panel.removeAll();

        // JComponents
        JLabel result = new JLabel();
        JButton evaluateButton = new JButton("Evaluate");

        // Set panel defaults
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(10)); // spacer

        FormulaRecord record;

        try {
            record = FormulaProcessor.processFormula("Test formula", formula);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        JLabel label = new JLabel();
        label.setIcon(FormulaProcessor.generateLatexIcon(record.expression()));
        addComponent(label, panel, 10);
        HashMap<String, String> variables = new HashMap<>();

        for (String symbol : record.symbols()) {
            variables.put(symbol, symbol);
            JLabel lab = new JLabel(symbol);
            addComponent(lab, panel, 0);
            JTextField field = new JTextField();
            addComponent(field, panel, 10);
            field.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    assignVariable();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    assignVariable();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    assignVariable();
                }
                private void assignVariable() {
                    variables.put(symbol, field.getText());
                }
            });
        }

        evaluateButton.addActionListener(e -> {
            result.setText(FormulaEvaluator.evaluate(record, variables).toString());
        });

        addComponent(result, panel, 10);
        addComponent(evaluateButton, panel, 10);
    }

    private JDialog createCalculatorDialog(JFrame owner, JTextField formulaInput) {
        JDialog dialog = new JDialog(owner, "Calculator Functions", false);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        // Input field at top
        dialog.add(formulaInput, BorderLayout.NORTH);

        // Panel for buttons with grid layout: 3 columns (adjust as needed)
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (var record : FunctionDictionary.COMMON_FUNCTIONS) {
            JButton btn = createCalculatorSymbolButton(formulaInput, record.symjaFunction(), record.latexFunction());
            if (btn == null) { continue; }
            buttonsPanel.add(btn);
        }

        dialog.add(new JScrollPane(buttonsPanel), BorderLayout.CENTER);

        return dialog;
    }

    // helper methods
    private void openFormulaInputScreen() {

    }

    private void addComponent(JComponent component, JPanel panel, int spacer) {

        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(component);
        panel.add(Box.createVerticalStrut(spacer)); // spacer
    }

    private void insertFunction(JTextComponent inputField, String beforeCursor, String afterCursor) {
        int start = inputField.getSelectionStart();
        int end = inputField.getSelectionEnd();
        String currentText = inputField.getText();

        String newText;

        if (start != end) {
            // Wrap selected text
            String selectedText = currentText.substring(start, end);
            newText = currentText.substring(0, start) + beforeCursor + selectedText + afterCursor + currentText.substring(end);
            inputField.setText(newText);

            // Reselect the text inside the new wrapper
            inputField.requestFocusInWindow();
            inputField.select(start + beforeCursor.length(), start + beforeCursor.length() + selectedText.length());
        } else {
            // No selection: insert template and place cursor between
            int pos = inputField.getCaretPosition();
            newText = currentText.substring(0, pos) + beforeCursor + afterCursor + currentText.substring(pos);
            inputField.setText(newText);

            inputField.requestFocusInWindow();
            inputField.setCaretPosition(pos + beforeCursor.length());
        }
    }

    private JButton createCalculatorSymbolButton(JTextField formulaInput, String function, String latex) {
        try {
            JButton button = new JButton(FormulaProcessor.generateLatexIcon(function));

            int inputStart = function.indexOf('(');
            String funcName = inputStart != -1 ? function.substring(0, inputStart + 1) : function;
            String funcArgs = inputStart != -1 ? function.substring(inputStart + 1) : "";

            button.addActionListener(e -> {
                insertFunction(formulaInput, funcName, funcArgs);
            });

            return button;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void receiveMessage(String message) {

        systemOutput.setText(message);
    }
}