package movieManagement.src.payment;

public class FPSProcessor implements PaymentProcessor {
    @Override
    public void executePayment(double amount) {
        System.out.printf("Processing FPS (Fast Payment System) payment: $%.2f\n", amount);
    }
}

