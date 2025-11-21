import java.io.BufferedReader;

public class FileReaderExample {
    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new java.io.FileReader(args[0]));
            int i = 0;
            while (reader.readLine() != null)
                i++;
            System.out.println("Lines: " + i);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Error: File not found.");
        } catch (java.io.IOException e) {
            System.err.println("Error: An I/O error occurred.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Please provide a file name as an argument.");
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (java.io.IOException e) {
                System.err.println("Error: Failed to close the reader.");
            }
        }
    }
}
