package test;


import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import movieManagement.movie.Movie;
import src.movieManagement.movie.Customer_stub;
import src.movieManagement.movie.RentalMovieDuplicate;
import src.movieManagement.movie.SalableMovieDuplicate;

class TestMovie {
	PrintStream oldPrintStream;
    ByteArrayOutputStream bos;
    
    private void setOutput() throws Exception {
        oldPrintStream = System.out;
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
    }

    private String getOutput() {
        System.setOut(oldPrintStream);
        return bos.toString();
    }
    
	@Test
	void testMovie_01() throws Exception {
		//Add rental movie copy, expected result: "Add one rentable copy of movie\n"
		setOutput();
		Movie movie = new Movie("tt0111161","The Shawshank Redemption","Frank Darabont","Castle Rock Entertainment", "1994-09-23", "Two imprisoned men bond over a number of years", 15);
		RentalMovieDuplicate copy1 = new RentalMovieDuplicate(movie);
		movie.addRentalCopy(copy1);
		assertEquals("Add one rentable copy of movie\n", getOutput());
	}
	
	@Test
	void testMovie_02() throws Exception {
		//Add salable movie copy, expected result: "Add one seleable copy of movie\n"
		setOutput();
		Movie movie = new Movie("tt0111161","The Shawshank Redemption","Frank Darabont","Castle Rock Entertainment", "1994-09-23", "Two imprisoned men bond over a number of years", 15);
		SalableMovieDuplicate copy2 = new SalableMovieDuplicate(movie);
		movie.addSaleableCopy(copy2);
		assertEquals("Add one seleable copy of movie\n", getOutput());
	}
	
	@Test
	void testMovie_03() throws Exception{
		//Lend a movie, expected result: "Lending Successfully for movie\n"
		setOutput();
		Movie movie = new Movie("tt0111161","The Shawshank Redemption","Frank Darabont","Castle Rock Entertainment", "1994-09-23", "Two imprisoned men bond over a number of years", 15);
		Customer_stub customer = new Customer_stub();
		movie.addRentalCopy(new RentalMovieDuplicate(movie));
		getOutput();
		setOutput();
		movie.Lend(customer);
		assertEquals("Lending Successfully for movie\n", getOutput());
	}
	
	@Test
	void testMovie_04() throws Exception {
		//buy a movie, expected result: "Buying Successfully for movie\n"
		setOutput();
		Movie movie = new Movie("tt0111161","The Shawshank Redemption","Frank Darabont","Castle Rock Entertainment", "1994-09-23", "Two imprisoned men bond over a number of years", 15);
		Customer_stub customer = new Customer_stub();
		movie.addSaleableCopy(new SalableMovieDuplicate(movie));
		getOutput();
		setOutput();
		movie.Buy(customer);
		assertEquals("Buying Successfully for movie\n", getOutput());
		
	}
	
	@Test
	void testMovie_05() throws Exception {
		//lend a movie when there is no rental copy of that movie, expected result: "No movie available for lending.\n"
		setOutput();
		Movie movie = new Movie("tt0111161","The Shawshank Redemption","Frank Darabont","Castle Rock Entertainment", "1994-09-23", "Two imprisoned men bond over a number of years", 15);
		Customer_stub customer = new Customer_stub();
		movie.Lend(customer);
		assertEquals("No movie available for lending.\n", getOutput());

		
	}
	
	
	@Test
	void testMovie_06() throws Exception {
		//buy a movie when there is no sale copy of that movie, expected result: "No movie available for selling.\n"
		setOutput();
		Movie movie = new Movie("tt0111161","The Shawshank Redemption","Frank Darabont","Castle Rock Entertainment", "1994-09-23", "Two imprisoned men bond over a number of years", 15);
		Customer_stub customer = new Customer_stub();
		movie.Buy(customer);
		assertEquals("No movie available for selling.\n", getOutput());
		
	}
}

