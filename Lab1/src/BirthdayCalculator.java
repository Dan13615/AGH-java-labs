import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BirthdayCalculator {
    public static void main(String[] args) {
        LocalDate birthday = LocalDate.of(2004, 8, 29);
        LocalDate now = LocalDate.now();
        LocalDate currentYearBirthday = LocalDate.of(now.getYear(), 8, 29);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy"); 

        long daysAlive = birthday.until(now, ChronoUnit.DAYS);
        long daysBeforeBirthday = now.until(currentYearBirthday, ChronoUnit.DAYS);

        System.out.println("Today is " + now.format(fmt) + " - since the birthdate on " + birthday.format(fmt) + ", "
                + daysAlive + " days have passed.");
        System.out.println("Days to birthday this year: " + daysBeforeBirthday);
    }
}
