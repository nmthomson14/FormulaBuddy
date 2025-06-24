package com.example.FormulaBuddy.GUI;

import com.example.FormulaBuddy.FormulaEvaluator;
import com.example.FormulaBuddy.FormulaProcessor;
import com.example.FormulaBuddy.FormulaRecord;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UseFormulaMenu {
    private JLabel formulaNameLabel;
    private JTable table1;
    private JPanel panel1;
    private JLabel formulaIconLabel;
    private JButton evaluateButton;
    private JButton clearButton;
    private JLabel output;

    static class TableResult {

        public enum ReturnResult {
            NAN,
            NO_UNKNOWN,
            TOO_MANY_UNKNOWNS,
            PASS,
            FAIL;
        }

        public ReturnResult result = ReturnResult.FAIL;
        public String unknown = null;
        public HashMap<String, Double> variables = new HashMap<>();
    }

    static class FormulaVariableTableModel extends AbstractTableModel {
        private final List<String> variables;
        private final String[] columnNames = {"Variable", "Value"};
        private final Object[][] data;

        public FormulaVariableTableModel(List<String> variables) {
            this.variables = variables;
            data = new Object[variables.size()][2];

            // Populate column 1 (Variable names)
            for (int i = 0; i < variables.size(); i++) {
                data[i][0] = variables.get(i);
                data[i][1] = ""; // Blank values
            }
        }

        @Override
        public int getRowCount() {
            return variables.size();
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
            frame.setVisible(true); // Show window
        });
    }

    private UseFormulaMenu(FormulaRecord record) {
        createUIComponents();

        formulaNameLabel.setText(record.name());
        formulaIconLabel.setIcon(FormulaProcessor.generateLatexIcon(record.expression()));

        // Populate table
        List<String> variables = new ArrayList<>(record.symbols());
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
    }

    private void showError(String s) {
        output.setText(s);
    }

    private void clear() {
        FormulaVariableTableModel model = (FormulaVariableTableModel) table1.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            model.setValueAt("", row, 1);
        }
        model.fireTableDataChanged();

        output.setText("");
    }

    private void createUIComponents() { }

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
