package control;
import entity.*;
import boundaries.*;
import hms.*;
import hms.CSVService;

import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentService handles the management of appointment records.
 * Provides methods for converting appointments to/from CSV format,
 * as well as filtering appointments by doctor, patient, or status.
 */
public class AppointmentService extends CSVService<AppointmentRecord> {
    protected static final String APPOINTMENTS_FILE = "appointments.csv";

    /**
     * Constructs an AppointmentService to manage appointments.
     */
    public AppointmentService() {
        super(APPOINTMENTS_FILE);
    }

    /**
     * Converts a CSV-formatted record to an AppointmentRecord object.
     * 
     * @param fields the fields of the CSV record
     * @return the AppointmentRecord object
     */
    @Override
    public AppointmentRecord CSVFormatToObject(String[] fields) {
        String appointmentId = fields[0].trim();
        String patientId = fields[1].trim();
        String doctorId = fields[2].trim();
        String appointmentDate = fields[3].trim();
        String appointmentTime = fields[4].trim();
        String status = fields[5].trim();
        
        return new AppointmentRecord(appointmentId, patientId, doctorId, appointmentDate, appointmentTime, status);
    }

    /**
     * Converts an AppointmentRecord object to CSV format.
     * 
     * @param appointmentRecord the AppointmentRecord object to convert
     * @return the appointment record in CSV format as a string
     */
    @Override
    public String objectToCSVFormat(AppointmentRecord appointmentRecord) {
        return String.join(",",
            appointmentRecord.getAppointmentId(),
            appointmentRecord.getPatientId(),
            appointmentRecord.getDoctorId(),
            appointmentRecord.getAppointmentDate(),
            appointmentRecord.getAppointmentTime(),
            appointmentRecord.getStatus()
        );
    }

    /**
     * Filters a list of appointment records by doctor ID.
     * 
     * @param appointmentRecords the list of appointment records to filter
     * @param doctorId           the doctor ID to filter by
     * @return a list of appointment records for the specified doctor
     */
    public static List<AppointmentRecord> filterAppointmentsByDoctorId(List<AppointmentRecord> appointmentRecords, String doctorId) {
        List<AppointmentRecord> filteredAppointments = new ArrayList<>();
        
        for (AppointmentRecord appointment : appointmentRecords) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctorId)) {
                filteredAppointments.add(appointment);
            }
        }
        
        return filteredAppointments;
    }

    /**
     * Filters a list of appointment records by patient ID.
     * 
     * @param appointmentRecords the list of appointment records to filter
     * @param patientId          the patient ID to filter by
     * @return a list of appointment records for the specified patient
     */
    public static List<AppointmentRecord> filterAppointmentsByPatientId(List<AppointmentRecord> appointmentRecords, String patientId) {
        List<AppointmentRecord> filteredAppointments = new ArrayList<>();
        
        for (AppointmentRecord appointment : appointmentRecords) {
            if (appointment.getPatientId().equalsIgnoreCase(patientId)) {
                filteredAppointments.add(appointment);
            }
        }
        
        return filteredAppointments;
    }

    /**
     * Filters a list of appointment records by status.
     * 
     * @param appointmentRecords the list of appointment records to filter
     * @param status             the status to filter by (e.g., "PENDING", "CONFIRMED")
     * @return a list of appointment records with the specified status
     */
    public static List<AppointmentRecord> filterAppointmentsByStatus(List<AppointmentRecord> appointmentRecords, String status) {
        List<AppointmentRecord> filteredAppointments = new ArrayList<>();
        
        for (AppointmentRecord appointment : appointmentRecords) {
            if (appointment.getStatus().equalsIgnoreCase(status)) {
                filteredAppointments.add(appointment);
            }
        }
        
        return filteredAppointments;
    }
}
