import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import i2jp.oop.Group;
import i2jp.oop.GroupRegistry;
import i2jp.oop.Person;
import i2jp.oop.Student;

public class StudentManagerApp {
    private static final Map<String, Student> studentRepo = new HashMap<>();
    private static final Map<String, Group> groupRepo = new HashMap<>();

    private static Properties config = new Properties();
    private static final String CONFIG_FILE = "console.properties";
    private static String delimiter = ";";
    private static String studentsFile = "students.csv";
    private static String groupsFile = "groups.csv";

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadConfiguration();

        System.out.println("=== Student & Group Manager ===");
        System.out.println("Configuration loaded from " + CONFIG_FILE);
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
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
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }

        System.out.println("Goodbye!");
        scanner.close();
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

    private static void loadConfiguration() {
        Path configPath = Paths.get(CONFIG_FILE);

        if (!Files.exists(configPath)) {
            createDefaultConfiguration(configPath);
        } else {
            try (InputStream in = Files.newInputStream(configPath)) {
                config.load(in);
                delimiter = config.getProperty("delimiter", ";");
                studentsFile = config.getProperty("students", "students.csv");
                groupsFile = config.getProperty("groups", "groups.csv");
            } catch (IOException e) {
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
            System.out.println("[INFO] Created default configuration: " + CONFIG_FILE);
        } catch (IOException e) {
            System.out.println("[ERROR] Could not create configuration file: " + e.getMessage());
        }

        delimiter = ";";
        studentsFile = "students.csv";
        groupsFile = "groups.csv";
    }

    private static void showConfiguration() {
        System.out.println("=== Current Configuration ===");
        System.out.println("Delimiter: " + delimiter);
        System.out.println("Students file: " + studentsFile);
        System.out.println("Groups file: " + groupsFile);
    }

    private static void editConfiguration() {
        System.out.println("=== Edit Configuration ===");
        System.out.print("New delimiter (current: " + delimiter + "): ");
        String newDelim = scanner.nextLine().trim();
        if (!newDelim.isEmpty())
            delimiter = newDelim;

        System.out.print("New students file (current: " + studentsFile + "): ");
        String newStudents = scanner.nextLine().trim();
        if (!newStudents.isEmpty())
            studentsFile = newStudents;

        System.out.print("New groups file (current: " + groupsFile + "): ");
        String newGroups = scanner.nextLine().trim();
        if (!newGroups.isEmpty())
            groupsFile = newGroups;

        config.setProperty("delimiter", delimiter);
        config.setProperty("students", studentsFile);
        config.setProperty("groups", groupsFile);

        try (OutputStream out = Files.newOutputStream(Paths.get(CONFIG_FILE))) {
            config.store(out, "Student Manager Configuration");
            System.out.println("[INFO] Configuration saved successfully.");
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save configuration: " + e.getMessage());
        }
    }

    private static void loadStudentsFromCsv() throws IOException {
        Path path = Paths.get(studentsFile);
        if (!Files.exists(path)) {
            System.out.println("File not found: " + studentsFile);
            return;
        }

        List<String> lines = Files.readAllLines(path);
        int count = 0;

        for (String line : lines) {
            String[] fields = line.split(Pattern.quote(delimiter));
            if (fields.length != 6) {
                System.out.println("Skipping malformed line: " + line);
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
                    System.out.println("Invalid grade " + g + " for student " + index);
                }
            }

            studentRepo.put(student.getId(), student);
            count++;
        }

        System.out.println("Imported " + count + " students from " + studentsFile);
    }

    private static void saveStudentsToCsv() throws IOException {
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
        }

        Files.write(Paths.get(studentsFile), lines);
        System.out.println("Exported " + lines.size() + " students to " + studentsFile);
    }

    private static void loadGroupsFromCsv() throws IOException {
        Path path = Paths.get(groupsFile);
        if (!Files.exists(path)) {
            System.out.println("File not found: " + groupsFile);
            return;
        }

        List<String> lines = Files.readAllLines(path);
        int count = 0;

        for (String line : lines) {
            String[] fields = line.split(Pattern.quote(delimiter));
            if (fields.length != 3) {
                System.out.println("Skipping malformed line: " + line);
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
                        System.out.println("Warning: Student " + studentId +
                                " already in group " + GroupRegistry.assignedGroup(studentId));
                    }
                } else {
                    System.out.println("Warning: Student ID " + studentId + " not found");
                }
            }

            groupRepo.put(groupName, group);
            count++;
        }

        System.out.println("Imported " + count + " groups from " + groupsFile);
    }

    private static void saveGroupsToCsv() throws IOException {
        List<String> lines = new ArrayList<>();

        for (Group g : groupRepo.values()) {
            String studentIds = formatStudentIds(g.getMembers());
            String line = String.join(delimiter,
                    g.getName(),
                    g.getDescription(),
                    studentIds);
            lines.add(line);
        }

        Files.write(Paths.get(groupsFile), lines);
        System.out.println("Exported " + lines.size() + " groups to " + groupsFile);
    }

    private static void addNewStudent() {
        System.out.println("=== Add New Student ===");
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
            System.out.println("Invalid gender, using OTHER");
        }

        System.out.print("Index number: ");
        String index = scanner.nextLine().trim();

        Student student = new Student(firstName, lastName, birthDate, gender, index);
        studentRepo.put(student.getId(), student);

        System.out.println("Student created with ID: " + student.getId());
    }

    private static void addNewGroup() {
        System.out.println("=== Add New Group ===");
        System.out.print("Group name: ");
        String name = scanner.nextLine().trim();

        if (groupRepo.containsKey(name)) {
            System.out.print("Group already exists. Overwrite? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                System.out.println("Cancelled.");
                return;
            }
        }

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        Group group = new Group(name, description);
        groupRepo.put(name, group);

        System.out.println("Group created: " + name);
    }

    private static void assignStudentToGroup() {
        System.out.println("=== Assign Student to Group ===");
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentRepo.get(studentId);
        if (student == null) {
            System.out.println("Student not found: " + studentId);
            return;
        }

        System.out.print("Group name: ");
        String groupName = scanner.nextLine().trim();

        Group group = groupRepo.get(groupName);
        if (group == null) {
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
            System.out.println("Student not found: " + studentId);
            return;
        }

        System.out.print("Group name: ");
        String groupName = scanner.nextLine().trim();

        Group group = groupRepo.get(groupName);
        if (group == null) {
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
            System.out.println("Group not found: " + groupName);
            return;
        }

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

    private static List<Double> parseGrades(String gradesRaw) {
        List<Double> grades = new ArrayList<>();
        String inner = gradesRaw.replace("[", "").replace("]", "").trim();
        if (!inner.isEmpty()) {
            String[] parts = inner.split(",");
            for (String p : parts) {
                try {
                    grades.add(Double.parseDouble(p.trim()));
                } catch (NumberFormatException e) {
                    // Skip invalid grades
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