package movieManagement.src.payment;

public class CreditCardRefundProcessor implements RefundProcessor {
    @Override
    public void executeRefund(double amount) {
        System.out.printf("Processing credit card refund: $%.2f\n", amount);
    }
}

