import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FacultyInterviews {

    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    public static JPanel createInterviewsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ---------------- HEADER ----------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 78, 121));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Interview Management");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitle = new JLabel("Schedule and manage student interviews");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));

        headingPanel.add(title);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(subtitle);

        header.add(headingPanel, BorderLayout.WEST);

        mainPanel.add(header, BorderLayout.NORTH);

        // ---------------- TABLE ----------------
        String[] columns = {
                "Interview ID",
                "Application ID",
                "Student ID",
                "Student Name",
                "Company",
                "Job Role",
                "Date",
                "Time",
                "Mode",
                "Venue / Link",
                "Round",
                "Status",
                "Feedback"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ---------------- BUTTONS ----------------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton scheduleButton = new JButton("Schedule Interview");
        JButton completedButton = new JButton("Mark Completed");
        JButton cancelledButton = new JButton("Cancel Interview");
        JButton refreshButton = new JButton("Refresh");

        scheduleButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        completedButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelledButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));

        bottomPanel.add(scheduleButton);
        bottomPanel.add(completedButton);
        bottomPanel.add(cancelledButton);
        bottomPanel.add(refreshButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ---------------- LOAD DATA ----------------
        Runnable loadInterviews = () -> {

            model.setRowCount(0);

            String sql = """
                    SELECT
                        i.interview_id,
                        i.application_id,
                        s.student_id,
                        s.name,
                        c.company_name,
                        c.job_role,
                        i.interview_date,
                        i.interview_time,
                        i.mode,
                        i.venue,
                        i.round_name,
                        i.status,
                        i.feedback
                    FROM interviews i
                    JOIN applications a
                        ON i.application_id = a.application_id
                    JOIN students s
                        ON a.student_id = s.student_id
                    JOIN companies c
                        ON a.company_id = c.company_id
                    ORDER BY i.interview_date, i.interview_time
                    """;

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Date dbDate = rs.getDate("interview_date");

                    String dateText = "";

                    if (dbDate != null) {
                        dateText = dbDate.toLocalDate().format(DISPLAY_DATE);
                    }

                    String timeText = "";

                    Time dbTime = rs.getTime("interview_time");

                    if (dbTime != null) {
                        timeText = dbTime.toLocalTime().format(TIME_FORMAT);
                    }

                    model.addRow(new Object[]{
                            rs.getInt("interview_id"),
                            rs.getInt("application_id"),
                            rs.getString("student_id"),
                            rs.getString("name"),
                            rs.getString("company_name"),
                            rs.getString("job_role"),
                            dateText,
                            timeText,
                            rs.getString("mode"),
                            rs.getString("venue"),
                            rs.getString("round_name"),
                            rs.getString("status"),
                            rs.getString("feedback")
                    });
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error loading interviews:\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };

        // ---------------- SCHEDULE ----------------
        scheduleButton.addActionListener(e -> {

            JTextField applicationField = new JTextField();
            JTextField dateField = new JTextField();
            JTextField timeField = new JTextField();

            JComboBox<String> modeBox = new JComboBox<>(
                    new String[]{"Online", "Offline"}
            );

            JTextField venueField = new JTextField();
            JTextField roundField = new JTextField();

            dateField.setToolTipText("DD-MM-YYYY");
            timeField.setToolTipText("HH:MM");

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

            panel.add(new JLabel("Application ID:"));
            panel.add(applicationField);

            panel.add(new JLabel("Interview Date (DD-MM-YYYY):"));
            panel.add(dateField);

            panel.add(new JLabel("Interview Time (HH:MM):"));
            panel.add(timeField);

            panel.add(new JLabel("Mode:"));
            panel.add(modeBox);

            panel.add(new JLabel("Venue / Meeting Link:"));
            panel.add(venueField);

            panel.add(new JLabel("Interview Round:"));
            panel.add(roundField);

            int result = JOptionPane.showConfirmDialog(
                    mainPanel,
                    panel,
                    "Schedule Interview",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String applicationIdText = applicationField.getText().trim();
            String dateText = dateField.getText().trim();
            String timeText = timeField.getText().trim();
            String mode = (String) modeBox.getSelectedItem();
            String venue = venueField.getText().trim();
            String round = roundField.getText().trim();

            if (applicationIdText.isEmpty()
                    || dateText.isEmpty()
                    || timeText.isEmpty()
                    || venue.isEmpty()
                    || round.isEmpty()) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please fill all fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int applicationId;

            try {
                applicationId = Integer.parseInt(applicationIdText);
            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Application ID must be a number.",
                        "Invalid Application ID",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            LocalDate interviewDate;

            try {
                interviewDate =
                        LocalDate.parse(dateText, DISPLAY_DATE);
            } catch (DateTimeParseException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please enter date in DD-MM-YYYY format.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            LocalTime interviewTime;

            try {
                interviewTime =
                        LocalTime.parse(timeText, TIME_FORMAT);
            } catch (DateTimeParseException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please enter time in HH:MM format.",
                        "Invalid Time",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String checkSql = """
                    SELECT application_id
                    FROM applications
                    WHERE application_id = ?
                    AND status = 'Shortlisted'
                    """;

            String insertSql = """
                    INSERT INTO interviews
                    (
                        application_id,
                        interview_date,
                        interview_time,
                        mode,
                        venue,
                        round_name,
                        status
                    )
                    VALUES (?, ?, ?, ?, ?, ?, 'Scheduled')
                    """;

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement checkStmt =
                         conn.prepareStatement(checkSql)) {

                checkStmt.setInt(1, applicationId);

                try (ResultSet rs = checkStmt.executeQuery()) {

                    if (!rs.next()) {

                        JOptionPane.showMessageDialog(
                                mainPanel,
                                "Interview can be scheduled only for a shortlisted application.",
                                "Invalid Application",
                                JOptionPane.WARNING_MESSAGE
                        );

                        return;
                    }
                }

                try (PreparedStatement insertStmt =
                             conn.prepareStatement(insertSql)) {

                    insertStmt.setInt(1, applicationId);
                    insertStmt.setDate(
                            2,
                            java.sql.Date.valueOf(interviewDate)
                    );
                    insertStmt.setTime(
                            3,
                            java.sql.Time.valueOf(
                                    interviewTime
                            )
                    );
                    insertStmt.setString(4, mode);
                    insertStmt.setString(5, venue);
                    insertStmt.setString(6, round);

                    insertStmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Interview scheduled successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadInterviews.run();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error scheduling interview:\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ---------------- MARK COMPLETED ----------------
        completedButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an interview.",
                        "Select Interview",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int interviewId =
                    Integer.parseInt(
                            table.getValueAt(selectedRow, 0).toString()
                    );

            JTextField feedbackField = new JTextField();

            JPanel feedbackPanel = new JPanel(new BorderLayout(10, 10));

            feedbackPanel.add(
                    new JLabel("Interview Feedback:"),
                    BorderLayout.WEST
            );

            feedbackPanel.add(
                    feedbackField,
                    BorderLayout.CENTER
            );

            int result = JOptionPane.showConfirmDialog(
                    mainPanel,
                    feedbackPanel,
                    "Complete Interview",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String feedback = feedbackField.getText().trim();

            String sql = """
                    UPDATE interviews
                    SET status = 'Completed',
                        feedback = ?
                    WHERE interview_id = ?
                    """;

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, feedback);
                stmt.setInt(2, interviewId);

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Interview marked as completed.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadInterviews.run();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error updating interview:\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ---------------- CANCEL ----------------
        cancelledButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select an interview.",
                        "Select Interview",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int interviewId =
                    Integer.parseInt(
                            table.getValueAt(selectedRow, 0).toString()
                    );

            int choice = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Are you sure you want to cancel this interview?",
                    "Cancel Interview",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            String sql = """
                    UPDATE interviews
                    SET status = 'Cancelled'
                    WHERE interview_id = ?
                    """;

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, interviewId);

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Interview cancelled.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadInterviews.run();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error cancelling interview:\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ---------------- REFRESH ----------------
        refreshButton.addActionListener(e -> loadInterviews.run());

        loadInterviews.run();

        return mainPanel;
    }
}