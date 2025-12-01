package movieManagement.test.payment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.payment.FPSProcessor;
import movieManagement.src.payment.PaymentProcessor;

public class FPSProcessorTest {

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
    public void testFPSProcessorInitialization() {
        FPSProcessor processor = new FPSProcessor();
        assertNotNull(processor);
        assertTrue(processor instanceof PaymentProcessor);
    }

    @Test
    public void testExecutePaymentWithZeroAmount() {
        FPSProcessor processor = new FPSProcessor();
        processor.executePayment(0.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $0.00"));
    }

    @Test
    public void testExecutePaymentWithPositiveAmount() {
        FPSProcessor processor = new FPSProcessor();
        processor.executePayment(200.25);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $200.25"));
    }

    @Test
    public void testExecutePaymentWithLargeAmount() {
        FPSProcessor processor = new FPSProcessor();
        processor.executePayment(10000.00);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $10000.00"));
    }

    @Test
    public void testExecutePaymentWithDecimalAmount() {
        FPSProcessor processor = new FPSProcessor();
        processor.executePayment(75.50);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $75.50"));
    }

    @Test
    public void testExecutePaymentWithSmallAmount() {
        FPSProcessor processor = new FPSProcessor();
        processor.executePayment(0.99);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $0.99"));
    }

    @Test
    public void testExecutePaymentMultipleTimes() {
        FPSProcessor processor = new FPSProcessor();
        processor.executePayment(15.0);
        processor.executePayment(25.0);
        processor.executePayment(35.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $15.00"));
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $25.00"));
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $35.00"));
    }

    @Test
    public void testExecutePaymentWithNegativeAmount() {
        FPSProcessor processor = new FPSProcessor();
        processor.executePayment(-100.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) payment: $-100.00"));
    }
}

