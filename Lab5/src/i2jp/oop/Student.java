// File: src/i2jp/oop/Student.java
// Author: Student & Group Manager Implementation
package i2jp.oop;

import java.util.*;

public class Student extends Person {
    private final String indexNumber;
    private final List<Double> grades = new ArrayList<>();
    private static final Set<Double> ALLOWED = Set.of(2.0, 3.0, 3.5, 4.0, 4.5, 5.0);

    public Student(String firstName, String lastName, String birthDmy, Gender gender, String indexNumber) {
        super(firstName, lastName, birthDmy, gender);
        this.indexNumber = indexNumber;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void addGrade(double g) {
        if (!ALLOWED.contains(g))
            throw new IllegalArgumentException("Grade not allowed: " + g);
        grades.add(g);
    }

    public OptionalDouble average() {
        return grades.stream().mapToDouble(Double::doubleValue).average();
    }

    public List<Double> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    @Override
    public String toString() {
        return super.toString() + " [index=" + indexNumber + "]";
    }
}