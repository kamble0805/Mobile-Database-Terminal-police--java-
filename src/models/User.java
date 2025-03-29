package models;

public class User {
    private String cid;
    private String username;
    private String password;
    private String name;
    private String dob;
    private String jobType;
    private String fingerprint;
    private String tags;
    private String information;
    private String gallery;
    private User loggedInUser ; // Declare the variable

    // 🔹 Default Constructor
    public User() {}

    // 🔹 Constructor (Without fingerprint, tags, info, gallery)
    public User(String cid, String username, String password, String name, String dob, String jobType) {
        this.cid = cid;
        this.username = username;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.jobType = jobType;
    }

    // 🔹 Constructor (With fingerprint, tags, info, gallery)
    public User(String cid, String username, String password, String name, String dob, String jobType, 
                String fingerprint, String tags, String information, String gallery) {
        this.cid = cid;
        this.username = username;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.jobType = jobType;
        this.fingerprint = fingerprint;
        this.tags = tags;
        this.information = information;
        this.gallery = gallery;
    }

    // 🔹 Getter & Setter Methods
    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getFingerprint() { return fingerprint; }
    public void setFingerprint(String fingerprint) { this.fingerprint = fingerprint; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getInformation() { return information; }
    public void setInformation(String information) { this.information = information; }

    public String getGallery() { return gallery; }
    public void setGallery(String gallery) { this.gallery = gallery; }

    // ✅ Ensure JList displays proper user details instead of "models.User@3d3406b2"
    @Override
    public String toString() {
        return name + " (CID: " + cid + ")";
    }
}
