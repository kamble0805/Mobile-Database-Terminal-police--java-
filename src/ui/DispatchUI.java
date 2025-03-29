package ui;

import database.ClockingDAO;
import database.CallDAO;
// Import BulletinDAO for bulletin board
import models.ClockingRecord;
import models.Call;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class DispatchUI extends JFrame {

    private JPanel officerStatusPanel;
    private JPanel recentCallsPanel;
    private JPanel bulletinPanel; // Panel for bulletin board
    private JButton logoutButton;
    private JButton dashboardButton;
    private Timer refreshTimer;

    public DispatchUI() {
        setTitle("Dispatch Center");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // LEFT PANEL - Officer Status
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Officer Status"));

        officerStatusPanel = new JPanel();
        officerStatusPanel.setLayout(new BoxLayout(officerStatusPanel, BoxLayout.Y_AXIS));
        JScrollPane officerScrollPane = new JScrollPane(officerStatusPanel);
        officerScrollPane.setPreferredSize(new Dimension(200, 0));

        leftPanel.add(officerScrollPane, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // RIGHT PANEL - Dispatch Calls
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Dispatch Calls"));

        recentCallsPanel = new JPanel();
        recentCallsPanel.setLayout(new BoxLayout(recentCallsPanel, BoxLayout.Y_AXIS));
        JScrollPane callScrollPane = new JScrollPane(recentCallsPanel);
        callScrollPane.setPreferredSize(new Dimension(320, 0));

        JButton dispatchCallButton = new JButton("Dispatch Call");
        rightPanel.add(callScrollPane, BorderLayout.CENTER);
        rightPanel.add(dispatchCallButton, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // CENTER PANEL - Bulletin Board
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Bulletin Board"));

        bulletinPanel = new JPanel();
        bulletinPanel.setLayout(new BoxLayout(bulletinPanel, BoxLayout.Y_AXIS));
        JScrollPane bulletinScrollPane = new JScrollPane(bulletinPanel);
        
        

        centerPanel.add(bulletinScrollPane, BorderLayout.CENTER);
      

        // BUTTONS PANEL - Logout & Dashboard
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButton = new JButton("Logout");
        dashboardButton = new JButton("Back to Dashboard");

        buttonPanel.add(dashboardButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Event Handlers
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
            new LoginUI();
            dispose();
        });

        dashboardButton.addActionListener(e -> openDashboard());

        // Load Data
        loadOfficerStatus();
        loadRecentCalls();
       // Load bulletins when UI starts

        // Auto-refresh timers
        refreshTimer = new Timer(5000, e -> {
            loadRecentCalls();
           
        });
        refreshTimer.start();

        setVisible(true);
    }

    private void loadOfficerStatus() {
        List<ClockingRecord> officers = ClockingDAO.getClockedInOfficers();
        officerStatusPanel.removeAll();
        for (ClockingRecord officer : officers) {
            JPanel officerPanel = new JPanel();
            officerPanel.setLayout(new BoxLayout(officerPanel, BoxLayout.Y_AXIS));
            officerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel officerLabel = new JLabel("Name: " + officer.getName());
            JLabel statusLabel = new JLabel("Status: " + officer.getOfficerStatus());
            JButton statusButton = new JButton("Update Status");

            statusButton.addActionListener(e -> {
                String newStatus = (String) JOptionPane.showInputDialog(
                        this, "Select new status for " + officer.getName(), "Change Status",
                        JOptionPane.PLAIN_MESSAGE, null,
                        new String[]{"Available", "Unavailable", "Responding to Call", "Break/Report Writing"},
                        officer.getOfficerStatus());

                if (newStatus != null) {
                    ClockingDAO.updateOfficerStatus(officer.getName(), newStatus);
                    loadOfficerStatus();
                }
            });

            officerPanel.add(officerLabel);
            officerPanel.add(statusLabel);
            officerPanel.add(statusButton);
            officerStatusPanel.add(officerPanel);
        }
        officerStatusPanel.revalidate();
        officerStatusPanel.repaint();
    }

    private void loadRecentCalls() {
        List<Call> recentCalls = CallDAO.loadRecentCalls();
        recentCallsPanel.removeAll();
        for (Call call : recentCalls) {
            recentCallsPanel.add(createCallCard(call), 0);
        }
        recentCallsPanel.revalidate();
        recentCallsPanel.repaint();
    }

    private JPanel createCallCard(Call call) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cardPanel.setBackground(Color.LIGHT_GRAY);
        cardPanel.setPreferredSize(new Dimension(300, 100));

        JLabel callerIdLabel = new JLabel("Caller ID: " + call.getCallerCid());
        JLabel callerNameLabel = new JLabel("Caller Name: " + call.getCallerName());
        JLabel detailsLabel = new JLabel("Details: " + call.getCallDetails());
        JLabel timestampLabel = new JLabel("Timestamp: " + call.getTimestamp());

        cardPanel.add(callerIdLabel);
        cardPanel.add(callerNameLabel);
        cardPanel.add(detailsLabel);
        cardPanel.add(timestampLabel);

        return cardPanel;
    }

    
   

    private void openDashboard() {
        new DashboardUI("Officer");
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DispatchUI::new);
    }
}
