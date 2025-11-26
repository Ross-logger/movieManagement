# Movie Management System

A comprehensive Java-based movie management system demonstrating object-oriented design principles, inheritance patterns, and software engineering best practices. This system manages movie inventory, rentals, sales, and customer reviews with a clean, maintainable architecture.

## 📋 Overview

### Project Description

The Movie Management System is a library-style application that handles:
- **Movie Catalog Management**: Store and manage movie metadata (title, director, studio, IMDB ID, release date, price)
- **Rental Operations**: Track movie rentals with borrowing history and due dates
- **Sales Operations**: Manage movie purchases and track sold inventory
- **Review System**: Allow customers to rate and review movies
- **Waitlist Management**: Handle customer waitlists for unavailable movies

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
- **Observer Pattern**: Foundation for waitlist notifications (stub implementation)

### Project Structure

```
src/
├── movieManagement/
│   ├── movie/                    # Main package
│   │   ├── Movie.java            # Core entity - manages movie catalog
│   │   ├── MovieDuplicate.java   # Abstract base for movie copies
│   │   ├── RentalMovieDuplicate.java  # Rental copy implementation
│   │   ├── SalableMovieDuplicate.java # Sale copy implementation
│   │   ├── RentalRecord.java     # Rental transaction history
│   │   ├── Review.java           # Customer reviews and ratings
│   │   ├── Observer.java         # Observer pattern base
│   │   ├── Customer_stub.java    # Customer representation
│   │   ├── Observer_stub.java    # Observer stub
│   │   └── Review_stub.java      # Review stub
│   ├── MovieManagementSystem.java # Console interface (main entry point)
│   └── testMovie/                 # Test package
│       ├── TestMovie.java        # Movie class tests
│       ├── TestRentalRecord.java # Rental record tests
│       └── TestReview.java       # Review class tests
├── lib/                          # JUnit dependencies
├── compile.sh                    # Compilation script
├── run-tests.sh                  # Test execution script
└── run-system.sh                 # Run console interface script
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
   - Handle waitlists when no copies are available

3. **Sales System**
   - Purchase movies permanently
   - Track sold inventory
   - Manage purchase waitlists

4. **Review System**
   - Add customer reviews with ratings (0-10 scale)
   - Display all reviews for a movie
   - Validate rating ranges

## 🚀 Quickstart

### Prerequisites

- **Java Development Kit (JDK)**: Version 18 or higher
- **JUnit 5**: Included in `lib/` directory
- **Bash shell**: For running compilation and test scripts (Unix/Linux/macOS)

### Compilation

#### Option 1: Using the Compilation Script (Recommended)

```bash
# Make the script executable (first time only)
chmod +x compile.sh

# Compile the project
./compile.sh
```

This script will:
- Compile all source files in `movieManagement/movie/`
- Compile all test files in `movieManagement/testMovie/`
- Place compiled classes in `build/classes/` and `build/test-classes/`

#### Option 2: Manual Compilation

```bash
# Set up classpath
CLASSPATH="lib/*:movieManagement/movie:movieManagement/testMovie"

# Compile main source files
javac -cp "$CLASSPATH" -d build/classes movieManagement/movie/*.java

# Compile test files
javac -cp "$CLASSPATH:build/classes" -d build/test-classes movieManagement/testMovie/*.java
```

### Running Tests

#### Option 1: Using the Test Script (Recommended)

```bash
# Make the script executable (first time only)
chmod +x run-tests.sh

# Run all tests
./run-tests.sh
```

#### Option 2: Manual Test Execution

```bash
# Set up classpath for tests
CLASSPATH="lib/junit-platform-console-standalone-1.10.0.jar:build/classes:build/test-classes"

# Run tests using JUnit Platform Console Launcher
java -cp "$CLASSPATH" org.junit.platform.console.ConsoleLauncher \
    --class-path build/test-classes \
    --scan-class-path
```

### Running the Application

#### Option 1: Console Interface (Recommended for Non-Programmers)

The system includes a user-friendly console interface that allows users to interact with the system without writing any code. This is designed for demonstration purposes and makes the system accessible to users not familiar with programming.

```bash
# Make the script executable (first time only)
chmod +x run-system.sh

# Run the Movie Management System
./run-system.sh
```

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

When running tests, you should see output similar to:

```
Test run finished after XXX ms
[         6 containers found      ]
[         0 containers skipped     ]
[         6 containers started     ]
[         0 containers aborted     ]
[         6 containers successful  ]
[         0 containers failed      ]
[        10 tests found            ]
[         0 tests skipped          ]
[        10 tests started          ]
[         0 tests aborted           ]
[        10 tests successful       ]
[         0 tests failed           ]
```

## 📚 Key Classes Reference

### `Movie`
Main entity class managing the movie catalog and operations.

**Key Methods:**
- `addRentalCopy(RentalMovieDuplicate)` - Add a copy available for rental
- `addSaleableCopy(SalableMovieDuplicate)` - Add a copy available for purchase
- `Lend(Customer_stub)` - Rent a movie to a customer
- `Buy(Customer_stub)` - Sell a movie to a customer
- `addReview(Review)` - Add a customer review
- `displayReviews()` - Show all reviews for the movie

### `MovieDuplicate`
Abstract base class providing common functionality for all movie copies.

**Key Methods:**
- `getCopyId()` - Get unique identifier for the copy

### `RentalMovieDuplicate`
Represents a movie copy available for rental.

**Key Methods:**
- `rent(Customer_stub)` - Mark copy as rented
- `returnMovie()` - Mark copy as available
- `isRented()` - Check rental status
- `addRentalRecord(RentalRecord)` - Record rental history

### `SalableMovieDuplicate`
Represents a movie copy available for purchase.

**Key Methods:**
- `sold(Customer_stub)` - Mark copy as sold
- `isSold()` - Check if copy has been sold

### `RentalRecord`
Immutable record of a rental transaction.

**Key Methods:**
- `getRentalDate()` - Get when movie was borrowed
- `getReturnDate()` - Get when movie should be returned
- `extendRentalPeriod(int)` - Extend the rental period

### `Review`
Customer review and rating for a movie.

**Key Methods:**
- `getFeedback()` - Get review text
- `getMediaScore()` - Get rating (0-10)

## 🧪 Testing

The project includes comprehensive unit tests:

- **TestMovie.java**: 6 test cases covering movie operations
  - Adding rental copies
  - Adding sale copies
  - Successful lending
  - Successful purchasing
  - Handling unavailable copies (waitlist scenarios)

- **TestReview.java**: 4 test cases covering review functionality
  - Review creation
  - Rating validation
  - String formatting

- **TestRentalRecord.java**: Placeholder for rental record tests

## 🖥️ Console Interface

The Movie Management System includes a **console-based user interface** designed for demonstration purposes. This interface allows users who are not familiar with programming to interact with the system easily through simple menu selections and text input.

### Features

- **No Programming Required**: Users can interact with the system through a simple menu-driven interface
- **Intuitive Commands**: Clear menu options guide users through all operations
- **Error Handling**: Input validation ensures users provide correct information
- **User-Friendly**: Designed for non-technical users while maintaining full system functionality

### Available Commands

1. **Add a new movie** - Create movie entries with all metadata
2. **Add rental copy** - Add copies available for rental
3. **Add sale copy** - Add copies available for purchase
4. **Rent a movie** - Borrow a movie from the catalog
5. **Buy a movie** - Purchase a movie permanently
6. **Leave a review** - Add reviews and ratings (0-10 scale)
7. **View movie details** - See movie information and all reviews
8. **List all movies** - Browse the entire catalog
9. **Return a rented movie** - Return functionality (for demonstration)

### Usage Example

```bash
# Compile the project
./compile.sh

# Run the console interface
./run-system.sh
```

The interface will guide you through each operation step-by-step. Simply follow the on-screen prompts to:
- Enter movie information when adding new movies
- Select movies from numbered lists
- Provide review text and ratings
- View available copies and inventory status

**Note**: The console interface is provided for demonstration purposes to make the system accessible to users without programming knowledge. All core functionality is available through this interface without requiring any code modifications.

## 📝 Notes

- The system uses a 7-day default rental period for movies
- Movie ratings are on a scale of 0-10
- Waitlists are automatically managed when copies are unavailable
- All copy IDs are generated using UUID for uniqueness
- The console interface provides full system access without programming knowledge

## 🔧 Troubleshooting

### Compilation Errors

If you encounter compilation errors:
1. Ensure JDK 18+ is installed: `java -version`
2. Verify all JUnit JARs are present in `lib/` directory
3. Check that package declarations match directory structure

### Test Failures

If tests fail:
1. Ensure compilation completed successfully
2. Check that test output matches expected strings exactly (including newlines)
3. Verify classpath includes both source and test classes

## 📄 License

This project is part of a software engineering demonstration project.

---

**Built with:** Java 18, JUnit 5  
**Architecture:** Object-Oriented Design with Inheritance and Polymorphism

