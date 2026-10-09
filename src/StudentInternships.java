import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudentInternships {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static JPanel createInternshipsPanel(String studentId) {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel title = new JLabel("INTERNSHIP PORTAL");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        mainPanel.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // Available internships table
        String[] internshipColumns = {
                "ID", "Company", "Role", "Duration", "Stipend",
                "Min CGPA", "Max Backlogs", "Eligible Departments",
                "Start Date", "Last Date", "Applicants",
                "Eligibility", "Status"
        };

        DefaultTableModel internshipModel =
                createTableModel(internshipColumns);

        JTable internshipTable = new JTable(internshipModel);
        internshipTable.setRowHeight(25);
        internshipTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JButton applyButton = new JButton("Apply");
        JButton refreshInternshipsButton = new JButton("Refresh");

        JPanel internshipButtons =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        internshipButtons.add(applyButton);
        internshipButtons.add(refreshInternshipsButton);

        JPanel availablePanel = new JPanel(new BorderLayout(10, 10));
        availablePanel.add(internshipButtons, BorderLayout.NORTH);
        availablePanel.add(
                new JScrollPane(internshipTable),
                BorderLayout.CENTER
        );

        tabs.addTab("Available Internships", availablePanel);

        // My internship applications table
        String[] applicationColumns = {
                "Application ID", "Company", "Internship Role",
                "Duration", "Stipend", "Date Applied", "Status"
        };

        DefaultTableModel applicationModel =
                createTableModel(applicationColumns);

        JTable applicationTable = new JTable(applicationModel);
        applicationTable.setRowHeight(25);
        applicationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JButton refreshApplicationsButton =
                new JButton("Refresh Applications");

        JPanel applicationButtons =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        applicationButtons.add(refreshApplicationsButton);

        JPanel myApplicationsPanel =
                new JPanel(new BorderLayout(10, 10));

        myApplicationsPanel.add(
                applicationButtons,
                BorderLayout.NORTH
        );

        myApplicationsPanel.add(
                new JScrollPane(applicationTable),
                BorderLayout.CENTER
        );

        tabs.addTab("My Internship Applications", myApplicationsPanel);

        mainPanel.add(tabs, BorderLayout.CENTER);

        // Initial loading
        loadInternships(internshipModel, studentId, mainPanel);
        loadMyApplications(applicationModel, studentId, mainPanel);

        // Apply for selected internship
        applyButton.addActionListener(e -> {

            int selectedRow = internshipTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an internship first."
                );
                return;
            }

            int row = internshipTable.convertRowIndexToModel(selectedRow);

            int internshipId = Integer.parseInt(
                    internshipModel.getValueAt(row, 0).toString()
            );

            String eligibility =
                    internshipModel.getValueAt(row, 11).toString();

            String status =
                    internshipModel.getValueAt(row, 12).toString();

            String deadline =
                    internshipModel.getValueAt(row, 9).toString();

            if (!"Eligible".equalsIgnoreCase(eligibility)) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "You cannot apply. Eligibility: " + eligibility
                );
                return;
            }

            if (!"Open".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "This internship is closed."
                );
                return;
            }

            try {
                LocalDate lastDate =
                        LocalDate.parse(deadline, DATE_FORMAT);

                if (LocalDate.now().isAfter(lastDate)) {
                    JOptionPane.showMessageDialog(
                            mainPanel,
                            "The application deadline has passed."
                    );
                    return;
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "The application deadline is invalid."
                );
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Submit your application for this internship?",
                    "Confirm Application",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }

            String sql = """
                    INSERT INTO internship_applications
                    (student_id, internship_id, application_date, status)
                    VALUES (?, ?, CURRENT_DATE, 'Applied')
                    """;

            try (Connection connection =
                         DatabaseConnection.getConnection()) {

                if (connection == null) {
                    JOptionPane.showMessageDialog(
                            mainPanel,
                            "Database connection failed."
                    );
                    return;
                }

                // Recheck that the internship remains open
                // and the deadline has not passed.
                String checkSql = """
                        SELECT status, last_date_to_apply
                        FROM internships
                        WHERE internship_id = ?
                        """;

                try (PreparedStatement check =
                             connection.prepareStatement(checkSql)) {

                    check.setInt(1, internshipId);

                    try (ResultSet result = check.executeQuery()) {

                        if (!result.next()
                                || !"Open".equalsIgnoreCase(
                                        result.getString("status"))) {

                            JOptionPane.showMessageDialog(
                                    mainPanel,
                                    "This internship is no longer open."
                            );
                            loadInternships(
                                    internshipModel, studentId, mainPanel
                            );
                            return;
                        }

                        Date dbDeadline =
                                result.getDate("last_date_to_apply");

                        if (dbDeadline == null
                                || LocalDate.now().isAfter(
                                        dbDeadline.toLocalDate())) {

                            JOptionPane.showMessageDialog(
                                    mainPanel,
                                    "The application deadline has passed."
                            );
                            loadInternships(
                                    internshipModel, studentId, mainPanel
                            );
                            return;
                        }
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(sql)) {

                    statement.setString(1, studentId);
                    statement.setInt(2, internshipId);

                    statement.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Internship application submitted successfully!"
                );

                loadInternships(
                        internshipModel, studentId, mainPanel
                );

                loadMyApplications(
                        applicationModel, studentId, mainPanel
                );

                tabs.setSelectedIndex(1);

            } catch (SQLIntegrityConstraintViolationException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "You have already applied for this internship."
                );

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error submitting application:\n" + ex.getMessage()
                );
            }
        });

        refreshInternshipsButton.addActionListener(e ->
                loadInternships(
                        internshipModel, studentId, mainPanel
                )
        );

        refreshApplicationsButton.addActionListener(e ->
                loadMyApplications(
                        applicationModel, studentId, mainPanel
                )
        );

        return mainPanel;
    }

    private static DefaultTableModel createTableModel(
            String[] columns) {

        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static void loadInternships(
            DefaultTableModel model,
            String studentId,
            JPanel parent) {

        model.setRowCount(0);

        String studentSql = """
                SELECT department, cgpa, active_backlogs
                FROM students
                WHERE student_id = ?
                """;

        String internshipSql = """
                SELECT i.internship_id,
                       i.company_name,
                       i.internship_role,
                       i.duration,
                       i.stipend,
                       i.min_cgpa,
                       i.max_backlogs,
                       i.eligible_departments,
                       i.start_date,
                       i.last_date_to_apply,
                       (SELECT COUNT(*)
                        FROM internship_applications ia
                        WHERE ia.internship_id = i.internship_id)
                        AS applicant_count,
                       i.status
                FROM internships i
                ORDER BY i.start_date, i.internship_id
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

            String department;
            double cgpa;
            int backlogs;

            try (PreparedStatement statement =
                         connection.prepareStatement(studentSql)) {

                statement.setString(1, studentId);

                try (ResultSet result = statement.executeQuery()) {

                    if (!result.next()) {
                        JOptionPane.showMessageDialog(
                                parent,
                                "Student profile not found."
                        );
                        return;
                    }

                    department = result.getString("department");
                    cgpa = result.getDouble("cgpa");
                    backlogs = result.getInt("active_backlogs");
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(internshipSql);
                 ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    String eligibleDepartments =
                            result.getString("eligible_departments");

                    String status = result.getString("status");

                    Date startDate = result.getDate("start_date");
                    Date lastDate = result.getDate("last_date_to_apply");

                    String startText = startDate == null
                            ? ""
                            : startDate.toLocalDate().format(DATE_FORMAT);

                    String lastText = lastDate == null
                            ? ""
                            : lastDate.toLocalDate().format(DATE_FORMAT);

                    boolean branchEligible =
                            isDepartmentEligible(
                                    department, eligibleDepartments
                            );

                    boolean cgpaEligible =
                            cgpa >= result.getDouble("min_cgpa");

                    boolean backlogEligible =
                            backlogs <= result.getInt("max_backlogs");

                    boolean deadlineValid =
                            lastDate != null
                                    && !LocalDate.now().isAfter(
                                            lastDate.toLocalDate()
                                    );

                    String eligibility;

                    if (!"Open".equalsIgnoreCase(status)) {
                        eligibility = "Closed";
                    } else if (!deadlineValid) {
                        eligibility = "Deadline Passed";
                    } else if (branchEligible
                            && cgpaEligible
                            && backlogEligible) {
                        eligibility = "Eligible";
                    } else {
                        eligibility = "Not Eligible";
                    }

                    model.addRow(new Object[]{
                            result.getInt("internship_id"),
                            result.getString("company_name"),
                            result.getString("internship_role"),
                            result.getString("duration"),
                            result.getBigDecimal("stipend"),
                            result.getBigDecimal("min_cgpa"),
                            result.getInt("max_backlogs"),
                            eligibleDepartments,
                            startText,
                            lastText,
                            result.getInt("applicant_count"),
                            eligibility,
                            status
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Error loading internships:\n" + ex.getMessage()
            );
        }
    }

    private static void loadMyApplications(
            DefaultTableModel model,
            String studentId,
            JPanel parent) {

        model.setRowCount(0);

        String sql = """
                SELECT ia.application_id,
                       i.company_name,
                       i.internship_role,
                       i.duration,
                       i.stipend,
                       ia.application_date,
                       ia.status
                FROM internship_applications ia
                JOIN internships i
                    ON ia.internship_id = i.internship_id
                WHERE ia.student_id = ?
                ORDER BY ia.application_id DESC
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

                statement.setString(1, studentId);

                try (ResultSet result = statement.executeQuery()) {

                    while (result.next()) {

                        Date applicationDate =
                                result.getDate("application_date");

                        String dateText = applicationDate == null
                                ? ""
                                : applicationDate.toLocalDate()
                                        .format(DATE_FORMAT);

                        model.addRow(new Object[]{
                                result.getInt("application_id"),
                                result.getString("company_name"),
                                result.getString("internship_role"),
                                result.getString("duration"),
                                result.getBigDecimal("stipend"),
                                dateText,
                                result.getString("status")
                        });
                    }
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Error loading your applications:\n" + ex.getMessage()
            );
        }
    }

    private static boolean isDepartmentEligible(
            String studentDepartment,
            String eligibleDepartments) {

        if (studentDepartment == null
                || eligibleDepartments == null) {
            return false;
        }

        for (String department : eligibleDepartments.split(",")) {

            if (studentDepartment.trim().equalsIgnoreCase(
                    department.trim())) {
                return true;
            }
        }

        return false;
    }
}