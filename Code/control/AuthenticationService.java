package control;
import entity.*;
import boundaries.*;
import hms.*;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import entity.Doctor;
import entity.Patient;
import entity.Pharmacist;
import entity.Staff;
import entity.User;
import entity.User.UserRoles;
import hms.CSVDataAccess;
import hms.IDGenerator;

/**
 * Provides authentication and registration functionalities for users in the Hospital Management System.
 */
public class AuthenticationService {
    private static final PatientService patientService = new PatientService();
    private static final StaffService staffService = new StaffService();
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Updates the password of a given doctor in the database.
     * 
     * @param doctor the doctor whose password is to be updated
     */
    public static void updatePasswordInDatabase(Doctor doctor) {
        final String STAFF_FILE = "staff.csv"; // File where staff data is stored

        try {
            List<String[]> allRecords = CSVDataAccess.readAllRecords(STAFF_FILE);
            boolean updated = false;

            // Iterate through records to find the matching doctor
            for (String[] record : allRecords) {
                if (record[0].equals(doctor.getId())) { // Assuming record[0] is the ID
                    record[1] = doctor.getPassword(); // Assuming record[1] is the password
                    updated = true;
                    break;
                }
            }

            if (updated) {
                CSVDataAccess.writeAllRecords(STAFF_FILE, allRecords);
                System.out.println("Password updated successfully in the database for Doctor ID: " + doctor.getId());
            } else {
                System.out.println("Doctor not found in the database.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the password: " + e.getMessage());
        }
    }

    public static void updatePasswordInDatabase(Pharmacist pharmacist) {
        final String STAFF_FILE = "staff.csv"; // File where staff data is stored

        try {
            List<String[]> allRecords = CSVDataAccess.readAllRecords(STAFF_FILE);
            boolean updated = false;

            // Iterate through records to find the matching doctor
            for (String[] record : allRecords) {
                if (record[0].equals(pharmacist.getId())) { // Assuming record[0] is the ID
                    record[1] = pharmacist.getPassword(); // Assuming record[1] is the password
                    updated = true;
                    break;
                }
            }

            if (updated) {
                CSVDataAccess.writeAllRecords(STAFF_FILE, allRecords);
                System.out.println("Password updated successfully in the database for Doctor ID: " + pharmacist.getId());
            } else {
                System.out.println("Doctor not found in the database.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the password: " + e.getMessage());
        }
    }

    /**
     * Prompts the user to enter a new password.
     * 
     * @return the new password entered by the user
     */
    public static String promptNewPassword() {
        System.out.println("Your password must meet the following criteria:");
        System.out.println("- At least one uppercase letter");
        System.out.println("- At least one lowercase letter");
        System.out.println("- At least one digit");
        System.out.println("- At least 8 characters long");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your new password: ");
        return scanner.nextLine();
    }

    /**
     * Authenticates a user based on their user ID and password.
     * 
     * @param userId   the user ID to authenticate
     * @param password the password to verify
     * @return the authenticated User object if successful, or null if authentication fails
     */
    public User authenticate(String userId, String password) {
        char roleChar = userId.charAt(0);

        switch (roleChar) {
            case 'P':
                Patient patient = patientService.getPatientById(userId);
                if (patient != null && password.equals(patient.getPassword())) {
                    System.out.println("Patient authentication successful!");
                    return patient;
                }
                break;

            case 'D':
            case 'A':
            case 'F':
                Staff staff = staffService.getStaffById(userId);
                if (staff != null && password.equals(staff.getPassword())) {
                    System.out.println("Staff authentication successful!");
                    return staff;
                }
                break;

            default:
                System.out.println("Invalid user role.");
                break;
        }

        return null;
    }

    /**
     * Handles user login by prompting for user ID and password.
     * 
     * @return the authenticated User object if successful, or null if login fails
     */
    public static User login() {
        AuthenticationService as = new AuthenticationService();

        System.out.println("******* USER LOGIN *******");
        System.out.print("Enter User ID: ");
        String userId = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = as.authenticate(userId, password);
        if (user != null) {
            System.out.println("Login successful! Role: " + user.getRole());
            return user;
        } else {
            System.out.println("Invalid credentials. Please try again.");
            return null;
        }
    }

    /**
     * Registers a new patient by collecting required information and saving it to the system.
     */
    public static void registerPatient() {
        System.out.println("******* PATIENT REGISTRATION *******\n");
        IDGenerator idg = new IDGenerator();

        String newUserID = idg.generateUniqueUserID(UserRoles.PATIENT);
        System.out.println("User ID generated: " + newUserID);

        // Validate password with rules
        String password;
        while (true) {
            System.out.print("\nEnter Password: ");
            password = sc.nextLine();
            if (Utility.validatePassword(password)) {
                break;
            } else {
                System.out.println("Invalid password. It must contain at least:");
                System.out.println("- One uppercase letter");
                System.out.println("- One lowercase letter");
                System.out.println("- One digit");
                System.out.println("- One special character");
                System.out.println("- At least 8 characters in length");
            }
        }

        System.out.print("\nEnter Name: ");
        String name = sc.nextLine();
        System.out.print("\nEnter Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = sc.nextLine();
        System.out.print("\nEnter Gender: ");
        String gender = sc.nextLine();
        System.out.print("\nEnter Contact Information (e.g., phone number, email): ");
        String contactInfo = sc.nextLine();
        System.out.print("\nEnter Blood Type: ");
        String bloodType = sc.nextLine();

        Patient newPatient = new Patient(newUserID, password, name, gender, dateOfBirth, contactInfo, bloodType);
        patientService.addToFile(newPatient);
        System.out.println("Patient registered successfully!");
    }

    /**
     * Utility class for validating input, such as passwords.
     */
    public static class Utility {
        /**
         * Validates a password based on specific rules.
         * The password must contain:
         * - At least one uppercase letter
         * - At least one lowercase letter
         * - At least one digit
         * - At least one special character
         * - A minimum of 8 characters
         * 
         * @param password the password to validate
         * @return true if the password is valid, false otherwise
         */
        public static boolean validatePassword(String password) {
            String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
            Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(password).matches();
        }
    }
}
