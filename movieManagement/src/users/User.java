package movieManagement.src.users;

import java.util.UUID;
import movieManagement.src.authentication.CredentialsCheck;

public abstract class User {
    private final String userId;
    private final String username;
    private CredentialsCheck credential;

    public User(String username, CredentialsCheck credential) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.credential = credential;
    }
    
    public User(String username, Integer id) {
        this.username = username;
        this.userId = String.valueOf(id);
        this.credential = new CredentialsCheck("defaultPass123!");
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public CredentialsCheck getCredential() {
        return credential;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return userId.equals(other.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", username=" + username + "]";
    }
}

