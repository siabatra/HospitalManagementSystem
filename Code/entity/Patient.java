package entity;
import control.*;
import boundaries.*;
import hms.*;

import java.util.ArrayList;
import java.util.List;

import control.PatientService;

/**
 * The Patient class represents a patient in the hospital management system.
 * It extends the User class to add additional patient-specific attributes and methods.
 */
public class Patient extends User {
    public enum PatientAttribute {
        userId, password, name, gender, dateOfBirth, contactInfo, bloodType, pastDiagnoses, pastTreatments
    }

    private final PatientService patientService = new PatientService();

    private String dateOfBirth;
    private String contactInfo;
    private String bloodType;
    private List<String> pastDiagnoses;
    private List<String> pastTreatments;

    /**
     * Constructs a Patient object with the given details.
     * 
     * @param userId      the ID of the patient
     * @param password    the password of the patient
     * @param name        the name of the patient
     * @param gender      the gender of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param contactInfo the contact information of the patient
     * @param bloodType   the blood type of the patient
     */
    public Patient(String userId, String password, String name, String gender, String dateOfBirth, String contactInfo, String bloodType) {
        super(userId, password, UserRoles.PATIENT, name, gender);
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
        this.bloodType = bloodType;
        this.pastDiagnoses = new ArrayList<>();
        this.pastTreatments = new ArrayList<>();
    }

    /**
     * Constructs a Patient object with the given details, including past diagnoses and treatments.
     * 
     * @param userId        the ID of the patient
     * @param password      the password of the patient
     * @param name          the name of the patient
     * @param gender        the gender of the patient
     * @param dateOfBirth   the date of birth of the patient
     * @param contactInfo   the contact information of the patient
     * @param bloodType     the blood type of the patient
     * @param pastDiagnoses a list of past diagnoses of the patient
     * @param pastTreatments a list of past treatments of the patient
     */
    public Patient(String userId, String password, String name, String gender, String dateOfBirth, String contactInfo, String bloodType, List<String> pastDiagnoses, List<String> pastTreatments) {
        super(userId, password, UserRoles.PATIENT, name, gender);
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.pastTreatments = pastTreatments;
    }

    /**
     * Gets the date of birth of the patient.
     * 
     * @return the date of birth as a String
     */
    public String getDateOfBirth() { return dateOfBirth; }

    /**
     * Gets the contact information of the patient.
     * 
     * @return the contact information as a String
     */
    public String getContactInfo() { return contactInfo; }

    /**
     * Sets the contact information of the patient and updates the CSV file.
     * 
     * @param contactInfo the new contact information
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
        patientService.updateInFile(this, this.userId, 0);
    }

    /**
     * Sets the password of the patient and updates the CSV file.
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        super.setPassword(password);
        patientService.updateInFile(this, this.userId, 0);
    }

    /**
     * Gets the blood type of the patient.
     * 
     * @return the blood type as a String
     */
    public String getBloodType() { return bloodType; }

    /**
     * Gets the list of past diagnoses of the patient.
     * 
     * @return a list of past diagnoses
     */
    public List<String> getPastDiagnoses() { return pastDiagnoses; }

    /**
     * Adds a diagnosis to the patient's list of past diagnoses and updates the CSV file.
     * 
     * @param diagnosis the diagnosis to add
     */
    public void addDiagnosis(String diagnosis) {
        this.pastDiagnoses.add(diagnosis);
        patientService.updateInFile(this, this.userId, 0);
    }

    /**
     * Gets the list of past treatments of the patient.
     * 
     * @return a list of past treatments
     */
    public List<String> getTreatments() { return pastTreatments; }

    /**
     * Adds a treatment to the patient's list of past treatments and updates the CSV file.
     * 
     * @param treatment the treatment to add
     */
    public void addTreatment(String treatment) {
        this.pastTreatments.add(treatment);
        patientService.updateInFile(this, this.userId, 0);
    }

    /**
     * Gets the details of the patient, including their ID, name, date of birth, gender, contact info,
     * blood type, past diagnoses, and past treatments.
     * 
     * @return a formatted string representing the details of the patient
     */
    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Patient ID: ").append(userId).append("\n");
        details.append("Name: ").append(name).append("\n");
        details.append("Date of Birth: ").append(dateOfBirth).append("\n");
        details.append("Gender: ").append(gender).append("\n");
        details.append("Contact Information: ").append(contactInfo).append("\n");
        details.append("Blood Type: ").append(bloodType).append("\n");
        details.append("Past Diagnoses: ").append(pastDiagnoses).append("\n");
        details.append("Past Treatments: ").append(pastTreatments).append("\n");

        return details.toString();
    }
}
