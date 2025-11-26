package movieManagement.src.users;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import movieManagement.src.movie.Movie;
import movieManagement.src.movie.Review;
import movieManagement.src.authentication.CredentialsCheck;

public class Customer extends User {
    private ArrayList<Movie> rentedMovies;
    private ArrayList<Movie> purchasedMovies;
    private Set<Review> reviews;
    private Membership membership;

    public Customer(String username, CredentialsCheck credential) {
        super(username, credential);
        this.rentedMovies = new ArrayList<>();
        this.purchasedMovies = new ArrayList<>();
        this.reviews = new HashSet<>();
        this.membership = new Membership();
    }
    
    public Customer(String username) {
        super(username, new CredentialsCheck("defaultPass123!"));
        this.rentedMovies = new ArrayList<>();
        this.purchasedMovies = new ArrayList<>();
        this.reviews = new HashSet<>();
        this.membership = new Membership();
    }

    public Customer(String username, Integer id) {
        super(username, id);
        this.rentedMovies = new ArrayList<>();
        this.purchasedMovies = new ArrayList<>();
        this.reviews = new HashSet<>();
        this.membership = new Membership();
    }

    public ArrayList<Movie> getRentedMovies() {
        return rentedMovies;
    }

    public ArrayList<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void addRentedMovie(Movie movie) {
        rentedMovies.add(movie);
    }

    public void removeRentedMovie(Movie movie) {
        rentedMovies.remove(movie);
    }

    public void addPurchasedMovie(Movie movie) {
        purchasedMovies.add(movie);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(MembershipState newState) {
        membership.setState(newState);
    }
}

