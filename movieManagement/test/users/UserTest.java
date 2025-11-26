package movieManagement.test.users;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.src.users.User;

public class UserTest {
    private static class TestUser extends User {
        public TestUser(String userId, String username, CredentialsCheck credential) {
            super(username, credential);
            
            try {
                java.lang.reflect.Field userIdField = User.class.getDeclaredField("userId");
                userIdField.setAccessible(true);
                userIdField.set(this, userId);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to set userId via reflection", e);
            }
        }
    }

    private CredentialsCheck credential;
    private TestUser user1;
    private TestUser user2;
    private TestUser user3;

    @BeforeEach
    public void setUp() {
        credential = new CredentialsCheck("SecurePass123!");
        
        user1 = new TestUser("user-001", "Alice", credential);
        user2 = new TestUser("user-001", "Alice", credential);
        user3 = new TestUser("user-002", "Bob", credential);
    }

    @Test
    public void testUserCreationAndGetters() {
        assertNotNull(user1.getUserId());
        assertEquals("Alice", user1.getUsername());
        assertEquals(credential, user1.getCredential());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(user1.equals(user1));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(user1.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(user1.equals("Not a User object"));
    }

    @Test
    public void testEqualsDifferentUserId() {
        assertFalse(user1.equals(user3));
    }

    @Test
    public void testEqualsSameUserId() {
        assertTrue(user1.equals(user2));
    }

    @Test
    public void testHashCodeConsistency() {
        assertEquals(user1.hashCode(), user1.hashCode());
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    public void testToString() {
        String result = user1.toString();
        assertTrue(result.contains("user-001"));
        assertTrue(result.contains("Alice"));
        assertTrue(result.startsWith("User ["));
    }

    @Test
    public void testUserWithIntegerId() {
        CredentialsCheck cred = new CredentialsCheck("Pass123!");
        User userWithId = new TestUser("999", "TestUser", cred);
        assertNotNull(userWithId.getUserId());
        assertNotNull(userWithId.getUsername());
    }
}

