package movieManagement.src.exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("The password you entered is incorrect!\n");
    }
}

