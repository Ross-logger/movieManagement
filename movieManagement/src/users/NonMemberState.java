package movieManagement.src.users;

public class NonMemberState implements MembershipState {

    @Override
    public int getMaxRentMovies() {
        return 2;
    }

    @Override
    public int getRentalDays() {
        return 7;
    }

    @Override
    public double getPurchaseDiscount() {
        return 0.0;
    }

    @Override
    public String getType() {
        return "NON_MEMBER";
    }
}

