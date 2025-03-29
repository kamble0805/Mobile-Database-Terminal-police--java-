package ui;

import database.DatabaseManager;
import models.Vehicle;
import database.VehicleDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DashboardUI extends JFrame {
    private DefaultTableModel warrantModel;
    private JTable warrantsTable;

    public DashboardUI(String officerName) {
        setTitle("Police MDT - Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar Panel (Left)
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setLayout(new GridBagLayout());

        // Sidebar Buttons
        JButton nameButton = new JButton("Name Lookup");
        JButton reportsButton = new JButton("Reports");
        JButton vehicleLookupButton = new JButton("Vehicle Lookup");
        JButton caseManagementButton = new JButton("Case Management");
        JButton chargesDisplayButton = new JButton("Charges Display");
        JButton warrantButton = new JButton("Warrant");
        JButton clockOutButton = new JButton("Clock Out"); // New Clock Out Button
        JButton dispatchButton = new JButton("Dispatch"); // New Dispatch Button

        // Action Listeners for Sidebar Buttons
        nameButton.addActionListener(e -> {
            dispose();
            new NameLookupUI();
        });

        reportsButton.addActionListener(e -> {
            dispose();
            new ReportPageUI();
        });

        vehicleLookupButton.addActionListener(e -> {
            dispose();
            new VehicleLookupUI();
        });

        caseManagementButton.addActionListener(e -> {
            dispose();
            new CaseUI();
        });

        chargesDisplayButton.addActionListener(e -> {
            ChargesDisplayUI chargesUI = new ChargesDisplayUI();
            chargesUI.setVisible(true);
        });

        warrantButton.addActionListener(e -> {
            dispose();
            new WarrantSubmissionUI();
        });

        // Action Listener for Clock Out Button
        clockOutButton.addActionListener(e -> {
            dispose();
            new ClockoutPage(); // Open ClockoutPage
        });

        // Action Listener for Dispatch Button
        dispatchButton.addActionListener(e -> {
            dispose();
            new DispatchUI(); // Open DispatchUI
        });

        // Sidebar Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        sidebarPanel.add(nameButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(vehicleLookupButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(reportsButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(caseManagementButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(chargesDisplayButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(warrantButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(clockOutButton, gbc); // Add Clock Out Button
        gbc.gridy++;
        sidebarPanel.add(dispatchButton, gbc); // Add Dispatch Button

        // Main Content Panel (Right)
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Header Label
        JLabel headerLabel = new JLabel("Welcome Officer " + officerName, SwingConstants.CENTER);
        contentPanel.add(headerLabel, BorderLayout.NORTH);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setContinuousLayout(true);

        // High Alert Vehicles Section
        JPanel highAlertPanel = new JPanel();
        highAlertPanel.setLayout(new BorderLayout());
        highAlertPanel.setBorder(BorderFactory.createTitledBorder("High Alert Vehicles"));

        String[] vehicleColumnNames = {"Plate", "Owner"};
        DefaultTableModel vehicleModel = new DefaultTableModel(vehicleColumnNames, 0);
        JTable highAlertTable = new JTable(vehicleModel);
        JScrollPane vehicleScrollPane = new JScrollPane(highAlertTable);
        highAlertPanel.add(vehicleScrollPane, BorderLayout.CENTER);

        loadHighAlertVehicles(vehicleModel);

        // Warrants Section
        JPanel warrantsPanel = new JPanel(new BorderLayout());
        warrantsPanel.setBorder(BorderFactory.createTitledBorder("Active Warrants"));

        String[] warrantColumnNames = {"ID", "Civilian", "Charges", "Officer", "Date Issued"};
        warrantModel = new DefaultTableModel(warrantColumnNames, 0);
        warrantsTable = new JTable(warrantModel);
        JScrollPane warrantsScrollPane = new JScrollPane(warrantsTable);
        warrantsPanel.add(warrantsScrollPane, BorderLayout.CENTER);

        // Button Panel for Warrants
        JPanel buttonPanel = new JPanel();
        JButton markServedButton = new JButton("Mark as Served");
       
        buttonPanel.add(markServedButton);
        
        warrantsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load active warrants
        loadActiveWarrants();

        // Button Action: Mark Selected Warrant as Served
        markServedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markWarrantAsServed();
            }
        });

        // Add panels to split pane
        splitPane.setLeftComponent(highAlertPanel);
        splitPane.setRightComponent(warrantsPanel);

        // Add split pane to content panel
        contentPanel.add(splitPane, BorderLayout.CENTER);

        // Add Panels to Frame
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // ✅ Fetch and Load High Alert Vehicles into Table
    private void loadHighAlertVehicles(DefaultTableModel model) {
        List<Vehicle> highAlertVehicles = VehicleDAO.getHighAlertVehicles();

        for (Vehicle vehicle : highAlertVehicles) {
            Object[] row = {
                vehicle.getPlate(),
                vehicle.getName()
            };
            model.addRow(row);
        }
    }

    // ✅ Fetch and Load Active Warrants
    private void loadActiveWarrants() {
        warrantModel.setRowCount(0); // Clear existing data

        String sql = "SELECT id, civilian_name, charges, officer_name, date_issued FROM mdt_warrants WHERE status = 'Active'";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt ("id"),
                    rs.getString("civilian_name"),
                    rs.getString("charges"),
                    rs.getString("officer_name"),
                    rs.getTimestamp("date_issued")
                };
                warrantModel.addRow(row);
            }
        } catch (Exception e) {
            System.err.println("❌ Error loading active warrants: " + e.getMessage());
        }
    }

    // ✅ Update Warrant Status to "Served"
    private void markWarrantAsServed() {
        int selectedRow = warrantsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a warrant to mark as served.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int warrantId = (int) warrantModel.getValueAt(selectedRow, 0); // Get warrant ID

        String sql = "UPDATE mdt_warrants SET status = 'Served' WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, warrantId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "✅ Warrant marked as Served!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadActiveWarrants(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Failed to update warrant status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("❌ Error updating warrant status: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new DashboardUI("John Doe");
    }
}