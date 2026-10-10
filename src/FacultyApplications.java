import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FacultyApplications {

    public static JPanel createApplicationsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // TITLE
        JLabel title = new JLabel(
                "STUDENT APPLICATIONS",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));

        // FILTER PANEL
        JPanel filtersPanel = new JPanel(
                new GridLayout(2, 1, 4, 4)
        );

        filtersPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Filter and Sort Applicants"
                        ),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)
                )
        );

        // FIRST FILTER ROW
        JPanel firstFilterRow = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 4)
        );

        JTextField searchField = new JTextField(12);

        JComboBox<String> departmentBox = new JComboBox<>(
                new String[]{
                        "ALL", "CSE", "EEE", "IT",
                        "ETM", "ECE", "CSM", "CSD"
                }
        );

        JTextField minCgpaField = new JTextField(5);

        firstFilterRow.add(new JLabel("Search ID/Name:"));
        firstFilterRow.add(searchField);

        firstFilterRow.add(new JLabel("Branch:"));
        firstFilterRow.add(departmentBox);

        firstFilterRow.add(new JLabel("Minimum CGPA:"));
        firstFilterRow.add(minCgpaField);

        // SECOND FILTER ROW
        JPanel secondFilterRow = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 4)
        );

        JTextField maxBacklogsField = new JTextField(3);
        JTextField minPastInternshipsField = new JTextField(3);

        JComboBox<String> sortBox = new JComboBox<>(
                new String[]{
                        "Newest Applications",
                        "Highest CGPA",
                        "Most Past Internships",
                        "Highest CGPA, then Internships"
                }
        );

        JButton applyFiltersButton = new JButton("Apply Filters");
        JButton clearButton = new JButton("Clear");
        JButton refreshButton = new JButton("Refresh");

        secondFilterRow.add(new JLabel("Max Backlogs:"));
        secondFilterRow.add(maxBacklogsField);

        secondFilterRow.add(new JLabel("Min Past Internships:"));
        secondFilterRow.add(minPastInternshipsField);

        secondFilterRow.add(new JLabel("Sort By:"));
        secondFilterRow.add(sortBox);

        secondFilterRow.add(applyFiltersButton);
        secondFilterRow.add(clearButton);
        secondFilterRow.add(refreshButton);

        filtersPanel.add(firstFilterRow);
        filtersPanel.add(secondFilterRow);

        JPanel northPanel = new JPanel(
                new BorderLayout(8, 8)
        );

        northPanel.add(title, BorderLayout.NORTH);
        northPanel.add(filtersPanel, BorderLayout.CENTER);

        mainPanel.add(northPanel, BorderLayout.NORTH);

        // APPLICATION TABLE
        String[] columns = {
                "Application ID",
                "Student ID",
                "Student Name",
                "Branch",
                "CGPA",
                "Backlogs",
                "Past Internships",
                "Company",
                "Job Role",
                "Package",
                "Applied Date",
                "Status"
        };

        DefaultTableModel model = new DefaultTableModel(
                columns, 0
        ) {
            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] columnWidths = {
                100, 115, 150, 75, 65, 70,
                115, 140, 140, 85, 105, 100
        };

        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(columnWidths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ACTION BUTTONS
        JLabel instructionLabel = new JLabel(
                "Select an application with status Applied."
        );

        JButton shortlistButton = new JButton("Shortlist");
        JButton rejectButton = new JButton("Reject");

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        buttonPanel.add(instructionLabel);
        buttonPanel.add(shortlistButton);
        buttonPanel.add(rejectButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // RELOAD USING THE SELECTED FILTERS
        Runnable reload = () -> loadApplications(
                model,
                searchField.getText(),
                (String) departmentBox.getSelectedItem(),
                minCgpaField.getText(),
                maxBacklogsField.getText(),
                minPastInternshipsField.getText(),
                (String) sortBox.getSelectedItem()
        );

        applyFiltersButton.addActionListener(
                e -> reload.run()
        );

        refreshButton.addActionListener(
                e -> reload.run()
        );

        clearButton.addActionListener(e -> {
            searchField.setText("");
            departmentBox.setSelectedItem("ALL");
            minCgpaField.setText("");
            maxBacklogsField.setText("");
            minPastInternshipsField.setText("");
            sortBox.setSelectedItem("Newest Applications");
            reload.run();
        });

        // SHORTLIST
        shortlistButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an application first."
                );
                return;
            }

            String status = model.getValueAt(row, 11).toString();

            if (!status.equals("Applied")) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Only applications with status 'Applied' "
                                + "can be shortlisted."
                );
                return;
            }

            int applicationId = Integer.parseInt(
                    model.getValueAt(row, 0).toString()
            );

            updateApplicationStatus(
                    applicationId, "Shortlisted", reload
            );
        });

        // REJECT
        rejectButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an application first."
                );
                return;
            }

            String status = model.getValueAt(row, 11).toString();

            if (!status.equals("Applied")) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Only applications with status 'Applied' "
                                + "can be rejected."
                );
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Are you sure you want to reject this application?",
                    "Reject Application",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                int applicationId = Integer.parseInt(
                        model.getValueAt(row, 0).toString()
                );

                updateApplicationStatus(
                        applicationId, "Rejected", reload
                );
            }
        });

        // INITIAL LOAD
        loadApplications(
                model,
                "",
                "ALL",
                "",
                "",
                "",
                "Newest Applications"
        );

        return mainPanel;
    }

    // LOAD AND FILTER APPLICATIONS
    private static void loadApplications(
            DefaultTableModel model,
            String searchText,
            String department,
            String minCgpaText,
            String maxBacklogsText,
            String minPastInternshipsText,
            String sortOption) {

        Double minCgpa = null;
        Integer maxBacklogs = null;
        Integer minPastInternships = null;

        // VALIDATE FILTER VALUES
        try {

            if (minCgpaText != null
                    && !minCgpaText.trim().isEmpty()) {

                minCgpa = Double.parseDouble(
                        minCgpaText.trim()
                );

                if (minCgpa < 0 || minCgpa > 10) {
                    throw new IllegalArgumentException(
                            "Minimum CGPA must be between 0 and 10."
                    );
                }
            }

            if (maxBacklogsText != null
                    && !maxBacklogsText.trim().isEmpty()) {

                maxBacklogs = Integer.parseInt(
                        maxBacklogsText.trim()
                );

                if (maxBacklogs < 0) {
                    throw new IllegalArgumentException(
                            "Maximum backlogs cannot be negative."
                    );
                }
            }

            if (minPastInternshipsText != null
                    && !minPastInternshipsText.trim().isEmpty()) {

                minPastInternships = Integer.parseInt(
                        minPastInternshipsText.trim()
                );

                if (minPastInternships < 0) {
                    throw new IllegalArgumentException(
                            "Minimum past internships cannot be negative."
                    );
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Enter valid numbers for CGPA, backlogs, "
                            + "and past internships.",
                    "Invalid Filter",
                    JOptionPane.WARNING_MESSAGE
            );

            return;

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Invalid Filter",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // SQL QUERY
        StringBuilder sql = new StringBuilder("""
                SELECT
                    a.application_id,
                    s.student_id,
                    s.name,
                    s.department,
                    s.cgpa,
                    s.active_backlogs,
                    COALESCE(s.past_internships, 0) AS past_internships,
                    c.company_name,
                    c.job_role,
                    c.package_lpa,
                    a.application_date,
                    a.status
                FROM applications a
                JOIN students s
                    ON a.student_id = s.student_id
                JOIN companies c
                    ON a.company_id = c.company_id
                WHERE 1 = 1
                """);

        List<Object> parameters = new ArrayList<>();

        // BRANCH FILTER
        if (department != null && !department.equals("ALL")) {
            sql.append(" AND s.department = ?");
            parameters.add(department);
        }

        // SEARCH BY STUDENT ID OR NAME
        if (searchText != null && !searchText.trim().isEmpty()) {

            sql.append(
                    " AND (s.student_id LIKE ? OR s.name LIKE ?)"
            );

            String pattern = "%" + searchText.trim() + "%";

            parameters.add(pattern);
            parameters.add(pattern);
        }

        // MINIMUM CGPA
        if (minCgpa != null) {
            sql.append(" AND s.cgpa >= ?");
            parameters.add(minCgpa);
        }

        // MAXIMUM ACTIVE BACKLOGS
        if (maxBacklogs != null) {
            sql.append(" AND s.active_backlogs <= ?");
            parameters.add(maxBacklogs);
        }

        // MINIMUM PAST INTERNSHIPS
        if (minPastInternships != null) {
            sql.append(
                    " AND COALESCE(s.past_internships, 0) >= ?"
            );
            parameters.add(minPastInternships);
        }

        // SORT RESULTS
        if ("Highest CGPA".equals(sortOption)) {

            sql.append(
                    " ORDER BY s.cgpa DESC, a.application_id DESC"
            );

        } else if ("Most Past Internships".equals(sortOption)) {

            sql.append(
                    " ORDER BY COALESCE(s.past_internships, 0) DESC, "
                            + "s.cgpa DESC, a.application_id DESC"
            );

        } else if ("Highest CGPA, then Internships".equals(sortOption)) {

            sql.append(
                    " ORDER BY s.cgpa DESC, "
                            + "COALESCE(s.past_internships, 0) DESC, "
                            + "a.application_id DESC"
            );

        } else {

            sql.append(" ORDER BY a.application_id DESC");
        }

        model.setRowCount(0);

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql.toString())
        ) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy");

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    String applicationDate = "";
                    Date date = result.getDate("application_date");

                    if (date != null) {
                        applicationDate = date.toLocalDate()
                                .format(formatter);
                    }

                    model.addRow(new Object[]{
                            result.getInt("application_id"),
                            result.getString("student_id"),
                            result.getString("name"),
                            result.getString("department"),
                            result.getDouble("cgpa"),
                            result.getInt("active_backlogs"),
                            result.getInt("past_internships"),
                            result.getString("company_name"),
                            result.getString("job_role"),
                            result.getDouble("package_lpa") + " LPA",
                            applicationDate,
                            result.getString("status")
                    });
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Database error: " + e.getMessage()
                            + "\nCheck that the students table has "
                            + "a past_internships column.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // UPDATE APPLICATION STATUS
    private static void updateApplicationStatus(
            int applicationId,
            String newStatus,
            Runnable reload) {

        String sql = """
                UPDATE applications
                SET status = ?
                WHERE application_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, newStatus);
            statement.setInt(2, applicationId);

            int rows = statement.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                        null,
                        "Application status updated to "
                                + newStatus + "."
                );

                reload.run();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Application could not be updated."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Database error: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}