package com.sro.algorithm;

import com.sro.model.Delivery;
import com.sro.model.Vehicle;
import com.sro.model.Route;
import com.sro.model.Location;
import java.util.*;

/**
 * Vehicle Routing Problem (VRP) Solver
 * Combines multiple algorithms for complete route optimization
 * Main orchestrator for the optimization system
 */
public class VehicleRoutingProblem {
    private GreedyVehicleAssignment vehicleAssignment;
    private NearestNeighborHeuristic nearestNeighbor;
    private TwoOptOptimization twoOpt;

    public VehicleRoutingProblem() {
        this.vehicleAssignment = new GreedyVehicleAssignment();
        this.nearestNeighbor = new NearestNeighborHeuristic();
        this.twoOpt = new TwoOptOptimization();
    }

    /**
     * Solve VRP - main optimization method
     * Returns optimized routes for all vehicles
     */
    public VRPSolution solve(List<Delivery> deliveries, List<Vehicle> vehicles, Location warehouse) {
        long startTime = System.currentTimeMillis();

        // Step 1: Assign deliveries to vehicles
        Map<Vehicle, List<Delivery>> assignment = vehicleAssignment.assignDeliveries(deliveries, vehicles);

        // Step 2: Build initial routes for each vehicle
        Map<Vehicle, Route> routes = new HashMap<>();
        double totalDistance = 0;
        double totalCost = 0;

        int routeCounter = 1;
        for (Map.Entry<Vehicle, List<Delivery>> entry : assignment.entrySet()) {
            Vehicle vehicle = entry.getKey();
            List<Delivery> assignedDeliveries = entry.getValue();

            if (assignedDeliveries.isEmpty()) {
                continue;
            }

            // Build route using Nearest Neighbor
            List<Delivery> orderedRoute = nearestNeighbor.buildRoute(warehouse, assignedDeliveries);

            // Optimize route using 2-Opt
            TwoOptOptimization.OptimizationResult optimizationResult = 
                twoOpt.optimize(orderedRoute, warehouse, warehouse);

            // Create Route object
            Route route = new Route("R" + routeCounter++, vehicle);
            for (Delivery delivery : optimizationResult.optimizedRoute) {
                route.addDelivery(delivery);
            }

            route.setTotalDistance(optimizationResult.totalDistance);
            route.setTotalCost(optimizationResult.totalDistance * vehicle.getFuelCostPerKm());
            route.setTotalTime(estimateRouteTime(optimizationResult.totalDistance));

            routes.put(vehicle, route);
            totalDistance += route.getTotalDistance();
            totalCost += route.getTotalCost();
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        return new VRPSolution(
                new ArrayList<>(routes.values()),
                totalDistance,
                totalCost,
                vehicleAssignment.calculateAssignmentEfficiency(assignment),
                executionTime
        );
    }

    /**
     * Estimate route time based on distance
     * Assumes average speed of 40 km/h and 5 minutes per delivery
     */
    private double estimateRouteTime(double distance) {
        double travelTime = (distance / 40) * 60; // Convert to minutes
        int deliveryCount = (int) Math.ceil(distance / 10); // Rough estimate
        double deliveryTime = deliveryCount * 5; // 5 minutes per delivery
        return travelTime + deliveryTime;
    }

    /**
     * VRP Solution class
     */
    public static class VRPSolution {
        public List<Route> routes;
        public double totalDistance;
        public double totalCost;
        public double capacityUtilization;
        public long executionTimeMs;

        public VRPSolution(List<Route> routes, double totalDistance, double totalCost,
                          double capacityUtilization, long executionTimeMs) {
            this.routes = routes;
            this.totalDistance = totalDistance;
            this.totalCost = totalCost;
            this.capacityUtilization = capacityUtilization;
            this.executionTimeMs = executionTimeMs;
        }

        @Override
        public String toString() {
            return "VRPSolution{" +
                    "routes=" + routes.size() +
                    ", totalDistance=" + String.format("%.2f", totalDistance) + "km" +
                    ", totalCost=₹" + String.format("%.2f", totalCost) +
                    ", utilization=" + String.format("%.2f", capacityUtilization) + "%" +
                    ", time=" + executionTimeMs + "ms" +
                    '}';
        }
    }
}
