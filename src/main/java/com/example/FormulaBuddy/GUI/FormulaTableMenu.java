package com.example.FormulaBuddy.GUI;

import com.example.FormulaBuddy.FileHandler;
import com.example.FormulaBuddy.FormulaProcessor;
import com.example.FormulaBuddy.FormulaRecord;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final String[] comboBoxItems = new String[]{"Name", "Tags", "Expression", "Variables"};

    //references
    private FormulaRecord selectedFormula = null;
    private FormulaTableModel model;

    private void setupUI() {
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        GridBagConstraints c = new GridBagConstraints();

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Logo64x64.png"))));
        c.gridx = 3;
        c.gridy = 0;
        panel1.add(logoLabel, c);

        // Title: Formula
        JLabel formulaTitle = new JLabel("Formula");
        formulaTitle.setFont(new Font("Default", Font.BOLD, 20));
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;
        panel1.add(formulaTitle, c);

        // Title: Buddy
        JLabel buddyTitle = new JLabel("Buddy");
        buddyTitle.setFont(new Font("Default", Font.BOLD, 20));
        c.gridx = 4;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        panel1.add(buddyTitle, c);

        // Search Label
        JLabel searchLabel = new JLabel("Search:");
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        panel1.add(searchLabel, c);

        // Search TextField
        searchTextField = new JTextField(20);
        c.gridx = 2;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(searchTextField, c);

        // Search By Label
        JLabel searchByLabel = new JLabel("Search By:");
        c.gridx = 5;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        panel1.add(searchByLabel, c);

        // ComboBox
        comboBox1 = new JComboBox<>();
        c.gridx = 6;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBox1, c);

        // Search Help Label
        JLabel helpLabel = new JLabel("Separate search terms with a comma (,)");
        helpLabel.setFont(new Font("Default", Font.PLAIN, 10));
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 5;
        panel1.add(helpLabel, c);

        // Table
        table1 = new JTable();
        table1.setPreferredScrollableViewportSize(new Dimension(600, 400));
        JScrollPane scrollPane = new JScrollPane(table1);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 8;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        panel1.add(scrollPane, c);

        // Rendered Formula Labels
        JLabel renderedFormulaText = new JLabel("Rendered Formula");
        c.gridy = 4;
        c.gridx = 1;
        c.gridwidth = 7;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        panel1.add(renderedFormulaText, c);

        renderedFormulaLabel = new JLabel("Rendered Formula");
        c.gridy = 5;
        panel1.add(renderedFormulaLabel, c);

        // Buttons
        useFormulaButton = new JButton("Use Formula");
        c.gridy = 6;
        c.gridx = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(useFormulaButton, c);

        addFormula = new JButton("Add New Formula");
        c.gridx = 2;
        panel1.add(addFormula, c);

        editFormulaButton = new JButton("Edit Formula");
        c.gridx = 3;
        panel1.add(editFormulaButton, c);

        deleteFormulaButton = new JButton("Delete Formula");
        deleteFormulaButton.setForeground(new Color(-1769216));
        c.gridx = 4;
        c.gridwidth = 3;
        panel1.add(deleteFormulaButton, c);

        resetFormulasButton = new JButton("Reset Formulas");
        resetFormulasButton.setForeground(new Color(-1769216));
        c.gridx = 7;
        c.gridwidth = 1;
        panel1.add(resetFormulasButton, c);
    }

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
        setupUI();
        updateTable();

        // Create events
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        useFormulaButton.addActionListener(e -> {
            if (selectedFormula == null) return;
            UseFormulaMenu.createMenu(selectedFormula);
        });

        deleteFormulaButton.addActionListener(e -> {
            AreYouSureMenu.CreateMenu("Once you delete a formula, it is gone for good!", () -> { // creates runnable
                if (selectedFormula == null) return;
                FileHandler.deleteFormulaRecord(selectedFormula);
                updateTable();
            });
        });

        resetFormulasButton.addActionListener(e -> {
            AreYouSureMenu.CreateMenu("Resetting will delete all custom added formulas and restore only defaults!", () -> { // creates runnable
                FileHandler.resetFormulaRecords();
                updateTable();
            });
        });

        editFormulaButton.addActionListener(e -> {
            AddFormulaMenu.createMenu(selectedFormula, this::updateTable);
        });

        addFormula.addActionListener(e -> {
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
