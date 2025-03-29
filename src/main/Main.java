package main;

import database.DatabaseManager;
import ui.LoginUI;


public class Main {
    public static void main(String[] args) {
        // ✅ Test Database Connection
        if (DatabaseManager.connect() != null) {
            System.out.println("✅ Database Connection Successful.");
        } else {
            System.out.println("❌ Database Connection Failed.");
            return; // Stop execution if DB connection fails
        }

        // ✅ Launch Swing Login Window
        new LoginUI();

    }
}