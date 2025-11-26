#!/bin/bash
# Run the Movie Management System

CLASSPATH="lib/*:build/classes"

# Check if classes are compiled
if [ ! -d "build/classes" ]; then
    echo "Classes not compiled. Running compile.sh first..."
    ./compile.sh
fi

# Run the Movie Management System
java -cp "$CLASSPATH" movieManagement.src.Main

