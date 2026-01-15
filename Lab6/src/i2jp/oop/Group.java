// File: src/i2jp/oop/Group.java
// Group class with Log4j 2 logging and CsvFormatException
package i2jp.oop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group {
  private static final Logger log = LogManager.getLogger(Group.class);

  private final String name;
  private String description;
  private final Set<Student> members = new HashSet<>();
  private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  public Group(String name, String description) {
    this.name = name;
    this.description = description;
    log.info("Group created: name='{}', description='{}'", name, description);
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    log.debug("Changing description for group '{}' from '{}' to '{}'",
        name, this.description, description);
    this.description = description;
  }

  public boolean addStudent(Student s) {
    if (GroupRegistry.isAssigned(s.getId())) {
      String assigned = GroupRegistry.assignedGroup(s.getId());
      log.warn("Cannot add student id={} to group='{}' — already in group='{}'",
          s.getId(), name, assigned);
      return false;
    }
    boolean ok = members.add(s);
    if (ok) {
      GroupRegistry.assign(s.getId(), name);
      log.info("Student index={} added to group='{}'", s.getIndexNumber(), name);
    } else {
      log.debug("Student index={} already in group='{}'", s.getIndexNumber(), name);
    }
    return ok;
  }

  public boolean removeStudent(Student s) {
    boolean ok = members.remove(s);
    if (ok) {
      GroupRegistry.unassign(s.getId());
      log.info("Student index={} removed from group='{}'", s.getIndexNumber(), name);
    } else {
      log.warn("Attempt to remove non-member index={} from group='{}'",
          s.getIndexNumber(), name);
    }
    return ok;
  }

  public Set<Student> getMembers() {
    return Collections.unmodifiableSet(members);
  }

  @Override
  public String toString() {
    return "Group " + name + " (" + description + ") size=" + members.size();
  }

  // ===== CSV EXPORT (with logging) =====

  public void exportToCsv(Path file) throws IOException {
    exportToCsv(file, ";");
  }

  public void exportToCsv(Path file, String delimiter) throws IOException {
    log.info("Exporting {} students from group='{}' to file={}",
        members.size(), name, file);

    List<String> lines = members.stream()
        .map(s -> String.join(delimiter,
            s.getId(),
            s.getIndexNumber(),
            s.getFirstName(),
            s.getLastName(),
            s.getBirthDateFormatted(),
            formatGrades(s.getGrades())))
        .peek(line -> log.debug("CSV export line: {}", line))
        .collect(Collectors.toList());

    try {
      Files.write(file, lines);
      log.info("Export successful: {} lines written to {}", lines.size(), file);
    } catch (IOException e) {
      log.error("Export failed for file={}: {}", file, e.getMessage(), e);
      throw e;
    }
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

  // ===== CSV IMPORT (with logging and CsvFormatException) =====

  public void importFromCsv(Path file) throws IOException, CsvFormatException {
    importFromCsv(file, ";");
  }

  public void importFromCsv(Path file, String delimiter)
      throws IOException, CsvFormatException {

    log.info("Importing students into group='{}' from file={}", name, file);

    List<String> lines = Files.readAllLines(file);
    int imported = 0;
    int lineNumber = 0;

    for (String line : lines) {
      lineNumber++;
      log.debug("Processing CSV line {}: {}", lineNumber, line);

      String[] fields = line.split(Pattern.quote(delimiter));
      if (fields.length != 6) {
        String msg = String.format(
            "Malformed CSV line %d (expected 6 fields, got %d): %s",
            lineNumber, fields.length, line);
        log.error(msg);
        throw new CsvFormatException(msg);
      }

      try {
        String id = fields[0];
        String index = fields[1];
        String first = fields[2];
        String last = fields[3];
        String birthDmy = fields[4];
        String gradesRaw = fields[5];

        List<Double> grades = parseGrades(gradesRaw);

        Student s = new Student(first, last, birthDmy, Person.Gender.OTHER, index);
        for (double g : grades) {
          s.addGrade(g);
        }

        if (addStudent(s)) {
          imported++;
        }
      } catch (Exception e) {
        String msg = String.format("Error parsing CSV line %d: %s", lineNumber, e.getMessage());
        log.error(msg, e);
        throw new CsvFormatException(msg, e);
      }
    }

    log.info("Import finished: {} students imported into group='{}'", imported, name);
  }

  private List<Double> parseGrades(String gradesRaw) throws CsvFormatException {
    List<Double> grades = new ArrayList<>();
    String inner = gradesRaw.replace("[", "").replace("]", "").trim();

    if (!inner.isEmpty()) {
      String[] parts = inner.split(",");
      for (String p : parts) {
        try {
          double grade = Double.parseDouble(p.trim());
          grades.add(grade);
        } catch (NumberFormatException e) {
          throw new CsvFormatException("Invalid grade value: " + p, e);
        }
      }
    }

    log.trace("Parsed {} grades from: {}", grades.size(), gradesRaw);
    return grades;
  }
}