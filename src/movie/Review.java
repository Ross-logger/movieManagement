package src.movie;

import src.users.Customer;

public class Review {

    private String comments;
    private int rating;
    private Customer customer;

    public Review(String comments, int rating, Customer customer) {
        this.comments = comments;
        this.customer = customer;
        if (rating < 0 || rating > 10) {
            System.out.print("Rating should be 0 to 10");
        }
        else{
            this.rating = rating;
        }
    }

    public String getComments() {
        return comments;
    }

    public int getMovieRating() {
        return rating;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setRating(int rating) {
        if (rating < 0 || rating > 10) {
            System.out.print("Rating should be 0 to 10");
        } else {
            this.rating = rating;
        }
    }

    @Override
    public String toString() {
        return "Movie Review\nMovie Rating: " + rating + "\n" +  "Comment: " + comments;
    }
}

