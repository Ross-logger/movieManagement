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
        review = new Review("Great movie!", 8);
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
}

