# Movie Management System

A comprehensive Java-based movie management system demonstrating object-oriented design principles, inheritance patterns, and software engineering best practices. This system manages movie inventory, rentals, sales, and customer reviews with a clean, maintainable architecture.

## 📋 Overview

### Project Description

The Movie Management System is a library-style application that handles:
- **Movie Catalog Management**: Store and manage movie metadata (title, director, studio, IMDB ID, release date, price)
- **Rental Operations**: Track movie rentals with borrowing history and due dates
- **Sales Operations**: Manage movie purchases and track sold inventory
- **Review System**: Allow customers to rate and review movies

### Architecture & Design Patterns

This project showcases several important software engineering concepts:

#### **Inheritance Hierarchy**
- `MovieDuplicate` (abstract base class) - Provides common functionality for all movie copies
  - `RentalMovieDuplicate` - Handles rental-specific operations
  - `SalableMovieDuplicate` - Handles sale-specific operations

#### **Key Design Principles**
- **DRY (Don't Repeat Yourself)**: Common copy functionality centralized in `MovieDuplicate`
- **Separation of Concerns**: Each class has a single, well-defined responsibility
- **Polymorphism**: Different copy types can be treated uniformly through the base class

### Project Structure

```
movieManagement/
├── src/                          # Source code
│   ├── Main.java                 # Main entry point - console interface
│   ├── authentication/           # Authentication system
│   ├── movie/                    # Movie management classes
│   ├── payment/                  # Payment processing system
│   ├── users/                    # User and membership classes
│   └── exceptions/                # Custom exceptions
├── data/                         # Data files
│   ├── PredefinedAdmins.java     # Predefined admin accounts
│   └── PredefinedMoviesList.java # Predefined movie catalog
├── test/                         # Test suite
│   ├── MainTest.java             # Main class tests (312 test methods)
│   ├── authentication/           # Authentication tests
│   ├── movie/                    # Movie tests
│   ├── payment/                  # Payment tests
│   └── users/                    # User tests
├── pom.xml                       # Maven configuration
├── compile.sh                    # Compilation script (legacy)
├── run-tests.sh                  # Test execution script (legacy)
└── run-system.sh                 # Run application script (legacy)
```

### Core Features

1. **Movie Management**
   - Create movie entries with full metadata
   - Add rental and sale copies to inventory
   - Track available copies

2. **Rental System**
   - Borrow movies with automatic due date calculation (7-day default)
   - Return movies to make them available again
   - Maintain rental history per copy

3. **Sales System**
   - Purchase movies permanently
   - Track sold inventory

4. **Review System**
   - Add customer reviews with ratings (0-10 scale)
   - Display all reviews for a movie
   - Validate rating ranges

## 📦 Distribution Package

A complete distribution package (`MovieManagementSystem.zip`) is available containing:

- **Executable JAR**: `movie-management-system-1.0-SNAPSHOT.jar` - Ready to run
- **Source Code**: Complete source files for customization
- **Test Suite**: All 312 test methods
- **Documentation**: README and coverage reports
- **Configuration**: Maven `pom.xml` for building from source

**To create a new distribution package:**
```bash
# Build the project
mvn clean package

# The distribution script will be available (if created)
# Or manually create a zip with: jar, src/, test/, pom.xml, README.md
```

## 🚀 Quickstart

### Prerequisites

- **Java Development Kit (JDK)**: Version 18 or higher
- **Maven**: Version 3.6+ (for building and testing)
- **JUnit 5**: Managed by Maven dependencies
- **Bash shell**: For running scripts (Unix/Linux/macOS) - Optional

### Building the Project

#### Option 1: Using Maven (Recommended)

```bash
# Compile the project
mvn clean compile

# Compile and package (creates JAR file)
mvn clean package

# Compile, run tests, and generate coverage report
mvn clean test
```

This will:
- Compile all source files in `movieManagement/src/`
- Compile all test files in `movieManagement/test/`
- Place compiled classes in `target/classes/` and `target/test-classes/`
- Generate JAR file in `target/` directory
- Run all tests and generate JaCoCo coverage report

#### Option 2: Using Legacy Scripts

```bash
# Make the script executable (first time only)
chmod +x compile.sh

# Compile the project
./compile.sh
```

**Note**: Maven is the recommended build tool for this project. The legacy scripts are provided for compatibility.

### Running Tests

#### Option 1: Using Maven (Recommended)

```bash
# Run all tests
mvn test

# Run tests and generate coverage report
mvn clean test
```

#### Option 2: Using Legacy Test Script

```bash
# Make the script executable (first time only)
chmod +x run-tests.sh

# Run all tests
./run-tests.sh
```

**Note**: Maven automatically runs tests during the build process and generates coverage reports.

### Running the Application

#### Option 1: Using the JAR File (Recommended)

```bash
# Run the executable JAR file
java -jar target/movie-management-system-1.0-SNAPSHOT.jar
```

#### Option 2: Using Maven

```bash
# Run via Maven exec plugin
mvn exec:java -Dexec.mainClass="movieManagement.src.Main"
```

#### Option 3: Using Legacy Script

```bash
# Make the script executable (first time only)
chmod +x run-system.sh

# Run the Movie Management System
./run-system.sh
```

The system includes a user-friendly console interface that allows users to interact with the system without writing any code. This is designed for demonstration purposes and makes the system accessible to users not familiar with programming.

**Console Interface Features:**
- Interactive menu-driven system
- Add movies to catalog
- Add rental and sale copies
- Rent movies
- Purchase movies
- Leave reviews and ratings
- View movie details and reviews
- List all movies in catalog

**Example Console Session:**
```
========================================
   Movie Management System
========================================

--- Main Menu ---
1. Add a new movie to catalog
2. Add rental copy of a movie
3. Add sale copy of a movie
4. Rent a movie
5. Buy a movie
6. Leave a review for a movie
7. View movie details and reviews
8. List all movies
9. Return a rented movie
0. Exit

Enter your choice: 1

--- Add New Movie ---
Enter IMDB ID (e.g., tt0111161): tt0111161
Enter movie title: The Shawshank Redemption
...
```

#### Option 2: Programmatic Usage

For developers who want to use the system programmatically:

```java
import movieManagement.movie.*;

// Create a movie
Movie movie = new Movie(
    "tt0111161",
    "The Shawshank Redemption",
    "Frank Darabont",
    "Castle Rock Entertainment",
    "1994-09-23",
    "Two imprisoned men bond over a number of years",
    15
);

// Add rental copies
RentalMovieDuplicate rentalCopy = new RentalMovieDuplicate(movie);
movie.addRentalCopy(rentalCopy);

// Add sale copies
SalableMovieDuplicate saleCopy = new SalableMovieDuplicate(movie);
movie.addSaleableCopy(saleCopy);

// Create a customer
Customer_stub customer = new Customer_stub();

// Borrow a movie
movie.Lend(customer);

// Purchase a movie
movie.Buy(customer);

// Add a review
Review review = new Review("Excellent cinematography!", 9);
movie.addReview(review);
movie.displayReviews();
```

### Expected Test Output

When running tests with Maven, you should see output similar to:

```
[INFO] Tests run: 612, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

The project includes **312 test methods** covering:
- Authentication and user management
- Movie operations (rental, purchase, reviews)
- Payment processing (Credit Card and FPS)
- Membership and state management
- Comprehensive integration tests

## 📚 Key Classes Reference

### `Main`
Main entry point providing console interface for the Movie Management System.

**Key Features:**
- User authentication (sign in, register)
- Admin operations (add movies, manage inventory)
- Customer operations (rent, buy, review, refund)
- Interactive menu-driven interface

### `Movie`
Main entity class managing the movie catalog and operations.

**Key Methods:**
- `addRentalCopy(RentalMovieDuplicate)` - Add a copy available for rental
- `addSaleableCopy(SalableMovieDuplicate)` - Add a copy available for purchase
- `Lend(Customer)` - Rent a movie to a customer
- `Buy(Customer)` - Sell a movie to a customer
- `Return(Customer)` - Return a rented movie
- `addReview(Review)` - Add a customer review
- `displayReviews()` - Show all reviews for the movie
- `hasAvailableRentalCopy()` - Check if rental copies are available

### `MovieDuplicate`
Abstract base class providing common functionality for all movie copies.

**Key Methods:**
- `getCopyId()` - Get unique identifier for the copy

### `RentalMovieDuplicate`
Represents a movie copy available for rental.

**Key Methods:**
- `rent(Customer)` - Mark copy as rented
- `returnMovie()` - Mark copy as available
- `isRented()` - Check rental status
- `getCustomer()` - Get the customer who rented this copy

### `SalableMovieDuplicate`
Represents a movie copy available for purchase.

**Key Methods:**
- `sold(Customer)` - Mark copy as sold
- `isSold()` - Check if copy has been sold

### `Customer`
Represents a customer with membership and rental/purchase history.

**Key Methods:**
- `getMembership()` - Get customer's membership
- `getRentedMovies()` - Get list of rented movies
- `getPurchasedMovies()` - Get list of purchased movies
- `getReviews()` - Get all reviews written by customer
- `addRentedMovie(Movie)` - Add movie to rented list
- `addPurchasedMovie(Movie)` - Add movie to purchased list

### `Membership`
Manages customer membership state and benefits.

**Key Methods:**
- `getType()` - Get membership type (Non-Member, Gold, Platinum)
- `getMaxRentMovies()` - Get maximum rental limit
- `getRentalDays()` - Get rental period in days
- `getState()` - Get current membership state

### `PaymentTransaction`
Represents a payment transaction for movie purchases.

**Key Methods:**
- `completePayment()` - Process the payment
- `completeRefund()` - Process refund for the transaction
- `isPaymentCompleted()` - Check if payment was completed
- `isRefundCompleted()` - Check if refund was completed

### `Review`
Customer review and rating for a movie.

**Key Methods:**
- `getComments()` - Get review text
- `getMovieRating()` - Get rating (0-10)
- `getCustomer()` - Get the customer who wrote the review

## 🧪 Testing

The project includes comprehensive unit tests with **312 test methods** covering all major functionality:

### Test Coverage

- **MainTest.java**: 312 test methods covering the main application
  - Authentication flows (sign in, register, sign out)
  - Admin operations (add movie, add copies)
  - Customer operations (rent, buy, review, return, refund)
  - View operations (membership, rented, purchased, reviews)
  - Error handling and edge cases
  - Input validation and boundary conditions

- **Movie Tests**: Coverage for movie operations
- **Payment Tests**: Credit Card and FPS payment processing
- **Authentication Tests**: User authentication and session management
- **User Tests**: Customer and membership functionality

### Test Coverage Report (JaCoCo)

**It is highly recommended to view the test coverage report** to understand which parts of the codebase are tested and identify areas that may need additional testing.

#### Viewing Coverage Report

The project uses **JaCoCo (Java Code Coverage)** to generate detailed test coverage reports. After running tests, a comprehensive HTML report is automatically generated.

**To generate and view the coverage report:**

```bash
# Run tests (coverage report is generated automatically)
mvn clean test

# Open the coverage report in your browser
# On macOS:
open target/site/jacoco/index.html

# On Linux:
xdg-open target/site/jacoco/index.html

# On Windows:
start target/site/jacoco/index.html
```

#### Understanding the Coverage Report

The JaCoCo report provides:

1. **Overall Coverage Summary**: Shows instruction coverage, branch coverage, line coverage, and method coverage percentages
2. **Package-Level Coverage**: Breakdown by package (authentication, movie, payment, users)
3. **Class-Level Coverage**: Detailed coverage for each class
4. **Line-by-Line Coverage**: Visual indicators showing which lines are covered (green) and which are not (red)
5. **Branch Coverage**: Shows which conditional branches are tested

#### Coverage Report Features

- **Color-Coded Coverage**: 
  - 🟢 Green bars = Covered code
  - 🔴 Red bars = Uncovered code
  
- **Interactive Navigation**: Click on packages/classes to drill down into detailed coverage

- **Coverage Metrics**:
  - **Instructions**: Percentage of bytecode instructions executed
  - **Branches**: Percentage of conditional branches tested
  - **Lines**: Percentage of source lines executed
  - **Methods**: Percentage of methods called

#### Current Coverage Status

The project maintains high test coverage:
- **Instruction Coverage**: ~77%+
- **Branch Coverage**: ~64%+
- **312 comprehensive test methods** covering all major code paths

#### For New Developers

**Recommended workflow:**

1. **Run tests and view coverage:**
   ```bash
   mvn clean test
   open target/site/jacoco/index.html
   ```

2. **Review uncovered code**: 
   - Look for red bars in the coverage report
   - Identify untested branches and edge cases
   - Add tests for uncovered code paths

3. **Check specific classes:**
   - Navigate to `movieManagement.src` → `Main.html` for detailed Main.java coverage
   - Review branch coverage to see which conditional paths need testing

4. **Improve coverage:**
   - Focus on methods with low coverage percentages
   - Add tests for error paths and edge cases
   - Ensure all branches are tested (if/else, switch cases, loops)

**Example**: To check Main.java coverage specifically:
```bash
# After running mvn test
open target/site/jacoco/movieManagement.src/Main.html
```

This will show you exactly which lines and branches in Main.java are covered by tests.

## 🖥️ Console Interface

The Movie Management System includes a **console-based user interface** designed for demonstration purposes. This interface allows users who are not familiar with programming to interact with the system easily through simple menu selections and text input.

### Features

- **No Programming Required**: Users can interact with the system through a simple menu-driven interface
- **Intuitive Commands**: Clear menu options guide users through all operations
- **Error Handling**: Input validation ensures users provide correct information
- **User-Friendly**: Designed for non-technical users while maintaining full system functionality

### Available Commands

1. **Add a new movie** - Create movie entries with all metadata (Admin only)
2. **Add rental copy** - Add copies available for rental (Admin only)
3. **Add sale copy** - Add copies available for purchase (Admin only)
4. **Rent a movie** - Borrow a movie from the catalog (Customer only)
5. **Buy a movie** - Purchase a movie permanently (Customer only)
6. **Leave a review** - Add reviews and ratings (0-10 scale) (Customer only)
7. **View movie details** - See movie information and all reviews
8. **List all movies** - Browse the entire catalog
9. **Return a rented movie** - Return a rented movie (Customer only)
10. **Process refund** - Request refund for purchased movies (Customer only)
11. **View my membership** - View membership details and benefits (Customer only)
12. **View my rented movies** - List all currently rented movies (Customer only)
13. **View my purchased movies** - List all purchased movies (Customer only)
14. **View my reviews** - View all reviews you've written (Customer only)
15. **Sign Out** - Sign out of the current session

### Usage Example

```bash
# Build the project (using Maven)
mvn clean package

# Run the console interface
java -jar target/movie-management-system-1.0-SNAPSHOT.jar

# Or use the legacy script
./run-system.sh
```

The interface will guide you through each operation step-by-step. Simply follow the on-screen prompts to:
- Enter movie information when adding new movies
- Select movies from numbered lists
- Provide review text and ratings
- View available copies and inventory status

**Note**: The console interface is provided for demonstration purposes to make the system accessible to users without programming knowledge. All core functionality is available through this interface without requiring any code modifications.

## 📝 Notes

- The system uses membership-based rental limits (Non-member: 1, Gold: 3, Platinum: 5)
- Rental period varies by membership type
- Movie ratings are on a scale of 0-10
- All copy IDs are generated using UUID for uniqueness
- The console interface provides full system access without programming knowledge
- Payment methods supported: Credit Card and FPS (Fast Payment System)
- Predefined movies are loaded automatically on startup

## 🔧 Troubleshooting

### Compilation Errors

If you encounter compilation errors:
1. Ensure JDK 18+ is installed: `java -version`
2. Verify all JUnit JARs are present in `lib/` directory
3. Check that package declarations match directory structure

### Test Failures

If tests fail:
1. Ensure compilation completed successfully: `mvn clean compile`
2. Check test output for specific failure messages
3. Verify all dependencies are resolved: `mvn dependency:resolve`
4. Review coverage report to identify problematic areas

### Coverage Report Not Generated

If the coverage report is not generated:
1. Ensure tests ran successfully: `mvn clean test`
2. Check that JaCoCo plugin is configured in `pom.xml`
3. Verify report location: `target/site/jacoco/index.html`
4. Try regenerating: `mvn clean test jacoco:report`

## 📄 License

This project is part of a software engineering demonstration project.

---

**Built with:** Java 18, Maven, JUnit 5, JaCoCo  
**Architecture:** Object-Oriented Design with Inheritance and Polymorphism  
**Test Coverage:** 312 test methods with comprehensive coverage reporting via JaCoCo

