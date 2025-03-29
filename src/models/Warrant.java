package models;

import java.sql.Timestamp;

public class Warrant {
    private int id;
    private String civilianName;  // Changed from civilianCid to civilianName
    private String charges;
    private String officerName;   // Changed from officerCid to officerName
    private String status;
    private Timestamp createdAt;

    // ✅ Constructor
    public Warrant(int id, String civilianName, String charges, String officerName, String status, Timestamp createdAt) {
        this.id = id;
        this.civilianName = civilianName;
        this.charges = charges;
        this.officerName = officerName;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ✅ Getters & Setters
    public int getId() { return id; }
    public String getCivilianName() { return civilianName; }
    public String getCharges() { return charges; }
    public String getOfficerName() { return officerName; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
}
