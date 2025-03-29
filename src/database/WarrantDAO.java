package database;

import models.Warrant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WarrantDAO {
    public static boolean addWarrant(Warrant warrant) {
        String sql = "INSERT INTO mdt_warrants (civilian_name, charges, officer_name, status, date_issued) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, warrant.getCivilianName());  // Store civilian's name
            stmt.setString(2, warrant.getCharges());       // Store charges
            stmt.setString(3, warrant.getOfficerName());   // Store officer's name
            stmt.setString(4, warrant.getStatus());        // Store status
            stmt.setTimestamp(5, warrant.getCreatedAt());  // Store timestamp

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding warrant: " + e.getMessage());
            return false;
        }
    }
    
    
    
}
