package qa.autotest.framework.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for date/time operations
 */
@Slf4j
public final class DateUtils {
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter ISO_ZULU_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Parse ISO 8601 date string to OffsetDateTime
     */
    public static OffsetDateTime parseIsoDateTime(String dateTimeString) {
        try {
            return OffsetDateTime.parse(dateTimeString, ISO_FORMATTER);
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse with ISO_FORMATTER, trying ISO_ZULU_FORMATTER");
            return parseIsoZuluDateTime(dateTimeString);
        }
    }
    
    /**
     * Parse ISO 8601 date string with Zulu time
     */
    public static OffsetDateTime parseIsoZuluDateTime(String dateTimeString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, ISO_ZULU_FORMATTER);
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
    
    /**
     * Check if date string is in valid ISO 8601 format
     */
    public static boolean isValidIsoDateTime(String dateTimeString) {
        try {
            parseIsoDateTime(dateTimeString);
            return true;
        } catch (DateTimeParseException e) {
            log.debug("Invalid ISO date format: {}", dateTimeString);
            return false;
        }
    }
    
    /**
     * Get current UTC time as ISO string
     */
    public static String getCurrentIsoDateTime() {
        return OffsetDateTime.now(ZoneOffset.UTC).format(ISO_FORMATTER);
    }
    
    /**
     * Check if date is in the past
     */
    public static boolean isInPast(String dateTimeString) {
        OffsetDateTime dateTime = parseIsoDateTime(dateTimeString);
        return dateTime.isBefore(OffsetDateTime.now(ZoneOffset.UTC));
    }
    
    /**
     * Check if date is in the future
     */
    public static boolean isInFuture(String dateTimeString) {
        OffsetDateTime dateTime = parseIsoDateTime(dateTimeString);
        return dateTime.isAfter(OffsetDateTime.now(ZoneOffset.UTC));
    }
}
