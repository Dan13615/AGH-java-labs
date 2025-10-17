import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FormatTimeAndDate {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime octoberFirst = LocalDateTime.of(2025, 10, 1, 0, 0);
        LocalDateTime nowPlusSevenDays = now.plusDays(7);
        LocalDateTime nowPlusHundredDays = now.plusDays(100);
        LocalDateTime newYearEve = LocalDateTime.of(now.getYear(), 12, 31, 23, 59);

        long fromOctoberFirst = octoberFirst.until(now, ChronoUnit.DAYS);
        long untilNewYearEve = now.until(newYearEve, ChronoUnit.DAYS);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("EEEE, MMMM yyyy HH:mm");
        DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("EEEE");

        System.out.println("Hello! Today is: " + now.format(fmt));
        System.out.println("The day of the week for today: " + now.format(fmt2));
        System.out.println("Time elapsed since October 1st, 2025: " + fromOctoberFirst + " days");
        System.out.println("Date after adding 7 days: " + nowPlusSevenDays.format(fmt1));
        System.out.println("Date after adding 100 days: " + nowPlusHundredDays.format(fmt1));
        System.out.println("Time until New Year's Eve: " + untilNewYearEve + " days");
    }
}