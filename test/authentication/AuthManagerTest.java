package authentication;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.authentication.AuthManager;
import src.authentication.AuthSession;
import src.authentication.CredentialsCheck;
import src.exceptions.UserNotFoundException;
import src.users.Customer;

public class AuthManagerTest {

    private AuthManager authManager;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setUp() {
        // Get the AuthManager instance
        authManager = AuthManager.getInstance();
        
        // Clear active sessions using reflection
        try {
            java.lang.reflect.Field sessionsField = AuthManager.class.getDeclaredField("activeSessions");
            sessionsField.setAccessible(true);
            ArrayList<?> sessions = (ArrayList<?>) sessionsField.get(authManager);
            sessions.clear();
        } catch (Exception e) {
            fail("Failed to clear AuthManager sessions: " + e.getMessage());
        }
        
        CredentialsCheck cred1 = new CredentialsCheck("TestPass123!");
        CredentialsCheck cred2 = new CredentialsCheck("Another123!");
        customer1 = new Customer("user1", cred1);
        customer2 = new Customer("user2", cred2);
    }

    @Test
    public void testGetInstance() {
        AuthManager manager1 = AuthManager.getInstance();
        AuthManager manager2 = AuthManager.getInstance();
        assertSame(manager1, manager2);
        assertNotNull(manager1);
    }

    @Test
    public void testGetActiveSessionCountInitiallyZero() {
        assertEquals(0, authManager.getActiveSessionCount());
    }

    @Test
    public void testAddSession() {
        AuthSession session = new AuthSession(customer1);
        authManager.addSession(session);
        assertEquals(1, authManager.getActiveSessionCount());
    }

    @Test
    public void testAddMultipleSessions() {
        AuthSession session1 = new AuthSession(customer1);
        AuthSession session2 = new AuthSession(customer2);
        
        authManager.addSession(session1);
        authManager.addSession(session2);
        
        assertEquals(2, authManager.getActiveSessionCount());
    }

    @Test
    public void testCreateSession() {
        authManager.createSession(customer1);
        assertEquals(1, authManager.getActiveSessionCount());
        
        AuthSession foundSession = authManager.findSessionByUser(customer1);
        assertNotNull(foundSession);
        assertEquals(customer1, foundSession.getUser());
        assertTrue(foundSession.isActive());
    }

    @Test
    public void testCreateMultipleSessionsForSameUser() {
        authManager.createSession(customer1);
        authManager.createSession(customer1);
        
        assertEquals(2, authManager.getActiveSessionCount());
    }

    @Test
    public void testTerminateSessionSuccess() throws UserNotFoundException {
        authManager.createSession(customer1);
        assertEquals(1, authManager.getActiveSessionCount());
        
        authManager.terminateSession(customer1);
        assertEquals(0, authManager.getActiveSessionCount());
        
        AuthSession foundSession = authManager.findSessionByUser(customer1);
        assertNull(foundSession);
    }

    @Test
    public void testTerminateSessionUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            authManager.terminateSession(customer1);
        });
    }

    @Test
    public void testTerminateSessionWhenMultipleSessionsExist() throws UserNotFoundException {
        authManager.createSession(customer1);
        authManager.createSession(customer1);
        authManager.createSession(customer2);
        
        assertEquals(3, authManager.getActiveSessionCount());
        
        authManager.terminateSession(customer1);
        assertEquals(2, authManager.getActiveSessionCount());
        
        // Should still have one session for customer1
        AuthSession foundSession = authManager.findSessionByUser(customer1);
        assertNotNull(foundSession);
    }

    @Test
    public void testTerminateSessionSetsInactive() throws UserNotFoundException {
        authManager.createSession(customer1);
        AuthSession session = authManager.findSessionByUser(customer1);
        assertTrue(session.isActive());
        
        authManager.terminateSession(customer1);
        assertFalse(session.isActive());
    }

    @Test
    public void testTerminateAllSessionsForUser() throws UserNotFoundException {
        authManager.createSession(customer1);
        authManager.createSession(customer1);
        authManager.createSession(customer1);
        
        assertEquals(3, authManager.getActiveSessionCount());
        
        // Terminate first session
        authManager.terminateSession(customer1);
        assertEquals(2, authManager.getActiveSessionCount());
        
        // Terminate second session
        authManager.terminateSession(customer1);
        assertEquals(1, authManager.getActiveSessionCount());
        
        // Terminate third session
        authManager.terminateSession(customer1);
        assertEquals(0, authManager.getActiveSessionCount());
        
        assertThrows(UserNotFoundException.class, () -> {
            authManager.terminateSession(customer1);
        });
    }

    @Test
    public void testFindSessionByUserFound() {
        authManager.createSession(customer1);
        AuthSession foundSession = authManager.findSessionByUser(customer1);
        
        assertNotNull(foundSession);
        assertEquals(customer1, foundSession.getUser());
        assertTrue(foundSession.isActive());
    }

    @Test
    public void testFindSessionByUserNotFound() {
        AuthSession foundSession = authManager.findSessionByUser(customer1);
        assertNull(foundSession);
    }

    @Test
    public void testFindSessionByUserInactiveSession() throws UserNotFoundException {
        authManager.createSession(customer1);
        AuthSession session = authManager.findSessionByUser(customer1);
        session.setActive(false);
        
        AuthSession foundSession = authManager.findSessionByUser(customer1);
        assertNull(foundSession);
    }

    @Test
    public void testFindSessionByUserWithMultipleSessions() {
        authManager.createSession(customer1);
        authManager.createSession(customer1);
        
        AuthSession foundSession = authManager.findSessionByUser(customer1);
        assertNotNull(foundSession);
        assertEquals(customer1, foundSession.getUser());
    }

    @Test
    public void testFindSessionByUserDifferentUser() {
        authManager.createSession(customer1);
        AuthSession foundSession = authManager.findSessionByUser(customer2);
        assertNull(foundSession);
    }

    @Test
    public void testTerminateSessionRemovesFromActiveSessions() throws UserNotFoundException {
        authManager.createSession(customer1);
        authManager.createSession(customer2);
        
        assertEquals(2, authManager.getActiveSessionCount());
        
        authManager.terminateSession(customer1);
        assertEquals(1, authManager.getActiveSessionCount());
        
        AuthSession foundSession = authManager.findSessionByUser(customer2);
        assertNotNull(foundSession);
        assertEquals(customer2, foundSession.getUser());
    }
}

