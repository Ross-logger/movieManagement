package movieManagement.src.users;

public class Membership {
    private MembershipState state;

    public Membership() {
        this.state = new NonMemberState();
    }

    public void setState(MembershipState newState) {
        this.state = newState;
        System.out.println("Membership changed to: " + state.getType());
    }

    public MembershipState getState() {
        return state;
    }

    public String getType() {
        return state.getType();
    }

    public int getMaxRentMovies() {
        return state.getMaxRentMovies();
    }

    public int getRentalDays() {
        return state.getRentalDays();
    }

    public double calculateDiscountedPrice(double originalPrice) {
        return originalPrice * (1 - state.getPurchaseDiscount());
    }
}

