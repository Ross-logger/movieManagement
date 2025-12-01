package movieManagement.test.payment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.payment.CreditCardProcessor;
import movieManagement.src.payment.PaymentProcessor;

public class CreditCardProcessorTest {

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
    public void testCreditCardProcessorInitialization() {
        CreditCardProcessor processor = new CreditCardProcessor();
        assertNotNull(processor);
        assertTrue(processor instanceof PaymentProcessor);
    }

    @Test
    public void testExecutePaymentWithZeroAmount() {
        CreditCardProcessor processor = new CreditCardProcessor();
        processor.executePayment(0.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card payment: $0.00"));
    }

    @Test
    public void testExecutePaymentWithPositiveAmount() {
        CreditCardProcessor processor = new CreditCardProcessor();
        processor.executePayment(100.50);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card payment: $100.50"));
    }

    @Test
    public void testExecutePaymentWithLargeAmount() {
        CreditCardProcessor processor = new CreditCardProcessor();
        processor.executePayment(9999.99);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card payment: $9999.99"));
    }

    @Test
    public void testExecutePaymentWithDecimalAmount() {
        CreditCardProcessor processor = new CreditCardProcessor();
        processor.executePayment(25.75);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card payment: $25.75"));
    }

    @Test
    public void testExecutePaymentWithSmallAmount() {
        CreditCardProcessor processor = new CreditCardProcessor();
        processor.executePayment(0.01);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card payment: $0.01"));
    }

    @Test
    public void testExecutePaymentMultipleTimes() {
        CreditCardProcessor processor = new CreditCardProcessor();
        processor.executePayment(10.0);
        processor.executePayment(20.0);
        processor.executePayment(30.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card payment: $10.00"));
        assertTrue(output.contains("Processing credit card payment: $20.00"));
        assertTrue(output.contains("Processing credit card payment: $30.00"));
    }

    @Test
    public void testExecutePaymentWithNegativeAmount() {
        CreditCardProcessor processor = new CreditCardProcessor();
        processor.executePayment(-50.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing credit card payment: $-50.00"));
    }
}

