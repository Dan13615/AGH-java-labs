
/*
 * EXERCISE 3 public class First {
 * 
 * public static void main(String[] args) { System.out.println("Hello!"); }
 * 
 * }
 */

/*
 * EXERCISE 4.3 import java.util.Date;
 * 
 * public class Main {
 * 
 * public static void main(String[] args) { Date current = new Date();
 * System.out.println("Hello! " + current); }
 * 
 * }
 */
/*
 * 
 * EXERCISE 4.4 and 4.5
 */

import java.util.Date;

public class Main {  
	 public static void main(String[] args) { 
		 int current = new Date().getDay();
		 String[] Days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			 "Saturday", "Sunday"}; 
		 if (current == 6 || current == 7)
			 System.out.println("Weekend !");
		 else
			 System.out.println("Weekday...");
		 System.out.println("Today is " + Days[current - 1]); 
	 }
 }

