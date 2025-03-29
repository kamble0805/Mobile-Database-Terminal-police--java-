package database;

import models.ClockingRecord; // Import the ClockingRecord model
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class ClockingDAO {

    // Method to clock in a user
    public static boolean clockIn(String cid, String name, String officerStatus, Timestamp clockInTime) {
        String sql = "INSERT INTO mdt_clocking (cid, name, status, officer_status, clock_in_time) VALUES (?, ?, 'clocked_in', ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cid);
            stmt.setString(2, name);
            stmt.setString(3, officerStatus); // Set officer status
            stmt.setTimestamp(4, clockInTime);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Return true if the insert was successful
        } catch (Exception e) {
            System.err.println("❌ Error clocking in: " + e.getMessage());
            return false; // Return false if there was an error
        }
    }

    // Method to clock out a user
    public static boolean clockOut(String cid) {
        String sql = "UPDATE mdt_clocking SET status = 'clocked_out', clock_out_time = NOW() WHERE cid = ? AND status = 'clocked_in'";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cid);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Return true if the update was successful
        } catch (Exception e) {
            System.err.println("❌ Error clocking out: " + e.getMessage());
            return false; // Return false if there was an error
        }
    }

    // Method to get a ClockingRecord if the user is clocked in
    public static ClockingRecord getClockingRecord(String cid, String name) {
        String sql = "SELECT * FROM mdt_clocking WHERE cid = ? AND name = ? AND status = 'clocked_in'";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cid);
            stmt.setString(2, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create and return a ClockingRecord object
                return new ClockingRecord(
                    rs.getInt("id"),
                    rs.getString("cid"),
                    rs.getString("name"),
                    rs.getString("status"),
                    rs.getString("officer_status"), // Retrieve officer status
                    rs.getTimestamp("clock_in_time"),
                    rs.getTimestamp("clock_out_time")
                );
            }
        } catch (Exception e) {
            System.err.println("❌ Error retrieving clocking record: " + e.getMessage());
        }
        return null; // Return null if no record is found
    }

    // Method to get a list of officers who are currently clocked in
    public static List<ClockingRecord> getClockedInOfficers() {
        List<ClockingRecord> officers = new ArrayList<>();
        String sql = "SELECT * FROM mdt_clocking WHERE status = 'clocked_in'";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Create a ClockingRecord object for each officer
                ClockingRecord officer = new ClockingRecord(
                    rs.getInt("id"),
                    rs.getString("cid"),
                    rs.getString("name"),
                    rs.getString("status"),
                    rs.getString("officer_status"), // Retrieve officer status
                    rs.getTimestamp("clock_in_time"),
                    rs.getTimestamp("clock_out_time")
                );
                officers.add(officer);
            }
        } catch (Exception e) {
            System.err.println("❌ Error retrieving clocked-in officers: " + e.getMessage());
        }
        return officers; // Return the list of clocked-in officers
    }

    // Method to update officer status
    public static void updateOfficerStatus(String name, String newStatus) {
    // SQL query to update officer status only if the officer is clocked in
    String sql = "UPDATE mdt_clocking SET officer_status = ? WHERE name = ? AND status = 'clocked_in'";
    try (Connection conn = DatabaseManager.connect();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, newStatus);
        stmt.setString(2, name); // Check the name

        // Debugging output
        System.out.println("Executing SQL: " + stmt.toString());

        int rowsUpdated = stmt.executeUpdate();
        
        if (rowsUpdated > 0) {
            System.out.println("✅ Officer status updated to: " + newStatus);
        } else {
            System.err.println("⚠️ No officer found with the given name, or officer is not clocked in.");
        }
    } catch (Exception e) {
        System.err.println("❌ Error updating officer status: " + e.getMessage());
    }
}
}