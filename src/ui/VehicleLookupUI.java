package ui;

import database.VehicleDAO;
import models.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VehicleLookupUI extends JFrame {
    private JTextField searchField;
    private DefaultListModel<Vehicle> listModel;
    private JList<Vehicle> resultList;
    private JPanel vehiclePanel;
    private JLabel plateLabel, nameLabel, stolenLabel, pointsLabel;
    private JTextArea infoArea, editInfoArea;
    private JCheckBox code5CheckBox;  // ✅ Checkbox for Code 5
    private JSpinner pointsSpinner;   // ✅ Spinner for Points
    private JButton saveButton;
    private Vehicle selectedVehicle;

    public VehicleLookupUI() {
        setTitle("Vehicle Lookup");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter Plate:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchVehicle());
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
                showVehicleDetails(resultList.getSelectedValue());
            }
        });

        // 🔹 Vehicle Panel
        vehiclePanel = new JPanel(new GridLayout(9, 1, 5, 5));
        vehiclePanel.setBorder(BorderFactory.createTitledBorder("Vehicle Details"));

        plateLabel = new JLabel("Plate: ");
        nameLabel = new JLabel("Name: ");
        stolenLabel = new JLabel("Stolen: ");
        pointsLabel = new JLabel("Points: ");

        infoArea = new JTextArea(2, 20);
        infoArea.setLineWrap(true);
        infoArea.setEditable(false);

        editInfoArea = new JTextArea(2, 20);
        editInfoArea.setLineWrap(true);

        code5CheckBox = new JCheckBox("Code 5 (High Alert)");  // ✅ Checkbox for Code 5

        pointsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));  // ✅ Spinner for points

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveVehicleInfo());

        vehiclePanel.add(plateLabel);
        vehiclePanel.add(nameLabel);
        vehiclePanel.add(stolenLabel);
        vehiclePanel.add(pointsLabel);
        vehiclePanel.add(new JLabel("Info:"));
        vehiclePanel.add(new JScrollPane(infoArea));
        vehiclePanel.add(new JLabel("Edit:"));
        vehiclePanel.add(new JScrollPane(editInfoArea));
        vehiclePanel.add(code5CheckBox);  // ✅ Added checkbox
        vehiclePanel.add(new JLabel("Update Points:"));
        vehiclePanel.add(pointsSpinner);  // ✅ Added spinner
        vehiclePanel.add(saveButton);

        // Layout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(new JScrollPane(resultList));
        mainPanel.add(vehiclePanel);

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

    // ✅ Search Vehicle Function
    private void searchVehicle() {
        String plate = searchField.getText().trim();
        if (plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a plate number.");
            return;
        }

        listModel.clear();
        List<Vehicle> vehicles = VehicleDAO.getVehiclesByPlate(plate);
        if (!vehicles.isEmpty()) {
            for (Vehicle vehicle : vehicles) {
                listModel.addElement(vehicle);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No match found.");
        }
    }

    // ✅ Show Vehicle Details
    private void showVehicleDetails(Vehicle vehicle) {
        if (vehicle == null) return;

        selectedVehicle = vehicle;
        plateLabel.setText("Plate: " + vehicle.getPlate());
        nameLabel.setText("Name: " + vehicle.getName());
        stolenLabel.setText("Stolen: " + (vehicle.isStolen() ? "Yes" : "No"));
        pointsLabel.setText("Points: " + vehicle.getPoints());
        infoArea.setText(vehicle.getInformation());
        editInfoArea.setText(vehicle.getInformation());

        code5CheckBox.setSelected(vehicle.isCode5());  // ✅ Set checkbox value
        pointsSpinner.setValue(vehicle.getPoints());   // ✅ Set spinner value
    }

    // ✅ Save Vehicle Information
    private void saveVehicleInfo() {
        if (selectedVehicle == null) {
            JOptionPane.showMessageDialog(this, "No vehicle selected.");
            return;
        }

        String newInfo = editInfoArea.getText().trim();
        boolean newCode5 = code5CheckBox.isSelected();  // ✅ Get checkbox value
        int newPoints = (int) pointsSpinner.getValue(); // ✅ Get spinner value

        boolean success = VehicleDAO.updateVehicleInfo(
                selectedVehicle.getId(), newInfo, selectedVehicle.isStolen(), newCode5, newPoints
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Info updated.");
            selectedVehicle.setInformation(newInfo);
            selectedVehicle.setCode5(newCode5);  // ✅ Update local object
            selectedVehicle.setPoints(newPoints); // ✅ Update local object
            infoArea.setText(newInfo);
            pointsLabel.setText("Points: " + newPoints);
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }

    public static void main(String[] args) {
        new VehicleLookupUI();
    }
}
