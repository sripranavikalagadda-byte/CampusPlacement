import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FacultyCompanies {

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ==========================================
    // MAIN COMPANY DRIVES PANEL
    // ==========================================

    public static JPanel createCompaniesPanel() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBackground(
                new Color(245, 247, 250)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // ==========================================
        // TITLE
        // ==========================================

        JLabel title =
                new JLabel(
                        "COMPANY DRIVES",
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

        // ==========================================
        // BUTTONS
        // ==========================================

        JPanel topPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton addButton =
                new JButton("Add Company Drive");

        JButton editButton =
                new JButton("Edit Drive");

        JButton closeButton =
                new JButton("Close Drive");

        JButton deleteButton =
                new JButton("Delete Drive");

        JButton refreshButton =
                new JButton("Refresh");

        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(closeButton);
        topPanel.add(deleteButton);
        topPanel.add(refreshButton);

        // ==========================================
        // COMPANY TABLE
        // ==========================================

        String[] columns = {
                "ID",
                "Company",
                "Job Role",
                "Package (LPA)",
                "Min CGPA",
                "Max Backlogs",
                "Eligible Departments",
                "Drive Date",
                "Last Date",
                "Applicants",
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

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        JScrollPane scrollPane =
        new JScrollPane(table);

JPanel centerPanel =
        new JPanel(new BorderLayout(10, 10));

centerPanel.add(
        topPanel,
        BorderLayout.NORTH
);

centerPanel.add(
        scrollPane,
        BorderLayout.CENTER
);

mainPanel.add(
        centerPanel,
        BorderLayout.CENTER
);

        // ==========================================
        // LOAD DATA
        // ==========================================

        Runnable loadCompanies = () -> {

            model.setRowCount(0);

            String sql = """
                    SELECT
                        c.company_id,
                        c.company_name,
                        c.job_role,
                        c.package_lpa,
                        c.min_cgpa,
                        c.max_backlogs,
                        c.drive_date,
                        c.last_date_to_apply,
                        c.drive_status,

                        (
                            SELECT COUNT(*)
                            FROM applications a
                            WHERE a.company_id = c.company_id
                        ) AS applicant_count,

                        (
                            SELECT GROUP_CONCAT(
                                cd.department
                                ORDER BY cd.department
                                SEPARATOR ', '
                            )
                            FROM company_departments cd
                            WHERE cd.company_id = c.company_id
                        ) AS departments

                    FROM companies c

                    ORDER BY c.drive_date
                    """;

            try (
                    Connection connection =
                            DatabaseConnection
                                    .getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql);

                    ResultSet result =
                            statement.executeQuery()
            ) {

                while (result.next()) {

                    String driveDate = "";

                    Date dbDriveDate =
                            result.getDate(
                                    "drive_date"
                            );

                    if (dbDriveDate != null) {

                        driveDate =
                                dbDriveDate
                                        .toLocalDate()
                                        .format(
                                                DISPLAY_FORMAT
                                        );
                    }

                    String lastDate = "";

                    Date dbLastDate =
                            result.getDate(
                                    "last_date_to_apply"
                            );

                    if (dbLastDate != null) {

                        lastDate =
                                dbLastDate
                                        .toLocalDate()
                                        .format(
                                                DISPLAY_FORMAT
                                        );
                    }

                    String departments =
                            result.getString(
                                    "departments"
                            );

                    if (departments == null ||
                            departments.isEmpty()) {

                        departments = "-";
                    }

                    String status =
                            result.getString(
                                    "drive_status"
                            );

                    if (status == null ||
                            status.isEmpty()) {

                        status = "Open";
                    }

                    model.addRow(
                            new Object[]{
                                    result.getInt(
                                            "company_id"
                                    ),

                                    result.getString(
                                            "company_name"
                                    ),

                                    result.getString(
                                            "job_role"
                                    ),

                                    result.getDouble(
                                            "package_lpa"
                                    ),

                                    result.getDouble(
                                            "min_cgpa"
                                    ),

                                    result.getInt(
                                            "max_backlogs"
                                    ),

                                    departments,

                                    driveDate,

                                    lastDate,

                                    result.getInt(
                                            "applicant_count"
                                    ),

                                    status
                            }
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error loading company drives:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };

        // ==========================================
        // ADD COMPANY
        // ==========================================

        addButton.addActionListener(e -> {
    showCompanyDialog(
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
        "Open"
    );
});

        // ==========================================
        // EDIT COMPANY
        // ==========================================

        editButton.addActionListener(e -> {

            int selectedRow =
                    table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select a company drive first.",
                        "Select Drive",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int companyId =
                    Integer.parseInt(
                            table.getValueAt(
                                    selectedRow,
                                    0
                            ).toString()
                    );

            String companyName =
                    table.getValueAt(
                            selectedRow,
                            1
                    ).toString();

            String jobRole =
                    table.getValueAt(
                            selectedRow,
                            2
                    ).toString();

            String packageLpa =
                    table.getValueAt(
                            selectedRow,
                            3
                    ).toString();

            String minCgpa =
                    table.getValueAt(
                            selectedRow,
                            4
                    ).toString();

            String maxBacklogs =
                    table.getValueAt(
                            selectedRow,
                            5
                    ).toString();

            String departments =
                    table.getValueAt(
                            selectedRow,
                            6
                    ).toString();

            String driveDate =
                    table.getValueAt(
                            selectedRow,
                            7
                    ).toString();

            String lastDate =
                    table.getValueAt(
                            selectedRow,
                            8
                    ).toString();

            String status =
                    table.getValueAt(
                            selectedRow,
                            10
                    ).toString();

            showCompanyDialog(
                    mainPanel,
                    model,
                    companyId,
                    companyName,
                    jobRole,
                    packageLpa,
                    minCgpa,
                    maxBacklogs,
                    departments,
                    driveDate,
                    lastDate,
                    status
            );
        });

        // ==========================================
        // CLOSE DRIVE
        // ==========================================

        closeButton.addActionListener(e -> {

            int selectedRow =
                    table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select a company drive first.",
                        "Select Drive",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int companyId =
                    Integer.parseInt(
                            table.getValueAt(
                                    selectedRow,
                                    0
                            ).toString()
                    );

            String companyName =
                    table.getValueAt(
                            selectedRow,
                            1
                    ).toString();

            String currentStatus =
                    table.getValueAt(
                            selectedRow,
                            10
                    ).toString();

            if ("Closed".equalsIgnoreCase(
                    currentStatus
            )) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "This company drive is already closed."
                );

                return;
            }

            int choice =
                    JOptionPane.showConfirmDialog(
                            mainPanel,
                            "Close the recruitment drive for "
                                    + companyName
                                    + "?",
                            "Close Drive",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice !=
                    JOptionPane.YES_OPTION) {

                return;
            }

            String sql = """
                    UPDATE companies
                    SET drive_status = 'Closed'
                    WHERE company_id = ?
                    """;

            try (
                    Connection connection =
                            DatabaseConnection
                                    .getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setInt(
                        1,
                        companyId
                );

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Company drive closed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadCompanies.run();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error closing company drive:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // DELETE DRIVE
        // ==========================================

        deleteButton.addActionListener(e -> {

            int selectedRow =
                    table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select a company drive first.",
                        "Select Drive",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int companyId =
                    Integer.parseInt(
                            table.getValueAt(
                                    selectedRow,
                                    0
                            ).toString()
                    );

            String companyName =
                    table.getValueAt(
                            selectedRow,
                            1
                    ).toString();

            int choice =
                    JOptionPane.showConfirmDialog(
                            mainPanel,
                            "Are you sure you want to delete "
                                    + companyName
                                    + "?\n\n"
                                    + "A drive with applications "
                                    + "cannot be deleted.",
                            "Delete Drive",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (choice !=
                    JOptionPane.YES_OPTION) {

                return;
            }

            String checkSql = """
                    SELECT COUNT(*)
                    FROM applications
                    WHERE company_id = ?
                    """;

            String deleteDepartmentsSql = """
                    DELETE FROM company_departments
                    WHERE company_id = ?
                    """;

            String deleteCompanySql = """
                    DELETE FROM companies
                    WHERE company_id = ?
                    """;

            try (
                    Connection connection =
                            DatabaseConnection
                                    .getConnection()
            ) {

                int applicantCount = 0;

                try (
                        PreparedStatement checkStatement =
                                connection.prepareStatement(
                                        checkSql
                                )
                ) {

                    checkStatement.setInt(
                            1,
                            companyId
                    );

                    try (
                            ResultSet result =
                                    checkStatement
                                            .executeQuery()
                    ) {

                        if (result.next()) {

                            applicantCount =
                                    result.getInt(1);
                        }
                    }
                }

                if (applicantCount > 0) {

                    JOptionPane.showMessageDialog(
                            mainPanel,
                            "This company drive cannot be deleted "
                                    + "because students have already applied.",
                            "Cannot Delete",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                connection.setAutoCommit(false);

                try {

                    try (
                            PreparedStatement departmentStatement =
                                    connection.prepareStatement(
                                            deleteDepartmentsSql
                                    )
                    ) {

                        departmentStatement.setInt(
                                1,
                                companyId
                        );

                        departmentStatement.executeUpdate();
                    }

                    try (
                            PreparedStatement companyStatement =
                                    connection.prepareStatement(
                                            deleteCompanySql
                                    )
                    ) {

                        companyStatement.setInt(
                                1,
                                companyId
                        );

                        companyStatement.executeUpdate();
                    }

                    connection.commit();

                } catch (Exception ex) {

                    connection.rollback();
                    throw ex;
                }

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Company drive deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadCompanies.run();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error deleting company drive:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // REFRESH
        // ==========================================

        refreshButton.addActionListener(
                e -> loadCompanies.run()
        );

        // ==========================================
        // INITIAL LOAD
        // ==========================================

        loadCompanies.run();

        return mainPanel;
    }

    // ==========================================
    // COMPANY DIALOG
    // ==========================================

    private static void showCompanyDialog(
            JPanel parent,
            DefaultTableModel model,
            int companyId,
            String existingCompanyName,
            String existingJobRole,
            String existingPackage,
            String existingMinCgpa,
            String existingMaxBacklogs,
            String existingDepartments,
            String existingDriveDate,
            String existingLastDate,
            String existingStatus
    ) {

        boolean editMode =
                companyId != -1;

        JTextField companyField =
                new JTextField(
                        existingCompanyName == null
                                ? ""
                                : existingCompanyName
                );

        JTextField roleField =
                new JTextField(
                        existingJobRole == null
                                ? ""
                                : existingJobRole
                );

        JTextField packageField =
                new JTextField(
                        existingPackage == null
                                ? ""
                                : existingPackage
                );

        JTextField minCgpaField =
                new JTextField(
                        existingMinCgpa == null
                                ? ""
                                : existingMinCgpa
                );

        JTextField maxBacklogsField =
                new JTextField(
                        existingMaxBacklogs == null
                                ? ""
                                : existingMaxBacklogs
                );

        JTextField departmentsField =
                new JTextField(
                        existingDepartments == null
                                ? ""
                                : existingDepartments
                );

        JTextField driveDateField =
                new JTextField(
                        existingDriveDate == null
                                ? ""
                                : existingDriveDate
                );

        JTextField lastDateField =
                new JTextField(
                        existingLastDate == null
                                ? ""
                                : existingLastDate
                );

        JComboBox<String> statusBox =
                new JComboBox<>(
                        new String[]{
                                "Open",
                                "Closed"
                        }
                );

        if (existingStatus != null &&
                !existingStatus.isEmpty()) {

            statusBox.setSelectedItem(
                    existingStatus
            );
        }

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                0,
                                2,
                                10,
                                10
                        )
                );

        panel.add(
                new JLabel(
                        "Company Name:"
                )
        );

        panel.add(companyField);

        panel.add(
                new JLabel(
                        "Job Role:"
                )
        );

        panel.add(roleField);

        panel.add(
                new JLabel(
                        "Package (LPA):"
                )
        );

        panel.add(packageField);

        panel.add(
                new JLabel(
                        "Minimum CGPA:"
                )
        );

        panel.add(minCgpaField);

        panel.add(
                new JLabel(
                        "Maximum Backlogs:"
                )
        );

        panel.add(maxBacklogsField);

        panel.add(
                new JLabel(
                        "Eligible Departments:"
                )
        );

        panel.add(departmentsField);

        panel.add(
                new JLabel(
                        "Drive Date (DD-MM-YYYY):"
                )
        );

        panel.add(driveDateField);

        panel.add(
                new JLabel(
                        "Last Date (DD-MM-YYYY):"
                )
        );

        panel.add(lastDateField);

        if (editMode) {

            panel.add(
                    new JLabel(
                            "Drive Status:"
                    )
            );

            panel.add(statusBox);
        }

        String dialogTitle =
                editMode
                        ? "Edit Company Drive"
                        : "Add Company Drive";

        int result =
                JOptionPane.showConfirmDialog(
                        parent,
                        panel,
                        dialogTitle,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String companyName =
                companyField.getText().trim();

        String jobRole =
                roleField.getText().trim();

        String packageText =
                packageField.getText().trim();

        String minCgpaText =
                minCgpaField.getText().trim();

        String maxBacklogsText =
                maxBacklogsField.getText().trim();

        String departmentsText =
                departmentsField.getText().trim();

        String driveDateText =
                driveDateField.getText().trim();

        String lastDateText =
                lastDateField.getText().trim();

        String status =
                editMode
                        ? statusBox
                            .getSelectedItem()
                            .toString()
                        : "Open";

        // ==========================================
        // VALIDATION
        // ==========================================

        if (companyName.isEmpty()
                || jobRole.isEmpty()
                || packageText.isEmpty()
                || minCgpaText.isEmpty()
                || maxBacklogsText.isEmpty()
                || departmentsText.isEmpty()
                || driveDateText.isEmpty()
                || lastDateText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Please fill all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double packageLpa;
        double minCgpa;
        int maxBacklogs;

        LocalDate driveDate;
        LocalDate lastDate;

        try {

            packageLpa =
                    Double.parseDouble(
                            packageText
                    );

            minCgpa =
                    Double.parseDouble(
                            minCgpaText
                    );

            maxBacklogs =
                    Integer.parseInt(
                            maxBacklogsText
                    );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Package, CGPA and backlogs must contain valid numbers.",
                    "Invalid Number",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            driveDate =
                    LocalDate.parse(
                            driveDateText,
                            DISPLAY_FORMAT
                    );

            lastDate =
                    LocalDate.parse(
                            lastDateText,
                            DISPLAY_FORMAT
                    );

        } catch (DateTimeParseException ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Please enter dates in DD-MM-YYYY format.",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (packageLpa < 0) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Package cannot be negative.",
                    "Invalid Package",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (minCgpa < 0 ||
                minCgpa > 10) {

            JOptionPane.showMessageDialog(
                    parent,
                    "CGPA must be between 0 and 10.",
                    "Invalid CGPA",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (maxBacklogs < 0) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Backlogs cannot be negative.",
                    "Invalid Backlogs",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (lastDate.isBefore(
                driveDate
        )) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Last date to apply cannot be before the drive date.",
                    "Invalid Dates",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // ==========================================
        // ADD NEW COMPANY
        // ==========================================

        if (!editMode) {

            String insertCompanySql = """
                    INSERT INTO companies
                    (
                        company_name,
                        job_role,
                        package_lpa,
                        min_cgpa,
                        max_backlogs,
                        drive_date,
                        last_date_to_apply,
                        drive_status
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, 'Open')
                    """;

            String insertDepartmentSql = """
                    INSERT INTO company_departments
                    (
                        company_id,
                        department
                    )
                    VALUES (?, ?)
                    """;

            try (
                    Connection connection =
                            DatabaseConnection
                                    .getConnection();

                    PreparedStatement companyStatement =
                            connection.prepareStatement(
                                    insertCompanySql,
                                    Statement
                                            .RETURN_GENERATED_KEYS
                            )
            ) {

                companyStatement.setString(
                        1,
                        companyName
                );

                companyStatement.setString(
                        2,
                        jobRole
                );

                companyStatement.setDouble(
                        3,
                        packageLpa
                );

                companyStatement.setDouble(
                        4,
                        minCgpa
                );

                companyStatement.setInt(
                        5,
                        maxBacklogs
                );

                companyStatement.setDate(
                        6,
                        java.sql.Date.valueOf(
                                driveDate
                        )
                );

                companyStatement.setDate(
                        7,
                        java.sql.Date.valueOf(
                                lastDate
                        )
                );

                companyStatement.executeUpdate();

                try (
                        ResultSet keys =
                                companyStatement
                                        .getGeneratedKeys()
                ) {

                    if (keys.next()) {

                        int newCompanyId =
                                keys.getInt(1);

                        try (
                                PreparedStatement departmentStatement =
                                        connection.prepareStatement(
                                                insertDepartmentSql
                                        )
                        ) {

                            addDepartments(
                                    departmentStatement,
                                    newCompanyId,
                                    departmentsText
                            );
                        }
                    }
                }

                JOptionPane.showMessageDialog(
                        parent,
                        "Company drive added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshTable(model);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        parent,
                        "Error adding company drive:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            return;
        }

        // ==========================================
        // EDIT EXISTING COMPANY
        // ==========================================

        String updateCompanySql = """
                UPDATE companies
                SET
                    company_name = ?,
                    job_role = ?,
                    package_lpa = ?,
                    min_cgpa = ?,
                    max_backlogs = ?,
                    drive_date = ?,
                    last_date_to_apply = ?,
                    drive_status = ?
                WHERE company_id = ?
                """;

        String deleteDepartmentsSql = """
                DELETE FROM company_departments
                WHERE company_id = ?
                """;

        String insertDepartmentSql = """
                INSERT INTO company_departments
                (
                    company_id,
                    department
                )
                VALUES (?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection
                                .getConnection()
        ) {

            connection.setAutoCommit(false);

            try {

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        updateCompanySql
                                )
                ) {

                    statement.setString(
                            1,
                            companyName
                    );

                    statement.setString(
                            2,
                            jobRole
                    );

                    statement.setDouble(
                            3,
                            packageLpa
                    );

                    statement.setDouble(
                            4,
                            minCgpa
                    );

                    statement.setInt(
                            5,
                            maxBacklogs
                    );

                    statement.setDate(
                            6,
                            java.sql.Date.valueOf(
                                    driveDate
                            )
                    );

                    statement.setDate(
                            7,
                            java.sql.Date.valueOf(
                                    lastDate
                            )
                    );

                    statement.setString(
                            8,
                            status
                    );

                    statement.setInt(
                            9,
                            companyId
                    );

                    statement.executeUpdate();
                }

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        deleteDepartmentsSql
                                )
                ) {

                    statement.setInt(
                            1,
                            companyId
                    );

                    statement.executeUpdate();
                }

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        insertDepartmentSql
                                )
                ) {

                    addDepartments(
                            statement,
                            companyId,
                            departmentsText
                    );
                }

                connection.commit();

                JOptionPane.showMessageDialog(
                        parent,
                        "Company drive updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshTable(model);

            } catch (Exception ex) {

                connection.rollback();

                throw ex;
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Error updating company drive:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // ADD DEPARTMENTS
    // ==========================================

    private static void addDepartments(
            PreparedStatement statement,
            int companyId,
            String departmentsText
    ) throws SQLException {

        String[] departments =
                departmentsText.split(",");

        for (String department :
                departments) {

            String departmentName =
                    department.trim()
                            .toUpperCase();

            if (departmentName.isEmpty()) {
                continue;
            }

            statement.setInt(
                    1,
                    companyId
            );

            statement.setString(
                    2,
                    departmentName
            );

            statement.addBatch();
        }

        statement.executeBatch();
    }

    // ==========================================
    // REFRESH TABLE
    // ==========================================

    private static void refreshTable(
            DefaultTableModel model
    ) {

        model.setRowCount(0);

        String sql = """
                SELECT
                    c.company_id,
                    c.company_name,
                    c.job_role,
                    c.package_lpa,
                    c.min_cgpa,
                    c.max_backlogs,
                    c.drive_date,
                    c.last_date_to_apply,
                    c.drive_status,

                    (
                        SELECT COUNT(*)
                        FROM applications a
                        WHERE a.company_id = c.company_id
                    ) AS applicant_count,

                    (
                        SELECT GROUP_CONCAT(
                            cd.department
                            ORDER BY cd.department
                            SEPARATOR ', '
                        )
                        FROM company_departments cd
                        WHERE cd.company_id = c.company_id
                    ) AS departments

                FROM companies c

                ORDER BY c.drive_date
                """;

        try (
                Connection connection =
                        DatabaseConnection
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {

            while (result.next()) {

                String driveDate = "";

                Date dbDriveDate =
                        result.getDate(
                                "drive_date"
                        );

                if (dbDriveDate != null) {

                    driveDate =
                            dbDriveDate
                                    .toLocalDate()
                                    .format(
                                            DISPLAY_FORMAT
                                    );
                }

                String lastDate = "";

                Date dbLastDate =
                        result.getDate(
                                "last_date_to_apply"
                        );

                if (dbLastDate != null) {

                    lastDate =
                            dbLastDate
                                    .toLocalDate()
                                    .format(
                                            DISPLAY_FORMAT
                                    );
                }

                String departments =
                        result.getString(
                                "departments"
                        );

                if (departments == null ||
                        departments.isEmpty()) {

                    departments = "-";
                }

                String status =
                        result.getString(
                                "drive_status"
                        );

                if (status == null ||
                        status.isEmpty()) {

                    status = "Open";
                }

                model.addRow(
                        new Object[]{
                                result.getInt(
                                        "company_id"
                                ),

                                result.getString(
                                        "company_name"
                                ),

                                result.getString(
                                        "job_role"
                                ),

                                result.getDouble(
                                        "package_lpa"
                                ),

                                result.getDouble(
                                        "min_cgpa"
                                ),

                                result.getInt(
                                        "max_backlogs"
                                ),

                                departments,

                                driveDate,

                                lastDate,

                                result.getInt(
                                        "applicant_count"
                                ),

                                status
                        }
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error refreshing company drives:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}