import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class FacultySelected {

    public static JPanel createSelectedPanel() {

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

        JLabel title =
                new JLabel(
                        "SELECTED STUDENTS",
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

        String[] columns = {
                "Selection ID",
                "Student ID",
                "Student Name",
                "Branch",
                "Company",
                "Job Role",
                "Package",
                "Offer Date"
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

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        JButton refreshButton =
                new JButton("Refresh");

        bottomPanel.add(
                refreshButton
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        loadSelectedStudents(model);

        refreshButton.addActionListener(
                e -> loadSelectedStudents(model)
        );

        return mainPanel;
    }

    private static void loadSelectedStudents(
            DefaultTableModel model) {

        model.setRowCount(0);

        String sql = """
                SELECT
                    se.selection_id,
                    s.student_id,
                    s.name,
                    s.department,
                    c.company_name,
                    c.job_role,
                    se.package_lpa,
                    se.offer_date
                FROM selections se
                JOIN students s
                    ON se.student_id = s.student_id
                JOIN companies c
                    ON se.company_id = c.company_id
                ORDER BY se.selection_id DESC
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
                    DateTimeFormatter.ofPattern(
                            "dd-MM-yyyy"
                    );

            while (result.next()) {

                String offerDate = "";

                if (
                        result.getDate(
                                "offer_date"
                        ) != null
                ) {

                    offerDate =
                            result.getDate(
                                    "offer_date"
                            )
                            .toLocalDate()
                            .format(formatter);
                }

                model.addRow(
                        new Object[]{
                                result.getInt(
                                        "selection_id"
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

                                result.getString(
                                        "company_name"
                                ),

                                result.getString(
                                        "job_role"
                                ),

                                result.getDouble(
                                        "package_lpa"
                                ) + " LPA",

                                offerDate
                        }
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
}