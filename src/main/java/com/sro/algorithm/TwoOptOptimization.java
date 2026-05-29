package com.sro.algorithm;

import com.sro.model.Delivery;
import com.sro.model.Location;
import java.util.*;

/**
 * 2-Opt Optimization Algorithm
 * Time Complexity: O(n^2) per iteration
 * Improves route by swapping edges to reduce total distance
 */
public class TwoOptOptimization {

    /**
     * Optimize route using 2-Opt swap
     * Returns improved route and new total distance
     */
    public OptimizationResult optimize(List<Delivery> route, Location startLocation, Location endLocation) {
        boolean improved = true;
        double currentDistance = calculateRouteDistance(route, startLocation, endLocation);
        List<Delivery> bestRoute = new ArrayList<>(route);

        int iterations = 0;
        int maxIterations = 1000; // Prevent infinite loops

        while (improved && iterations < maxIterations) {
            improved = false;
            iterations++;

            for (int i = 0; i < bestRoute.size() - 2; i++) {
                for (int j = i + 2; j < bestRoute.size(); j++) {
                    List<Delivery> newRoute = swap(bestRoute, i, j);
                    double newDistance = calculateRouteDistance(newRoute, startLocation, endLocation);

                    if (newDistance < currentDistance) {
                        bestRoute = newRoute;
                        currentDistance = newDistance;
                        improved = true;
                        break;
                    }
                }
                if (improved) break;
            }
        }

        return new OptimizationResult(bestRoute, currentDistance, iterations);
    }

    /**
     * Perform 2-Opt swap on route
     */
    private List<Delivery> swap(List<Delivery> route, int i, int j) {
        List<Delivery> newRoute = new ArrayList<>(route);
        
        // Reverse the segment between i+1 and j
        Collections.reverse(newRoute.subList(i + 1, j + 1));
        
        return newRoute;
    }

    /**
     * Calculate total distance of a route
     */
    private double calculateRouteDistance(List<Delivery> route, Location start, Location end) {
        if (route.isEmpty()) return 0;

        double totalDistance = start.distanceTo(route.get(0).getDeliveryLocation());

        for (int i = 0; i < route.size() - 1; i++) {
            totalDistance += route.get(i).getDeliveryLocation()
                    .distanceTo(route.get(i + 1).getDeliveryLocation());
        }

        if (end != null) {
            totalDistance += route.get(route.size() - 1).getDeliveryLocation().distanceTo(end);
        }

        return totalDistance;
    }

    /**
     * Result class for 2-Opt optimization
     */
    public static class OptimizationResult {
        public List<Delivery> optimizedRoute;
        public double totalDistance;
        public int iterations;

        public OptimizationResult(List<Delivery> optimizedRoute, double totalDistance, int iterations) {
            this.optimizedRoute = optimizedRoute;
            this.totalDistance = totalDistance;
            this.iterations = iterations;
        }
    }
}
