package ui;

import database.ChargeDAO;
import models.Charge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChargesDisplayUI extends JFrame {

    private JPanel misdemeanorPanel, felonyPanel, infractionPanel;

    public ChargesDisplayUI() {
        setTitle("Charge Display");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel with Dashboard and Logout buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton dashboardButton = new JButton("Dashboard");
        JButton logoutButton = new JButton("Logout");
        
        dashboardButton.addActionListener(e -> openDashboard());
        logoutButton.addActionListener(e -> logout());
        
        topPanel.add(dashboardButton);
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        // Main container with GridLayout for 3 equal columns
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Create panels for each charge severity
        misdemeanorPanel = createSectionPanel("Misdemeanors");
        felonyPanel = createSectionPanel("Felonies");
        infractionPanel = createSectionPanel("Infractions");

        // Wrap each panel in its own JScrollPane for independent scrolling
        JScrollPane misdemeanorScroll = new JScrollPane(misdemeanorPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane felonyScroll = new JScrollPane(felonyPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane infractionScroll = new JScrollPane(infractionPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Set preferred sizes for scroll panes
        misdemeanorScroll.setPreferredSize(new Dimension(260, 600));
        felonyScroll.setPreferredSize(new Dimension(260, 600));
        infractionScroll.setPreferredSize(new Dimension(260, 600));

        // Add scrollable panels to the main layout
        mainPanel.add(misdemeanorScroll);
        mainPanel.add(felonyScroll);
        mainPanel.add(infractionScroll);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Load charges from database
        loadCharges();
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title label
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(headerLabel);
        return panel;
    }

    private void loadCharges() {
    List<Charge> charges = ChargeDAO.getAllCharges();
    
    System.out.println("Charges loaded: " + charges.size()); // Debugging

    for (Charge charge : charges) {
        JPanel chargeCard = createChargeCard(charge);
        switch (charge.getSeverity()) {
            case "Misdemeanor":
                misdemeanorPanel.add(chargeCard);
                break;
            case "Felony":
                felonyPanel.add(chargeCard);
                break;
            case "Infraction":
                infractionPanel.add(chargeCard);
                break;
        }
    }

    refreshPanel(misdemeanorPanel);
    refreshPanel(felonyPanel);
    refreshPanel(infractionPanel);
}


    private void refreshPanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }

    private JPanel createChargeCard(Charge charge) {
        JPanel card = new JPanel();
        card.setLayout(new GridLayout(2, 1));
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        card.setPreferredSize(new Dimension(240, 60)); // Compact layout

        JLabel titleLabel = new JLabel("<html><small>" + charge.getPenalCode() + ": " + charge.getDescription() + "</small></html>");
        JLabel notesLabel = new JLabel("<html><small>Notes: " + (charge.getNotes() != null ? charge.getNotes() : "None") + "</small></html>");

        card.add(titleLabel);
        card.add(notesLabel);

        return card;
    }

    private void openDashboard() {
        new DashboardUI("Officer");
        dispose();
    }

    // ✅ Logout
    private void logout() {
        dispose();
        new LoginUI();
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChargesDisplayUI ui = new ChargesDisplayUI();
            ui.setVisible(true);
        });
    }
}