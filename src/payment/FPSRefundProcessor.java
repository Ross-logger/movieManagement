package src.payment;

public class FPSRefundProcessor implements RefundProcessor {
    @Override
    public void executeRefund(double amount) {
        System.out.printf("Processing FPS (Fast Payment System) refund: $%.2f\n", amount);
    }
}

