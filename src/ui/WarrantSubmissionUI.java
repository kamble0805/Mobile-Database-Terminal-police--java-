package ui;

import database.WarrantDAO;
import database.UserDAO;
import models.Warrant;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class WarrantSubmissionUI extends JFrame {
    private JComboBox<String> civilianComboBox, officerComboBox;
    private JTextArea chargesArea;
    private JComboBox<String> statusBox;
    private JButton submitButton;

    public WarrantSubmissionUI() {
        setTitle("New Warrant Submission");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Labels and input fields
        add(new JLabel("Civilian:"));
        civilianComboBox = new JComboBox<>();
        add(civilianComboBox);

        add(new JLabel("Issuing Officer:"));
        officerComboBox = new JComboBox<>();
        add(officerComboBox);

        add(new JLabel("Charges:"));
        chargesArea = new JTextArea(3, 20);
        add(new JScrollPane(chargesArea));

        add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"Active", "Served"});
        add(statusBox);

        // Submit Button
        submitButton = new JButton("Submit Warrant");
        submitButton.addActionListener(e -> submitWarrant());
        add(submitButton);

        setVisible(true);

        // Populate dropdowns
        populateCivilians();
        populateOfficers();
    }

    private void populateCivilians() {
        List<User> civilians = UserDAO.getUsersByNameOrCid("");
        for (User civilian : civilians) {
            civilianComboBox.addItem(civilian.getName()); // Display only name
        }
    }

    private void populateOfficers() {
        List<User> officers = UserDAO.getUsersByNameOrCid("");
        for (User officer : officers) {
            if ("police".equalsIgnoreCase(officer.getJobType())) {
                officerComboBox.addItem(officer.getName()); // Display only name
            }
        }
    }

    private void submitWarrant() {
        try {
            // Fetch selected names
            String civilianName = (String) civilianComboBox.getSelectedItem();
            String officerName = (String) officerComboBox.getSelectedItem();
            String charges = chargesArea.getText();
            String status = (String) statusBox.getSelectedItem();

            if (civilianName == null || officerName == null || charges.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fetch actual CID from the database
            String civilianCid = UserDAO.getCidByName(civilianName);
            String officerCid = UserDAO.getCidByName(officerName);

            if (civilianCid == null || officerCid == null) {
                JOptionPane.showMessageDialog(this, "Error retrieving user data!", "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create Warrant Object
            Warrant newWarrant = new Warrant(0, civilianCid, charges, officerCid, status, new Timestamp(System.currentTimeMillis()));

            // Add to database
            boolean success = WarrantDAO.addWarrant(newWarrant);

            if (success) {
                JOptionPane.showMessageDialog(this, "Warrant Submitted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error submitting warrant!", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new WarrantSubmissionUI();
    }
}
