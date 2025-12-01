package src.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("The specified user could not be found.\n");
    }
}

