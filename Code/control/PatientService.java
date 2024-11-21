package control;
import entity.*;
import boundaries.*;
import hms.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entity.Patient;
import hms.CSVService;

/**
 * The PatientService class handles the reading, writing, and manipulation of patient data stored in CSV format.
 * It extends the CSVService class to provide specific functionality for the Patient entity.
 */
public class PatientService extends CSVService<Patient> {
    protected static final String PATIENTS_FILE = "patients.csv";

    /**
     * Constructs a PatientService instance, initializing the parent CSVService class with the patients file path.
     */
    public PatientService() {
        super(PATIENTS_FILE);
    }

    /**
     * Converts a CSV-formatted string array into a Patient object.
     * 
     * @param fields the CSV fields representing a patient
     * @return a Patient object constructed from the CSV fields
     */
    @Override
    public Patient CSVFormatToObject(String[] fields) {
        String userId = fields[0].trim();
        String password = fields[1].trim();
        String name = fields[2].trim();
        String gender = fields[3].trim();
        String dateOfBirth = fields[4].trim();
        String contactInfo = fields[5].trim();
        String bloodType = fields[6].trim();
        List<String> pastDiagnoses = new ArrayList<>();
        List<String> pastTreatments = new ArrayList<>();

        // Extract past diagnoses from the CSV, if available
        if (fields.length > 7) {
            String rawDiagnoses = fields[7].trim();
            if (rawDiagnoses.isEmpty()) {
                pastDiagnoses = new ArrayList<>();
            } else {
                String[] diagnosesArray = rawDiagnoses.split(";");
                pastDiagnoses = new ArrayList<>(Arrays.asList(diagnosesArray));
            }
        }

        // Extract past treatments from the CSV, if available
        if (fields.length > 8) {
            String rawTreatments = fields[8].trim();
            if (rawTreatments.isEmpty()) {
                pastTreatments = new ArrayList<>();
            } else {
                String[] treatmentsArray = rawTreatments.split(";");
                pastTreatments = new ArrayList<>(Arrays.asList(treatmentsArray));
            }
        }

        return new Patient(userId, password, name, gender, dateOfBirth, contactInfo, bloodType, pastDiagnoses, pastTreatments);
    }

    /**
     * Converts a Patient object into CSV format.
     * 
     * @param patient the Patient object to be converted
     * @return a CSV-formatted string representing the patient
     */
    @Override
    public String objectToCSVFormat(Patient patient) {
        return String.join(",",
            patient.getUserId(),
            patient.getPassword(),
            patient.getName(),
            patient.getGender(),
            patient.getDateOfBirth(),
            patient.getContactInfo(),
            patient.getBloodType(),
            String.join(";", patient.getPastDiagnoses()),
            String.join(";", patient.getTreatments())
        );
    }

    /**
     * Retrieves a Patient object by their unique user ID.
     * 
     * @param userId the ID of the patient to retrieve
     * @return the Patient object with the specified user ID, or null if not found
     */
    public Patient getPatientById(String userId) {
        return getObjectByAttribute(userId, 0);
    }
}
