package users;

import src.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GoldMemberStateTest {
    private GoldMemberState state;

    @BeforeEach
    public void setUp() {
        state = new GoldMemberState();
    }

    @Test
    public void testGoldMemberStateProperties() {
        assertEquals(5, state.getMaxRentMovies());
        assertEquals(14, state.getRentalDays());
        assertEquals(0.10, state.getPurchaseDiscount(), 0.001);
        assertEquals("GOLD", state.getType());
    }

    @Test
    public void testGoldMemberDiscount() {
        double originalPrice = 100.0;
        double expectedPrice = 90.0;
        double actualDiscount = state.getPurchaseDiscount();
        assertEquals(expectedPrice, originalPrice * (1 - actualDiscount), 0.001);
    }

    @Test
    public void testGoldMemberRentalCapacity() {
        assertTrue(state.getMaxRentMovies() > 2);
        assertTrue(state.getMaxRentMovies() < 8);
    }

    @Test
    public void testGoldMemberRentalPeriod() {
        assertTrue(state.getRentalDays() > 7);
        assertTrue(state.getRentalDays() < 21);
    }
}

