# Name of the binary to produce
BINARY_NAME = TaskManager

# Explicit list of source files
SOURCES :=  src/MainPage.java \
			src/FileManager.java \
			src/TaskManager.java \
			src/Task.java \
			src/TaskListCellRenderer.java \
			src/TaskDetails.java \
			src/SubTaskManager.java

# Path to the JSON library JAR file
JSON_LIB := lib/json-20240303.jar

# Make sure the shell and encoding are set to UTF-8
SHELL := /bin/bash
export LC_ALL=C.UTF-8

# Default command
default: build

# Build command
build:
	@echo -e "Building the project..."
	javac -d build -cp $(JSON_LIB) $(SOURCES) 

# Command to clean the project (remove the binary)
clean:
	@echo -e "Cleaning..."
	rm -rf build
	rm -f $(BINARY_NAME)

# Command to run the program
run:
	@echo -e "Running the program..."
	java -cp build src/$(BINARY_NAME) $(JSON_LIB)

# 'Phony' option to indicate that 'clean', 'run', and 'build' are not files
.PHONY: build clean run