package entity;
import hms.*;
import control.*;
import boundaries.*;
import hms.*;

/**
 * The Pharmacist class represents a pharmacist in the hospital management system.
 * It extends the Staff class and includes attributes specific to pharmacists.
 */
public class Pharmacist extends Staff {

    /**
     * Enum to represent the attributes of a Pharmacist.
     */
    public enum PharmacistAttribute {
        userId, password, name, gender, age
    }

    /**
     * Constructs a Pharmacist instance with specified details.
     * 
     * @param userId   the user ID of the pharmacist
     * @param password the password for the pharmacist
     * @param name     the name of the pharmacist
     * @param gender   the gender of the pharmacist
     * @param age      the age of the pharmacist
     */
    public Pharmacist(String userId, String password, String name, String gender, int age) {
        super(userId, password, UserRoles.PHARMACIST, name, gender, age);
    }
    public String getId() {
        return userId;
    }

    /**
     * Gets the details of the pharmacist in a formatted string.
     * 
     * @return a string containing the pharmacist's details
     */
    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Pharmacist ID: ").append(userId).append(", ");
        details.append("Name: ").append(name).append(", ");
        details.append("Role: ").append(role).append(", ");
        details.append("Gender: ").append(gender).append(", ");
        details.append("Age: ").append(age).append("\n");

        return details.toString();
    }
}
