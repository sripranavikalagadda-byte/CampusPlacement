import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class StudentProfile {

    // ==========================================
    // SHOW PROFILE
    // ==========================================

    public static void showProfile(String email) {

        JFrame frame =
                new JFrame("My Profile");

        frame.setSize(900, 800);

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(15, 15));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        // ==========================================
        // TITLE
        // ==========================================

        JLabel title =
                new JLabel(
                        "MY PROFILE",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // ==========================================
        // CONTENT
        // ==========================================

        JPanel contentPanel =
                new JPanel();

        contentPanel.setLayout(
                new BoxLayout(
                        contentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        // ==========================================
        // PERSONAL & ACADEMIC DETAILS
        // ==========================================

        JPanel detailsPanel =
                new JPanel(
                        new GridLayout(
                                5,
                                4,
                                10,
                                10
                        )
                );

        detailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        "Personal & Academic Details",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font(
                                "Arial",
                                Font.BOLD,
                                16
                        )
                )
        );

        JLabel nameValue = new JLabel();
        JLabel studentIdValue = new JLabel();
        JLabel emailValue = new JLabel();
        JLabel mobileValue = new JLabel();

        JLabel parentValue = new JLabel();
        JLabel parentMobileValue = new JLabel();
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

        detailsPanel.add(new JLabel("Parent Name:"));
        detailsPanel.add(parentValue);

        detailsPanel.add(new JLabel("Parent Mobile:"));
        detailsPanel.add(parentMobileValue);

        detailsPanel.add(new JLabel("Department:"));
        detailsPanel.add(departmentValue);

        detailsPanel.add(new JLabel("Study Year:"));
        detailsPanel.add(yearValue);

        detailsPanel.add(new JLabel("CGPA:"));
        detailsPanel.add(cgpaValue);

        detailsPanel.add(new JLabel("Active Backlogs:"));
        detailsPanel.add(backlogValue);

        // ==========================================
        // SKILLS
        // ==========================================

        JTextArea skillsArea =
                new JTextArea();

        skillsArea.setEditable(false);
        skillsArea.setLineWrap(true);
        skillsArea.setWrapStyleWord(true);

        skillsArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JPanel skillsPanel =
                new JPanel(new BorderLayout(5, 5));

        skillsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        "Skills",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font(
                                "Arial",
                                Font.BOLD,
                                16
                        )
                )
        );

        JButton addSkillButton =
                new JButton("Add Skill");

        JButton deleteSkillButton =
                new JButton("Delete Skill");

        JPanel skillsButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        skillsButtons.add(addSkillButton);
        skillsButtons.add(deleteSkillButton);

        skillsPanel.add(
                new JScrollPane(skillsArea),
                BorderLayout.CENTER
        );

        skillsPanel.add(
                skillsButtons,
                BorderLayout.SOUTH
        );

        // ==========================================
        // ACHIEVEMENTS
        // ==========================================

        JTextArea achievementsArea =
                new JTextArea();

        achievementsArea.setEditable(false);
        achievementsArea.setLineWrap(true);
        achievementsArea.setWrapStyleWord(true);

        achievementsArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JPanel achievementsPanel =
                new JPanel(new BorderLayout(5, 5));

        achievementsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        "Achievements",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font(
                                "Arial",
                                Font.BOLD,
                                16
                        )
                )
        );

        JButton addAchievementButton =
                new JButton("Add Achievement");

        JButton deleteAchievementButton =
                new JButton("Delete Achievement");

        JPanel achievementButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        achievementButtons.add(
                addAchievementButton
        );

        achievementButtons.add(
                deleteAchievementButton
        );

        achievementsPanel.add(
                new JScrollPane(achievementsArea),
                BorderLayout.CENTER
        );

        achievementsPanel.add(
                achievementButtons,
                BorderLayout.SOUTH
        );

        // ==========================================
        // SKILLS + ACHIEVEMENTS
        // ==========================================

        JPanel middlePanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                15
                        )
                );

        middlePanel.add(skillsPanel);
        middlePanel.add(achievementsPanel);

        middlePanel.setPreferredSize(
                new Dimension(
                        800,
                        200
                )
        );

        // ==========================================
        // CERTIFICATES
        // ==========================================

        JTextArea certificatesArea =
                new JTextArea();

        certificatesArea.setEditable(false);
        certificatesArea.setLineWrap(true);
        certificatesArea.setWrapStyleWord(true);

        certificatesArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JPanel certificatesPanel =
                new JPanel(new BorderLayout(5, 5));

        certificatesPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        "Certificates",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font(
                                "Arial",
                                Font.BOLD,
                                16
                        )
                )
        );

        JButton addCertificateButton =
                new JButton("Add Certificate");

        JButton deleteCertificateButton =
                new JButton("Delete Certificate");

        JPanel certificateButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        certificateButtons.add(
                addCertificateButton
        );

        certificateButtons.add(
                deleteCertificateButton
        );

        certificatesPanel.add(
                new JScrollPane(certificatesArea),
                BorderLayout.CENTER
        );

        certificatesPanel.add(
                certificateButtons,
                BorderLayout.SOUTH
        );

        certificatesPanel.setPreferredSize(
                new Dimension(
                        800,
                        160
                )
        );

        // ==========================================
        // RESUME
        // ==========================================

        JPanel resumePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        resumePanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        "Resume",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font(
                                "Arial",
                                Font.BOLD,
                                16
                        )
                )
        );

        JButton uploadResumeButton =
                new JButton("Upload Resume");

        JButton openResumeButton =
                new JButton("Open Resume");

        resumePanel.add(
                uploadResumeButton
        );

        resumePanel.add(
                openResumeButton
        );

        // ==========================================
        // EDIT PROFILE BUTTON
        // ==========================================

        JButton editProfileButton =
                new JButton("Edit Profile");

        editProfileButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        JButton closeButton =
                new JButton("Close");

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottomPanel.add(
                editProfileButton
        );

        bottomPanel.add(
                closeButton
        );

        // ==========================================
        // DATABASE DATA
        // ==========================================

        String studentSql = """
                SELECT
                    s.student_id,
                    s.name,
                    u.college_email,
                    s.mobile,
                    s.parent_name,
                    s.parent_mobile,
                    s.department,
                    s.study_year,
                    s.cgpa,
                    s.active_backlogs,
                    s.resume_path
                FROM students s
                JOIN users u
                    ON s.user_id = u.user_id
                WHERE LOWER(u.college_email) = LOWER(?)
                """;

        final String[] studentIdHolder =
                new String[1];

        final String[] resumePathHolder =
                new String[1];

        // ==========================================
        // LOAD PROFILE
        // ==========================================

        Runnable loadProfile = () -> {

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    studentSql
                            )
            ) {

                statement.setString(
                        1,
                        email
                );

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (!result.next()) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "Student profile not found.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    String studentId =
                            result.getString(
                                    "student_id"
                            );

                    studentIdHolder[0] =
                            studentId;

                    nameValue.setText(
                            safe(result.getString("name"))
                    );

                    studentIdValue.setText(
                            safe(studentId)
                    );

                    emailValue.setText(
                            safe(result.getString(
                                    "college_email"
                            ))
                    );

                    mobileValue.setText(
                            safe(result.getString("mobile"))
                    );

                    parentValue.setText(
                            safe(result.getString(
                                    "parent_name"
                            ))
                    );

                    parentMobileValue.setText(
                            safe(result.getString(
                                    "parent_mobile"
                            ))
                    );

                    departmentValue.setText(
                            safe(result.getString(
                                    "department"
                            ))
                    );

                    yearValue.setText(
                            String.valueOf(
                                    result.getInt(
                                            "study_year"
                                    )
                            )
                    );

                    cgpaValue.setText(
                            String.valueOf(
                                    result.getDouble(
                                            "cgpa"
                                    )
                            )
                    );

                    backlogValue.setText(
                            String.valueOf(
                                    result.getInt(
                                            "active_backlogs"
                                    )
                                )
                    );

                    resumePathHolder[0] =
                            result.getString(
                                    "resume_path"
                            );
                }

                loadSkills(
                        connection,
                        studentIdHolder[0],
                        skillsArea
                );

                loadAchievements(
                        connection,
                        studentIdHolder[0],
                        achievementsArea
                );

                loadCertificates(
                        connection,
                        studentIdHolder[0],
                        certificatesArea
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Database error:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };

        // ==========================================
        // EDIT PROFILE
        // ==========================================

        editProfileButton.addActionListener(e -> {

            JTextField nameField =
                    new JTextField(
                            nameValue.getText()
                    );

            JTextField mobileField =
                    new JTextField(
                            mobileValue.getText()
                    );

            JTextField parentField =
                    new JTextField(
                            parentValue.getText()
                    );

            JTextField parentMobileField =
                    new JTextField(
                            parentMobileValue.getText()
                    );

            JTextField departmentField =
                    new JTextField(
                            departmentValue.getText()
                    );

            JTextField yearField =
                    new JTextField(
                            yearValue.getText()
                    );

            JTextField cgpaField =
                    new JTextField(
                            cgpaValue.getText()
                    );

            JTextField backlogField =
                    new JTextField(
                            backlogValue.getText()
                    );

            JPanel panel =
                    new JPanel(
                            new GridLayout(
                                    0,
                                    2,
                                    10,
                                    10
                            )
                    );

            panel.add(new JLabel("Name:"));
            panel.add(nameField);

            panel.add(new JLabel("Mobile:"));
            panel.add(mobileField);

            panel.add(new JLabel("Parent Name:"));
            panel.add(parentField);

            panel.add(new JLabel("Parent Mobile:"));
            panel.add(parentMobileField);

            panel.add(new JLabel("Department:"));
            panel.add(departmentField);

            panel.add(new JLabel("Study Year:"));
            panel.add(yearField);

            panel.add(new JLabel("CGPA:"));
            panel.add(cgpaField);

            panel.add(new JLabel("Active Backlogs:"));
            panel.add(backlogField);

            int result =
                    JOptionPane.showConfirmDialog(
                            frame,
                            panel,
                            "Edit Profile",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            try {

                int studyYear =
                        Integer.parseInt(
                                yearField.getText().trim()
                        );

                double cgpa =
                        Double.parseDouble(
                                cgpaField.getText().trim()
                        );

                int backlogs =
                        Integer.parseInt(
                                backlogField.getText().trim()
                        );

                if (studyYear < 1 ||
                        studyYear > 4) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Study year must be between 1 and 4.",
                            "Invalid Study Year",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                if (cgpa < 0 ||
                        cgpa > 10) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "CGPA must be between 0 and 10.",
                            "Invalid CGPA",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                if (backlogs < 0) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Backlogs cannot be negative.",
                            "Invalid Backlogs",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                String updateSql = """
                        UPDATE students
                        SET name = ?,
                            mobile = ?,
                            parent_name = ?,
                            parent_mobile = ?,
                            department = ?,
                            study_year = ?,
                            cgpa = ?,
                            active_backlogs = ?
                        WHERE student_id = ?
                        """;

                try (
                        Connection connection =
                                DatabaseConnection.getConnection();

                        PreparedStatement statement =
                                connection.prepareStatement(
                                        updateSql
                                )
                ) {

                    statement.setString(
                            1,
                            nameField.getText().trim()
                    );

                    statement.setString(
                            2,
                            mobileField.getText().trim()
                    );

                    statement.setString(
                            3,
                            parentField.getText().trim()
                    );

                    statement.setString(
                            4,
                            parentMobileField.getText().trim()
                    );

                    statement.setString(
                            5,
                            departmentField.getText().trim()
                    );

                    statement.setInt(
                            6,
                            studyYear
                    );

                    statement.setDouble(
                            7,
                            cgpa
                    );

                    statement.setInt(
                            8,
                            backlogs
                    );

                    statement.setString(
                            9,
                            studentIdHolder[0]
                    );

                    statement.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                        frame,
                        "Profile updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadProfile.run();

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Study Year, CGPA and Backlogs must contain valid numbers.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Error updating profile:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // ADD SKILL
        // ==========================================

        addSkillButton.addActionListener(e -> {

            String skill =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter skill:"
                    );

            if (skill == null ||
                    skill.trim().isEmpty()) {
                return;
            }

            String sql = """
                    INSERT INTO student_skills
                    (student_id, skill_name)
                    VALUES (?, ?)
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        studentIdHolder[0]
                );

                statement.setString(
                        2,
                        skill.trim()
                );

                statement.executeUpdate();

                loadProfile.run();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Error adding skill:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // DELETE SKILL
        // ==========================================

        deleteSkillButton.addActionListener(e -> {

            String skill =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter the exact skill name to delete:"
                    );

            if (skill == null ||
                    skill.trim().isEmpty()) {
                return;
            }

            String sql = """
                    DELETE FROM student_skills
                    WHERE student_id = ?
                    AND skill_name = ?
                    LIMIT 1
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        studentIdHolder[0]
                );

                statement.setString(
                        2,
                        skill.trim()
                );

                int rows =
                        statement.executeUpdate();

                if (rows == 0) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Skill not found."
                    );

                } else {

                    loadProfile.run();
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Error deleting skill:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // ADD ACHIEVEMENT
        // ==========================================

        addAchievementButton.addActionListener(e -> {

            JTextField achievementField =
                    new JTextField();

            JTextField descriptionField =
                    new JTextField();

            JPanel panel =
                    new JPanel(
                            new GridLayout(
                                    0,
                                    2,
                                    10,
                                    10
                            )
                    );

            panel.add(
                    new JLabel(
                            "Achievement:"
                    )
            );

            panel.add(
                    achievementField
            );

            panel.add(
                    new JLabel(
                            "Description:"
                    )
            );

            panel.add(
                    descriptionField
            );

            int result =
                    JOptionPane.showConfirmDialog(
                            frame,
                            panel,
                            "Add Achievement",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            if (achievementField
                    .getText()
                    .trim()
                    .isEmpty()) {

                return;
            }

            String sql = """
                    INSERT INTO achievements
                    (
                        student_id,
                        achievement_name,
                        description
                    )
                    VALUES (?, ?, ?)
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        studentIdHolder[0]
                );

                statement.setString(
                        2,
                        achievementField
                                .getText()
                                .trim()
                );

                statement.setString(
                        3,
                        descriptionField
                                .getText()
                                .trim()
                );

                statement.executeUpdate();

                loadProfile.run();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Error adding achievement:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // DELETE ACHIEVEMENT
        // ==========================================

        deleteAchievementButton.addActionListener(e -> {

            String achievement =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter the exact achievement name to delete:"
                    );

            if (achievement == null ||
                    achievement.trim().isEmpty()) {
                return;
            }

            String sql = """
                    DELETE FROM achievements
                    WHERE student_id = ?
                    AND achievement_name = ?
                    LIMIT 1
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        studentIdHolder[0]
                );

                statement.setString(
                        2,
                        achievement.trim()
                );

                int rows =
                        statement.executeUpdate();

                if (rows == 0) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Achievement not found."
                    );

                } else {

                    loadProfile.run();
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Error deleting achievement:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // ADD CERTIFICATE
        // ==========================================

        addCertificateButton.addActionListener(e -> {

            JTextField certificateField =
                    new JTextField();

            JTextField urlField =
                    new JTextField();

            JPanel panel =
                    new JPanel(
                            new GridLayout(
                                    0,
                                    2,
                                    10,
                                    10
                            )
                    );

            panel.add(
                    new JLabel(
                            "Certificate Name:"
                    )
            );

            panel.add(
                    certificateField
            );

            panel.add(
                    new JLabel(
                            "Certificate URL:"
                    )
            );

            panel.add(
                    urlField
            );

            int result =
                    JOptionPane.showConfirmDialog(
                            frame,
                            panel,
                            "Add Certificate",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            if (certificateField
                    .getText()
                    .trim()
                    .isEmpty()) {

                return;
            }

            String sql = """
                    INSERT INTO certificates
                    (
                        student_id,
                        certificate_name,
                        certificate_url
                    )
                    VALUES (?, ?, ?)
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        studentIdHolder[0]
                );

                statement.setString(
                        2,
                        certificateField
                                .getText()
                                .trim()
                );

                statement.setString(
                        3,
                        urlField.getText().trim()
                );

                statement.executeUpdate();

                loadProfile.run();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Error adding certificate:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // DELETE CERTIFICATE
        // ==========================================

        deleteCertificateButton.addActionListener(e -> {

            String certificate =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter the exact certificate name to delete:"
                    );

            if (certificate == null ||
                    certificate.trim().isEmpty()) {
                return;
            }

            String sql = """
                    DELETE FROM certificates
                    WHERE student_id = ?
                    AND certificate_name = ?
                    LIMIT 1
                    """;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        studentIdHolder[0]
                );

                statement.setString(
                        2,
                        certificate.trim()
                );

                int rows =
                        statement.executeUpdate();

                if (rows == 0) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Certificate not found."
                    );

                } else {

                    loadProfile.run();
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Error deleting certificate:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // UPLOAD RESUME
        // ==========================================

        uploadResumeButton.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            chooser.setDialogTitle(
                    "Select Resume"
            );

            int result =
                    chooser.showOpenDialog(
                            frame
                    );

            if (result !=
                    JFileChooser.APPROVE_OPTION) {
                return;
            }

            File selectedFile =
                    chooser.getSelectedFile();

            try {

                Path uploadDirectory =
                        Path.of(
                                "uploads",
                                "resumes"
                        );

                Files.createDirectories(
                        uploadDirectory
                );

                String fileName =
                        studentIdHolder[0]
                                + "_resume_"
                                + selectedFile.getName();

                Path destination =
                        uploadDirectory.resolve(
                                fileName
                        );

                Files.copy(
                        selectedFile.toPath(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );

                String relativePath =
                        destination
                                .toString();

                String sql = """
                        UPDATE students
                        SET resume_path = ?
                        WHERE student_id = ?
                        """;

                try (
                        Connection connection =
                                DatabaseConnection.getConnection();

                        PreparedStatement statement =
                                connection.prepareStatement(sql)
                ) {

                    statement.setString(
                            1,
                            relativePath
                    );

                    statement.setString(
                            2,
                            studentIdHolder[0]
                    );

                    statement.executeUpdate();
                }

                resumePathHolder[0] =
                        relativePath;

                JOptionPane.showMessageDialog(
                        frame,
                        "Resume uploaded successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Error uploading resume:\n"
                                + ex.getMessage(),
                        "Upload Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // OPEN RESUME
        // ==========================================

        openResumeButton.addActionListener(e -> {

            String resumePath =
                    resumePathHolder[0];

            if (resumePath == null ||
                    resumePath.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
        frame,
        "No resume uploaded yet.",
        "Resume",
        JOptionPane.INFORMATION_MESSAGE
);

                return;
            }

            try {

                File file =
                        new File(resumePath);

                if (!file.exists()) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Resume file not found:\n"
                                    + file.getAbsolutePath(),
                            "Resume Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                Desktop.getDesktop().open(
                        file
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Unable to open resume:\n"
                                + ex.getMessage(),
                        "Resume Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // CLOSE
        // ==========================================

        closeButton.addActionListener(
                e -> frame.dispose()
        );

        // ==========================================
        // ADD EVERYTHING
        // ==========================================

        contentPanel.add(detailsPanel);

        contentPanel.add(
                Box.createVerticalStrut(15)
        );

        contentPanel.add(middlePanel);

        contentPanel.add(
                Box.createVerticalStrut(15)
        );

        contentPanel.add(certificatesPanel);

        contentPanel.add(
                Box.createVerticalStrut(15)
        );

        contentPanel.add(resumePanel);

        mainPanel.add(
                new JScrollPane(contentPanel),
                BorderLayout.CENTER
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        frame.add(mainPanel);

        // First load
        loadProfile.run();

        frame.setVisible(true);
    }

    // ==========================================
    // LOAD SKILLS
    // ==========================================

    private static void loadSkills(
            Connection connection,
            String studentId,
            JTextArea area
    ) throws SQLException {

        String sql = """
                SELECT skill_name
                FROM student_skills
                WHERE student_id = ?
                ORDER BY skill_id
                """;

        StringBuilder builder =
                new StringBuilder();

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    studentId
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                while (result.next()) {

                    builder.append("• ")
                            .append(
                                    result.getString(
                                            "skill_name"
                                    )
                            )
                            .append("\n");
                }
            }
        }

        area.setText(
                builder.toString()
        );
    }

    // ==========================================
    // LOAD ACHIEVEMENTS
    // ==========================================

    private static void loadAchievements(
            Connection connection,
            String studentId,
            JTextArea area
    ) throws SQLException {

        String sql = """
                SELECT achievement_name, description
                FROM achievements
                WHERE student_id = ?
                ORDER BY achievement_id
                """;

        StringBuilder builder =
                new StringBuilder();

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    studentId
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                while (result.next()) {

                    builder.append("• ")
                            .append(
                                    result.getString(
                                            "achievement_name"
                                    )
                            );

                    String description =
                            result.getString(
                                    "description"
                            );

                    if (description != null &&
                            !description.isEmpty()) {

                        builder.append(
                                " - "
                        ).append(
                                description
                        );
                    }

                    builder.append("\n");
                }
            }
        }

        area.setText(
                builder.toString()
        );
    }

    // ==========================================
    // LOAD CERTIFICATES
    // ==========================================

    private static void loadCertificates(
            Connection connection,
            String studentId,
            JTextArea area
    ) throws SQLException {

        String sql = """
                SELECT certificate_name, certificate_url
                FROM certificates
                WHERE student_id = ?
                ORDER BY certificate_id
                """;

        StringBuilder builder =
                new StringBuilder();

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    studentId
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                while (result.next()) {

                    builder.append("• ")
                            .append(
                                    result.getString(
                                            "certificate_name"
                                    )
                            );

                    String url =
                            result.getString(
                                    "certificate_url"
                            );

                    if (url != null &&
                            !url.isEmpty()) {

                        builder.append(
                                " - "
                        ).append(url);
                    }

                    builder.append("\n");
                }
            }
        }

        area.setText(
                builder.toString()
        );
    }

    // ==========================================
    // SAFE STRING
    // ==========================================

    private static String safe(
            String value
    ) {

        if (value == null) {
            return "-";
        }

        return value;
    }
}