package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import database.CaseDAO;
import database.UserDAO; // Use UserDAO instead of MdtDataDAO
import models.Case;
import models.User; // Assuming you have a model for User
import database.DatabaseManager;

public class CaseUI extends JFrame { // Extend JFrame
    private JTextField titleField, detailsField, chargesField, fineField, sentenceField;
    private JComboBox<String> caseTypeComboBox; // JComboBox for case types
    private JComboBox<String> civilianComboBox; // JComboBox for civilians
    private JComboBox<String> officerComboBox; // JComboBox for officers
    private JButton addButton, deleteButton, viewChargesButton; // Added viewChargesButton
    private JTextArea outputArea; // Output area for messages
    private JTable caseTable; // JTable to display all cases
    private DefaultTableModel tableModel; // Table model for the JTable

    public CaseUI() { // No logged-in user parameter
        setTitle("Case Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600); // Increased window size
        setLayout(new BorderLayout());

        // Create a JSplitPane to divide the content area
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500); // Set initial divider location
        splitPane.setContinuousLayout(true); // Enable continuous layout

        // Left Panel - Case List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("All Cases"));

        String[] columns = {"ID", "Case Type", "Title", "Details", "Charges", "Fine", "Sentence", "Created At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        caseTable = new JTable(tableModel);
        caseTable.getTableHeader().setReorderingAllowed(false);
        loadCases(); // Load cases into the table

        JScrollPane tableScrollPane = new JScrollPane(caseTable);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Right Panel - Holds Form
        JPanel rightPanel = new JPanel(new CardLayout());
        JPanel formPanel = createFormPanel();

        rightPanel.add(formPanel, "Form");

        // Add panels to the split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Add the split pane to the frame
        add(splitPane, BorderLayout.CENTER);

        // Bottom Panel - Logout & Back to Dashboard Buttons
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back to Dashboard");
        JButton logoutButton = new JButton("Logout");

        // Back to Dashboard Action
        backButton.addActionListener(e -> {
            dispose();  // Close CaseUI
            new DashboardUI("John Doe"); // Pass a placeholder officer name
        });

        // Logout Action
        logoutButton.addActionListener(e -> {
            dispose();  // Close CaseUI
            new LoginUI(); // Ensure LoginUI is imported and exists
        });

        // Add View Charges Button
        viewChargesButton = new JButton("View Charges");
        viewChargesButton.addActionListener(e -> openChargesDisplay());

        bottomPanel.add(backButton);
        bottomPanel.add(logoutButton);
        bottomPanel.add(viewChargesButton); // Add the view charges button

        // Add components to Frame
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);

        // Populate JComboBoxes
        populateCivilians();
        populateOfficers();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(11, 2, 10, 10)); // Adjusted to 11 rows
        panel.setBorder(BorderFactory.createTitledBorder("Submit New Case"));

        // Form Fields
        caseTypeComboBox = new JComboBox<>(new String[]{"Incident", "Conviction", "Warrant"}); // Case types
        civilianComboBox = new JComboBox<>(); // JComboBox for civilians
        officerComboBox = new JComboBox<>(); // JComboBox for officers
        titleField = new JTextField();
        detailsField = new JTextField();
        chargesField = new JTextField();
        fineField = new JTextField();
        sentenceField = new JTextField();

        panel.add(new JLabel("Case Type:"));
        panel.add(caseTypeComboBox);
        panel.add(new JLabel("Civilian:"));
        panel.add(civilianComboBox);
        panel.add(new JLabel(" Officer:"));
        panel.add(officerComboBox);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Details:"));
        panel.add(detailsField);
        panel.add(new JLabel("Charges:"));
        panel.add(chargesField);
        panel.add(new JLabel("Fine:"));
        panel.add(fineField);
        panel.add(new JLabel("Sentence:"));
        panel.add(sentenceField);

        // Add buttons for actions
        addButton = new JButton("Add Case");
        deleteButton = new JButton("Delete Case");

        // Add action listeners for buttons
        addButton.addActionListener(e -> addCase());
        deleteButton.addActionListener(e -> deleteCase());

        panel.add(addButton);
        panel.add(deleteButton);

        return panel;
    }

    private void loadCases() {
        try (Connection conn = DatabaseManager.connect()) {
            if (conn == null) {
                System.out.println("❌ Connection failed in loadCases()");
                return;
            }

            String sql = "SELECT id, case_type, title, details, charges, fine, sentence, created_at FROM mdt_cases ORDER BY id DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear previous data
            while (rs.next()) {
                int id = rs.getInt("id");
                String caseType = rs.getString("case_type");
                String title = rs.getString("title");
                String details = rs.getString("details");
                String charges = rs.getString("charges");
                int fine = rs.getInt("fine");
                int sentence = rs.getInt("sentence");
                Timestamp createdAt = rs.getTimestamp("created_at");

                tableModel.addRow(new Object[]{
                    id, caseType, title, details, charges, fine, sentence, createdAt
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Print stack trace for debugging
        }
    }

    private void populateCivilians() {
        List<User> civilians = UserDAO.getUsersByNameOrCid(""); // Fetch all civilians
        for (User  civilian : civilians) {
            civilianComboBox.addItem(civilian.getName() + " (" + civilian.getCid() + ")"); // Display name and CID
        }
    }

    private void populateOfficers() {
        List<User> officers = UserDAO.getUsersByNameOrCid(""); // Fetch all officers
        for (User  officer : officers) {
            if ("police".equalsIgnoreCase(officer.getJobType())) { // Filter by job type
                officerComboBox.addItem(officer.getName() + " (" + officer.getCid() + ")"); // Display name and CID
            }
        }
    }

    private void addCase() {
        try {
            Case newCase = new Case(
                0,
                (String) caseTypeComboBox.getSelectedItem(),
                titleField.getText(),
                detailsField.getText(),
                "", // Assuming evidence is not provided here
                officerComboBox.getSelectedItem().toString().split("\\(")[1].replace(")", ""),
                civilianComboBox.getSelectedItem().toString().split("\\(")[1].replace(")", ""),
                chargesField.getText(),
                Integer.parseInt(fineField.getText()),
                Integer.parseInt(sentenceField.getText()),
                new Timestamp(System.currentTimeMillis())
            );

            if (CaseDAO.addCase(newCase)) {
                JOptionPane.showMessageDialog(this, "Case added successfully!");
                loadCases(); // Refresh the case list
            } else {
                JOptionPane.showMessageDialog(this, "Error adding case.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Fine and Sentence must be valid integers.");
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteCase() {
        try {
            int caseId = Integer.parseInt(titleField.getText());
            if (CaseDAO.deleteCase(caseId)) {
                JOptionPane.showMessageDialog(this, "Case deleted successfully!");
                loadCases(); // Refresh the case list
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting case.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Case ID must be a valid integer.");
        } catch (Exception ex) {
            ex.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void openChargesDisplay() {
    ChargesDisplayUI chargesUI = new ChargesDisplayUI();
    chargesUI.setVisible(true);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CaseUI(); // Launch the application on the EDT
        });
    }
}