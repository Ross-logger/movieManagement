package src.authentication;

import java.util.ArrayList;
import java.util.Iterator;

import src.users.User;
import src.exceptions.UserNotFoundException;

public class AuthManager {
    private ArrayList<AuthSession> activeSessions;
    private static AuthManager instance = new AuthManager();

    private AuthManager() {
        activeSessions = new ArrayList<AuthSession>();
    }

    public static AuthManager getInstance() {
        return instance;
    }

    public void addSession(AuthSession session) {
        activeSessions.add(session);
    }

    public int getActiveSessionCount() {
        return activeSessions.size();
    }

    public void createSession(User user) {
        AuthSession session = new AuthSession(user);
        addSession(session);
    }

    public void terminateSession(User user) throws UserNotFoundException {
        Iterator<AuthSession> iterator = activeSessions.iterator();
        while (iterator.hasNext()) {
            AuthSession session = iterator.next();
            if (session.getUser().equals(user)) {
                session.setActive(false);
                iterator.remove();
                return;
            }
        }
        
        throw new UserNotFoundException();
    }
    
    public AuthSession findSessionByUser(User user) {
        for (AuthSession session : activeSessions) {
            if (session.getUser().equals(user) && session.isActive()) {
                return session;
            }
        }
        return null;
    }
}

