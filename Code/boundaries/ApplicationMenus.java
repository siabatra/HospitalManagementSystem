package boundaries;
import entity.*;
import control.*;
import hms.*;
import java.util.Scanner;

/**
 * Represents the menus used in the Hospital Management System (HMS).
 * Provides various static methods to display different menus based on user roles.
 */
public class ApplicationMenus {
    static Scanner sc = new Scanner(System.in);

    /**
     * Displays the landing page menu and prompts the user to select an option.
     * 
     * @return the selected option as an integer
     */
    public static int landingPage() {
        System.out.println("\n******* HOSPITAL MANAGEMENT SYSTEM *******");
        System.out.println("(1) Login");
        System.out.println("(2) Register as New PATIENT");
        System.out.println("(0) Quit Program");
        System.out.println("******************************************");
        System.out.print("Select an option: ");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        return choice;
    }

    /**
     * Displays the patient menu and prompts the patient to select an option.
     * 
     * @return the selected option as an integer
     */
    public static int patientMenu() {
        System.out.println("\n******* PATIENT MENU *******");
        System.out.println("(1) View Medical Record");
        System.out.println("(2) Update Personal Information");
        System.out.println("(3) View Available Appointment Slots");
        System.out.println("(4) Schedule an Appointment");
        System.out.println("(5) Reschedule an Appointment");
        System.out.println("(6) Cancel an Appointment");
        System.out.println("(7) View Scheduled Appointments");
        System.out.println("(8) View Past Appointment Outcome Records");
        System.out.println("(9) Logout");
        System.out.println("****************************");
        System.out.print("Choose an option: ");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        return choice;
    }

    /**
     * Displays the doctor menu and prompts the doctor to select an option.
     * 
     * @return the selected option as an integer
     */
    public static int doctorMenu() {
        System.out.println("\n******* DOCTOR MENU *******");
        System.out.println("(1) View Patient Medical Records");
        System.out.println("(2) Update Patient Medical Records");
        System.out.println("(3) View Personal Schedule");
        System.out.println("(4) Set Availability for Appointments");
        System.out.println("(5) Accept or Decline Appointment Requests");
        System.out.println("(6) View Upcoming Appointments");
        System.out.println("(7) Record Appointment Outcome");
        System.out.println("(8) Logout");
        System.out.println("****************************");
        System.out.print("Choose an option: ");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        return choice;
    }

    /**
     * Displays the admin menu and prompts the admin to select an option.
     * 
     * @return the selected option as an integer
     */
    public static int adminMenu() {
        System.out.println("\n******* ADMIN MENU *******");
        System.out.println("(1) View & Manage Hospital Staff");
        System.out.println("(2) View Appointment Details");
        System.out.println("(3) View & Manage Medication Inventory");
        System.out.println("(4) Approve Replenishment Requests");
        System.out.println("(5) Logout");
        System.out.println("****************************");
        System.out.print("Choose an option: ");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        return choice;
    }

    /**
     * Displays the pharmacist menu and prompts the pharmacist to select an option.
     * 
     * @return the selected option as an integer
     */
    public static int pharmaMenu() {
        System.out.println("\n******* PHARMACIST MENU *******");
        System.out.println("(1) View Appointment Outcome Record");
        System.out.println("(2) Update Prescription Status");
        System.out.println("(3) View Medication Inventory");
        System.out.println("(4) Submit Replenishment Request");
        System.out.println("(5) Logout");
        System.out.println("*********************************");
        System.out.print("Choose an option: ");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        return choice;
    }
}
