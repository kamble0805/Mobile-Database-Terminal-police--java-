package ui;

import database.UserDAO;
import models.User;

import javax.swing.*;
import java.awt.*;

public class RegisterUI extends JFrame {
    private JTextField cidField, usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterUI() {
        setTitle("Register - Police MDT");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Title Label
        JLabel titleLabel = new JLabel("Police MDT - Register", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        // Form Panel
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Form Components
        JLabel cidLabel = new JLabel("Citizen ID (CID):");
        cidField = new JTextField(20);
        cidField.setToolTipText("Enter your Citizen ID (Alphanumeric)");

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        usernameField.setToolTipText("Enter a username (min 3 characters)");

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setToolTipText("Enter a password (min 6 characters)");

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        // Layout Configuration
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        cardPanel.add(cidLabel, gbc);
        gbc.gridx = 1;
        cardPanel.add(cidField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        cardPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        cardPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        cardPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        cardPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        cardPanel.add(registerButton, gbc);

        // Wrapper Panel
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(44, 62, 80));
        wrapperPanel.add(cardPanel);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(wrapperPanel, BorderLayout.CENTER);

        // Register Button Action
        registerButton.addActionListener(e -> registerUser ());

        setVisible(true);
    }

    private void registerUser () {
        String cid = cidField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // 🔹 Input Validation
        if (cid.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("All fields are required!");
            return;
        }
        // Allow alphanumeric CID
        if (!cid.matches("[a-zA-Z0-9]+")) {
            showError("CID must be alphanumeric!");
            return;
        }
        if (username.length() < 3) {
            showError("Username must be at least 3 characters long!");
            return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters long!");
            return;
        }

        // 🔹 Disable button while processing
        registerButton.setEnabled(false);

        // 🔹 Check if CID exists
        User existingUser  = UserDAO.getUserByCid(cid);
        if (existingUser  == null) {
            showError("CID not found in the database!");
            registerButton.setEnabled(true);
            return;
        }

        // 🔹 Check if username is already taken
        if (UserDAO.isUsernameTaken(username)) {
            showError("Username is already taken. Please choose another.");
            registerButton.setEnabled(true);
            return;
        }

        // 🔹 Hash Password (Optional - If you add hashing in UserDAO)
        String hashedPassword = password; // Replace with hash function if needed

        // 🔹 Create user and update database
        User newUser  = new User(cid, username, hashedPassword, existingUser .getName(), existingUser .getDob(), existingUser .getJobType(),
                existingUser .getFingerprint(), existingUser .getTags(), existingUser .getInformation(), existingUser .getGallery());

        if (UserDAO.registerUser (newUser )) {
            JOptionPane.showMessageDialog(this, "User  registered successfully!");
            dispose();
        } else {
            showError("Error updating user info!");
        }

        // 🔹 Re-enable button
        registerButton.setEnabled(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}