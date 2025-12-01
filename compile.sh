#!/bin/bash

# Compile script for Movie Management System
# This script compiles all Java source files

echo "Compiling Movie Management System..."
echo "======================================"

# Create output directory if it doesn't exist
mkdir -p target/classes

# Clean previous compilation
echo "Cleaning previous compilation..."
rm -rf target/classes/*

# Find all Java files in src directory
echo "Finding source files..."
find src -name "*.java" > sources.txt

if [ ! -s sources.txt ]; then
    echo "✗ No Java source files found in src/"
    rm -f sources.txt
    exit 1
fi

# Build classpath from lib directory
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

# Compile all source files
# Target Java 21 for Eclipse compatibility
echo "Compiling (targeting Java 21)..."
javac --release 21 -d target/classes -cp "$CLASSPATH" @sources.txt

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Compilation successful!"
    echo "Compiled classes are in target/classes/"
    rm -f sources.txt
else
    echo ""
    echo "✗ Compilation failed!"
    rm -f sources.txt
    exit 1
fi

