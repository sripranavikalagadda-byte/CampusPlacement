import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudentInternships {

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static JPanel createInternshipsPanel(String studentId) {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("INTERNSHIP OPPORTUNITIES");
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
                "Eligibility",
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

        JButton applyButton = new JButton("Apply");
        JButton refreshButton = new JButton("Refresh");

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        buttonPanel.add(applyButton);
        buttonPanel.add(refreshButton);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel centerPanel =
                new JPanel(new BorderLayout(10, 10));

        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        loadInternships(model, studentId);

        applyButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an internship first."
                );
                return;
            }

            int row = table.convertRowIndexToModel(selectedRow);

            int internshipId = Integer.parseInt(
                    model.getValueAt(row, 0).toString()
            );

            String eligibility =
                    model.getValueAt(row, 11).toString();

            String status =
                    model.getValueAt(row, 12).toString();

            String lastDateText =
                    model.getValueAt(row, 9).toString();

            if (!"Eligible".equalsIgnoreCase(eligibility)) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "You are not eligible for this internship."
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
                        LocalDate.parse(
                                lastDateText,
                                DISPLAY_FORMAT
                        );

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
                        "Invalid application deadline."
                );

                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Apply for this internship?",
                    "Confirm Application",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            String sql = """
                    INSERT INTO internship_applications
                    (student_id, internship_id, application_date, status)
                    VALUES (?, ?, CURRENT_DATE, 'Applied')
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(1, studentId);
                statement.setInt(2, internshipId);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Internship application submitted successfully."
                );

                loadInternships(
                        model,
                        studentId
                );

            } catch (SQLIntegrityConstraintViolationException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "You have already applied for this internship."
                );

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Database error: " + ex.getMessage()
                );
            }
        });

        refreshButton.addActionListener(e ->
                loadInternships(
                        model,
                        studentId
                )
        );

        return mainPanel;
    }

    private static void loadInternships(
            DefaultTableModel model,
            String studentId) {

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

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement studentStatement =
                        connection.prepareStatement(studentSql)
        ) {

            studentStatement.setString(1, studentId);

            String department;
            double cgpa;
            int backlogs;

            try (
                    ResultSet studentResult =
                            studentStatement.executeQuery()
            ) {

                if (!studentResult.next()) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Student profile not found."
                    );

                    return;
                }

                department =
                        studentResult.getString("department");

                cgpa =
                        studentResult.getDouble("cgpa");

                backlogs =
                        studentResult.getInt("active_backlogs");
            }

            try (
                    PreparedStatement internshipStatement =
                            connection.prepareStatement(internshipSql);

                    ResultSet result =
                            internshipStatement.executeQuery()
            ) {

                while (result.next()) {

                    double minCgpa =
                            result.getDouble("min_cgpa");

                    int maxBacklogs =
                            result.getInt("max_backlogs");

                    String eligibleDepartments =
                            result.getString(
                                    "eligible_departments"
                            );

                    Date startDate =
                            result.getDate("start_date");

                    Date lastDate =
                            result.getDate("last_date_to_apply");

                    String startDateText =
                            startDate == null
                                    ? ""
                                    : startDate
                                            .toLocalDate()
                                            .format(DISPLAY_FORMAT);

                    String lastDateText =
                            lastDate == null
                                    ? ""
                                    : lastDate
                                            .toLocalDate()
                                            .format(DISPLAY_FORMAT);

                    boolean branchEligible =
                            isDepartmentEligible(
                                    department,
                                    eligibleDepartments
                            );

                    boolean cgpaEligible =
                            cgpa >= minCgpa;

                    boolean backlogEligible =
                            backlogs <= maxBacklogs;

                    boolean deadlineValid =
                            lastDate == null
                                    || !LocalDate.now().isAfter(
                                            lastDate.toLocalDate()
                                    );

                    boolean open =
                            "Open".equalsIgnoreCase(
                                    result.getString("status")
                            );

                    String eligibility;

                    if (!open) {

                        eligibility = "Closed";

                    } else if (!deadlineValid) {

                        eligibility = "Deadline Passed";

                    } else if (
                            branchEligible
                                    && cgpaEligible
                                    && backlogEligible
                    ) {

                        eligibility = "Eligible";

                    } else {

                        eligibility = "Not Eligible";
                    }

                    model.addRow(
                            new Object[]{
                                    result.getInt("internship_id"),
                                    result.getString("company_name"),
                                    result.getString("internship_role"),
                                    result.getString("duration"),
                                    result.getBigDecimal("stipend"),
                                    result.getBigDecimal("min_cgpa"),
                                    result.getInt("max_backlogs"),
                                    eligibleDepartments,
                                    startDateText,
                                    lastDateText,
                                    result.getInt("applicant_count"),
                                    eligibility,
                                    result.getString("status")
                            }
                    );
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error loading internships: "
                            + ex.getMessage()
            );
        }
    }

    private static boolean isDepartmentEligible(
            String studentDepartment,
            String eligibleDepartments) {

        if (
                studentDepartment == null
                        || eligibleDepartments == null
        ) {
            return false;
        }

        String normalizedStudentDepartment =
                studentDepartment.trim().toLowerCase();

        String[] departments =
                eligibleDepartments.split(",");

        for (String department : departments) {

            if (
                    normalizedStudentDepartment.equals(
                            department.trim().toLowerCase()
                    )
            ) {
                return true;
            }
        }

        return false;
    }
}