import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FacultyDashboard {

    private static JFrame frame;
    private static JPanel contentPanel;
    private static CardLayout cardLayout;

    // ==============================
    // MAIN FACULTY DASHBOARD
    // ==============================

public static void showDashboard() {

        frame = new JFrame("Campus Placement & Internship Management System");
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ==============================
        // HEADER
        // ==============================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(35, 55, 90));
        header.setPreferredSize(new Dimension(1100, 75));

        JLabel title = new JLabel(
                "  Campus Placement & Internship Management System  "
        );

        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel facultyLabel = new JLabel(
                "Faculty Portal  "
        );

        facultyLabel.setForeground(Color.WHITE);
        facultyLabel.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        header.add(title, BorderLayout.WEST);
        header.add(facultyLabel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // ==============================
        // SIDEBAR
        // ==============================

        JPanel sidebar = new JPanel();

        sidebar.setLayout(
                new GridLayout(11, 1, 0, 8)
        );

        sidebar.setPreferredSize(
                new Dimension(210, 0)
        );

        sidebar.setBackground(
                new Color(25, 38, 65)
        );

        JButton dashboardButton =
                createMenuButton("Dashboard");

        JButton studentsButton =
                createMenuButton("Students");

        JButton companiesButton =
                createMenuButton("Company Drives");

        JButton internshipsButton =
                createMenuButton("Internships");

        JButton internshipApplicationsButton =
                createMenuButton("Internship Applications");        

        JButton applicationsButton =
                createMenuButton("Applications");

        JButton shortlistedButton =
                createMenuButton("Shortlisted Students");

        JButton selectedButton =
                createMenuButton("Selected Students");

        JButton interviewsButton =
                createMenuButton("Interview Management");

        JButton reportsButton =
                createMenuButton("Placement Reports");

        JButton logoutButton =
                createMenuButton("Logout");

        sidebar.add(dashboardButton);
        sidebar.add(studentsButton);
        sidebar.add(companiesButton);
        sidebar.add(internshipsButton);
        sidebar.add(internshipApplicationsButton);
        sidebar.add(applicationsButton);
        sidebar.add(shortlistedButton);
        sidebar.add(selectedButton);
        sidebar.add(interviewsButton);
        sidebar.add(reportsButton);
        sidebar.add(logoutButton);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // ==============================
        // CONTENT
        // ==============================

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // ==============================
        // DASHBOARD
        // ==============================

        JPanel dashboardPanel =
                createDashboardHome();

        contentPanel.add(
                dashboardPanel,
                "dashboard"
        );

        // ==============================
        // STUDENTS
        // ==============================

        JPanel studentsPanel =
                FacultyStudents.createStudentsPanel();

        contentPanel.add(
                studentsPanel,
                "students"
        );

        // ==============================
        // COMPANY DRIVES
        // ==============================

        JPanel companiesPanel =
                FacultyCompanies.createCompaniesPanel();

        contentPanel.add(
                companiesPanel,
                "companies"
        );

        // ==============================
        // INTERNSHIPS
        // ==============================

        JPanel internshipsPanel =
                FacultyInternships.createInternshipsPanel();

        contentPanel.add(
                internshipsPanel,
                "internships"
        );
        JPanel internshipApplicationsPanel =
        FacultyInternshipApplications.createApplicationsPanel();

contentPanel.add(
        internshipApplicationsPanel,
        "internshipApplications"
);

        // ==============================
        // APPLICATIONS
        // ==============================

        JPanel applicationsPanel =
                FacultyApplications.createApplicationsPanel();

        contentPanel.add(
                applicationsPanel,
                "applications"
        );

        // ==============================
        // SHORTLISTED STUDENTS
        // ==============================

        JPanel shortlistedPanel =
                FacultyShortlisted.createShortlistedPanel();

        contentPanel.add(
                shortlistedPanel,
                "shortlisted"
        );

        // ==============================
        // SELECTED STUDENTS
        // ==============================

        JPanel selectedPanel =
                FacultySelected.createSelectedPanel();

        contentPanel.add(
                selectedPanel,
                "selected"
        );

        // ==============================
        // INTERVIEW MANAGEMENT
        // ==============================

        JPanel interviewsPanel =
                FacultyInterviews.createInterviewsPanel();

        contentPanel.add(
                interviewsPanel,
                "interviews"
        );

        // ==============================
        // PLACEMENT REPORTS
        // ==============================

        JPanel reportsPanel =
                FacultyPlacementReports.createReportsPanel();

        contentPanel.add(
                reportsPanel,
                "reports"
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        // ==============================
        // BUTTON ACTIONS
        // ==============================

        // Dashboard
        dashboardButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "dashboard"
                )
        );

        // Students
        studentsButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "students"
                )
        );

        // Company Drives
        companiesButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "companies"
                )
        );

        // Internships
        internshipsButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "internships"
                )
        );
        internshipApplicationsButton.addActionListener(e ->
        cardLayout.show(
                contentPanel,
                "internshipApplications"
        )
);

        // Applications
        applicationsButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "applications"
                )
        );

        // Shortlisted Students
        shortlistedButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "shortlisted"
                )
        );

        // Selected Students
        selectedButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "selected"
                )
        );

        // Interview Management
        interviewsButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "interviews"
                )
        );

        // Placement Reports
        reportsButton.addActionListener(e ->
                cardLayout.show(
                        contentPanel,
                        "reports"
                )
        );

        // Logout
        logoutButton.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                frame.dispose();

                App.main(new String[]{});
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // ==============================
    // CREATE MENU BUTTON
    // ==============================

    private static JButton createMenuButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(45, 65, 100)
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        return button;
    }

    // ==============================
    // DASHBOARD HOME
    // ==============================

    private static JPanel createDashboardHome() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(20, 20)
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );

        // ==============================
        // WELCOME
        // ==============================

        JPanel welcomePanel =
                new JPanel();

        welcomePanel.setLayout(
                new BoxLayout(
                        welcomePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel welcome =
                new JLabel(
                        "Welcome to Faculty Dashboard"
                );

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Manage students, recruitment drives and placement activities."
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        welcomePanel.add(welcome);

        welcomePanel.add(
                Box.createVerticalStrut(8)
        );

        welcomePanel.add(subtitle);

        panel.add(
                welcomePanel,
                BorderLayout.NORTH
        );

        // ==============================
        // STATISTICS
        // ==============================

        JPanel statsPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );

        int totalStudents =
                getCount(
                        "SELECT COUNT(*) FROM students"
                );

        int studentsWithBacklogs =
                getCount(
                        """
                        SELECT COUNT(*)
                        FROM students
                        WHERE active_backlogs > 0
                        """
                );

        int companies =
                getCount(
                        "SELECT COUNT(*) FROM companies"
                );

        int selectedStudents =
                getCount(
                        """
                        SELECT COUNT(DISTINCT student_id)
                        FROM selections
                        """
                );

        statsPanel.add(
                createStatCard(
                        "TOTAL STUDENTS",
                        String.valueOf(totalStudents),
                        new Color(52, 152, 219)
                )
        );

        statsPanel.add(
                createStatCard(
                        "STUDENTS WITH BACKLOGS",
                        String.valueOf(studentsWithBacklogs),
                        new Color(231, 76, 60)
                )
        );

        statsPanel.add(
                createStatCard(
                        "COMPANY DRIVES",
                        String.valueOf(companies),
                        new Color(46, 204, 113)
                )
        );

        statsPanel.add(
                createStatCard(
                        "SELECTED STUDENTS",
                        String.valueOf(selectedStudents),
                        new Color(155, 89, 182)
                )
        );

        panel.add(
                statsPanel,
                BorderLayout.CENTER
        );

        // ==============================
        // QUICK MESSAGE
        // ==============================

        JLabel bottomLabel =
                new JLabel(
                        "Use the menu on the left to manage the placement process.",
                        SwingConstants.CENTER
                );

        bottomLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        panel.add(
                bottomLabel,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // ==============================
    // STAT CARD
    // ==============================

    private static JPanel createStatCard(
            String title,
            String value,
            Color color
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                color,
                                2
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        JLabel valueLabel =
                new JLabel(
                        value,
                        SwingConstants.CENTER
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        38
                )
        );

        valueLabel.setForeground(color);

        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );

        return card;
    }

    // ==============================
    // DATABASE COUNT
    // ==============================

    private static int getCount(
            String sql
    ) {

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {

            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return 0;
    }
}