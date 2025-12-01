package movie;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.authentication.CredentialsCheck;
import src.movie.Movie;
import src.movie.RentalMovieDuplicate;
import src.movie.RentalRecord;
import src.users.Customer;

public class RentalMovieDuplicateTest {

    private Movie movie;
    private RentalMovieDuplicate rentalCopy;
    private Customer customer;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        movie = new Movie("tt1234567", "Test Movie", "Test Director", "Test Studio", "2024-01-01", "Test Description", 25);
        rentalCopy = new RentalMovieDuplicate(movie);
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
    public void testRentalMovieDuplicateInitialization() {
        assertNotNull(rentalCopy);
        assertNotNull(rentalCopy.getCopyId());
        assertFalse(rentalCopy.isRented());
        assertNull(rentalCopy.getCustomer());
    }

    @Test
    public void testGetCopyId() {
        String copyId = rentalCopy.getCopyId();
        assertNotNull(copyId);
        assertFalse(copyId.isEmpty());
    }

    @Test
    public void testGetCopyIdUnique() {
        RentalMovieDuplicate copy1 = new RentalMovieDuplicate(movie);
        RentalMovieDuplicate copy2 = new RentalMovieDuplicate(movie);
        assertNotEquals(copy1.getCopyId(), copy2.getCopyId());
    }

    @Test
    public void testIsRentedInitiallyFalse() {
        assertFalse(rentalCopy.isRented());
    }

    @Test
    public void testRentSuccessfully() {
        outputStream.reset();
        rentalCopy.rent(customer);
        assertTrue(rentalCopy.isRented());
        assertEquals(customer, rentalCopy.getCustomer());
        String output = getOutput();
        assertTrue(output.isEmpty());
    }

    @Test
    public void testRentWhenAlreadyRented() {
        rentalCopy.rent(customer);
        outputStream.reset();
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        rentalCopy.rent(customer2);
        String output = getOutput();
        assertTrue(output.contains("This copy is already rented out"));
        assertEquals(customer, rentalCopy.getCustomer());
    }

    @Test
    public void testReturnMovie() {
        rentalCopy.rent(customer);
        assertTrue(rentalCopy.isRented());
        assertEquals(customer, rentalCopy.getCustomer());
        
        rentalCopy.returnMovie();
        assertFalse(rentalCopy.isRented());
        assertNull(rentalCopy.getCustomer());
    }

    @Test
    public void testReturnMovieWhenNotRented() {
        assertFalse(rentalCopy.isRented());
        rentalCopy.returnMovie();
        assertFalse(rentalCopy.isRented());
        assertNull(rentalCopy.getCustomer());
    }

    @Test
    public void testGetCustomerWhenNotRented() {
        assertNull(rentalCopy.getCustomer());
    }

    @Test
    public void testGetCustomerWhenRented() {
        rentalCopy.rent(customer);
        assertEquals(customer, rentalCopy.getCustomer());
    }

    @Test
    public void testRentAndReturnCycle() {
        rentalCopy.rent(customer);
        assertTrue(rentalCopy.isRented());
        rentalCopy.returnMovie();
        assertFalse(rentalCopy.isRented());
        
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        rentalCopy.rent(customer2);
        assertTrue(rentalCopy.isRented());
        assertEquals(customer2, rentalCopy.getCustomer());
    }

    @Test
    public void testAddRentalRecord() {
        RentalRecord record = new RentalRecord(customer, rentalCopy);
        rentalCopy.addRentalRecord(record);
        // Since there's no getter for rental history, we can only test that no exception is thrown
        assertNotNull(rentalCopy);
    }

    @Test
    public void testAddRentalRecordNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            rentalCopy.addRentalRecord(null);
        });
    }

    @Test
    public void testAddMultipleRentalRecords() {
        rentalCopy.rent(customer);
        RentalRecord record1 = new RentalRecord(customer, rentalCopy);
        rentalCopy.addRentalRecord(record1);
        
        rentalCopy.returnMovie();
        rentalCopy.rent(customer);
        RentalRecord record2 = new RentalRecord(customer, rentalCopy);
        rentalCopy.addRentalRecord(record2);
        
        assertNotNull(rentalCopy);
    }

    @Test
    public void testDisplayCopyDetails() {
        outputStream.reset();
        rentalCopy.displayCopyDetails();
        String output = getOutput();
        assertTrue(output.contains("Rental Copy ID:"));
        assertTrue(output.contains(rentalCopy.getCopyId()));
    }

    @Test
    public void testRentWithDifferentCustomers() {
        Customer customer1 = new Customer("customer1", new CredentialsCheck("Pass123!"));
        Customer customer2 = new Customer("customer2", new CredentialsCheck("Pass123!"));
        
        rentalCopy.rent(customer1);
        assertEquals(customer1, rentalCopy.getCustomer());
        rentalCopy.returnMovie();
        
        rentalCopy.rent(customer2);
        assertEquals(customer2, rentalCopy.getCustomer());
    }

    @Test
    public void testMultipleCopiesSameMovie() {
        RentalMovieDuplicate copy1 = new RentalMovieDuplicate(movie);
        RentalMovieDuplicate copy2 = new RentalMovieDuplicate(movie);
        
        assertNotEquals(copy1.getCopyId(), copy2.getCopyId());
        
        copy1.rent(customer);
        assertTrue(copy1.isRented());
        assertFalse(copy2.isRented());
    }

    @Test
    public void testRentalMovieDuplicateWithDifferentMovies() {
        Movie movie2 = new Movie("tt9999999", "Another Movie", "Director", "Studio", "2024-01-01", "Desc", 20);
        RentalMovieDuplicate copy1 = new RentalMovieDuplicate(movie);
        RentalMovieDuplicate copy2 = new RentalMovieDuplicate(movie2);
        
        assertNotEquals(copy1.getCopyId(), copy2.getCopyId());
        assertNotNull(copy1.getCopyId());
        assertNotNull(copy2.getCopyId());
    }

    @Test
    public void testRentSameCustomerMultipleTimes() {
        rentalCopy.rent(customer);
        rentalCopy.returnMovie();
        rentalCopy.rent(customer);
        assertTrue(rentalCopy.isRented());
        assertEquals(customer, rentalCopy.getCustomer());
    }
}

