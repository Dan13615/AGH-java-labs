public class PrimeCheck {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly one number n");
            return;
        }

        int n = Integer.parseInt(args[0]);
        if (n <= 1) {
            System.out.println(n + " is not a prime number.");
            return;
        }

        boolean isPrime = true;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                isPrime = false;
                break;
            }
        }

        if (isPrime) {
            System.out.println(n + " is a prime number.");
        } else {
            System.out.println(n + " is not a prime number.");
            System.out.print("Smallest divisors: ");
            for (int i = 2; i <= n / 2; i++) {
                if (n % i == 0) {
                    System.out.print(i + " ");
                    break;
                }
            }
            System.out.println();
        }
    }
}
