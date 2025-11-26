package movieManagement.src.users;

public class PlatinumMemberState implements MembershipState {

    @Override
    public int getMaxRentMovies() {
        return 8;
    }

    @Override
    public int getRentalDays() {
        return 21;
    }

    @Override
    public double getPurchaseDiscount() {
        return 0.15;
    }

    @Override
    public String getType() {
        return "PLATINUM";
    }
}

