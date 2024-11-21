package entity;
import control.StaffService;
import control.*;
import boundaries.*;
import hms.*;

/**
 * Represents a staff member in the Hospital Management System.
 * Extends the User class and adds specific details for staff members.
 */
public class Staff extends User{
    private static final StaffService staffService = new StaffService();

    /**
     * Enum representing the attributes of a staff member.
     */
    public enum StaffAttribute {
        userId, password, role, name, gender, age
    }
    
    protected int age;
    protected final static String STAFF_FILE = "staff.csv";

    /**
     * Constructs a new Staff object.
     * 
     * @param userId    the unique user ID of the staff member
     * @param password  the password for the staff member
     * @param role      the role of the staff member (e.g., Doctor, Admin)
     * @param name      the name of the staff member
     * @param gender    the gender of the staff member
     * @param age       the age of the staff member
     */
    public Staff(String userId, String password, UserRoles role, String name, String gender, int age){
        super(userId, password, role, name, gender);
        this.age = age;
    }

    /**
     * Sets the name of the staff member and updates the record in the file.
     * 
     * @param name the new name of the staff member
     */
    public void setName(String name) {
        super.setName(name);
        staffService.updateInFile(this, this.userId, 0);
    }

    /**
     * Sets the gender of the staff member and updates the record in the file.
     * 
     * @param gender the new gender of the staff member
     */
    public void setGender(String gender) {
        super.setGender(gender);
        staffService.updateInFile(this, this.userId, 0);
    }

    /**
     * Gets the age of the staff member.
     * 
     * @return the age of the staff member
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the staff member and updates the record in the file.
     * 
     * @param age the new age of the staff member
     */
    public void setAge(int age) {
        this.age = age;
        staffService.updateInFile(this, this.userId, 0);
    }

    /**
     * Sets the password of the staff member and updates the record in the file.
     * 
     * @param password the new password of the staff member
     */
    public void setPassword(String password) { 
        super.setPassword(password);
        staffService.updateInFile(this, this.userId, 0);
    }
    
    /**
     * Provides the details of the staff member.
     * 
     * @return a string containing details of the staff member
     */
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Staff ID: ").append(userId).append(", ");
        details.append("Name: ").append(name).append(", ");
        details.append("Role: ").append(role).append(", ");
        details.append("Gender: ").append(gender).append(", ");
        details.append("Age: ").append(age).append("\n");

        return details.toString();
    }
}
