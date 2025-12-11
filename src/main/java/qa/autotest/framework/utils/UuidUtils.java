package qa.autotest.framework.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for UUID operations
 */
@Slf4j
public final class UuidUtils {
    
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );
    
    private UuidUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Validate UUID format using regex
     */
    public static boolean isValidUuid(String uuidString) {
        if (uuidString == null || uuidString.isEmpty()) {
            return false;
        }
        return UUID_PATTERN.matcher(uuidString).matches();
    }
    
    /**
     * Parse string to UUID
     */
    public static UUID parseUuid(String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", uuidString);
            throw e;
        }
    }
    
    /**
     * Generate random UUID
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
