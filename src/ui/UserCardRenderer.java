package ui;

import models.User;

import javax.swing.*;
import java.awt.*;

public class UserCardRenderer extends JPanel implements ListCellRenderer<User> {
    private JLabel nameLabel;
    private JLabel cidLabel;

    public UserCardRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(Color.WHITE);

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        cidLabel = new JLabel();
        cidLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        cidLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(nameLabel);
        textPanel.add(cidLabel);

        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User user, int index, boolean isSelected, boolean cellHasFocus) {
        if (user != null) {
            nameLabel.setText("👤 " + user.getName());
            cidLabel.setText("CID: " + user.getCid());

            if (isSelected) {
                setBackground(new Color(52, 152, 219));
                nameLabel.setForeground(Color.WHITE);
                cidLabel.setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                nameLabel.setForeground(Color.BLACK);
                cidLabel.setForeground(Color.GRAY);
            }
        }

        return this;
    }
}
