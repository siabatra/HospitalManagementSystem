package hms;
import control.*;
import boundaries.*;
import entity.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class that provides helper methods for various functionalities
 * in the Hospital Management System, such as time conversions, day conversions,
 * and password validation.
 */
public class Utility {

    /**
     * Converts time represented by indices to a string in the format "hh:mmam/pm - hh:mmam/pm".
     * The start time and end time are converted to a more readable format.
     * 
     * @param startTime the index representing the start time
     * @param endTime the index representing the end time
     * @return the time range as a formatted string
     */
    public static String convertTimeToString(int startTime, int endTime) {
        int startHours = (startTime + 1) / 2 + 8;
        int startMinutes = (startTime % 2) * 30;
        String startPeriod = (startHours >= 12) ? "pm" : "am";
        startHours = startHours % 12;
        if (startHours == 0) startHours = 12;

        String startTimeString = String.format("%d:%02d%s", startHours, startMinutes, startPeriod);

        int endHours = (endTime + 1) / 2 + 8;
        int endMinutes = ((endTime + 1) % 2) * 30;
        String endPeriod = (endHours >= 12) ? "pm" : "am";
        endHours = endHours % 12;
        if (endHours == 0) endHours = 12;

        String endTimeString = String.format("%d:%02d%s", endHours, endMinutes, endPeriod);

        return startTimeString + " - " + endTimeString;
    }

    /**
     * Converts a day number to the day name.
     * 
     * @param day the day number (e.g., 1 for Monday)
     * @return the name of the day as a string, or "Invalid day" if the day number is out of range
     */
    public static String getDayString(int day) {
        switch (day) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            default: return "Invalid day";
        }
    }

    /**
     * Validates a password based on the following criteria:
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     * - Minimum length of 8 characters
     * 
     * @param password the password to validate
     * @return true if the password matches the validation criteria, otherwise false
     */
    public static boolean validatePassword(String password) {
        // Regular expression to check for at least one uppercase, one lowercase, one digit, one special character and at least 8 characters
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$";
        
        // Pattern and matcher to validate the password
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        
        return matcher.matches(); // Returns true if the password matches the criteria
    }

}
