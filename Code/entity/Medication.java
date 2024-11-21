package entity;
import control.MedicationService;
import hms.*;
import control.*;
import boundaries.*;
import hms.*;

/**
 * Represents a medication in the Hospital Management System (HMS) inventory.
 * Provides methods for managing stock, low stock levels, and replenishment status.
 */
public class Medication {
    private static final MedicationService medicationService = new MedicationService();

    public enum MedicationAttribute {
        name, stock, lowStockLevel
    }

    private static final String MEDICATIONS_FILE = "inventory.csv";

    private String name;
    private int stock;
    private int lowStockLevel;
    private boolean replenish;

    /**
     * Constructs a Medication object with a given name.
     * Initializes stock and low stock level by reading from the file.
     *
     * @param name the name of the medication
     */
    public Medication(String name) {
        this.name = name;
        int[] numbers = getMedicineNumbersFromFile(name);
        this.stock = numbers[0];
        this.lowStockLevel = numbers[1];
        this.replenish = false;
    }

    /**
     * Constructs a Medication object with all details.
     *
     * @param name          the name of the medication
     * @param stock         the current stock of the medication
     * @param lowStockLevel the level below which stock is considered low
     * @param replenish     the replenishment request status
     */
    public Medication(String name, int stock, int lowStockLevel, boolean replenish) {
        this.name = name;
        this.stock = stock;
        this.lowStockLevel = lowStockLevel;
        this.replenish = replenish;
    }

    /**
     * Gets the name of the medication.
     *
     * @return the name of the medication
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the medication and updates the record in the file.
     *
     * @param name the new name of the medication
     */
    public void setName(String name) {
        this.name = name;
        medicationService.updateInFile(this, this.getName(), 0);
    }

    /**
     * Gets the current stock level of the medication.
     *
     * @return the current stock level
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock level of the medication and updates the record in the file.
     *
     * @param stock the new stock level
     */
    public void setStock(int stock) {
        this.stock = stock;
        medicationService.updateInFile(this, this.getName(), 0);
    }

    /**
     * Retrieves the stock and low stock levels from the file based on the medication name.
     *
     * @param name the name of the medication
     * @return an array containing the current stock and low stock level
     */
    public int[] getMedicineNumbersFromFile(String name) {
        String[] entry = CSVDataAccess.searchRecord(MEDICATIONS_FILE, name, 0);
        int[] values = {Integer.parseInt(entry[1]), Integer.parseInt(entry[2])};
        return values;
    }

    /**
     * Gets the low stock level threshold for the medication.
     *
     * @return the low stock level threshold
     */
    public int getLowStockLevel() {
        return lowStockLevel;
    }

    /**
     * Sets the low stock level threshold and updates the record in the file.
     *
     * @param lowStockLevel the new low stock level threshold
     */
    public void setLowStockLevel(int lowStockLevel) {
        this.lowStockLevel = lowStockLevel;
        medicationService.updateInFile(this, this.getName(), 0);
    }

    /**
     * Gets the replenishment request status of the medication.
     *
     * @return true if replenishment has been requested, false otherwise
     */
    public boolean getReplenish() {
        return replenish;
    }

    /**
     * Sets the replenishment request status and updates the record in the file.
     *
     * @param replenish the new replenishment request status
     */
    public void setReplenish(boolean replenish) {
        this.replenish = replenish;
        medicationService.updateInFile(this, this.getName(), 0);
    }

    /**
     * Gets the details of the medication.
     *
     * @return a string containing the details of the medication
     */
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Medicine: ").append(name).append(" | ");
        details.append("Stock: ").append(stock).append(" | ");
        details.append("Low Stock?: ").append(isLowStock() ? "Yes" : "No").append(" | ");
        details.append("Replenishment Request Status: ").append(replenish).append("\n");

        return details.toString();
    }

    /**
     * Checks if the medication is currently in low stock.
     *
     * @return true if the current stock is less than the low stock level, false otherwise
     */
    public boolean isLowStock() {
        return stock < lowStockLevel;
    }
}

