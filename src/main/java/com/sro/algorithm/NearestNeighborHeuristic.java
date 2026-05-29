package com.sro.algorithm;

import com.sro.model.Delivery;
import com.sro.model.Vehicle;
import com.sro.model.Location;
import java.util.*;

/**
 * Nearest Neighbor Heuristic for route planning
 * Time Complexity: O(n^2)
 * Greedy approach: always visit nearest unvisited location
 */
public class NearestNeighborHeuristic {

    /**
     * Build route using nearest neighbor approach
     */
    public List<Delivery> buildRoute(Location start, List<Delivery> unvisitedDeliveries) {
        List<Delivery> route = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Location currentLocation = start;

        while (visited.size() < unvisitedDeliveries.size()) {
            // Find nearest unvisited delivery
            int nearestIndex = findNearestDelivery(currentLocation, unvisitedDeliveries, visited);
            
            if (nearestIndex == -1) break;

            visited.add(nearestIndex);
            Delivery delivery = unvisitedDeliveries.get(nearestIndex);
            route.add(delivery);
            currentLocation = delivery.getDeliveryLocation();
        }

        return route;
    }

    /**
     * Find index of nearest delivery location
     */
    private int findNearestDelivery(Location current, List<Delivery> deliveries, Set<Integer> visited) {
        double minDistance = Double.MAX_VALUE;
        int nearestIndex = -1;

        for (int i = 0; i < deliveries.size(); i++) {
            if (!visited.contains(i)) {
                double distance = current.distanceTo(deliveries.get(i).getDeliveryLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestIndex = i;
                }
            }
        }

        return nearestIndex;
    }

    /**
     * Calculate total distance of route
     */
    public double calculateRouteDistance(Location start, List<Delivery> route, Location end) {
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
}
