package users;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import src.movie.Movie;
import java.util.List;
import java.util.ArrayList;

import src.users.Admin;

public class AdminTest {

    @Test
    public void testGetInstance() {
        try {
            java.lang.reflect.Field instanceField = Admin.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            fail("Failed to reset Admin.instance");
        }

        Admin admin1 = Admin.getInstance();
        assertNotNull(admin1);

        Admin admin2 = Admin.getInstance();
        assertSame(admin1, admin2);
    }

    @Test
    public void testAddMovie() {
        Admin admin = Admin.getInstance();
        List<Movie> movieCatalog = new ArrayList<>();

        Movie movie = new Movie("tt1234567", "Test Movie", "Test Director", "Test Studio", "2024-01-01", "Test Description", 25);

        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        admin.addMovie(movie, movieCatalog);
        assertTrue(movieCatalog.contains(movie));
        String expectedOutput1 = "Movie added to catalog: " + movie.getDisplayText() + System.lineSeparator();
        assertEquals(expectedOutput1, outContent.toString());

        outContent.reset();

        admin.addMovie(movie, movieCatalog);
        String expectedOutput2 = "Movie already exists in catalog: " + movie.getDisplayText() + System.lineSeparator();
        assertEquals(expectedOutput2, outContent.toString());
    }

    @Test
    public void testRemoveMovie() {
        Admin admin = Admin.getInstance();
        List<Movie> movieCatalog = new ArrayList<>();

        Movie movie = new Movie("tt1234567", "Test Movie", "Test Director", "Test Studio", "2024-01-01", "Test Description", 25);
        movieCatalog.add(movie);

        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        admin.removeMovie(movie, movieCatalog);
        assertFalse(movieCatalog.contains(movie));
        String expectedOutput1 = "Movie removed from catalog: " + movie.getDisplayText() + System.lineSeparator();
        assertEquals(expectedOutput1, outContent.toString());

        outContent.reset();

        admin.removeMovie(movie, movieCatalog);
        String expectedOutput2 = "Movie not found in catalog: " + movie.getDisplayText() + System.lineSeparator();
        assertEquals(expectedOutput2, outContent.toString());
    }

    @Test
    public void testAddMultipleMovies() {
        Admin admin = Admin.getInstance();
        List<Movie> movieCatalog = new ArrayList<>();

        Movie movie1 = new Movie("tt1111111", "Movie 1", "Director 1", "Studio 1", "2024-01-01", "Desc 1", 20);
        Movie movie2 = new Movie("tt2222222", "Movie 2", "Director 2", "Studio 2", "2024-01-02", "Desc 2", 25);

        admin.addMovie(movie1, movieCatalog);
        admin.addMovie(movie2, movieCatalog);

        assertEquals(2, movieCatalog.size());
        assertTrue(movieCatalog.contains(movie1));
        assertTrue(movieCatalog.contains(movie2));
    }
}

