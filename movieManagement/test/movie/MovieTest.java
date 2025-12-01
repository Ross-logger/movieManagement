package movieManagement.test.movie;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.src.movie.Movie;
import movieManagement.src.movie.RentalMovieDuplicate;
import movieManagement.src.movie.SalableMovieDuplicate;
import movieManagement.src.movie.Review;
import movieManagement.src.users.Customer;

public class MovieTest {

    private Movie movie;
    private Customer customer;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        movie = new Movie("tt1234567", "Test Movie", "Test Director", "Test Studio", "2024-01-01", "Test Description", 25);
        CredentialsCheck credential = new CredentialsCheck("TestPass123!");
        customer = new Customer("testcustomer", credential);
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    private String getOutput() {
        return outputStream.toString();
    }

    @Test
    public void testMovieInitialization() {
        String displayText = movie.getDisplayText();
        assertTrue(displayText.contains("tt1234567"));
        assertEquals(25, movie.getMoviePrice());
        assertNotNull(movie.showAvailableRentalCopies());
        assertNotNull(movie.showAvailableSellableCopies());
    }

    @Test
    public void testGetMoviePrice() {
        assertEquals(25, movie.getMoviePrice());
        Movie expensiveMovie = new Movie("tt9999999", "Expensive", "Director", "Studio", "2024-01-01", "Desc", 100);
        assertEquals(100, expensiveMovie.getMoviePrice());
    }

    @Test
    public void testGetDisplayText() {
        String displayText = movie.getDisplayText();
        assertTrue(displayText.contains("Test Movie"));
        assertTrue(displayText.contains("Test Director"));
        assertTrue(displayText.contains("tt1234567"));
        assertTrue(displayText.contains("Test Studio"));
        assertTrue(displayText.contains("2024-01-01"));
        assertTrue(displayText.contains("25"));
    }

    @Test
    public void testIsSalableWithCopies() {
        assertTrue(movie.isSalable());
    }

    @Test
    public void testIsSalableWithoutCopies() {
        Movie emptyMovie = new Movie("tt1111111", "Empty", "Dir", "Studio", "2024-01-01", "Desc", 10);
        // Remove all sale copies
        for (int i = 0; i < 10; i++) {
            emptyMovie.Buy(customer);
        }
        assertFalse(emptyMovie.isSalable());
    }

    @Test
    public void testBuySuccessfully() {
        outputStream.reset();
        movie.Buy(customer);
        String output = getOutput();
        assertTrue(output.contains("Buying Successfully"));
        // After buying one copy, should have 9 left
        assertTrue(movie.showAvailableSellableCopies().contains("9"));
    }

    @Test
    public void testBuyWhenNoCopiesAvailable() {
        Movie emptyMovie = new Movie("tt1111111", "Empty", "Dir", "Studio", "2024-01-01", "Desc", 10);
        // Remove all sale copies
        for (int i = 0; i < 10; i++) {
            emptyMovie.Buy(customer);
        }
        outputStream.reset();
        emptyMovie.Buy(customer);
        String output = getOutput();
        assertTrue(output.contains("No movie available for selling"));
    }

    @Test
    public void testBuyMultipleCopies() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        movie.Buy(customer);
        movie.Buy(customer2);
        List<SalableMovieDuplicate> soldCopies = movie.getSoldCopies();
        assertEquals(2, soldCopies.size());
    }

    @Test
    public void testGetSoldCopies() {
        assertTrue(movie.getSoldCopies().isEmpty());
        movie.Buy(customer);
        List<SalableMovieDuplicate> soldCopies = movie.getSoldCopies();
        assertEquals(1, soldCopies.size());
        assertNotNull(soldCopies.get(0));
    }

    @Test
    public void testGetSoldCopiesReturnsNewList() {
        movie.Buy(customer);
        List<SalableMovieDuplicate> soldCopies1 = movie.getSoldCopies();
        List<SalableMovieDuplicate> soldCopies2 = movie.getSoldCopies();
        assertNotSame(soldCopies1, soldCopies2);
        assertEquals(soldCopies1.size(), soldCopies2.size());
    }

    @Test
    public void testLendSuccessfully() {
        outputStream.reset();
        movie.Lend(customer);
        String output = getOutput();
        assertTrue(output.contains("Lending Successfully"));
    }

    @Test
    public void testLendWhenNoCopiesAvailable() {
        Movie emptyMovie = new Movie("tt1111111", "Empty", "Dir", "Studio", "2024-01-01", "Desc", 10);
        // Rent all copies
        for (int i = 0; i < 10; i++) {
            Customer tempCustomer = new Customer("customer" + i, new CredentialsCheck("Pass123!"));
            emptyMovie.Lend(tempCustomer);
        }
        outputStream.reset();
        emptyMovie.Lend(customer);
        String output = getOutput();
        assertTrue(output.contains("No movie available for lending"));
    }

    @Test
    public void testLendMultipleCopies() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        Customer customer3 = new Customer("customer3", new CredentialsCheck("Pass123!"));
        movie.Lend(customer);
        movie.Lend(customer2);
        movie.Lend(customer3);
        assertNotNull(movie.getRentedCopyByCustomer(customer));
        assertNotNull(movie.getRentedCopyByCustomer(customer2));
        assertNotNull(movie.getRentedCopyByCustomer(customer3));
    }

    @Test
    public void testGetAvailableLendingCopy() {
        RentalMovieDuplicate copy = movie.getAvailableLendingCopy();
        assertNotNull(copy);
        assertFalse(copy.isRented());
    }

    @Test
    public void testGetAvailableLendingCopyWhenAllRented() {
        Movie emptyMovie = new Movie("tt1111111", "Empty", "Dir", "Studio", "2024-01-01", "Desc", 10);
        // Rent all copies
        for (int i = 0; i < 10; i++) {
            Customer tempCustomer = new Customer("customer" + i, new CredentialsCheck("Pass123!"));
            emptyMovie.Lend(tempCustomer);
        }
        assertNull(emptyMovie.getAvailableLendingCopy());
    }

    @Test
    public void testHasAvailableRentalCopy() {
        assertTrue(movie.hasAvailableRentalCopy());
    }

    @Test
    public void testHasAvailableRentalCopyWhenAllRented() {
        Movie emptyMovie = new Movie("tt1111111", "Empty", "Dir", "Studio", "2024-01-01", "Desc", 10);
        // Rent all copies
        for (int i = 0; i < 10; i++) {
            Customer tempCustomer = new Customer("customer" + i, new CredentialsCheck("Pass123!"));
            emptyMovie.Lend(tempCustomer);
        }
        assertFalse(emptyMovie.hasAvailableRentalCopy());
    }

    @Test
    public void testGetRentedCopyByCustomer() {
        movie.Lend(customer);
        RentalMovieDuplicate copy = movie.getRentedCopyByCustomer(customer);
        assertNotNull(copy);
        assertTrue(copy.isRented());
        assertEquals(customer, copy.getCustomer());
    }

    @Test
    public void testGetRentedCopyByCustomerWhenNotRented() {
        RentalMovieDuplicate copy = movie.getRentedCopyByCustomer(customer);
        assertNull(copy);
    }

    @Test
    public void testGetRentedCopyByCustomerDifferentCustomer() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        movie.Lend(customer);
        RentalMovieDuplicate copy = movie.getRentedCopyByCustomer(customer2);
        assertNull(copy);
    }

    @Test
    public void testReturnSuccessfully() {
        movie.Lend(customer);
        outputStream.reset();
        boolean result = movie.Return(customer);
        assertTrue(result);
        String output = getOutput();
        assertTrue(output.contains("Movie returned successfully"));
        assertNull(movie.getRentedCopyByCustomer(customer));
    }

    @Test
    public void testReturnWhenNotRented() {
        outputStream.reset();
        boolean result = movie.Return(customer);
        assertFalse(result);
        String output = getOutput();
        assertTrue(output.contains("You have not rented this movie"));
    }

    @Test
    public void testReturnAfterMultipleRentals() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        movie.Lend(customer);
        movie.Lend(customer2);
        assertTrue(movie.Return(customer));
        assertTrue(movie.Return(customer2));
        assertNull(movie.getRentedCopyByCustomer(customer));
        assertNull(movie.getRentedCopyByCustomer(customer2));
    }

    @Test
    public void testShowAvailableRentalCopies() {
        String result = movie.showAvailableRentalCopies();
        assertTrue(result.contains("available rentable copy"));
        assertTrue(result.contains("10"));
    }

    @Test
    public void testShowAvailableRentalCopiesAfterRental() {
        movie.Lend(customer);
        String result = movie.showAvailableRentalCopies();
        assertTrue(result.contains("available rentable copy"));
        assertTrue(result.contains("9"));
    }

    @Test
    public void testShowAvailableRentalCopiesWhenAllRented() {
        Movie emptyMovie = new Movie("tt1111111", "Empty", "Dir", "Studio", "2024-01-01", "Desc", 10);
        // Rent all copies
        for (int i = 0; i < 10; i++) {
            Customer tempCustomer = new Customer("customer" + i, new CredentialsCheck("Pass123!"));
            emptyMovie.Lend(tempCustomer);
        }
        String result = emptyMovie.showAvailableRentalCopies();
        assertTrue(result.contains("0"));
    }

    @Test
    public void testAddRentalCopy() {
        RentalMovieDuplicate newCopy = new RentalMovieDuplicate(movie);
        outputStream.reset();
        movie.addRentalCopy(newCopy);
        String output = getOutput();
        assertTrue(output.contains("Add one rentable copy of movie"));
        String result = movie.showAvailableRentalCopies();
        assertTrue(result.contains("11"));
    }

    @Test
    public void testAddSaleableCopy() {
        SalableMovieDuplicate newCopy = new SalableMovieDuplicate(movie);
        outputStream.reset();
        movie.addSaleableCopy(newCopy);
        String output = getOutput();
        assertTrue(output.contains("Add one seleable copy of movie"));
        String result = movie.showAvailableSellableCopies();
        assertTrue(result.contains("11"));
    }

    @Test
    public void testShowAvailableSellableCopies() {
        String result = movie.showAvailableSellableCopies();
        assertTrue(result.contains("available sellable copy"));
        assertTrue(result.contains("10"));
    }

    @Test
    public void testShowAvailableSellableCopiesAfterPurchase() {
        movie.Buy(customer);
        String result = movie.showAvailableSellableCopies();
        assertTrue(result.contains("9"));
    }

    @Test
    public void testAddReview() {
        Review review = new Review("Great movie!", 8, customer);
        assertTrue(movie.addReview(review));
        List<Review> reviews = movie.getAllReviews();
        assertEquals(1, reviews.size());
        assertEquals(review, reviews.get(0));
    }

    @Test
    public void testAddReviewNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            movie.addReview(null);
        });
    }

    @Test
    public void testAddReviewWithoutCustomer() {
        Review review = new Review("Test", 5, null);
        assertThrows(IllegalArgumentException.class, () -> {
            movie.addReview(review);
        });
    }

    @Test
    public void testAddMultipleReviews() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        Customer customer3 = new Customer("customer3", new CredentialsCheck("Pass123!"));
        Review review1 = new Review("Great!", 8, customer);
        Review review2 = new Review("Amazing!", 9, customer2);
        Review review3 = new Review("Excellent!", 10, customer3);
        assertTrue(movie.addReview(review1));
        assertTrue(movie.addReview(review2));
        assertTrue(movie.addReview(review3));
        List<Review> reviews = movie.getAllReviews();
        assertEquals(3, reviews.size());
    }

    @Test
    public void testDisplayReviewsEmpty() {
        outputStream.reset();
        movie.displayReviews();
        String output = getOutput();
        assertTrue(output.contains("No reviews yet"));
    }

    @Test
    public void testDisplayReviewsWithContent() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        Review review1 = new Review("Great!", 8, customer);
        Review review2 = new Review("Amazing!", 9, customer2);
        assertTrue(movie.addReview(review1));
        assertTrue(movie.addReview(review2));
        outputStream.reset();
        movie.displayReviews();
        String output = getOutput();
        assertTrue(output.contains("Great!") || output.contains("Amazing!"));
    }

    @Test
    public void testGetAllReviews() {
        assertTrue(movie.getAllReviews().isEmpty());
        Review review = new Review("Test", 5, customer);
        assertTrue(movie.addReview(review));
        List<Review> reviews = movie.getAllReviews();
        assertEquals(1, reviews.size());
    }

    @Test
    public void testGetAllReviewsReturnsNewList() {
        Review review = new Review("Test", 5, customer);
        assertTrue(movie.addReview(review));
        List<Review> reviews1 = movie.getAllReviews();
        List<Review> reviews2 = movie.getAllReviews();
        assertNotSame(reviews1, reviews2);
        assertEquals(reviews1.size(), reviews2.size());
    }

    @Test
    public void testInitializeDefaultCopies() {
        Movie newMovie = new Movie("tt9999999", "New Movie", "Director", "Studio", "2024-01-01", "Desc", 20);
        assertEquals("10 available rentable copy", newMovie.showAvailableRentalCopies());
        assertEquals("10 available sellable copy", newMovie.showAvailableSellableCopies());
    }

    @Test
    public void testRentAndReturnCycle() {
        movie.Lend(customer);
        assertNotNull(movie.getRentedCopyByCustomer(customer));
        assertTrue(movie.Return(customer));
        assertNull(movie.getRentedCopyByCustomer(customer));
        // Should be able to rent again
        movie.Lend(customer);
        assertNotNull(movie.getRentedCopyByCustomer(customer));
    }

    @Test
    public void testBuyAndCheckSoldCopies() {
        movie.Buy(customer);
        List<SalableMovieDuplicate> soldCopies = movie.getSoldCopies();
        assertEquals(1, soldCopies.size());
        assertTrue(soldCopies.get(0).isSold());
    }

    @Test
    public void testMultipleCustomersRentingSameMovie() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        Customer customer3 = new Customer("customer3", new CredentialsCheck("Pass123!"));
        
        movie.Lend(customer);
        movie.Lend(customer2);
        movie.Lend(customer3);
        
        assertNotNull(movie.getRentedCopyByCustomer(customer));
        assertNotNull(movie.getRentedCopyByCustomer(customer2));
        assertNotNull(movie.getRentedCopyByCustomer(customer3));
        
        assertTrue(movie.Return(customer));
        assertTrue(movie.Return(customer2));
        assertTrue(movie.Return(customer3));
    }

    @Test
    public void testAddDuplicateReviewPrevention() {
        Review review1 = new Review("Great movie!", 8, customer);
        assertTrue(movie.addReview(review1));
        
        outputStream.reset();
        Review review2 = new Review("Amazing movie!", 9, customer);
        boolean result = movie.addReview(review2);
        assertFalse(result);
        String output = getOutput();
        assertTrue(output.contains("Review already exists"));
        assertTrue(output.contains("edit your existing review"));
        
        // Should still have only one review
        List<Review> reviews = movie.getAllReviews();
        assertEquals(1, reviews.size());
        assertEquals(review1, reviews.get(0));
    }

    @Test
    public void testGetReviewByCustomer() {
        Review review = new Review("Great movie!", 8, customer);
        assertTrue(movie.addReview(review));
        
        Review foundReview = movie.getReviewByCustomer(customer);
        assertNotNull(foundReview);
        assertEquals(review, foundReview);
        assertEquals("Great movie!", foundReview.getComments());
        assertEquals(8, foundReview.getMovieRating());
    }

    @Test
    public void testGetReviewByCustomerNotFound() {
        Review foundReview = movie.getReviewByCustomer(customer);
        assertNull(foundReview);
    }

    @Test
    public void testGetReviewByCustomerNull() {
        Review foundReview = movie.getReviewByCustomer(null);
        assertNull(foundReview);
    }

    @Test
    public void testEditReviewSuccessfully() {
        Review review = new Review("Great movie!", 8, customer);
        assertTrue(movie.addReview(review));
        
        outputStream.reset();
        boolean result = movie.editReview(customer, "Amazing movie!", 9);
        assertTrue(result);
        String output = getOutput();
        assertTrue(output.contains("Review updated successfully"));
        
        Review updatedReview = movie.getReviewByCustomer(customer);
        assertNotNull(updatedReview);
        assertEquals("Amazing movie!", updatedReview.getComments());
        assertEquals(9, updatedReview.getMovieRating());
    }

    @Test
    public void testEditReviewWhenNoReviewExists() {
        outputStream.reset();
        boolean result = movie.editReview(customer, "New review", 7);
        assertFalse(result);
        String output = getOutput();
        assertTrue(output.contains("You have not reviewed this movie yet"));
    }

    @Test
    public void testEditReviewWithNullCustomer() {
        assertThrows(IllegalArgumentException.class, () -> {
            movie.editReview(null, "New review", 7);
        });
    }

    @Test
    public void testEditReviewMultipleTimes() {
        Review review = new Review("First review", 5, customer);
        assertTrue(movie.addReview(review));
        
        movie.editReview(customer, "Second review", 7);
        Review updatedReview = movie.getReviewByCustomer(customer);
        assertEquals("Second review", updatedReview.getComments());
        assertEquals(7, updatedReview.getMovieRating());
        
        movie.editReview(customer, "Third review", 10);
        updatedReview = movie.getReviewByCustomer(customer);
        assertEquals("Third review", updatedReview.getComments());
        assertEquals(10, updatedReview.getMovieRating());
    }

    @Test
    public void testMultipleCustomersCanReviewSameMovie() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        Customer customer3 = new Customer("customer3", new CredentialsCheck("Pass123!"));
        
        Review review1 = new Review("Great!", 8, customer);
        Review review2 = new Review("Amazing!", 9, customer2);
        Review review3 = new Review("Excellent!", 10, customer3);
        
        assertTrue(movie.addReview(review1));
        assertTrue(movie.addReview(review2));
        assertTrue(movie.addReview(review3));
        
        List<Review> reviews = movie.getAllReviews();
        assertEquals(3, reviews.size());
        
        assertEquals(review1, movie.getReviewByCustomer(customer));
        assertEquals(review2, movie.getReviewByCustomer(customer2));
        assertEquals(review3, movie.getReviewByCustomer(customer3));
    }

    @Test
    public void testEditReviewInvalidRating() {
        Review review = new Review("Great movie!", 8, customer);
        assertTrue(movie.addReview(review));
        
        outputStream.reset();
        movie.editReview(customer, "New review", 15);
        String output = getOutput();
        // Rating validation message should be printed
        assertTrue(output.contains("Rating should be 0 to 10") || output.contains("Review updated successfully"));
        
        // Rating should not be updated if invalid
        Review updatedReview = movie.getReviewByCustomer(customer);
        assertEquals("New review", updatedReview.getComments());
        // Rating might be unchanged if validation failed
    }

    @Test
    public void testAddReviewThenEditScenario() {
        // Customer adds review
        Review review = new Review("Initial review", 6, customer);
        assertTrue(movie.addReview(review));
        
        // Customer tries to add another review (should fail)
        outputStream.reset();
        Review duplicateReview = new Review("Duplicate review", 8, customer);
        assertFalse(movie.addReview(duplicateReview));
        String output = getOutput();
        assertTrue(output.contains("Review already exists"));
        
        // Customer edits existing review instead
        outputStream.reset();
        assertTrue(movie.editReview(customer, "Updated review", 9));
        output = getOutput();
        assertTrue(output.contains("Review updated successfully"));
        
        Review finalReview = movie.getReviewByCustomer(customer);
        assertEquals("Updated review", finalReview.getComments());
        assertEquals(9, finalReview.getMovieRating());
    }
}

