package src.exceptions;

public class UsernameNotFoundException extends Exception {
    public UsernameNotFoundException() {
        super("The username you entered does not exist!\n");
    }
}

