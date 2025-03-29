package database;

import models.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    // ✅ Fetch vehicles by plate number
    public static List<Vehicle> getVehiclesByPlate(String plate) {
        List<Vehicle> vehicles = new ArrayList<>();
        Connection conn = DatabaseManager.connect();

        if (conn == null) {
            System.err.println("Database connection failed!");
            return vehicles;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT v.*, d.name AS owner_name " +
                "FROM mdt_vehicleinfo v " +
                "JOIN mdt_data d ON v.cid = d.cid " +
                "WHERE v.plate LIKE ?"
            );
            stmt.setString(1, "%" + plate + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vehicles.add(new Vehicle(
                    rs.getInt("id"),
                    rs.getString("plate"),
                    rs.getString("information"),
                    rs.getBoolean("stolen"), // Correctly retrieve as boolean
                    rs.getBoolean("code5"),   // Correctly retrieve as boolean
                    rs.getString("image"),
                    rs.getInt("points"),         // ✅ Ensure points field is fetched
                    rs.getString("cid"),
                    rs.getString("owner_name")   // ✅ Fetch owner name
                ));
            }

            // ✅ Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }
    public static List<Vehicle> getVehiclesByCid(String cid) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM mdt_vehicleinfo WHERE cid = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                    rs.getInt("id"),
                    rs.getString("plate"),
                    rs.getString("information"),
                    rs.getBoolean("stolen"),
                    rs.getBoolean("code5"),
                    rs.getString("image"),
                    rs.getInt("points"),
                    rs.getString("cid"),
                    rs.getString("name")
                );
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }
    // ✅ Update vehicle info, including points
    public static boolean updateVehicleInfo(int id, String info, boolean stolen, boolean code5, int points) {
        Connection conn = DatabaseManager.connect();

        if (conn == null) {
            System.err.println("Database connection failed!");
            return false;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE mdt_vehicleinfo SET information=?, stolen=?, code5=?, points=? WHERE id=?"
            );
            stmt.setString(1, info);
            stmt.setBoolean(2, stolen); // Correctly set as boolean
            stmt.setBoolean(3, code5);   // Correctly set as boolean
            stmt.setInt(4, points);  // ✅ Now updates points
            stmt.setInt(5, id);

            boolean success = stmt.executeUpdate() > 0;

            // ✅ Close resources
            stmt.close();
            conn.close();

            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Vehicle> getHighAlertVehicles() {
        List<Vehicle> highAlertVehicles = new ArrayList<>();
        String sql = "SELECT * FROM mdt_vehicleinfo WHERE code5 = 1"; // Query to get high alert vehicles

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Assuming you have a Vehicle constructor that takes these parameters
                Vehicle vehicle = new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate"),
                        rs.getString("information"),
                        rs.getBoolean("stolen"), // Correctly retrieve as boolean
                        rs.getBoolean("code5"),   // Correctly retrieve as boolean
                        rs.getString("image"),
                        rs.getInt("points"),
                        rs.getString("cid"),
                        rs.getString("name")
                );
                highAlertVehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return highAlertVehicles;
    }
}