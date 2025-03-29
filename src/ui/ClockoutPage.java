package ui;

import database.ClockingDAO; // Import the ClockingDAO class
import models.ClockingRecord; // Import the ClockingRecord model
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClockoutPage extends JFrame {
    private JTextField cidField;
    private JTextField nameField;
    private JButton clockOutButton;
    private JButton backButton; // Button to go back to the dashboard
    private JButton logoutButton; // Button to logout

    public ClockoutPage() {
        setTitle("Clock Out");
        setSize(250, 150); // Reduced size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2)); // 5 rows, 2 columns

        // CID Input
        JLabel cidLabel = new JLabel("CID:");
        cidField = new JTextField();
        add(cidLabel);
        add(cidField);

        // Name Input
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        add(nameLabel);
        add(nameField);

        // Clock Out Button
        clockOutButton = new JButton("Clock Out");
        add(clockOutButton);

        // Back to Dashboard Button
        backButton = new JButton("Back to Dashboard");
        add(backButton);

        // Logout Button
        logoutButton = new JButton("Logout");
        add(logoutButton);

        // Action Listener for Clock Out Button
        clockOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clockOut();
            }
        });

        // Action Listener for Back to Dashboard Button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDashboard(); // Call the method to open the dashboard
            }
        });

        // Action Listener for Logout Button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to logout the user
                JOptionPane.showMessageDialog(ClockoutPage.this, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
                new LoginUI(); // Assuming you have a LoginPage class
                dispose(); // Close the current window
            }
        });

        setVisible(true);
    }

    private void clockOut() {
        String cid = cidField.getText().trim();
        String name = nameField.getText().trim();

        // Input validation
        if (cid.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter both CID and Name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if validation fails
        }

        // Check if the user is clocked in
        ClockingRecord record = ClockingDAO.getClockingRecord(cid, name);
        if (record != null) {
            // User is clocked in, proceed to clock out
            if (ClockingDAO.clockOut(cid)) {
                JOptionPane.showMessageDialog(this, "✅ Successfully clocked out!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Failed to clock out. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // User is not clocked in
            JOptionPane.showMessageDialog(this, "⚠️ User is not clocked in or invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDashboard() {
        new DashboardUI("Officer"); // Assuming "Officer" is a role or identifier
        dispose(); // Close the current window
    }

    public static void main(String[] args) {
        new ClockoutPage();
    }
}