package hms;
import control.*;
import boundaries.*;
import entity.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for providing CSV data access functionality to derived classes.
 * This class offers methods to add, update, remove, and retrieve objects from a CSV file.
 *
 * @param <T> the type of the objects to be managed
 */
public abstract class CSVService<T> {
    protected String filePath;

    /**
     * Constructs a CSVService with the specified file path.
     *
     * @param filePath the path of the CSV file to operate on
     */
    public CSVService(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Adds an object to the CSV file.
     *
     * @param obj the object to add
     */
    public void addToFile(T obj) {
        String data = objectToCSVFormat(obj);
        CSVDataAccess.addRecord(filePath, data);
    }

    // public void addToFile(String[] entry) {
    //     String data = String.join(",", entry);
    //     CSVDataAccess.addRecord(filePath, data);
    // }

    /**
     * Updates an existing record in the CSV file.
     *
     * @param obj            the object to update
     * @param identifier     the unique identifier of the record to update
     * @param identifierIndex the index of the identifier in the CSV fields
     */
    public void updateInFile(T obj, String identifier, int identifierIndex) {
        String data = objectToCSVFormat(obj);
        CSVDataAccess.updateRecord(filePath, identifier, identifierIndex, data);
    }

    /**
     * Removes a record from the CSV file.
     *
     * @param identifier     the unique identifier of the record to remove
     * @param identifierIndex the index of the identifier in the CSV fields
     */
    public void removeFromFile(String identifier, int identifierIndex) {
        CSVDataAccess.removeRecord(filePath, identifier, identifierIndex);
    }

    /**
     * Removes a record from the CSV file using an entry array.
     *
     * @param entry an array of strings representing the record to be removed
     */
    public void removeFromFile(String[] entry) {
        CSVDataAccess.removeRecord(filePath, entry);
    }

    /**
     * Retrieves all records from the CSV file and converts them to objects.
     *
     * @return a list of objects representing all records in the CSV file
     */
    public List<T> getAll() {
        List<String[]> records = CSVDataAccess.readAllRecords(filePath);
        List<T> allRecords = new ArrayList<>();
        
        for (String[] record : records) {
            allRecords.add(CSVFormatToObject(record));
        }

        return allRecords;
    }

    /**
     * Converts CSV-formatted fields to an object.
     * Subclasses must implement this method to define how to convert CSV data to an object.
     *
     * @param fields the fields of the CSV record
     * @return an object of type T
     */
    public abstract T CSVFormatToObject(String[] fields);

    /**
     * Converts an object to a CSV-formatted string.
     * Subclasses must implement this method to define how to convert an object to CSV format.
     *
     * @param obj the object to convert
     * @return the CSV-formatted string representing the object
     */
    public abstract String objectToCSVFormat(T obj);

    /**
     * Retrieves an object by searching for a specific attribute in the CSV file.
     *
     * @param attributeValue the value of the attribute to search for
     * @param attributeIndex the index of the attribute in the CSV fields
     * @return the object if found, otherwise null
     */
    public T getObjectByAttribute(String attributeValue, int attributeIndex) {
        String[] record = CSVDataAccess.searchRecord(filePath, attributeValue, attributeIndex);
        return (record != null) ? CSVFormatToObject(record) : null;
    }
}
