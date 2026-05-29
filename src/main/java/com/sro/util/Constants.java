package com.sro.util;

/**
 * Application-wide constants for Smart Route Optimization System
 */
public final class Constants {

    private Constants() {}

    // Speed and time
    public static final double AVERAGE_SPEED_KMH = 35.0;         // City average speed
    public static final double MINUTES_PER_DELIVERY_STOP = 5.0;   // Time at each delivery stop
    public static final double MINUTES_PER_PICKUP_STOP = 3.0;     // Time at pickup stop

    // Business hours
    public static final int BUSINESS_START_HOUR = 9;
    public static final int BUSINESS_END_HOUR = 21;

    // Optimization thresholds
    public static final double VEHICLE_FULL_THRESHOLD = 0.90;     // 90% = full
    public static final int MAX_DELIVERIES_PER_VEHICLE = 20;
    public static final int TWO_OPT_MAX_ITERATIONS = 1000;

    // Distance
    public static final double EARTH_RADIUS_KM = 6371.0;

    // Default warehouse
    public static final String DEFAULT_WAREHOUSE_ID = "W001";

    // API version
    public static final String API_VERSION = "v1";

    // HTTP messages
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
}
