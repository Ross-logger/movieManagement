package movieManagement.src.movie;

public class Review_stub {

    private String comments;
    private int rating;

    public Review_stub(String comment, int rate) {
        this.comments = comment;
        if (rate < 0 || rate > 10) {
            System.out.println("Rating should be 0 to 10");
            return;
        }
        else{
            this.rating = rate;
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
        return "Movie Review\n" + "Movie Rating:" + rating + "\n" +  "Comment=" + comments;
    }
}

