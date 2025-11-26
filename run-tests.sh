#!/bin/bash
# Run JUnit tests

CLASSPATH="lib/junit-platform-console-standalone-1.10.0.jar:build/classes:build/test-classes"

# Run tests using JUnit Platform Console Launcher
java -cp "$CLASSPATH" org.junit.platform.console.ConsoleLauncher --class-path build/test-classes --scan-class-path

