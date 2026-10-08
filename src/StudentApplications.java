import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class StudentApplications {

    public static JPanel createApplicationsPanel(String email) {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // ==============================
        // TITLE
        // ==============================

        JLabel title = new JLabel(
                "MY APPLICATIONS",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(title, BorderLayout.NORTH);

        // ==============================
        // TABLE
        // ==============================

        String[] columns = {
                "Company",
                "Job Role",
                "Package",
                "Application Date",
                "Status"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        JTable table = new JTable(model);

        table.setRowHeight(32);

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
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

        JLabel statusLabel =
                new JLabel(
                        "Click Refresh Status to check for updates."
                );

        JButton refreshButton =
                new JButton("Refresh Status");

        bottomPanel.add(statusLabel);
        bottomPanel.add(refreshButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // ==============================
        // LOAD APPLICATIONS
        // ==============================

        loadApplications(
                email,
                model,
                statusLabel
        );

        // ==============================
        // REFRESH
        // ==============================

        refreshButton.addActionListener(e -> {

            loadApplications(
                    email,
                    model,
                    statusLabel
            );
        });

        return mainPanel;
    }

    // ==============================
    // LOAD APPLICATIONS
    // ==============================

    private static void loadApplications(
            String email,
            DefaultTableModel model,
            JLabel statusLabel) {

        model.setRowCount(0);

        String studentId =
                getStudentId(email);

        if (studentId == null) {

            statusLabel.setText(
                    "Student profile not found."
            );

            return;
        }

        String sql = """
                SELECT c.company_name,
                       c.job_role,
                       c.package_lpa,
                       a.application_date,
                       a.status
                FROM applications a
                JOIN companies c
                    ON a.company_id = c.company_id
                WHERE a.student_id = ?
                ORDER BY a.application_id DESC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    studentId
            );

            ResultSet result =
                    statement.executeQuery();

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd-MM-yyyy"
                    );

            int applicationCount = 0;

            while (result.next()) {

                String applicationDate = "";

                if (
                        result.getDate(
                                "application_date"
                        ) != null
                ) {

                    applicationDate =
                            result.getDate(
                                    "application_date"
                            )
                            .toLocalDate()
                            .format(formatter);
                }

                String status =
                        result.getString("status");

                model.addRow(
                        new Object[]{
                                result.getString(
                                        "company_name"
                                ),

                                result.getString(
                                        "job_role"
                                ),

                                result.getDouble(
                                        "package_lpa"
                                ) + " LPA",

                                applicationDate,

                                status
                        }
                );

                applicationCount++;
            }

            if (applicationCount == 0) {

                statusLabel.setText(
                        "You have not applied for any company yet."
                );

            } else {

                statusLabel.setText(
                        applicationCount
                                + " application(s) found. "
                                + "Refresh to check the latest status."
                );
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
}