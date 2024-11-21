package entity;
import java.util.ArrayList;
import java.util.List;
import control.*;
import boundaries.*;
import hms.*;

import control.OutcomeService;

/**
 * Represents an appointment outcome in the Hospital Management System.
 * Extends the AppointmentRecord class to include additional information related to service, consultation notes, and prescriptions.
 */
public class AppointmentOutcome extends AppointmentRecord {
    private static final OutcomeService outcomeService = new OutcomeService();

    private String service;
    private String consultationNotes;
    private List<PrescriptionEntry> prescription;

    /**
     * Constructs an AppointmentOutcome with the specified details.
     * 
     * @param appointmentId       the ID of the appointment
     * @param patientId           the ID of the patient
     * @param doctorId            the ID of the doctor
     * @param appointmentDate     the date of the appointment
     * @param appointmentTime     the time of the appointment
     * @param status              the status of the appointment
     * @param service             the service provided during the appointment
     * @param consultationNotes   the consultation notes recorded during the appointment
     */
    public AppointmentOutcome(String appointmentId, String patientId, String doctorId, String appointmentDate, String appointmentTime, String status,
                                String service, String consultationNotes) {
        super(appointmentId, patientId, doctorId, appointmentDate, appointmentTime, status);
        this.service = service;
        this.consultationNotes = consultationNotes;
        this.prescription = new ArrayList<>();
    }

    /**
     * Gets the service provided during the appointment.
     * 
     * @return the service as a string
     */
    public String getService() { return service; }

    /**
     * Sets the service provided during the appointment.
     * 
     * @param service the service as a string
     */
    public void setService(String service) { this.service = service; }

    /**
     * Gets the consultation notes recorded during the appointment.
     * 
     * @return the consultation notes as a string
     */
    public String getConsultationNotes() { return consultationNotes; }

    /**
     * Sets the consultation notes recorded during the appointment.
     * 
     * @param consultationNotes the consultation notes as a string
     */
    public void setConsultationNotes(String consultationNotes) { this.consultationNotes = consultationNotes; }

    /**
     * Gets the list of prescription entries associated with the appointment.
     * 
     * @return the list of prescription entries
     */
    public List<PrescriptionEntry> getPrescription() { return prescription; }

    /**
     * Adds a prescription entry to the appointment outcome.
     * 
     * @param prescriptionEntry the prescription entry to add
     */
    public void addPrescriptionEntry(PrescriptionEntry prescriptionEntry) {
        this.prescription.add(prescriptionEntry);
    }

    /**
     * Updates the status of all prescription entries associated with the appointment.
     * 
     * @param newStatus the new status for the prescriptions
     */
    public void updatePrescriptionStatus(String newStatus) {
        for (PrescriptionEntry entry : this.prescription) {
            entry.setStatus(newStatus);
        }
        outcomeService.updateInFile(this, this.getAppointmentId(), 0);
    }

    /**
     * Provides the details of the appointment outcome.
     * 
     * @return the details of the appointment outcome as a string
     */
    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append(super.getDetails());
        details.append("Service: ").append(service).append(" | ");
        details.append("Consultation Notes: ").append(consultationNotes).append("\n");

        if (!prescription.isEmpty()) {
            details.append("Prescription:\n");
            for (PrescriptionEntry entry : prescription) {
                details.append("- ").append(entry.getMedicationName()).append(": ");
                details.append(entry.getQuantity()).append(" (").append(entry.getStatus()).append(")\n");
            }
        }

        details.append("\n");

        return details.toString();
    }
}
