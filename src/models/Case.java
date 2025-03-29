package models;

import java.sql.Timestamp;

public class Case {
    private int id; // Unique identifier for the case
    private String caseType; // Type of case (incident, conviction, warrant)
    private String title; // Case title
    private String details; // Case details
    private String evidence; // Evidence related to the case
    private String officers; // Officers involved
    private String civilians; // Civilians involved
    private String charges; // Charges for convictions
    private int fine; // Fine amount
    private int sentence; // Sentence duration
    private Timestamp createdAt; // Creation timestamp

    // Constructor
    public Case(int id, String caseType, String title, String details,
                String evidence, String officers, String civilians,
                String charges, int fine, int sentence, Timestamp createdAt) {
        this.id = id;
        this.caseType = caseType;
        this.title = title;
        this.details = details;
        this.evidence = evidence;
        this.officers = officers;
        this.civilians = civilians;
        this.charges = charges;
        this.fine = fine;
        this.sentence = sentence;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getOfficers() {
        return officers;
    }

    public void setOfficers(String officers) {
        this.officers = officers;
    }

    public String getCivilians() {
        return civilians;
    }

    public void setCivilians(String civilians) {
        this.civilians = civilians;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public int getSentence() {
        return sentence;
    }

    public void setSentence(int sentence) {
        this.sentence = sentence;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return title; // Customize this to display what you want in the UI
    }
}