package i2jp.oop;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class Person {
    private static final AtomicLong COUNTER = new AtomicLong(1L);
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final int ID_LEN = 7;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    private final String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;

    public Person(String firstName, String lastName, String birthDmy, Gender gender) {
        this.id = toBase36Padded(COUNTER.getAndIncrement(), ID_LEN);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = LocalDate.parse(birthDmy, DMY);
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDmy) {
        this.birthDate = LocalDate.parse(birthDmy, DMY);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBirthDateFormatted() {
        return birthDate.format(DMY);
    }

    public int getAgeYears() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + birthDate.format(DMY) + ", " + gender + ")";
    }

    private static String toBase36Padded(long n, int len) {
        String s = Long.toString(n, 36).toUpperCase();
        int pad = len - s.length();
        return (pad > 0 ? "0".repeat(pad) : "") + s;
    }

    public static void resetCounter() {
        COUNTER.set(1L);
    }
}