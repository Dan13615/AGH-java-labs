// File: src/i2jp/oop/Person.java
// Person class with Log4j 2 logging integration
package i2jp.oop;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Person {
    private static final Logger log = LogManager.getLogger(Person.class);

    // --- Static fields and constants ---
    private static final AtomicLong COUNTER = new AtomicLong(1L);
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final int ID_LEN = 7;

    // --- Enum defined inside Person ---
    public enum Gender {
        MALE, FEMALE, OTHER
    }

    // --- Instance fields ---
    private final String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;

    // --- Constructor ---
    public Person(String firstName, String lastName, String birthDmy, Gender gender) {
        this.id = toBase36Padded(COUNTER.getAndIncrement(), ID_LEN);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = LocalDate.parse(birthDmy, DMY);
        this.gender = gender;

        log.debug("Created Person id={} name={} {} birth={} gender={}",
                id, firstName, lastName, birthDmy, gender);
    }

    // --- Getters and setters ---
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        log.trace("Changing firstName from '{}' to '{}' for id={}",
                this.firstName, firstName, id);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        log.trace("Changing lastName from '{}' to '{}' for id={}",
                this.lastName, lastName, id);
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDmy) {
        LocalDate oldDate = this.birthDate;
        this.birthDate = LocalDate.parse(birthDmy, DMY);
        log.trace("Changed birthDate from {} to {} for id={}",
                oldDate.format(DMY), birthDmy, id);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        log.trace("Changing gender from {} to {} for id={}",
                this.gender, gender, id);
        this.gender = gender;
    }

    // Helper to get formatted birth date
    public String getBirthDateFormatted() {
        return birthDate.format(DMY);
    }

    // --- Utility methods ---
    public int getAgeYears() {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        log.trace("Computed age for {} {}: {} years", firstName, lastName, age);
        return age;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + birthDate.format(DMY) + ", " + gender + ")";
    }

    // --- Helper for ID formatting ---
    private static String toBase36Padded(long n, int len) {
        String s = Long.toString(n, 36).toUpperCase();
        int pad = len - s.length();
        String result = (pad > 0 ? "0".repeat(pad) : "") + s;
        log.trace("Generated base36 id={} from n={}", result, n);
        return result;
    }

    // Reset counter for testing purposes
    public static void resetCounter() {
        long oldValue = COUNTER.get();
        COUNTER.set(1L);
        log.debug("Reset Person ID counter from {} to 1", oldValue);
    }
}