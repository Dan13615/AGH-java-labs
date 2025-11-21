public class SafeDivision {
    public static void main(String[] args) {
        try {
            double numerator = Double.parseDouble(args[0]);
            double denominator = Double.parseDouble(args[1]);
            double result = divide(numerator, denominator);
            if (!Double.isNaN(result)) {
                System.out.println("Result: " + result);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Please provide both numerator and denominator.");
        }
    }

    public static double divide(double numerator, double denominator) {
        try {
            if (denominator == 0) {
                throw new ArithmeticException("Denominator cannot be zero !");
            }
            return numerator / denominator;
        } catch (ArithmeticException e) {
            System.err.println("Error: " + e.getMessage());
            return Double.NaN;
        }
    }
}
