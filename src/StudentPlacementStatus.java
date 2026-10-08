import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class StudentPlacementStatus {

    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static JPanel createPlacementStatusPanel(String email) {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ==============================
        // HEADER
        // ==============================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 78, 121));
        header.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        JLabel title = new JLabel("Placement Status");
        title.setForeground(Color.WHITE);
        title.setFont(
                new Font("Segoe UI", Font.BOLD, 24)
        );

        JLabel subtitle = new JLabel(
                "Track your placement applications and final selection"
        );
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        JPanel headingPanel = new JPanel();
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

        // ==============================
        // MAIN CONTENT
        // ==============================

        JPanel contentPanel = new JPanel();
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
                        25, 30, 25, 30
                )
        );

        // Student information
        JLabel studentNameLabel =
                new JLabel("Student: Loading...");

        studentNameLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        JLabel studentIdLabel =
                new JLabel("Student ID: Loading...");

        studentIdLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        contentPanel.add(studentNameLabel);

        contentPanel.add(
                Box.createVerticalStrut(5)
        );

        contentPanel.add(studentIdLabel);

        contentPanel.add(
                Box.createVerticalStrut(20)
        );

        // ==============================
        // STATUS CARD
        // ==============================

        JPanel statusCard =
                new JPanel(
                        new BorderLayout(
                                10, 10
                        )
                );

        statusCard.setBackground(Color.WHITE);

        statusCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 215, 220),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        JLabel statusTitle =
                new JLabel("Current Placement Status");

        statusTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        JLabel statusLabel =
                new JLabel("Loading...");

        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        statusLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        statusCard.add(
                statusTitle,
                BorderLayout.NORTH
        );

        statusCard.add(
                statusLabel,
                BorderLayout.CENTER
        );

        contentPanel.add(statusCard);

        contentPanel.add(
                Box.createVerticalStrut(20)
        );

        // ==============================
        // DETAILS CARD
        // ==============================

        JPanel detailsCard =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                15,
                                15
                        )
                );

        detailsCard.setBackground(Color.WHITE);

        detailsCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 215, 220),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        JLabel companyLabel =
                new JLabel("Company:");

        JLabel companyValue =
                new JLabel("-");

        JLabel roleLabel =
                new JLabel("Job Role:");

        JLabel roleValue =
                new JLabel("-");

        JLabel packageLabel =
                new JLabel("Package:");

        JLabel packageValue =
                new JLabel("-");

        JLabel offerDateLabel =
                new JLabel("Offer Date:");

        JLabel offerDateValue =
                new JLabel("-");

        JLabel applicationsLabel =
                new JLabel("Applications:");

        JLabel applicationsValue =
                new JLabel("0");

        Font labelFont =
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                );

        Font valueFont =
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                );

        companyLabel.setFont(labelFont);
        roleLabel.setFont(labelFont);
        packageLabel.setFont(labelFont);
        offerDateLabel.setFont(labelFont);
        applicationsLabel.setFont(labelFont);

        companyValue.setFont(valueFont);
        roleValue.setFont(valueFont);
        packageValue.setFont(valueFont);
        offerDateValue.setFont(valueFont);
        applicationsValue.setFont(valueFont);

        detailsCard.add(companyLabel);
        detailsCard.add(companyValue);

        detailsCard.add(roleLabel);
        detailsCard.add(roleValue);

        detailsCard.add(packageLabel);
        detailsCard.add(packageValue);

        detailsCard.add(offerDateLabel);
        detailsCard.add(offerDateValue);

        detailsCard.add(applicationsLabel);
        detailsCard.add(applicationsValue);

        contentPanel.add(detailsCard);

        contentPanel.add(
                Box.createVerticalStrut(20)
        );

        // ==============================
        // REFRESH BUTTON
        // ==============================

        JButton refreshButton =
                new JButton("Refresh Status");

        refreshButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        refreshButton.setPreferredSize(
                new Dimension(160, 40)
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttonPanel.setBackground(
                new Color(245, 247, 250)
        );

        buttonPanel.add(refreshButton);

        contentPanel.add(buttonPanel);

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        // ==============================
        // LOAD STATUS
        // ==============================

        Runnable loadStatus = () -> {

            String studentSql = """
                    SELECT
                        s.student_id,
                        s.name
                    FROM students s
                    JOIN users u
                        ON s.user_id = u.user_id
                    WHERE LOWER(u.college_email) = LOWER(?)
                    """;

            String applicationCountSql = """
                    SELECT COUNT(*)
                    FROM applications
                    WHERE student_id = ?
                    """;

            String statusSql = """
                    SELECT
                        a.status,
                        c.company_name,
                        c.job_role,
                        c.package_lpa
                    FROM applications a
                    JOIN companies c
                        ON a.company_id = c.company_id
                    WHERE a.student_id = ?
                    ORDER BY a.application_id DESC
                    LIMIT 1
                    """;

            String selectionSql = """
                    SELECT
                        c.company_name,
                        c.job_role,
                        s.package_lpa,
                        s.offer_date
                    FROM selections s
                    JOIN companies c
                        ON s.company_id = c.company_id
                    WHERE s.student_id = ?
                    ORDER BY s.selection_id DESC
                    LIMIT 1
                    """;

            try (Connection connection =
                         DatabaseConnection.getConnection()) {

                String studentId = null;
                String studentName = null;

                // ------------------------------
                // STUDENT DETAILS
                // ------------------------------

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     studentSql
                             )) {

                    statement.setString(1, email);

                    try (ResultSet result =
                                 statement.executeQuery()) {

                        if (result.next()) {

                            studentId =
                                    result.getString(
                                            "student_id"
                                    );

                            studentName =
                                    result.getString(
                                            "name"
                                    );
                        }
                    }
                }

                if (studentId == null) {

                    JOptionPane.showMessageDialog(
                            mainPanel,
                            "Student profile not found.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                studentNameLabel.setText(
                        "Student: " + studentName
                );

                studentIdLabel.setText(
                        "Student ID: " + studentId
                );

                // ------------------------------
                // APPLICATION COUNT
                // ------------------------------

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     applicationCountSql
                             )) {

                    statement.setString(1, studentId);

                    try (ResultSet result =
                                 statement.executeQuery()) {

                        if (result.next()) {

                            applicationsValue.setText(
                                    String.valueOf(
                                            result.getInt(1)
                                    )
                            );
                        }
                    }
                }

                // ------------------------------
                // DEFAULT VALUES
                // ------------------------------

                statusLabel.setText("Not Placed");

                companyValue.setText("-");
                roleValue.setText("-");
                packageValue.setText("-");
                offerDateValue.setText("-");

                // ------------------------------
                // CHECK FINAL SELECTION
                // ------------------------------

                boolean selected = false;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     selectionSql
                             )) {

                    statement.setString(1, studentId);

                    try (ResultSet result =
                                 statement.executeQuery()) {

                        if (result.next()) {

                            selected = true;

                            statusLabel.setText(
                                    "Selected"
                            );

                            companyValue.setText(
                                    result.getString(
                                            "company_name"
                                    )
                            );

                            roleValue.setText(
                                    result.getString(
                                            "job_role"
                                    )
                            );

                            double packageLpa =
                                    result.getDouble(
                                            "package_lpa"
                                    );

                            packageValue.setText(
                                    String.format(
                                            "%.2f LPA",
                                            packageLpa
                                    )
                            );

                            Date offerDate =
                                    result.getDate(
                                            "offer_date"
                                    );

                            if (offerDate != null) {

                                offerDateValue.setText(
                                        offerDate
                                                .toLocalDate()
                                                .format(
                                                        DISPLAY_DATE
                                                )
                                );
                            }
                        }
                    }
                }

                // ------------------------------
                // CHECK LATEST APPLICATION
                // ------------------------------

                if (!selected) {

                    try (PreparedStatement statement =
                                 connection.prepareStatement(
                                         statusSql
                                 )) {

                        statement.setString(
                                1,
                                studentId
                        );

                        try (ResultSet result =
                                     statement.executeQuery()) {

                            if (result.next()) {

                                String status =
                                        result.getString(
                                                "status"
                                        );

                                if (status != null) {

                                    statusLabel.setText(
                                            status
                                    );
                                }

                                companyValue.setText(
                                        result.getString(
                                                "company_name"
                                        )
                                );

                                roleValue.setText(
                                        result.getString(
                                                "job_role"
                                        )
                                );

                                double packageLpa =
                                        result.getDouble(
                                                "package_lpa"
                                        );

                                packageValue.setText(
                                        String.format(
                                                "%.2f LPA",
                                                packageLpa
                                        )
                                );
                            }
                        }
                    }
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error loading placement status:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };

        refreshButton.addActionListener(
                e -> loadStatus.run()
        );

        loadStatus.run();

        return mainPanel;
    }
}