package com.sro.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for time-related operations in route optimization
 */
public class TimeUtils {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private TimeUtils() {}

    /**
     * Add minutes to a LocalTime
     */
    public static LocalTime addMinutes(LocalTime time, double minutes) {
        return time.plusMinutes((long) minutes);
    }

    /**
     * Check if a time falls within a time window [start, end]
     */
    public static boolean isWithinWindow(LocalTime time, LocalTime windowStart, LocalTime windowEnd) {
        return !time.isBefore(windowStart) && !time.isAfter(windowEnd);
    }

    /**
     * Format time as HH:mm string
     */
    public static String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    /**
     * Calculate time difference in minutes between two LocalTimes
     */
    public static double differenceInMinutes(LocalTime start, LocalTime end) {
        return java.time.Duration.between(start, end).toMinutes();
    }

    /**
     * Get default business start time
     */
    public static LocalTime businessStart() {
        return LocalTime.of(Constants.BUSINESS_START_HOUR, 0);
    }

    /**
     * Get default business end time
     */
    public static LocalTime businessEnd() {
        return LocalTime.of(Constants.BUSINESS_END_HOUR, 0);
    }

    /**
     * Convert minutes to "Xh Ym" format
     */
    public static String formatMinutes(double totalMinutes) {
        int hours = (int) (totalMinutes / 60);
        int minutes = (int) (totalMinutes % 60);
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m";
    }
}
