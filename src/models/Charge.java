package models;

public class Charge {
    private int id;
    private String description;
    private String severity;
    private String penalCode;
    private String notes;

    public Charge(int id, String description, String severity, String penalCode, String notes) {
        this.id = id;
        this.description = description;
        this.severity = severity;
        this.penalCode = penalCode;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }

    public String getPenalCode() {
        return penalCode;
    }

    public String getNotes() {
        return notes;
    }
}
