package database;

import models.Call;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CallDAO {

    // Method to insert a new call into the database
    public static void insertCall(Call call) {
        String insertCallQuery = "INSERT INTO mdt_calls (caller_cid, caller_name, call_details, status, timestamp) VALUES (?, ?, ?, ?, NOW())";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertCallQuery)) {
            preparedStatement.setString(1, call.getCallerCid());
            preparedStatement.setString(2, call.getCallerName());
            preparedStatement.setString(3, call.getCallDetails());
            preparedStatement.setString(4, call.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to load recent calls from the last 15 minutes
    public static List<Call> loadRecentCalls() {
        List<Call> recentCalls = new ArrayList<>();
        String loadRecentCallsQuery = "SELECT caller_cid, caller_name, call_details, status, timestamp FROM mdt_calls WHERE timestamp >= NOW() - INTERVAL 15 MINUTE";

        try (Connection connection = DatabaseManager.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(loadRecentCallsQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String callerCid = resultSet.getString("caller_cid");
                String callerName = resultSet.getString("caller_name");
                String callDetails = resultSet.getString("call_details");
                String status = resultSet.getString("status");
                String timestamp = resultSet.getString("timestamp"); // Get the timestamp

                // Create a Call object with all the necessary details
                Call call = new Call(callerCid, callerName, callDetails, status, timestamp);
                recentCalls.add(call);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recentCalls;
    }
}