package com.api.framework.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility methods for generating booking-related dates.
 */
public class DateUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtils() {}

    public static String today() {
        return LocalDate.now().format(FORMATTER);
    }

    public static String futureDate(int daysFromNow) {
        return LocalDate.now().plusDays(daysFromNow).format(FORMATTER);
    }

    public static String pastDate(int daysAgo) {
        return LocalDate.now().minusDays(daysAgo).format(FORMATTER);
    }
}