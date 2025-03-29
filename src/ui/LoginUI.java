package ui;

import database.UserDAO;
import models.User;
import database.ClockingDAO; // Assuming you have a DAO for clocking

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JCheckBox clockInCheckBox; // Checkbox for clocking in
    private JComboBox<String> officerStatusComboBox; // Dropdown for officer status

    public LoginUI() {
        setTitle("Police MDT - Login");
        setSize(350, 250); // Increased size to accommodate dropdown
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5)); // 5 rows, 2 columns, 5px padding
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Labels and Fields
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(12); // Small text field
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(12); // Small text field
        formPanel.add(passwordField);

        // Clock In Checkbox
        clockInCheckBox = new JCheckBox("Clock In"); // New checkbox
        formPanel.add(clockInCheckBox);
        formPanel.add(new JLabel("")); // Empty label for layout alignment

        // Officer Status Dropdown
        formPanel.add(new JLabel("Officer Status:"));
        officerStatusComboBox = new JComboBox<>(new String[]{
            "Available",
            "Unavailable",
            "Responding to Call",
            "Break/Report Writing"
        });
        formPanel.add(officerStatusComboBox);

        // Buttons Panel (Keeps Buttons Close)
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add Panels to Frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Login Button Action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User authenticatedUser  = UserDAO.authenticateUser (username, password);
            if (authenticatedUser  != null) {
                JOptionPane.showMessageDialog(this, "Login Successful!");

                if ("police".equalsIgnoreCase(authenticatedUser .getJobType())) {
                    // If clock in checkbox is selected, log the clock in
                    if (clockInCheckBox.isSelected()) {
                        // Insert into clocking table with selected officer status
                        String officerStatus = (String) officerStatusComboBox.getSelectedItem();
                        boolean clockInSuccess = ClockingDAO.clockIn(authenticatedUser .getCid(), authenticatedUser .getName(), officerStatus, new Timestamp(System.currentTimeMillis()));
                        if (clockInSuccess) {
                            JOptionPane.showMessageDialog(this, "Clock In Successful!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to clock in.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    dispose(); // Close login window
                    new DashboardUI(authenticatedUser .getName()); // Open dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Access Denied! Only police officers can access the system.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register Button Action
        registerButton.addActionListener(e -> new RegisterUI());

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}