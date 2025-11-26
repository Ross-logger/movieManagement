package movieManagement.src.movie;

import movieManagement.src.users.Customer;

public class SalableMovieDuplicate extends MovieDuplicate {
	private boolean isSold;  
    
    public SalableMovieDuplicate(Movie movie) {
        super(movie);
        this.isSold = false;
    }
   
	public boolean isSold() {
		return isSold;
	}
	
	public void sold(Customer customer) {
		isSold = true;
		
	}
	
}

