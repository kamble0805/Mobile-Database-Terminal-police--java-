package database;

import models.User;
import models.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    // ✅ Check if CID exists in the database
    public static boolean checkCidExists(String cid) {
        if (cid == null || cid.isEmpty()) {
            return false;
        }

        String sql = "SELECT cid FROM mdt_data WHERE cid = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cid);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // True if CID exists
        } catch (SQLException e) {
            System.err.println("❌ Error checking CID: " + e.getMessage());
        }
        return false;
    }

    // ✅ Update user information
    public static boolean updateUserInformation(String cid, String newInfo) {
        String sql = "UPDATE mdt_data SET information = ? WHERE cid = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newInfo);
            stmt.setString(2, cid);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error updating user info: " + e.getMessage());
        }
        return false;
    }

    // ✅ Check if Username is already taken
    public static boolean isUsernameTaken(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }

        String sql = "SELECT username FROM mdt_data WHERE username = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // True if username exists
        } catch (SQLException e) {
            System.err.println("❌ Error checking username: " + e.getMessage());
        }
        return false;
    }

    // ✅ Fetch users by Name (partial match) or CID (exact match)
    public static List<User> getUsersByNameOrCid(String query) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT cid, name, dob, jobType, fingerprint, username, tags, information, gallery FROM mdt_data WHERE cid = ? OR name LIKE ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, query);
            stmt.setString(2, "%" + query + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("cid"),
                        rs.getString("username"),
                        "", // Password is not fetched for security reasons
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("jobType"),
                        rs.getString("fingerprint"),
                        rs.getString("tags"),
                        rs.getString("information"),
                        rs.getString("gallery")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching users: " + e.getMessage());
        }
        return users;
    }

    // ✅ Get user by CID
    public static User getUserByCid(String cid) {
        if (cid == null || cid.isEmpty()) {
            return null;
        }

        String sql = "SELECT username, name, dob, jobType, fingerprint, tags, information, gallery FROM mdt_data WHERE cid = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        cid,
                        rs.getString("username"),
                        "", // Password is not fetched for security reasons
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("jobType"),
                        rs.getString("fingerprint"),
                        rs.getString("tags"),
                        rs.getString("information"),
                        rs.getString("gallery")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching user by CID: " + e.getMessage());
        }
        return null;
    }

    // ✅ Register a new user
    public static boolean registerUser(User user) {
        if (!checkCidExists(user.getCid())) {
            System.err.println("❌ CID not found in database.");
            return false;
        }

        if (isUsernameTaken(user.getUsername())) {
            System.err.println("❌ Username already exists.");
            return false;
        }

        String sql = "UPDATE mdt_data SET username = ?, password = ? WHERE cid = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // ⚠️ Implement password hashing
            stmt.setString(3, user.getCid());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error registering user: " + e.getMessage());
        }
        return false;
    }

    // ✅ Fetch vehicles linked to a CID
    public static List<Vehicle> getVehiclesByCid(String cid) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM mdt_vehicleinfo WHERE cid = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate"),
                        rs.getString("information"),
                        rs.getBoolean("stolen"),
                        rs.getBoolean("code5"),
                        rs.getString("image"),
                        rs.getInt("points"),
                        rs.getString("cid"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    // ✅ Authenticate user login
    public static User authenticateUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return null;
        }

        String sql = "SELECT cid, name, dob, jobType FROM mdt_data WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // ⚠️ Replace with hashed password verification

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("cid"),
                        username,
                        password, // Store hashed password in the future
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("jobType")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error during authentication: " + e.getMessage());
        }
        return null;
    }
    // ✅ Get CID by Name
public static String getCidByName(String name) {
    if (name == null || name.isEmpty()) {
        return null;
    }

    String sql = "SELECT cid FROM mdt_data WHERE name = ? LIMIT 1";

    try (Connection conn = DatabaseManager.connect();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getString("cid"); // Return CID if found
        }
    } catch (SQLException e) {
        System.err.println("❌ Error fetching CID by name: " + e.getMessage());
    }
    return null; // Return null if not found
}

    // ✅ Get user by Username
    public static User getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }

        String sql = "SELECT cid, name, dob, jobType FROM mdt_data WHERE username = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("cid"),
                        username,
                        "", // Do not return password for security reasons
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("jobType")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching user by username: " + e.getMessage());
        }
        return null;
    }
}
