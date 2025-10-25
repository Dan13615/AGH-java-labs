import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class BirthdayInfo {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java BirthdayInfo <year> <month> <day>");
            return;
        }

        int year, month, day;
        try {
            year = Integer.parseInt(args[0]);
            month = Integer.parseInt(args[1]);
            day = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Arguments must be integers: <year> <month> <day>");
            return;
        }

        LocalDate birth;
        try {
            birth = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            System.out.println("Invalid date provided.");
            return;
        }

        LocalDate today = LocalDate.now();
        final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        System.out.println("Birthday: " + birth.format(DATE_FMT));
        System.out.println("Day of week: " + birth.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));

        if (birth.isLeapYear()) {
            System.out.println(birth.getYear() + " is a leap year");
        }

        System.out.println("Western zodiac: " + westernZodiac(MonthDay.from(birth)));

        String[] cz = chineseZodiac(birth);
        String animal = cz[0];
        boolean warn = "1".equals(cz[1]);
        System.out.print("Chinese zodiac: " + animal);
        if (warn)
            System.out.println(" (warning: possible inaccuracy for Jan 21–Feb 3, simplified boundary Feb 4)");
        System.out.println();
        long daysLived = ChronoUnit.DAYS.between(birth, today);
        System.out.println("Today: " + today.format(DATE_FMT) + " - since the birthday on " + birth.format(DATE_FMT)
                + ", " + daysLived + " days have passed");
        Period age = Period.between(birth, today);
        System.out.println(
                "Age today: " + age.getYears() + " years, " + age.getMonths() + " months, " + age.getDays() + " days");
        LocalDate birthdayThisYear = adjustBirthdayForYear(birth, today.getYear());

        if (birthdayThisYear.isEqual(today)) {
            System.out.println("Birthday is today.");
            LocalDate nextBirthday = adjustBirthdayForYear(birth, today.getYear() + 1);
            long daysUntilNext = ChronoUnit.DAYS.between(today, nextBirthday);
            System.out.println("Next birthday: " + nextBirthday.format(DATE_FMT) + " ("
                    + nextBirthday.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "), in "
                    + daysUntilNext + " days.");
        } else if (birthdayThisYear.isAfter(today)) {
            long daysUntil = ChronoUnit.DAYS.between(today, birthdayThisYear);
            System.out.println("Birthday is in " + daysUntil + " days (this year).");
        } else {
            long daysAgo = ChronoUnit.DAYS.between(birthdayThisYear, today);
            System.out.println("Birthday was " + daysAgo + " days ago (this year).");

            LocalDate nextBirthday = adjustBirthdayForYear(birth, today.getYear() + 1);
            long daysUntilNext = ChronoUnit.DAYS.between(today, nextBirthday);
            System.out.println("Next birthday: " + nextBirthday.format(DATE_FMT) + " ("
                    + nextBirthday.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "), in "
                    + daysUntilNext + " days.");
        }
    }

    private static LocalDate adjustBirthdayForYear(LocalDate birth, int targetYear) {
        if (birth.getMonth() == Month.FEBRUARY && birth.getDayOfMonth() == 29) {
            if (Year.isLeap(targetYear))
                return LocalDate.of(targetYear, 2, 29);
            else
                return LocalDate.of(targetYear, 2, 28);
        } else
            return LocalDate.of(targetYear, birth.getMonth(), birth.getDayOfMonth());
    }

    private static String westernZodiac(MonthDay md) {
        MonthDay mar21 = MonthDay.of(3, 21);
        MonthDay apr19 = MonthDay.of(4, 19);
        MonthDay apr20 = MonthDay.of(4, 20);
        MonthDay may20 = MonthDay.of(5, 20);
        MonthDay may21 = MonthDay.of(5, 21);
        MonthDay jun20 = MonthDay.of(6, 20);
        MonthDay jun21 = MonthDay.of(6, 21);
        MonthDay jul22 = MonthDay.of(7, 22);
        MonthDay jul23 = MonthDay.of(7, 23);
        MonthDay aug22 = MonthDay.of(8, 22);
        MonthDay aug23 = MonthDay.of(8, 23);
        MonthDay sep22 = MonthDay.of(9, 22);
        MonthDay sep23 = MonthDay.of(9, 23);
        MonthDay oct22 = MonthDay.of(10, 22);
        MonthDay oct23 = MonthDay.of(10, 23);
        MonthDay nov21 = MonthDay.of(11, 21);
        MonthDay nov22 = MonthDay.of(11, 22);
        MonthDay dec21 = MonthDay.of(12, 21);
        MonthDay dec22 = MonthDay.of(12, 22);
        MonthDay jan19 = MonthDay.of(1, 19);
        MonthDay jan20 = MonthDay.of(1, 20);
        MonthDay feb18 = MonthDay.of(2, 18);
        MonthDay feb19 = MonthDay.of(2, 19);
        MonthDay mar20 = MonthDay.of(3, 20);

        if (!md.isBefore(mar21) && !md.isAfter(apr19))
            return "Aries";
        if (!md.isBefore(apr20) && !md.isAfter(may20))
            return "Taurus";
        if (!md.isBefore(may21) && !md.isAfter(jun20))
            return "Gemini";
        if (!md.isBefore(jun21) && !md.isAfter(jul22))
            return "Cancer";
        if (!md.isBefore(jul23) && !md.isAfter(aug22))
            return "Leo";
        if (!md.isBefore(aug23) && !md.isAfter(sep22))
            return "Virgo";
        if (!md.isBefore(sep23) && !md.isAfter(oct22))
            return "Libra";
        if (!md.isBefore(oct23) && !md.isAfter(nov21))
            return "Scorpio";
        if (!md.isBefore(nov22) && !md.isAfter(dec21))
            return "Sagittarius";
        if ((!md.isBefore(dec22) && !md.isAfter(MonthDay.of(12, 31)))
                || (!md.isBefore(MonthDay.of(1, 1)) && !md.isAfter(jan19)))
            return "Capricorn";
        if (!md.isBefore(jan20) && !md.isAfter(feb18))
            return "Aquarius";
        if (!md.isBefore(feb19) && !md.isAfter(mar20))
            return "Pisces";

        return "Unknown";
    }

    private static String[] chineseZodiac(LocalDate birth) {
        String[] animals = { "Rat", "Ox", "Tiger", "Rabbit", "Dragon", "Snake", "Horse", "Goat", "Monkey",
                "Rooster", "Dog", "Pig" };

        int effectiveYear = birth.getYear();
        LocalDate boundary = LocalDate.of(birth.getYear(), 2, 4);
        if (birth.isBefore(boundary)) {
            effectiveYear = birth.getYear() - 1;
        }

        int idx = Math.floorMod(effectiveYear - 1900, 12);
        String animal = animals[idx];
        boolean warn = (birth.getMonth() == Month.JANUARY && birth.getDayOfMonth() >= 21)
                || (birth.getMonth() == Month.FEBRUARY && birth.getDayOfMonth() <= 3);
        return new String[] { animal, warn ? "1" : "0" };
    }
}
