package entity;
import control.*;
import boundaries.*;
import hms.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import control.AppointmentService;
import control.OutcomeService;
import control.PatientService;

/**
 * Represents a Doctor in the Hospital Management System.
 * A Doctor is a specialized type of Staff member who can manage patients and handle appointments.
 */
public class Doctor extends Staff {
    private static final PatientService patientService = new PatientService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final OutcomeService outcomeService = new OutcomeService();
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Constructs a Doctor with the specified details.
     *
     * @param userId     the unique ID of the doctor
     * @param password   the password for the doctor
     * @param name       the name of the doctor
     * @param gender     the gender of the doctor
     * @param age        the age of the doctor
     * @param firstLogin flag indicating if it's the doctor's first login
     */
    public Doctor(String userId, String password, String name, String gender, int age, boolean firstLogin) {
        super(userId, password, UserRoles.DOCTOR, name, gender, age);
    }

    /**
     * Gets the ID of the doctor.
     *
     * @return the user ID of the doctor
     */
    public String getId() {
        return userId;
    }

    /**
     * Provides the details of the doctor.
     *
     * @return the details of the doctor as a formatted string
     */
    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Doctor ID: ").append(userId).append(" | ");
        details.append("Name: ").append(name).append(" | ");
        details.append("Role: ").append(role).append(" | ");
        details.append("Gender: ").append(gender).append(" | ");
        details.append("Age: ").append(age).append("\n");

        return details.toString();
    }

    /**
     * Retrieves a list of patients under the care of the doctor.
     *
     * @return a list of patients currently under the care of the doctor
     */
    public List<Patient> getPatientsUnderDoctorCare() {
        List<AppointmentRecord> allAppointments = appointmentService.getAll();
        List<AppointmentOutcome> allOutcomes = outcomeService.getAll();

        List<AppointmentRecord> doctorAppointments = AppointmentService.filterAppointmentsByDoctorId(allAppointments, userId);
        List<AppointmentOutcome> doctorOutcomes = OutcomeService.filterOutcomesByDoctorId(allOutcomes, userId);

        doctorAppointments.addAll(doctorOutcomes);

        List<Patient> patientsUnderDoc = new ArrayList<>();
        List<String> processedIDs = new ArrayList<>();

        for (AppointmentRecord app : doctorAppointments) {
            String patientId = app.getPatientId();
            Patient patient = patientService.getPatientById(patientId);

            if (!processedIDs.contains(patientId)) {
                patientsUnderDoc.add(patient);
                processedIDs.add(patientId);
            }
        }

        return patientsUnderDoc;
    }
}
