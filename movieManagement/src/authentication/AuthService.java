package movieManagement.src.authentication;

import java.util.ArrayList;
import movieManagement.src.exceptions.*;
import movieManagement.src.users.*;
import movieManagement.data.PredefinedAdmins;

public class AuthService {
    private static Admin admin = Admin.getInstance();
    private static ArrayList<User> registeredUsers;
    private static AuthManager authManager = AuthManager.getInstance();
    private static AuthService instance = new AuthService();

    private AuthService() {
        AuthService.registeredUsers = new ArrayList<User>();
        // Register all predefined admins
        Admin[] admins = PredefinedAdmins.loadAdmins();
        for (Admin predefinedAdmin : admins) {
            registeredUsers.add(predefinedAdmin);
        }
    }

    public static AuthService getInstance() {
        return instance;
    }

    private static User findUserByUsername(String username) {
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public void registerUser(User user) {
        registeredUsers.add(user);
    }

    public Admin authenticateAdmin(CredentialsCheck credential) throws WrongPasswordException {
        validateAdminCredential(admin, credential);
        return admin;
    }

    private static boolean verifyCredential(User user, String passwordValue) {
        CredentialsCheck providedCredential = new CredentialsCheck(passwordValue);
        CredentialsCheck userCredential = user.getCredential();
        return providedCredential.equals(userCredential);
    }

    private static void validateAdminCredential(User admin, CredentialsCheck credential) throws WrongPasswordException {
        CredentialsCheck adminCredential = admin.getCredential();
        if (!credential.equals(adminCredential))
            throw new WrongPasswordException();
    }

    private static boolean usernameExists(String username) {
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public User signIn(String username, String passwordValue) throws UsernameNotFoundException, WrongPasswordException {
        User user = findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException();
        }

        boolean isValidCredential = verifyCredential(user, passwordValue);

        if (!isValidCredential) {
            throw new WrongPasswordException();
        }

        authManager.createSession(user);
        return user;
    }

    public Admin signInAdmin(String passwordValue) throws WrongPasswordException {
        CredentialsCheck credential = new CredentialsCheck(passwordValue);
        return authenticateAdmin(credential);
    }

    public void signOut(User user) throws UserNotFoundException {
        authManager.terminateSession(user);
    }

    public Customer registerCustomer(String username, String passwordValue) throws InvalidCredentialException, UsernameAlreadyTakenException {
        if (usernameExists(username))
            throw new UsernameAlreadyTakenException();

        CredentialsCheck credential = new CredentialsCheck(passwordValue);
        Customer customer = new Customer(username, credential);

        try {
            CredentialsCheck.validateCredential(credential);
        } catch (InvalidCredentialException e) {
            throw e;
        }

        registeredUsers.add(customer);
        return customer;
    }
}

