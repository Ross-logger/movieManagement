package movieManagement.test.authentication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.src.exceptions.InvalidCredentialException;

public class CredentialsCheckTest {

    private CredentialsCheck validCredential;

    @BeforeEach
    public void setUp() {
        validCredential = new CredentialsCheck("ValidPass123!");
    }

    @Test
    public void testConstructor() {
        CredentialsCheck cred = new CredentialsCheck("Test123!");
        assertNotNull(cred);
        assertEquals("Test123!", cred.getValue());
    }

    @Test
    public void testGetValue() {
        String password = "MyPassword123!";
        CredentialsCheck cred = new CredentialsCheck(password);
        assertEquals(password, cred.getValue());
    }

    @Test
    public void testGetLength() {
        CredentialsCheck cred = new CredentialsCheck("Test123!");
        assertEquals(8, CredentialsCheck.getLength(cred));
        
        CredentialsCheck longCred = new CredentialsCheck("VeryLongPassword123!");
        assertEquals(20, CredentialsCheck.getLength(longCred));
        
        CredentialsCheck shortCred = new CredentialsCheck("Short1!");
        assertEquals(7, CredentialsCheck.getLength(shortCred));
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(validCredential.equals(validCredential));
    }

    @Test
    public void testEqualsSameValue() {
        CredentialsCheck cred1 = new CredentialsCheck("Test123!");
        CredentialsCheck cred2 = new CredentialsCheck("Test123!");
        assertTrue(cred1.equals(cred2));
    }

    @Test
    public void testEqualsDifferentValue() {
        CredentialsCheck cred1 = new CredentialsCheck("Test123!");
        CredentialsCheck cred2 = new CredentialsCheck("Different123!");
        assertFalse(cred1.equals(cred2));
    }

    @Test
    public void testEqualsNull() {
        // equals() will throw NullPointerException when comparing with null
        assertThrows(NullPointerException.class, () -> {
            validCredential.equals(null);
        });
    }

    @Test
    public void testValidateCredentialValid() {
        assertDoesNotThrow(() -> {
            CredentialsCheck.validateCredential(validCredential);
        });
    }

    @Test
    public void testValidateCredentialTooShort() {
        CredentialsCheck shortCred = new CredentialsCheck("Short1!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(shortCred);
        });
        assertTrue(exception.getMessage().contains("at least 8 characters"));
    }

    @Test
    public void testValidateCredentialNoUppercase() {
        CredentialsCheck cred = new CredentialsCheck("lowercase123!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("uppercase"));
    }

    @Test
    public void testValidateCredentialNoLowercase() {
        CredentialsCheck cred = new CredentialsCheck("UPPERCASE123!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("lowercase"));
    }

    @Test
    public void testValidateCredentialNoDigit() {
        CredentialsCheck cred = new CredentialsCheck("NoDigitPass!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("digit"));
    }

    @Test
    public void testValidateCredentialNoSpecialChar() {
        CredentialsCheck cred = new CredentialsCheck("NoSpecial123");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("special character"));
    }

    @Test
    public void testValidateCredentialMissingTwoRequirements() {
        CredentialsCheck cred = new CredentialsCheck("lowercase123");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("uppercase") && exception.getMessage().contains("special character"));
    }

    @Test
    public void testValidateCredentialMissingThreeRequirements() {
        CredentialsCheck cred = new CredentialsCheck("lowercase!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        String message = exception.getMessage();
        assertTrue(message.contains("uppercase") && 
                   message.contains("digit"));
        // Note: special character is present, so it won't be in the missing list
    }

    @Test
    public void testValidateCredentialMissingAllRequirements() {
        CredentialsCheck cred = new CredentialsCheck("lowercase");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("uppercase") && 
                   exception.getMessage().contains("digit") &&
                   exception.getMessage().contains("special character"));
    }

    @Test
    public void testValidateCredentialOnlyUppercase() {
        CredentialsCheck cred = new CredentialsCheck("UPPERCASE!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("lowercase") && exception.getMessage().contains("digit"));
    }

    @Test
    public void testValidateCredentialOnlyLowercase() {
        CredentialsCheck cred = new CredentialsCheck("lowercase!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("uppercase") && exception.getMessage().contains("digit"));
    }

    @Test
    public void testValidateCredentialOnlyDigits() {
        CredentialsCheck cred = new CredentialsCheck("12345678!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("uppercase") && exception.getMessage().contains("lowercase"));
    }

    @Test
    public void testValidateCredentialOnlySpecialChars() {
        CredentialsCheck cred = new CredentialsCheck("!!!!!!!!");
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class, () -> {
            CredentialsCheck.validateCredential(cred);
        });
        assertTrue(exception.getMessage().contains("uppercase") && 
                   exception.getMessage().contains("lowercase") &&
                   exception.getMessage().contains("digit"));
    }

    @Test
    public void testValidateCredentialExactMinimumLength() {
        CredentialsCheck cred = new CredentialsCheck("Test123!");
        assertDoesNotThrow(() -> {
            CredentialsCheck.validateCredential(cred);
        });
    }

    @Test
    public void testValidateCredentialWithVariousSpecialChars() {
        String[] specialChars = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+"};
        for (String special : specialChars) {
            CredentialsCheck cred = new CredentialsCheck("Test123" + special);
            assertDoesNotThrow(() -> {
                CredentialsCheck.validateCredential(cred);
            }, "Should accept special char: " + special);
        }
    }

    @Test
    public void testValidateCredentialComplexValidPassword() {
        CredentialsCheck cred = new CredentialsCheck("ComplexP@ssw0rd!");
        assertDoesNotThrow(() -> {
            CredentialsCheck.validateCredential(cred);
        });
    }

    @Test
    public void testValidateCredentialWithSpaces() {
        CredentialsCheck cred = new CredentialsCheck("Test 123!");
        assertDoesNotThrow(() -> {
            CredentialsCheck.validateCredential(cred);
        });
    }

    @Test
    public void testValidateCredentialWithUnicode() {
        CredentialsCheck cred = new CredentialsCheck("Test123é!");
        assertDoesNotThrow(() -> {
            CredentialsCheck.validateCredential(cred);
        });
    }
}

