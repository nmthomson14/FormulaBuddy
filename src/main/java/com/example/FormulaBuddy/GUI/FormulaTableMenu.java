package com.example.FormulaBuddy.GUI;

import com.example.FormulaBuddy.FileHandler;
import com.example.FormulaBuddy.FormulaProcessor;
import com.example.FormulaBuddy.FormulaRecord;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FormulaTableMenu {

    // ui
    private JPanel panel1;
    private JTable table1;
    private JButton deleteFormulaButton;
    private JButton useFormulaButton;
    private JButton editFormulaButton;
    private JTextField searchTextField;
    private JLabel renderedFormulaLabel;
    private JComboBox comboBox1;
    private JButton addFormula;
    private JButton resetFormulasButton;
    private TableRowSorter<FormulaTableModel> rowSorter;
    private String[] comboBoxItems = new String[] {"Name", "Tags", "Expression", "Variables"};

    //references
    private FormulaRecord selectedFormula = null;
    private FormulaTableModel model;

    static class FormulaTableModel extends AbstractTableModel {
        private final List<FormulaRecord> formulas;
        private final String[] columnNames = {"Formula Name", "Tags", "Calculator Expression", "Variables"};

        public FormulaTableModel(List<FormulaRecord> formulas) {
            this.formulas = formulas;
        }

        @Override
        public int getRowCount() {
            return formulas.size();
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
            FormulaRecord formula = formulas.get(row);
            return switch (column) {
                case 0 -> formula.name();
                case 1 -> String.join(", ", formula.tags());
                case 2 -> formula.expression();
                case 3 -> String.join(", ", formula.symbols());
                default -> "";
            };
        }

        // Method to retrieve full FormulaRecord object
        public FormulaRecord getFormulaAt(int row) {
            return formulas.get(row);
        }
    }

    public static void createMenu() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Formula Buddy"); // Set window title
            FormulaTableMenu menu = new FormulaTableMenu(); // Create an instance

            frame.setContentPane(menu.panel1); // Set the UI panel
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); // Auto-size based on UI components
            frame.setLocationRelativeTo(null);
            frame.setVisible(true); // Show window
        });
    }

    private FormulaTableMenu() {
        updateTable();

        // Create events
        searchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        useFormulaButton.addActionListener(_ -> {
            if (selectedFormula == null) return;
            UseFormulaMenu.createMenu(selectedFormula);
        });

        deleteFormulaButton.addActionListener(_ -> {
            AreYouSureMenu.CreateMenu("Once you delete a formula, it is gone for good!", () -> { // creates runnable
                if (selectedFormula == null) return;
                FileHandler.deleteFormulaRecord(selectedFormula);
                updateTable();
            });
        });

        resetFormulasButton.addActionListener(_ -> {
            AreYouSureMenu.CreateMenu("Resetting will delete all custom added formulas and restore only defaults!", () -> { // creates runnable
                FileHandler.resetFormulaRecords();
                updateTable();
            });
        });

        editFormulaButton.addActionListener(_ -> {
            AddFormulaMenu.createMenu(selectedFormula, this::updateTable);
        });

        addFormula.addActionListener(_ -> {
            AddFormulaMenu.createMenu(null, this::updateTable);
        });

        table1.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int viewRow = table1.getSelectedRow();
                if (viewRow != -1) { // If selected a row
                    int modelRow = table1.convertRowIndexToModel(viewRow);
                    selectedFormula = model.getFormulaAt(modelRow);
                    Icon latexIcon = FormulaProcessor.generateLatexIcon(selectedFormula.expression(), 30);
                    renderedFormulaLabel.setIcon(latexIcon);
                } else { // If unselected a row
                    selectedFormula = null;
                    renderedFormulaLabel.setIcon(null);
                }
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTable();
            }
        });
    }


    private void createUIComponents() { }

    private void updateTable() {
        List<FormulaRecord> formulaRecords = FileHandler.getFormulaRecords();
        model = new FormulaTableModel(formulaRecords);
        table1.setModel(model);
        rowSorter = new TableRowSorter<>(model);
        table1.setRowSorter(rowSorter);
        comboBox1.setModel(new DefaultComboBoxModel<>(comboBoxItems));
        selectedFormula = null;
        renderedFormulaLabel.setText(null);
    }

    private void filterTable() {
        String searchText = searchTextField.getText().trim();
        int selectedColumn = comboBox1.getSelectedIndex();

        if (searchText.isEmpty()) {
            rowSorter.setRowFilter(null);
            return;
        }

        String[] searchKeywords = searchText.split(",");
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        for (String keyword : searchKeywords) {
            keyword = keyword.trim();
            if (!keyword.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), selectedColumn));
            }
        }

        if (filters.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            // Combine filters with AND (all must match)
            RowFilter<Object, Object> combinedFilter = RowFilter.andFilter(filters);
            rowSorter.setRowFilter(combinedFilter);
        }
    }
}
