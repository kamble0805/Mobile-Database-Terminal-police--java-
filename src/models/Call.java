package models;

public class Call {
    private String callerCid;
    private String callerName;
    private String callDetails;
    private String status;
    private String timestamp; // Add timestamp field

    public Call(String callerCid, String callerName, String callDetails, String status, String timestamp) {
        this.callerCid = callerCid;
        this.callerName = callerName;
        this.callDetails = callDetails;
        this.status = status;
        this.timestamp = timestamp; // Initialize timestamp
    }

    // Getters and Setters
    public String getCallerCid() {
        return callerCid;
    }

    public void setCallerCid(String callerCid) {
        this.callerCid = callerCid;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallDetails() {
        return callDetails;
    }

    public void setCallDetails(String callDetails) {
        this.callDetails = callDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp; // Getter for timestamp
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp; // Setter for timestamp
    }
}