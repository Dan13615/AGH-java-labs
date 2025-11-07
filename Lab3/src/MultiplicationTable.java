public class MultiplicationTable {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly one number n");
            return;
        }

        int n = Integer.parseInt(args[0]);

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                int product = i * j;
                System.out.print(product + " " + (product < 10 ? " " : ""));
            }
            System.out.println();
        }
    }
}
