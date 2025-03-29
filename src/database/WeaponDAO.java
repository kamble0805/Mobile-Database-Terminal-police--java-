package database;

import models.Weapon;

import java.sql.*;

public class WeaponDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/pol_db"; // Update with your database URL
    private static final String USERNAME = "root"; // Update with your database username
    private static final String PASSWORD = "123456"; // Update with your database password

    public Weapon getWeaponBySerial(String serial) {
        String query = "SELECT * FROM mdt_weaponinfo WHERE serial = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, serial);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Weapon(
                        rs.getInt("id"),
                        rs.getString("serial"),
                        rs.getString("owner"),
                        rs.getString("information"),
                        rs.getString("weapClass"),
                        rs.getString("weapModel"),
                        rs.getString("image"),
                        rs.getString("cid") // Retrieve CID
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no weapon is found
    }

    public boolean addWeapon(Weapon weapon) {
        String query = "INSERT INTO mdt_weaponinfo (serial, owner, information, weapClass, weapModel, image, cid) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, weapon.getSerial());
            pstmt.setString(2, weapon.getOwner());
            pstmt.setString(3, weapon.getInformation());
            pstmt.setString(4, weapon.getWeapClass());
            pstmt.setString(5, weapon.getWeapModel());
            pstmt.setString(6, weapon.getImage());
            pstmt.setString(7, weapon.getCid()); // Set CID

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if the insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurred
        }
    }
}