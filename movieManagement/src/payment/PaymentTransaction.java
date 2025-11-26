package movieManagement.src.payment;

import java.util.HashMap;
import java.util.Map;

public class PaymentTransaction {
    private double amount;
    private String transactionId;
    private boolean paymentCompleted = false;
    private boolean refundCompleted = false;
    private PaymentProcessor paymentProcessor;
    private RefundProcessor refundProcessor;
    private static Map<String, Boolean> transactionRegistry = new HashMap<String, Boolean>();
    
    public PaymentTransaction(String transactionId, double amount, PaymentProcessor paymentProcessor, RefundProcessor refundProcessor) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentProcessor = paymentProcessor;
        this.refundProcessor = refundProcessor;
    }

    public void completePayment() {
        paymentProcessor.executePayment(amount);
        transactionRegistry.put(transactionId, true);
        paymentCompleted = true;
        System.out.printf("Payment transaction %s completed successfully.\n", transactionId);
    }

    public void completeRefund() {
        if (transactionRegistry.get(transactionId) != null && transactionRegistry.get(transactionId) == true) {
            refundProcessor.executeRefund(amount);
            transactionRegistry.put(transactionId, false);
            refundCompleted = true;
            System.out.printf("Refund for transaction %s completed successfully.\n", transactionId);
        } else if (refundCompleted) {
            System.out.printf("This transaction has already been refunded.\n");
        } else {
            System.out.printf("Transaction %s not found or payment was not completed.\n", transactionId);
        }
    }

    public boolean isPaymentCompleted() {
        return this.paymentCompleted;
    }
    
    public boolean isRefundCompleted() {
        return this.refundCompleted;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public static String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + (transactionRegistry.size() + 1);
    }
}

