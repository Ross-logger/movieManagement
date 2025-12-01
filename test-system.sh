#!/bin/bash

# Test script for Movie Management System
# This script compiles and runs all JUnit tests

echo "Running Movie Management System Tests..."
echo "========================================"
echo ""

# Create output directories if they don't exist
mkdir -p target/classes
mkdir -p target/test-classes

# Build classpath for compilation
CLASSPATH=""
if [ -d "lib" ]; then
    for jar in lib/*.jar; do
        if [ -f "$jar" ]; then
            if [ -z "$CLASSPATH" ]; then
                CLASSPATH="$jar"
            else
                CLASSPATH="$CLASSPATH:$jar"
            fi
        fi
    done
fi

# Compile source files first (tests depend on source)
# Target Java 21 for Eclipse compatibility
echo "Step 1: Compiling source files (targeting Java 21)..."
find src -name "*.java" > sources.txt
javac --release 21 -d target/classes -cp "$CLASSPATH" @sources.txt

if [ $? -ne 0 ]; then
    echo "✗ Source compilation failed!"
    rm -f sources.txt
    exit 1
fi
rm -f sources.txt

# Build classpath for test compilation (includes compiled source classes)
TEST_CLASSPATH="target/classes"
if [ -d "lib" ]; then
    for jar in lib/*.jar; do
        if [ -f "$jar" ]; then
            TEST_CLASSPATH="$TEST_CLASSPATH:$jar"
        fi
    done
fi

# Compile test files
# Target Java 21 for Eclipse compatibility
echo "Step 2: Compiling test files (targeting Java 21)..."
find test -name "*.java" > test-sources.txt
javac --release 21 -d target/test-classes -cp "$TEST_CLASSPATH" @test-sources.txt

if [ $? -ne 0 ]; then
    echo "✗ Test compilation failed!"
    rm -f test-sources.txt
    exit 1
fi
rm -f test-sources.txt

# Build classpath for running tests
RUN_CLASSPATH="target/classes:target/test-classes"
if [ -d "lib" ]; then
    for jar in lib/*.jar; do
        if [ -f "$jar" ]; then
            RUN_CLASSPATH="$RUN_CLASSPATH:$jar"
        fi
    done
fi

# Run tests using JUnit Platform Console Launcher
echo ""
echo "Step 3: Running tests..."
echo ""

java -jar lib/junit-platform-console-standalone-1.10.0.jar \
    --class-path "$RUN_CLASSPATH" \
    --scan-class-path

echo ""
echo "Test execution completed!"

