package control;
import entity.*;
import boundaries.*;
import hms.*;
import hms.CSVService;

import java.util.ArrayList;
import java.util.List;

/**
 * The OutcomeService class is responsible for managing appointment outcome records.
 * It extends the CSVService class to provide functionality for adding, updating,
 * retrieving, and deleting appointment outcome records from a CSV file.
 */
public class OutcomeService extends CSVService<AppointmentOutcome> {
    protected static final String OUTCOME_FILE = "outcomes.csv"; // File for storing appointment outcome records
    private static final AppointmentService appointmentService = new AppointmentService();

    /**
     * Constructor for OutcomeService that passes the file path to the parent CSVService class.
     */
    public OutcomeService() {
        super(OUTCOME_FILE);
    }

    /**
     * Converts an AppointmentOutcome object to CSV format for storage.
     *
     * @param outcome the AppointmentOutcome object to be converted
     * @return a string representing the appointment outcome in CSV format
     */
    @Override
    public String objectToCSVFormat(AppointmentOutcome outcome) {
        // Build prescription entries part of the CSV
        StringBuilder prescriptionCSV = new StringBuilder();
        for (PrescriptionEntry p : outcome.getPrescription()) {
            if (prescriptionCSV.length() > 0) {
                prescriptionCSV.append(";");
            }
            prescriptionCSV.append(p.getMedicationName())
                            .append(":")
                            .append(p.getQuantity())
                            .append(":")
                            .append(p.getStatus());
        }

        return appointmentService.objectToCSVFormat(outcome) + "," +
            outcome.getService() + "," +
            outcome.getConsultationNotes() + "," +
            prescriptionCSV.toString();
    }

    /**
     * Converts a CSV-formatted record (array of fields) into an AppointmentOutcome object.
     *
     * @param fields an array of strings representing the CSV fields
     * @return an AppointmentOutcome object created from the CSV fields
     */
    @Override
    public AppointmentOutcome CSVFormatToObject(String[] fields) {
        String appointmentId = fields[0].trim();
        String patientId = fields[1].trim();
        String doctorId = fields[2].trim();
        String appointmentDate = fields[3].trim();
        String appointmentTime = fields[4].trim();
        String status = fields[5].trim();
        String service = fields[6].trim();
        String consultationNotes = fields[7].trim();

        AppointmentOutcome outcome = new AppointmentOutcome(
            appointmentId, patientId, doctorId, appointmentDate, appointmentTime, status,
            service, consultationNotes
        );

        // Parsing prescription entries if available
        if (fields.length > 8) {
            String[] prescriptionEntries = fields[8].split(";");
            for (String entry : prescriptionEntries) {
                String[] prescriptionDetails = entry.split(":");
                if (prescriptionDetails.length == 3) {
                    String medicationName = prescriptionDetails[0].trim();
                    int quantity = Integer.parseInt(prescriptionDetails[1].trim());
                    String prescriptionStatus = prescriptionDetails[2].trim();
                    outcome.addPrescriptionEntry(new PrescriptionEntry(medicationName, quantity, prescriptionStatus));
                }
            }
        }

        return outcome;
    }

    /**
     * Retrieves all AppointmentOutcome records from the CSV file.
     *
     * @return a list of all AppointmentOutcome records
     */
    public List<AppointmentOutcome> getAllOutcomes() {
        return getAll();
    }

    /**
     * Retrieves an AppointmentOutcome by appointment ID.
     *
     * @param appointmentId the ID of the appointment
     * @return the AppointmentOutcome with the specified appointment ID, or null if not found
     */
    public AppointmentOutcome getOutcomeByAppointmentId(String appointmentId) {
        return getObjectByAttribute(appointmentId, 0); // Assuming appointmentId is stored in the first column
    }

    /**
     * Filters appointment outcomes by patient ID.
     *
     * @param appointmentOutcomes the list of appointment outcomes to filter
     * @param patientId the ID of the patient to filter by
     * @return a list of filtered AppointmentOutcome records for the specified patient
     */
    public static List<AppointmentOutcome> filterOutcomesByPatientId(List<AppointmentOutcome> appointmentOutcomes, String patientId) {
        List<AppointmentOutcome> filteredOutcomes = new ArrayList<>();
        for (AppointmentOutcome outcome : appointmentOutcomes) {
            if (outcome.getPatientId().equalsIgnoreCase(patientId)) {
                filteredOutcomes.add(outcome);
            }
        }
        return filteredOutcomes;
    }

    /**
     * Filters appointment outcomes by doctor ID.
     *
     * @param appointmentOutcomes the list of appointment outcomes to filter
     * @param doctorId the ID of the doctor to filter by
     * @return a list of filtered AppointmentOutcome records for the specified doctor
     */
    public static List<AppointmentOutcome> filterOutcomesByDoctorId(List<AppointmentOutcome> appointmentOutcomes, String doctorId) {
        List<AppointmentOutcome> filteredOutcomes = new ArrayList<>();
        for (AppointmentOutcome outcome : appointmentOutcomes) {
            if (outcome.getDoctorId().equalsIgnoreCase(doctorId)) {
                filteredOutcomes.add(outcome);
            }
        }
        return filteredOutcomes;
    }

    /**
     * Filters appointment outcomes by status.
     *
     * @param appointmentOutcomes the list of appointment outcomes to filter
     * @param status the status to filter by (e.g., "COMPLETED")
     * @return a list of filtered AppointmentOutcome records with the specified status
     */
    public static List<AppointmentOutcome> filterOutcomesByStatus(List<AppointmentOutcome> appointmentOutcomes, String status) {
        List<AppointmentOutcome> filteredOutcomes = new ArrayList<>();
        for (AppointmentOutcome outcome : appointmentOutcomes) {
            if (outcome.getStatus().equalsIgnoreCase(status)) {
                filteredOutcomes.add(outcome);
            }
        }
        return filteredOutcomes;
    }

    public static List<AppointmentOutcome> filterOutcomesByPrescriptionStatus(List<AppointmentOutcome> appointmentOutcomes, String prescriptionStatus) {
        List<AppointmentOutcome> filteredOutcomes = new ArrayList<>();
        
        for (AppointmentOutcome outcome : appointmentOutcomes) {
            for (PrescriptionEntry prescription : outcome.getPrescription()) {
                if (prescription.getStatus().equalsIgnoreCase(prescriptionStatus)) {
                    filteredOutcomes.add(outcome);
                    break; 
                }
            }
        }
        
        return filteredOutcomes;
    }
    
}
