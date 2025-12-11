package examples;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import qa.autotest.framework.client.BeeceptorClient;
import qa.autotest.framework.client.ReqResClient;
import qa.autotest.framework.config.ConfigFactory;
import qa.autotest.framework.config.TestConfig;

/**
 * Base test class with common setup and teardown
 * Supports parallel execution
 */
@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseTest {

    protected static final TestConfig CONFIG = ConfigFactory.getConfig();
    protected ReqResClient reqResClient;
    protected BeeceptorClient beeceptorClient;

    @BeforeEach
    void setUp() {
        log.info("=== Test Started: {} ===", getClass().getSimpleName());
        log.info("Thread: {}", Thread.currentThread().getName());
        reqResClient = new ReqResClient();
        beeceptorClient = new BeeceptorClient();
    }

    @AfterEach
    void tearDown() {
        log.info("=== Test Finished: {} ===", getClass().getSimpleName());
    }
}
