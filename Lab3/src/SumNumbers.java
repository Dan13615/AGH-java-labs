public class SumNumbers {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly one number n");
            return;
        }

        int n = Integer.parseInt(args[0]);
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        System.out.println("Sum = " + sum);
    }
}
