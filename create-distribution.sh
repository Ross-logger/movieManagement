#!/bin/bash
# Create distribution zip file

DIST_NAME="MovieManagementSystem"
ZIP_FILE="${DIST_NAME}.zip"
BASE_DIR="/Users/admin/Downloads/MovieManagement"

cd "${BASE_DIR}"

echo "Creating distribution package: ${ZIP_FILE}"

# Create temporary directory
TEMP_DIR=$(mktemp -d)
DIST_DIR="${TEMP_DIR}/${DIST_NAME}"

mkdir -p "${DIST_DIR}"

# Copy jar file
echo "Copying JAR file..."
cp target/movie-management-system-1.0-SNAPSHOT.jar "${DIST_DIR}/"

# Copy source code
echo "Copying source code..."
cp -r movieManagement/src "${DIST_DIR}/"
cp -r movieManagement/data "${DIST_DIR}/"

# Copy test code
echo "Copying test code..."
cp -r movieManagement/test "${DIST_DIR}/"

# Copy configuration files
echo "Copying configuration files..."
cp pom.xml "${DIST_DIR}/"
cp README.md "${DIST_DIR}/"

# Copy scripts
echo "Copying scripts..."
[ -f run-system.sh ] && cp run-system.sh "${DIST_DIR}/"
[ -f run-tests.sh ] && cp run-tests.sh "${DIST_DIR}/"
[ -f compile.sh ] && cp compile.sh "${DIST_DIR}/"

# Copy coverage report (optional but useful)
echo "Copying coverage report..."
if [ -d "target/site/jacoco" ]; then
    mkdir -p "${DIST_DIR}/coverage-report"
    cp -r target/site/jacoco "${DIST_DIR}/coverage-report/"
fi

# Create a README for the distribution
cat > "${DIST_DIR}/DISTRIBUTION-README.txt" << 'README'
Movie Management System - Distribution Package
==============================================

This package contains:

1. JAR FILE:
   - movie-management-system-1.0-SNAPSHOT.jar
   - Executable JAR with Main class configured
   - Run with: java -jar movie-management-system-1.0-SNAPSHOT.jar

2. SOURCE CODE:
   - src/ - All source files
   - data/ - Data files (PredefinedAdmins, PredefinedMoviesList)

3. TEST CODE:
   - test/ - All test files (312 test methods)

4. CONFIGURATION:
   - pom.xml - Maven configuration

5. DOCUMENTATION:
   - README.md - Project documentation
   - coverage-report/ - Test coverage report (HTML)

6. SCRIPTS:
   - run-system.sh - Run the application
   - run-tests.sh - Run tests
   - compile.sh - Compile the project

QUICK START:
============

To run the application:
  java -jar movie-management-system-1.0-SNAPSHOT.jar

To build from source:
  mvn clean package

To run tests:
  mvn test

To view coverage report:
  Open coverage-report/jacoco/index.html in a web browser

REQUIREMENTS:
=============
- Java 18 or higher
- Maven (for building from source)
README

# Create the zip file
echo "Creating ZIP file..."
cd "${TEMP_DIR}"
zip -r "${ZIP_FILE}" "${DIST_NAME}" > /dev/null

# Move zip to original directory
mv "${ZIP_FILE}" "${BASE_DIR}/"

# Clean up
rm -rf "${TEMP_DIR}"

echo ""
echo "✓ Distribution package created: ${ZIP_FILE}"
echo "  Location: ${BASE_DIR}/${ZIP_FILE}"
echo ""
echo "Package contents summary:"
unzip -l "${BASE_DIR}/${ZIP_FILE}" | tail -5
echo ""
echo "Total files in package:"
unzip -l "${BASE_DIR}/${ZIP_FILE}" | grep -E "\.(jar|java|xml|md|sh|html)$" | wc -l
