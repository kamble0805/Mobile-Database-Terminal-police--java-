package database;

import models.Case;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaseDAO {

    // Add a new case
    public static boolean addCase(Case newCase) {
        String sql = "INSERT INTO mdt_cases (case_type, title, details, officers_involved, civs_involved, charges, fine, sentence, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, newCase.getCaseType());
            stmt.setString(2, newCase.getTitle());
            stmt.setString(3, newCase.getDetails());
            stmt.setString(4, newCase.getOfficers());
            stmt.setString(5, newCase.getCivilians());
            stmt.setString(6, newCase.getCharges());
            stmt.setInt(7, newCase.getFine());
            stmt.setInt(8, newCase.getSentence());
            stmt.setTimestamp(9, newCase.getCreatedAt()); // Set the created_at timestamp
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding case: " + e.getMessage());
            return false;
        }
    }

    // Update an existing case
    public static boolean updateCase(Case updatedCase) {
        String sql = "UPDATE mdt_cases SET case_type = ?, title = ?, details = ?, officers_involved = ?, civs_involved = ?, charges = ?, fine = ?, sentence = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, updatedCase.getCaseType());
            stmt.setString(2, updatedCase.getTitle());
            stmt.setString(3, updatedCase.getDetails());
            stmt.setString(4, updatedCase.getOfficers());
            stmt.setString(5, updatedCase.getCivilians());
            stmt.setString(6, updatedCase.getCharges());
            stmt.setInt(7, updatedCase.getFine());
            stmt.setInt(8, updatedCase.getSentence());
            stmt.setInt(9, updatedCase.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating case: " + e.getMessage());
            return false;
        }
    }

    // Delete a case
    public static boolean deleteCase(int caseId) {
        String sql = "DELETE FROM mdt_cases WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, caseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting case: " + e.getMessage());
            return false;
        }
    }

    // Retrieve a case by ID
    public static Case getCaseById(int caseId) {
        String sql = "SELECT * FROM mdt_cases WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, caseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Case(
                    rs.getInt("id"),
                    rs.getString("case_type"),
                    rs.getString("title"),
                    rs.getString("details"),
                    rs.getString("evidence"),
                    rs.getString("officers_involved"),
                    rs.getString("civs_involved"),
                    rs.getString("charges"),
                    rs.getInt("fine"),
                    rs.getInt("sentence"),
                    rs.getTimestamp("created_at") // Retrieve created_at as well
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving case: " + e.getMessage());
        }
        return null; // Return null if no case is found or an error occurs
    }

    // Optional: Method to retrieve all cases
    public static List<Case> getAllCases() {
        List<Case> cases = new ArrayList<>();
        String sql = "SELECT * FROM mdt_cases";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                cases.add(new Case(
                    rs.getInt("id"),
                    rs.getString("case_type"),
                    rs.getString("title"),
                    rs.getString("details"),
                    rs.getString("evidence"),
                    rs.getString("officers_involved"),
                    rs.getString("civs_involved"),
                    rs.getString("charges"),
                    rs.getInt("fine"),
                    rs.getInt("sentence"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all cases: " + e.getMessage());
        }
        return cases; // Return the list of cases
    }
}