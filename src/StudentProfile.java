
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class StudentProfile {

    public static void showProfile(String email) {

        JFrame frame = new JFrame("My Profile");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel title = new JLabel("MY PROFILE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(
                new BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        );

        // PERSONAL AND ACADEMIC DETAILS

        JPanel detailsPanel = new JPanel(
                new GridLayout(4, 4, 10, 10)
        );

        detailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Personal & Academic Details",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 16)
                )
        );

        JLabel nameValue = new JLabel();
        JLabel studentIdValue = new JLabel();
        JLabel emailValue = new JLabel();
        JLabel mobileValue = new JLabel();
        JLabel departmentValue = new JLabel();
        JLabel yearValue = new JLabel();
        JLabel cgpaValue = new JLabel();
        JLabel backlogValue = new JLabel();

        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(nameValue);

        detailsPanel.add(new JLabel("Student ID:"));
        detailsPanel.add(studentIdValue);

        detailsPanel.add(new JLabel("College Email:"));
        detailsPanel.add(emailValue);

        detailsPanel.add(new JLabel("Mobile:"));
        detailsPanel.add(mobileValue);

        detailsPanel.add(new JLabel("Department:"));
        detailsPanel.add(departmentValue);

        detailsPanel.add(new JLabel("Study Year:"));
        detailsPanel.add(yearValue);

        detailsPanel.add(new JLabel("CGPA:"));
        detailsPanel.add(cgpaValue);

        detailsPanel.add(new JLabel("Active Backlogs:"));
        detailsPanel.add(backlogValue);

        // PARENT CONTACT DETAILS

        JPanel parentPanel = new JPanel(new BorderLayout(10, 8));

        parentPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Parent Contact Details",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 16)
                )
        );

        JPanel parentFieldsPanel = new JPanel(
                new GridLayout(2, 4, 10, 10)
        );

        JTextField motherNameField = new JTextField();
        JTextField motherMobileField = new JTextField();
        JTextField fatherNameField = new JTextField();
        JTextField fatherMobileField = new JTextField();

        // Parent fields remain display-only.
        motherNameField.setEditable(false);
        motherMobileField.setEditable(false);
        fatherNameField.setEditable(false);
        fatherMobileField.setEditable(false);

        parentFieldsPanel.add(new JLabel("Mother's Name:"));
        parentFieldsPanel.add(motherNameField);

        parentFieldsPanel.add(new JLabel("Mother's Mobile:"));
        parentFieldsPanel.add(motherMobileField);

        parentFieldsPanel.add(new JLabel("Father's Name:"));
        parentFieldsPanel.add(fatherNameField);

        parentFieldsPanel.add(new JLabel("Father's Mobile:"));
        parentFieldsPanel.add(fatherMobileField);

        JTextArea parentInstruction = new JTextArea(
                "Use Edit Profile to update your personal and parent details."
        );
        parentInstruction.setEditable(false);
        parentInstruction.setLineWrap(true);
        parentInstruction.setWrapStyleWord(true);
        parentInstruction.setOpaque(false);
        parentInstruction.setFont(new Font("Arial", Font.PLAIN, 12));
        parentInstruction.setRows(2);

        JButton editProfileButton = new JButton("Edit Profile");

        JPanel parentBottomPanel = new JPanel(new BorderLayout(8, 5));
        parentBottomPanel.add(
                parentInstruction,
                BorderLayout.CENTER
        );

        JPanel editButtonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        editButtonPanel.add(editProfileButton);

        parentBottomPanel.add(
                editButtonPanel,
                BorderLayout.SOUTH
        );

        parentPanel.add(parentFieldsPanel, BorderLayout.NORTH);
        parentPanel.add(parentBottomPanel, BorderLayout.CENTER);

        // SKILLS

        JTextArea skillsArea = new JTextArea();
        skillsArea.setEditable(false);
        skillsArea.setLineWrap(true);
        skillsArea.setWrapStyleWord(true);
        skillsArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel skillsPanel = new JPanel(new BorderLayout());
        skillsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Skills"
                )
        );
        skillsPanel.add(new JScrollPane(skillsArea), BorderLayout.CENTER);

        // ACHIEVEMENTS

        JTextArea achievementsArea = new JTextArea();
        achievementsArea.setEditable(false);
        achievementsArea.setLineWrap(true);
        achievementsArea.setWrapStyleWord(true);
        achievementsArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel achievementsPanel = new JPanel(new BorderLayout());
        achievementsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Achievements"
                )
        );
        achievementsPanel.add(
                new JScrollPane(achievementsArea),
                BorderLayout.CENTER
        );

        JPanel middlePanel = new JPanel(
                new GridLayout(1, 2, 15, 15)
        );
        middlePanel.add(skillsPanel);
        middlePanel.add(achievementsPanel);
        middlePanel.setPreferredSize(new Dimension(800, 160));

        // CERTIFICATES

        JTextArea certificatesArea = new JTextArea();
        certificatesArea.setEditable(false);
        certificatesArea.setLineWrap(true);
        certificatesArea.setWrapStyleWord(true);
        certificatesArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel certificatesPanel = new JPanel(new BorderLayout());
        certificatesPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Certificates"
                )
        );
        certificatesPanel.add(
                new JScrollPane(certificatesArea),
                BorderLayout.CENTER
        );
        certificatesPanel.setPreferredSize(new Dimension(800, 120));

        // RESUME

        JPanel resumePanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );
        resumePanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Resume"
                )
        );

        JButton uploadResumeButton = new JButton("Upload Resume");
        JButton openResumeButton = new JButton("Open Resume");

        String[] resumePathHolder = {null};
        String[] studentIdHolder = {null};

        uploadResumeButton.addActionListener(e -> {

            if (studentIdHolder[0] == null) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Student profile has not loaded."
                );
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Your Resume");

            int result = chooser.showOpenDialog(frame);

            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File selectedFile = chooser.getSelectedFile();

            try {
                Path uploadDirectory =
                        Path.of("uploads", "resumes");

                Files.createDirectories(uploadDirectory);

                String originalName = selectedFile.getName();
                String extension = "";

                int dot = originalName.lastIndexOf('.');
                if (dot >= 0) {
                    extension = originalName.substring(dot);
                }

                String savedName =
                        studentIdHolder[0].replaceAll(
                                "[^a-zA-Z0-9_-]", "_"
                        )
                        + "_" + System.currentTimeMillis()
                        + extension;

                Path destination =
                        uploadDirectory.resolve(savedName);

                Files.copy(
                        selectedFile.toPath(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );

                String savedPath =
                        destination.toAbsolutePath().toString();

                String updateSql = """
                        UPDATE students
                        SET resume_path = ?
                        WHERE student_id = ?
                        """;

                try (
                        Connection connection =
                                DatabaseConnection.getConnection();

                        PreparedStatement statement =
                                connection.prepareStatement(updateSql)
                ) {
                    statement.setString(1, savedPath);
                    statement.setString(2, studentIdHolder[0]);

                    int rows = statement.executeUpdate();

                    if (rows > 0) {
                        resumePathHolder[0] = savedPath;
                        openResumeButton.setEnabled(true);

                        JOptionPane.showMessageDialog(
                                frame,
                                "Resume uploaded successfully!"
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                frame,
                                "Resume was copied, but the student record "
                                + "could not be updated."
                        );
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Could not upload resume: " + ex.getMessage(),
                        "Resume Error",
                        JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        openResumeButton.addActionListener(e -> {

            String path = resumePathHolder[0];

            if (path == null || path.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "No resume has been uploaded."
                );
                return;
            }

            try {
                File resumeFile = new File(path);

                if (!resumeFile.exists()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Resume file not found at:\n" + path
                    );
                    return;
                }

                if (!Desktop.isDesktopSupported()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Opening files is not supported on this system."
                    );
                    return;
                }

                Desktop.getDesktop().open(resumeFile);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Could not open resume: " + ex.getMessage(),
                        "Resume Error",
                        JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        resumePanel.add(uploadResumeButton);
        resumePanel.add(openResumeButton);

        // ARRANGE ALL SECTIONS

        contentPanel.add(detailsPanel);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(parentPanel);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(middlePanel);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(certificatesPanel);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(resumePanel);

        mainPanel.add(
                new JScrollPane(contentPanel),
                BorderLayout.CENTER
        );

        frame.add(mainPanel);

        // EDIT PROFILE: STUDENT AND PARENT DETAILS

        editProfileButton.addActionListener(e -> {

            if (studentIdHolder[0] == null) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Student profile has not loaded."
                );
                return;
            }

            JTextField editName = new JTextField(nameValue.getText());
            JTextField editMobile = new JTextField(mobileValue.getText());
            JTextField editMotherName =
                    new JTextField(motherNameField.getText());
            JTextField editMotherMobile =
                    new JTextField(motherMobileField.getText());
            JTextField editFatherName =
                    new JTextField(fatherNameField.getText());
            JTextField editFatherMobile =
                    new JTextField(fatherMobileField.getText());

            JPanel editPanel = new JPanel(new GridLayout(6, 2, 8, 8));

            editPanel.add(new JLabel("Student Name:"));
            editPanel.add(editName);

            editPanel.add(new JLabel("Student Mobile:"));
            editPanel.add(editMobile);

            editPanel.add(new JLabel("Mother's Name:"));
            editPanel.add(editMotherName);

            editPanel.add(new JLabel("Mother's Mobile:"));
            editPanel.add(editMotherMobile);

            editPanel.add(new JLabel("Father's Name:"));
            editPanel.add(editFatherName);

            editPanel.add(new JLabel("Father's Mobile:"));
            editPanel.add(editFatherMobile);

            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    editPanel,
                    "Edit Profile",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (choice != JOptionPane.OK_OPTION) {
                return;
            }

            String newName = editName.getText().trim();
            String newMobile = editMobile.getText().trim();
            String motherName = editMotherName.getText().trim();
            String motherMobile = editMotherMobile.getText().trim();
            String fatherName = editFatherName.getText().trim();
            String fatherMobile = editFatherMobile.getText().trim();

            if (newName.isEmpty() || newMobile.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Student name and mobile number are required."
                );
                return;
            }

            if (!newMobile.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Student mobile number must contain exactly 10 digits."
                );
                return;
            }

            if (motherName.isEmpty() != motherMobile.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Enter both the mother's name and mobile number, "
                        + "or leave both blank."
                );
                return;
            }

            if (fatherName.isEmpty() != fatherMobile.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Enter both the father's name and mobile number, "
                        + "or leave both blank."
                );
                return;
            }

            if (motherName.isEmpty() && fatherName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Enter at least one complete parent contact."
                );
                return;
            }

            if (!motherMobile.isEmpty()
                    && !motherMobile.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Mother's mobile number must contain exactly 10 digits."
                );
                return;
            }

            if (!fatherMobile.isEmpty()
                    && !fatherMobile.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Father's mobile number must contain exactly 10 digits."
                );
                return;
            }

            String primaryParentName =
                    !motherName.isEmpty() ? motherName : fatherName;

            String primaryParentMobile =
                    !motherMobile.isEmpty() ? motherMobile : fatherMobile;

            String updateSql = """
                    UPDATE students
                    SET
                        name = ?,
                        mobile = ?,
                        mother_name = ?,
                        mother_mobile = ?,
                        father_name = ?,
                        father_mobile = ?,
                        parent_name = ?,
                        parent_mobile = ?
                    WHERE student_id = ?
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(updateSql)
            ) {
                statement.setString(1, newName);
                statement.setString(2, newMobile);
                statement.setString(3, nullableText(motherName));
                statement.setString(4, nullableText(motherMobile));
                statement.setString(5, nullableText(fatherName));
                statement.setString(6, nullableText(fatherMobile));
                statement.setString(7, primaryParentName);
                statement.setString(8, primaryParentMobile);
                statement.setString(9, studentIdHolder[0]);

                int rows = statement.executeUpdate();

                if (rows > 0) {
                    nameValue.setText(newName);
                    mobileValue.setText(newMobile);

                    motherNameField.setText(motherName);
                    motherMobileField.setText(motherMobile);
                    fatherNameField.setText(fatherName);
                    fatherMobileField.setText(fatherMobile);

                    parentInstruction.setText(
                            "Profile and parent details updated successfully."
                    );

                    JOptionPane.showMessageDialog(
                            frame,
                            "Profile updated successfully!"
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "No student record was updated."
                    );
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Could not update profile: " + ex.getMessage(),
                        "Update Error",
                        JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        // LOAD PROFILE FROM MYSQL

        String sql = """
                SELECT
                    s.student_id,
                    s.name,
                    u.college_email,
                    s.mobile,
                    s.parent_name,
                    s.parent_mobile,
                    s.mother_name,
                    s.mother_mobile,
                    s.father_name,
                    s.father_mobile,
                    s.department,
                    s.study_year,
                    s.cgpa,
                    s.active_backlogs,
                    s.resume_path
                FROM students s
                JOIN users u ON s.user_id = u.user_id
                WHERE LOWER(u.college_email) = LOWER(?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);

            String currentStudentId;

            try (ResultSet result = statement.executeQuery()) {

                if (!result.next()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Student profile not found."
                    );
                    frame.dispose();
                    return;
                }

                currentStudentId = result.getString("student_id");
                studentIdHolder[0] = currentStudentId;

                nameValue.setText(textOrEmpty(result.getString("name")));
                studentIdValue.setText(currentStudentId);
                emailValue.setText(
                        textOrEmpty(result.getString("college_email"))
                );
                mobileValue.setText(textOrEmpty(result.getString("mobile")));
                departmentValue.setText(
                        textOrEmpty(result.getString("department"))
                );

                Object studyYear = result.getObject("study_year");
                yearValue.setText(
                        studyYear == null ? "" : studyYear.toString()
                );

                Object cgpa = result.getObject("cgpa");
                cgpaValue.setText(
                        cgpa == null ? "" : cgpa.toString()
                );

                backlogValue.setText(
                        String.valueOf(result.getInt("active_backlogs"))
                );

                motherNameField.setText(
                        textOrEmpty(result.getString("mother_name"))
                );
                motherMobileField.setText(
                        textOrEmpty(result.getString("mother_mobile"))
                );
                fatherNameField.setText(
                        textOrEmpty(result.getString("father_name"))
                );
                fatherMobileField.setText(
                        textOrEmpty(result.getString("father_mobile"))
                );

                resumePathHolder[0] = result.getString("resume_path");

                openResumeButton.setEnabled(
                        resumePathHolder[0] != null
                        && !resumePathHolder[0].trim().isEmpty()
                );

                // PRESERVE OLD SINGLE-PARENT DATA

                String oldParentName =
                        textOrEmpty(result.getString("parent_name"));
                String oldParentMobile =
                        textOrEmpty(result.getString("parent_mobile"));

                boolean newParentDetailsEmpty =
                        motherNameField.getText().trim().isEmpty()
                        && motherMobileField.getText().trim().isEmpty()
                        && fatherNameField.getText().trim().isEmpty()
                        && fatherMobileField.getText().trim().isEmpty();

                if (newParentDetailsEmpty
                        && (!oldParentName.isEmpty()
                        || !oldParentMobile.isEmpty())) {

                    parentInstruction.setText(
                            "Previously saved parent contact: "
                            + oldParentName + " / " + oldParentMobile
                            + ". Update it under the correct parent fields "
                            + "using Edit Profile."
                    );
                }
            }

            skillsArea.setText(
                    loadSkills(connection, currentStudentId)
            );

            achievementsArea.setText(
                    loadAchievements(connection, currentStudentId)
            );

            certificatesArea.setText(
                    loadCertificates(connection, currentStudentId)
            );

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Database error: " + e.getMessage(),
                    "Profile Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            frame.dispose();
            return;
        }

        frame.setVisible(true);
    }

    // SKILLS LIST

    private static String loadSkills(
            Connection connection,
            String studentId) throws SQLException {

        String sql = """
                SELECT skill_name
                FROM student_skills
                WHERE student_id = ?
                """;

        StringBuilder items = new StringBuilder();

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, studentId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    items.append("- ")
                            .append(result.getString("skill_name"))
                            .append("\n");
                }
            }
        }

        return items.length() == 0
                ? "No skills added yet."
                : items.toString();
    }

    // ACHIEVEMENTS LIST

    private static String loadAchievements(
            Connection connection,
            String studentId) throws SQLException {

        String sql = """
                SELECT achievement_name, description
                FROM achievements
                WHERE student_id = ?
                """;

        StringBuilder items = new StringBuilder();

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, studentId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {

                    items.append("- ")
                            .append(result.getString("achievement_name"));

                    String description = result.getString("description");

                    if (description != null
                            && !description.trim().isEmpty()) {
                        items.append(" - ").append(description);
                    }

                    items.append("\n");
                }
            }
        }

        return items.length() == 0
                ? "No achievements added yet."
                : items.toString();
    }

    // CERTIFICATES LIST

    private static String loadCertificates(
            Connection connection,
            String studentId) throws SQLException {

        String sql = """
                SELECT certificate_name
                FROM certificates
                WHERE student_id = ?
                """;

        StringBuilder items = new StringBuilder();

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, studentId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    items.append("- ")
                            .append(result.getString("certificate_name"))
                            .append("\n");
                }
            }
        }

        return items.length() == 0
                ? "No certificates added yet."
                : items.toString();
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private static String nullableText(String value) {
        return value == null || value.trim().isEmpty()
                ? null
                : value.trim();
    }
}
