package com.sro.util;

import com.sro.model.Location;

/**
 * Utility class for distance calculations
 * Uses Haversine formula for accurate lat/lng distance
 */
public class DistanceUtils {

    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calculate distance between two locations using Haversine formula
     * Returns distance in kilometers
     */
    public static double haversineDistance(Location from, Location to) {
        return haversineDistance(from.getLatitude(), from.getLongitude(),
                to.getLatitude(), to.getLongitude());
    }

    /**
     * Calculate distance between two lat/lng points using Haversine formula
     * Returns distance in kilometers
     */
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Estimate travel time in minutes given distance (km)
     * Assumes average city speed of 35 km/h accounting for traffic
     */
    public static double estimateTravelTimeMinutes(double distanceKm) {
        return (distanceKm / Constants.AVERAGE_SPEED_KMH) * 60;
    }

    /**
     * Calculate fuel cost for given distance and cost per km
     */
    public static double calculateFuelCost(double distanceKm, double costPerKm) {
        return distanceKm * costPerKm;
    }

    /**
     * Build a full distance matrix between all locations
     * Returns matrix[i][j] = distance from location[i] to location[j]
     */
    public static double[][] buildDistanceMatrix(Location[] locations) {
        int n = locations.length;
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (i == j) ? 0 : haversineDistance(locations[i], locations[j]);
            }
        }
        return matrix;
    }
}
