public class FizzBuzz {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly one number n");
            return;
        }

        int n = Integer.parseInt(args[0]);

        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.print("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.print("Fizz");
            } else if (i % 5 == 0) {
                System.out.print("Buzz");
            } else {
                System.out.print(i);
            }
            if (i < n) {
                System.out.print(" ");
            }
        }
    }
}
