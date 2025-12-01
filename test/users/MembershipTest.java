package users;

import src.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MembershipTest {
    private Membership membership;

    @BeforeEach
    public void setUp() {
        membership = new Membership();
    }

    @Test
    public void testDefaultMembershipState() {
        assertTrue(membership.getState() instanceof NonMemberState);
        assertEquals("NON_MEMBER", membership.getType());
        assertEquals(2, membership.getMaxRentMovies());
        assertEquals(7, membership.getRentalDays());
        assertEquals(100.0, membership.calculateDiscountedPrice(100.0), 0.001);
    }

    @Test
    public void testGoldMembershipBenefits() {
        membership.setState(new GoldMemberState());
        assertTrue(membership.getState() instanceof GoldMemberState);
        assertEquals("GOLD", membership.getType());
        assertEquals(5, membership.getMaxRentMovies());
        assertEquals(14, membership.getRentalDays());
        assertEquals(90.0, membership.calculateDiscountedPrice(100.0), 0.001);
    }

    @Test
    public void testPlatinumMembershipBenefits() {
        membership.setState(new PlatinumMemberState());
        assertTrue(membership.getState() instanceof PlatinumMemberState);
        assertEquals("PLATINUM", membership.getType());
        assertEquals(8, membership.getMaxRentMovies());
        assertEquals(21, membership.getRentalDays());
        assertEquals(85.0, membership.calculateDiscountedPrice(100.0), 0.001);
    }

    @Test
    public void testMembershipStateChanges() {
        membership.setState(new GoldMemberState());
        assertEquals("GOLD", membership.getType());
        
        membership.setState(new PlatinumMemberState());
        assertEquals("PLATINUM", membership.getType());
        
        membership.setState(new NonMemberState());
        assertEquals("NON_MEMBER", membership.getType());
    }

    @Test
    public void testDiscountCalculation() {
        membership.setState(new NonMemberState());
        assertEquals(50.0, membership.calculateDiscountedPrice(50.0), 0.001);
        
        membership.setState(new GoldMemberState());
        assertEquals(45.0, membership.calculateDiscountedPrice(50.0), 0.001);
        
        membership.setState(new PlatinumMemberState());
        assertEquals(42.5, membership.calculateDiscountedPrice(50.0), 0.001);
    }

    @Test
    public void testRentalDaysForEachMembership() {
        membership.setState(new NonMemberState());
        assertEquals(7, membership.getRentalDays());
        
        membership.setState(new GoldMemberState());
        assertEquals(14, membership.getRentalDays());
        
        membership.setState(new PlatinumMemberState());
        assertEquals(21, membership.getRentalDays());
    }

    @Test
    public void testMaxRentMoviesForEachMembership() {
        membership.setState(new NonMemberState());
        assertEquals(2, membership.getMaxRentMovies());
        
        membership.setState(new GoldMemberState());
        assertEquals(5, membership.getMaxRentMovies());
        
        membership.setState(new PlatinumMemberState());
        assertEquals(8, membership.getMaxRentMovies());
    }
}

