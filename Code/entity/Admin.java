package entity;
import control.*;
import boundaries.*;
import hms.*;


/**
 * Represents an Admin in the Hospital Management System.
 * Inherits common staff properties and behaviors from the Staff class.
 */
public class Admin extends Staff {

    /**
     * Enum for Admin attributes, used for filtering or identifying Admin-specific data.
     */
    public enum AdminAttribute{
        userId, password, name, gender, age
    }

    /**
     * Constructs an Admin object with the specified details.
     * 
     * @param userId   the unique ID of the admin
     * @param password the password for the admin account
     * @param name     the name of the admin
     * @param gender   the gender of the admin
     * @param age      the age of the admin
     */
    public Admin(String userId, String password, String name, String gender, int age) {
        super(userId, password, UserRoles.ADMIN, name, gender, age);
    }

    /**
     * Provides the details of the Admin.
     * 
     * @return the details of the admin as a string
     */
    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Admin ID: ").append(userId).append(" | ");
        details.append("Name: ").append(name).append(" | ");
        details.append("Role: ").append(role).append(" | ");
        details.append("Gender: ").append(gender).append(" | ");
        details.append("Age: ").append(age).append("\n");

        return details.toString();
    }
}
