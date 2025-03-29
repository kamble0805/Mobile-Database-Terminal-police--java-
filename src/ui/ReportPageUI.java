package ui;

import database.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ReportPageUI extends JFrame {
    private JTextField titleField, tagsField, officersField, civsField;
    private JComboBox<String> typeDropdown;
    private JTextArea detailsArea, reportDetailsArea;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JPanel rightPanel, formPanel, detailsPanel;
    private JComboBox<String> officersDropdown;


     public ReportPageUI() {
        setTitle("MDT Reports");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Split Pane to separate left and right panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Left panel takes 50% width

        // Left Panel - Report List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("All Reports"));

        String[] columns = {"ID", "Title", "Type", "Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(tableModel);
        reportTable.getTableHeader().setReorderingAllowed(false);
        loadReports();

        JScrollPane tableScrollPane = new JScrollPane(reportTable);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Right Panel - Holds Form and Details
        rightPanel = new JPanel(new CardLayout());
        formPanel = createFormPanel();
        detailsPanel = createDetailsPanel();
        
        rightPanel.add(formPanel, "Form");
        rightPanel.add(detailsPanel, "Details");

        // Add Panels to Split Pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Bottom Panel - Logout & Back to Dashboard Buttons
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back to Dashboard");
        JButton logoutButton = new JButton("Logout");

        // Back to Dashboard Action
        backButton.addActionListener(e -> {
            dispose();  // Close Reports Page
            new DashboardUI("John Doe"); // Replace with actual officer name
        });

        // Logout Action
        logoutButton.addActionListener(e -> {
            dispose();  // Close Reports Page
            new LoginUI(); // Open Login Page (Replace with your actual Login UI class)
        });

        bottomPanel.add(backButton);
        bottomPanel.add(logoutButton);

        // Show report details on click
        reportTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = reportTable.getSelectedRow();
                if (selectedRow != -1) {
                    int reportId = (int) tableModel.getValueAt(selectedRow, 0);
                    loadReportDetails(reportId);
                }
            }
        });

        // Add components to Frame
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    
    }

    private JPanel createFormPanel() {
    JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
    panel.setBorder(BorderFactory.createTitledBorder("Submit New Report"));

    // Form Fields
    titleField = new JTextField();
    typeDropdown = new JComboBox<>(new String[]{
        "Investigation", "Civilian Report", "Lost Report", "Closed Report"
    });
    detailsArea = new JTextArea(5, 20);
    tagsField = new JTextField();
    officersDropdown = new JComboBox<>(); // Changed from JTextField to JComboBox
    civsField = new JTextField();

    // Load officers from database
    loadOfficers();

    panel.add(new JLabel("Title:"));
    panel.add(titleField);
    panel.add(new JLabel("Type:"));
    panel.add(typeDropdown);
    panel.add(new JLabel("Details:"));
    panel.add(new JScrollPane(detailsArea));
    panel.add(new JLabel("Tags:"));
    panel.add(tagsField);
    panel.add(new JLabel("Officers Involved:"));
    panel.add(officersDropdown); // Use JComboBox instead of JTextField
    panel.add(new JLabel("Civilians Involved:"));
    panel.add(civsField);

    // Buttons
    JButton submitButton = new JButton("Submit Report");
    submitButton.addActionListener(e -> submitReport());

    JButton clearButton = new JButton("Clear");
    clearButton.addActionListener(e -> clearForm());

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(submitButton);
    buttonPanel.add(clearButton);

    JPanel formContainer = new JPanel(new BorderLayout());
    formContainer.add(panel, BorderLayout.CENTER);
    formContainer.add(buttonPanel, BorderLayout.SOUTH);

    return formContainer;
}


    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Report Details"));

        reportDetailsArea = new JTextArea();
        reportDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(reportDetailsArea);
        panel.add(detailsScrollPane, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back to Submission");
        backButton.addActionListener(e -> switchToForm());

        JPanel buttonPanel = new JPanel();
        
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void clearForm() {
    if (titleField != null) titleField.setText("");
    if (detailsArea != null) detailsArea.setText("");
    if (tagsField != null) tagsField.setText("");

    if (officersDropdown != null) {
        officersDropdown.setSelectedIndex(0); // Reset officers dropdown
    }

    if (civsField != null) { 
        civsField.setText(""); // Clear civilians field (REMOVE if not needed)
    }
}


    private void refreshReportList() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            loadReports();
        });
    }
    private void loadOfficers() {
    try (Connection conn = DatabaseManager.connect()) {
        if (conn == null) {
            System.out.println("❌ Connection failed in loadOfficers()");
            return;
        }

        String sql = "SELECT name FROM mdt_data WHERE jobtype = 'police'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        officersDropdown.addItem("Select Officer"); // Default option

        // Debug print to see if we have any officers
        boolean hasOfficers = false;
        while (rs.next()) {
            String officerName = rs.getString("name");
            officersDropdown.addItem(officerName);
            hasOfficers = true;
        }

        // Debugging
        if (!hasOfficers) {
            System.out.println("❌ No officers found in mdt_data table.");
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

    private void loadReports() {
        try (Connection conn = DatabaseManager.connect()) {
            if (conn == null) {
                System.out.println("❌ Connection failed in loadReports()");
                return;
            }

            // Exclude BOLO reports
            String sql = "SELECT id, title, type, time FROM mdt_reports WHERE type != 'BOLO (Be On The Lookout)' ORDER BY id DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String type = rs.getString("type");
                String time = rs.getString("time");
                tableModel.addRow(new Object[]{id, title, type, time});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void submitReport() {
    String title = titleField.getText();
    String type = (String) typeDropdown.getSelectedItem();
    String details = detailsArea.getText();
    String tags = tagsField.getText();
    String officers = (String) officersDropdown.getSelectedItem(); // Get selected officer
    String civs = civsField.getText();
    String currentTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

    if (title.isEmpty() || details.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Title and Details are required!", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (officers.equals("Select Officer")) { // Ensure an officer is selected
        JOptionPane.showMessageDialog(this, "Please select an officer involved!", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection conn = DatabaseManager.connect()) {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "❌ Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO mdt_reports (author, title, type, details, tags, officersinvolved, civsinvolved, time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "Officer Name"); // Replace with actual officer name from session or user input
        stmt.setString(2, title);
        stmt.setString(3, type);
        stmt.setString(4, details);
        stmt.setString(5, tags);
        stmt.setString(6, officers);
        stmt.setString(7, civs);
        stmt.setString(8, currentTime);

        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "✅ Report submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            refreshReportList();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to submit report.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


    private void loadReportDetails(int reportId) {
        try (Connection conn = DatabaseManager.connect()) {
            String sql = "SELECT * FROM mdt_reports WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                reportDetailsArea.setText("ID: " + rs.getInt("id") + "\nTitle: " + rs.getString("title") + "\nType: " + rs.getString("type") +
                        "\nDetails: " + rs.getString("details") + "\nTags: " + rs.getString("tags") +
                        "\nOfficers: " + rs.getString("officersinvolved") + "\nCivilians: " + rs.getString("civsinvolved") +
                        "\nTime: " + rs.getString("time"));
                switchToDetails();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void switchToDetails() {
        ((CardLayout) rightPanel.getLayout()).show(rightPanel, "Details");
    }

    private void switchToForm() {
        ((CardLayout) rightPanel.getLayout()).show(rightPanel, "Form");
    }

    public static void main(String[] args) {
        new ReportPageUI();
    }
}
