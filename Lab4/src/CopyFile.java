
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CopyFile {
    public static void main(String[] args) {
        java.io.InputStream input = null;
        java.io.OutputStream output = null;
        try {
            String source = args[0];
            String destination = args[1];

            if (source.startsWith("http://") || source.startsWith("https://")) {
                java.net.URL url = new java.net.URL(source);
                input = url.openStream();
            } else {
                input = new java.io.FileInputStream(source);
            }

            output = new java.io.FileOutputStream(destination);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = buffer.readLine()) != null) {
                output.write(line.getBytes());
            }
            System.out.println("File copied successfully.");
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Error: Source file not found.");
        } catch (java.io.IOException e) {
            System.err.println("Error: An I/O error occurred.");
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (java.io.IOException e) {
                System.err.println("Error: Failed to close streams.");
            }
        }
    }
}
