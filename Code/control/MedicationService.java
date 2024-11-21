package control;
import entity.*;

import java.util.ArrayList;
import java.util.List;

import boundaries.*;
import hms.*;
import entity.Medication;
import hms.CSVService;

/**
 * The MedicationService class is responsible for managing medication records in the inventory.
 * It extends the CSVService class to provide functionality for adding, updating, retrieving,
 * and deleting medication records from a CSV file.
 */
public class MedicationService extends CSVService<Medication> {
    protected static final String MEDICATIONS_FILE = "inventory.csv"; // File for storing medication records

    /**
     * Constructor for MedicationService that passes the file path to the parent CSVService class.
     */
    public MedicationService() {
        super(MEDICATIONS_FILE);
    }

    /**
     * Converts a Medication object to CSV format for storage.
     *
     * @param medication the Medication object to be converted
     * @return a string representing the medication in CSV format
     */
    @Override
    public String objectToCSVFormat(Medication medication) {
        return String.join(",",
            medication.getName(),
            String.valueOf(medication.getStock()),
            String.valueOf(medication.getLowStockLevel()),
            String.valueOf(medication.getReplenish())
        );
    }

    /**
     * Converts a CSV-formatted record (array of fields) into a Medication object.
     *
     * @param fields an array of strings representing the CSV fields
     * @return a Medication object created from the CSV fields
     */
    @Override
    public Medication CSVFormatToObject(String[] fields) {
        String name = fields[0].trim();
        int stock = Integer.parseInt(fields[1].trim());
        int lowStockLevel = Integer.parseInt(fields[2].trim());
        boolean replenish = Boolean.parseBoolean(fields[3].trim());

        return new Medication(name, stock, lowStockLevel, replenish);
    }

    /**
     * Filters medications based on their replenish request status.
     *
     * @param medications a list of Medication objects to filter
     * @param replenishRequestStatus the status of replenish request to filter by
     * @return a list of Medication objects with the specified replenish request status
     */
    public static List<Medication> filterMedicationByReplenishRequestStatus(List<Medication> medications, boolean replenishRequestStatus) {
        List<Medication> filteredMedications = new ArrayList<>();
        
        for (Medication medication : medications) {
            if (medication.getReplenish() == replenishRequestStatus) {
                filteredMedications.add(medication);
            }
        }
        
        return filteredMedications;
    }

}
