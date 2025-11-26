package movieManagement.data;

import java.util.ArrayList;
import java.util.List;
import movieManagement.src.movie.Movie;

public class PredefinedMoviesList {
    
    private static final String[][] MOVIE_DATA = {
        {"tt0111161", "The Shawshank Redemption", "Frank Darabont", "Castle Rock Entertainment", "1994-09-23", "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.", "15"},
        {"tt0068646", "The Godfather", "Francis Ford Coppola", "Paramount Pictures", "1972-03-24", "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.", "20"},
        {"tt0468569", "The Dark Knight", "Christopher Nolan", "Warner Bros.", "2008-07-18", "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.", "18"},
        {"tt0109830", "Forrest Gump", "Robert Zemeckis", "Paramount Pictures", "1994-07-06", "The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75.", "12"},
        {"tt0133093", "The Matrix", "Lana Wachowski, Lilly Wachowski", "Warner Bros.", "1999-03-31", "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.", "16"},
        {"tt0167260", "The Lord of the Rings: The Return of the King", "Peter Jackson", "New Line Cinema", "2003-12-17", "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.", "22"},
        {"tt1375666", "Inception", "Christopher Nolan", "Warner Bros.", "2010-07-16", "A skilled thief is given a chance at redemption if he can successfully pull off an impossible task: inception.", "19"},
        {"tt0816692", "Interstellar", "Christopher Nolan", "Paramount Pictures", "2014-11-07", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", "21"},
        {"tt0110912", "Pulp Fiction", "Quentin Tarantino", "Miramax Films", "1994-10-14", "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.", "17"},
        {"tt0120737", "The Lord of the Rings: The Fellowship of the Ring", "Peter Jackson", "New Line Cinema", "2001-12-19", "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.", "20"}
    };
    
    public static List<Movie> loadMovies() {
        List<Movie> movies = new ArrayList<>();
        
        for (String[] movieData : MOVIE_DATA) {
            String imdbId = movieData[0];
            String title = movieData[1];
            String director = movieData[2];
            String studio = movieData[3];
            String releaseDate = movieData[4];
            String description = movieData[5];
            int price = Integer.parseInt(movieData[6]);
            
            Movie movie = new Movie(imdbId, title, director, studio, releaseDate, description, price);
            movies.add(movie);
        }
        
        return movies;
    }
}

