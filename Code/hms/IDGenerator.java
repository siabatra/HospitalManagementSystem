package hms;
import control.*;
import boundaries.*;
import entity.*;
import java.util.Map;
import java.io.*;
import java.util.HashMap;

import entity.User.UserRoles;

/**
 * Class responsible for generating unique user IDs for different roles in the Hospital Management System (HMS).
 * IDs are stored and tracked in a CSV file to ensure uniqueness.
 */
public class IDGenerator {

    private static final String LAST_USED_ID_FILE = "lastUsedIds.csv";
    private Map<UserRoles, Integer> lastUsedIds = new HashMap<>();

    /**
     * Constructs an IDGenerator instance. Loads or initializes the ID file used to track the last used IDs.
     */
    public IDGenerator() {
        File file = new File(LAST_USED_ID_FILE);
        if (!file.exists()) {
            initializeFile();
        }
        loadLastUsedIds();
    }

    /**
     * Initializes the ID file with initial values (0) for each role type.
     * This method is called if the file doesn't exist.
     */
    private void initializeFile() {
        for (UserRoles role : UserRoles.values()) {
            lastUsedIds.put(role, 0);
        }
        saveLastUsedIds();
    }

    /**
     * Loads the last used IDs from the CSV file into the lastUsedIds map.
     * This method reads the CSV file and populates the map.
     */
    private void loadLastUsedIds() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_USED_ID_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    System.out.println("Warning: Malformed line in lastUsedIds.csv: " + line);
                    continue;
                }
                try {
                    UserRoles role = UserRoles.valueOf(parts[0].toUpperCase());
                    lastUsedIds.put(role, Integer.parseInt(parts[1]));
                } catch (IllegalArgumentException e) {
                    System.out.println("Error parsing ID for invalid role: " + parts[0]);
                } 
            }
        } catch (IOException e) {
            System.out.println("Error loading last used IDs.");
        }
    }

    /**
     * Generates a unique user ID based on the provided role.
     * Each role type has a unique prefix (e.g., 'P' for Patient).
     *
     * @param role the role for which the user ID is being generated (e.g., "PATIENT", "DOCTOR")
     * @return a unique user ID as a String, or null if the role is invalid or the ID limit is exceeded
     */
    public String generateUniqueUserID(UserRoles role) {
        char roleCode = getRoleCode(role);
        if (roleCode == ' ') return null;

        int idNumber = lastUsedIds.getOrDefault(role, 0) + 1;
        if (idNumber > 9999) return null;

        String userID = String.format("%c%04d", roleCode, idNumber);
        lastUsedIds.put(role, idNumber);

        saveLastUsedIds();
        return userID;
    }

    /**
     * Maps a role to its corresponding role code.
     *
     * @param role the role name (e.g., "PATIENT", "DOCTOR")
     * @return the role code character (e.g., 'P' for Patient), or a space if the role is invalid
     */
    private char getRoleCode(UserRoles role) {
        switch (role) {
            case PATIENT: return 'P';
            case DOCTOR: return 'D';
            case ADMIN: return 'A';
            case PHARMACIST: return 'F';
            default: return ' ';
        }
    }

    /**
     * Saves the last used IDs back to the CSV file.
     * This method updates the file to persist any changes made during runtime.
     */
    private void saveLastUsedIds() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LAST_USED_ID_FILE))) {
            for (Map.Entry<UserRoles, Integer> entry : lastUsedIds.entrySet()) {
                writer.write(entry.getKey().name() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving last used IDs.");
        }
    }
}
