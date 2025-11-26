package test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import src.movieManagement.movie.Review;

class TestReview {
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
	public void testReview_01() {
		Review review1 = new Review("Excellent cinematography and storytelling.", 9);
		assertEquals("Excellent cinematography and storytelling.", review1.getComments());
	}
	
	@Test
	public void testReview_02() {
		Review review1 = new Review("Excellent cinematography and storytelling.", 9);
		assertEquals(9, review1.getMovieRating());
	}
	
	@Test
	public void testReview_03() {
		Review review1 = new Review("Excellent cinematography and storytelling.", 9);
		assertEquals("Movie Review\nMovie Rating: 9\nComment: Excellent cinematography and storytelling.", review1.toString());
		
	}
	
	@Test
	public void testReview_04() throws Exception{
		setOutput();
		Review review1 = new Review("Excellent cinematography and storytelling.", 11);
		assertEquals("Rating should be 0 to 10", getOutput());
	}

}

