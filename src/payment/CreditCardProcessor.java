package src.payment;

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public void executePayment(double amount) {
        System.out.printf("Processing credit card payment: $%.2f\n", amount);
    }
}

