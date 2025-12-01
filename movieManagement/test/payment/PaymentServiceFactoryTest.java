package movieManagement.test.payment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import movieManagement.src.payment.CreditCardProcessor;
import movieManagement.src.payment.CreditCardRefundProcessor;
import movieManagement.src.payment.FPSProcessor;
import movieManagement.src.payment.FPSRefundProcessor;
import movieManagement.src.payment.PaymentProcessor;
import movieManagement.src.payment.PaymentServiceFactory;
import movieManagement.src.payment.PaymentTransaction;
import movieManagement.src.payment.RefundProcessor;

public class PaymentServiceFactoryTest {

    @Test
    public void testCreatePaymentProcessorWithCreditCard() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("creditcard");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithCredit() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("credit");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithCreditCardUpperCase() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("CREDITCARD");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithCreditUpperCase() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("CREDIT");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithCreditCardMixedCase() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("CreditCard");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithCreditMixedCase() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("Credit");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithFPS() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("fps");
        assertNotNull(processor);
        assertTrue(processor instanceof FPSProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithFPSUpperCase() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("FPS");
        assertNotNull(processor);
        assertTrue(processor instanceof FPSProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithFPSMixedCase() {
        PaymentProcessor processor = PaymentServiceFactory.createPaymentProcessor("Fps");
        assertNotNull(processor);
        assertTrue(processor instanceof FPSProcessor);
    }

    @Test
    public void testCreatePaymentProcessorWithInvalidType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PaymentServiceFactory.createPaymentProcessor("invalid");
        });
        assertTrue(exception.getMessage().contains("Unsupported payment method: invalid"));
    }

    @Test
    public void testCreatePaymentProcessorWithEmptyString() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PaymentServiceFactory.createPaymentProcessor("");
        });
        assertTrue(exception.getMessage().contains("Unsupported payment method:"));
    }

    @Test
    public void testCreatePaymentProcessorWithNull() {
        assertThrows(NullPointerException.class, () -> {
            PaymentServiceFactory.createPaymentProcessor(null);
        });
    }

    @Test
    public void testCreateRefundProcessorWithCreditCard() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("creditcard");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithCredit() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("credit");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithCreditCardUpperCase() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("CREDITCARD");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithCreditUpperCase() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("CREDIT");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithCreditCardMixedCase() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("CreditCard");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithCreditMixedCase() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("Credit");
        assertNotNull(processor);
        assertTrue(processor instanceof CreditCardRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithFPS() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("fps");
        assertNotNull(processor);
        assertTrue(processor instanceof FPSRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithFPSUpperCase() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("FPS");
        assertNotNull(processor);
        assertTrue(processor instanceof FPSRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithFPSMixedCase() {
        RefundProcessor processor = PaymentServiceFactory.createRefundProcessor("Fps");
        assertNotNull(processor);
        assertTrue(processor instanceof FPSRefundProcessor);
    }

    @Test
    public void testCreateRefundProcessorWithInvalidType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PaymentServiceFactory.createRefundProcessor("invalid");
        });
        assertTrue(exception.getMessage().contains("Unsupported refund method: invalid"));
    }

    @Test
    public void testCreateRefundProcessorWithEmptyString() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PaymentServiceFactory.createRefundProcessor("");
        });
        assertTrue(exception.getMessage().contains("Unsupported refund method:"));
    }

    @Test
    public void testCreateRefundProcessorWithNull() {
        assertThrows(NullPointerException.class, () -> {
            PaymentServiceFactory.createRefundProcessor(null);
        });
    }

    @Test
    public void testCreatePaymentTransactionWithCreditCard() {
        PaymentTransaction transaction = PaymentServiceFactory.createPaymentTransaction("TXN-001", 100.0, "creditcard");
        assertNotNull(transaction);
        assertEquals("TXN-001", transaction.getTransactionId());
        assertEquals(100.0, transaction.getAmount());
    }

    @Test
    public void testCreatePaymentTransactionWithCredit() {
        PaymentTransaction transaction = PaymentServiceFactory.createPaymentTransaction("TXN-002", 200.0, "credit");
        assertNotNull(transaction);
        assertEquals("TXN-002", transaction.getTransactionId());
        assertEquals(200.0, transaction.getAmount());
    }

    @Test
    public void testCreatePaymentTransactionWithFPS() {
        PaymentTransaction transaction = PaymentServiceFactory.createPaymentTransaction("TXN-003", 300.0, "fps");
        assertNotNull(transaction);
        assertEquals("TXN-003", transaction.getTransactionId());
        assertEquals(300.0, transaction.getAmount());
    }

    @Test
    public void testCreatePaymentTransactionWithInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            PaymentServiceFactory.createPaymentTransaction("TXN-004", 100.0, "invalid");
        });
    }

    @Test
    public void testCreatePaymentTransactionWithZeroAmount() {
        PaymentTransaction transaction = PaymentServiceFactory.createPaymentTransaction("TXN-005", 0.0, "creditcard");
        assertNotNull(transaction);
        assertEquals(0.0, transaction.getAmount());
    }

    @Test
    public void testCreatePaymentTransactionWithLargeAmount() {
        PaymentTransaction transaction = PaymentServiceFactory.createPaymentTransaction("TXN-006", 9999.99, "fps");
        assertNotNull(transaction);
        assertEquals(9999.99, transaction.getAmount());
    }

    @Test
    public void testCreatePaymentTransactionWithNegativeAmount() {
        PaymentTransaction transaction = PaymentServiceFactory.createPaymentTransaction("TXN-007", -50.0, "creditcard");
        assertNotNull(transaction);
        assertEquals(-50.0, transaction.getAmount());
    }
}

