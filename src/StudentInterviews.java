import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class StudentInterviews {

    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final DateTimeFormatter DISPLAY_TIME =
            DateTimeFormatter.ofPattern("HH:mm");

    public static JPanel createInterviewsPanel(String email) {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ==========================================
        // HEADER
        // ==========================================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 78, 121));
        header.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        JLabel title = new JLabel("My Interviews");

        title.setForeground(Color.WHITE);
        title.setFont(
                new Font("Segoe UI", Font.BOLD, 24)
        );

        JLabel subtitle = new JLabel(
                "View your scheduled placement interviews"
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

        // ==========================================
        // TABLE
        // ==========================================

        String[] columns = {
                "Company",
                "Job Role",
                "Interview Date",
                "Time",
                "Mode",
                "Venue / Meeting Link",
                "Round",
                "Status",
                "Feedback"
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

        JTable table = new JTable(model);

        table.setRowHeight(30);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        table.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        10,
                        15
                )
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // ==========================================
        // BOTTOM PANEL
        // ==========================================

        JButton refreshButton =
                new JButton("Refresh Interviews");

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

        bottomPanel.setBackground(Color.WHITE);

        bottomPanel.add(refreshButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // ==========================================
        // LOAD INTERVIEWS
        // ==========================================

        Runnable loadInterviews = () -> {

            model.setRowCount(0);

            String sql = """
                    SELECT
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
                    JOIN users u
                        ON s.user_id = u.user_id
                    JOIN companies c
                        ON a.company_id = c.company_id
                    WHERE LOWER(u.college_email) = LOWER(?)
                    ORDER BY
                        i.interview_date,
                        i.interview_time
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

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    while (result.next()) {

                        String dateText = "";

                        Date interviewDate =
                                result.getDate(
                                        "interview_date"
                                );

                        if (interviewDate != null) {

                            dateText =
                                    interviewDate
                                            .toLocalDate()
                                            .format(
                                                    DISPLAY_DATE
                                            );
                        }

                        String timeText = "";

                        Time interviewTime =
                                result.getTime(
                                        "interview_time"
                                );

                        if (interviewTime != null) {

                            timeText =
                                    interviewTime
                                            .toLocalTime()
                                            .format(
                                                    DISPLAY_TIME
                                            );
                        }

                        model.addRow(
                                new Object[]{
                                        result.getString(
                                                "company_name"
                                        ),
                                        result.getString(
                                                "job_role"
                                        ),
                                        dateText,
                                        timeText,
                                        result.getString(
                                                "mode"
                                        ),
                                        result.getString(
                                                "venue"
                                        ),
                                        result.getString(
                                                "round_name"
                                        ),
                                        result.getString(
                                                "status"
                                        ),
                                        result.getString(
                                                "feedback"
                                        )
                                }
                        );
                    }
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Error loading interviews:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };

        refreshButton.addActionListener(
                e -> loadInterviews.run()
        );

        loadInterviews.run();

        return mainPanel;
    }
}