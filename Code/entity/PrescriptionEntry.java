/**
 * Represents a prescription entry for a medication.
 * Contains information about the medication, quantity, and status.
 */
package entity;
import control.*;
import boundaries.*;
import hms.*;

public class PrescriptionEntry {
    private String medicationName;
    private int quantity;
    private String status;

    /**
     * Constructs a PrescriptionEntry with the specified medication name, quantity, and status.
     *
     * @param medicationName the name of the medication
     * @param quantity the quantity of the medication prescribed
     * @param status the status of the prescription (e.g., "PENDING", "DISPENSED")
     */
    public PrescriptionEntry(String medicationName, int quantity, String status) {
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.status = status;
    }

    /**
     * Gets the name of the medication.
     *
     * @return the medication name
     */
    public String getMedicationName() { return medicationName; }

    /**
     * Sets the name of the medication.
     *
     * @param medicationName the new medication name
     */
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }

    /**
     * Gets the quantity of the medication prescribed.
     *
     * @return the quantity of the medication
     */
    public int getQuantity() { return quantity; }

    /**
     * Sets the quantity of the medication prescribed.
     *
     * @param quantity the new quantity of the medication
     */
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /**
     * Gets the status of the prescription.
     *
     * @return the prescription status
     */
    public String getStatus() { return status; }

    /**
     * Sets the status of the prescription.
     *
     * @param status the new prescription status
     */
    public void setStatus(String status) { this.status = status; }
}
