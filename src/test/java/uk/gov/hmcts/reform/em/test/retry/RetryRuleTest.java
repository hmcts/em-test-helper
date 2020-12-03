package uk.gov.hmcts.reform.em.test.retry;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetryRuleTest {

    @Rule
    public RetryRule retryRule = new RetryRule(3);

    int numberOfRetries;

    @Test
    public void shouldRetryThreeTimesWhenTestCaseFailed() {
        final int createdStatusCode = 201;
        final int notFoundStatusCode = 400;

        final int retryCount = retryRule.getRetryCount();
        int totalRetries = retryRule.getTotalRetries();
        int expected = numberOfRetries++;

        assertEquals(3, retryCount);
        assertEquals(expected, totalRetries);

        assertThrows(AssertionError.class, () -> assertEquals(notFoundStatusCode, createdStatusCode));
    }

    @Test
    public void shouldNotRetryThreeTimesWhenTestCaseNotFailed() {
        final int createdStatusCode = 201;

        final int retryCount = retryRule.getRetryCount();
        int totalRetries = retryRule.getTotalRetries();
        int expected = numberOfRetries;

        assertEquals(3, retryCount);
        assertEquals(expected, totalRetries);

        assertEquals(createdStatusCode, createdStatusCode);
    }
}
