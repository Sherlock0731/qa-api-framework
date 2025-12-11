package qa.autotest.framework.config;

import org.aeonbits.owner.Config;

/**
 * Configuration interface for test environment properties
 * Uses Owner library for configuration management
 * 
 * Configuration priority (highest to lowest):
 * 1. System properties (-Dkey=value)
 * 2. Environment variables
 * 3. Environment-specific properties (local.properties, ci.properties, etc.)
 * 4. Default properties (default.properties)
 * 
 * All default values are defined in src/main/resources/config/default.properties
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:config/${env}.properties",
        "classpath:config/default.properties"
})
public interface TestConfig extends Config {
    
    /**
     * Environment name (local, dev, staging, prod)
     */
    @Key("env")
    String environment();
    
    /**
     * Base URL for ReqRes API
     */
    @Key("reqres.base.url")
    String reqresBaseUrl();
    
    /**
     * Base URL for Beeceptor API
     */
    @Key("beeceptor.base.url")
    String beeceptorBaseUrl();
    
    /**
     * API Key for ReqRes
     */
    @Key("reqres.api.key")
    String reqresApiKey();
    
    /**
     * Test user email
     */
    @Key("test.user.email")
    String testUserEmail();
    
    /**
     * Test user password
     */
    @Key("test.user.password")
    String testUserPassword();
    
    /**
     * Test user first name
     */
    @Key("test.user.firstname")
    String testUserFirstName();
    
    /**
     * Test user last name
     */
    @Key("test.user.lastname")
    String testUserLastName();
    
    /**
     * Test user ID
     */
    @Key("test.user.id")
    Integer testUserId();
    
    /**
     * Request timeout in milliseconds
     */
    @Key("request.timeout")
    Integer requestTimeout();
    
    /**
     * Number of retry attempts for failed requests
     */
    @Key("retry.attempts")
    Integer retryAttempts();
    
    /**
     * Thread count for parallel execution
     */
    @Key("thread.count")
    Integer threadCount();
    
    /**
     * Enable detailed logging
     */
    @Key("logging.detailed")
    Boolean detailedLogging();
}
