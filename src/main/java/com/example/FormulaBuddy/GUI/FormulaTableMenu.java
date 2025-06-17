package com.example.FormulaBuddy.GUI;

import com.example.FormulaBuddy.FileHandler;
import com.example.FormulaBuddy.FormulaProcessor;
import com.example.FormulaBuddy.FormulaRecord;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

public class FormulaTableMenu {
    private JPanel panel1;
    private JTable table1;
    private JButton deleteFormulaButton;
    private JButton useFormulaButton;
    private JButton editFormulaButton;
    private JButton mainMenuButton;
    private JTextField searchTextField;
    private JLabel renderedFormulaLabel;
    private TableRowSorter<FormulaTableModel> rowSorter;

    static class FormulaTableModel extends AbstractTableModel {
        private final List<FormulaRecord> formulas;
        private final String[] columnNames = {"Formula Name", "Calculator Expression", "Variables"};

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
                case 1 -> formula.expression();
                case 2 -> String.join(", ", formula.symbols());
                default -> "";
            };
        }

        // Method to retrieve full FormulaRecord object
        public FormulaRecord getFormulaAt(int row) {
            return formulas.get(row);
        }
    }

    public FormulaTableMenu() {

        // Create search events
        searchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }

            private void filterTable() {
                String searchText = searchTextField.getText().trim();
                if (searchText.isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
                }
            }
        });

        // Populate table and create selection event
        List<FormulaRecord> formulaRecords = FileHandler.getFormulaRecords();
        FormulaTableModel model = new FormulaTableModel(formulaRecords);
        table1.setModel(model);
        rowSorter = new TableRowSorter<>(model);
        table1.setRowSorter(rowSorter);
        renderedFormulaLabel.setText(null);

        table1.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    FormulaRecord selectedFormula = model.getFormulaAt(selectedRow);
                    Icon latexIcon = FormulaProcessor.generateLatexIcon(selectedFormula.expression());
                    renderedFormulaLabel.setIcon(latexIcon);
                }
            }
        });
    }

    private void createUIComponents() {
        renderedFormulaLabel = new JLabel("");
        renderedFormulaLabel.setText(null);

        // Table creation
//        List<FormulaRecord> formulaRecords = FileHandler.getFormulaRecords();
//        FormulaTableModel model = new FormulaTableModel(formulaRecords);
//        table1.setModel(model);

//        String[] columnNames = {"Formula Name", "Calculator Expression", "Variables"};
//        List<FormulaRecord> formulaRecords = FileHandler.getFormulaRecords();
//        String[][] data = new String[formulaRecords.size()][3];
//
//        for (int i = 0; i < formulaRecords.size(); i++) {
//            FormulaRecord record = formulaRecords.get(i);
//            data[i][0] = record.name();                                 // Formula Name
//            data[i][1] = record.expression();                           // Calculator Expression
//            data[i][2] = String.join(", ", record.symbols());   // Symbols (Set -> String)
//        }
//
//        DefaultTableModel model = new DefaultTableModel(data, columnNames);
//        table1.setModel(model);
//        rowSorter = new TableRowSorter<>(model);
//        table1.setRowSorter(rowSorter);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Formula Buddy"); // Set window title
            FormulaTableMenu menu = new FormulaTableMenu(); // Create an instance

            frame.setContentPane(menu.panel1); // Set the UI panel
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); // Auto-size based on UI components
            frame.setVisible(true); // Show window
        });
    }
}
