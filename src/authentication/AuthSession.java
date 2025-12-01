package src.authentication;

import src.users.User;
import java.util.UUID;

public class AuthSession {
    private User user;
    private UUID sessionToken;
    private boolean active;
    private long createdAt;
    
    public AuthSession(User user) {
        this.user = user;
        this.active = true;
        this.sessionToken = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }
    
    public UUID getSessionToken() {
        return sessionToken;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
}

