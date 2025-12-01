package users;

import src.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlatinumMemberStateTest {
    private PlatinumMemberState state;

    @BeforeEach
    public void setUp() {
        state = new PlatinumMemberState();
    }

    @Test
    public void testPlatinumMemberStateProperties() {
        assertEquals(8, state.getMaxRentMovies());
        assertEquals(21, state.getRentalDays());
        assertEquals(0.15, state.getPurchaseDiscount(), 0.001);
        assertEquals("PLATINUM", state.getType());
    }

    @Test
    public void testPlatinumMemberDiscount() {
        double originalPrice = 200.0;
        double expectedPrice = 170.0;
        double actualDiscount = state.getPurchaseDiscount();
        assertEquals(expectedPrice, originalPrice * (1 - actualDiscount), 0.001);
    }

    @Test
    public void testPlatinumMemberHighestBenefits() {
        NonMemberState nonMember = new NonMemberState();
        GoldMemberState gold = new GoldMemberState();
        
        assertTrue(state.getMaxRentMovies() > gold.getMaxRentMovies());
        assertTrue(state.getMaxRentMovies() > nonMember.getMaxRentMovies());
        assertTrue(state.getRentalDays() > gold.getRentalDays());
        assertTrue(state.getRentalDays() > nonMember.getRentalDays());
        assertTrue(state.getPurchaseDiscount() > gold.getPurchaseDiscount());
        assertTrue(state.getPurchaseDiscount() > nonMember.getPurchaseDiscount());
    }
}

