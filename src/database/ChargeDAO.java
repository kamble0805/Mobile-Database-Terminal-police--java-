package database;

import models.Charge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChargeDAO {

    // Method to add a new charge
    public static boolean addCharge(Charge charge) {
        String sql = "INSERT INTO penal_code_charges (description, severity, penal_code, notes) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, charge.getDescription());
            stmt.setString(2, charge.getSeverity());
            stmt.setString(3, charge.getPenalCode());
            stmt.setString(4, charge.getNotes());
            return stmt.executeUpdate() > 0; // Returns true if a row was inserted
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Method to retrieve all charges
    public static List<Charge> getAllCharges() {
        List<Charge> charges = new ArrayList<>();
        String sql = "SELECT id, description, severity, penal_code, notes FROM penal_code_charges ORDER BY id";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Charge charge = new Charge(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("severity"),
                        rs.getString("penal_code"),
                        rs.getString("notes")
                );
                charges.add(charge);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return charges;
    }

    // Method to delete a charge by ID
    public static boolean deleteCharge(int id) {
        String sql = "DELETE FROM penal_code_charges WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0; // Returns true if a row was deleted
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Additional methods for updating charges can be added here
}