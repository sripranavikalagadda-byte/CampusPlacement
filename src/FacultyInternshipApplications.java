
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class FacultyInternshipApplications {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static JPanel createApplicationsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel titleLabel = new JLabel("INTERNSHIP APPLICATIONS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Application ID", "Student ID", "Student Name",
                "Branch", "CGPA", "Backlogs", "Company",
                "Internship Role", "Application Date", "Status",
                "Past Internships", "Actual Date"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0 || column == 5 || column == 10) {
                    return Integer.class;
                }
                if (column == 4) {
                    return BigDecimal.class;
                }
                if (column == 11) {
                    return java.sql.Date.class;
                }
                return String.class;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] widths = {
                100, 115, 150, 75, 65, 75, 140, 150, 115, 100
        };

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        // Keep filtering/sorting data in the model but hide helper columns.
        table.removeColumn(table.getColumnModel().getColumn(11));
        table.removeColumn(table.getColumnModel().getColumn(10));

        TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        
        // FILTER PANEL

        JPanel filterPanel = new JPanel(
                new GridLayout(2, 1, 4, 4)
        );

        filterPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Filter and Sort Applicants"
                )
        );

        // FILTER ROW 1

        JPanel firstFilterRow = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 4)
        );

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 26));

        JComboBox<String> branchCombo = new JComboBox<>(
                new String[]{"ALL"}
        );

        JTextField minCgpaField = new JTextField();
        minCgpaField.setPreferredSize(new Dimension(75, 26));

        firstFilterRow.add(new JLabel("Search ID/Name:"));
        firstFilterRow.add(searchField);
        firstFilterRow.add(new JLabel("Branch:"));
        firstFilterRow.add(branchCombo);
        firstFilterRow.add(new JLabel("Minimum CGPA:"));
        firstFilterRow.add(minCgpaField);

        // FILTER ROW 2

        JPanel secondFilterRow = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 4)
        );

        JTextField maxBacklogsField = new JTextField();
        maxBacklogsField.setPreferredSize(new Dimension(45, 26));

        JTextField minInternshipsField = new JTextField();
        minInternshipsField.setPreferredSize(new Dimension(50, 26));

        JComboBox<String> sortCombo = new JComboBox<>(
                new String[]{
                        "Newest Applications",
                        "Oldest Applications",
                        "Highest CGPA",
                        "Lowest CGPA",
                        "Fewest Backlogs",
                        "Most Backlogs"
                }
        );

        sortCombo.setPreferredSize(new Dimension(180, 26));

        JButton applyButton = new JButton("Apply Filters");
        JButton clearButton = new JButton("Clear");

        secondFilterRow.add(new JLabel("Max Backlogs:"));
        secondFilterRow.add(maxBacklogsField);
        secondFilterRow.add(new JLabel("Min Past Internships:"));
        secondFilterRow.add(minInternshipsField);
        secondFilterRow.add(new JLabel("Sort By:"));
        secondFilterRow.add(sortCombo);
        secondFilterRow.add(applyButton);
        secondFilterRow.add(clearButton);

        filterPanel.add(firstFilterRow);
        filterPanel.add(secondFilterRow);


        // EXISTING ACTION BUTTONS

        JButton shortlistButton = new JButton("Shortlist");
        JButton selectedButton = new JButton("Mark Selected");
        JButton rejectButton = new JButton("Reject");
        JButton refreshButton = new JButton("Refresh");

        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 5)
        );
        actionPanel.add(shortlistButton);
        actionPanel.add(selectedButton);
        actionPanel.add(rejectButton);
        actionPanel.add(refreshButton);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(actionPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        // Give the table the majority of the available height.
        mainPanel.remove(tablePanel);
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // LOAD APPLICATIONS

        loadApplications(model, mainPanel);
        updateBranches(model, branchCombo);

        // APPLY FILTERS AND SORTING

        Runnable applyFilters = () -> {
            try {
                String search = searchField.getText().trim();
                String branch = (String) branchCombo.getSelectedItem();

                String cgpaText = minCgpaField.getText().trim();
                String backlogText = maxBacklogsField.getText().trim();
                String internshipsText =
                        minInternshipsField.getText().trim();

                BigDecimal minCgpa = cgpaText.isEmpty()
                        ? null : new BigDecimal(cgpaText);

                Integer maxBacklogs = backlogText.isEmpty()
                        ? null : Integer.parseInt(backlogText);

                Integer minInternships = internshipsText.isEmpty()
                        ? null : Integer.parseInt(internshipsText);

                if (minCgpa != null
                        && (minCgpa.compareTo(BigDecimal.ZERO) < 0
                        || minCgpa.compareTo(new BigDecimal("10")) > 0)) {
                    throw new IllegalArgumentException(
                            "Minimum CGPA must be between 0 and 10."
                    );
                }

                if (maxBacklogs != null && maxBacklogs < 0) {
                    throw new IllegalArgumentException(
                            "Maximum backlogs cannot be negative."
                    );
                }

                if (minInternships != null && minInternships < 0) {
                    throw new IllegalArgumentException(
                            "Minimum past internships cannot be negative."
                    );
                }

                RowFilter<DefaultTableModel, Integer> filter =
                        new RowFilter<>() {
                    @Override
                    public boolean include(
                            Entry<? extends DefaultTableModel,
                                    ? extends Integer> entry) {

                        String studentId =
                                String.valueOf(entry.getValue(1));
                        String studentName =
                                String.valueOf(entry.getValue(2));
                        String studentBranch =
                                String.valueOf(entry.getValue(3));

                        if (!search.isEmpty()
                                && !studentId.toLowerCase().contains(
                                        search.toLowerCase())
                                && !studentName.toLowerCase().contains(
                                        search.toLowerCase())) {
                            return false;
                        }

                        if (branch != null && !branch.equals("ALL")
                                && !branch.equalsIgnoreCase(studentBranch)) {
                            return false;
                        }

                        Object cgpaValue = entry.getValue(4);
                        if (minCgpa != null && cgpaValue != null) {
                            BigDecimal cgpa =
                                    new BigDecimal(cgpaValue.toString());
                            if (cgpa.compareTo(minCgpa) < 0) {
                                return false;
                            }
                        } else if (minCgpa != null) {
                            return false;
                        }

                        int backlogs =
                                Integer.parseInt(entry.getValue(5).toString());

                        if (maxBacklogs != null
                                && backlogs > maxBacklogs) {
                            return false;
                        }

                        int pastInternships =
                                Integer.parseInt(entry.getValue(10).toString());

                        if (minInternships != null
                                && pastInternships < minInternships) {
                            return false;
                        }

                        return true;
                    }
                };

                sorter.setRowFilter(filter);

                String sortChoice =
                        (String) sortCombo.getSelectedItem();

                int sortColumn;
                SortOrder order;

                switch (sortChoice) {
                    case "Oldest Applications":
                        sortColumn = 11;
                        order = SortOrder.ASCENDING;
                        break;
                    case "Highest CGPA":
                        sortColumn = 4;
                        order = SortOrder.DESCENDING;
                        break;
                    case "Lowest CGPA":
                        sortColumn = 4;
                        order = SortOrder.ASCENDING;
                        break;
                    case "Fewest Backlogs":
                        sortColumn = 5;
                        order = SortOrder.ASCENDING;
                        break;
                    case "Most Backlogs":
                        sortColumn = 5;
                        order = SortOrder.DESCENDING;
                        break;
                    default:
                        sortColumn = 11;
                        order = SortOrder.DESCENDING;
                }

                sorter.setSortKeys(
                        List.of(new RowSorter.SortKey(sortColumn, order))
                );

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Enter valid numbers in the CGPA and filter fields."
                );
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainPanel, ex.getMessage());
            }
        };

        applyButton.addActionListener(e -> applyFilters.run());

        clearButton.addActionListener(e -> {
            searchField.setText("");
            branchCombo.setSelectedItem("ALL");
            minCgpaField.setText("");
            maxBacklogsField.setText("");
            minInternshipsField.setText("");
            sortCombo.setSelectedItem("Newest Applications");
            applyFilters.run();
        });

        // EXISTING STATUS ACTIONS

        shortlistButton.addActionListener(e ->
                updateApplicationStatus(
                        mainPanel, table, model, "Shortlisted"
                )
        );

        selectedButton.addActionListener(e ->
                updateApplicationStatus(
                        mainPanel, table, model, "Selected"
                )
        );

        rejectButton.addActionListener(e ->
                updateApplicationStatus(
                        mainPanel, table, model, "Rejected"
                )
        );

        refreshButton.addActionListener(e -> {
            loadApplications(model, mainPanel);
            updateBranches(model, branchCombo);
            applyFilters.run();
        });

        sortCombo.addActionListener(e -> applyFilters.run());

        return mainPanel;
    }

    private static void updateBranches(
            DefaultTableModel model,
            JComboBox<String> branchCombo) {

        String selected = (String) branchCombo.getSelectedItem();

        branchCombo.removeAllItems();
        branchCombo.addItem("ALL");

        Set<String> branches = new TreeSet<>(
                String.CASE_INSENSITIVE_ORDER
        );

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 3);
            if (value != null && !value.toString().isBlank()) {
                branches.add(value.toString());
            }
        }

        for (String branch : branches) {
            branchCombo.addItem(branch);
        }

        branchCombo.setSelectedItem(
                selected != null && branches.contains(selected)
                        ? selected : "ALL"
        );
    }

    private static void loadApplications(
            DefaultTableModel model,
            JPanel parent) {

        model.setRowCount(0);

        String sql = """
                SELECT
                    ia.application_id,
                    s.student_id,
                    s.name AS student_name,
                    s.department,
                    s.cgpa,
                    s.active_backlogs,
                    i.company_name,
                    i.internship_role,
                    ia.application_date,
                    ia.status,
                    s.past_internships
                FROM internship_applications ia
                JOIN students s
                    ON ia.student_id = s.student_id
                JOIN internships i
                    ON ia.internship_id = i.internship_id
                ORDER BY ia.application_date DESC
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if (connection == null) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Database connection failed."
                );
                return;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql);
                 ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    java.sql.Date applicationDate =
                       result.getDate("application_date");

                    String dateText = applicationDate == null
                            ? ""
                            : applicationDate.toLocalDate()
                                    .format(DATE_FORMAT);

                    model.addRow(new Object[]{
                            result.getInt("application_id"),
                            result.getString("student_id"),
                            result.getString("student_name"),
                            result.getString("department"),
                            result.getBigDecimal("cgpa"),
                            result.getInt("active_backlogs"),
                            result.getString("company_name"),
                            result.getString("internship_role"),
                            dateText,
                            result.getString("status"),
                            result.getInt("past_internships"),
                            applicationDate
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Error loading internship applications:\n"
                            + ex.getMessage()
            );
        }
    }

    private static void updateApplicationStatus(
            JPanel parent,
            JTable table,
            DefaultTableModel model,
            String newStatus) {

        int selectedViewRow = table.getSelectedRow();

        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Please select an application first."
            );
            return;
        }

        int row = table.convertRowIndexToModel(selectedViewRow);

        int applicationId = Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );

        String currentStatus = model.getValueAt(row, 9).toString();

        boolean validTransition;

        switch (newStatus) {
            case "Shortlisted":
                validTransition =
                        "Applied".equalsIgnoreCase(currentStatus);
                break;
            case "Selected":
                validTransition =
                        "Shortlisted".equalsIgnoreCase(currentStatus);
                break;
            case "Rejected":
                validTransition =
                        "Applied".equalsIgnoreCase(currentStatus)
                        || "Shortlisted".equalsIgnoreCase(currentStatus);
                break;
            default:
                validTransition = false;
        }

        if (!validTransition) {
            JOptionPane.showMessageDialog(
                    parent,
                    switch (newStatus) {
                        case "Shortlisted" ->
                                "Only Applied applications can be shortlisted.";
                        case "Selected" ->
                                "Shortlist the application before marking it Selected.";
                        case "Rejected" ->
                                "Only Applied or Shortlisted applications can be rejected.";
                        default -> "Invalid status change.";
                    }
            );
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                parent,
                "Change application status from "
                        + currentStatus + " to " + newStatus + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = """
                UPDATE internship_applications
                SET status = ?
                WHERE application_id = ?
                  AND status = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if (connection == null) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Database connection failed."
                );
                return;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, newStatus);
                statement.setInt(2, applicationId);
                statement.setString(3, currentStatus);

                int updated = statement.executeUpdate();

                if (updated == 1) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "Application status updated to "
                                    + newStatus + "."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            parent,
                            "The application changed. Refresh and try again."
                    );
                }

                loadApplications(model, parent);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Error updating application:\n" + ex.getMessage()
            );
        }
    }
}
