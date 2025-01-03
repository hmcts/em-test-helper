package uk.gov.hmcts.reform.em.test.retry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class RetryExtensionTest {

    @RegisterExtension
    static RetryExtension retryExtension = new RetryExtension(3);

    private int numberOfRetries = 0;

    @BeforeEach
    void resetRetries() {
        numberOfRetries = 0;
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
        assertEquals(2, retryExtension.getTotalRetries());
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
}