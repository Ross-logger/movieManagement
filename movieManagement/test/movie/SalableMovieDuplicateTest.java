package movieManagement.test.movie;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.src.movie.Movie;
import movieManagement.src.movie.SalableMovieDuplicate;
import movieManagement.src.users.Customer;

public class SalableMovieDuplicateTest {

    private Movie movie;
    private SalableMovieDuplicate saleCopy;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        movie = new Movie("tt1234567", "Test Movie", "Test Director", "Test Studio", "2024-01-01", "Test Description", 25);
        saleCopy = new SalableMovieDuplicate(movie);
        CredentialsCheck credential = new CredentialsCheck("TestPass123!");
        customer = new Customer("testcustomer", credential);
    }

    @Test
    public void testSalableMovieDuplicateInitialization() {
        assertNotNull(saleCopy);
        assertNotNull(saleCopy.getCopyId());
        assertFalse(saleCopy.isSold());
    }

    @Test
    public void testGetCopyId() {
        String copyId = saleCopy.getCopyId();
        assertNotNull(copyId);
        assertFalse(copyId.isEmpty());
    }

    @Test
    public void testGetCopyIdUnique() {
        SalableMovieDuplicate copy1 = new SalableMovieDuplicate(movie);
        SalableMovieDuplicate copy2 = new SalableMovieDuplicate(movie);
        assertNotEquals(copy1.getCopyId(), copy2.getCopyId());
    }

    @Test
    public void testIsSoldInitiallyFalse() {
        assertFalse(saleCopy.isSold());
    }

    @Test
    public void testSold() {
        assertFalse(saleCopy.isSold());
        saleCopy.sold(customer);
        assertTrue(saleCopy.isSold());
    }

    @Test
    public void testSoldWithNullCustomer() {
        saleCopy.sold(null);
        assertTrue(saleCopy.isSold());
    }

    @Test
    public void testSoldWithDifferentCustomers() {
        Customer customer1 = new Customer("customer1", new CredentialsCheck("Pass123!"));
        
        saleCopy.sold(customer1);
        assertTrue(saleCopy.isSold());
        
        // Once sold, it should remain sold even if we try to sell again
        // (though this shouldn't happen in practice)
        assertTrue(saleCopy.isSold());
    }

    @Test
    public void testMultipleCopiesSameMovie() {
        SalableMovieDuplicate copy1 = new SalableMovieDuplicate(movie);
        SalableMovieDuplicate copy2 = new SalableMovieDuplicate(movie);
        
        assertNotEquals(copy1.getCopyId(), copy2.getCopyId());
        
        copy1.sold(customer);
        assertTrue(copy1.isSold());
        assertFalse(copy2.isSold());
    }

    @Test
    public void testSalableMovieDuplicateWithDifferentMovies() {
        Movie movie2 = new Movie("tt9999999", "Another Movie", "Director", "Studio", "2024-01-01", "Desc", 20);
        SalableMovieDuplicate copy1 = new SalableMovieDuplicate(movie);
        SalableMovieDuplicate copy2 = new SalableMovieDuplicate(movie2);
        
        assertNotEquals(copy1.getCopyId(), copy2.getCopyId());
        assertNotNull(copy1.getCopyId());
        assertNotNull(copy2.getCopyId());
    }

    @Test
    public void testSoldCannotBeUndone() {
        saleCopy.sold(customer);
        assertTrue(saleCopy.isSold());
        // There's no method to unsell, so once sold it stays sold
        assertTrue(saleCopy.isSold());
    }

    @Test
    public void testCopyIdFormat() {
        String copyId = saleCopy.getCopyId();
        // UUID format: 8-4-4-4-12 hexadecimal characters
        assertTrue(copyId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    public void testMultipleSales() {
        SalableMovieDuplicate copy1 = new SalableMovieDuplicate(movie);
        SalableMovieDuplicate copy2 = new SalableMovieDuplicate(movie);
        SalableMovieDuplicate copy3 = new SalableMovieDuplicate(movie);
        
        Customer customer1 = new Customer("customer1", new CredentialsCheck("Pass123!"));
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        Customer customer3 = new Customer("customer3", new CredentialsCheck("Pass123!"));
        
        copy1.sold(customer1);
        copy2.sold(customer2);
        copy3.sold(customer3);
        
        assertTrue(copy1.isSold());
        assertTrue(copy2.isSold());
        assertTrue(copy3.isSold());
    }
}

