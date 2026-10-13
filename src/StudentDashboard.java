import javax.swing.*;
import java.awt.*;

public class StudentDashboard {

    private static JFrame frame;
    private static JPanel contentPanel;
    private static CardLayout cardLayout;

    // ==========================================
    // SHOW STUDENT DASHBOARD
    // ==========================================

    public static void showDashboard(String email) {

        frame = new JFrame(
                "Campus Placement Management System"
        );

        frame.setSize(1100, 700);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        // ==========================================
        // HEADER
        // ==========================================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(
                new Color(35, 55, 90)
        );

        header.setPreferredSize(
                new Dimension(1100, 75)
        );

       JLabel title =
        new JLabel(
                "Campus Placement & Internship Management System"
        );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel studentLabel =
                new JLabel(
                        "Student Portal   "
                );

        studentLabel.setForeground(
                Color.WHITE
        );

        studentLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        header.add(
                title,
                BorderLayout.WEST
        );

        header.add(
                studentLabel,
                BorderLayout.EAST
        );

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // ==========================================
        // SIDEBAR
        // ==========================================

        JPanel sidebar =
                new JPanel();

        sidebar.setLayout(
                new GridLayout(
                        7,
                        1,
                        0,
                        8
                )
        );

        sidebar.setPreferredSize(
                new Dimension(210, 0)
        );

        sidebar.setBackground(
                new Color(25, 38, 65)
        );

        JButton dashboardButton =
                createMenuButton(
                        "Dashboard"
                );

        JButton profileButton =
                createMenuButton(
                        "My Profile"
                );

        JButton drivesButton = createMenuButton(
        "<html><center>Company Drives &amp;<br>Internships</center></html>"
);       

        JButton applicationsButton =
                createMenuButton(
                        "My Applications"
                );

        JButton statusButton =
                createMenuButton(
                        "Placement Status"
                );

        JButton interviewsButton =
                createMenuButton(
                        "My Interviews"
                );

        JButton logoutButton =
                createMenuButton(
                        "Logout"
                );

        sidebar.add(
                dashboardButton
        );

        sidebar.add(
                profileButton
        );

        sidebar.add(drivesButton);

        sidebar.add(
                applicationsButton
        );

        sidebar.add(
                statusButton
        );

        sidebar.add(
                interviewsButton
        );

        sidebar.add(
                logoutButton
        );

        mainPanel.add(
                sidebar,
                BorderLayout.WEST
        );

        // ==========================================
        // CONTENT AREA
        // ==========================================

        cardLayout =
                new CardLayout();

        contentPanel =
                new JPanel(cardLayout);

        // ==========================================
        // DASHBOARD
        // ==========================================

        JPanel dashboardPanel =
                createDashboardHome(email);

        contentPanel.add(
                dashboardPanel,
                "dashboard"
        );

        
        // ==========================================
        // COMPANY DRIVES AND INTERNSHIPS
        // ==========================================

        JPanel drivesPanel =
                StudentCompanyDrives.createCompanyDrivesPanel(email);

        String studentId =
                email.substring(0, email.indexOf('@'));

        JPanel internshipsPanel =
                StudentInternships.createInternshipsPanel(studentId);

        // Both existing pages are placed inside one page using tabs.
        JTabbedPane opportunitiesTabs = new JTabbedPane();

        opportunitiesTabs.addTab(
                "Company Drives",
                drivesPanel
        );

        opportunitiesTabs.addTab(
                "Internships",
                internshipsPanel
        );

        JPanel opportunitiesPanel =
                new JPanel(new BorderLayout(10, 10));

        JLabel opportunitiesTitle = new JLabel(
                "COMPANY DRIVES AND INTERNSHIPS",
                SwingConstants.CENTER
        );

        opportunitiesTitle.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        opportunitiesTitle.setBorder(
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        );

        opportunitiesPanel.add(
                opportunitiesTitle,
                BorderLayout.NORTH
        );

        opportunitiesPanel.add(
                opportunitiesTabs,
                BorderLayout.CENTER
        );

        contentPanel.add(
                opportunitiesPanel,
                "opportunities"
        );

        // ==========================================
        // MY APPLICATIONS
        // ==========================================

        JPanel applicationsPanel =
                StudentApplications
                        .createApplicationsPanel(email);

        contentPanel.add(
                applicationsPanel,
                "applications"
        );

        // ==========================================
        // PLACEMENT STATUS
        // ==========================================

        JPanel placementStatusPanel =
                StudentPlacementStatus
                        .createPlacementStatusPanel(email);

        contentPanel.add(
                placementStatusPanel,
                "status"
        );

        // ==========================================
        // MY INTERVIEWS
        // ==========================================

        JPanel interviewsPanel =
                StudentInterviews
                        .createInterviewsPanel(email);

        contentPanel.add(
                interviewsPanel,
                "interviews"
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        // ==========================================
        // BUTTON ACTIONS
        // ==========================================

        // Dashboard
        dashboardButton.addActionListener(
                e ->
                        cardLayout.show(
                                contentPanel,
                                "dashboard"
                        )
        );

        // My Profile
        // StudentProfile.showProfile() opens the
        // profile window, so it is NOT added as JPanel.
        profileButton.addActionListener(
                e ->
                        StudentProfile.showProfile(email)
        );

        // Company Drives
        drivesButton.addActionListener(e ->
        cardLayout.show(
                contentPanel,
                "opportunities"
        )
);
        

        // My Applications
        applicationsButton.addActionListener(
                e ->
                        cardLayout.show(
                                contentPanel,
                                "applications"
                        )
        );

        // Placement Status
        statusButton.addActionListener(
                e ->
                        cardLayout.show(
                                contentPanel,
                                "status"
                        )
        );

        // My Interviews
        interviewsButton.addActionListener(
                e ->
                        cardLayout.show(
                                contentPanel,
                                "interviews"
                        )
        );

        // ==========================================
        // LOGOUT
        // ==========================================

        logoutButton.addActionListener(e -> {

            int choice =
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to logout?",
                            "Logout",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice ==
                    JOptionPane.YES_OPTION) {

                frame.dispose();

                App.main(
                        new String[]{}
                );
            }
        });

        frame.add(mainPanel);

        frame.setVisible(true);
    }

    // ==========================================
    // CREATE MENU BUTTON
    // ==========================================

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

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                new Color(45, 65, 100)
        );

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        return button;
    }

    // ==========================================
    // DASHBOARD HOME
    // ==========================================

    private static JPanel createDashboardHome(
            String email
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                20,
                                20
                        )
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );

        // ==========================================
        // WELCOME
        // ==========================================

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
                        "Welcome to Student Dashboard"
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
                        "View company drives, applications and your placement progress."
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        welcomePanel.add(
                welcome
        );

        welcomePanel.add(
                Box.createVerticalStrut(8)
        );

        welcomePanel.add(
                subtitle
        );

        panel.add(
                welcomePanel,
                BorderLayout.NORTH
        );

        // ==========================================
        // INFORMATION CARDS
        // ==========================================

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );

        cardsPanel.add(
                createInfoCard(
                        "MY PROFILE",
                        "View and manage your personal and academic details.",
                        new Color(52, 152, 219)
                )
        );

        cardsPanel.add(
                createInfoCard(
                        "COMPANY DRIVES",
                        "View companies and check your eligibility.",
                        new Color(46, 204, 113)
                )
        );

        cardsPanel.add(
                createInfoCard(
                        "MY APPLICATIONS",
                        "Track the companies you have applied to.",
                        new Color(241, 196, 15)
                )
        );

        cardsPanel.add(
                createInfoCard(
                        "PLACEMENT STATUS",
                        "Check whether your application is Applied, Shortlisted, Selected or Rejected.",
                        new Color(155, 89, 182)
                )
        );

        panel.add(
                cardsPanel,
                BorderLayout.CENTER
        );

        // ==========================================
        // BOTTOM MESSAGE
        // ==========================================

        JLabel bottomLabel =
                new JLabel(
                        "Use the menu on the left to manage your placement activities.",
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

    // ==========================================
    // INFORMATION CARD
    // ==========================================

    private static JPanel createInfoCard(
            String title,
            String description,
            Color color
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
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
                                20,
                                20,
                                20,
                                20
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
                        18
                )
        );

        titleLabel.setForeground(
                color
        );

        JLabel descriptionLabel =
                new JLabel(
                        "<html><div style='text-align:center;'>"
                                + description
                                + "</div></html>",
                        SwingConstants.CENTER
                );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        card.add(
                descriptionLabel,
                BorderLayout.CENTER
        );

        return card;
    }
}