package movieManagement.test.payment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.payment.CreditCardProcessor;
import movieManagement.src.payment.CreditCardRefundProcessor;
import movieManagement.src.payment.FPSProcessor;
import movieManagement.src.payment.FPSRefundProcessor;
import movieManagement.src.payment.PaymentTransaction;

public class PaymentTransactionTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() throws Exception {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        // Reset transaction registry before each test
        Field registryField = PaymentTransaction.class.getDeclaredField("transactionRegistry");
        registryField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Boolean> registry = (java.util.Map<String, Boolean>) registryField.get(null);
        registry.clear();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    private String getOutput() {
        return outputStream.toString();
    }

    @Test
    public void testPaymentTransactionInitialization() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-001", 
            100.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        assertNotNull(transaction);
        assertEquals("TXN-001", transaction.getTransactionId());
        assertEquals(100.0, transaction.getAmount());
        assertFalse(transaction.isPaymentCompleted());
        assertFalse(transaction.isRefundCompleted());
    }

    @Test
    public void testPaymentTransactionWithZeroAmount() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-002", 
            0.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        assertEquals(0.0, transaction.getAmount());
    }

    @Test
    public void testPaymentTransactionWithLargeAmount() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-003", 
            9999.99, 
            new FPSProcessor(), 
            new FPSRefundProcessor()
        );
        assertEquals(9999.99, transaction.getAmount());
    }

    @Test
    public void testCompletePayment() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-004", 
            150.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        
        outputStream.reset();
        transaction.completePayment();
        
        assertTrue(transaction.isPaymentCompleted());
        assertFalse(transaction.isRefundCompleted());
        String output = getOutput();
        assertTrue(output.contains("Processing credit card payment: $150.00"));
        assertTrue(output.contains("Payment transaction TXN-004 completed successfully."));
    }

    @Test
    public void testCompletePaymentWithFPS() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-005", 
            250.0, 
            new FPSProcessor(), 
            new FPSRefundProcessor()
        );
        
        outputStream.reset();
        transaction.completePayment();
        
        assertTrue(transaction.isPaymentCompleted());
        String output = getOutput();
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $250.00"));
        assertTrue(output.contains("Payment transaction TXN-005 completed successfully."));
    }

    @Test
    public void testCompleteRefundAfterPayment() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-006", 
            200.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        
        transaction.completePayment();
        outputStream.reset();
        transaction.completeRefund();
        
        assertTrue(transaction.isPaymentCompleted());
        assertTrue(transaction.isRefundCompleted());
        String output = getOutput();
        assertTrue(output.contains("Processing credit card refund: $200.00"));
        assertTrue(output.contains("Refund for transaction TXN-006 completed successfully."));
    }

    @Test
    public void testCompleteRefundWithoutPayment() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-007", 
            100.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        
        outputStream.reset();
        transaction.completeRefund();
        
        assertFalse(transaction.isPaymentCompleted());
        assertFalse(transaction.isRefundCompleted());
        String output = getOutput();
        assertTrue(output.contains("Transaction TXN-007 not found or payment was not completed."));
    }

    @Test
    public void testCompleteRefundAlreadyRefunded() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-008", 
            300.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        
        transaction.completePayment();
        transaction.completeRefund();
        outputStream.reset();
        transaction.completeRefund();
        
        assertTrue(transaction.isPaymentCompleted());
        assertTrue(transaction.isRefundCompleted());
        String output = getOutput();
        assertTrue(output.contains("This transaction has already been refunded."));
    }

    @Test
    public void testCompleteRefundWithFPS() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-009", 
            400.0, 
            new FPSProcessor(), 
            new FPSRefundProcessor()
        );
        
        transaction.completePayment();
        outputStream.reset();
        transaction.completeRefund();
        
        assertTrue(transaction.isRefundCompleted());
        String output = getOutput();
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $400.00"));
        assertTrue(output.contains("Refund for transaction TXN-009 completed successfully."));
    }

    @Test
    public void testGetTransactionId() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-010", 
            50.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        assertEquals("TXN-010", transaction.getTransactionId());
    }

    @Test
    public void testGetAmount() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-011", 
            75.5, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        assertEquals(75.5, transaction.getAmount());
    }

    @Test
    public void testIsPaymentCompletedInitiallyFalse() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-012", 
            100.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        assertFalse(transaction.isPaymentCompleted());
    }

    @Test
    public void testIsRefundCompletedInitiallyFalse() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-013", 
            100.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        assertFalse(transaction.isRefundCompleted());
    }

    @Test
    public void testGenerateTransactionId() {
        String id1 = PaymentTransaction.generateTransactionId();
        String id2 = PaymentTransaction.generateTransactionId();
        
        assertNotNull(id1);
        assertNotNull(id2);
        assertTrue(id1.startsWith("TXN-"));
        assertTrue(id2.startsWith("TXN-"));
        assertNotEquals(id1, id2);
    }

    @Test
    public void testGenerateTransactionIdMultipleTimes() {
        String[] ids = new String[10];
        PaymentTransaction[] transactions = new PaymentTransaction[10];
        
        // Generate IDs and create transactions to ensure registry size increments
        for (int i = 0; i < 10; i++) {
            ids[i] = PaymentTransaction.generateTransactionId();
            assertNotNull(ids[i]);
            assertTrue(ids[i].startsWith("TXN-"));
            
            // Create transaction to increment registry size for next iteration
            transactions[i] = new PaymentTransaction(
                ids[i],
                10.0 + i,
                new CreditCardProcessor(),
                new CreditCardRefundProcessor()
            );
            transactions[i].completePayment();
        }
        
        // All IDs should be unique
        for (int i = 0; i < 10; i++) {
            for (int j = i + 1; j < 10; j++) {
                assertNotEquals(ids[i], ids[j], 
                    "Transaction IDs should be unique. Found duplicate: " + ids[i]);
            }
        }
    }

    @Test
    public void testCompleteRefundWithNullInRegistry() throws Exception {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-014", 
            100.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        
        // Manually set registry entry to null
        Field registryField = PaymentTransaction.class.getDeclaredField("transactionRegistry");
        registryField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Boolean> registry = (java.util.Map<String, Boolean>) registryField.get(null);
        registry.put("TXN-014", null);
        
        outputStream.reset();
        transaction.completeRefund();
        
        String output = getOutput();
        assertTrue(output.contains("Transaction TXN-014 not found or payment was not completed."));
    }

    @Test
    public void testCompleteRefundWithFalseInRegistry() throws Exception {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-015", 
            100.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        
        // Manually set registry entry to false
        Field registryField = PaymentTransaction.class.getDeclaredField("transactionRegistry");
        registryField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Boolean> registry = (java.util.Map<String, Boolean>) registryField.get(null);
        registry.put("TXN-015", false);
        
        outputStream.reset();
        transaction.completeRefund();
        
        String output = getOutput();
        assertTrue(output.contains("Transaction TXN-015 not found or payment was not completed."));
    }

    @Test
    public void testMultipleTransactions() {
        PaymentTransaction transaction1 = new PaymentTransaction(
            "TXN-016", 
            100.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        PaymentTransaction transaction2 = new PaymentTransaction(
            "TXN-017", 
            200.0, 
            new FPSProcessor(), 
            new FPSRefundProcessor()
        );
        
        transaction1.completePayment();
        transaction2.completePayment();
        
        assertTrue(transaction1.isPaymentCompleted());
        assertTrue(transaction2.isPaymentCompleted());
        
        outputStream.reset();
        transaction1.completeRefund();
        assertTrue(transaction1.isRefundCompleted());
        assertFalse(transaction2.isRefundCompleted());
    }

    @Test
    public void testCompletePaymentMultipleTimes() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-018", 
            100.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        
        transaction.completePayment();
        outputStream.reset();
        transaction.completePayment();
        
        // Should still be completed
        assertTrue(transaction.isPaymentCompleted());
        String output = getOutput();
        assertTrue(output.contains("Processing credit card payment: $100.00"));
        assertTrue(output.contains("Payment transaction TXN-018 completed successfully."));
    }

    @Test
    public void testTransactionWithNegativeAmount() {
        PaymentTransaction transaction = new PaymentTransaction(
            "TXN-019", 
            -50.0, 
            new CreditCardProcessor(), 
            new CreditCardRefundProcessor()
        );
        
        assertEquals(-50.0, transaction.getAmount());
        transaction.completePayment();
        assertTrue(transaction.isPaymentCompleted());
    }
}

