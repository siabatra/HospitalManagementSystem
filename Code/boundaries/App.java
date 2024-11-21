package boundaries;
import control.AuthenticationService;
import entity.*;
import control.*;
import hms.*;

import entity.Admin;
import entity.Doctor;
import entity.Patient;
import entity.Pharmacist;
import entity.User;

/**
 * The main entry point for the Hospital Management System (HMS).
 * This class handles the initial user login and navigation based on user roles.
 */
public class App {
    
    /**
     * The main method to run the Hospital Management System.
     * Displays a welcome message, handles user login, and directs users to their respective interfaces.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("** Welcome to the Hospital Management System (HMS) **\n");

        boolean exitApp = false;

        while (!exitApp) {
            boolean isLoggedIn = false;
            User loggedInUser = null;

            // Loop until the user is successfully logged in
            while (!isLoggedIn) {
                switch (ApplicationMenus.landingPage()) {
                    case 1:
                        loggedInUser = AuthenticationService.login();
                        if (loggedInUser != null) {
                            isLoggedIn = true;
                        }
                        break;

                    case 2:
                        AuthenticationService.registerPatient();
                        break;

                    case 0:
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }

            // Direct the logged-in user to the appropriate application based on their role
            if (loggedInUser instanceof Patient) {
                PatientApp patientApp = new PatientApp((Patient) loggedInUser);
                patientApp.run();
            } else if (loggedInUser instanceof Doctor) {
                handleDoctorPasswordReset((Doctor) loggedInUser);
                DoctorApp doctorApp = new DoctorApp((Doctor) loggedInUser);
                doctorApp.run();
            } else if (loggedInUser instanceof Admin) {
                AdminApp adminApp = new AdminApp((Admin) loggedInUser);
                adminApp.run();
            } else if (loggedInUser instanceof Pharmacist) {
                handlePharmacistPasswordReset((Pharmacist) loggedInUser);
                PharmacistApp pharmacistApp = new PharmacistApp((Pharmacist) loggedInUser);
                pharmacistApp.run();
            } else {
                System.out.println("Unknown role. Exiting application.");
            }
        }

        System.out.println("Thank you for using the HMS system.");
    }

    /**
     * Handles the password reset requirement for a doctor user with a default password.
     * Prompts the doctor to set a new password if the current password is still set to the default.
     * 
     * @param doctor the doctor user whose password needs to be reset
     */
    private static void handleDoctorPasswordReset(Doctor doctor) {
        if (doctor.getPassword().equals("password")) {
            System.out.println("\nYour password is set to the default. You must reset it before proceeding.");

            boolean passwordReset = false;
            while (!passwordReset) {
                String newPassword = AuthenticationService.promptNewPassword();
                if (!newPassword.equals("password")) {
                    doctor.setPassword(newPassword);
                    AuthenticationService.updatePasswordInDatabase(doctor); // Assuming a method to persist password changes
                    passwordReset = true;
                    System.out.println("Password successfully reset.");
                } else {
                    System.out.println("New password cannot be the same as the default. Please try again.");
                }
            }
        }
    }
        private static void handlePharmacistPasswordReset(Pharmacist pharmacist) {
            if (pharmacist.getPassword().equals("password")) {
                System.out.println("\nYour password is set to the default. You must reset it before proceeding.");
    
                boolean passwordReset = false;
                while (!passwordReset) {
                    String newPassword = AuthenticationService.promptNewPassword();
                    if (!newPassword.equals("p")) {
                        pharmacist .setPassword(newPassword);
                        AuthenticationService.updatePasswordInDatabase(pharmacist); 
                        passwordReset = true;
                        System.out.println("Password successfully reset.");
                    } else {
                        System.out.println("New password cannot be the same as the default. Please try again.");
                    }
                }
            }
    }
}
