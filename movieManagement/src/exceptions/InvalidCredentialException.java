package movieManagement.src.exceptions;

import java.util.ArrayList;

public class InvalidCredentialException extends Exception {
    private static String buildMessage(boolean hasUpper, boolean hasLower, boolean hasNumber, boolean hasSpecial) {
        ArrayList<String> missing = new ArrayList<String>();
        
        if (!hasNumber)
            missing.add("at least one digit");
        if (!hasUpper)
            missing.add("at least one uppercase letter");
        if (!hasLower)
            missing.add("at least one lowercase letter");
        if (!hasSpecial)
            missing.add("at least one special character");
        
        int count = missing.size();
        
        if (count == 0)
            return "Password must be at least 8 characters long\n";
        
        String result = "Password must contain ";
        if (count == 1)
            result += missing.get(0);
        else if (count == 2)
            result += missing.get(0) + " and " + missing.get(1);
        else if (count == 3)
            result += missing.get(0) + ", " + missing.get(1) + ", and " + missing.get(2);
        else
            result += missing.get(0) + ", " + missing.get(1) + ", " + missing.get(2) + ", and " + missing.get(3);
        
        return result + "\n";
    }

    public InvalidCredentialException() {
        super("Password must be at least 8 characters long\n");
    }

    public InvalidCredentialException(boolean hasUpper, boolean hasLower, boolean hasNumber, boolean hasSpecial) {
        super(buildMessage(hasUpper, hasLower, hasNumber, hasSpecial));
    }
}

