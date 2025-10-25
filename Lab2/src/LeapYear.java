public class LeapYear {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Invalid input. Please provide <year>.");
        } else {
            int year = Integer.parseInt(args[0]);
            boolean isLeap = false;

            if (year % 4 == 0) {
                if (year % 100 == 0) {
                    if (year % 400 == 0)
                        isLeap = true;
                } else
                    isLeap = true;
            }

            if (isLeap) {
                System.out.println(year + " is a leap year.");
            } else {
                System.out.println(year + " is not a leap year.");
            }
        }
    }
}
