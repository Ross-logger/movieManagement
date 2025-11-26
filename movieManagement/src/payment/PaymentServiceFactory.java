package movieManagement.src.payment;

public class PaymentServiceFactory {
    public static PaymentProcessor createPaymentProcessor(String paymentType) {
        switch (paymentType.toLowerCase()) {
            case "creditcard":
            case "credit":
                return new CreditCardProcessor();
            case "fps":
                return new FPSProcessor();
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + paymentType);
        }
    }

    public static RefundProcessor createRefundProcessor(String paymentType) {
        switch (paymentType.toLowerCase()) {
            case "creditcard":
            case "credit":
                return new CreditCardRefundProcessor();
            case "fps":
                return new FPSRefundProcessor();
            default:
                throw new IllegalArgumentException("Unsupported refund method: " + paymentType);
        }
    }

    public static PaymentTransaction createPaymentTransaction(String transactionId, double amount, String paymentType) {
        PaymentProcessor paymentProcessor = createPaymentProcessor(paymentType);
        RefundProcessor refundProcessor = createRefundProcessor(paymentType);
        return new PaymentTransaction(transactionId, amount, paymentProcessor, refundProcessor);
    }
}

