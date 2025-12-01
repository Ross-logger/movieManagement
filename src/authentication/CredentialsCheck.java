package src.authentication;

import src.exceptions.InvalidCredentialException;

public class CredentialsCheck {
    private String value;
    
    public CredentialsCheck(String value) {
        this.value = value;
    }
    
    public static int getLength(CredentialsCheck credential) {
        return credential.value.length();
    }

    private static boolean containsLowercase(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (c >= 'a' && c <= 'z')
                return true;
        }
        return false;
    }

    private static boolean containsUppercase(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (c >= 'A' && c <= 'Z')
                return true;
        }
        return false;
    }

    private static boolean containsDigit(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (c >= '0' && c <= '9')
                return true;
        }
        return false;
    }

    private static boolean containsSpecialChar(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (!Character.isLetterOrDigit(c))
                return true;
        }
        return false;
    }

    public static void validateCredential(CredentialsCheck credential) throws InvalidCredentialException {
        String credValue = credential.value;

        if (getLength(credential) < 8)
            throw new InvalidCredentialException();

        boolean hasUpper = containsUppercase(credValue);
        boolean hasLower = containsLowercase(credValue);
        boolean hasNumber = containsDigit(credValue);
        boolean hasSpecial = containsSpecialChar(credValue);

        if (!(hasUpper && hasLower && hasNumber && hasSpecial))
            throw new InvalidCredentialException(hasUpper, hasLower, hasNumber, hasSpecial);
    }

    public boolean equals(CredentialsCheck other) {
        return this.value.equals(other.value);
    }
    
    public String getValue() {
        return value;
    }
}

