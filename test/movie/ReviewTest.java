package movie;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.authentication.CredentialsCheck;
import src.movie.Review;
import src.users.Customer;

public class ReviewTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        CredentialsCheck credential = new CredentialsCheck("TestPass123!");
        customer = new Customer("testcustomer", credential);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    private String getOutput() {
        return outputStream.toString();
    }

    @Test
    public void testReviewInitialization() {
        Review review = new Review("Great movie!", 8, customer);
        assertNotNull(review);
        assertEquals("Great movie!", review.getComments());
        assertEquals(8, review.getMovieRating());
        assertEquals(customer, review.getCustomer());
    }

    @Test
    public void testGetComments() {
        Review review = new Review("Excellent cinematography and storytelling.", 9, customer);
        assertEquals("Excellent cinematography and storytelling.", review.getComments());
    }

    @Test
    public void testGetMovieRating() {
        Review review = new Review("Great!", 7, customer);
        assertEquals(7, review.getMovieRating());
    }

    @Test
    public void testReviewWithMinimumRating() {
        Review review = new Review("Test", 0, customer);
        assertEquals(0, review.getMovieRating());
        assertEquals("Test", review.getComments());
    }

    @Test
    public void testReviewWithMaximumRating() {
        Review review = new Review("Perfect!", 10, customer);
        assertEquals(10, review.getMovieRating());
        assertEquals("Perfect!", review.getComments());
    }

    @Test
    public void testReviewWithRatingBelowZero() {
        outputStream.reset();
        Review review = new Review("Bad rating", -1, customer);
        String output = getOutput();
        assertTrue(output.contains("Rating should be 0 to 10"));
        // Rating should not be set when invalid
        assertEquals(0, review.getMovieRating());
    }

    @Test
    public void testReviewWithRatingAboveTen() {
        outputStream.reset();
        Review review = new Review("Bad rating", 11, customer);
        String output = getOutput();
        assertTrue(output.contains("Rating should be 0 to 10"));
        // Rating should not be set when invalid
        assertEquals(0, review.getMovieRating());
    }

    @Test
    public void testReviewWithRatingExactlyEleven() {
        outputStream.reset();
        Review review = new Review("Test", 11, customer);
        String output = getOutput();
        assertTrue(output.contains("Rating should be 0 to 10"));
        assertEquals(0, review.getMovieRating());
    }

    @Test
    public void testReviewWithRatingNegative() {
        outputStream.reset();
        Review review = new Review("Test", -5, customer);
        String output = getOutput();
        assertTrue(output.contains("Rating should be 0 to 10"));
        assertEquals(0, review.getMovieRating());
    }

    @Test
    public void testReviewWithValidRatings() {
        for (int i = 0; i <= 10; i++) {
            outputStream.reset();
            Review review = new Review("Rating " + i, i, customer);
            if (i >= 0 && i <= 10) {
                assertEquals(i, review.getMovieRating());
            }
        }
    }

    @Test
    public void testToString() {
        Review review = new Review("Excellent cinematography and storytelling.", 9, customer);
        String result = review.toString();
        assertNotNull(result);
        assertTrue(result.contains("Movie Review"));
        assertTrue(result.contains("Movie Rating: 9"));
        assertTrue(result.contains("Comment: Excellent cinematography and storytelling."));
    }

    @Test
    public void testToStringWithDifferentRatings() {
        Review review1 = new Review("Great!", 5, customer);
        String result1 = review1.toString();
        assertTrue(result1.contains("Movie Rating: 5"));
        assertTrue(result1.contains("Comment: Great!"));

        Review review2 = new Review("Amazing!", 10, customer);
        String result2 = review2.toString();
        assertTrue(result2.contains("Movie Rating: 10"));
        assertTrue(result2.contains("Comment: Amazing!"));
    }

    @Test
    public void testToStringWithEmptyComment() {
        Review review = new Review("", 7, customer);
        String result = review.toString();
        assertTrue(result.contains("Movie Rating: 7"));
        assertTrue(result.contains("Comment: "));
    }

    @Test
    public void testToStringWithLongComment() {
        String longComment = "This is a very long comment that describes the movie in great detail. " +
                            "It includes multiple sentences and provides comprehensive feedback about " +
                            "the cinematography, acting, plot, and overall experience.";
        Review review = new Review(longComment, 8, customer);
        String result = review.toString();
        assertTrue(result.contains(longComment));
        assertTrue(result.contains("Movie Rating: 8"));
    }

    @Test
    public void testReviewWithSpecialCharactersInComment() {
        Review review = new Review("Great movie! 5/5 stars ⭐⭐⭐⭐⭐", 9, customer);
        assertEquals("Great movie! 5/5 stars ⭐⭐⭐⭐⭐", review.getComments());
        assertEquals(9, review.getMovieRating());
    }

    @Test
    public void testMultipleReviews() {
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        Customer customer3 = new Customer("customer3", new CredentialsCheck("Pass123!"));
        Review review1 = new Review("First review", 7, customer);
        Review review2 = new Review("Second review", 8, customer2);
        Review review3 = new Review("Third review", 9, customer3);
        
        assertNotEquals(review1.getComments(), review2.getComments());
        assertNotEquals(review1.getMovieRating(), review2.getMovieRating());
        assertEquals(7, review1.getMovieRating());
        assertEquals(8, review2.getMovieRating());
        assertEquals(9, review3.getMovieRating());
    }

    @Test
    public void testReviewRatingBoundaryConditions() {
        // Test boundary: -1 (invalid)
        outputStream.reset();
        Review review1 = new Review("Test", -1, customer);
        assertTrue(getOutput().contains("Rating should be 0 to 10"));
        assertEquals(0, review1.getMovieRating());

        // Test boundary: 0 (valid)
        outputStream.reset();
        Review review2 = new Review("Test", 0, customer);
        assertTrue(getOutput().isEmpty());
        assertEquals(0, review2.getMovieRating());

        // Test boundary: 10 (valid)
        outputStream.reset();
        Review review3 = new Review("Test", 10, customer);
        assertTrue(getOutput().isEmpty());
        assertEquals(10, review3.getMovieRating());

        // Test boundary: 11 (invalid)
        outputStream.reset();
        Review review4 = new Review("Test", 11, customer);
        assertTrue(getOutput().contains("Rating should be 0 to 10"));
        assertEquals(0, review4.getMovieRating());
    }

    @Test
    public void testSetComments() {
        Review review = new Review("Original comment", 8, customer);
        review.setComments("Updated comment");
        assertEquals("Updated comment", review.getComments());
    }

    @Test
    public void testSetRating() {
        Review review = new Review("Comment", 5, customer);
        review.setRating(9);
        assertEquals(9, review.getMovieRating());
    }

    @Test
    public void testSetRatingInvalid() {
        outputStream.reset();
        Review review = new Review("Comment", 5, customer);
        review.setRating(15);
        String output = getOutput();
        assertTrue(output.contains("Rating should be 0 to 10"));
        // Rating should remain unchanged
        assertEquals(5, review.getMovieRating());
    }

    @Test
    public void testGetCustomer() {
        Review review = new Review("Comment", 8, customer);
        assertEquals(customer, review.getCustomer());
    }
}

