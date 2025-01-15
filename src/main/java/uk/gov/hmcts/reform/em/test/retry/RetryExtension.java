package uk.gov.hmcts.reform.em.test.retry;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.opentest4j.TestAbortedException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

@Slf4j
public class RetryExtension implements TestExecutionExceptionHandler, TestInstancePostProcessor {

    private static final int MAX_RETRIES = 30;
    private static final AtomicInteger totalRetries = new AtomicInteger(0);
    private final int retryCount;

    public RetryExtension(int retryCount) {
        if (retryCount > MAX_RETRIES) {
            throw new IllegalArgumentException("Retry count should not exceed " + MAX_RETRIES);
        }
        this.retryCount = retryCount;
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        // No-op
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        int failCount = 0;
        Throwable caughtThrowable = null;

        for (int i = 0; i < retryCount; i++) {
            try {
                if (i == 0) {
                    throw throwable;
                }
                context.getRequiredTestMethod().invoke(context.getRequiredTestInstance());
                return;
            }
            catch (TestAbortedException testAbortedException) {
                caughtThrowable = testAbortedException;
                break;
            }
            catch (Throwable t) {
                caughtThrowable = t;
                failCount++;
                log.error("- Retry #{} failed - {}", (i + 1), stackTraceAsString(t));
                if (totalRetries.get() >= MAX_RETRIES) {
                    log.warn("Maximum retry limit across test suite exceeded (max. {}): not retrying failed test.",
                        MAX_RETRIES);
                    break;
                }
                totalRetries.incrementAndGet();
                sleep(5000);
            }
        }
        log.error("{}: giving up after {} failures.", context.getDisplayName(), failCount);
        assert caughtThrowable != null;
        throw caughtThrowable;
    }

    private static String stackTraceAsString(Throwable throwable) {
        final StringWriter errors = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

    public int getTotalRetries() {
        return totalRetries.get();
    }
}