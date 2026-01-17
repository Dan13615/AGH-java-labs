package i2jp.oop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class Group {
    private final String name;
    private String description;
    private final Set<Student> members = new HashSet<>();
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean addStudent(Student s) {
        if (GroupRegistry.isAssigned(s.getId())) {
            return false;
        }
        boolean ok = members.add(s);
        if (ok)
            GroupRegistry.assign(s.getId(), name);
        return ok;
    }

    public boolean removeStudent(Student s) {
        boolean ok = members.remove(s);
        if (ok)
            GroupRegistry.unassign(s.getId());
        return ok;
    }

    public Set<Student> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public String toString() {
        return "Group " + name + " (" + description + ") size=" + members.size();
    }

    // ===== CSV EXPORT (Task 5) =====

    public void exportToCsv(Path file) throws IOException {
        exportToCsv(file, ";");
    }

    public void exportToCsv(Path file, String delimiter) throws IOException {
        List<String> lines = members.stream()
                .map(s -> String.join(delimiter,
                        s.getId(),
                        s.getIndexNumber(),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getBirthDateFormatted(),
                        formatGrades(s.getGrades())))
                .toList();

        Files.write(file, lines);
    }

    private String formatGrades(List<Double> grades) {
        if (grades.isEmpty())
            return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < grades.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(String.format(Locale.US, "%.1f", grades.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    // ===== CSV IMPORT (Task 6) =====

    public void importFromCsv(Path file) throws IOException {
        importFromCsv(file, ";");
    }

    public void importFromCsv(Path file, String delimiter) throws IOException {
        List<String> lines = Files.readAllLines(file);

        for (String line : lines) {
            String[] f = line.split(Pattern.quote(delimiter));
            if (f.length != 6)
                throw new IOException("Malformed CSV line: " + line);

            String id = f[0];
            String index = f[1];
            String first = f[2];
            String last = f[3];
            String birthDmy = f[4];
            String gradesRaw = f[5];

            List<Double> grades = parseGrades(gradesRaw);

            Student s = new Student(first, last, birthDmy, Person.Gender.OTHER, index);
            for (double g : grades)
                s.addGrade(g);

            this.addStudent(s);
        }
    }

    private List<Double> parseGrades(String gradesRaw) {
        List<Double> grades = new ArrayList<>();
        String inner = gradesRaw.replace("[", "").replace("]", "").trim();
        if (!inner.isEmpty()) {
            String[] parts = inner.split(",");
            for (String p : parts) {
                grades.add(Double.parseDouble(p.trim()));
            }
        }
        return grades;
    }
}