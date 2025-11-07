public class Factorial {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly one number n");
            return;
        }

        int n = Integer.parseInt(args[0]);
        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        System.out.println(n + "! = " + factorial);
    }
}
