package entity;
import control.*;
import boundaries.*;
import hms.*;

import java.util.*;

import control.AvailabilityService;
import hms.*;

/**
 * Represents an availability slot for a doctor in the Hospital Management System.
 * Handles creating, viewing, and managing availability slots.
 */
public class AvailabilitySlot {
    private static final String AVAILABILITY_FILE = "availability_slots.csv";
    private static final Scanner sc = new Scanner(System.in);
    private final AvailabilityService availabilityService = new AvailabilityService();

    private String dayString;
    private String timeString;
    private String doctorName;
    private String doctorId;

    /**
     * Default constructor for AvailabilitySlot.
     */
    public AvailabilitySlot() {}

    /**
     * Constructs an AvailabilitySlot for the given doctor.
     * 
     * @param doctor the doctor for whom the slot is being created
     */
    public AvailabilitySlot(Doctor doctor) {
        this.doctorName = doctor.getName();
        this.doctorId = doctor.getUserId();
    }

    /**
     * Constructs an AvailabilitySlot with specified details.
     * 
     * @param doctor the doctor for whom the slot is being created
     * @param dayString the day of the availability slot (e.g., "Monday")
     * @param timeString the time range of the availability slot (e.g., "9:00 - 9:30")
     */
    public AvailabilitySlot(Doctor doctor, String dayString, String timeString) {
        this.doctorId = doctor.getUserId();
        this.doctorName = doctor.getName();
        this.dayString = dayString;
        this.timeString = timeString;
    }

    /**
     * Gets the doctor ID.
     * 
     * @return the doctor ID
     */
    public String getDoctorId() { return doctorId; }

    /**
     * Gets the doctor name.
     * 
     * @return the doctor name
     */
    public String getDoctorName() { return doctorName; }

    /**
     * Gets the day of the availability slot.
     * 
     * @return the day as a string
     */
    public String getDayString() { return dayString; }

    /**
     * Sets the day of the availability slot.
     * 
     * @param dayString the day as a string
     */
    public void setDayString(String dayString) { this.dayString = dayString; }

    /**
     * Gets the time range of the availability slot.
     * 
     * @return the time range as a string
     */
    public String getTimeString() { return timeString; }

    /**
     * Sets the time range of the availability slot.
     * 
     * @param timeString the time range as a string
     */
    public void setTimeString(String timeString) { this.timeString = timeString; }

    /**
     * Provides the details of the availability slot.
     * 
     * @return the details of the slot as a string
     */
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Day: ").append(dayString).append(" | ");
        details.append("Time: ").append(timeString).append("\n");
        return details.toString();
    }

    /**
     * Adds an availability slot for a doctor on a specific day.
     * 
     * @param day the day number (e.g., 1 for Monday)
     * @param startTime the start time of the slot (e.g., 9.0 for 9:00 AM)
     */
    public void addAvailability(int day, double startTime) {
        setDayString(Utility.getDayString(day)); // Converts day number to day name
        String timeString = Utility.convertTimeToString(startTime, startTime + 0.5); // Convert to 30-minute slot
        setTimeString(timeString);

        availabilityService.addToFile(this);
    }

    /**
     * Displays all available slots with 30-minute intervals.
     * 
     * @return a list of available slots as arrays of strings
     */
    public static List<String[]> viewAvailabilities() {
        System.out.println("******* AVAILABLE SLOTS *******");
        List<String[]> availableSlots = CSVDataAccess.readAllRecords(AVAILABILITY_FILE);
        for (int i = 0; i < availableSlots.size(); i++) {
            String[] slot = availableSlots.get(i);
            String doctorName = slot[1];
            String day = slot[2];
            String time = slot[3];
            System.out.printf("(%d) Doctor: %s | Day: %s | Time: %s%n", i + 1, doctorName, day, time);
        }
        System.out.println("*******************************");

        return availableSlots;
    }

    /**
     * Filters availability slots based on a specified attribute.
     * 
     * @param availabilitySlots the list of slots to filter
     * @param filterAttribute the attribute to filter by (e.g., "doctorId", "dayString")
     * @param filterValue the value to match for the filter
     * @return a list of filtered availability slots
     */
    public static List<AvailabilitySlot> filterslots(List<AvailabilitySlot> availabilitySlots, String filterAttribute, String filterValue) {
        List<AvailabilitySlot> filteredAvailabilitySlots = new ArrayList<>();

        String curFilterAttributeValue = "";

        for (AvailabilitySlot slot : availabilitySlots) {
            switch (filterAttribute) {
                case "doctorId":
                    curFilterAttributeValue = slot.getDoctorId();
                    break;
                case "dayString":
                    curFilterAttributeValue = slot.getDayString();
                    break;
                default:
                    System.out.println("Invalid filterAttribute");
            }

            if (filterValue.equalsIgnoreCase(curFilterAttributeValue)) {
                filteredAvailabilitySlots.add(slot);
            }
        }

        return filteredAvailabilitySlots;
    }

    /**
     * Allows a patient to pick an available 30-minute slot.
     * 
     * @return the chosen slot as an array of strings
     */
    public static String[] pickAvailability() {
        List<String[]> availableSlots = viewAvailabilities();

        if(availableSlots.isEmpty()){
            System.out.println("No available slots yet.");
            return null;
        }

        System.out.println("Enter your choice (select a slot by number): ");
        int choice = Integer.parseInt(sc.nextLine());

        // Return the chosen slot
        return availableSlots.get(choice - 1);
    }


    /**
     * Utility class for date and time operations.
     */
    public static class Utility {
        /**
         * Converts a time range to a string with 30-minute intervals.
         * 
         * @param startTime the start time in hours (e.g., 9.0 for 9:00 AM)
         * @param endTime the end time in hours
         * @return the time range as a string
         */
        public static String convertTimeToString(double startTime, double endTime) {
            int startHour = (int) startTime;
            int startMinute = (int) ((startTime - startHour) * 60);

            int endHour = (int) endTime;
            int endMinute = (int) ((endTime - endHour) * 60);

            return String.format("%02d:%02d - %02d:%02d", startHour, startMinute, endHour, endMinute);
        }

        /**
         * Gets the day name from the day number.
         * 
         * @param day the day number (0 for Sunday, 1 for Monday, etc.)
         * @return the day name as a string
         */
        public static String getDayString(int day) {
            String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            return days[day];
        }

        /**
         * Gets the day number from the day name.
         * 
         * @param dayString the day name (e.g., "Monday")
         * @return the day number (0 for Sunday, 1 for Monday, etc.)
         */
        public static int getDay(String dayString) {
            String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            for (int i = 0; i < days.length; i++) {
                if (days[i].equalsIgnoreCase(dayString)) {
                    return i; // Returns the day index
                }
            }
            return -1; // If dayString is invalid
        }
    }

    /**
     * Adds all availability slots for a doctor on a specific day.
     * 
     * @param doctor the doctor for whom the slots are being added
     * @param dayString the day of the availability (e.g., "Monday")
     */
    public static void addAllAvailabilitySlotsForDoctor(Doctor doctor, String dayString) {
        double startTime = 9.0; // 9 AM
        double endTime = 17.0;  // 5 PM

        int day = Utility.getDay(dayString);
        if (day == -1) {
            System.out.println("Invalid day string provided.");
            return;
        }

        AvailabilitySlot availabilitySlot = new AvailabilitySlot(doctor);

        while (startTime < endTime) {
            availabilitySlot.addAvailability(day, startTime);
            startTime += 0.5; // Move to the next 30-minute slot
        }
    }

    /**
     * Displays the menu for selecting a time slot.
     */
    public static void displayTimeSlotMenu() {
        System.out.println("Pick start time:");
        System.out.println("(1) 9:00am (2) 9:30am (3) 10:00am (4) 10:30am (5) 11:00am (6) 11:30am");
        System.out.println("(7) 12:00pm (8) 12:30pm (9) 1:00pm (10) 1:30pm (11) 2:00pm (12) 2:30pm");
        System.out.println("(13) 3:00pm (14) 3:30pm (15) 4:00pm (16) 4:30pm");
    }

    /**
     * Gets the start time for a given time slot number.
     * 
     * @param slot the slot number (1-16)
     * @return the start time in hours, or -1 if the slot number is invalid
     */
    public static double getStartTimeForSlot(int slot) {
        double startTime = 0.0;

        switch (slot) {
            case 1: startTime = 9.0; break;   // 9:00am
            case 2: startTime = 9.5; break;   // 9:30am
            case 3: startTime = 10.0; break;  // 10:00am
            case 4: startTime = 10.5; break;  // 10:30am
            case 5: startTime = 11.0; break;  // 11:00am
            case 6: startTime = 11.5; break;  // 11:30am
            case 7: startTime = 12.0; break;  // 12:00pm
            case 8: startTime = 12.5; break;  // 12:30pm
            case 9: startTime = 13.0; break;  // 1:00pm
            case 10: startTime = 13.5; break; // 1:30pm
            case 11: startTime = 14.0; break; // 2:00pm
            case 12: startTime = 14.5; break; // 2:30pm
            case 13: startTime = 15.0; break; // 3:00pm
            case 14: startTime = 15.5; break; // 3:30pm
            case 15: startTime = 16.0; break; // 4:00pm
            case 16: startTime = 16.5; break; // 4:30pm
            default: System.out.println("Invalid option."); return -1; // Invalid choice
        }

        return startTime;
    }
}


