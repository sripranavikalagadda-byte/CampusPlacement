import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FacultyInternships {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static JPanel createInternshipsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("INTERNSHIP MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "ID",
                "Company",
                "Role",
                "Duration",
                "Stipend",
                "Min CGPA",
                "Max Backlogs",
                "Eligible Departments",
                "Start Date",
                "Last Date",
                "Applicants",
                "Status"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton addButton = new JButton("Add Internship");
        JButton editButton = new JButton("Edit Internship");
        JButton closeButton = new JButton("Close Internship");
        JButton deleteButton = new JButton("Delete Internship");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        loadInternships(model);

        addButton.addActionListener(e ->
        showInternshipDialog(
                mainPanel,
                model,
                -1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Open"
        )
);

        editButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an internship to edit."
                );
                return;
            }

            int row = table.convertRowIndexToModel(selectedRow);

            int internshipId =
                    Integer.parseInt(model.getValueAt(row, 0).toString());

            String company =
                    model.getValueAt(row, 1).toString();

            String role =
                    model.getValueAt(row, 2).toString();

            String duration =
                    model.getValueAt(row, 3).toString();

            String stipend =
                    model.getValueAt(row, 4).toString();

            String minCgpa =
                    model.getValueAt(row, 5).toString();

            String maxBacklogs =
                    model.getValueAt(row, 6).toString();

            String departments =
                    model.getValueAt(row, 7).toString();

            String startDate =
                    model.getValueAt(row, 8).toString();

            String lastDate =
                    model.getValueAt(row, 9).toString();

            String status =
                    model.getValueAt(row, 11).toString();

            showInternshipDialog(
                    mainPanel,
                    model,
                    internshipId,
                    company,
                    role,
                    duration,
                    stipend,
                    minCgpa,
                    maxBacklogs,
                    departments,
                    startDate,
                    lastDate,
                    status
            );
        });

        closeButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an internship to close."
                );
                return;
            }

            int row = table.convertRowIndexToModel(selectedRow);

            int internshipId =
                    Integer.parseInt(model.getValueAt(row, 0).toString());

            String status =
                    model.getValueAt(row, 11).toString();

            if ("Closed".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "This internship is already closed."
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Close this internship drive?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            String sql =
                    "UPDATE internships SET status = 'Closed' WHERE internship_id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(1, internshipId);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Internship closed successfully."
                );

                loadInternships(model);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error closing internship: " + ex.getMessage()
                );
            }
        });

        deleteButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an internship to delete."
                );
                return;
            }

            int row = table.convertRowIndexToModel(selectedRow);

            int internshipId =
                    Integer.parseInt(model.getValueAt(row, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Delete this internship?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            String sql =
                    "DELETE FROM internships WHERE internship_id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(1, internshipId);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Internship deleted successfully."
                );

                loadInternships(model);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error deleting internship: " + ex.getMessage()
                );
            }
        });

        refreshButton.addActionListener(e ->
                loadInternships(model)
        );

        return mainPanel;
    }

    private static void loadInternships(DefaultTableModel model) {

        model.setRowCount(0);

        String sql =
                "SELECT i.internship_id, " +
                "i.company_name, " +
                "i.internship_role, " +
                "i.duration, " +
                "i.stipend, " +
                "i.min_cgpa, " +
                "i.max_backlogs, " +
                "i.eligible_departments, " +
                "i.start_date, " +
                "i.last_date_to_apply, " +
                "(SELECT COUNT(*) FROM internship_applications ia " +
                " WHERE ia.internship_id = i.internship_id) AS applicants, " +
                "i.status " +
                "FROM internships i " +
                "ORDER BY i.internship_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Date startDate = resultSet.getDate("start_date");
                Date lastDate = resultSet.getDate("last_date_to_apply");

                String startDateText =
                        startDate == null
                                ? ""
                                : startDate.toLocalDate().format(DATE_FORMAT);

                String lastDateText =
                        lastDate == null
                                ? ""
                                : lastDate.toLocalDate().format(DATE_FORMAT);

                model.addRow(new Object[]{
                        resultSet.getInt("internship_id"),
                        resultSet.getString("company_name"),
                        resultSet.getString("internship_role"),
                        resultSet.getString("duration"),
                        resultSet.getBigDecimal("stipend"),
                        resultSet.getBigDecimal("min_cgpa"),
                        resultSet.getInt("max_backlogs"),
                        resultSet.getString("eligible_departments"),
                        startDateText,
                        lastDateText,
                        resultSet.getInt("applicants"),
                        resultSet.getString("status")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading internships: " + ex.getMessage()
            );
        }
    }

    private static void showInternshipDialog(
            JPanel parent,
            DefaultTableModel model,
            int internshipId,
            String existingCompany,
            String existingRole,
            String existingDuration,
            String existingStipend,
            String existingMinCgpa,
            String existingMaxBacklogs,
            String existingDepartments,
            String existingStartDate,
            String existingLastDate,
            String existingStatus) {

        boolean editMode = internshipId != -1;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(parent),
                editMode ? "Edit Internship" : "Add Internship",
                true
        );

        dialog.setSize(500, 650);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField companyField =
                new JTextField(existingCompany == null ? "" : existingCompany);

        JTextField roleField =
                new JTextField(existingRole == null ? "" : existingRole);

        JTextField durationField =
                new JTextField(existingDuration == null ? "" : existingDuration);

        JTextField stipendField =
                new JTextField(existingStipend == null ? "" : existingStipend);

        JTextField minCgpaField =
                new JTextField(existingMinCgpa == null ? "" : existingMinCgpa);

        JTextField maxBacklogsField =
                new JTextField(existingMaxBacklogs == null ? "" : existingMaxBacklogs);

        JTextField departmentsField =
                new JTextField(existingDepartments == null ? "" : existingDepartments);

        JTextField startDateField =
                new JTextField(existingStartDate == null ? "" : existingStartDate);

        JTextField lastDateField =
                new JTextField(existingLastDate == null ? "" : existingLastDate);

        JComboBox<String> statusCombo =
                new JComboBox<>(new String[]{"Open", "Closed"});

        statusCombo.setSelectedItem(
                existingStatus == null ? "Open" : existingStatus
        );

        int row = 0;

        addField(panel, gbc, row++, "Company Name:", companyField);
        addField(panel, gbc, row++, "Internship Role:", roleField);
        addField(panel, gbc, row++, "Duration:", durationField);
        addField(panel, gbc, row++, "Stipend:", stipendField);
        addField(panel, gbc, row++, "Minimum CGPA:", minCgpaField);
        addField(panel, gbc, row++, "Maximum Backlogs:", maxBacklogsField);
        addField(panel, gbc, row++, "Eligible Departments:", departmentsField);
        addField(panel, gbc, row++, "Start Date (DD-MM-YYYY):", startDateField);
        addField(panel, gbc, row++, "Last Date (DD-MM-YYYY):", lastDateField);
        addField(panel, gbc, row++, "Status:", statusCombo);

        JButton saveButton =
                new JButton(editMode ? "Update Internship" : "Add Internship");

        JButton cancelButton =
                new JButton("Cancel");

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        panel.add(buttonPanel, gbc);

        saveButton.addActionListener(e -> {

            String company = companyField.getText().trim();
            String role = roleField.getText().trim();
            String duration = durationField.getText().trim();
            String stipendText = stipendField.getText().trim();
            String minCgpaText = minCgpaField.getText().trim();
            String maxBacklogsText = maxBacklogsField.getText().trim();
            String departments = departmentsField.getText().trim();
            String startDateText = startDateField.getText().trim();
            String lastDateText = lastDateField.getText().trim();
            String status = statusCombo.getSelectedItem().toString();

            if (company.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Company name and internship role are required."
                );
                return;
            }

            if (stipendText.isEmpty() ||
                    minCgpaText.isEmpty() ||
                    maxBacklogsText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Stipend, minimum CGPA and maximum backlogs are required."
                );
                return;
            }

            try {

                double stipend = Double.parseDouble(stipendText);
                double minCgpa = Double.parseDouble(minCgpaText);
                int maxBacklogs = Integer.parseInt(maxBacklogsText);

                LocalDate startDate =
                        LocalDate.parse(startDateText, DATE_FORMAT);

                LocalDate lastDate =
                        LocalDate.parse(lastDateText, DATE_FORMAT);

                if (lastDate.isBefore(startDate)) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Last date cannot be before the start date."
                    );
                    return;
                }

                String sql;

                if (editMode) {

                    sql =
                            "UPDATE internships SET " +
                            "company_name = ?, " +
                            "internship_role = ?, " +
                            "duration = ?, " +
                            "stipend = ?, " +
                            "min_cgpa = ?, " +
                            "max_backlogs = ?, " +
                            "eligible_departments = ?, " +
                            "start_date = ?, " +
                            "last_date_to_apply = ?, " +
                            "status = ? " +
                            "WHERE internship_id = ?";

                } else {

                    sql =
                            "INSERT INTO internships " +
                            "(company_name, internship_role, duration, stipend, " +
                            "min_cgpa, max_backlogs, eligible_departments, " +
                            "start_date, last_date_to_apply, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                }

                try (Connection connection =
                             DatabaseConnection.getConnection();
                     PreparedStatement statement =
                             connection.prepareStatement(sql)) {

                    statement.setString(1, company);
                    statement.setString(2, role);
                    statement.setString(3, duration);
                    statement.setDouble(4, stipend);
                    statement.setDouble(5, minCgpa);
                    statement.setInt(6, maxBacklogs);
                    statement.setString(7, departments);
                    statement.setDate(8, Date.valueOf(startDate));
                    statement.setDate(9, Date.valueOf(lastDate));
                    statement.setString(10, status);

                    if (editMode) {
                        statement.setInt(11, internshipId);
                    }

                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(
                            dialog,
                            editMode
                                    ? "Internship updated successfully."
                                    : "Internship added successfully."
                    );

                    dialog.dispose();
                    loadInternships(model);
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter valid numeric values."
                );

            } catch (DateTimeParseException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter dates in DD-MM-YYYY format."
                );

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Database error: " + ex.getMessage()
                );
            }
        });

        cancelButton.addActionListener(e ->
                dialog.dispose()
        );

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private static void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent field) {

        gbc.gridy = row;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }
}