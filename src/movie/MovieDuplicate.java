package src.movie;

import java.util.UUID;

public abstract class MovieDuplicate {
    private final String copyId; 
    private Movie movie;

    public MovieDuplicate(Movie movie) {
        this.copyId = UUID.randomUUID().toString();
        this.movie = movie;
    }

    public String getCopyId() {
        return copyId;
    }
}

