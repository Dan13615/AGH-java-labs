// File: StudentManagerApp.java
// Complete Student & Group Manager with Log4j 2 logging

import i2jp.oop.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StudentManagerApp {
    private static final Logger log = LogManager.getLogger(StudentManagerApp.class);

    // Repositories
    private static final Map<String, Student> studentRepo = new HashMap<>();
    private static final Map<String, Group> groupRepo = new HashMap<>();

    // Configuration
    private static Properties config = new Properties();
    private static final String CONFIG_FILE = "console.properties";
    private static String delimiter = ";";
    private static String studentsFile = "students.csv";
    private static String groupsFile = "groups.csv";

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        log.info("=== Student & Group Manager Application Starting ===");
        log.info("Java version: {}", System.getProperty("java.version"));

        try {
            loadConfiguration();

            System.out.println("=== Student & Group Manager ===");
            System.out.println("Configuration loaded from " + CONFIG_FILE);
            System.out.println();

            runMainLoop();

        } catch (Exception e) {
            log.fatal("Unexpected fatal error in main()", e);
            System.err.println("FATAL ERROR: " + e.getMessage());
            System.err.println("See logs for details.");
        } finally {
            log.info("=== Application Shutting Down ===");
            scanner.close();
        }
    }

    private static void runMainLoop() {
        boolean running = true;
        while (running) {
            try {
                printMenu();
                String choice = scanner.nextLine().trim();
                log.debug("User selected menu option: '{}'", choice);

                switch (choice) {
                    case "1" -> loadStudentsFromCsv();
                    case "2" -> loadGroupsFromCsv();
                    case "3" -> saveStudentsToCsv();
                    case "4" -> saveGroupsToCsv();
                    case "5" -> addNewStudent();
                    case "6" -> addNewGroup();
                    case "7" -> assignStudentToGroup();
                    case "8" -> removeStudentFromGroup();
                    case "9" -> showAllStudents();
                    case "10" -> showAllGroups();
                    case "11" -> showGroupDetails();
                    case "12" -> showConfiguration();
                    case "13" -> editConfiguration();
                    case "0" -> {
                        running = false;
                        log.info("User requested exit");
                    }
                    default -> {
                        System.out.println("Invalid option. Please try again.");
                        log.warn("Invalid menu option entered: '{}'", choice);
                    }
                }
            } catch (Exception e) {
                log.error("Error processing menu option", e);
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }

        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("=== Student & Group Manager ===");
        System.out.println("1)  Load students from CSV");
        System.out.println("2)  Load groups from CSV");
        System.out.println("3)  Save students to CSV");
        System.out.println("4)  Save groups to CSV");
        System.out.println("5)  Add new student");
        System.out.println("6)  Add new group");
        System.out.println("7)  Assign student to group");
        System.out.println("8)  Remove student from group");
        System.out.println("9)  Show all students");
        System.out.println("10) Show all groups");
        System.out.println("11) Show group details");
        System.out.println("12) Show current configuration");
        System.out.println("13) Edit configuration");
        System.out.println("0)  Exit");
        System.out.print("Select option: ");
    }

    // ===== CONFIGURATION MANAGEMENT =====

    private static void loadConfiguration() {
        Path configPath = Paths.get(CONFIG_FILE);

        if (!Files.exists(configPath)) {
            log.warn("Configuration file not found: {}", CONFIG_FILE);
            createDefaultConfiguration(configPath);
        } else {
            try (InputStream in = Files.newInputStream(configPath)) {
                config.load(in);
                delimiter = config.getProperty("delimiter", ";");
                studentsFile = config.getProperty("students", "students.csv");
                groupsFile = config.getProperty("groups", "groups.csv");
                log.info("Configuration loaded: delimiter='{}', students='{}', groups='{}'",
                        delimiter, studentsFile, groupsFile);
            } catch (IOException e) {
                log.error("Failed to load configuration from {}", CONFIG_FILE, e);
                System.out.println("[WARNING] Could not load configuration: " + e.getMessage());
                createDefaultConfiguration(configPath);
            }
        }
    }

    private static void createDefaultConfiguration(Path configPath) {
        config.setProperty("delimiter", ";");
        config.setProperty("students", "students.csv");
        config.setProperty("groups", "groups.csv");

        try (OutputStream out = Files.newOutputStream(configPath)) {
            config.store(out, "Student Manager Configuration");
            log.info("Created default configuration file: {}", CONFIG_FILE);
            System.out.println("[INFO] Created default configuration: " + CONFIG_FILE);
        } catch (IOException e) {
            log.error("Failed to create configuration file: {}", CONFIG_FILE, e);
            System.out.println("[ERROR] Could not create configuration file: " + e.getMessage());
        }

        delimiter = ";";
        studentsFile = "students.csv";
        groupsFile = "groups.csv";
    }

    private static void showConfiguration() {
        log.debug("Displaying current configuration");
        System.out.println("=== Current Configuration ===");
        System.out.println("Delimiter: " + delimiter);
        System.out.println("Students file: " + studentsFile);
        System.out.println("Groups file: " + groupsFile);
    }

    private static void editConfiguration() {
        log.info("User entering configuration edit mode");
        System.out.println("=== Edit Configuration ===");

        System.out.print("New delimiter (current: " + delimiter + "): ");
        String newDelim = scanner.nextLine().trim();
        if (!newDelim.isEmpty()) {
            log.info("Changing delimiter from '{}' to '{}'", delimiter, newDelim);
            delimiter = newDelim;
        }

        System.out.print("New students file (current: " + studentsFile + "): ");
        String newStudents = scanner.nextLine().trim();
        if (!newStudents.isEmpty()) {
            log.info("Changing students file from '{}' to '{}'", studentsFile, newStudents);
            studentsFile = newStudents;
        }

        System.out.print("New groups file (current: " + groupsFile + "): ");
        String newGroups = scanner.nextLine().trim();
        if (!newGroups.isEmpty()) {
            log.info("Changing groups file from '{}' to '{}'", groupsFile, newGroups);
            groupsFile = newGroups;
        }

        config.setProperty("delimiter", delimiter);
        config.setProperty("students", studentsFile);
        config.setProperty("groups", groupsFile);

        try (OutputStream out = Files.newOutputStream(Paths.get(CONFIG_FILE))) {
            config.store(out, "Student Manager Configuration");
            log.info("Configuration saved successfully to {}", CONFIG_FILE);
            System.out.println("[INFO] Configuration saved successfully.");
        } catch (IOException e) {
            log.error("Failed to save configuration", e);
            System.out.println("[ERROR] Could not save configuration: " + e.getMessage());
        }
    }

    // ===== STUDENT CSV OPERATIONS =====

    private static void loadStudentsFromCsv() {
        Path path = Paths.get(studentsFile);
        log.info("Attempting to load students from: {}", path);

        if (!Files.exists(path)) {
            log.warn("Student file not found: {}", studentsFile);
            System.out.println("File not found: " + studentsFile);
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            int count = 0;
            int errors = 0;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                log.debug("Processing student CSV line {}: {}", i + 1, line);

                try {
                    String[] fields = line.split(Pattern.quote(delimiter));
                    if (fields.length != 6) {
                        log.warn("Skipping malformed line {}: expected 6 fields, got {}",
                                i + 1, fields.length);
                        System.out.println("Skipping malformed line " + (i + 1) + ": " + line);
                        errors++;
                        continue;
                    }

                    String id = fields[0];
                    String index = fields[1];
                    String firstName = fields[2];
                    String lastName = fields[3];
                    String birthDate = fields[4];
                    String gradesRaw = fields[5];

                    Student student = new Student(firstName, lastName, birthDate,
                            Person.Gender.OTHER, index);

                    List<Double> grades = parseGrades(gradesRaw);
                    for (double g : grades) {
                        try {
                            student.addGrade(g);
                        } catch (IllegalArgumentException e) {
                            log.warn("Invalid grade {} for student {} on line {}", g, index, i + 1);
                            System.out.println("Invalid grade " + g + " for student " + index);
                        }
                    }

                    studentRepo.put(student.getId(), student);
                    count++;

                } catch (Exception e) {
                    log.error("Error processing line {}", i + 1, e);
                    System.out.println("Error on line " + (i + 1) + ": " + e.getMessage());
                    errors++;
                }
            }

            log.info("Student import completed: {} imported, {} errors", count, errors);
            System.out.println("Imported " + count + " students from " + studentsFile);
            if (errors > 0) {
                System.out.println("(" + errors + " lines had errors)");
            }

        } catch (IOException e) {
            log.error("I/O error loading students from {}", studentsFile, e);
            System.out.println("I/O Error: " + e.getMessage());
        }
    }

    private static void saveStudentsToCsv() {
        log.info("Attempting to save {} students to: {}", studentRepo.size(), studentsFile);

        try {
            List<String> lines = new ArrayList<>();

            for (Student s : studentRepo.values()) {
                String line = String.join(delimiter,
                        s.getId(),
                        s.getIndexNumber(),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getBirthDateFormatted(),
                        formatGrades(s.getGrades()));
                lines.add(line);
                log.debug("Saving student: {}", line);
            }

            Files.write(Paths.get(studentsFile), lines);
            log.info("Successfully exported {} students to {}", lines.size(), studentsFile);
            System.out.println("Exported " + lines.size() + " students to " + studentsFile);

        } catch (IOException e) {
            log.error("Failed to save students to {}", studentsFile, e);
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    // ===== GROUP CSV OPERATIONS =====

    private static void loadGroupsFromCsv() {
        Path path = Paths.get(groupsFile);
        log.info("Attempting to load groups from: {}", path);

        if (!Files.exists(path)) {
            log.warn("Groups file not found: {}", groupsFile);
            System.out.println("File not found: " + groupsFile);
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            int count = 0;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                log.debug("Processing group CSV line {}: {}", i + 1, line);

                String[] fields = line.split(Pattern.quote(delimiter));
                if (fields.length != 3) {
                    log.warn("Skipping malformed group line {}", i + 1);
                    System.out.println("Skipping malformed line " + (i + 1) + ": " + line);
                    continue;
                }

                String groupName = fields[0];
                String description = fields[1];
                String studentIdsRaw = fields[2];

                Group group = new Group(groupName, description);

                List<String> studentIds = parseStudentIds(studentIdsRaw);
                for (String studentId : studentIds) {
                    Student student = studentRepo.get(studentId);
                    if (student != null) {
                        if (!group.addStudent(student)) {
                            log.warn("Could not add student {} to group {}", studentId, groupName);
                            System.out.println("Warning: Student " + studentId +
                                    " already in another group");
                        }
                    } else {
                        log.warn("Student ID {} not found in repository", studentId);
                        System.out.println("Warning: Student ID " + studentId + " not found");
                    }
                }

                groupRepo.put(groupName, group);
                count++;
            }

            log.info("Group import completed: {} groups loaded", count);
            System.out.println("Imported " + count + " groups from " + groupsFile);

        } catch (IOException e) {
            log.error("I/O error loading groups from {}", groupsFile, e);
            System.out.println("I/O Error: " + e.getMessage());
        }
    }

    private static void saveGroupsToCsv() {
        log.info("Attempting to save {} groups to: {}", groupRepo.size(), groupsFile);

        try {
            List<String> lines = new ArrayList<>();

            for (Group g : groupRepo.values()) {
                String studentIds = formatStudentIds(g.getMembers());
                String line = String.join(delimiter,
                        g.getName(),
                        g.getDescription(),
                        studentIds);
                lines.add(line);
                log.debug("Saving group: {}", line);
            }

            Files.write(Paths.get(groupsFile), lines);
            log.info("Successfully exported {} groups to {}", lines.size(), groupsFile);
            System.out.println("Exported " + lines.size() + " groups to " + groupsFile);

        } catch (IOException e) {
            log.error("Failed to save groups to {}", groupsFile, e);
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    // ===== INTERACTIVE OPERATIONS =====

    private static void addNewStudent() {
        log.info("User adding new student interactively");
        System.out.println("=== Add New Student ===");

        try {
            System.out.print("First name: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Last name: ");
            String lastName = scanner.nextLine().trim();

            System.out.print("Birth date (DD.MM.YYYY): ");
            String birthDate = scanner.nextLine().trim();

            System.out.print("Gender (MALE/FEMALE/OTHER): ");
            String genderStr = scanner.nextLine().trim().toUpperCase();
            Person.Gender gender;
            try {
                gender = Person.Gender.valueOf(genderStr);
            } catch (IllegalArgumentException e) {
                gender = Person.Gender.OTHER;
                log.warn("Invalid gender '{}', defaulting to OTHER", genderStr);
                System.out.println("Invalid gender, using OTHER");
            }

            System.out.print("Index number: ");
            String index = scanner.nextLine().trim();

            Student student = new Student(firstName, lastName, birthDate, gender, index);
            studentRepo.put(student.getId(), student);

            log.info("New student created via UI: id={} index={}", student.getId(), index);
            System.out.println("Student created with ID: " + student.getId());

        } catch (Exception e) {
            log.error("Error creating student", e);
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addNewGroup() {
        log.info("User adding new group interactively");
        System.out.println("=== Add New Group ===");

        System.out.print("Group name: ");
        String name = scanner.nextLine().trim();

        if (groupRepo.containsKey(name)) {
            log.warn("Group '{}' already exists, asking for confirmation", name);
            System.out.print("Group already exists. Overwrite? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                log.info("User cancelled group overwrite");
                System.out.println("Cancelled.");
                return;
            }
        }

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        Group group = new Group(name, description);
        groupRepo.put(name, group);

        log.info("New group created via UI: '{}'", name);
        System.out.println("Group created: " + name);
    }

    private static void assignStudentToGroup() {
        System.out.println("=== Assign Student to Group ===");
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentRepo.get(studentId);
        if (student == null) {
            log.warn("Student not found in assign operation: {}", studentId);
            System.out.println("Student not found: " + studentId);
            return;
        }

        System.out.print("Group name: ");
        String groupName = scanner.nextLine().trim();

        Group group = groupRepo.get(groupName);
        if (group == null) {
            log.warn("Group not found in assign operation: {}", groupName);
            System.out.println("Group not found: " + groupName);
            return;
        }

        if (group.addStudent(student)) {
            System.out.println("Added " + student.getFirstName() + " " +
                    student.getLastName() + " to " + groupName);
        } else {
            String currentGroup = GroupRegistry.assignedGroup(studentId);
            System.out.println("Failed: Student already in group " + currentGroup);
        }
    }

    private static void removeStudentFromGroup() {
        System.out.println("=== Remove Student from Group ===");
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentRepo.get(studentId);
        if (student == null) {
            log.warn("Student not found in remove operation: {}", studentId);
            System.out.println("Student not found: " + studentId);
            return;
        }

        System.out.print("Group name: ");
        String groupName = scanner.nextLine().trim();

        Group group = groupRepo.get(groupName);
        if (group == null) {
            log.warn("Group not found in remove operation: {}", groupName);
            System.out.println("Group not found: " + groupName);
            return;
        }

        if (group.removeStudent(student)) {
            System.out.println("Removed " + student.getFirstName() + " " +
                    student.getLastName() + " from " + groupName);
        } else {
            System.out.println("Student not in this group");
        }
    }

    private static void showAllStudents() {
        log.debug("Displaying all students (total: {})", studentRepo.size());
        System.out.println("=== All Students (" + studentRepo.size() + ") ===");

        if (studentRepo.isEmpty()) {
            System.out.println("No students in repository.");
            return;
        }

        List<Student> students = new ArrayList<>(studentRepo.values());
        students.sort(Comparator.comparing(Student::getLastName)
                .thenComparing(Student::getFirstName));

        for (Student s : students) {
            String groupName = GroupRegistry.assignedGroup(s.getId());
            String groupInfo = groupName != null ? " [Group: " + groupName + "]" : "";
            System.out.printf("%s: %s avg=%.2f%s%n",
                    s.getId(),
                    s,
                    s.average().orElse(0.0),
                    groupInfo);
        }
    }

    private static void showAllGroups() {
        log.debug("Displaying all groups (total: {})", groupRepo.size());
        System.out.println("=== All Groups (" + groupRepo.size() + ") ===");

        if (groupRepo.isEmpty()) {
            System.out.println("No groups in repository.");
            return;
        }

        for (Group g : groupRepo.values()) {
            System.out.println(g);
        }
    }

    private static void showGroupDetails() {
        System.out.print("Group name: ");
        String groupName = scanner.nextLine().trim();

        Group group = groupRepo.get(groupName);
        if (group == null) {
            log.warn("Group not found in show details: {}", groupName);
            System.out.println("Group not found: " + groupName);
            return;
        }

        log.debug("Showing details for group: {}", groupName);
        System.out.println("\n=== " + group + " ===");
        System.out.println("Members:");

        if (group.getMembers().isEmpty()) {
            System.out.println("  (no members)");
            return;
        }

        List<Student> members = new ArrayList<>(group.getMembers());
        members.sort(Comparator.comparing(Student::getLastName)
                .thenComparing(Student::getFirstName));

        for (Student s : members) {
            System.out.printf("  - %s %s (%s) avg=%.2f%n",
                    s.getFirstName(),
                    s.getLastName(),
                    s.getIndexNumber(),
                    s.average().orElse(0.0));
        }

        OptionalDouble groupAvg = members.stream()
                .mapToDouble(s -> s.average().orElse(0.0))
                .average();

        System.out.printf("\nGroup average: %.2f%n", groupAvg.orElse(0.0));
    }

    // ===== HELPER METHODS =====

    private static List<Double> parseGrades(String gradesRaw) {
        List<Double> grades = new ArrayList<>();
        String inner = gradesRaw.replace("[", "").replace("]", "").trim();
        if (!inner.isEmpty()) {
            String[] parts = inner.split(",");
            for (String p : parts) {
                try {
                    grades.add(Double.parseDouble(p.trim()));
                } catch (NumberFormatException e) {
                    log.warn("Invalid grade format: {}", p);
                }
            }
        }
        return grades;
    }

    private static String formatGrades(List<Double> grades) {
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

    private static List<String> parseStudentIds(String idsRaw) {
        List<String> ids = new ArrayList<>();
        String inner = idsRaw.replace("[", "").replace("]", "").trim();
        if (!inner.isEmpty()) {
            String[] parts = inner.split(",");
            for (String p : parts) {
                ids.add(p.trim());
            }
        }
        return ids;
    }

    private static String formatStudentIds(Set<Student> students) {
        if (students.isEmpty())
            return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Student s : students) {
            if (!first)
                sb.append(",");
            sb.append(s.getId());
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }
}