package movieManagement.src.users;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import movieManagement.src.movie.Movie;
import movieManagement.src.movie.Review;
import movieManagement.src.authentication.CredentialsCheck;
import movieManagement.src.users.NonMemberState;
import movieManagement.src.users.GoldMemberState;
import movieManagement.src.users.PlatinumMemberState;
import movieManagement.src.users.MembershipState;

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
        checkMembershipUpgrade();
    }
    
    private void checkMembershipUpgrade() {
        int purchaseCount = purchasedMovies.size();
        MembershipState currentState = membership.getState();
        
        // Upgrade to Gold after 3 purchases
        if (purchaseCount >= 3 && currentState instanceof NonMemberState) {
            membership.setState(new GoldMemberState());
            System.out.println("\n🎉 Congratulations! You've been upgraded to GOLD membership!");
        }
        // Upgrade to Platinum after 10 purchases
        else if (purchaseCount >= 10 && currentState instanceof GoldMemberState) {
            membership.setState(new PlatinumMemberState());
            System.out.println("\n🎉 Congratulations! You've been upgraded to PLATINUM membership!");
        }
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

