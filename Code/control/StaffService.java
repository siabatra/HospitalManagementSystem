package control;

import java.util.ArrayList;
import java.util.List;
import entity.*;
import boundaries.*;
import hms.*;
import entity.Admin;
import entity.Doctor;
import entity.Pharmacist;
import entity.User.UserRoles;
import hms.CSVService;

/**
 * Service class for managing staff data in the Hospital Management System.
 * Extends CSVService to provide file operations for staff members.
 */
public class StaffService extends CSVService<Staff> {
    protected static final String STAFF_FILE = "staff.csv";

    /**
     * Constructs a StaffService instance.
     */
    public StaffService() {
        super(STAFF_FILE);
    }

    /**
     * Converts a Staff object to CSV format.
     *
     * @param staff the Staff object to convert
     * @return a CSV formatted string representing the Staff object
     */
    @Override
    public String objectToCSVFormat(Staff staff) {
        return String.join(",",
            staff.getUserId(),
            staff.getPassword(),
            staff.getRole().name(),
            staff.getName(),
            staff.getGender(),
            String.valueOf(staff.getAge())
        );
    }

    /**
     * Converts a CSV formatted array of fields to a Staff object.
     *
     * @param fields an array of strings representing the fields in CSV format
     * @return a Staff object constructed from the provided fields
     */
    @Override
    public Staff CSVFormatToObject(String[] fields) {
        String userId = fields[0].trim();
        String password = fields[1].trim();
        UserRoles role = UserRoles.valueOf(fields[2].trim());
        String name = fields[3].trim();
        String gender = fields[4].trim();
        int age = Integer.parseInt(fields[5].trim());

        // Create appropriate subclass instance based on role
        switch (role) {
            case DOCTOR:
                return new Doctor(userId, password, name, gender, age, true);
            case PHARMACIST:
                return new Pharmacist(userId, password, name, gender, age);
            case ADMIN:
                return new Admin(userId, password, name, gender, age);
            default:
                return new Staff(userId, password, role, name, gender, age);
        }
    }

    /**
     * Retrieves a staff member by their user ID.
     *
     * @param userId the user ID of the staff member
     * @return the Staff object if found, otherwise null
     */
    public Staff getStaffById(String userId) {
        Staff staff = getObjectByAttribute(userId, 0);

        if (staff != null) {
            // Return the specific subclass based on role
            switch (staff.getRole()) {
                case DOCTOR:
                    return (Doctor) staff;
                case PHARMACIST:
                    return (Pharmacist) staff;
                case ADMIN:
                    return (Admin) staff;
                default:
                    return staff;
            }
        }
        
        return null; // If no staff is found, return null
    }

    /**
     * Filters a list of staff members by their role.
     *
     * @param staffRecords the list of staff members to filter
     * @param role the role to filter by
     * @return a list of staff members with the specified role
     */
    public static List<Staff> filterStaffByRole(List<Staff> staffRecords, String role) {
        List<Staff> filteredStaff = new ArrayList<>();
        
        for (Staff staff : staffRecords) {
            if (staff.getRole().name().equalsIgnoreCase(role)) {
                filteredStaff.add(staff);
            }
        }
        
        return filteredStaff;
    }

    /**
     * Filters a list of staff members by their gender.
     *
     * @param staffRecords the list of staff members to filter
     * @param gender the gender to filter by
     * @return a list of staff members with the specified gender
     */
    public static List<Staff> filterStaffByGender(List<Staff> staffRecords, String gender) {
        List<Staff> filteredStaff = new ArrayList<>();
        
        for (Staff staff : staffRecords) {
            if (staff.getGender().equalsIgnoreCase(gender)) {
                filteredStaff.add(staff);
            }
        }
        
        return filteredStaff;
    }

    /**
     * Filters a list of staff members by their age.
     *
     * @param staffRecords the list of staff members to filter
     * @param age the age to filter by
     * @return a list of staff members with the specified age
     */
    public static List<Staff> filterStaffByAge(List<Staff> staffRecords, int age) {
        List<Staff> filteredStaff = new ArrayList<>();
        
        for (Staff staff : staffRecords) {
            if (staff.getAge() == age) {
                filteredStaff.add(staff);
            }
        }
        
        return filteredStaff;
    }
}
