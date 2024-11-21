package control;
import entity.*;
import boundaries.*;
import hms.*;
import entity.AvailabilitySlot;
import entity.Doctor;
import hms.CSVService;

/**
 * Provides services related to managing availability slots for doctors.
 * Extends CSVService to handle data storage and retrieval.
 */
public class AvailabilityService extends CSVService<AvailabilitySlot> {
    protected static final String AVAILABILITY_SLOTS_FILE = "availability_slots.csv";
    private static final StaffService staffService = new StaffService();

    /**
     * Constructor for AvailabilityService.
     * Initializes the service with the specified file path.
     */
    public AvailabilityService() {
        super(AVAILABILITY_SLOTS_FILE);
    }

    /**
     * Converts an AvailabilitySlot object to a CSV-formatted string.
     * 
     * @param availabilitySlot the AvailabilitySlot object to convert
     * @return the CSV representation of the AvailabilitySlot object
     */
    @Override
    public String objectToCSVFormat(AvailabilitySlot availabilitySlot) {
        return String.join(",",
            availabilitySlot.getDoctorId(),
            availabilitySlot.getDoctorName(),
            availabilitySlot.getDayString(),
            availabilitySlot.getTimeString()
        );
    }

    /**
     * Converts a CSV-formatted string to an AvailabilitySlot object.
     * 
     * @param fields the fields in the CSV string
     * @return the AvailabilitySlot object represented by the CSV fields
     */
    @Override
    public AvailabilitySlot CSVFormatToObject(String[] fields) {
        String doctorId = fields[0].trim();
        String dayString = fields[2].trim();
        String timeString = fields[3].trim();

        Doctor doctor = (Doctor) staffService.getStaffById(doctorId);
        return new AvailabilitySlot(doctor, dayString, timeString);
    }

    /**
     * Retrieves an AvailabilitySlot object for a specific doctor ID.
     * 
     * @param doctorId the ID of the doctor
     * @return the AvailabilitySlot object for the given doctor ID, or null if not found
     */
    public AvailabilitySlot getAvailabilitySlotByDoctorId(String doctorId) {
        return getObjectByAttribute(doctorId, 0);
    }
}
