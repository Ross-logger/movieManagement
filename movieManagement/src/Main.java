package movieManagement.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import movieManagement.src.movie.*;
import movieManagement.src.users.*;
import movieManagement.src.authentication.AuthService;
import movieManagement.src.exceptions.*;
import movieManagement.src.payment.*;
import movieManagement.data.PredefinedMoviesList;
import movieManagement.src.users.Membership;

public class Main {
    private static List<Movie> movies = new ArrayList<>();
    private static List<PaymentTransaction> transactions = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static AuthService authService = AuthService.getInstance();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Movie Management System");
        System.out.println("========================================");
        System.out.println();
        
        // Load predefined movies from data file
        movies.addAll(PredefinedMoviesList.loadMovies());
        if (!movies.isEmpty()) {
            System.out.println("Loaded " + movies.size() + " movies from catalog.");
        }

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = handleAuthentication();
            } else {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        addMovie();
                        break;
                    case 2:
                        addRentalCopy();
                        break;
                    case 3:
                        addSaleCopy();
                        break;
                    case 4:
                        rentMovie();
                        break;
                    case 5:
                        buyMovie();
                        break;
                    case 6:
                        addReview();
                        break;
                    case 7:
                        viewMovieDetails();
                        break;
                    case 8:
                        listAllMovies();
                        break;
                    case 9:
                        returnMovie();
                        break;
                    case 10:
                        processRefund();
                        break;
                    case 11:
                        viewMembership();
                        break;
                    case 12:
                        viewRentedMovies();
                        break;
                    case 13:
                        viewPurchasedMovies();
                        break;
                    case 14:
                        viewMyReviews();
                        break;
                    case 15:
                        signOut();
                        break;
                    case 0:
                        System.out.println("\nThank you for using Movie Management System!");
                        running = false;
                        break;
                    default:
                        System.out.println("\nInvalid choice. Please try again.\n");
                }
            }
        }
        scanner.close();
    }

    private static boolean handleAuthentication() {
        System.out.println("\n--- Authentication ---");
        System.out.println("1. Sign In");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.println();
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                return signIn();
            case 2:
                register();
                return true;
            case 0:
                return false;
            default:
                System.out.println("\nInvalid choice. Please try again.\n");
                return true;
        }
    }

    private static boolean signIn() {
        System.out.println("\n--- Sign In ---");
        String username = getStringInput("Enter username: ");
        String password = getStringInput("Enter password: ");
        
        try {
            User user = authService.signIn(username, password);
            currentUser = user;
            if (user instanceof Admin) {
                System.out.println("\n✓ Successfully signed in as admin: " + currentUser.getUsername());
            } else {
                System.out.println("\n✓ Successfully signed in as: " + currentUser.getUsername());
            }
            return true;
        } catch (UsernameNotFoundException e) {
            System.out.println("\n✗ " + e.getMessage());
            return true;
        } catch (WrongPasswordException e) {
            System.out.println("\n✗ " + e.getMessage());
            return true;
        }
    }

    private static void register() {
        System.out.println("\n--- Register ---");
        String username = getStringInput("Enter username: ");
        String password = getStringInput("Enter password: ");
        
        try {
            Customer customer = authService.registerCustomer(username, password);
            System.out.println("\n✓ Registration successful! Welcome, " + customer.getUsername());
            System.out.println("Please sign in to continue.");
        } catch (UsernameAlreadyTakenException e) {
            System.out.println("\n✗ " + e.getMessage());
        } catch (InvalidCredentialException e) {
            System.out.println("\n✗ " + e.getMessage());
        }
    }

    private static void viewMembership() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers have memberships.");
            return;
        }
        
        Customer customer = (Customer) currentUser;
        Membership membership = customer.getMembership();
        
        System.out.println("\n--- My Membership ---");
        System.out.println("Membership Type: " + membership.getType());
        System.out.println("Max Rental Movies: " + membership.getMaxRentMovies());
        System.out.println("Rental Days: " + membership.getRentalDays());
        System.out.println("Purchase Discount: " + (membership.getState().getPurchaseDiscount() * 100) + "%");
        System.out.println("\nCurrently Rented Movies: " + customer.getRentedMovies().size());
        System.out.println("Purchased Movies: " + customer.getPurchasedMovies().size());
    }

    private static void viewRentedMovies() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers can view rented movies.");
            return;
        }
        
        Customer customer = (Customer) currentUser;
        ArrayList<Movie> rentedMovies = customer.getRentedMovies();
        
        System.out.println("\n--- My Rented Movies ---");
        System.out.println("=".repeat(50));
        
        if (rentedMovies.isEmpty()) {
            System.out.println("You have no rented movies currently.");
        } else {
            System.out.println("Total rented movies: " + rentedMovies.size());
            System.out.println();
            for (int i = 0; i < rentedMovies.size(); i++) {
                Movie movie = rentedMovies.get(i);
                System.out.println((i + 1) + ". " + movie.getDisplayText());
            }
        }
        System.out.println("=".repeat(50));
    }

    private static void viewPurchasedMovies() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers can view purchased movies.");
            return;
        }
        
        Customer customer = (Customer) currentUser;
        ArrayList<Movie> purchasedMovies = customer.getPurchasedMovies();
        
        System.out.println("\n--- My Purchased Movies ---");
        System.out.println("=".repeat(50));
        
        if (purchasedMovies.isEmpty()) {
            System.out.println("You have no purchased movies currently.");
        } else {
            System.out.println("Total purchased movies: " + purchasedMovies.size());
            System.out.println();
            for (int i = 0; i < purchasedMovies.size(); i++) {
                Movie movie = purchasedMovies.get(i);
                System.out.println((i + 1) + ". " + movie.getDisplayText());
            }
        }
        System.out.println("=".repeat(50));
    }

    private static void viewMyReviews() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers can view their reviews.");
            return;
        }
        
        Customer customer = (Customer) currentUser;
        Set<Review> reviews = customer.getReviews();
        
        System.out.println("\n--- My Reviews ---");
        System.out.println("=".repeat(50));
        
        if (reviews.isEmpty()) {
            System.out.println("You have not written any reviews yet.");
        } else {
            System.out.println("Total reviews: " + reviews.size());
            System.out.println();
            
            // Find which movie each review belongs to by searching through all movies
            int reviewNum = 1;
            for (Review review : reviews) {
                // Find the movie this review belongs to
                String movieTitle = "Unknown Movie";
                for (Movie movie : movies) {
                    // Check if this movie has this review by comparing comment and rating
                    List<Review> movieReviews = movie.getAllReviews();
                    if (movieReviews != null) {
                        for (Review movieReview : movieReviews) {
                            if (movieReview.getComments().equals(review.getComments()) && 
                                movieReview.getMovieRating() == review.getMovieRating()) {
                                movieTitle = movie.getDisplayText();
                                break;
                            }
                        }
                    }
                    if (!movieTitle.equals("Unknown Movie")) {
                        break;
                    }
                }
                
                System.out.println(reviewNum + ". Movie: " + movieTitle);
                System.out.println("   Rating: " + review.getMovieRating() + "/10");
                System.out.println("   Comment: " + review.getComments());
                System.out.println();
                reviewNum++;
            }
        }
        System.out.println("=".repeat(50));
    }

    private static void signOut() {
        try {
            authService.signOut(currentUser);
            System.out.println("\n✓ Successfully signed out");
            currentUser = null;
        } catch (UserNotFoundException e) {
            System.out.println("\n✗ " + e.getMessage());
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("Logged in as: " + currentUser.getUsername());
        System.out.println("1. Add a new movie to catalog");
        System.out.println("2. Add rental copy of a movie");
        System.out.println("3. Add sale copy of a movie");
        System.out.println("4. Rent a movie");
        System.out.println("5. Buy a movie");
        System.out.println("6. Leave a review for a movie");
        System.out.println("7. View movie details and reviews");
        System.out.println("8. List all movies");
        System.out.println("9. Return a rented movie");
        System.out.println("10. Process refund");
        System.out.println("11. View my membership");
        System.out.println("12. View my rented movies");
        System.out.println("13. View my purchased movies");
        System.out.println("14. View my reviews");
        System.out.println("15. Sign Out");
        System.out.println("0. Exit");
        System.out.println();
    }

    private static void addMovie() {
        if (!(currentUser instanceof Admin)) {
            System.out.println("\n✗ Only administrators can add movies.");
            return;
        }

        System.out.println("\n--- Add New Movie ---");
        String imdbId = getStringInput("Enter IMDB ID (e.g., tt0111161): ");
        String title = getStringInput("Enter movie title: ");
        String director = getStringInput("Enter director name: ");
        String studio = getStringInput("Enter studio name: ");
        String releaseDate = getDateInput("Enter release date (YYYY-MM-DD): ");
        String description = getStringInput("Enter movie description: ");
        int price = getIntInput("Enter price: $");

        Movie movie = new Movie(imdbId, title, director, studio, releaseDate, description, price);
        movies.add(movie);
        System.out.println("\n✓ Movie added successfully!");
        System.out.println("Movie: " + movie.getDisplayText());
    }

    private static void addRentalCopy() {
        if (!(currentUser instanceof Admin)) {
            System.out.println("\n✗ Only administrators can add rental copies.");
            return;
        }

        if (movies.isEmpty()) {
            System.out.println("\n✗ No movies in catalog. Please add a movie first.");
            return;
        }

        System.out.println("\n--- Add Rental Copy ---");
        Movie movie = selectMovie();
        if (movie != null) {
            RentalMovieDuplicate copy = new RentalMovieDuplicate(movie);
            movie.addRentalCopy(copy);
            System.out.println("\n✓ Rental copy added successfully!");
            System.out.println("Copy ID: " + copy.getCopyId());
        }
    }

    private static void addSaleCopy() {
        if (!(currentUser instanceof Admin)) {
            System.out.println("\n✗ Only administrators can add sale copies.");
            return;
        }

        if (movies.isEmpty()) {
            System.out.println("\n✗ No movies in catalog. Please add a movie first.");
            return;
        }

        System.out.println("\n--- Add Sale Copy ---");
        Movie movie = selectMovie();
        if (movie != null) {
            SalableMovieDuplicate copy = new SalableMovieDuplicate(movie);
            movie.addSaleableCopy(copy);
            System.out.println("\n✓ Sale copy added successfully!");
            System.out.println("Copy ID: " + copy.getCopyId());
        }
    }

    private static void rentMovie() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers can rent movies.");
            return;
        }

        if (movies.isEmpty()) {
            System.out.println("\n✗ No movies in catalog.");
            return;
        }

        Customer customer = (Customer) currentUser;
        Membership membership = customer.getMembership();
        int maxRentals = membership.getMaxRentMovies();
        int currentRentals = customer.getRentedMovies().size();
        
        // Check if customer has reached max rental limit
        if (currentRentals >= maxRentals) {
            System.out.println("\n✗ Rental limit reached! You can rent a maximum of " + maxRentals + " movie(s) with your " + membership.getType() + " membership.");
            System.out.println("Currently rented: " + currentRentals + " movie(s)");
            System.out.println("Please return a movie before renting another one.");
            return;
        }

        System.out.println("\n--- Rent a Movie ---");
        Movie movie = selectMovie();
        if (movie != null) {
            System.out.println("\nAttempting to rent: " + movie.getDisplayText());
            // Check if movie has available rental copies
            if (!movie.hasAvailableRentalCopy()) {
                System.out.println("\n✗ No rental copies available for this movie.");
                return;
            }
            movie.Lend(customer);
            // Add movie to customer's rented movies list
            customer.addRentedMovie(movie);
            System.out.println("Available rental copies: " + movie.showAvailableRentalCopies());
        }
    }

    private static void buyMovie() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers can buy movies.");
            return;
        }

        if (movies.isEmpty()) {
            System.out.println("\n✗ No movies in catalog.");
            return;
        }

        System.out.println("\n--- Buy a Movie ---");
        Movie movie = selectMovie();
        if (movie != null) {
            System.out.println("\nMovie: " + movie.getDisplayText());
            System.out.println("Price: $" + movie.getMoviePrice());
            
            System.out.println("\nSelect payment method:");
            System.out.println("1. Credit Card");
            System.out.println("2. FPS (Fast Payment System)");
            int paymentChoice = getIntInput("Enter your choice: ");
            
            String paymentType;
            switch (paymentChoice) {
                case 1:
                    paymentType = "CreditCard";
                    break;
                case 2:
                    paymentType = "FPS";
                    break;
                default:
                    System.out.println("\n✗ Invalid payment method selection.");
                    return;
            }
            
            try {
                String transactionId = PaymentTransaction.generateTransactionId();
                PaymentTransaction transaction = PaymentServiceFactory.createPaymentTransaction(
                    transactionId, 
                    movie.getMoviePrice(), 
                    paymentType
                );
                
                transaction.completePayment();
                
                if (transaction.isPaymentCompleted()) {
                    Customer customer = (Customer) currentUser;
                    movie.Buy(customer);
                    customer.addPurchasedMovie(movie);
                    transactions.add(transaction);
                    System.out.println("Available sale copies: " + movie.showAvailableSellableCopies());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("\n✗ " + e.getMessage());
            }
        }
    }
    
    private static void processRefund() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers can process refunds.");
            return;
        }
        
        if (transactions.isEmpty()) {
            System.out.println("\n✗ No transactions found.");
            return;
        }
        
        System.out.println("\n--- Process Refund ---");
        System.out.println("\nYour transactions:");
        for (int i = 0; i < transactions.size(); i++) {
            PaymentTransaction txn = transactions.get(i);
            System.out.println((i + 1) + ". Transaction ID: " + txn.getTransactionId() + 
                             " | Amount: $" + txn.getAmount() + 
                             " | Status: " + (txn.isRefundCompleted() ? "Refunded" : 
                                             (txn.isPaymentCompleted() ? "Paid" : "Pending")));
        }
        
        int choice = getIntInput("\nEnter transaction number to refund (0 to cancel): ");
        if (choice == 0) {
            return;
        }
        
        if (choice >= 1 && choice <= transactions.size()) {
            PaymentTransaction transaction = transactions.get(choice - 1);
            if (transaction.isRefundCompleted()) {
                System.out.println("\n✗ This transaction has already been refunded.");
            } else if (!transaction.isPaymentCompleted()) {
                System.out.println("\n✗ Payment for this transaction was not completed.");
            } else {
                transaction.completeRefund();
            }
        } else {
            System.out.println("\n✗ Invalid transaction selection.");
        }
    }

    private static void addReview() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers can leave reviews.");
            return;
        }

        if (movies.isEmpty()) {
            System.out.println("\n✗ No movies in catalog.");
            return;
        }

        System.out.println("\n--- Leave a Review ---");
        Movie movie = selectMovie();
        if (movie != null) {
            System.out.println("\nReviewing: " + movie.getDisplayText());
            String comment = getStringInput("Enter your review comment: ");
            int rating = getIntInput("Enter rating (0-10): ");

            if (rating < 0 || rating > 10) {
                System.out.println("Invalid rating. Please enter a number between 0 and 10.");
                return;
            }

            Customer customer = (Customer) currentUser;
            Review review = new Review(comment, rating, customer);
            boolean added = movie.addReview(review);
            if (added) {
                // Also add review to customer's review list
                customer.addReview(review);
                System.out.println("\n✓ Review added successfully!");
            } else {
                System.out.println("\n⚠ You already have a review for this movie. Use the edit option to update it.");
            }
        }
    }

    private static void viewMovieDetails() {
        if (movies.isEmpty()) {
            System.out.println("\n✗ No movies in catalog.");
            return;
        }

        System.out.println("\n--- Movie Details ---");
        Movie movie = selectMovie();
        if (movie != null) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println(movie.getDisplayText());
            System.out.println("=".repeat(50));
            System.out.println("\nAvailable rental copies: " + movie.showAvailableRentalCopies());
            System.out.println("Available sale copies: " + movie.showAvailableSellableCopies());
            System.out.println("\n--- Reviews ---");
            movie.displayReviews();
        }
    }

    private static void listAllMovies() {
        if (movies.isEmpty()) {
            System.out.println("\n✗ No movies in catalog.");
            return;
        }

        System.out.println("\n--- All Movies in Catalog ---");
        System.out.println("=".repeat(50));
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getDisplayText());
        }
        System.out.println("=".repeat(50));
    }

    private static void returnMovie() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("\n✗ Only customers can return movies.");
            return;
        }

        Customer customer = (Customer) currentUser;
        ArrayList<Movie> rentedMovies = customer.getRentedMovies();
        
        if (rentedMovies.isEmpty()) {
            System.out.println("\n✗ You have no rented movies to return.");
            return;
        }

        System.out.println("\n--- Return a Rented Movie ---");
        System.out.println("\nYour currently rented movies:");
        for (int i = 0; i < rentedMovies.size(); i++) {
            System.out.println((i + 1) + ". " + rentedMovies.get(i).getDisplayText());
        }
        System.out.println();
        
        int choice = getIntInput("Enter the number of the movie you want to return (0 to cancel): ");
        
        if (choice == 0) {
            return;
        }
        
        if (choice < 1 || choice > rentedMovies.size()) {
            System.out.println("\n✗ Invalid choice. Please try again.");
            return;
        }
        
        Movie movieToReturn = rentedMovies.get(choice - 1);
        System.out.println("\nReturning: " + movieToReturn.getDisplayText());
        
        // Return the movie - only remove from list if return was successful
        boolean returnSuccess = movieToReturn.Return(customer);
        
        if (returnSuccess) {
            // Remove from customer's rented movies list using index (more reliable)
            customer.getRentedMovies().remove(choice - 1);
            System.out.println("✓ Movie removed from your rented list.");
            System.out.println("Available rental copies: " + movieToReturn.showAvailableRentalCopies());
        } else {
            System.out.println("\n✗ Failed to return movie. Please try again.");
        }
    }

    private static Movie selectMovie() {
        if (movies.isEmpty()) {
            return null;
        }

        System.out.println("\nSelect a movie:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getDisplayText());
        }

        int choice = getIntInput("\nEnter movie number: ");
        if (choice >= 1 && choice <= movies.size()) {
            return movies.get(choice - 1);
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static String getDateInput(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("✗ Invalid date format. Please use YYYY-MM-DD format (e.g., 1994-09-23)");
                continue;
            }
            
            try {
                LocalDate.parse(input, formatter);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("✗ Invalid date. Please enter a valid date in YYYY-MM-DD format (e.g., 1994-09-23)");
                System.out.println("   Make sure the month is between 01-12 and day is valid for that month.");
            }
        }
    }
}

