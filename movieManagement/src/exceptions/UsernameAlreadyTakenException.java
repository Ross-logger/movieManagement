package movieManagement.src.exceptions;

public class UsernameAlreadyTakenException extends Exception {
    public UsernameAlreadyTakenException() {
        super("This username is already registered. Please choose a different one.\n");
    }
}

