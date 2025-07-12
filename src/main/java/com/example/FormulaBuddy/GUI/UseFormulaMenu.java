package com.example.FormulaBuddy.GUI;

import com.example.FormulaBuddy.FormulaEvaluator;
import com.example.FormulaBuddy.FormulaProcessor;
import com.example.FormulaBuddy.FormulaRecord;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.*;

public class UseFormulaMenu {
    private JLabel formulaNameLabel;
    private JTable table1;
    private JPanel panel1;
    private JLabel formulaIconLabel;
    private JButton evaluateButton;
    private JButton clearButton;
    private JLabel output;
    private JTextArea logOut;

    private void setupUI() {
        panel1 = new JPanel(new GridBagLayout());
        panel1.setEnabled(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Formula Name Label
        formulaNameLabel = new JLabel("Formula Name", SwingConstants.CENTER);
        Font font = new Font(formulaNameLabel.getFont().getName(), Font.BOLD, 20);
        formulaNameLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(formulaNameLabel, gbc);

        // Formula Icon Label
        formulaIconLabel = new JLabel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel1.add(formulaIconLabel, gbc);

        // Table inside ScrollPane
        table1 = new JTable();
        JScrollPane scrollPane1 = new JScrollPane(table1);
        scrollPane1.setPreferredSize(new Dimension(-1, 200));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);

        // "Log" label
        JLabel logLabel = new JLabel("Log");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(logLabel, gbc);

        // Log Output (JTextArea inside ScrollPane)
        logOut = new JTextArea();
        logOut.setEditable(false);
        logOut.setLineWrap(true);
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black)));
        logPanel.setBackground(new Color(-3904));
        logPanel.add(logOut);
        JScrollPane scrollPane2 = new JScrollPane(logPanel);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panel1.add(scrollPane2, gbc);

        // "Result" Label
        JLabel resultLabel = new JLabel("Result");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(resultLabel, gbc);

        // Output Panel with Label
        output = new JLabel("");
        output.setFont(new Font(output.getFont().getName(), Font.BOLD, 20));
        output.setBackground(new Color(-11826809));
        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBackground(new Color(-3904));
        resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black)));
        resultPanel.add(output);
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(resultPanel, gbc);

        // Evaluate Button
        evaluateButton = new JButton("Evaluate");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(evaluateButton, gbc);

        // Clear Button
        clearButton = new JButton("Clear");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        panel1.add(clearButton, gbc);

        scrollPane1.setPreferredSize(new Dimension(400, 200));
        scrollPane2.setPreferredSize(new Dimension(400, 100));
        resultPanel.setPreferredSize(new Dimension(200, 40));
        panel1.setMinimumSize(new Dimension(500, 600)); // Adjust as needed
        table1.setMinimumSize(new Dimension(400, 200));
        logOut.setMinimumSize(new Dimension(300, 80));
    }


    static class TableResult {

        public enum ReturnResult {
            NAN, NO_UNKNOWN, TOO_MANY_UNKNOWNS, PASS, FAIL;
        }

        public ReturnResult result = ReturnResult.FAIL;
        public String unknown = null;
        public HashMap<String, Double> variables = new HashMap<>();
    }

    static class FormulaVariableTableModel extends AbstractTableModel {
        private final String[] variables;
        private final String[] columnNames = {"Variable", "Value"};
        private final Object[][] data;

        public FormulaVariableTableModel(String[] variables) {
            this.variables = variables;
            data = new Object[variables.length][2];

            // Populate column 1 (Variable names)
            for (int i = 0; i < variables.length; i++) {
                data[i][0] = variables[i];
                data[i][1] = ""; // Blank values
            }
        }

        @Override
        public int getRowCount() {
            return variables.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            return data[row][column];
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            if (column == 1) { // Allow editing only in column 2
                data[row][1] = value;
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1; // Only allow edits in column 2 (Value)
        }
    }

    public static void createMenu(FormulaRecord record) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Formula Buddy");
            UseFormulaMenu menu = new UseFormulaMenu(record);

            frame.setContentPane(menu.panel1); // Set the UI panel
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack(); // Auto-size based on UI components
            frame.setLocationRelativeTo(null);
            frame.setVisible(true); // Show window
        });
    }

    private UseFormulaMenu(FormulaRecord record) {
        setupUI();
        formulaNameLabel.setText(record.name());
        formulaIconLabel.setIcon(FormulaProcessor.generateLatexIcon(record.expression(), 30));

        // Populate table
        String[] variables = Arrays.copyOf(record.symbols(), record.symbols().length);
        FormulaVariableTableModel model = new FormulaVariableTableModel(variables);
        table1.setModel(model);

        // Handle evaluate button
        evaluateButton.addActionListener(e -> {

            // Ensure all values are collected properly, even those currently being edited
            if (table1.isEditing()) {
                table1.getCellEditor().stopCellEditing();
            }

            TableResult result = tryReadTable();
            switch (result.result) {
                case PASS -> solve(record.expression(), result.variables, result.unknown);
                case NAN -> showError("One or more inputs are not valid numbers.");
                case TOO_MANY_UNKNOWNS -> showError("Can only solve for one unknown variable.");
                case NO_UNKNOWN -> showError("No unknown variable. Please leave one table cell blank.");
            }
        });

        clearButton.addActionListener(e -> clear());
    }

    private void solve(String formula, HashMap<String, Double> variables, String unknown) {
        String result = FormulaEvaluator.solveForUnknown(formula, variables, unknown);
        output.setText(result);
        addLog(variables, result);
    }

    private void addLog(HashMap<String, Double> variables, String result) {
        String log = String.format("Variables: %s\nResult: %s\n", variables, result);
        logOut.append(log);
    }

    private void showError(String s) {
        output.setText("Error");
        logOut.append(s + "\n");
    }

    private void clear() {
        FormulaVariableTableModel model = (FormulaVariableTableModel) table1.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            model.setValueAt("", row, 1);
        }
        model.fireTableDataChanged();

        output.setText("");
    }

    private TableResult tryReadTable() {

        TableResult tableResult = new TableResult();
        FormulaVariableTableModel model = (FormulaVariableTableModel) table1.getModel();

        for (int row = 0; row < model.getRowCount(); row++) {
            String variableName = model.getValueAt(row, 0).toString().trim(); // Column 1: Variable
            String variableValue = model.getValueAt(row, 1).toString().trim();

            try {
                if (variableValue.isBlank() && tableResult.unknown == null) {
                    tableResult.unknown = variableName;
                    continue;

                } else if (variableValue.isBlank() && tableResult.unknown != null) {
                    tableResult.result = TableResult.ReturnResult.TOO_MANY_UNKNOWNS;
                    return tableResult;
                }
                Double doubleValue = Double.parseDouble(variableValue); // Column 2: User-provided Value
                tableResult.variables.put(variableName, doubleValue);

            } catch (NumberFormatException e) {
                tableResult.result = TableResult.ReturnResult.NAN;
                return tableResult;
            }
        }

        if (tableResult.unknown == null) {
            tableResult.result = TableResult.ReturnResult.NO_UNKNOWN;
            return tableResult;
        }

        tableResult.result = TableResult.ReturnResult.PASS;
        return tableResult;
    }
}
