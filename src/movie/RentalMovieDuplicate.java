package src.movie;


import java.util.ArrayList;
import java.util.List;

import src.users.Customer;

public class RentalMovieDuplicate extends MovieDuplicate {
    private boolean isRented; 
    private Customer customer;
    private final List<RentalRecord> rentalHistory; 

    public RentalMovieDuplicate(Movie movie) {
        super(movie);
        this.isRented = false; 
        this.customer = null; 
        this.rentalHistory = new ArrayList<>();
    }

    
    public boolean isRented() {
        return isRented;
    }

	public void rent(Customer customer) {
		if (isRented) {
            System.out.print("This copy is already rented out");
        }else {
        	this.isRented = true;
    		this.customer = customer;
        }
	}

	public void returnMovie() {
		this.isRented = false;
		this.customer = null;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void addRentalRecord(RentalRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Rental record cannot be null");
        }
        rentalHistory.add(record);
    }
	
	public void displayCopyDetails() {
        System.out.println("Rental Copy ID: " + getCopyId());
    }


}

