import java.io.BufferedReader;

public class GradeStats {
    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new java.io.FileReader(args[0]));
            String line;
            Double total = 0.0;
            Double highest = 0.0;
            Double lowest = Double.MAX_VALUE;
            int count = 0;
            line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Double grade = Double.valueOf(parts[1]);
                total += grade;
                count++;
                if (grade > highest) {
                    highest = grade;
                }
                if (grade < lowest) {
                    lowest = grade;
                }
            }
            Double average = total / count;
            System.out.printf("Average: %.2f%n", average);
            System.out.printf("Highest: %.1f%n", highest);
            System.out.printf("Lowest: %.1f%n", lowest);
            System.out.printf("Count: %d%n", count);

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
