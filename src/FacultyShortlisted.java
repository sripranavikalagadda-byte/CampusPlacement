import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FacultyShortlisted {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // =========================================================
    // MAIN PANEL
    // =========================================================

    public static JPanel createShortlistedPanel() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "SHORTLISTED STUDENTS",
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

        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
                "Application ID",
                "Student ID",
                "Student Name",
                "Branch",
                "CGPA",
                "Backlogs",
                "Company",
                "Job Role",
                "Package",
                "Status"
        };

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        JTable table =
                new JTable(model);

        table.setRowHeight(30);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                10
                        )
                );

        JButton selectedButton =
                new JButton("Mark Selected");

        JButton rejectedButton =
                new JButton("Mark Rejected");

        JButton refreshButton =
                new JButton("Refresh");

        buttonPanel.add(selectedButton);
        buttonPanel.add(rejectedButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // LOAD DATA
        // =====================================================

        loadShortlistedStudents(
                model,
                mainPanel
        );

        // =====================================================
        // MARK SELECTED
        // =====================================================

        selectedButton.addActionListener(e ->
                markSelected(
                        table,
                        model,
                        mainPanel
                )
        );

        // =====================================================
        // MARK REJECTED
        // =====================================================

        rejectedButton.addActionListener(e ->
                markRejected(
                        table,
                        model,
                        mainPanel
                )
        );

        // =====================================================
        // REFRESH
        // =====================================================

        refreshButton.addActionListener(e ->
                loadShortlistedStudents(
                        model,
                        mainPanel
                )
        );

        return mainPanel;
    }

    // =========================================================
    // MARK SELECTED
    // =========================================================

    private static void markSelected(
            JTable table,
            DefaultTableModel model,
            Component parent
    ) {

        int viewRow =
                table.getSelectedRow();

        if (viewRow == -1) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Please select a student first.",
                    "Select Student",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int row =
                table.convertRowIndexToModel(
                        viewRow
                );

        int applicationId;
        String studentId;
        String studentName;
        String company;
        String packageText;

        // =====================================================
        // READ SELECTED ROW
        // =====================================================

        try {

            applicationId =
                    Integer.parseInt(
                            model.getValueAt(
                                    row,
                                    0
                            ).toString()
                    );

            studentId =
                    model.getValueAt(
                            row,
                            1
                    ).toString();

            studentName =
                    model.getValueAt(
                            row,
                            2
                    ).toString();

            company =
                    model.getValueAt(
                            row,
                            6
                    ).toString();

            packageText =
                    model.getValueAt(
                            row,
                            8
                    ).toString();

        } catch (RuntimeException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Unable to read the selected student record.",
                    "Data Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =====================================================
        // PACKAGE
        // =====================================================

        double packageLpa;

        try {

            packageLpa =
                    Double.parseDouble(
                            packageText
                                    .replace(
                                            " LPA",
                                            ""
                                    )
                                    .trim()
                    );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Invalid package value for the selected student.",
                    "Data Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =====================================================
        // OFFER DATE
        // =====================================================

        JTextField offerDateField =
                new JTextField();

        JPanel datePanel =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                10,
                                10
                        )
                );

        datePanel.add(
                new JLabel("Student:")
        );

        datePanel.add(
                new JLabel(studentName)
        );

        datePanel.add(
                new JLabel("Student ID:")
        );

        datePanel.add(
                new JLabel(studentId)
        );

        datePanel.add(
                new JLabel(
                        "Offer Date (DD-MM-YYYY):"
                )
        );

        datePanel.add(
                offerDateField
        );

        int choice =
                JOptionPane.showConfirmDialog(
                        parent,
                        datePanel,
                        "Mark Selected - " + company,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        String offerDateText =
                offerDateField
                        .getText()
                        .trim();

        if (offerDateText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Please enter the offer date.",
                    "Missing Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalDate offerDate;

        try {

            offerDate =
                    LocalDate.parse(
                            offerDateText,
                            DATE_FORMAT
                    );

        } catch (DateTimeParseException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Please use date format DD-MM-YYYY.",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =====================================================
        // SQL
        // =====================================================

        String checkSql = """
                SELECT status
                FROM applications
                WHERE application_id = ?
                """;

        String insertSelectionSql = """
                INSERT INTO selections
                (
                    student_id,
                    company_id,
                    package_lpa,
                    offer_date
                )
                SELECT
                    student_id,
                    company_id,
                    ?,
                    ?
                FROM applications
                WHERE application_id = ?
                """;

        String updateApplicationSql = """
                UPDATE applications
                SET status = 'Selected'
                WHERE application_id = ?
                """;

        // =====================================================
        // DATABASE
        // =====================================================

        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {

            if (connection == null) {

                JOptionPane.showMessageDialog(
                        parent,
                        "Database connection could not be established.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            boolean originalAutoCommit =
                    connection.getAutoCommit();

            connection.setAutoCommit(false);

            try {

                // =================================================
                // CHECK APPLICATION STATUS
                // =================================================

                String currentStatus;

                try (
                        PreparedStatement checkStatement =
                                connection.prepareStatement(
                                        checkSql
                                )
                ) {

                    checkStatement.setInt(
                            1,
                            applicationId
                    );

                    try (
                            ResultSet result =
                                    checkStatement.executeQuery()
                    ) {

                        if (!result.next()) {

                            throw new SQLException(
                                    "Application not found."
                            );
                        }

                        currentStatus =
                                result.getString(
                                        "status"
                                );
                    }
                }

                if (!"Shortlisted".equalsIgnoreCase(
                        currentStatus
                )) {

                    throw new SQLException(
                            "Only shortlisted applications can be marked as selected."
                    );
                }

                // =================================================
                // INSERT INTO SELECTIONS
                // =================================================

                try (
                        PreparedStatement selectionStatement =
                                connection.prepareStatement(
                                        insertSelectionSql
                                )
                ) {

                    selectionStatement.setDouble(
                            1,
                            packageLpa
                    );

                    selectionStatement.setDate(
                            2,
                            Date.valueOf(offerDate)
                    );

                    selectionStatement.setInt(
                            3,
                            applicationId
                    );

                    int inserted =
                            selectionStatement.executeUpdate();

                    if (inserted == 0) {

                        throw new SQLException(
                                "Selection could not be created."
                        );
                    }
                }

                // =================================================
                // UPDATE APPLICATION STATUS
                // =================================================

                try (
                        PreparedStatement statusStatement =
                                connection.prepareStatement(
                                        updateApplicationSql
                                )
                ) {

                    statusStatement.setInt(
                            1,
                            applicationId
                    );

                    int updated =
                            statusStatement.executeUpdate();

                    if (updated == 0) {

                        throw new SQLException(
                                "Application status could not be updated."
                        );
                    }
                }

                // =================================================
                // COMMIT
                // =================================================

                connection.commit();

                connection.setAutoCommit(
                        originalAutoCommit
                );

                JOptionPane.showMessageDialog(
                        parent,
                        studentName +
                                " has been marked as Selected.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadShortlistedStudents(
                        model,
                        parent
                );

            } catch (Exception ex) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackEx) {

                    rollbackEx.printStackTrace();
                }

                try {

                    connection.setAutoCommit(
                            originalAutoCommit
                    );

                } catch (SQLException resetEx) {

                    resetEx.printStackTrace();
                }

                JOptionPane.showMessageDialog(
                        parent,
                        "Error marking student as Selected:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Database error: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =========================================================
    // MARK REJECTED
    // =========================================================

    private static void markRejected(
            JTable table,
            DefaultTableModel model,
            Component parent
    ) {

        int viewRow =
                table.getSelectedRow();

        if (viewRow == -1) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Please select a student first.",
                    "Select Student",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int row =
                table.convertRowIndexToModel(
                        viewRow
                );

        int applicationId;
        String studentName;

        try {

            applicationId =
                    Integer.parseInt(
                            model.getValueAt(
                                    row,
                                    0
                            ).toString()
                    );

            studentName =
                    model.getValueAt(
                            row,
                            2
                    ).toString();

        } catch (RuntimeException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Unable to read the selected student record.",
                    "Data Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        parent,
                        "Mark "
                                + studentName
                                + " as Rejected?",
                        "Confirm Rejection",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = """
                UPDATE applications
                SET status = 'Rejected'
                WHERE application_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {

            if (connection == null) {

                JOptionPane.showMessageDialog(
                        parent,
                        "Database connection could not be established.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setInt(
                        1,
                        applicationId
                );

                statement.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    parent,
                    studentName
                            + " has been marked as Rejected.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadShortlistedStudents(
                    model,
                    parent
            );

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Database error: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =========================================================
    // LOAD SHORTLISTED STUDENTS
    // =========================================================

    private static void loadShortlistedStudents(
            DefaultTableModel model,
            Component parent
    ) {

        model.setRowCount(0);

        String sql = """
                SELECT
                    a.application_id,
                    s.student_id,
                    s.name,
                    s.department,
                    s.cgpa,
                    s.active_backlogs,
                    c.company_name,
                    c.job_role,
                    c.package_lpa,
                    a.status
                FROM applications a
                JOIN students s
                    ON a.student_id = s.student_id
                JOIN companies c
                    ON a.company_id = c.company_id
                WHERE a.status = 'Shortlisted'
                ORDER BY a.application_id DESC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {

            if (connection == null) {

                JOptionPane.showMessageDialog(
                        parent,
                        "Database connection could not be established.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(sql);

                    ResultSet result =
                            statement.executeQuery()
            ) {

                while (result.next()) {

                    double packageLpa =
                            result.getDouble(
                                    "package_lpa"
                            );

                    model.addRow(
                            new Object[]{
                                    result.getInt(
                                            "application_id"
                                    ),

                                    result.getString(
                                            "student_id"
                                    ),

                                    result.getString(
                                            "name"
                                    ),

                                    result.getString(
                                            "department"
                                    ),

                                    result.getDouble(
                                            "cgpa"
                                    ),

                                    result.getInt(
                                            "active_backlogs"
                                    ),

                                    result.getString(
                                            "company_name"
                                    ),

                                    result.getString(
                                            "job_role"
                                    ),

                                    packageLpa + " LPA",

                                    result.getString(
                                            "status"
                                    )
                            }
                    );
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Database error: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }
}