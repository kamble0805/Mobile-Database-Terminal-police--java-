package models;

public class Weapon {
    private int id;
    private String serial;
    private String owner;
    private String information;
    private String weapClass;
    private String weapModel;
    private String image;
    private String cid; // New field for CID

    // Constructor
    public Weapon(int id, String serial, String owner, String information, String weapClass, String weapModel, String image, String cid) {
        this.id = id;
        this.serial = serial;
        this.owner = owner;
        this.information = information;
        this.weapClass = weapClass;
        this.weapModel = weapModel;
        this.image = image;
        this.cid = cid; // Initialize CID
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getSerial() {
        return serial;
    }

    public String getOwner() {
        return owner;
    }

    public String getInformation() {
        return information;
    }

    public String getWeapClass() {
        return weapClass;
    }

    public String getWeapModel() {
        return weapModel;
    }

    public String getImage() {
        return image;
    }

    public String getCid() {
        return cid; // Getter for CID
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "id=" + id +
                ", serial='" + serial + '\'' +
                ", owner='" + owner + '\'' +
                ", information='" + information + '\'' +
                ", weapClass='" + weapClass + '\'' +
                ", weapModel='" + weapModel + '\'' +
                ", image='" + image + '\'' +
                ", cid='" + cid + '\'' + // Include CID in toString
                '}';
    }
}