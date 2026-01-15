# Makefile for Student & Group Manager with Log4j 2

# Directories
SRC_DIR = src
BIN_DIR = bin
LIB_DIR = lib

# Java compiler and runtime
JAVAC = javac
JAVA = java

# Classpath
CP_COMPILE = $(LIB_DIR)/*:$(SRC_DIR)
CP_RUN = $(BIN_DIR):$(LIB_DIR)/*

# Main class (in default package, not i2jp.oop)
MAIN_CLASS = StudentManagerApp

# Source files
SOURCES = $(SRC_DIR)/i2jp/oop/Person.java \
          $(SRC_DIR)/i2jp/oop/Student.java \
          $(SRC_DIR)/i2jp/oop/GroupRegistry.java \
          $(SRC_DIR)/i2jp/oop/Group.java \
          $(SRC_DIR)/i2jp/oop/CsvFormatException.java \
          $(SRC_DIR)/$(MAIN_CLASS).java

# Default target
all: compile

# Compile all Java files
compile:
	@echo "Compiling Java files..."
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -cp $(CP_COMPILE) -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(SOURCES)
	@echo "Copying log4j2.xml to bin directory..."
	@cp $(SRC_DIR)/log4j2.xml $(BIN_DIR)/
	@echo "Compilation successful!"

# Run the application
run: compile
	@echo "Running Student Manager Application..."
	$(JAVA) -cp $(CP_RUN) $(MAIN_CLASS)

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	@rm -rf $(BIN_DIR)
	@rm -f *.class
	@echo "Clean complete!"

# Clean everything including logs and generated files
cleanall: clean
	@echo "Cleaning logs and generated files..."
	@rm -rf logs
	@rm -f console.properties
	@rm -f students.csv groups.csv
	@echo "Clean all complete!"

# Create sample test data
testdata:
	@echo "Creating sample test data..."
	@echo "0000001;S12345;Anna;Kowalska;15.03.2003;[5.0,4.5,4.0]" > students.csv
	@echo "0000002;S67890;Piotr;Nowak;02.09.2002;[4.0,4.0,3.5]" >> students.csv
	@echo "0000003;S11111;Julia;Mazur;01.01.2004;[3.5,3.0]" >> students.csv
	@echo "G1;Java Monday;[0000001,0000002]" > groups.csv
	@echo "G2;Java Friday;[0000003]" >> groups.csv
	@echo "Test data created: students.csv and groups.csv"

# Show help
help:
	@echo "Student Manager - Makefile Commands:"
	@echo "  make compile   - Compile all Java files"
	@echo "  make run       - Compile and run the application"
	@echo "  make clean     - Remove compiled files"
	@echo "  make cleanall  - Remove compiled files, logs, and data"
	@echo "  make testdata  - Create sample CSV test files"
	@echo "  make help      - Show this help message"

.PHONY: all compile run clean cleanall testdata help