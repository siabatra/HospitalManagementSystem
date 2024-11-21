package boundaries;
import entity.*;
import control.*;
import hms.*;
import java.util.List;
import java.util.Scanner;

import control.MedicationService;
import control.OutcomeService;

/**
 * The PharmacistApp class represents the application for a pharmacist in the hospital management system.
 * It provides various options for the pharmacist to manage medications and update prescription statuses.
 */
public class PharmacistApp {
    private Pharmacist pharma;

    private static final MedicationService medicationService = new MedicationService();
    private static final OutcomeService outcomeService = new OutcomeService();
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Constructs a PharmacistApp instance for the given pharmacist.
     *
     * @param pharma the pharmacist using the application
     */
    public PharmacistApp(Pharmacist pharma) {
        this.pharma = pharma;
    }

    /**
     * Runs the pharmacist application, displaying the menu and handling user actions.
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            switch (ApplicationMenus.pharmaMenu()) {
                case 1 -> viewAppointmentOutcomeRecords();
                case 2 -> updatePrescriptionStatus();
                case 3 -> viewMedicationInventory();
                case 4 -> submitReplenishmentRequest();
                case 5 -> {
                    exit = true;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Displays all appointment outcome records.
     *
     * @return a list of appointment outcomes
     */
    private List<AppointmentOutcome> viewAppointmentOutcomeRecords() {
        System.out.println("******* APPOINTMENT OUTCOME RECORDS *******");

        List<AppointmentOutcome> outcomes = outcomeService.getAll();
        int i = 1;
        for (AppointmentOutcome appOutcome : outcomes) {
            System.out.print("(" + i++ + ") " + appOutcome.getDetails());
        }
        System.out.println("*******************************************");

        return outcomes;
    }

    /**
     * Updates the status of a prescription to "DISPENSED" if there is sufficient stock.
     */
    private void updatePrescriptionStatus() {
        System.out.println("******* UPDATE PRESCRIPTION STATUS *******");
        
        List<AppointmentOutcome> outcomes = OutcomeService.filterOutcomesByPrescriptionStatus(outcomeService.getAll(), "PENDING");
        
        if(outcomes.isEmpty()){
            System.out.println("No pending prescriptions.");
            System.out.println("******************************************");
            return;
        }

        int i = 1;
        for (AppointmentOutcome appOutcome : outcomes) {
            List<PrescriptionEntry> prescription = appOutcome.getPrescription();
            System.out.println("\n(" + i++ + ") Appointment " + appOutcome.getAppointmentId());
            if (!prescription.isEmpty()) {
                System.out.println("Prescription:");
                for (PrescriptionEntry entry : prescription) {
                    System.out.println("- " + entry.getMedicationName() + ": " + entry.getQuantity() + " (" +entry.getStatus()+")");
                }
            }
        }

        System.out.print("Choose prescription to dispense: ");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        if(choice > outcomes.size() || choice < 0){
            System.out.println("Invalid input");
            return;
        }
        System.out.println();
        AppointmentOutcome chosenOutcome = outcomes.get(choice - 1);

        List<PrescriptionEntry> chosenOutcomePrescription = chosenOutcome.getPrescription();

        for (PrescriptionEntry entry : chosenOutcomePrescription) {
            if(entry.getStatus().equalsIgnoreCase("DISPENSED")){
                System.out.println("Not Dispensed - " + entry.getMedicationName() + ": Already dispensed for Appointment " + chosenOutcome.getAppointmentId());
                continue;
            }
            Medication med = new Medication(entry.getMedicationName());
            int curStock = med.getStock();
            int dispenseQuantity = entry.getQuantity();
            if (dispenseQuantity <= curStock) {
                med.setStock(curStock - dispenseQuantity);
                chosenOutcome.updatePrescriptionStatus("DISPENSED");
                System.out.println("Dispensed - "+ entry.getMedicationName() + ": Quantity - " + dispenseQuantity + " for Appointment " + chosenOutcome.getAppointmentId());
            }
            else{
                System.out.println("Not Dispensed - "+ entry.getMedicationName() + ": No stock remaining");
            }
        }
        System.out.println("******************************************");
    }

    /**
     * Displays the current medication inventory.
     *
     * @return a list of medications in the inventory
     */
    private List<Medication> viewMedicationInventory() {
        System.out.println("******* MEDICATION INVENTORY *******");
        
        List<Medication> inventory = medicationService.getAll();

        if(inventory.isEmpty()){
            System.out.println("No medications in inventory.");
            System.out.println("************************************");
            return inventory;
        }

        int i = 1;
        for (Medication medicine : inventory) {
            System.out.print("(" + i++ + ") " + medicine.getDetails());
        }
        
        System.out.println("************************************");
        return inventory;
    }

    /**
     * Submits a replenishment request for all medications that are low in stock and have not already requested replenishment.
     */
    private void submitReplenishmentRequest() {
        System.out.println("******* SUBMIT REPLENISHMENT REQUEST *******");
        
        List<Medication> inventory = medicationService.getAll();
        int count=0;

        for (Medication medicine : inventory) {
            boolean curStatus = medicine.getReplenish();
            if (medicine.isLowStock() && !curStatus) {
                count++;
                medicine.setReplenish(true);
                System.out.println("Request sent: " + medicine.getName());
            }
        }

        if(count == 0){
            System.out.println("No requests sent.");
        }
        System.out.println("********************************************");
    }
}
