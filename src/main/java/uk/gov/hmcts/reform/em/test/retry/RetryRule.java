package uk.gov.hmcts.reform.em.test.retry;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.PrintWriter;
import java.io.StringWriter;

import static java.lang.Thread.sleep;

@Slf4j
public final class RetryRule implements TestRule {

    private static final int MAX_RETRIES = 30;
    private static int totalRetries = 0;
    private final int retryCount;

    public RetryRule(final int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new RetryRuleStatement(statement, description);
    }

    private class RetryRuleStatement extends Statement {
        private final Statement base;
        private final Description description;

        public RetryRuleStatement(final Statement base, final Description description) {
            this.base = base;
            this.description = description;
        }

        @Override
        public void evaluate() throws Throwable {
            Throwable caughtThrowable = null;
            int failCount = 0;

            for (int i = 0; i < retryCount; i++) {
                try {
                    base.evaluate();
                    return;
                } catch (Throwable throwable) {
                    caughtThrowable = throwable;
                    failCount++;
                    log.error("- Retry #{} failed - {}", (i + 1), stackTraceAsString(throwable));
                    if (totalRetries >= MAX_RETRIES) {
                        log.warn("Maximum retry limit across test suite exceeded (max. {}): not retrying failed test.", MAX_RETRIES);
                        break;
                    } else {
                        totalRetries++;
                    }

                    sleep(5000);
                }
            }
            log.error("{}: giving up after {} failures.", description.getDisplayName(), failCount);
            assert caughtThrowable != null;
            throw caughtThrowable;
        }
    }

    public static String stackTraceAsString(Throwable throwable) {
        final StringWriter errors = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

    public int getTotalRetries() {
        return totalRetries;
    }

    public int getRetryCount() {
        return retryCount;
    }

}

