import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class App {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Campus Placement Management System");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel title = new JLabel("CAMPUS PLACEMENT MANAGEMENT SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(80, 40, 470, 30);

        JLabel emailLabel = new JLabel("College Email:");
        emailLabel.setBounds(110, 110, 120, 25);

        JTextField emailField = new JTextField();
        emailField.setBounds(230, 110, 230, 30);

        JLabel passwordLabel = new JLabel("Password:");
passwordLabel.setBounds(110, 160, 120, 25);

JPasswordField passwordField = new JPasswordField();
passwordField.setBounds(230, 160, 190, 30);

// Eye button
JButton showPasswordButton = new JButton("👁");
showPasswordButton.setBounds(425, 160, 45, 30);
showPasswordButton.setMargin(new Insets(0, 0, 0, 0));
showPasswordButton.setToolTipText("Show / Hide Password");

// Remember the normal hidden-password character
char defaultEchoChar = passwordField.getEchoChar();

// Show / hide password
showPasswordButton.addActionListener(e -> {

    if (passwordField.getEchoChar() == 0) {
        passwordField.setEchoChar(defaultEchoChar);
    } else {
        passwordField.setEchoChar((char) 0);
    }
});

        JLabel roleLabel = new JLabel("Login As:");
        roleLabel.setBounds(110, 215, 120, 25);

        JRadioButton studentButton = new JRadioButton("Student");
        studentButton.setBounds(230, 215, 100, 25);
        studentButton.setSelected(true);

        JRadioButton facultyButton = new JRadioButton("Faculty");
        facultyButton.setBounds(340, 215, 100, 25);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(studentButton);
        roleGroup.add(facultyButton);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(245, 280, 120, 35);

        loginButton.addActionListener(e -> {

            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            String selectedRole;

            if (studentButton.isSelected()) {
                selectedRole = "STUDENT";
            } else {
                selectedRole = "FACULTY";
            }

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter email and password."
                );
                return;
            }

            // Check college email format
            if (!email.toLowerCase().endsWith("@gnits.ac.in")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please use your @gnits.ac.in college email."
                );
                return;
            }

            String sql = """
                    SELECT user_id, college_email, role
                    FROM users
                    WHERE LOWER(college_email) = LOWER(?)
                    AND password = ?
                    AND role = ?
                    """;

            try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {

                statement.setString(1, email);
                statement.setString(2, password);
                statement.setString(3, selectedRole);

                ResultSet result = statement.executeQuery();

                if (result.next()) {

                    if (selectedRole.equals("STUDENT")) {

                        frame.dispose();
                        StudentDashboard.showDashboard(email);

                    } else {

                        frame.dispose();
                        FacultyDashboard.showDashboard();
                    }

                } else {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Invalid email, password, or selected role."
                    );
                }

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Database error: " + ex.getMessage()
                );

                ex.printStackTrace();
            }
        });

        panel.add(title);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(showPasswordButton);
        panel.add(roleLabel);
        panel.add(studentButton);
        panel.add(facultyButton);
        panel.add(loginButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}