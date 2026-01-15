// File: src/i2jp/oop/CsvFormatException.java
// Custom checked exception for CSV format errors
package i2jp.oop;

/**
 * Exception thrown when CSV data is malformed or invalid.
 * This is a checked exception that separates CSV format errors
 * from general I/O errors (IOException).
 */
public class CsvFormatException extends Exception {

    /**
     * Creates a new CsvFormatException with the specified detail message.
     * 
     * @param message the detail message explaining the format error
     */
    public CsvFormatException(String message) {
        super(message);
    }

    /**
     * Creates a new CsvFormatException with the specified detail message
     * and cause.
     * 
     * @param message the detail message explaining the format error
     * @param cause   the underlying cause (e.g., NumberFormatException)
     */
    public CsvFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}