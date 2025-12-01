package movieManagement.test.users;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.src.movie.Movie;
import movieManagement.src.movie.Review;
import movieManagement.src.users.Customer;
import movieManagement.src.users.Membership;
import movieManagement.src.users.GoldMemberState;
import movieManagement.src.users.PlatinumMemberState;
import movieManagement.src.users.NonMemberState;

public class CustomerTest {

    private CredentialsCheck credential;
    private Customer customer;
    private Movie movie;
    private Review review;

    @BeforeEach
    public void setUp() {
        credential = new CredentialsCheck("TestPass123!");
        customer = new Customer("testcustomer", credential);
        movie = new Movie("tt1234567", "Test Movie", "Test Director", "Test Studio", "2024-01-01", "Test Description", 25);
        review = new Review("Great movie!", 8, customer);
    }

    @Test
    public void testCustomerInitialization() {
        assertNotNull(customer.getMembership());
        assertTrue(customer.getRentedMovies().isEmpty());
        assertTrue(customer.getPurchasedMovies().isEmpty());
        assertTrue(customer.getReviews().isEmpty());
        assertEquals("NON_MEMBER", customer.getMembership().getType());
    }

    @Test
    public void testGetMembership() {
        Membership membership = customer.getMembership();
        assertNotNull(membership);
        assertEquals("NON_MEMBER", membership.getType());
        assertEquals(2, membership.getMaxRentMovies());
    }

    @Test
    public void testAddRemoveRentedMovie() {
        customer.addRentedMovie(movie);
        assertTrue(customer.getRentedMovies().contains(movie));
        assertEquals(1, customer.getRentedMovies().size());

        customer.removeRentedMovie(movie);
        assertFalse(customer.getRentedMovies().contains(movie));
        assertEquals(0, customer.getRentedMovies().size());
    }

    @Test
    public void testAddPurchasedMovie() {
        customer.addPurchasedMovie(movie);
        assertTrue(customer.getPurchasedMovies().contains(movie));
        assertEquals(1, customer.getPurchasedMovies().size());
    }

    @Test
    public void testAddReview() {
        customer.addReview(review);
        assertTrue(customer.getReviews().contains(review));
        assertEquals(1, customer.getReviews().size());
    }

    @Test
    public void testSetMembershipToGold() {
        customer.setMembership(new GoldMemberState());
        assertEquals("GOLD", customer.getMembership().getType());
        assertEquals(5, customer.getMembership().getMaxRentMovies());
        assertEquals(14, customer.getMembership().getRentalDays());
    }

    @Test
    public void testSetMembershipToPlatinum() {
        customer.setMembership(new PlatinumMemberState());
        assertEquals("PLATINUM", customer.getMembership().getType());
        assertEquals(8, customer.getMembership().getMaxRentMovies());
        assertEquals(21, customer.getMembership().getRentalDays());
    }

    @Test
    public void testMultipleRentedMovies() {
        Movie movie1 = new Movie("tt1111111", "Movie 1", "Director 1", "Studio 1", "2024-01-01", "Desc 1", 20);
        Movie movie2 = new Movie("tt2222222", "Movie 2", "Director 2", "Studio 2", "2024-01-02", "Desc 2", 25);
        
        customer.addRentedMovie(movie1);
        customer.addRentedMovie(movie2);
        
        assertEquals(2, customer.getRentedMovies().size());
        assertTrue(customer.getRentedMovies().contains(movie1));
        assertTrue(customer.getRentedMovies().contains(movie2));
    }

    @Test
    public void testMultiplePurchasedMovies() {
        Movie movie1 = new Movie("tt1111111", "Movie 1", "Director 1", "Studio 1", "2024-01-01", "Desc 1", 20);
        Movie movie2 = new Movie("tt2222222", "Movie 2", "Director 2", "Studio 2", "2024-01-02", "Desc 2", 25);
        
        customer.addPurchasedMovie(movie1);
        customer.addPurchasedMovie(movie2);
        
        assertEquals(2, customer.getPurchasedMovies().size());
        assertTrue(customer.getPurchasedMovies().contains(movie1));
        assertTrue(customer.getPurchasedMovies().contains(movie2));
    }

    @Test
    public void testCustomerWithUsernameOnly() {
        Customer customer2 = new Customer("usernameonly");
        assertNotNull(customer2.getMembership());
        assertEquals("NON_MEMBER", customer2.getMembership().getType());
    }

    @Test
    public void testCustomerWithId() {
        Customer customer3 = new Customer("testuser", 123);
        assertNotNull(customer3.getMembership());
        assertEquals("NON_MEMBER", customer3.getMembership().getType());
    }

    @Test
    public void testMembershipUpgradeToGold() {
        Customer customer = new Customer("testuser", credential);
        assertEquals("NON_MEMBER", customer.getMembership().getType());
        
        // Add 2 movies - should still be NonMember
        Movie movie1 = new Movie("tt1111111", "Movie 1", "Director 1", "Studio 1", "2024-01-01", "Desc 1", 20);
        Movie movie2 = new Movie("tt2222222", "Movie 2", "Director 2", "Studio 2", "2024-01-02", "Desc 2", 25);
        customer.addPurchasedMovie(movie1);
        customer.addPurchasedMovie(movie2);
        assertEquals("NON_MEMBER", customer.getMembership().getType());
        
        // Add 3rd movie - should upgrade to Gold
        Movie movie3 = new Movie("tt3333333", "Movie 3", "Director 3", "Studio 3", "2024-01-03", "Desc 3", 30);
        customer.addPurchasedMovie(movie3);
        assertEquals("GOLD", customer.getMembership().getType());
        assertEquals(5, customer.getMembership().getMaxRentMovies());
        assertEquals(14, customer.getMembership().getRentalDays());
    }

    @Test
    public void testMembershipUpgradeToPlatinum() {
        Customer customer = new Customer("testuser", credential);
        // First upgrade to Gold by purchasing 3 movies
        for (int i = 1; i <= 3; i++) {
            Movie movie = new Movie("tt" + i, "Movie " + i, "Director " + i, "Studio " + i, "2024-01-01", "Desc " + i, 20);
            customer.addPurchasedMovie(movie);
        }
        assertEquals("GOLD", customer.getMembership().getType());
        
        // Add 6 more movies (total 9) - should still be Gold
        for (int i = 4; i <= 9; i++) {
            Movie movie = new Movie("tt" + i, "Movie " + i, "Director " + i, "Studio " + i, "2024-01-01", "Desc " + i, 20);
            customer.addPurchasedMovie(movie);
        }
        assertEquals("GOLD", customer.getMembership().getType());
        
        // Add 10th movie (total 10) - should upgrade to Platinum
        Movie movie10 = new Movie("tt10", "Movie 10", "Director 10", "Studio 10", "2024-01-01", "Desc 10", 20);
        customer.addPurchasedMovie(movie10);
        assertEquals("PLATINUM", customer.getMembership().getType());
        assertEquals(8, customer.getMembership().getMaxRentMovies());
        assertEquals(21, customer.getMembership().getRentalDays());
    }

    @Test
    public void testMembershipUpgradeFromNonMemberToPlatinum() {
        Customer customer = new Customer("testuser", credential);
        assertEquals("NON_MEMBER", customer.getMembership().getType());
        
        // Add 2 movies - should still be NonMember
        for (int i = 1; i <= 2; i++) {
            Movie movie = new Movie("tt" + i, "Movie " + i, "Director " + i, "Studio " + i, "2024-01-01", "Desc " + i, 20);
            customer.addPurchasedMovie(movie);
        }
        assertEquals("NON_MEMBER", customer.getMembership().getType());
        
        // Add 3rd movie - should upgrade to Gold
        Movie movie3 = new Movie("tt3", "Movie 3", "Director 3", "Studio 3", "2024-01-01", "Desc 3", 20);
        customer.addPurchasedMovie(movie3);
        assertEquals("GOLD", customer.getMembership().getType());
        
        // Add 7 more movies (total 10) - should upgrade to Platinum
        for (int i = 4; i <= 10; i++) {
            Movie movie = new Movie("tt" + i, "Movie " + i, "Director " + i, "Studio " + i, "2024-01-01", "Desc " + i, 20);
            customer.addPurchasedMovie(movie);
        }
        assertEquals("PLATINUM", customer.getMembership().getType());
    }

    @Test
    public void testNoUpgradeWhenAlreadyAtMaxLevel() {
        Customer customer = new Customer("testuser", credential);
        customer.setMembership(new PlatinumMemberState());
        assertEquals("PLATINUM", customer.getMembership().getType());
        
        // Add movies - should stay Platinum
        Movie movie = new Movie("tt1111111", "Movie 1", "Director 1", "Studio 1", "2024-01-01", "Desc 1", 20);
        customer.addPurchasedMovie(movie);
        assertEquals("PLATINUM", customer.getMembership().getType());
    }
}

