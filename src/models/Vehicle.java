package models;

public class Vehicle {
    private int id;
    private String plate;
    private String information;
    private boolean stolen;
    private boolean code5;
    private String image;
    private int points;
    private String cid;  // Foreign key referencing mdt_data
    private String name;  // Owner's name from mdt_data

    // 🔹 Default Constructor
    public Vehicle() {}

    // 🔹 Constructor (Without Name)
    public Vehicle(int id, String plate, String information, boolean stolen, boolean code5, String image, int points, String cid) {
        this.id = id;
        this.plate = plate;
        this.information = information;
        this.stolen = stolen;
        this.code5 = code5;
        this.image = image;
        this.points = points;
        this.cid = cid;
    }

    // 🔹 Constructor (With Name)
    public Vehicle(int id, String plate, String information, boolean stolen, boolean code5, String image, int points, String cid, String name) {
        this.id = id;
        this.plate = plate;
        this.information = information;
        this.stolen = stolen;
        this.code5 = code5;
        this.image = image;
        this.points = points;
        this.cid = cid;
        this.name = name;
    }

    // 🔹 Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public String getInformation() { return information; }
    public void setInformation(String information) { this.information = information; }

    public boolean isStolen() { return stolen; }
    public void setStolen(boolean stolen) { this.stolen = stolen; }

    public boolean isCode5() { return code5; }
    public void setCode5(boolean code5) { this.code5 = code5; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }

    public String getName() { return name; }  // Changed from getOwnerName() to getName()
    public void setName(String name) { this.name = name; }  // Changed from setOwnerName() to setName()

    // 🔹 Override toString() for UI display
    @Override
    public String toString() {
        return plate + " | Name: " + name;
    }
}
