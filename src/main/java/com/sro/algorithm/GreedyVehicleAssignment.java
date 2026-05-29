package com.sro.algorithm;

import com.sro.model.Delivery;
import com.sro.model.Vehicle;
import java.util.*;

/**
 * Greedy Vehicle Assignment Algorithm
 * Time Complexity: O(n * m) where n = deliveries, m = vehicles
 * Assigns deliveries to vehicles using greedy approach
 */
public class GreedyVehicleAssignment {

    /**
     * Assign deliveries to vehicles greedily
     * Prioritizes higher priority deliveries first
     */
    public Map<Vehicle, List<Delivery>> assignDeliveries(List<Delivery> deliveries, List<Vehicle> vehicles) {
        Map<Vehicle, List<Delivery>> assignment = new HashMap<>();
        
        // Initialize vehicle lists
        for (Vehicle vehicle : vehicles) {
            assignment.put(vehicle, new ArrayList<>());
        }

        // Sort deliveries by priority (descending)
        List<Delivery> sortedDeliveries = new ArrayList<>(deliveries);
        sortedDeliveries.sort((d1, d2) -> Integer.compare(d2.getPriority(), d1.getPriority()));

        // Assign each delivery to best available vehicle
        for (Delivery delivery : sortedDeliveries) {
            Vehicle bestVehicle = findBestVehicle(delivery, vehicles, assignment);
            
            if (bestVehicle != null) {
                assignment.get(bestVehicle).add(delivery);
                bestVehicle.addDelivery(delivery);
            }
        }

        return assignment;
    }

    /**
     * Find best vehicle for delivery based on capacity and efficiency
     */
    private Vehicle findBestVehicle(Delivery delivery, List<Vehicle> vehicles, 
                                    Map<Vehicle, List<Delivery>> currentAssignment) {
        Vehicle bestVehicle = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Vehicle vehicle : vehicles) {
            // Check if delivery fits
            if (!delivery.fitsInVehicle(vehicle)) {
                continue;
            }

            // Calculate score (prefer vehicles with more available capacity and lower cost)
            double score = vehicle.getAvailableCapacity() - (vehicle.getFuelCostPerKm() * 10);
            
            if (score > bestScore) {
                bestScore = score;
                bestVehicle = vehicle;
            }
        }

        return bestVehicle;
    }

    /**
     * Calculate assignment efficiency
     * Returns percentage of total vehicle capacity utilized
     */
    public double calculateAssignmentEfficiency(Map<Vehicle, List<Delivery>> assignment) {
        double totalCapacityUsed = 0;
        double totalCapacity = 0;

        for (Map.Entry<Vehicle, List<Delivery>> entry : assignment.entrySet()) {
            Vehicle vehicle = entry.getKey();
            List<Delivery> deliveries = entry.getValue();

            double deliveryWeight = deliveries.stream()
                    .mapToDouble(Delivery::getWeight)
                    .sum();
            
            totalCapacityUsed += deliveryWeight;
            totalCapacity += vehicle.getMaxWeightCapacity();
        }

        return totalCapacity > 0 ? (totalCapacityUsed / totalCapacity) * 100 : 0;
    }
}
