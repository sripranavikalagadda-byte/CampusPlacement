import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentCompanyDrives {

    public static JPanel createCompanyDrivesPanel(
            String email) {

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        // ==============================
        // TITLE
        // ==============================

        JLabel title =
                new JLabel(
                        "AVAILABLE COMPANY DRIVES",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // ==============================
        // TABLE
        // ==============================

        String[] columns = {
                "ID",
                "Company",
                "Job Role",
                "Package",
                "Min CGPA",
                "Max Backlogs",
                "Branches",
                "Drive Date",
                "Last Date",
                "Eligibility"
        };

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        JTable table =
                new JTable(model);

        table.setRowHeight(30);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane tableScrollPane =
                new JScrollPane(table);

        mainPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        // ==============================
        // BOTTOM PANEL
        // ==============================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                10
                        )
                );

        JButton applyButton =
                new JButton("APPLY NOW");

        JLabel statusLabel =
                new JLabel(
                        "Select a company to see eligibility."
                );

        bottomPanel.add(statusLabel);
        bottomPanel.add(applyButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        applyButton.setEnabled(false);

        // ==============================
        // GET STUDENT DETAILS
        // ==============================

        String studentId = getStudentId(email);

        if (studentId == null) {

            statusLabel.setText(
                    "Student profile not found."
            );

            return mainPanel;
        }

        // ==============================
        // LOAD COMPANY DRIVES
        // ==============================

        loadCompanyDrives(
                model,
                studentId
        );

        // ==============================
        // TABLE SELECTION
        // ==============================

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (
                            !e.getValueIsAdjusting()
                            && table.getSelectedRow() != -1
                    ) {

                        int row =
                                table.getSelectedRow();

                        String eligibility =
                                model.getValueAt(
                                        row,
                                        9
                                ).toString();

                        if (
                                eligibility.equals(
                                        "ELIGIBLE"
                                )
                        ) {

                            applyButton.setEnabled(
                                    true
                            );

                            statusLabel.setText(
                                    "You are eligible for this drive."
                            );

                        } else {

                            applyButton.setEnabled(
                                    false
                            );

                            statusLabel.setText(
                                    "You are not eligible for this drive."
                            );
                        }
                    }
                });

        // ==============================
        // APPLY BUTTON
        // ==============================

        applyButton.addActionListener(e -> {

            int row =
                    table.getSelectedRow();

            if (row == -1) {
                return;
            }

            String eligibility =
                    model.getValueAt(
                            row,
                            9
                    ).toString();

            if (
                    !eligibility.equals(
                            "ELIGIBLE"
                    )
            ) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "You are not eligible for this company."
                );

                return;
            }

            int companyId =
                    Integer.parseInt(
                            model.getValueAt(
                                    row,
                                    0
                            ).toString()
                    );

            applyForCompany(
                    studentId,
                    companyId
            );
        });

        return mainPanel;
    }

    // ==============================
    // GET STUDENT ID
    // ==============================

    private static String getStudentId(
            String email) {

        String sql = """
                SELECT s.student_id
                FROM students s
                JOIN users u
                    ON s.user_id = u.user_id
                WHERE LOWER(u.college_email)
                      = LOWER(?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    email
            );

            ResultSet result =
                    statement.executeQuery();

            if (result.next()) {

                return result.getString(
                        "student_id"
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    // ==============================
    // LOAD COMPANY DRIVES
    // ==============================

    private static void loadCompanyDrives(
            DefaultTableModel model,
            String studentId) {

        model.setRowCount(0);

        // Get student information
        String studentSQL = """
                SELECT department,
                       cgpa,
                       active_backlogs
                FROM students
                WHERE student_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement studentStatement =
                        connection.prepareStatement(
                                studentSQL
                        )
        ) {

            studentStatement.setString(
                    1,
                    studentId
            );

            ResultSet studentResult =
                    studentStatement.executeQuery();

            if (!studentResult.next()) {
                return;
            }

            String studentDepartment =
                    studentResult.getString(
                            "department"
                    );

            double studentCgpa =
                    studentResult.getDouble(
                            "cgpa"
                    );

            int studentBacklogs =
                    studentResult.getInt(
                            "active_backlogs"
                    );

            // ==============================
            // GET COMPANIES
            // ==============================

            String companySQL = """
                    SELECT c.company_id,
                           c.company_name,
                           c.job_role,
                           c.package_lpa,
                           c.min_cgpa,
                           c.max_backlogs,
                           c.drive_date,
                           c.last_date_to_apply,
                           GROUP_CONCAT(
                               cd.department
                               ORDER BY cd.department
                               SEPARATOR ', '
                           ) AS departments
                    FROM companies c
                    LEFT JOIN company_departments cd
                        ON c.company_id = cd.company_id
                    WHERE c.last_date_to_apply >= CURRENT_DATE
                    GROUP BY c.company_id
                    ORDER BY c.drive_date
                    """;

            try (
                    PreparedStatement companyStatement =
                            connection.prepareStatement(
                                    companySQL
                            );

                    ResultSet companyResult =
                            companyStatement.executeQuery()
            ) {

                while (companyResult.next()) {

                    int companyId =
                            companyResult.getInt(
                                    "company_id"
                            );

                    String companyName =
                            companyResult.getString(
                                    "company_name"
                            );

                    String jobRole =
                            companyResult.getString(
                                    "job_role"
                            );

                    double packageLpa =
                            companyResult.getDouble(
                                    "package_lpa"
                            );

                    double minCgpa =
                            companyResult.getDouble(
                                    "min_cgpa"
                            );

                    int maxBacklogs =
                            companyResult.getInt(
                                    "max_backlogs"
                            );

                    String departments =
                            companyResult.getString(
                                    "departments"
                            );

                    Date driveDate =
                            companyResult.getDate(
                                    "drive_date"
                            );

                    Date lastDate =
                            companyResult.getDate(
                                    "last_date_to_apply"
                            );

                    // ==============================
                    // CHECK ELIGIBILITY
                    // ==============================

                    boolean branchEligible =
                            false;

                    if (departments != null) {

                        String[] branchList =
                                departments.split(",");

                        for (
                                String branch :
                                branchList
                        ) {

                            if (
                                    branch.trim()
                                            .equalsIgnoreCase(
                                                    studentDepartment
                                            )
                            ) {

                                branchEligible = true;
                                break;
                            }
                        }
                    }

                    boolean cgpaEligible =
                            studentCgpa >= minCgpa;

                    boolean backlogEligible =
                            studentBacklogs <= maxBacklogs;

                    String eligibility;

                    if (
                            branchEligible
                            && cgpaEligible
                            && backlogEligible
                    ) {

                        eligibility =
                                "ELIGIBLE";

                    } else {

                        eligibility =
                                "NOT ELIGIBLE";
                    }

                    DateTimeFormatterHelper
                            formatter =
                            new DateTimeFormatterHelper();

                    model.addRow(
                            new Object[]{
                                    companyId,
                                    companyName,
                                    jobRole,
                                    packageLpa + " LPA",
                                    minCgpa,
                                    maxBacklogs,
                                    departments,
                                    formatter.format(
                                            driveDate
                                    ),
                                    formatter.format(
                                            lastDate
                                    ),
                                    eligibility
                            }
                    );
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Database error: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ==============================
    // APPLY TO COMPANY
    // ==============================

    private static void applyForCompany(
            String studentId,
            int companyId) {

        String checkSQL = """
                SELECT application_id, status
                FROM applications
                WHERE student_id = ?
                AND company_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement checkStatement =
                        connection.prepareStatement(
                                checkSQL
                        )
        ) {

            checkStatement.setString(
                    1,
                    studentId
            );

            checkStatement.setInt(
                    2,
                    companyId
            );

            ResultSet result =
                    checkStatement.executeQuery();

            if (result.next()) {

                JOptionPane.showMessageDialog(
                        null,
                        "You have already applied for this company."
                );

                return;
            }

            String insertSQL = """
                    INSERT INTO applications
                    (
                        student_id,
                        company_id,
                        application_date,
                        status
                    )
                    VALUES
                    (?, ?, CURRENT_DATE, 'Applied')
                    """;

            try (
                    PreparedStatement insertStatement =
                            connection.prepareStatement(
                                    insertSQL
                            )
            ) {

                insertStatement.setString(
                        1,
                        studentId
                );

                insertStatement.setInt(
                        2,
                        companyId
                );

                insertStatement.executeUpdate();

                JOptionPane.showMessageDialog(
                        null,
                        "Application submitted successfully!"
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Unable to submit application: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ==============================
    // DATE FORMATTER
    // ==============================

    private static class DateTimeFormatterHelper {

        private final java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy"
                );

        public String format(Date date) {

            if (date == null) {
                return "";
            }

            return date.toLocalDate()
                    .format(formatter);
        }
    }
}