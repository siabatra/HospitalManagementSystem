package hms;
import control.*;
import boundaries.*;
import entity.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for accessing and managing CSV data.
 * Provides methods for reading, writing, updating, and removing records in a CSV file.
 */
public class CSVDataAccess {

    /**
     * Reads all records from a CSV file.
     * 
     * @param filePath the path of the CSV file to read
     * @return a list of records, where each record is an array of strings
     */
    public static List<String[]> readAllRecords(String filePath) {
        List<String[]> records = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                records.add(fields);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
        
        return records;
    }

    /**
     * Writes all records to a CSV file, overwriting the existing file.
     * 
     * @param filePath the path of the CSV file to write to
     * @param records  the list of records to write, where each record is an array of strings
     */
    public static void writeAllRecords(String filePath, List<String[]> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] fields : records) {
                bw.write(String.join(",", fields));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Updates a record in a CSV file by first removing the existing record and then adding the new one.
     * 
     * @param filePath        the path of the CSV file to update
     * @param identifier      the unique identifier of the record to update
     * @param identifierIndex the index of the identifier in the record array
     * @param line            the new record as a comma-separated string
     */
    public static void updateRecord(String filePath, String identifier, int identifierIndex, String line) {
        removeRecord(filePath, identifier, identifierIndex);
        addRecord(filePath, line);
    }

    /**
     * Adds a new record to a CSV file.
     * 
     * @param filePath the path of the CSV file to write to
     * @param fields   the record fields to add as an array of strings
     */
    public static void addRecord(String filePath, String[] fields) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(String.join(",", fields));
            bw.newLine();
            // System.out.println("\nRecord added successfully!");
        } catch (IOException e) {
            // System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Adds a new record to a CSV file.
     * 
     * @param filePath the path of the CSV file to write to
     * @param line     the record to add as a comma-separated string
     */
    public static void addRecord(String filePath, String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(line);
            bw.newLine();
            // System.out.println("\nRecord added successfully!");
        } catch (IOException e) {
            // System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Removes a record from a CSV file based on an identifier.
     * 
     * @param filePath        the path of the CSV file
     * @param identifier      the unique identifier of the record to remove
     * @param identifierIndex the index of the identifier in the record array
     * @return true if the record was successfully removed, false otherwise
     */
    public static boolean removeRecord(String filePath, String identifier, int identifierIndex) {
        List<String[]> records = readAllRecords(filePath);
        boolean removed = false;

        List<String[]> updatedRecords = new ArrayList<>();
        for (String[] record : records) {
            if (record.length > identifierIndex && !record[identifierIndex].equals(identifier)) {
                updatedRecords.add(record);
            } else if (record.length > identifierIndex && record[identifierIndex].equals(identifier)) {
                removed = true;
            }
        }

        if (removed) {
            writeAllRecords(filePath, updatedRecords);
        }

        return removed;
    }

    /**
     * Removes a record from a CSV file based on a complete entry match.
     * 
     * @param filePath the path of the CSV file
     * @param entry    the record to remove as an array of strings
     * @return true if the record was successfully removed, false otherwise
     */
    public static boolean removeRecord(String filePath, String[] entry) {
        List<String[]> records = readAllRecords(filePath);
        boolean removed = false;

        List<String[]> updatedRecords = new ArrayList<>();

        for (String[] record : records) {
            boolean match = true;
            for (int i = 0; i < entry.length; i++) {
                if (!(entry[i].equalsIgnoreCase(record[i]))) {
                    match = false;
                    break;
                }
            }
            if (!match) {
                updatedRecords.add(record);
            } else {
                removed = true;
            }
        }

        if (removed) {
            writeAllRecords(filePath, updatedRecords);
        }

        return removed;
    }

    /**
     * Searches for a record in a CSV file based on an identifier.
     * 
     * @param filePath        the path of the CSV file
     * @param identifier      the unique identifier to search for
     * @param identifierIndex the index of the identifier in the record array
     * @return the record as an array of strings if found, null otherwise
     */
    public static String[] searchRecord(String filePath, String identifier, int identifierIndex) {
        List<String[]> records = readAllRecords(filePath);
        String[] result = null;

        for (String[] record : records) {
            if (record.length > identifierIndex && record[identifierIndex].equalsIgnoreCase(identifier)) {
                return record;
            }
        }

        return result;
    }
}
