import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class NumberFile {
    public static void main(String[] args) {
        File file = new File("numbers.txt");
        PrintWriter writer;
        BufferedReader reader = null;
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            writer = new PrintWriter(new java.io.FileWriter(file, true));
            for (int i = 1; i <= Integer.parseInt(args[0]); i++) {
                writer.println(i);
            }
            writer.close();
            reader = new BufferedReader(new java.io.FileReader(file));
            String line;
            int result = 0;
            while ((line = reader.readLine()) != null) {
                result += Integer.parseInt(line.trim());
            }
            System.out.println("Sum = " + result);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Please provide a number as an argument.");
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format.");
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred.");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Error: Failed to close resources.");
            }
        }
    }
}
