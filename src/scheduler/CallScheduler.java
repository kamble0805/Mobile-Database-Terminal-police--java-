package scheduler;

import database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class CallScheduler {

  
    private static final String[] INCIDENT_TYPES = {
        "House Robbery", 
        "Shots Fired", 
        "Car Jacking", 
        "EMS Down", 
        "Police Down", 
        "Bank Robbery", 
        "Jewelry Robbery", 
        "Active Boosting", 
        "911 Help Calls"
    };

    
    private static final String[] LOCATIONS = {
        "Los Santos International Airport",
        "Vinewood Hills",
        "Downtown Los Santos",
        "South Los Santos",
        "Paleto Bay",
        "Sandy Shores",
        "Mount Chiliad",
        "Fort Zancudo",
        "Grapeseed",
        "Del Perro Pier",
        "Rockford Hills",
        "Chumash",
        "Harmony",
        "Zancudo River",
        "East Vinewood",
        "West Vinewood",
        "Little Seoul",
        "Davis",
        "South Central",
        "North Chumash",
        "Vespucci Beach",
        "Vespucci Canals",
        "Pillbox Hill",
        "Mission Row",
        "Strawberry",
        "El Burro Heights",
        "Cypress Flats",
        "La Mesa",
        "Docks",
        "Port of Los Santos",
        "Los Santos Golf Club",
        "The Vinewood Bowl",
        "The Maze Bank Arena",
        "The Diamond Casino & Resort",
        "The Pacific Standard Public Deposit Bank",
        "The FIB Building",
        "The Los Santos Police Department",
        "The Los Santos Fire Department",
        "The Bureau of Bureaucracy",
        "The Alamo Sea",
        "The Great Ocean Highway",
        "The Senora Freeway",
        "The Los Santos Freeway",
        "The Pacific Coast Highway",
        "The Vinewood Sign",
        "The Observatory",
        "The Chiliad Mountain State Wilderness",
        "The Raton Canyon",
        "The Sandy Shores Airfield",
        "The Fort Zancudo Military Base"
    };

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(CallScheduler::addNewCall, 0, 5, TimeUnit.SECONDS);
    }

    private static void addNewCall() {
        try (Connection connection = DatabaseManager.connect()) {
            if (connection == null) {
                System.out.println("❌ Unable to connect to the database. Exiting...");
                return; // Exit if the connection failed
            }

            // Fetch a random caller_id and name from mdt_data
            String fetchRandomCallerQuery = "SELECT cid, name FROM mdt_data ORDER BY RAND() LIMIT 1";
            try (PreparedStatement fetchStatement = connection.prepareStatement(fetchRandomCallerQuery);
                 ResultSet resultSet = fetchStatement.executeQuery()) {

                if (resultSet.next()) {
                    String cid = resultSet.getString("cid");
                    String name = resultSet.getString("name");

                    // Select a random incident type and location
                    String incidentType = getRandomIncidentType();
                    String location = getRandomLocation();

                    // Insert a new call into mdt_calls
                    String insertCallQuery = "INSERT INTO mdt_calls (caller_cid, caller_name, call_details, status) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertCallQuery)) {
                        insertStatement.setString(1, cid);
                        insertStatement.setString(2, name);
                        insertStatement.setString(3, incidentType + " at " + location); // Use the random incident type and location
                        insertStatement.setString(4, "active"); // Customize as needed
                        insertStatement.executeUpdate();
                        System.out.println("Added call for " + name + " with CID " + cid + " - Incident: " + incidentType + " at " + location);
                    }
                } else {
                    System.out.println("❌ No civilians found in mdt_data.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get a random incident type
    private static String getRandomIncidentType() {
        Random random = new Random();
        return INCIDENT_TYPES[random.nextInt(INCIDENT_TYPES.length)];
    }

    // Method to get a random location
    private static String getRandomLocation() {
        Random random = new Random();
        return LOCATIONS[random.nextInt(LOCATIONS.length)];
    }
}