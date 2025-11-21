
import java.io.BufferedReader;

public class WordCounter {
    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            int wordCount = 0;
            int charCount = 0;
            reader = new BufferedReader(new java.io.FileReader(args[0]));
            String line;
            while ((line = reader.readLine()) != null) {
                wordCount += countWords(line);
                charCount += line.length();
            }
            System.out.println("Word: " + wordCount);
            System.out.println("Character: " + charCount);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Please provide a string input as an argument.");
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Error: File not found.");
        } catch (java.io.IOException e) {
            System.err.println("Error: An unexpected error occurred.");
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (java.io.IOException e) {
                System.err.println("Error: Failed to close the reader.");
            }
        }
    }

    public static int countWords(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.trim().split("\\s+");
        return words.length;
    }
}
