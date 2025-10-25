import java.time.LocalDate;
import java.time.DayOfWeek;

public class Weekend {

    public static void main(String[] args) {
        DayOfWeek now = LocalDate.now().getDayOfWeek();

        if (now == DayOfWeek.SATURDAY || now == DayOfWeek.SUNDAY)
            System.out.println("Weekend!");
        else {
            System.out.println("Days until weekend: " + (DayOfWeek.SATURDAY.getValue() - now.getValue()));
        }
    }
}