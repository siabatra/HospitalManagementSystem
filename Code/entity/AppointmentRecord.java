package entity;
import control.AppointmentService;
import control.*;
import boundaries.*;
import hms.*;
/**
 * Represents an appointment record in the Hospital Management System.
 * Stores details such as appointment ID, patient ID, doctor ID, appointment date, time, and status.
 */
public class AppointmentRecord {

    /**
     * Enumeration of appointment attributes.
     */
    public enum AppointmentAttribute {
        appointmentId, patientId, doctorId, appointmentDate, status
    }

    /**
     * Enumeration of appointment status values.
     */
    public enum AppointmentStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    protected static final String APPOINTMENTS_FILE = "appointments.csv";
    private final AppointmentService appointmentService = new AppointmentService();

    protected String appointmentId;
    protected String patientId;
    protected String doctorId;
    protected String appointmentDate;
    protected String appointmentTime;
    protected String status;

    /**
     * Constructs an AppointmentRecord with the specified details.
     * 
     * @param appointmentId    the ID of the appointment
     * @param patientId        the ID of the patient
     * @param doctorId         the ID of the doctor
     * @param appointmentDate  the date of the appointment
     * @param appointmentTime  the time of the appointment
     * @param status           the status of the appointment
     */
    public AppointmentRecord(String appointmentId, String patientId, String doctorId, String appointmentDate, String appointmentTime, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    /**
     * Gets the appointment ID.
     * 
     * @return the appointment ID as a string
     */
    public String getAppointmentId() { return appointmentId; }

    /**
     * Sets the appointment ID.
     * 
     * @param appointmentId the appointment ID as a string
     */
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    /**
     * Gets the patient ID.
     * 
     * @return the patient ID as a string
     */
    public String getPatientId() { return patientId; }

    /**
     * Sets the patient ID.
     * 
     * @param patientId the patient ID as a string
     */
    public void setPatientId(String patientId) { this.patientId = patientId; }

    /**
     * Gets the doctor ID.
     * 
     * @return the doctor ID as a string
     */
    public String getDoctorId() { return doctorId; }

    /**
     * Sets the doctor ID.
     * 
     * @param doctorId the doctor ID as a string
     */
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    /**
     * Gets the appointment date.
     * 
     * @return the appointment date as a string
     */
    public String getAppointmentDate() { return appointmentDate; }

    /**
     * Sets the appointment date.
     * 
     * @param appointmentDate the appointment date as a string
     */
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    /**
     * Gets the appointment time.
     * 
     * @return the appointment time as a string
     */
    public String getAppointmentTime() { return appointmentTime; }

    /**
     * Sets the appointment time.
     * 
     * @param appointmentTime the appointment time as a string
     */
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    /**
     * Gets the status of the appointment.
     * 
     * @return the status as a string
     */
    public String getStatus() { return status; }

    /**
     * Sets the status of the appointment and updates it in the file.
     * 
     * @param status the new status as a string
     */
    public void setStatus(String status) {
        this.status = status;
        appointmentService.updateInFile(this, this.appointmentId, 0);
    }

    /**
     * Provides the details of the appointment record.
     * 
     * @return the details of the appointment as a string
     */
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Appointment ID: ").append(appointmentId).append(" | ");
        details.append("Patient ID: ").append(patientId).append(" | ");
        details.append("Doctor ID: ").append(doctorId).append(" | ");
        details.append("Date: ").append(appointmentDate).append(" | ");
        details.append("Time: ").append(appointmentTime).append(" | ");
        details.append("Status: ").append(status).append("\n");

        return details.toString();
    }
}
