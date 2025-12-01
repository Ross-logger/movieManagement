#!/bin/bash

# Run script for Movie Management System
# This script runs the compiled Main class

echo "Running Movie Management System..."
echo "==================================="
echo ""

# Check if classes are compiled
if [ ! -d "target/classes" ] || [ -z "$(ls -A target/classes 2>/dev/null)" ]; then
    echo "✗ Classes not found. Compiling now..."
    ./compile.sh
    if [ $? -ne 0 ]; then
        echo "✗ Compilation failed. Cannot run the system."
        exit 1
    fi
    echo ""
fi

# Build classpath
CLASSPATH="target/classes"
if [ -d "lib" ]; then
    for jar in lib/*.jar; do
        if [ -f "$jar" ]; then
            CLASSPATH="$CLASSPATH:$jar"
        fi
    done
fi

# Run the Main class
java -cp "$CLASSPATH" src.Main

