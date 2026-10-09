import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class FacultyInternshipApplications {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static JPanel createApplicationsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel titleLabel =
                new JLabel("INTERNSHIP APPLICATIONS");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Application ID",
                "Student ID",
                "Student Name",
                "Branch",
                "CGPA",
                "Backlogs",
                "Company",
                "Internship Role",
                "Application Date",
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

        JButton shortlistButton = new JButton("Shortlist");
        JButton selectedButton = new JButton("Mark Selected");
        JButton rejectButton = new JButton("Reject");
        JButton refreshButton = new JButton("Refresh");

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        buttonPanel.add(shortlistButton);
        buttonPanel.add(selectedButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(refreshButton);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        loadApplications(model, mainPanel);

        shortlistButton.addActionListener(e ->
                updateApplicationStatus(
                        mainPanel, table, model, "Shortlisted"
                )
        );

        selectedButton.addActionListener(e ->
                updateApplicationStatus(
                        mainPanel, table, model, "Selected"
                )
        );

        rejectButton.addActionListener(e ->
                updateApplicationStatus(
                        mainPanel, table, model, "Rejected"
                )
        );

        refreshButton.addActionListener(e ->
                loadApplications(model, mainPanel)
        );

        return mainPanel;
    }

    private static void loadApplications(
            DefaultTableModel model,
            JPanel parent) {

        model.setRowCount(0);

        String sql = """
                SELECT
                    ia.application_id,
                    s.student_id,
                    s.name AS student_name,
                    s.department,
                    s.cgpa,
                    s.active_backlogs,
                    i.company_name,
                    i.internship_role,
                    ia.application_date,
                    ia.status
                FROM internship_applications ia
                JOIN students s
                    ON ia.student_id = s.student_id
                JOIN internships i
                    ON ia.internship_id = i.internship_id
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
                         connection.prepareStatement(sql);
                 ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Date applicationDate =
                            result.getDate("application_date");

                    String dateText = applicationDate == null
                            ? ""
                            : applicationDate.toLocalDate()
                                    .format(DATE_FORMAT);

                    model.addRow(new Object[]{
                            result.getInt("application_id"),
                            result.getString("student_id"),
                            result.getString("student_name"),
                            result.getString("department"),
                            result.getBigDecimal("cgpa"),
                            result.getInt("active_backlogs"),
                            result.getString("company_name"),
                            result.getString("internship_role"),
                            dateText,
                            result.getString("status")
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Error loading internship applications:\n"
                            + ex.getMessage()
            );
        }
    }

    private static void updateApplicationStatus(
            JPanel parent,
            JTable table,
            DefaultTableModel model,
            String newStatus) {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Please select an application first."
            );
            return;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        int applicationId = Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );

        String currentStatus =
                model.getValueAt(row, 9).toString();

        // Allow only valid status transitions.
        boolean validTransition;

        switch (newStatus) {
            case "Shortlisted":
                validTransition =
                        "Applied".equalsIgnoreCase(currentStatus);
                break;

            case "Selected":
                validTransition =
                        "Shortlisted".equalsIgnoreCase(currentStatus);
                break;

            case "Rejected":
                validTransition =
                        "Applied".equalsIgnoreCase(currentStatus)
                        || "Shortlisted".equalsIgnoreCase(currentStatus);
                break;

            default:
                validTransition = false;
        }

        if (!validTransition) {
            JOptionPane.showMessageDialog(
                    parent,
                    switch (newStatus) {
                        case "Shortlisted" ->
                                "Only Applied applications can be shortlisted.";
                        case "Selected" ->
                                "Shortlist the application before marking it Selected.";
                        case "Rejected" ->
                                "Only Applied or Shortlisted applications can be rejected.";
                        default ->
                                "Invalid status change.";
                    }
            );
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                parent,
                "Change application status from "
                        + currentStatus + " to " + newStatus + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = """
                UPDATE internship_applications
                SET status = ?
                WHERE application_id = ?
                  AND status = ?
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

                statement.setString(1, newStatus);
                statement.setInt(2, applicationId);
                statement.setString(3, currentStatus);

                int updated = statement.executeUpdate();

                if (updated == 1) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "Application status updated to "
                                    + newStatus + "."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            parent,
                            "The application changed. Refresh and try again."
                    );
                }

                loadApplications(model, parent);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Error updating application:\n"
                            + ex.getMessage()
            );
        }
    }
}