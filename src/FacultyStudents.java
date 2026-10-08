import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FacultyStudents {

    public static JPanel createStudentsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // ==========================
        // TITLE
        // ==========================

        JLabel title = new JLabel(
                "STUDENT MANAGEMENT",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));

        // ==========================
        // FILTER PANEL
        // ==========================

        JPanel filterPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 8)
        );

        JLabel branchLabel = new JLabel("Branch:");

        String[] branches = {
                "ALL",
                "CSE",
                "EEE",
                "IT",
                "ETM",
                "ECE",
                "CSM",
                "CSD"
        };

        JComboBox<String> branchCombo =
                new JComboBox<>(branches);

        JLabel searchLabel = new JLabel("Search:");

        JTextField searchField = new JTextField(12);

        JLabel minCgpaLabel = new JLabel("Min CGPA:");

        JTextField minCgpaField =
                new JTextField(5);

        JLabel maxBacklogLabel =
                new JLabel("Max Backlogs:");

        JTextField maxBacklogField =
                new JTextField(5);

        JButton filterButton =
                new JButton("Apply Filters");

        JButton clearButton =
                new JButton("Clear");

        JLabel countLabel =
                new JLabel("Students: 0");

        filterPanel.add(branchLabel);
        filterPanel.add(branchCombo);

        filterPanel.add(searchLabel);
        filterPanel.add(searchField);

        filterPanel.add(minCgpaLabel);
        filterPanel.add(minCgpaField);

        filterPanel.add(maxBacklogLabel);
        filterPanel.add(maxBacklogField);

        filterPanel.add(filterButton);
        filterPanel.add(clearButton);

        filterPanel.add(countLabel);

        // ==========================
        // TABLE
        // ==========================

        String[] columns = {
                "Student ID",
                "Name",
                "Department",
                "Year",
                "CGPA",
                "Backlogs",
                "Mobile"
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

        table.setRowHeight(28);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane tableScrollPane =
                new JScrollPane(table);

        // ==========================
        // STUDENT DETAILS
        // ==========================

        JPanel detailsPanel =
                new JPanel(new BorderLayout());

        detailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Selected Student"
                )
        );

        JLabel detailsLabel =
                new JLabel(
                        "<html><center>"
                        + "Select a student from the table"
                        + "<br>"
                        + "to view details."
                        + "</center></html>",
                        SwingConstants.CENTER
                );

        detailsLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        detailsPanel.add(
                detailsLabel,
                BorderLayout.CENTER
        );

        detailsPanel.setPreferredSize(
                new Dimension(320, 450)
        );

        // ==========================
        // INITIAL LOAD
        // ==========================

        loadStudents(
                model,
                "ALL",
                "",
                "",
                "",
                countLabel
        );

        // ==========================
        // FILTER BUTTON
        // ==========================

        filterButton.addActionListener(e -> {

            String branch =
                    branchCombo.getSelectedItem().toString();

            String search =
                    searchField.getText().trim();

            String minCgpa =
                    minCgpaField.getText().trim();

            String maxBacklogs =
                    maxBacklogField.getText().trim();

            loadStudents(
                    model,
                    branch,
                    search,
                    minCgpa,
                    maxBacklogs,
                    countLabel
            );

            detailsLabel.setText(
                    "<html><center>"
                    + "Select a student from the table"
                    + "<br>"
                    + "to view details."
                    + "</center></html>"
            );
        });

        // ==========================
        // CLEAR FILTERS
        // ==========================

        clearButton.addActionListener(e -> {

            branchCombo.setSelectedItem("ALL");
            searchField.setText("");
            minCgpaField.setText("");
            maxBacklogField.setText("");

            loadStudents(
                    model,
                    "ALL",
                    "",
                    "",
                    "",
                    countLabel
            );

            detailsLabel.setText(
                    "<html><center>"
                    + "Select a student from the table"
                    + "<br>"
                    + "to view details."
                    + "</center></html>"
            );
        });

        // ==========================
        // STUDENT CLICK
        // ==========================

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && table.getSelectedRow() != -1) {

                int row =
                        table.getSelectedRow();

                String studentId =
                        model.getValueAt(
                                row,
                                0
                        ).toString();

                showStudentDetails(
                        studentId,
                        detailsLabel
                );
            }
        });

        // ==========================
        // TOP AREA
        // ==========================

        JPanel northPanel =
                new JPanel(new BorderLayout());

        northPanel.add(
                title,
                BorderLayout.NORTH
        );

        northPanel.add(
                filterPanel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                northPanel,
                BorderLayout.NORTH
        );

        // ==========================
        // CENTER
        // ==========================

        mainPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        // ==========================
        // RIGHT DETAILS
        // ==========================

        mainPanel.add(
                detailsPanel,
                BorderLayout.EAST
        );

        return mainPanel;
    }

    // ==========================
    // LOAD STUDENTS
    // ==========================

    private static void loadStudents(
            DefaultTableModel model,
            String branch,
            String search,
            String minCgpaText,
            String maxBacklogsText,
            JLabel countLabel) {

        model.setRowCount(0);

        StringBuilder sql =
                new StringBuilder(
                        """
                        SELECT student_id,
                               name,
                               department,
                               study_year,
                               cgpa,
                               active_backlogs,
                               mobile
                        FROM students
                        WHERE 1 = 1
                        """
                );

        if (!branch.equals("ALL")) {
            sql.append(
                    " AND UPPER(department) = ? "
            );
        }

        if (!search.isEmpty()) {
            sql.append(
                    " AND (student_id LIKE ? OR name LIKE ?) "
            );
        }

        if (!minCgpaText.isEmpty()) {
            sql.append(
                    " AND cgpa >= ? "
            );
        }

        if (!maxBacklogsText.isEmpty()) {
            sql.append(
                    " AND active_backlogs <= ? "
            );
        }

        sql.append(
                " ORDER BY department, name"
        );

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql.toString()
                        )
        ) {

            int parameterIndex = 1;

            if (!branch.equals("ALL")) {

                statement.setString(
                        parameterIndex++,
                        branch
                );
            }

            if (!search.isEmpty()) {

                statement.setString(
                        parameterIndex++,
                        "%" + search + "%"
                );

                statement.setString(
                        parameterIndex++,
                        "%" + search + "%"
                );
            }

            if (!minCgpaText.isEmpty()) {

                double minCgpa =
                        Double.parseDouble(
                                minCgpaText
                        );

                statement.setDouble(
                        parameterIndex++,
                        minCgpa
                );
            }

            if (!maxBacklogsText.isEmpty()) {

                int maxBacklogs =
                        Integer.parseInt(
                                maxBacklogsText
                        );

                statement.setInt(
                        parameterIndex++,
                        maxBacklogs
                );
            }

            ResultSet result =
                    statement.executeQuery();

            int count = 0;

            while (result.next()) {

                model.addRow(
                        new Object[]{
                                result.getString(
                                        "student_id"
                                ),
                                result.getString(
                                        "name"
                                ),
                                result.getString(
                                        "department"
                                ),
                                result.getInt(
                                        "study_year"
                                ),
                                result.getDouble(
                                        "cgpa"
                                ),
                                result.getInt(
                                        "active_backlogs"
                                ),
                                result.getString(
                                        "mobile"
                                )
                        }
                );

                count++;
            }

            countLabel.setText(
                    "Students: " + count
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Please enter valid numbers for CGPA and backlogs."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Database error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ==========================
    // STUDENT DETAILS
    // ==========================

    private static void showStudentDetails(
            String studentId,
            JLabel detailsLabel) {

        String sql =
                """
                SELECT s.student_id,
                       s.name,
                       u.college_email,
                       s.mobile,
                       s.parent_name,
                       s.parent_mobile,
                       s.department,
                       s.study_year,
                       s.cgpa,
                       s.active_backlogs
                FROM students s
                JOIN users u
                    ON s.user_id = u.user_id
                WHERE s.student_id = ?
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

            if (result.next()) {

                String information =
                        "<html>"
                        + "<div style='width:280px;'>"

                        + "<h2>"
                        + result.getString("name")
                        + "</h2>"

                        + "<b>Student ID:</b> "
                        + result.getString("student_id")
                        + "<br><br>"

                        + "<b>Email:</b> "
                        + result.getString("college_email")
                        + "<br><br>"

                        + "<b>Mobile:</b> "
                        + result.getString("mobile")
                        + "<br><br>"

                        + "<b>Parent Name:</b> "
                        + result.getString("parent_name")
                        + "<br><br>"

                        + "<b>Parent Mobile:</b> "
                        + result.getString("parent_mobile")
                        + "<br><br>"

                        + "<b>Department:</b> "
                        + result.getString("department")
                        + "<br><br>"

                        + "<b>Study Year:</b> "
                        + result.getInt("study_year")
                        + "<br><br>"

                        + "<b>CGPA:</b> "
                        + result.getDouble("cgpa")
                        + "<br><br>"

                        + "<b>Active Backlogs:</b> "
                        + result.getInt("active_backlogs")

                        + "</div>"
                        + "</html>";

                detailsLabel.setText(
                        information
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