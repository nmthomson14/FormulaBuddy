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
import java.util.Map;

public class GUIBuilder implements MessageReceiver {

    // Constants
    private static final int UI_HEIGHT = 650;
    private static final int UI_WIDTH = 700;
    private static final String PROGRAM_NAME = "Formula Buddy";

    // Common functions and their latex equivalent. Left string is inserted into formula, right is used to generate a button.
    private static final Map<String, String> COMMON_FUNCTIONS = Map.ofEntries(
            Map.entry("sqrt()", "\\sqrt{x}"),          // square root
            Map.entry("abs()", "\\left|x\\right|"),    // absolute value
            Map.entry("sin()", "\\sin{x}"),            // sine
            Map.entry("cos()", "\\cos{x}"),            // cosine
            Map.entry("tan()", "\\tan{x}"),            // tangent
            Map.entry("asin()", "\\sin^{-1}{x}"),      // arcsine
            Map.entry("acos()", "\\cos^{-1}{x}"),      // arccosine
            Map.entry("atan()", "\\tan^{-1}{x}"),      // arctangent
            Map.entry("log()", "\\log_{10}{x}"),       // logarithm base 10
            Map.entry("ln()", "\\ln{x}"),              // natural logarithm
            Map.entry("exp()", "e^{x}"),               // exponential
            Map.entry("pow( , )", "x^{y}"),            // power
            Map.entry("nthRoot( , )", "\\sqrt[y]{x}"), // nth root
            Map.entry("floor()", "\\lfloor{x}\\rfloor"), // floor
            Map.entry("ceil()", "\\lceil{x}\\rceil"),    // ceiling
            Map.entry("round()", "\\text{round}(x)"),    // round
            Map.entry("min( , )", "\\min(x, y)"),         // minimum
            Map.entry("max( , )", "\\max(x, y)"),         // maximum
            Map.entry("mod( , )", "x \\bmod y"),          // modulo
            Map.entry("diff( , )", "\\frac{d}{dx}x"),     // derivative
            Map.entry("integrate( , )", "\\int x \\, dx"),// integral
            Map.entry("sum( , )", "\\sum_{i=1}^{n} x_i"), // summation
            Map.entry("product( , )", "\\prod_{i=1}^{n} x_i"), // product
            Map.entry("limit( , )", "\\lim_{x \\to a} f(x)")   // limit
    );

    // Ui components
    private JTextArea systemOutput;
    private CardLayout cardLayout;
    private JPanel stackedPanel;
    private JFrame mainFrame;

    // Handlers

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

        // TODO: CREATE A LIST OF BUTTON (PROBABLY A CLASS) THAT ARE DEFINED GLOBAL PRIVATE
        // THESE BUTTONS HAVE A NAME, IMAGE (LIKE SQUARE ROOT SYMBOL) AND WHEN PRESSED
        // INPUT A CERTAIN STRING IE SQUARE ROOT IS SQRT() MAKING IT EASIER FOR FORMULA INPUT

        // Define JComponents
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a formula!");
        JTextField formulaInput = new JTextField();
        JButton functions = new JButton("Functions Dictionary");
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        JDialog dialog = createCalculatorDialog(mainFrame, formulaInput);
        JLabel latexLabel = new JLabel();

        // Set panel defaults
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(10)); // spacer

        // Button handlers
        functions.addActionListener(e -> {
            dialog.setVisible(true);
        });
        save.addActionListener(e -> {
            throw new NotImplementedException();
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
                    IExpr parsedExpression = FormulaProcessor.EVALUATOR.parse(formulaInput.getText());
                    StringWriter writer = new StringWriter();
                    TeXUtilities texUtil = new TeXUtilities(FormulaProcessor.EVALUATOR.getEvalEngine(), false);
                    boolean success = texUtil.toTeX(parsedExpression, writer);
                    Icon latexIcon = generateLatexIcon(FormulaProcessor.generateLatexStub(parsedExpression));
                    if (success) {
                        latexLabel.setIcon(latexIcon);
                        latexLabel.setText(null);
                    }
                } catch (Exception ex) {
                    // Do not update
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

        for (Map.Entry<String, String> entry : COMMON_FUNCTIONS.entrySet()) {
            JButton btn = createCalculatorSymbolButton(formulaInput, entry.getKey(), entry.getValue());
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
        JButton button = new JButton(generateLatexIcon(latex));

        int inputStart = function.indexOf('(');
        String funcName = inputStart != -1 ? function.substring(0, inputStart + 1) : function;
        String funcArgs = inputStart != -1 ? function.substring(inputStart + 1) : "";

        button.addActionListener(e -> {
            insertFunction(formulaInput, funcName, funcArgs);
        });

        return button;
    }

    private Icon generateLatexIcon(String latex) {
        // Parse LaTeX formula
        TeXFormula formula = new TeXFormula(latex);

        // Create an icon of the formula (size 20)
        return formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
    }

    @Override
    public void receiveMessage(String message) {

        systemOutput.setText(message);
    }
}