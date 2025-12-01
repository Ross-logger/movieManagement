package movieManagement.test.payment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movieManagement.src.payment.FPSRefundProcessor;
import movieManagement.src.payment.RefundProcessor;

public class FPSRefundProcessorTest {

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
    public void testFPSRefundProcessorInitialization() {
        FPSRefundProcessor processor = new FPSRefundProcessor();
        assertNotNull(processor);
        assertTrue(processor instanceof RefundProcessor);
    }

    @Test
    public void testExecuteRefundWithZeroAmount() {
        FPSRefundProcessor processor = new FPSRefundProcessor();
        processor.executeRefund(0.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $0.00"));
    }

    @Test
    public void testExecuteRefundWithPositiveAmount() {
        FPSRefundProcessor processor = new FPSRefundProcessor();
        processor.executeRefund(300.50);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $300.50"));
    }

    @Test
    public void testExecuteRefundWithLargeAmount() {
        FPSRefundProcessor processor = new FPSRefundProcessor();
        processor.executeRefund(7500.00);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $7500.00"));
    }

    @Test
    public void testExecuteRefundWithDecimalAmount() {
        FPSRefundProcessor processor = new FPSRefundProcessor();
        processor.executeRefund(125.33);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $125.33"));
    }

    @Test
    public void testExecuteRefundWithSmallAmount() {
        FPSRefundProcessor processor = new FPSRefundProcessor();
        processor.executeRefund(0.25);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $0.25"));
    }

    @Test
    public void testExecuteRefundMultipleTimes() {
        FPSRefundProcessor processor = new FPSRefundProcessor();
        processor.executeRefund(50.0);
        processor.executeRefund(100.0);
        processor.executeRefund(150.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $50.00"));
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $100.00"));
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $150.00"));
    }

    @Test
    public void testExecuteRefundWithNegativeAmount() {
        FPSRefundProcessor processor = new FPSRefundProcessor();
        processor.executeRefund(-75.0);
        String output = outputStream.toString();
        assertTrue(output.contains("Processing FPS (Fast Payment System) refund: $-75.00"));
    }
}

