import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class FacultyApplications {

    public static JPanel createApplicationsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // ==============================
        // TITLE
        // ==============================

        JLabel title = new JLabel(
                "STUDENT APPLICATIONS",
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
                "Application ID",
                "Student ID",
                "Student Name",
                "Branch",
                "CGPA",
                "Backlogs",
                "Company",
                "Job Role",
                "Package",
                "Applied Date",
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

        // ==============================
        // BUTTONS
        // ==============================

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        JButton shortlistButton =
                new JButton("Shortlist");

        JButton rejectButton =
                new JButton("Reject");

        JButton refreshButton =
                new JButton("Refresh");

        JLabel instructionLabel =
                new JLabel(
                        "Select an Applied application."
                );

        buttonPanel.add(instructionLabel);
        buttonPanel.add(shortlistButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // ==============================
        // LOAD APPLICATIONS
        // ==============================

        loadApplications(model);

        // ==============================
        // SHORTLIST
        // ==============================

        shortlistButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an application first."
                );

                return;
            }

            int applicationId =
                    Integer.parseInt(
                            model.getValueAt(row, 0).toString()
                    );

            String status =
                    model.getValueAt(row, 10).toString();

            if (!status.equals("Applied")) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Only applications with status 'Applied' can be shortlisted."
                );

                return;
            }

            updateApplicationStatus(
                    applicationId,
                    "Shortlisted",
                    model
            );
        });

        // ==============================
        // REJECT
        // ==============================

        rejectButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an application first."
                );

                return;
            }

            int applicationId =
                    Integer.parseInt(
                            model.getValueAt(row, 0).toString()
                    );

            String status =
                    model.getValueAt(row, 10).toString();

            if (!status.equals("Applied")) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Only applications with status 'Applied' can be rejected."
                );

                return;
            }

            int choice =
                    JOptionPane.showConfirmDialog(
                            mainPanel,
                            "Are you sure you want to reject this application?",
                            "Reject Application",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice == JOptionPane.YES_OPTION) {

                updateApplicationStatus(
                        applicationId,
                        "Rejected",
                        model
                );
            }
        });

        // ==============================
        // REFRESH
        // ==============================

        refreshButton.addActionListener(e ->
                loadApplications(model)
        );

        return mainPanel;
    }

    // ==============================
    // LOAD APPLICATIONS
    // ==============================

    private static void loadApplications(
            DefaultTableModel model) {

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
                    a.application_date,
                    a.status
                FROM applications a
                JOIN students s
                    ON a.student_id = s.student_id
                JOIN companies c
                    ON a.company_id = c.company_id
                ORDER BY a.application_id DESC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (result.next()) {

                String applicationDate = "";

                if (result.getDate("application_date") != null) {

                    applicationDate =
                            result.getDate("application_date")
                                    .toLocalDate()
                                    .format(formatter);
                }

                model.addRow(
                        new Object[]{
                                result.getInt("application_id"),
                                result.getString("student_id"),
                                result.getString("name"),
                                result.getString("department"),
                                result.getDouble("cgpa"),
                                result.getInt("active_backlogs"),
                                result.getString("company_name"),
                                result.getString("job_role"),
                                result.getDouble("package_lpa") + " LPA",
                                applicationDate,
                                result.getString("status")
                        }
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Database error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ==============================
    // UPDATE STATUS
    // ==============================

    private static void updateApplicationStatus(
            int applicationId,
            String newStatus,
            DefaultTableModel model) {

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

            statement.setString(
                    1,
                    newStatus
            );

            statement.setInt(
                    2,
                    applicationId
            );

            int rows =
                    statement.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                        null,
                        "Application status updated to "
                                + newStatus + "."
                );

                loadApplications(model);

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Application could not be updated."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Database error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}