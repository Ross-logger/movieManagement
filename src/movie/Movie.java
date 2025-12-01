package src.movie;

import java.util.ArrayList;
import java.util.List;
import src.users.Customer;

public class Movie {
	private String imdbId;
    private String title;
    private String director;
    private String studio; 
    private String releaseDate; 
    private String movieDescription;
    private int moviePrice;
    private List<RentalMovieDuplicate> rentalCopies; 
    private List<SalableMovieDuplicate> saleCopies; 
    private ArrayList<SalableMovieDuplicate> soldCopies;
    private List<Review> allReviews;

    public Movie(String imdbId, String title, String director, String studio, String releaseDate, String movieDescription, int price) {
        this.imdbId = imdbId;
        this.title = title;
        this.director = director;
        this.studio = studio;
        this.releaseDate = releaseDate;
        this.movieDescription = movieDescription;
        this.moviePrice = price;
        this.rentalCopies = new ArrayList<>();
        this.saleCopies = new ArrayList<>();
        this.soldCopies = new ArrayList<>();
        this.allReviews = new ArrayList<>();
        initializeDefaultCopies();
    }
    
    private void initializeDefaultCopies() {
        // Add 10 rental copies
        for (int i = 0; i < 10; i++) {
            rentalCopies.add(new RentalMovieDuplicate(this));
        }
        // Add 10 sale copies
        for (int i = 0; i < 10; i++) {
            saleCopies.add(new SalableMovieDuplicate(this));
        }
    }
    
    //selling movie
    public boolean isSalable() {
    	if (saleCopies.isEmpty()) {
    		return false;
    	}else {
    		return true;
    	}
    }
    
    public void Buy(Customer customer) {
        if (!isSalable()) {
        	System.out.print("No movie available for selling.\n"); 
        }else{
        	SalableMovieDuplicate sellingCopy = saleCopies.remove(0);
        	sellingCopy.sold(customer);
        	soldCopies.add(sellingCopy);
           	System.out.print("Buying Successfully for movie\n");
        }
    	
    }
    
    public List<SalableMovieDuplicate> getSoldCopies() {
        return new ArrayList<>(soldCopies);
    } 

	//renting movie
    public void Lend(Customer customer) {
    	RentalMovieDuplicate rentingCopy = getAvailableLendingCopy();
        if (rentingCopy == null) { 
            System.out.print("No movie available for lending.\n"); 
        }else{
        	rentingCopy.rent(customer);
           	System.out.print("Lending Successfully for movie\n");
        }

	}
    
    public RentalMovieDuplicate getAvailableLendingCopy() {
    	for (RentalMovieDuplicate copy: this.rentalCopies) {
    		if (!copy.isRented()) {
    			return copy;
    		}
    	}
		return null;
	}
    
    public boolean hasAvailableRentalCopy() {
        return getAvailableLendingCopy() != null;
    }
    
    public RentalMovieDuplicate getRentedCopyByCustomer(Customer customer) {
        for (RentalMovieDuplicate copy: this.rentalCopies) {
            if (copy.isRented() && copy.getCustomer() != null && copy.getCustomer() == customer) {
                return copy;
            }
        }
        return null;
    }
    
    public boolean Return(Customer customer) {
        RentalMovieDuplicate rentedCopy = getRentedCopyByCustomer(customer);
        if (rentedCopy == null) {
            System.out.print("You have not rented this movie.\n");
            return false;
        } else {
            rentedCopy.returnMovie();
            System.out.print("Movie returned successfully.\n");
            return true;
        }
    }
	
    //rental copies add & get
    public String showAvailableRentalCopies() {
    	int availableCount = 0;
    	for (RentalMovieDuplicate rentalCopy: rentalCopies) {
    		if (!rentalCopy.isRented())
    			availableCount++;
    	}
        return availableCount + " available rentable copy";
    }

    public void addRentalCopy(RentalMovieDuplicate copy) {
        rentalCopies.add(copy);
        System.out.print("Add one rentable copy of movie\n");
    }

    //sale copies add & get
    public void addSaleableCopy(SalableMovieDuplicate copy) {
        saleCopies.add(copy);
        System.out.print("Add one seleable copy of movie\n");
    }
    public String showAvailableSellableCopies() {
        return this.saleCopies.size() + " available sellable copy";
    }

    //review add & get
    public boolean addReview(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null.");
        }
        if (review.getCustomer() == null) {
            throw new IllegalArgumentException("Review must have a customer.");
        }
        
        // Check if customer already has a review for this movie
        Review existingReview = getReviewByCustomer(review.getCustomer());
        if (existingReview != null) {
            System.out.print("Review already exists. You can edit your existing review.\n");
            return false;
        }
        
        allReviews.add(review);
        return true;
    }
    
    public Review getReviewByCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        for (Review review : allReviews) {
            if (review.getCustomer() != null && review.getCustomer().equals(customer)) {
                return review;
            }
        }
        return null;
    }
    
    public boolean editReview(Customer customer, String newComments, int newRating) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        
        Review existingReview = getReviewByCustomer(customer);
        if (existingReview == null) {
            System.out.print("You have not reviewed this movie yet.\n");
            return false;
        }
        
        existingReview.setComments(newComments);
        existingReview.setRating(newRating);
        System.out.print("Review updated successfully.\n");
        return true;
    }
    
    public void displayReviews() {
        if (allReviews.isEmpty()) {
            System.out.println("No reviews yet.");
        } else {
            for (Review review : allReviews) {
                System.out.println(review);
            }
        }
    }
    
    public List<Review> getAllReviews() {
        return new ArrayList<>(allReviews);
    }
    
    public int getMoviePrice() {
    	return this.moviePrice;
    }
    
	public String getDisplayText() {
		return title + " by " + director + " IMDB ID: " + imdbId + " Studio: " + studio + " Release Date: " + releaseDate + " Price: " + moviePrice;
	}
}

