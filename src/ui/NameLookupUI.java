package ui;

import database.UserDAO;
import database.VehicleDAO; // Import VehicleDAO to fetch vehicles
import models.User;
import models.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NameLookupUI extends JFrame {
    private JTextField searchField;
    private DefaultListModel<User> listModel;
    private JList<User> resultList;
    private JPanel profilePanel;
    private JLabel nameLabel, dobLabel, jobLabel, cidLabel, fingerprintLabel, usernameLabel, tagsLabel;
    private JTextArea infoArea, editInfoArea;
    private JButton saveButton;
    private User selectedUser ;
    private DefaultListModel<String> vehicleListModel; // Model for vehicle plates
    private JList<String> vehicleList; // List to display vehicle plates

    public NameLookupUI() {
        setTitle("Name Lookup");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter Name or CID:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchUser ());
        searchPanel.add(searchButton);

        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.addActionListener(e -> openDashboard());
        searchPanel.add(dashboardButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        searchPanel.add(logoutButton);

        // 🔹 Result List
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && resultList.getSelectedValue() != null) {
                showProfile(resultList.getSelectedValue());
            }
        });

        // 🔹 Profile Panel
        profilePanel = new JPanel(new GridLayout(10, 1, 5, 5));
        profilePanel.setBorder(BorderFactory.createTitledBorder("User  Profile"));

        nameLabel = new JLabel("Name: ");
        dobLabel = new JLabel("DOB: ");
        jobLabel = new JLabel("Job Type: ");
        cidLabel = new JLabel("CID: ");
        fingerprintLabel = new JLabel("Fingerprint: ");
        usernameLabel = new JLabel("Username: ");
        tagsLabel = new JLabel("Tags: ");

        infoArea = new JTextArea(2, 20);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setEditable(false);

        editInfoArea = new JTextArea(2, 20);
        editInfoArea.setLineWrap(true);
        editInfoArea.setWrapStyleWord(true);

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveInformation());

        // 🔹 Vehicle List Panel
        vehicleListModel = new DefaultListModel<>();
        vehicleList = new JList<>(vehicleListModel);
        vehicleList.setBorder(BorderFactory.createTitledBorder("Associated Vehicles"));

        profilePanel.add(nameLabel);
        profilePanel.add(dobLabel);
        profilePanel.add(jobLabel);
        profilePanel.add(cidLabel);
        profilePanel.add(fingerprintLabel);
        profilePanel.add(usernameLabel);
        profilePanel.add(tagsLabel);
        profilePanel.add(new JLabel("Info:"));
        profilePanel.add(new JScrollPane(infoArea));
        profilePanel.add(new JLabel("Edit:"));
        profilePanel.add(new JScrollPane(editInfoArea));
        profilePanel.add(saveButton);
        profilePanel.add(new JScrollPane(vehicleList)); // Add vehicle list to profile panel

        // Layout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(new JScrollPane(resultList));
        mainPanel.add(profilePanel);

        add(searchPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // ✅ Open Dashboard
    private void openDashboard() {
        new DashboardUI("Officer");
        dispose();
    }

    // ✅ Logout
    private void logout() {
        dispose();
        new LoginUI();
    }

    // ✅ Search Function
    private void searchUser () {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a name or CID.");
            return;
        }

        listModel.clear();

        List<User> users = UserDAO.getUsersByNameOrCid(query);
        if (!users.isEmpty()) {
            for (User  user : users) {
                listModel.addElement(user);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No match found.");
        }
    }

    // ✅ Show Profile Details
    private void showProfile(User user) {
        if (user == null) return;

        selectedUser  = user;
        nameLabel.setText("Name: " + user.getName());
        dobLabel.setText("DOB: " + user.getDob());
        jobLabel.setText("Job: " + user.getJobType());
        cidLabel.setText("CID: " + user.getCid());
        fingerprintLabel.setText("Fingerprint: " + user.getFingerprint());
        usernameLabel.setText("Username: " + user.getUsername());
        tagsLabel.setText("Tags: " + user.getTags());
        infoArea.setText(user.getInformation());
        editInfoArea.setText(user.getInformation());

        // Load associated vehicles
        loadAssociatedVehicles(user.getCid());
    }

    // ✅ Load Associated Vehicles
    private void loadAssociatedVehicles(String cid) {
        vehicleListModel.clear(); // Clear previous entries
        List<Vehicle> vehicles = VehicleDAO.getVehiclesByCid(cid); // Fetch vehicles by CID

        for (Vehicle vehicle : vehicles) {
            vehicleListModel.addElement(vehicle.getPlate()); // Add vehicle plate to the list
        }
    }

    // ✅ Save Information
    private void saveInformation() {
        if (selectedUser  == null) {
            JOptionPane.showMessageDialog(this, "No user selected.");
            return;
        }

        String newInfo = editInfoArea.getText().trim();
        if (newInfo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Info cannot be empty.");
            return;
        }

        boolean success = UserDAO.updateUserInformation(selectedUser .getCid(), newInfo);
        if (success) {
            JOptionPane.showMessageDialog(this, "Info updated.");
            selectedUser .setInformation(newInfo);
            infoArea.setText(newInfo);
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }

    public static void main(String[] args) {
        new NameLookupUI();
    }
}