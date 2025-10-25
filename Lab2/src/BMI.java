public class BMI {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid input. Please provide weight, and height.");
        } else {
            float weight = Float.parseFloat(args[0]);
            float height = Float.parseFloat(args[1]);

            float bmi = weight / (height * height);
            System.out.printf("Your BMI is: %.2f%n", bmi);

            if (bmi < 18.5) {
                System.out.println("Underweight.");
            } else if (bmi >= 18.5 && bmi < 24.9) {
                System.out.println("Normal weight.");
            } else if (bmi >= 25 && bmi < 29.9) {
                System.out.println("Overweight.");
            } else {
                System.out.println("Obese.");
            }
        }
    }
}
