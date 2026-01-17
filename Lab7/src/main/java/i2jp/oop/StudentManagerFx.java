package i2jp.oop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// ==================== DOMAIN CLASSES ====================

class Student {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String indexNumber;
    private String id;
    private Double[] grades;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final Set<Double> VALID_GRADES = Set.of(2.0, 3.0, 3.5, 4.0, 4.5, 5.0);

    public Student(String firstName, String lastName, LocalDate birthDate, String indexNumber) {
        this(null, firstName, lastName, birthDate, indexNumber, "[]");
    }

    public Student(String firstName, String lastName, LocalDate birthDate, String indexNumber, String grades) {
        this(null, firstName, lastName, birthDate, indexNumber, grades);
    }

    public Student(String id, String firstName, String lastName, LocalDate birthDate, String indexNumber,
            String grades) {
        validateFirstName(firstName);
        validateLastName(lastName);
        validateBirthDate(birthDate);
        validateIndexNumber(indexNumber);
        validateGrades(grades);

        System.out.println("Creating student: " + firstName + " " + lastName + ", DOB: " + birthDate + ", Index: "
                + indexNumber + ", Grades: " + grades);

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.indexNumber = indexNumber;
        this.grades = fillGrades(grades);
    }

    private Double[] fillGrades(String grades) {
        if (grades == null)
            return new Double[0];

        String s = grades.trim();
        if (s.startsWith("[") && s.endsWith("]")) {
            String inner = s.substring(1, s.length() - 1).trim();
            if (inner.isEmpty())
                return new Double[0];

            String[] parts = inner.split(",");
            Double[] result = new Double[parts.length];
            for (int i = 0; i < parts.length; i++) {
                String p = parts[i].trim();
                if (p.isEmpty()) {
                    result[i] = null;
                    continue;
                }
                try {
                    double val = Double.parseDouble(p);
                    if (!VALID_GRADES.contains(val)) {
                        throw new IllegalArgumentException("Invalid grade value: " + val);
                    }
                    result[i] = val;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid grade format: '" + p + "'");
                }
            }
            return result;
        }

        // If not bracketed, try to parse as single value or comma-separated without
        // brackets
        if (s.isEmpty())
            return new Double[0];
        String[] parts = s.split(",");
        Double[] result = new Double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].trim();
            if (p.isEmpty()) {
                result[i] = null;
                continue;
            }
            try {
                double val = Double.parseDouble(p);
                if (!VALID_GRADES.contains(val)) {
                    throw new IllegalArgumentException("Invalid grade value: " + val);
                }
                result[i] = val;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid grade format: '" + p + "'");
            }
        }
        return result;
    }

    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
    }

    private void validateIndexNumber(String indexNumber) {
        if (indexNumber == null || indexNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Index number cannot be empty");
        }
    }

    private void validateGrades(String grades) {
        if (grades == null)
            throw new IllegalArgumentException("Grades cannot be null");
    }

    public void addGrade(double grade) {
        if (!VALID_GRADES.contains(grade)) {
            throw new IllegalArgumentException("Invalid grade: " + grade + ". Allowed: 2.0, 3.0, 3.5, 4.0, 4.5, 5.0");
        }
        Double[] newGrades = Arrays.copyOf(this.grades, this.grades.length + 1);
        newGrades[newGrades.length - 1] = grade;
        this.grades = newGrades;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public String getId() {
        return id;
    }

    public Double[] getGrades() {
        return Arrays.copyOf(grades, grades.length);
    }

    public String getBirthDateFormatted() {
        return birthDate.format(DATE_FORMAT);
    }

    public double getAverageGrade() {
        if (grades == null || grades.length == 0)
            return 0;
        double sum = 0.0;
        int count = 0;
        for (Double g : grades) {
            if (g != null) {
                sum += g;
                count++;
            }
        }
        return count == 0 ? 0 : sum / count;
    }

    private String gradesToCsvField() {
        if (grades == null || grades.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < grades.length; i++) {
            if (i > 0)
                sb.append(',');
            Double g = grades[i];
            sb.append(g == null ? "" : g.toString());
        }
        sb.append(']');
        return sb.toString();
    }

    public String toCsvLine() {
        // semicolon format: id;index;first;last;date;[grades]
        String idField = id == null ? "" : id;
        return String.join(";", idField, indexNumber, firstName, lastName, getBirthDateFormatted(), gradesToCsvField());
    }

    public static Student fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV line is empty");
        }
        String s = line.trim();
        try {
            if (s.contains(";")) {
                // Expected semicolon format: id;index;first;last;date;[grades]
                String[] parts = s.split(";", 6);
                if (parts.length < 5) {
                    throw new IllegalArgumentException("Invalid semicolon CSV format: expected at least 5 fields");
                }
                LocalDate date = LocalDate.parse(parts[4].trim(), DATE_FORMAT);
                String gradesField = parts.length >= 6 ? parts[5].trim() : "[]";
                // parts: 0=id,1=index,2=first,3=last,4=date,5=grades
                return new Student(parts[0].trim(), parts[2].trim(), parts[3].trim(), date, parts[1].trim(),
                        gradesField);
            } else {
                // Split into at most 5 parts so the grades field (last) may contain commas
                String[] parts = s.split(",", 5);
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Invalid CSV format: expected at least 4 fields");
                }
                LocalDate date = LocalDate.parse(parts[2].trim(), DATE_FORMAT);
                String gradesField = parts.length >= 5 ? parts[4].trim() : "[]";
                return new Student(parts[0].trim(), parts[1].trim(), date, parts[3].trim(), gradesField);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + e.getParsedString());
        }
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + indexNumber + ")";
    }
}

class Group {
    private String name;
    private String description;
    private List<Student> students;
    private int maxCapacity;

    public Group(String name, String description) {
        validateName(name);
        this.name = name;
        this.description = description == null ? "" : description;
        this.students = new ArrayList<>();
        this.maxCapacity = 50;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
    }

    public void addStudent(Student student) {
        if (students.size() >= maxCapacity) {
            throw new IllegalStateException("Group is at maximum capacity (" + maxCapacity + ")");
        }
        if (students.stream().anyMatch(s -> s.getIndexNumber().equals(student.getIndexNumber()))) {
            throw new IllegalArgumentException(
                    "Student with index " + student.getIndexNumber() + " already exists in this group");
        }
        students.add(student);
    }

    public void removeStudent(Student student) {
        if (!students.remove(student)) {
            throw new IllegalArgumentException("Student not found in this group");
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    @Override
    public String toString() {
        return name + " (" + students.size() + " students)";
    }
}

class StudentRegistry {
    private Map<String, Group> groups;
    private Map<String, Student> studentsById;

    public StudentRegistry() {
        this.groups = new HashMap<>();
        this.studentsById = new HashMap<>();
    }

    public void addGroup(Group group) {
        if (groups.containsKey(group.getName())) {
            throw new IllegalArgumentException("Group '" + group.getName() + "' already exists");
        }
        groups.put(group.getName(), group);
    }

    public Group getGroup(String name) {
        return groups.get(name);
    }

    public List<Group> getAllGroups() {
        return new ArrayList<>(groups.values());
    }

    public void transferStudent(Student student, Group fromGroup, Group toGroup) {
        fromGroup.removeStudent(student);
        try {
            toGroup.addStudent(student);
        } catch (Exception e) {
            fromGroup.addStudent(student);
            throw e;
        }
    }

    public void importFromCsv(File file, Group targetGroup) throws IOException {
        int imported = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                try {
                    Student student = Student.fromCsvLine(line);
                    // register and add to target group (checks duplicate id)
                    addStudentToGroup(student, targetGroup);
                    imported++;
                } catch (Exception e) {
                    throw new IOException("Error on line: " + line + " - " + e.getMessage());
                }
            }
        }
    }

    public void loadStudentsFile() throws IOException {
        File studentsFile = new File("students.csv");
        if (!studentsFile.exists())
            throw new IOException("students.csv not found in project root");
        try (BufferedReader reader = new BufferedReader(new FileReader(studentsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                Student s = Student.fromCsvLine(line);
                if (s.getId() != null && !s.getId().isEmpty()) {
                    if (studentsById.containsKey(s.getId())) {
                        throw new IOException("Duplicate student id in students.csv: " + s.getId());
                    }
                    studentsById.put(s.getId(), s);
                }
            }
        }
    }

    public void loadGroupsFile() throws IOException {
        File groupsFile = new File("groups.csv");
        if (!groupsFile.exists())
            throw new IOException("groups.csv not found in project root");
        // clear existing groups before loading
        this.groups.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(groupsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String s = line.trim();
                String[] parts = s.split(";", 3);
                if (parts.length < 2)
                    continue;
                String groupName = parts[1].trim();
                Group g = new Group(groupName, "");
                this.addGroup(g);
                if (parts.length == 3) {
                    String members = parts[2].trim();
                    if (members.startsWith("[") && members.endsWith("]")) {
                        String inner = members.substring(1, members.length() - 1).trim();
                        if (!inner.isEmpty()) {
                            String[] ids = inner.split(",");
                            for (String id : ids) {
                                String tid = id.trim();
                                Student st = studentsById.get(tid);
                                if (st != null) {
                                    try {
                                        g.addStudent(st);
                                    } catch (Exception e) {
                                        System.err.println("Could not add student " + tid + " to group " + groupName
                                                + ": " + e.getMessage());
                                    }
                                } else {
                                    System.err.println("Unknown student id in groups.csv: " + tid);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveStudentsFile() throws IOException {
        File studentsFile = new File("students.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(studentsFile))) {
            for (Student s : studentsById.values()) {
                writer.println(s.toCsvLine());
            }
        }
    }

    public void saveGroupsFile() throws IOException {
        File groupsFile = new File("groups.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(groupsFile))) {
            int gidx = 1;
            for (Group g : getAllGroups()) {
                String gid = "G" + gidx;
                StringBuilder sb = new StringBuilder();
                sb.append(gid).append(";").append(g.getName()).append(";");
                sb.append('[');
                List<Student> studs = g.getStudents();
                for (int i = 0; i < studs.size(); i++) {
                    if (i > 0)
                        sb.append(',');
                    String sid = studs.get(i).getId();
                    sb.append(sid == null ? "" : sid);
                }
                sb.append(']');
                writer.println(sb.toString());
                gidx++;
            }
        }
    }

    public void addStudentToGroup(Student student, Group group) {
        if (student.getId() != null && !student.getId().isEmpty()) {
            if (studentsById.containsKey(student.getId())) {
                throw new IllegalArgumentException("Student with id " + student.getId() + " already exists");
            }
            studentsById.put(student.getId(), student);
        }
        group.addStudent(student);
    }

    public Student getStudentById(String id) {
        return studentsById.get(id);
    }

    public void loadFromProjectRoot() {
        // Load students.csv and groups.csv from current working directory if present
        File studentsFile = new File("students.csv");
        File groupsFile = new File("groups.csv");
        // Clear existing
        this.groups.clear();
        this.studentsById.clear();

        if (studentsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(studentsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty())
                        continue;
                    Student s = Student.fromCsvLine(line);
                    if (s.getId() != null && !s.getId().isEmpty()) {
                        if (studentsById.containsKey(s.getId())) {
                            System.err.println("Duplicate student id in students.csv: " + s.getId() + " — skipping");
                            continue;
                        }
                        studentsById.put(s.getId(), s);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading students.csv: " + e.getMessage());
            }
        }

        if (groupsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(groupsFile))) {
                String line;
                int gid = 1;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty())
                        continue;
                    String s = line.trim();
                    String[] parts = s.split(";", 3);
                    if (parts.length < 2)
                        continue;
                    String groupName = parts[1].trim();
                    Group g = new Group(groupName, "");
                    this.addGroup(g);
                    if (parts.length == 3) {
                        String members = parts[2].trim();
                        if (members.startsWith("[") && members.endsWith("]")) {
                            String inner = members.substring(1, members.length() - 1).trim();
                            if (!inner.isEmpty()) {
                                String[] ids = inner.split(",");
                                for (String id : ids) {
                                    String tid = id.trim();
                                    Student st = studentsById.get(tid);
                                    if (st != null) {
                                        try {
                                            g.addStudent(st);
                                        } catch (Exception e) {
                                            System.err.println("Could not add student " + tid + " to group " + groupName
                                                    + ": " + e.getMessage());
                                        }
                                    } else {
                                        System.err.println("Unknown student id in groups.csv: " + tid);
                                    }
                                }
                            }
                        }
                    }
                    gid++;
                }
            } catch (Exception e) {
                System.err.println("Error loading groups.csv: " + e.getMessage());
            }
        }
    }

    public void saveProjectCsvs() throws IOException {
        // write students.csv and groups.csv in current working directory
        File studentsFile = new File("students.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(studentsFile))) {
            for (Student s : studentsById.values()) {
                writer.println(s.toCsvLine());
            }
        }

        File groupsFile = new File("groups.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(groupsFile))) {
            int gidx = 1;
            for (Group g : getAllGroups()) {
                String gid = "G" + gidx;
                StringBuilder sb = new StringBuilder();
                sb.append(gid).append(";").append(g.getName()).append(";");
                sb.append('[');
                List<Student> studs = g.getStudents();
                for (int i = 0; i < studs.size(); i++) {
                    if (i > 0)
                        sb.append(',');
                    String sid = studs.get(i).getId();
                    sb.append(sid == null ? "" : sid);
                }
                sb.append(']');
                writer.println(sb.toString());
                gidx++;
            }
        }
    }

    public void exportToCsv(File file, Group group) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Student student : group.getStudents()) {
                writer.println(student.toCsvLine());
            }
        }
    }
}

// ==================== GUI APPLICATION ====================

public class StudentManagerFx extends Application {
    private StudentRegistry registry;
    private ListView<Group> groupsListView;
    private TableView<Student> studentsTable;
    private ObservableList<Group> groupsList;
    private ObservableList<Student> studentsList;
    private Label statusLabel;
    private Group currentGroup;

    @Override
    public void start(Stage stage) {
        registry = new StudentRegistry();
        groupsList = FXCollections.observableArrayList();
        studentsList = FXCollections.observableArrayList();

        BorderPane root = new BorderPane();
        root.setTop(createMenuBar(stage));
        root.setLeft(createGroupsPanel());
        root.setCenter(createStudentsPanel());
        root.setRight(createActionsPanel());
        root.setBottom(createStatusBar());

        Scene scene = new Scene(root, 1100, 650);
        stage.setScene(scene);
        stage.setTitle("Student & Group Manager — JavaFX");
        stage.show();

        // Try to load students.csv and groups.csv from project root
        registry.loadFromProjectRoot();
        groupsList.addAll(registry.getAllGroups());
        if (groupsList.isEmpty()) {
            updateStatus("No groups loaded from project files");
        } else {
            updateStatus("Loaded groups and students from project files");
        }
    }

    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> stage.close());
        fileMenu.getItems().addAll(exit);
        menuBar.getMenus().add(fileMenu);

        return menuBar;
    }

    private VBox createGroupsPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(250);
        panel.setStyle("-fx-background-color: #f4f4f4;");

        Label title = new Label("Groups");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        groupsListView = new ListView<>(groupsList);
        groupsListView.setPrefHeight(400);
        groupsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadStudentsForGroup(newVal);
            }
        });

        Button addGroupBtn = new Button("Add Group");
        addGroupBtn.setMaxWidth(Double.MAX_VALUE);
        addGroupBtn.setOnAction(e -> handleAddGroup());

        Button editDescBtn = new Button("Edit Description");
        editDescBtn.setMaxWidth(Double.MAX_VALUE);
        editDescBtn.setOnAction(e -> handleEditDescription());

        panel.getChildren().addAll(title, groupsListView, addGroupBtn, editDescBtn);
        return panel;
    }

    private VBox createStudentsPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        Label title = new Label("Students");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        studentsTable = new TableView<>(studentsList);

        TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        firstNameCol.setPrefWidth(120);

        TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        lastNameCol.setPrefWidth(120);

        TableColumn<Student, String> birthDateCol = new TableColumn<>("Birth Date");
        birthDateCol
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDateFormatted()));
        birthDateCol.setPrefWidth(100);

        TableColumn<Student, String> indexCol = new TableColumn<>("Index Number");
        indexCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIndexNumber()));
        indexCol.setPrefWidth(120);

        TableColumn<Student, String> avgGradeCol = new TableColumn<>("Avg Grade");
        avgGradeCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getAverageGrade())));
        avgGradeCol.setPrefWidth(80);

        studentsTable.getColumns().addAll(firstNameCol, lastNameCol, birthDateCol, indexCol, avgGradeCol);

        panel.getChildren().addAll(title, studentsTable);
        VBox.setVgrow(studentsTable, Priority.ALWAYS);

        return panel;
    }

    private VBox createActionsPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(280);
        panel.setStyle("-fx-background-color: #f4f4f4;");

        Label title = new Label("Actions");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Add Student Form
        Label addStudentLabel = new Label("Add Student");
        addStudentLabel.setStyle("-fx-font-weight: bold;");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField birthDateField = new TextField();
        birthDateField.setPromptText("Birth Date (dd.MM.yyyy)");

        TextField indexField = new TextField();
        indexField.setPromptText("Index Number");

        Button addStudentBtn = new Button("Add Student");
        addStudentBtn.setMaxWidth(Double.MAX_VALUE);
        addStudentBtn.setOnAction(e -> {
            handleAddStudent(firstNameField.getText(), lastNameField.getText(),
                    birthDateField.getText(), indexField.getText());
            firstNameField.clear();
            lastNameField.clear();
            birthDateField.clear();
            indexField.clear();
        });

        Separator sep1 = new Separator();

        // Project file operations
        Button loadStudentsBtn = new Button("Load Students");
        loadStudentsBtn.setMaxWidth(Double.MAX_VALUE);
        loadStudentsBtn.setOnAction(e -> handleLoadStudents());

        Button loadGroupsBtn = new Button("Load Groups");
        loadGroupsBtn.setMaxWidth(Double.MAX_VALUE);
        loadGroupsBtn.setOnAction(e -> handleLoadGroups());

        Button saveStudentsBtn = new Button("Save Students");
        saveStudentsBtn.setMaxWidth(Double.MAX_VALUE);
        saveStudentsBtn.setOnAction(e -> handleSaveStudents());

        Button saveGroupsBtn = new Button("Save Group");
        saveGroupsBtn.setMaxWidth(Double.MAX_VALUE);
        saveGroupsBtn.setOnAction(e -> handleSaveGroups());

        Separator sep0 = new Separator();

        // Transfer Student
        Label transferLabel = new Label("Transfer Student");
        transferLabel.setStyle("-fx-font-weight: bold;");

        ComboBox<Group> targetGroupCombo = new ComboBox<>();
        targetGroupCombo.setPromptText("Select target group");
        targetGroupCombo.setMaxWidth(Double.MAX_VALUE);
        targetGroupCombo.setItems(groupsList);

        Button transferBtn = new Button("Move to Group");
        transferBtn.setMaxWidth(Double.MAX_VALUE);
        transferBtn.setOnAction(e -> handleTransferStudent(targetGroupCombo.getValue()));

        Separator sep2 = new Separator();

        // Remove Student
        Button removeStudentBtn = new Button("Remove Selected Student");
        removeStudentBtn.setMaxWidth(Double.MAX_VALUE);
        removeStudentBtn.setOnAction(e -> handleRemoveStudent());

        panel.getChildren().addAll(
                title,
                addStudentLabel, firstNameField, lastNameField, birthDateField, indexField, addStudentBtn,
                sep1,
                loadStudentsBtn, loadGroupsBtn, saveStudentsBtn, saveGroupsBtn,
                sep0,
                transferLabel, targetGroupCombo, transferBtn,
                sep2,
                removeStudentBtn);

        return panel;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #e0e0e0;");

        statusLabel = new Label("Ready");
        statusBar.getChildren().add(statusLabel);

        return statusBar;
    }

    private void handleAddGroup() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Group");
        dialog.setHeaderText("Create a new group");
        dialog.setContentText("Group name:");

        dialog.showAndWait().ifPresent(name -> {
            try {
                TextInputDialog descDialog = new TextInputDialog();
                descDialog.setTitle("Group Description");
                descDialog.setHeaderText("Add description (optional)");
                descDialog.setContentText("Description:");

                String description = descDialog.showAndWait().orElse("");

                Group group = new Group(name, description);
                registry.addGroup(group);
                groupsList.add(group);
                updateStatus("Group '" + name + "' created successfully");
                showInfo("Success", "Group created successfully");
            } catch (Exception e) {
                showError("Error", e.getMessage());
            }
        });
    }

    private void handleEditDescription() {
        Group selected = groupsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a group to edit");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getDescription());
        dialog.setTitle("Edit Description");
        dialog.setHeaderText("Edit group description");
        dialog.setContentText("Description:");

        dialog.showAndWait().ifPresent(description -> {
            selected.setDescription(description);
            updateStatus("Description updated for group '" + selected.getName() + "'");
            groupsListView.refresh();
        });
    }

    private void handleAddStudent(String firstName, String lastName, String birthDateStr, String indexNumber) {
        if (currentGroup == null) {
            showWarning("No Group Selected", "Please select a group first");
            return;
        }

        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            Student student = new Student(firstName, lastName, birthDate, indexNumber);
            currentGroup.addStudent(student);
            studentsList.add(student);
            updateStatus("Student added: " + student);
            showInfo("Success", "Student added successfully");
        } catch (DateTimeParseException e) {
            showError("Invalid Date", "Please use format dd.MM.yyyy (e.g., 15.03.2000)");
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    private void handleTransferStudent(Group targetGroup) {
        Student selected = studentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a student to transfer");
            return;
        }
        if (targetGroup == null) {
            showWarning("No Target", "Please select a target group");
            return;
        }
        if (currentGroup == targetGroup) {
            showWarning("Same Group", "Student is already in this group");
            return;
        }

        try {
            registry.transferStudent(selected, currentGroup, targetGroup);
            studentsList.remove(selected);
            updateStatus("Student transferred: " + selected + " → " + targetGroup.getName());
            showInfo("Success", "Student transferred successfully");
        } catch (Exception e) {
            showError("Transfer Error", e.getMessage());
        }
    }

    private void handleRemoveStudent() {
        Student selected = studentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a student to remove");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Remove student?");
        confirm.setContentText("Are you sure you want to remove " + selected + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    currentGroup.removeStudent(selected);
                    studentsList.remove(selected);
                    updateStatus("Student removed: " + selected);
                } catch (Exception e) {
                    showError("Error", e.getMessage());
                }
            }
        });
    }

    private void handleLoadCsv(Stage stage) {
        if (currentGroup == null) {
            showWarning("No Group Selected", "Please select a group to import students into");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                int beforeCount = currentGroup.getStudents().size();
                registry.importFromCsv(file, currentGroup);
                loadStudentsForGroup(currentGroup);
                int imported = currentGroup.getStudents().size() - beforeCount;
                updateStatus("Imported " + imported + " students from " + file.getName());
                showInfo("Import Success", "Imported " + imported + " students successfully");
            } catch (IOException e) {
                showError("Import Error", e.getMessage());
            }
        }
    }

    private void handleLoadStudents() {
        try {
            registry.loadStudentsFile();
            updateStatus("Loaded students.csv from project root");
            showInfo("Load Success", "Loaded students.csv successfully");
        } catch (Exception e) {
            showError("Load Error", e.getMessage());
        }
    }

    private void handleLoadGroups() {
        try {
            registry.loadGroupsFile();
            groupsList.clear();
            groupsList.addAll(registry.getAllGroups());
            updateStatus("Loaded groups.csv from project root");
            showInfo("Load Success", "Loaded groups.csv successfully");
        } catch (Exception e) {
            showError("Load Error", e.getMessage());
        }
    }

    private void handleSaveStudents() {
        try {
            registry.saveStudentsFile();
            updateStatus("Saved students.csv to project root");
            showInfo("Save Success", "students.csv saved successfully");
        } catch (Exception e) {
            showError("Save Error", e.getMessage());
        }
    }

    private void handleSaveGroups() {
        try {
            registry.saveGroupsFile();
            updateStatus("Saved groups.csv to project root");
            showInfo("Save Success", "groups.csv saved successfully");
        } catch (Exception e) {
            showError("Save Error", e.getMessage());
        }
    }

    private void handleSaveCsv(Stage stage) {
        if (currentGroup == null) {
            showWarning("No Group Selected", "Please select a group to export");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName(currentGroup.getName() + ".csv");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                registry.exportToCsv(file, currentGroup);
                updateStatus("Exported " + currentGroup.getStudents().size() + " students to " + file.getName());
                showInfo("Export Success", "Students exported successfully");
            } catch (IOException e) {
                showError("Export Error", e.getMessage());
            }
        }
    }

    private void loadStudentsForGroup(Group group) {
        currentGroup = group;
        studentsList.clear();
        studentsList.addAll(group.getStudents());
        updateStatus("Loaded group: " + group.getName() + " (" + group.getStudents().size() + " students)");
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void initializeSampleData() {
        try {
            Group groupA = new Group("Group A", "Computer Science students");
            Group groupB = new Group("Group B", "Software Engineering students");

            registry.addGroup(groupA);
            registry.addGroup(groupB);

            groupA.addStudent(new Student("Jan", "Kowalski", LocalDate.of(2000, 5, 15), "123456"));
            groupA.addStudent(new Student("Anna", "Nowak", LocalDate.of(2001, 3, 20), "123457"));
            groupB.addStudent(new Student("Piotr", "Wiśniewski", LocalDate.of(2000, 8, 10), "123458"));

            groupsList.addAll(registry.getAllGroups());
            updateStatus("Sample data loaded");
        } catch (Exception e) {
            showError("Initialization Error", e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}