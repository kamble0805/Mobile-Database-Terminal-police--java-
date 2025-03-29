package models;

import java.sql.Timestamp;

public class ClockingRecord {
    private int id;
    private String cid; // User identifier
    private String name; // User's full name
    private String status; // Status of the user (clocked_in or clocked_out)
    private String officerStatus; // Officer's status (Available, Unavailable, Responding to Call, Break/Report Writing)
    private Timestamp clockInTime; // Time when the user clocked in
    private Timestamp clockOutTime; // Time when the user clocked out

    public ClockingRecord(int id, String cid, String name, String status, String officerStatus, Timestamp clockInTime, Timestamp clockOutTime) {
        this.id = id;
        this.cid = cid;
        this.name = name;
        this.status = status;
        this.officerStatus = officerStatus; // Initialize officer status
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getOfficerStatus() {
        return officerStatus; // Getter for officer status
    }

    public Timestamp getClockInTime() {
        return clockInTime;
    }

    public Timestamp getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(Timestamp clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public void setOfficerStatus(String officerStatus) {
        this.officerStatus = officerStatus; // Setter for officer status
    }
}