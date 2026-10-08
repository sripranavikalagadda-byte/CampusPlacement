import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FacultyPlacementReports {

    // ==========================================
    // MAIN REPORTS PANEL
    // ==========================================

    public static JPanel createReportsPanel() {

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(245, 247, 250)
        );

        // ==========================================
        // HEADER
        // ==========================================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(
                new Color(31, 78, 121)
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        JLabel title =
                new JLabel("Placement Reports");

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Overall, department-wise and company-wise placement analytics"
                );

        subtitle.setForeground(Color.WHITE);

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        JPanel headingPanel =
                new JPanel();

        headingPanel.setOpaque(false);

        headingPanel.setLayout(
                new BoxLayout(
                        headingPanel,
                        BoxLayout.Y_AXIS
                )
        );

        headingPanel.add(title);

        headingPanel.add(
                Box.createVerticalStrut(5)
        );

        headingPanel.add(subtitle);

        header.add(
                headingPanel,
                BorderLayout.WEST
        );

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // ==========================================
        // MAIN CONTENT
        // ==========================================

        JPanel contentPanel =
                new JPanel();

        contentPanel.setBackground(
                new Color(245, 247, 250)
        );

        contentPanel.setLayout(
                new BoxLayout(
                        contentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        // ==========================================
        // SUMMARY CARDS
        // ==========================================

        JPanel summaryPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                15,
                                15
                        )
                );

        summaryPanel.setBackground(
                new Color(245, 247, 250)
        );

        JLabel totalStudentsValue =
                createValueLabel();

        JLabel placedStudentsValue =
                createValueLabel();

        JLabel unplacedStudentsValue =
                createValueLabel();

        JLabel placementPercentageValue =
                createValueLabel();

        JLabel averagePackageValue =
                createValueLabel();

        JLabel companiesValue =
                createValueLabel();

        summaryPanel.add(
                createStatCard(
                        "TOTAL STUDENTS",
                        totalStudentsValue,
                        new Color(52, 152, 219)
                )
        );

        summaryPanel.add(
                createStatCard(
                        "PLACED STUDENTS",
                        placedStudentsValue,
                        new Color(46, 204, 113)
                )
        );

        summaryPanel.add(
                createStatCard(
                        "UNPLACED STUDENTS",
                        unplacedStudentsValue,
                        new Color(231, 76, 60)
                )
        );

        summaryPanel.add(
                createStatCard(
                        "PLACEMENT %",
                        placementPercentageValue,
                        new Color(155, 89, 182)
                )
        );

        summaryPanel.add(
                createStatCard(
                        "AVERAGE PACKAGE",
                        averagePackageValue,
                        new Color(241, 196, 15)
                )
        );

        summaryPanel.add(
                createStatCard(
                        "COMPANIES",
                        companiesValue,
                        new Color(26, 188, 156)
                )
        );

        contentPanel.add(summaryPanel);

        contentPanel.add(
                Box.createVerticalStrut(20)
        );

        // ==========================================
        // DEPARTMENT-WISE REPORT
        // ==========================================

        JLabel departmentTitle =
                new JLabel(
                        "Department-wise Placement Report"
                );

        departmentTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        contentPanel.add(
                departmentTitle
        );

        contentPanel.add(
                Box.createVerticalStrut(8)
        );

        String[] departmentColumns = {
                "Department",
                "Total Students",
                "Placed Students",
                "Unplaced Students",
                "Placement %"
        };

        DefaultTableModel departmentModel =
                new DefaultTableModel(
                        departmentColumns,
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

        JTable departmentTable =
                new JTable(
                        departmentModel
                );

        departmentTable.setRowHeight(28);

        departmentTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        departmentTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        JScrollPane departmentScrollPane =
                new JScrollPane(
                        departmentTable
                );

        departmentScrollPane.setPreferredSize(
                new Dimension(
                        900,
                        180
                )
        );

        contentPanel.add(
                departmentScrollPane
        );

        contentPanel.add(
                Box.createVerticalStrut(20)
        );

        // ==========================================
        // COMPANY-WISE REPORT
        // ==========================================

        JLabel companyTitle =
                new JLabel(
                        "Company-wise Selection Report"
                );

        companyTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        contentPanel.add(
                companyTitle
        );

        contentPanel.add(
                Box.createVerticalStrut(8)
        );

        String[] companyColumns = {
                "Company",
                "Students Selected",
                "Average Package"
        };

        DefaultTableModel companyModel =
                new DefaultTableModel(
                        companyColumns,
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

        JTable companyTable =
                new JTable(
                        companyModel
                );

        companyTable.setRowHeight(28);

        companyTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        companyTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        JScrollPane companyScrollPane =
                new JScrollPane(
                        companyTable
                );

        companyScrollPane.setPreferredSize(
                new Dimension(
                        900,
                        180
                )
        );

        contentPanel.add(
                companyScrollPane
        );

        // ==========================================
        // SCROLL CONTENT
        // ==========================================

        JScrollPane mainScrollPane =
                new JScrollPane(
                        contentPanel
                );

        mainScrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        mainPanel.add(
                mainScrollPane,
                BorderLayout.CENTER
        );

        // ==========================================
        // REFRESH BUTTON
        // ==========================================

        JButton refreshButton =
                new JButton(
                        "Refresh Reports"
                );

        refreshButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottomPanel.setBackground(
                Color.WHITE
        );

        bottomPanel.add(
                refreshButton
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // ==========================================
        // LOAD ALL REPORTS
        // ==========================================

        Runnable loadReports = () -> {

            // Clear old table data
            departmentModel.setRowCount(0);
            companyModel.setRowCount(0);

            String totalStudentsSql =
                    "SELECT COUNT(*) FROM students";

            String placedStudentsSql =
                    """
                    SELECT COUNT(DISTINCT student_id)
                    FROM selections
                    """;

            String companiesSql =
                    "SELECT COUNT(*) FROM companies";

            String averagePackageSql =
                    """
                    SELECT COALESCE(
                        AVG(package_lpa),
                        0
                    )
                    FROM selections
                    """;

            // --------------------------------------
            // Department Report Query
            // --------------------------------------

            String departmentSql =
                    """
                    SELECT
                        s.department,
                        COUNT(DISTINCT s.student_id)
                            AS total_students,
                        COUNT(DISTINCT sel.student_id)
                            AS placed_students
                    FROM students s
                    LEFT JOIN selections sel
                        ON s.student_id = sel.student_id
                    GROUP BY s.department
                    ORDER BY s.department
                    """;

            // --------------------------------------
            // Company Report Query
            // --------------------------------------

            String companySql =
                    """
                    SELECT
                        c.company_name,
                        COUNT(DISTINCT s.student_id)
                            AS students_selected,
                        COALESCE(
                            AVG(sel.package_lpa),
                            0
                        ) AS average_package
                    FROM companies c
                    LEFT JOIN selections sel
                        ON c.company_id = sel.company_id
                    LEFT JOIN students s
                        ON sel.student_id = s.student_id
                    GROUP BY c.company_id, c.company_name
                    ORDER BY
                        students_selected DESC,
                        c.company_name
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection()
            ) {

                int totalStudents = 0;

                int placedStudents = 0;

                int companies = 0;

                double averagePackage = 0;

                // ======================================
                // TOTAL STUDENTS
                // ======================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        totalStudentsSql
                                );

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (result.next()) {

                        totalStudents =
                                result.getInt(1);
                    }
                }

                // ======================================
                // PLACED STUDENTS
                // ======================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        placedStudentsSql
                                );

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (result.next()) {

                        placedStudents =
                                result.getInt(1);
                    }
                }

                // ======================================
                // COMPANIES
                // ======================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        companiesSql
                                );

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (result.next()) {

                        companies =
                                result.getInt(1);
                    }
                }

                // ======================================
                // AVERAGE PACKAGE
                // ======================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        averagePackageSql
                                );

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (result.next()) {

                        averagePackage =
                                result.getDouble(1);
                    }
                }

                // ======================================
                // CALCULATE UNPLACED
                // ======================================

                int unplacedStudents =
                        totalStudents
                                - placedStudents;

                double placementPercentage = 0;

                if (totalStudents > 0) {

                    placementPercentage =
                            (placedStudents * 100.0)
                                    / totalStudents;
                }

                // ======================================
                // UPDATE SUMMARY CARDS
                // ======================================

                totalStudentsValue.setText(
                        String.valueOf(
                                totalStudents
                        )
                );

                placedStudentsValue.setText(
                        String.valueOf(
                                placedStudents
                        )
                );

                unplacedStudentsValue.setText(
                        String.valueOf(
                                unplacedStudents
                        )
                );

                placementPercentageValue.setText(
                        String.format(
                                "%.2f%%",
                                placementPercentage
                        )
                );

                averagePackageValue.setText(
                        String.format(
                                "%.2f LPA",
                                averagePackage
                        )
                );

                companiesValue.setText(
                        String.valueOf(
                                companies
                        )
                );

                // ======================================
                // DEPARTMENT-WISE REPORT
                // ======================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        departmentSql
                                );

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    while (result.next()) {

                        String department =
                                result.getString(
                                        "department"
                                );

                        int departmentTotal =
                                result.getInt(
                                        "total_students"
                                );

                        int departmentPlaced =
                                result.getInt(
                                        "placed_students"
                                );

                        int departmentUnplaced =
                                departmentTotal
                                        - departmentPlaced;

                        double departmentPercentage =
                                0;

                        if (departmentTotal > 0) {

                            departmentPercentage =
                                    (departmentPlaced
                                            * 100.0)
                                            / departmentTotal;
                        }

                        departmentModel.addRow(
                                new Object[]{
                                        department,
                                        departmentTotal,
                                        departmentPlaced,
                                        departmentUnplaced,
                                        String.format(
                                                "%.2f%%",
                                                departmentPercentage
                                        )
                                }
                        );
                    }
                }

                // ======================================
                // COMPANY-WISE REPORT
                // ======================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        companySql
                                );

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    while (result.next()) {

                        companyModel.addRow(
                                new Object[]{
                                        result.getString(
                                                "company_name"
                                        ),
                                        result.getInt(
                                                "students_selected"
                                        ),
                                        String.format(
                                                "%.2f LPA",
                                                result.getDouble(
                                                        "average_package"
                                                )
                                        )
                                }
                        );
                    }
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error loading placement reports:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };

        // ==========================================
        // REFRESH ACTION
        // ==========================================

        refreshButton.addActionListener(
                e -> loadReports.run()
        );

        // Load reports initially
        loadReports.run();

        return mainPanel;
    }

    // ==========================================
    // CREATE STAT CARD
    // ==========================================

    private static JPanel createStatCard(
            String title,
            JLabel valueLabel,
            Color color
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                color,
                                2
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        valueLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );

        return card;
    }

    // ==========================================
    // VALUE LABEL
    // ==========================================

    private static JLabel createValueLabel() {

        JLabel label =
                new JLabel("0");

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        return label;
    }
}