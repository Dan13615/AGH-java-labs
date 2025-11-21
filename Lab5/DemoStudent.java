import i2jp.oop.Person.Gender;
import i2jp.oop.Student;

public class DemoStudent {
    public static void main(String[] args) {
        Student a = new Student("Anna", "Kowalska", "15.03.2003", Gender.FEMALE, "S12345");
        a.addGrade(5.0);
        a.addGrade(4.5);
        a.addGrade(3.0);

        Student b = new Student("Piotr", "Nowak", "02.09.2002", Gender.MALE, "S99999");
        b.addGrade(3.5);
        b.addGrade(3.5);
        b.addGrade(3.5);

        System.out.printf("%s: %s avg=%.2f%n", a.getId(), a, a.average().orElse(Double.NaN));
        System.out.printf("%s: %s avg=%.2f%n", b.getId(), b, b.average().orElse(Double.NaN));
    }
}
