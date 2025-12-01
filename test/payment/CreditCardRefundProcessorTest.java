package payment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.payment.CreditCardRefundProcessor;
import src.payment.RefundProcessor;

public class CreditCardRefundProcessorTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testCreditCardRefundProcessorInitialization() {
        CreditCardRefundProcessor processor = new CreditCardRefundProcessor();
        assertNotNull(processor);
        assertTrue(processor instanceof RefundProcessor);
    }

    @Test
    public void testExecuteRefundWithZeroAmount() {
        CreditCardRefundProcessor processor = new CreditCardRefundProcessor();
        processor.executeRefund(0.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card refund: $0.00"));
    }

    @Test
    public void testExecuteRefundWithPositiveAmount() {
        CreditCardRefundProcessor processor = new CreditCardRefundProcessor();
        processor.executeRefund(150.75);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card refund: $150.75"));
    }

    @Test
    public void testExecuteRefundWithLargeAmount() {
        CreditCardRefundProcessor processor = new CreditCardRefundProcessor();
        processor.executeRefund(5000.00);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card refund: $5000.00"));
    }

    @Test
    public void testExecuteRefundWithDecimalAmount() {
        CreditCardRefundProcessor processor = new CreditCardRefundProcessor();
        processor.executeRefund(99.99);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card refund: $99.99"));
    }

    @Test
    public void testExecuteRefundWithSmallAmount() {
        CreditCardRefundProcessor processor = new CreditCardRefundProcessor();
        processor.executeRefund(0.50);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card refund: $0.50"));
    }

    @Test
    public void testExecuteRefundMultipleTimes() {
        CreditCardRefundProcessor processor = new CreditCardRefundProcessor();
        processor.executeRefund(10.0);
        processor.executeRefund(20.0);
        processor.executeRefund(30.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card refund: $10.00"));
        assertTrue(output.contains("Processing credit card refund: $20.00"));
        assertTrue(output.contains("Processing credit card refund: $30.00"));
    }

    @Test
    public void testExecuteRefundWithNegativeAmount() {
        CreditCardRefundProcessor processor = new CreditCardRefundProcessor();
        processor.executeRefund(-25.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card refund: $-25.00"));
    }
}

