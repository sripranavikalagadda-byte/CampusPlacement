import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FacultyStudentProfile {

    public static void showProfile(String studentId) {

        JFrame frame = new JFrame("Student Profile");
        frame.setSize(650, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel title = new JLabel("STUDENT PROFILE");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(220, 30, 300, 40);

        JLabel details = new JLabel();
        details.setBounds(70, 100, 500, 300);

        String sql = """
                SELECT s.student_id, s.name, u.college_email,
                       s.mobile, s.parent_name, s.parent_mobile,
                       s.department, s.study_year, s.cgpa,
                       s.active_backlogs
                FROM students s
                JOIN users u ON s.user_id = u.user_id
                WHERE s.student_id = ?
                """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(1, studentId);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                String information =
                        "<html>"
                        + "<b>Student ID:</b> " + result.getString("student_id")
                        + "<br><br>"
                        + "<b>Name:</b> " + result.getString("name")
                        + "<br><br>"
                        + "<b>Email:</b> " + result.getString("college_email")
                        + "<br><br>"
                        + "<b>Mobile:</b> " + result.getString("mobile")
                        + "<br><br>"
                        + "<b>Parent Name:</b> " + result.getString("parent_name")
                        + "<br><br>"
                        + "<b>Parent Mobile:</b> " + result.getString("parent_mobile")
                        + "<br><br>"
                        + "<b>Department:</b> " + result.getString("department")
                        + "<br><br>"
                        + "<b>Study Year:</b> " + result.getInt("study_year")
                        + "<br><br>"
                        + "<b>CGPA:</b> " + result.getDouble("cgpa")
                        + "<br><br>"
                        + "<b>Active Backlogs:</b> " + result.getInt("active_backlogs")
                        + "</html>";

                details.setText(information);

            } else {

                JOptionPane.showMessageDialog(
                        frame,
                        "Student not found."
                );
                return;
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Database error: " + e.getMessage()
            );
        }

        panel.add(title);
        panel.add(details);

        frame.add(panel);
        frame.setVisible(true);
    }
}