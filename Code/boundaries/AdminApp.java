package boundaries;
import entity.*;
import control.*;
import hms.*;


import java.util.List;
import java.util.Scanner;

import control.AppointmentService;
import control.MedicationService;
import control.OutcomeService;
import control.StaffService;
import entity.User.UserRoles;

/**
 * Represents the admin application for managing various hospital services.
 * Provides functionalities to manage staff, appointments, inventory, and replenishment requests.
 */
public class AdminApp {
    private Admin admin;

    private static final StaffService staffService = new StaffService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final MedicationService medicationService = new MedicationService();
    private static final OutcomeService outcomeService = new OutcomeService();
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Constructs an AdminApp instance with a specific admin.
     *
     * @param admin the admin using this application
     */
    public AdminApp(Admin admin) {
        this.admin = admin;
    }

    /**
     * Runs the admin application, displaying the menu and handling user input.
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            switch (ApplicationMenus.adminMenu()) {
                case 1 -> viewAndManageHospitalStaff();
                case 2 -> viewAppointments();
                case 3 -> viewAndManageInventory();
                case 4 -> approveReplenishmentRequests();
                case 5 -> {
                    exit = true;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Views and manages hospital staff, including adding, removing, and updating staff members.
     */
    public void viewAndManageHospitalStaff() {
        List<Staff> allStaff = staffService.getAll();
        int c = 1;
        

        System.out.println("******* HOSPITAL STAFF *******");
        for (Staff staff : allStaff)
            System.out.print("(" + c++ + ") " + staff.getDetails());
        System.out.println("******************************");

        System.out.println("(1) Add new staff member");
        System.out.println("(2) Remove current staff member");
        System.out.println("(3) Update current staff member");
        System.out.println("(4) Back to menu");

        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        switch(choice){
            case 1:
                addNewStaffMember();
                break;
            case 2:
            case 3:
                modifyStaffMember(allStaff, choice);
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input");
        }
    }

    /**
     * Views current appointments and past appointment outcomes.
     *
     * @return a list of current appointment records
     */
    public List<AppointmentRecord> viewAppointments() {
        System.out.println("******* APPOINTMENTS *******");
        System.out.println("CURRENT APPOINTMENTS:\n");

        List<AppointmentRecord> appointments = appointmentService.getAll();
        
        if(appointments.isEmpty())
            System.out.println("No current appointments.");
        else{
            for (AppointmentRecord appRecord : appointments) {
                System.out.print(appRecord.getDetails());
            }
        }

        System.out.println("\nPAST APPOINTMENT OUTCOMES:\n");
        List<AppointmentOutcome> outcomes = outcomeService.getAll();

        if(outcomes.isEmpty())
            System.out.println("No past appointment outcomes.\n");
        else{
            for (AppointmentOutcome appOutcome : outcomes) {
                System.out.print(appOutcome.getDetails());
            }
        }

        System.out.println("***************************");

        return appointments;
    }

    /**
     * Views and manages the medication inventory, including adding, removing, and updating medication.
     */
    public void viewAndManageInventory() {
        System.out.println("******* MEDICATION INVENTORY *******");
        List<Medication> inventory = medicationService.getAll();

        int i = 1;

        if(inventory.isEmpty()){
            System.out.println("No medications in inventory.");
        }
        else{
            for (Medication medicine : inventory) {
                System.out.print("(" + i++ + ") " + medicine.getDetails());
            }
        }

        System.out.println("************************************");

        System.out.println("\n(1) Add medication");
        System.out.println("(2) Remove medication");
        System.out.println("(3) Update medication");
        System.out.println("(4) STOP");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        switch(choice){
            case 1:
                addNewMedication();
                break;
            case 2:
            case 3:
                modifyMedication(inventory, choice);
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input");
        }
    }

    /**
     * Approves replenishment requests for medications that are running low in stock.
     */
    public void approveReplenishmentRequests() {
        List<Medication> inventory = medicationService.getAll();
        int count = 0;

        for (Medication medicine : inventory) {
            boolean curStatus = medicine.getReplenish();
            if (medicine.isLowStock() && curStatus) {
                count++;
                System.out.println("Replenished " + medicine.getName() + " successfully!");
                int newStock = 3 * medicine.getLowStockLevel();
                medicine.setStock(medicine.getStock() + newStock);
                medicine.setReplenish(false);
            }
        }

        if(count==0){
            System.out.println("No current replenishment requests.");
        }
    }

    /**
     * Adds a new staff member to the hospital.
     */
    private void addNewStaffMember() {
        System.out.println("******* ADD NEW STAFF *******");
        System.out.println("Enter staff member details: ");
        try {
            System.out.print("Staff Role: ");
            String role = sc.nextLine();

            role = role.toUpperCase();
            UserRoles roleEnum = UserRoles.valueOf(role);

            IDGenerator id = new IDGenerator();
            String userId = id.generateUniqueUserID(roleEnum);
            String password = "password";

            System.out.println("\nGenerated User ID: " + userId);
            System.out.println("Default password: " + password);

            System.out.print("\nEnter Staff Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Staff Gender: ");
            String gender = sc.nextLine();

            System.out.print("Enter Staff Age: ");
            
            int age=-1;
            try {
                age = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return;
            }

            Staff newStaff;
            if (roleEnum == UserRoles.DOCTOR) {
                newStaff = new Doctor(userId, password, name, gender, age, true);
            } else {
                newStaff = new Staff(userId, password, roleEnum, name, gender, age);
            }

            staffService.addToFile(newStaff);
            System.out.println(userId + " - " + name + " successfully added!");
            System.out.println("*****************************");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid input.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Modifies a staff member by either removing or updating their information.
     *
     * @param allStaff the list of all staff members
     * @param choice   the choice to either remove or update a staff member
     */
    private void modifyStaffMember(List<Staff> allStaff, int choice) {
        System.out.print("Choose staff member: ");

        int staffChoice=0;
        try {
            staffChoice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        if(staffChoice > allStaff.size() || choice < 0){
            System.out.println("Invalid input");
            return;
        }

        Staff chosenStaff = allStaff.get(staffChoice - 1);

        if (choice == 2) {
            staffService.removeFromFile(chosenStaff.getUserId(), 0);
            System.out.println("Staff member removed!");
        }
        if (choice == 3) {
            System.out.println("Chosen staff member: ");
            System.out.print(chosenStaff.getDetails());

            System.out.println("Which attribute do you wish to update?");
            System.out.println("(1) Name\n(2) Gender\n(3) Age");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    chosenStaff.setName(sc.nextLine());
                    break;

                case 2:
                    System.out.print("Enter new gender: ");
                    chosenStaff.setGender(sc.nextLine());
                    break;

                case 3:
                    System.out.print("Enter new age: ");
                    int newAge;
                    try {
                        newAge = Integer.parseInt(sc.nextLine());
                        chosenStaff.setAge(newAge);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                    break;

                default:
                    System.out.println("Invalid input");
                    return;
            }
        }
    }

    /**
     * Adds new medication to the inventory.
     */
    private void addNewMedication() {
        System.out.print("Medicine Name: ");
        String medicineName = sc.nextLine();
        System.out.print("Initial Stock: ");
        int stock = 0;

        try {
            stock = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        System.out.print("Low Stock Level: ");

        int lowStockLevel = 0;
        try {
            lowStockLevel = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        Medication newMed = new Medication(medicineName, stock, lowStockLevel, false);
        medicationService.addToFile(newMed);

        System.out.println("Medication added successfully!");
    }

    /**
     * Modifies a medication by either removing or updating its stock or low stock level.
     *
     * @param inventory the list of all medications in the inventory
     * @param choice    the choice to either remove or update a medication
     */
    private void modifyMedication(List<Medication> inventory, int choice) {
        System.out.print("Choose medication: ");

        int medChoice=0;
        try {
            medChoice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        if(medChoice > inventory.size() || choice < 0){
            System.out.println("Invalid input");
            return;
        }
        
        Medication chosenMed = inventory.get(medChoice - 1);

        if (choice == 2) {
            medicationService.removeFromFile(chosenMed.getName(), 0);
            System.out.println("Medication removed!");
        }
        if (choice == 3) {
            System.out.println("Chosen medication: ");
            System.out.print(chosenMed.getDetails());

            System.out.println("Which attribute do you wish to update?");
            System.out.println("(1) Stock\n(2) Low Stock Level");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return;
            }

            switch (choice) {
                case 1:
                    System.out.print("Change in stock (+ve: add, -ve: reduce): ");
                    int curStock = chosenMed.getStock();
                    int newStock=curStock;
                    try{
                        newStock = curStock + Integer.parseInt(sc.nextLine());
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                    chosenMed.setStock(newStock);
                    break;

                case 2:
                    System.out.print("Enter new stock level: ");
                    int newStockLevel=chosenMed.getLowStockLevel();
                    try{
                        newStockLevel = Integer.parseInt(sc.nextLine());
                    }
                    catch(NumberFormatException e){
                        System.out.println("Invalid input. Enter a valid number.");
                    }
                    chosenMed.setLowStockLevel(newStockLevel);
                    break;

                default:
                    System.out.println("Invalid input");
                    return;
            }
        }
    }
}
