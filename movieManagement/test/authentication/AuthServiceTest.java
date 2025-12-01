package movieManagement.test.authentication;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.authentication.AuthManager;
import movieManagement.src.authentication.AuthService;
import movieManagement.src.authentication.AuthSession;
import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.src.exceptions.InvalidCredentialException;
import movieManagement.src.exceptions.UserNotFoundException;
import movieManagement.src.exceptions.UsernameAlreadyTakenException;
import movieManagement.src.exceptions.UsernameNotFoundException;
import movieManagement.src.exceptions.WrongPasswordException;
import movieManagement.src.users.Admin;
import movieManagement.src.users.Customer;
import movieManagement.src.users.User;

public class AuthServiceTest {

    private AuthService authService;
    private Customer testCustomer;
    private CredentialsCheck validCredential;

    @BeforeEach
    public void setUp() {
        // Get the AuthService instance
        authService = AuthService.getInstance();
        assertNotNull(authService, "AuthService instance should not be null");
        
        // Clear registered users (except predefined admins)
        try {
            java.lang.reflect.Field usersField = AuthService.class.getDeclaredField("registeredUsers");
            usersField.setAccessible(true);
            ArrayList<?> users = (ArrayList<?>) usersField.get(authService);
            // Keep only predefined admins (first few entries)
            int adminCount = 3; // Based on PredefinedAdmins
            while (users.size() > adminCount) {
                users.remove(adminCount);
            }
        } catch (Exception e) {
            // Continue if reset fails - predefined admins will remain
        }
        
        validCredential = new CredentialsCheck("TestPass123!");
        testCustomer = new Customer("testuser", validCredential);
        
        // Reset AuthManager sessions
        try {
            AuthManager authManager = AuthManager.getInstance();
            java.lang.reflect.Field sessionsField = AuthManager.class.getDeclaredField("activeSessions");
            sessionsField.setAccessible(true);
            ArrayList<?> sessions = (ArrayList<?>) sessionsField.get(authManager);
            sessions.clear();
        } catch (Exception e) {
            // Continue if reset fails
        }
    }

    @Test
    public void testGetInstance() {
        AuthService service1 = AuthService.getInstance();
        AuthService service2 = AuthService.getInstance();
        assertSame(service1, service2);
        assertNotNull(service1);
    }

    @Test
    public void testRegisterUser() {
        int initialCount = getRegisteredUsersCount();
        authService.registerUser(testCustomer);
        assertEquals(initialCount + 1, getRegisteredUsersCount());
    }

    @Test
    public void testRegisterMultipleUsers() {
        int initialCount = getRegisteredUsersCount();
        CredentialsCheck cred2 = new CredentialsCheck("Another123!");
        Customer customer2 = new Customer("user2", cred2);
        
        authService.registerUser(testCustomer);
        authService.registerUser(customer2);
        
        assertEquals(initialCount + 2, getRegisteredUsersCount());
    }

    @Test
    public void testSignInSuccess() throws UsernameNotFoundException, WrongPasswordException {
        authService.registerUser(testCustomer);
        
        User signedInUser = authService.signIn("testuser", "TestPass123!");
        
        assertNotNull(signedInUser);
        assertEquals(testCustomer, signedInUser);
        assertEquals("testuser", signedInUser.getUsername());
        
        // Verify session was created
        AuthManager authManager = AuthManager.getInstance();
        AuthSession session = authManager.findSessionByUser(signedInUser);
        assertNotNull(session);
        assertTrue(session.isActive());
    }

    @Test
    public void testSignInUsernameNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> {
            authService.signIn("nonexistent", "Password123!");
        });
    }

    @Test
    public void testSignInWrongPassword() {
        authService.registerUser(testCustomer);
        
        assertThrows(WrongPasswordException.class, () -> {
            authService.signIn("testuser", "WrongPassword123!");
        });
    }

    @Test
    public void testSignInWithEmptyPassword() {
        authService.registerUser(testCustomer);
        
        assertThrows(WrongPasswordException.class, () -> {
            authService.signIn("testuser", "");
        });
    }

    @Test
    public void testSignInAdminSuccess() throws WrongPasswordException {
        Admin admin = Admin.getInstance();
        CredentialsCheck adminCred = admin.getCredential();
        String adminPassword = adminCred.getValue();
        
        Admin authenticatedAdmin = authService.signInAdmin(adminPassword);
        
        assertNotNull(authenticatedAdmin);
        assertEquals(admin, authenticatedAdmin);
        
        // Note: signInAdmin doesn't create a session, only authenticateAdmin is called
    }

    @Test
    public void testSignInAdminWrongPassword() {
        assertThrows(WrongPasswordException.class, () -> {
            authService.signInAdmin("WrongPassword123!");
        });
    }

    @Test
    public void testAuthenticateAdminSuccess() throws WrongPasswordException {
        Admin admin = Admin.getInstance();
        CredentialsCheck adminCred = admin.getCredential();
        
        Admin authenticatedAdmin = authService.authenticateAdmin(adminCred);
        
        assertNotNull(authenticatedAdmin);
        assertEquals(admin, authenticatedAdmin);
    }

    @Test
    public void testAuthenticateAdminWrongPassword() {
        CredentialsCheck wrongCred = new CredentialsCheck("WrongPassword123!");
        
        assertThrows(WrongPasswordException.class, () -> {
            authService.authenticateAdmin(wrongCred);
        });
    }

    @Test
    public void testSignOutSuccess() throws UserNotFoundException, UsernameNotFoundException, WrongPasswordException {
        authService.registerUser(testCustomer);
        authService.signIn("testuser", "TestPass123!");
        
        authService.signOut(testCustomer);
        
        AuthManager authManager = AuthManager.getInstance();
        AuthSession session = authManager.findSessionByUser(testCustomer);
        assertNull(session);
    }

    @Test
    public void testSignOutUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            authService.signOut(testCustomer);
        });
    }

    @Test
    public void testRegisterCustomerSuccess() throws InvalidCredentialException, UsernameAlreadyTakenException {
        Customer registered = authService.registerCustomer("newuser", "ValidPass123!");
        
        assertNotNull(registered);
        assertEquals("newuser", registered.getUsername());
        assertTrue(getRegisteredUsersCount() > 0);
    }

    @Test
    public void testRegisterCustomerUsernameAlreadyTaken() throws InvalidCredentialException, UsernameAlreadyTakenException {
        authService.registerCustomer("existinguser", "ValidPass123!");
        
        assertThrows(UsernameAlreadyTakenException.class, () -> {
            authService.registerCustomer("existinguser", "AnotherPass123!");
        });
    }

    @Test
    public void testRegisterCustomerInvalidCredentialTooShort() {
        assertThrows(InvalidCredentialException.class, () -> {
            authService.registerCustomer("newuser", "Short1!");
        });
    }

    @Test
    public void testRegisterCustomerInvalidCredentialNoUppercase() {
        assertThrows(InvalidCredentialException.class, () -> {
            authService.registerCustomer("newuser", "lowercase123!");
        });
    }

    @Test
    public void testRegisterCustomerInvalidCredentialNoLowercase() {
        assertThrows(InvalidCredentialException.class, () -> {
            authService.registerCustomer("newuser", "UPPERCASE123!");
        });
    }

    @Test
    public void testRegisterCustomerInvalidCredentialNoDigit() {
        assertThrows(InvalidCredentialException.class, () -> {
            authService.registerCustomer("newuser", "NoDigitPass!");
        });
    }

    @Test
    public void testRegisterCustomerInvalidCredentialNoSpecialChar() {
        assertThrows(InvalidCredentialException.class, () -> {
            authService.registerCustomer("newuser", "NoSpecial123");
        });
    }

    @Test
    public void testRegisterCustomerWithPredefinedAdminUsername() {
        // Predefined admins are registered, so their usernames should be taken
        assertThrows(UsernameAlreadyTakenException.class, () -> {
            authService.registerCustomer("admin", "ValidPass123!");
        });
    }

    @Test
    public void testSignInWithRegisteredCustomer() throws InvalidCredentialException, UsernameAlreadyTakenException, 
                                                          UsernameNotFoundException, WrongPasswordException {
        authService.registerCustomer("registereduser", "ValidPass123!");
        
        User signedInUser = authService.signIn("registereduser", "ValidPass123!");
        
        assertNotNull(signedInUser);
        assertEquals("registereduser", signedInUser.getUsername());
        assertTrue(signedInUser instanceof Customer);
    }

    @Test
    public void testMultipleSignInsSameUser() throws UsernameNotFoundException, WrongPasswordException {
        authService.registerUser(testCustomer);
        
        authService.signIn("testuser", "TestPass123!");
        authService.signIn("testuser", "TestPass123!");
        
        AuthManager authManager = AuthManager.getInstance();
        // Should have multiple sessions
        assertTrue(authManager.getActiveSessionCount() >= 2);
    }

    @Test
    public void testSignInSignOutCycle() throws UsernameNotFoundException, WrongPasswordException, UserNotFoundException {
        authService.registerUser(testCustomer);
        
        authService.signIn("testuser", "TestPass123!");
        AuthManager authManager = AuthManager.getInstance();
        assertNotNull(authManager.findSessionByUser(testCustomer));
        
        authService.signOut(testCustomer);
        assertNull(authManager.findSessionByUser(testCustomer));
        
        // Sign in again
        authService.signIn("testuser", "TestPass123!");
        assertNotNull(authManager.findSessionByUser(testCustomer));
    }

    @Test
    public void testFindUserByUsernameExists() {
        authService.registerUser(testCustomer);
        
        // Use reflection to test private method
        try {
            java.lang.reflect.Method method = AuthService.class.getDeclaredMethod("findUserByUsername", String.class);
            method.setAccessible(true);
            User found = (User) method.invoke(authService, "testuser");
            
            assertNotNull(found);
            assertEquals(testCustomer, found);
        } catch (Exception e) {
            fail("Failed to test findUserByUsername: " + e.getMessage());
        }
    }

    @Test
    public void testFindUserByUsernameNotExists() {
        try {
            java.lang.reflect.Method method = AuthService.class.getDeclaredMethod("findUserByUsername", String.class);
            method.setAccessible(true);
            User found = (User) method.invoke(authService, "nonexistent");
            
            assertNull(found);
        } catch (Exception e) {
            fail("Failed to test findUserByUsername: " + e.getMessage());
        }
    }

    @Test
    public void testUsernameExists() {
        authService.registerUser(testCustomer);
        
        try {
            java.lang.reflect.Method method = AuthService.class.getDeclaredMethod("usernameExists", String.class);
            method.setAccessible(true);
            boolean exists = (Boolean) method.invoke(authService, "testuser");
            
            assertTrue(exists);
            
            boolean notExists = (Boolean) method.invoke(authService, "nonexistent");
            assertFalse(notExists);
        } catch (Exception e) {
            fail("Failed to test usernameExists: " + e.getMessage());
        }
    }

    @Test
    public void testVerifyCredentialSuccess() {
        try {
            java.lang.reflect.Method method = AuthService.class.getDeclaredMethod("verifyCredential", User.class, String.class);
            method.setAccessible(true);
            boolean isValid = (Boolean) method.invoke(authService, testCustomer, "TestPass123!");
            
            assertTrue(isValid);
        } catch (Exception e) {
            fail("Failed to test verifyCredential: " + e.getMessage());
        }
    }

    @Test
    public void testVerifyCredentialFailure() {
        try {
            java.lang.reflect.Method method = AuthService.class.getDeclaredMethod("verifyCredential", User.class, String.class);
            method.setAccessible(true);
            boolean isValid = (Boolean) method.invoke(authService, testCustomer, "WrongPassword123!");
            
            assertFalse(isValid);
        } catch (Exception e) {
            fail("Failed to test verifyCredential: " + e.getMessage());
        }
    }

    @Test
    public void testPredefinedAdminsRegistered() {
        // Predefined admins should be registered during initialization
        assertTrue(getRegisteredUsersCount() >= 1);
    }

    // Helper method to get registered users count
    private int getRegisteredUsersCount() {
        try {
            java.lang.reflect.Field usersField = AuthService.class.getDeclaredField("registeredUsers");
            usersField.setAccessible(true);
            ArrayList<?> users = (ArrayList<?>) usersField.get(authService);
            return users.size();
        } catch (Exception e) {
            return 0;
        }
    }
}

