package users;

import src.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NonMemberStateTest {
    private NonMemberState state;

    @BeforeEach
    public void setUp() {
        state = new NonMemberState();
    }

    @Test
    public void testNonMemberStateProperties() {
        assertEquals(2, state.getMaxRentMovies());
        assertEquals(7, state.getRentalDays());
        assertEquals(0.0, state.getPurchaseDiscount(), 0.001);
        assertEquals("NON_MEMBER", state.getType());
    }

    @Test
    public void testNonMemberNoDiscount() {
        double originalPrice = 150.0;
        double expectedPrice = 150.0;
        double actualDiscount = state.getPurchaseDiscount();
        assertEquals(expectedPrice, originalPrice * (1 - actualDiscount), 0.001);
    }

    @Test
    public void testNonMemberBasicBenefits() {
        GoldMemberState gold = new GoldMemberState();
        PlatinumMemberState platinum = new PlatinumMemberState();
        
        assertTrue(state.getMaxRentMovies() < gold.getMaxRentMovies());
        assertTrue(state.getMaxRentMovies() < platinum.getMaxRentMovies());
        assertTrue(state.getRentalDays() < gold.getRentalDays());
        assertTrue(state.getRentalDays() < platinum.getRentalDays());
        assertTrue(state.getPurchaseDiscount() < gold.getPurchaseDiscount());
        assertTrue(state.getPurchaseDiscount() < platinum.getPurchaseDiscount());
    }
}

