package uk.gov.hmcts.reform.em.test.retry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class RetryExtensionTest {

    @RegisterExtension
    static RetryExtension retryExtension = new RetryExtension(3);

    private int numberOfRetries = 0;

    private int totalRetries = 0;

    @BeforeEach
    void setUp() {
        numberOfRetries = 0;
        totalRetries = retryExtension.getTotalRetries();
    }

    @Test
    void shouldRetryThreeTimesWhenTestCaseFails() {

        // Increment the retry counter
        numberOfRetries++;
        System.out.println("hello");
        // This assertion will fail, triggering retries
        if (numberOfRetries < 3) {
            fail("Simulating test failure");
        }

        // Verify that the test retried 3 times
        assertEquals(3, numberOfRetries);

        // Verify total retries using getTotalRetries method
        assertEquals(totalRetries + 2, retryExtension.getTotalRetries());
    }

    @Test
    void shouldNotRetryWhenTestCasePasses() {
        final int expectedStatusCode = 201;
        final int actualStatusCode = 201;

        // Verify total retries before test
        int initialTotalRetries = retryExtension.getTotalRetries();

        // This assertion will pass, no retries needed
        assertEquals(expectedStatusCode, actualStatusCode);

        // Verify total retries remain unchanged
        assertEquals(initialTotalRetries, retryExtension.getTotalRetries());
    }

    @Test
    void shouldThrowExceptionWhenRetryCountExceedsMaxRetries() {
        Executable executable = () -> new RetryExtension(31);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Retry count should not exceed 30", exception.getMessage());
    }
}