package movieManagement.test.authentication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.authentication.AuthSession;
import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.src.users.Customer;

public class AuthSessionTest {

    private Customer customer;
    private AuthSession session;

    @BeforeEach
    public void setUp() {
        CredentialsCheck credential = new CredentialsCheck("TestPass123!");
        customer = new Customer("testuser", credential);
        session = new AuthSession(customer);
    }

    @Test
    public void testAuthSessionConstructor() {
        assertNotNull(session);
        assertEquals(customer, session.getUser());
        assertTrue(session.isActive());
        assertNotNull(session.getSessionToken());
        assertTrue(session.getCreatedAt() > 0);
    }

    @Test
    public void testGetUser() {
        assertEquals(customer, session.getUser());
    }

    @Test
    public void testIsActiveInitiallyTrue() {
        assertTrue(session.isActive());
    }

    @Test
    public void testSetActiveToFalse() {
        session.setActive(false);
        assertFalse(session.isActive());
    }

    @Test
    public void testSetActiveToTrue() {
        session.setActive(false);
        session.setActive(true);
        assertTrue(session.isActive());
    }

    @Test
    public void testGetSessionToken() {
        assertNotNull(session.getSessionToken());
        assertTrue(session.getSessionToken().toString().length() > 0);
    }

    @Test
    public void testSessionTokenUniqueness() {
        AuthSession session1 = new AuthSession(customer);
        AuthSession session2 = new AuthSession(customer);
        assertNotEquals(session1.getSessionToken(), session2.getSessionToken());
    }

    @Test
    public void testGetCreatedAt() {
        long createdAt = session.getCreatedAt();
        assertTrue(createdAt > 0);
        assertTrue(createdAt <= System.currentTimeMillis());
    }

    @Test
    public void testCreatedAtTimestamp() {
        long beforeCreation = System.currentTimeMillis();
        AuthSession newSession = new AuthSession(customer);
        long afterCreation = System.currentTimeMillis();
        
        assertTrue(newSession.getCreatedAt() >= beforeCreation);
        assertTrue(newSession.getCreatedAt() <= afterCreation);
    }

    @Test
    public void testSessionWithDifferentUser() {
        CredentialsCheck cred2 = new CredentialsCheck("Another123!");
        Customer customer2 = new Customer("anotheruser", cred2);
        AuthSession session2 = new AuthSession(customer2);
        
        assertNotEquals(session.getUser(), session2.getUser());
        assertNotEquals(session.getSessionToken(), session2.getSessionToken());
    }

    @Test
    public void testMultipleSessionsForSameUser() {
        AuthSession session1 = new AuthSession(customer);
        // Small delay to ensure different timestamps
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            // Ignore
        }
        AuthSession session2 = new AuthSession(customer);
        
        assertEquals(session1.getUser(), session2.getUser());
        assertNotEquals(session1.getSessionToken(), session2.getSessionToken());
        // Timestamps might be equal if created very quickly, so just check they're reasonable
        assertTrue(session1.getCreatedAt() <= session2.getCreatedAt());
    }

    @Test
    public void testSetActiveMultipleTimes() {
        session.setActive(false);
        assertFalse(session.isActive());
        
        session.setActive(true);
        assertTrue(session.isActive());
        
        session.setActive(false);
        assertFalse(session.isActive());
        
        session.setActive(true);
        assertTrue(session.isActive());
    }
}

