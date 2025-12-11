package qa.autotest.framework.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Factory for creating and caching configuration instances
 * Thread-safe singleton implementation
 */
public final class ConfigFactory {
    
    private ConfigFactory() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Get cached configuration instance
     * Thread-safe method using ConfigCache
     *
     * @return TestConfig instance
     */
    public static TestConfig getConfig() {
        return ConfigCache.getOrCreate(TestConfig.class);
    }
}
