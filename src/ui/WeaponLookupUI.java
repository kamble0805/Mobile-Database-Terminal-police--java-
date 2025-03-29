package ui;

import database.WeaponDAO;
import models.Weapon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeaponLookupUI {
    private JFrame frame;
    private JTextField serialField;
    private JTextArea resultArea;

    public WeaponLookupUI() {
        // Set up the main frame
        frame = new JFrame("Weapon Lookup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // Input Panel for Lookup
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel serialLabel = new JLabel("Enter Serial:");
        serialField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton addButton = new JButton("Add Weapon");

        inputPanel.add(serialLabel);
        inputPanel.add(serialField);
        inputPanel.add(searchButton);
        inputPanel.add(addButton);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Add panels to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Action Listener for Search Button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchWeapon();
            }
        });

        // Action Listener for Add Button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddWeaponDialog();
            }
        });

        frame.setVisible(true);
    }

    private void searchWeapon() {
        String serial = serialField.getText().trim();
        WeaponDAO weaponDAO = new WeaponDAO();
        Weapon weapon = weaponDAO.getWeaponBySerial(serial);

        resultArea.setText(""); // Clear previous results

        if (weapon != null) {
            resultArea.setText(weapon.toString());
        } else {
            resultArea.setText("No weapon found with the given serial.");
        }
    }

    private void openAddWeaponDialog() {
        // Create a dialog for adding a weapon
        JDialog addWeaponDialog = new JDialog(frame, "Add Weapon", true);
        addWeaponDialog.setSize(400, 350);
        addWeaponDialog.setLayout(new GridLayout(8, 2));

        // Input fields for weapon details
        JTextField addSerialField = new JTextField();
        JTextField addOwnerField = new JTextField();
        JTextArea addInfoField = new JTextArea(3, 15);
        JTextField addClassField = new JTextField();
        JTextField addModelField = new JTextField();
        JTextField addImageField = new JTextField();
        JTextField addCidField = new JTextField(); // New field for CID

        // Add components to the dialog
        addWeaponDialog.add(new JLabel("Serial:"));
        addWeaponDialog.add(addSerialField);
        addWeaponDialog.add(new JLabel("Owner:"));
        addWeaponDialog.add(addOwnerField);
        addWeaponDialog.add(new JLabel("Information:"));
        addWeaponDialog.add(new JScrollPane(addInfoField));
        addWeaponDialog.add(new JLabel("Class:"));
        addWeaponDialog.add(addClassField);
        addWeaponDialog.add(new JLabel("Model:"));
        addWeaponDialog.add(addModelField);
        addWeaponDialog.add(new JLabel("Image:"));
        addWeaponDialog.add(addImageField);
        addWeaponDialog.add(new JLabel("CID:")); // Label for CID
        addWeaponDialog.add(addCidField); // Field for CID

        JButton addButton = new JButton("Add Weapon");
        addWeaponDialog.add(addButton);

        // Action Listener for Add Button in Dialog
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWeapon(addSerialField.getText().trim(), addOwnerField.getText().trim(),
                        addInfoField.getText().trim(), addClassField.getText().trim(),
                        addModelField.getText().trim(), addImageField.getText().trim(),
                        addCidField.getText().trim()); // Pass CID
                addWeaponDialog.dispose(); // Close the dialog after adding
            }
        });

        addWeaponDialog.setVisible(true); // Show the dialog
    }

    private void addWeapon(String serial, String owner, String information, String weapClass, String weapModel, String image, String cid) {
        Weapon newWeapon = new Weapon(0, serial, owner, information, weapClass, weapModel, image, cid); // Include CID
        WeaponDAO weaponDAO = new WeaponDAO();

        if (weaponDAO.addWeapon(newWeapon)) {
            JOptionPane.showMessageDialog(frame, "Weapon added successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to add weapon. Please try again.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WeaponLookupUI::new);
    }
}