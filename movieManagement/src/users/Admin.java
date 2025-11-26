package movieManagement.src.users;

import java.util.List;
import movieManagement.src.movie.Movie;
import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.data.PredefinedAdmins;

public class Admin extends User {
    private static Admin instance;

    public Admin(String username, CredentialsCheck credential) {
        super(username, credential);
    }

    public static synchronized Admin getInstance() {
        if (instance == null) {
            instance = PredefinedAdmins.loadAdmin();
        }
        return instance;
    }

    public void addMovie(Movie movie, List<Movie> movieCatalog) {
        if (!movieCatalog.contains(movie)) {
            movieCatalog.add(movie);
            System.out.println("Movie added to catalog: " + movie.getDisplayText());
        } else {
            System.out.println("Movie already exists in catalog: " + movie.getDisplayText());
        }
    }

    public void removeMovie(Movie movie, List<Movie> movieCatalog) {
        if (movieCatalog.contains(movie)) {
            movieCatalog.remove(movie);
            System.out.println("Movie removed from catalog: " + movie.getDisplayText());
        } else {
            System.out.println("Movie not found in catalog: " + movie.getDisplayText());
        }
    }
}
