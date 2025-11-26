package movieManagement.src.users;

public class GoldMemberState implements MembershipState {

    @Override
    public int getMaxRentMovies() {
        return 5;
    }

    @Override
    public int getRentalDays() {
        return 14;
    }

    @Override
    public double getPurchaseDiscount() {
        return 0.10;
    }

    @Override
    public String getType() {
        return "GOLD";
    }
}

