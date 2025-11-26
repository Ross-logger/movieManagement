#!/bin/bash
# Compile the Movie Management System with JUnit on classpath

# Create classpath with all JUnit JARs
CLASSPATH="lib/*"

# Find all Java files in movieManagement/src and movieManagement/data
SOURCE_FILES=$(find movieManagement/src movieManagement/data -name "*.java" 2>/dev/null | tr '\n' ' ')

# Compile movieManagement source files from src directory
if [ -n "$SOURCE_FILES" ]; then
    javac -cp "$CLASSPATH" -d build/classes $SOURCE_FILES
else
    echo "No source files found in movieManagement/src"
fi

# Compile test source
if [ -d "movieManagement/testMovie" ]; then
    TEST_FILES=$(find movieManagement/testMovie -name "*.java" | tr '\n' ' ')
    if [ -n "$TEST_FILES" ]; then
        javac -cp "$CLASSPATH:build/classes" -d build/test-classes $TEST_FILES
    fi
fi

echo "Compilation complete!"
