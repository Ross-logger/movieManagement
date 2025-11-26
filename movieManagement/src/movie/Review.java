package movieManagement.src.movie;

public class Review {

    private String comments;
    private int rating;

    public Review(String comments, int rating) {
        this.comments = comments;
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

    @Override
    public String toString() {
        return "Movie Review\nMovie Rating: " + rating + "\n" +  "Comment: " + comments;
    }
}

