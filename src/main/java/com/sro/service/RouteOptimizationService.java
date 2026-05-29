package com.sro.service;

import com.sro.algorithm.*;
import com.sro.model.*;
import com.sro.repository.RouteRepository;
import com.sro.repository.DeliveryRepository;
import com.sro.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for Route Optimization
 * Main orchestrator for the optimization system
 */
@Service
public class RouteOptimizationService {
    
    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private DeliveryRepository deliveryRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private LocationService locationService;
    
    private VehicleRoutingProblem vrpSolver;
    
    public RouteOptimizationService() {
        this.vrpSolver = new VehicleRoutingProblem();
    }
    
    /**
     * Optimize routes for all pending deliveries
     */
    public VehicleRoutingProblem.VRPSolution optimizeRoutes(String warehouseLocationId) {
        // Get all pending deliveries
        List<Delivery> pendingDeliveries = deliveryRepository.findByStatus(DeliveryStatus.PENDING);
        
        if (pendingDeliveries.isEmpty()) {
            throw new IllegalArgumentException("No pending deliveries found");
        }
        
        // Get all available vehicles
        List<Vehicle> availableVehicles = vehicleRepository.findByStatus(VehicleStatus.IDLE);
        
        if (availableVehicles.isEmpty()) {
            throw new IllegalArgumentException("No available vehicles found");
        }
        
        // Get warehouse location
        Optional<Location> warehouse = locationService.getLocationByLocationId(warehouseLocationId);
        if (!warehouse.isPresent()) {
            throw new IllegalArgumentException("Warehouse location not found");
        }
        
        // Solve VRP
        VehicleRoutingProblem.VRPSolution solution = vrpSolver.solve(
            pendingDeliveries, 
            availableVehicles, 
            warehouse.get()
        );
        
        // Save routes to database
        for (Route route : solution.routes) {
            route.setStartTime(LocalDateTime.now());
            routeRepository.save(route);
            
            // Update delivery status
            for (Delivery delivery : route.getDeliveries()) {
                delivery.setStatus(DeliveryStatus.ASSIGNED);
                deliveryRepository.save(delivery);
            }
        }
        
        return solution;
    }
    
    /**
     * Get all routes
     */
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
    
    /**
     * Get route by ID
     */
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }
    
    /**
     * Get route by routeId
     */
    public Optional<Route> getRouteByRouteId(String routeId) {
        return routeRepository.findByRouteId(routeId);
    }
    
    /**
     * Get all active routes
     */
    public List<Route> getActiveRoutes() {
        return routeRepository.findByStatus(RouteStatus.IN_PROGRESS);
    }
    
    /**
     * Start route
     */
    public Route startRoute(Long routeId) {
        Optional<Route> route = routeRepository.findById(routeId);
        if (route.isPresent()) {
            Route r = route.get();
            r.startRoute();
            return routeRepository.save(r);
        }
        return null;
    }
    
    /**
     * Complete route
     */
    public Route completeRoute(Long routeId) {
        Optional<Route> route = routeRepository.findById(routeId);
        if (route.isPresent()) {
            Route r = route.get();
            r.completeRoute();
            
            // Mark all deliveries as delivered
            for (Delivery delivery : r.getDeliveries()) {
                delivery.markAsDelivered();
                deliveryRepository.save(delivery);
            }
            
            return routeRepository.save(r);
        }
        return null;
    }
    
    /**
     * Get routes for vehicle
     */
    public List<Route> getRoutesForVehicle(Long vehicleId) {
        return routeRepository.findByVehicleId(vehicleId);
    }
    
    /**
     * Calculate total metrics for all routes
     */
    public RouteMetrics calculateTotalMetrics() {
        List<Route> routes = routeRepository.findByStatus(RouteStatus.COMPLETED);
        
        double totalDistance = 0;
        double totalCost = 0;
        int totalDeliveries = 0;
        
        for (Route route : routes) {
            totalDistance += route.getTotalDistance();
            totalCost += route.getTotalCost();
            totalDeliveries += route.getDeliveries().size();
        }
        
        return new RouteMetrics(
            routes.size(),
            totalDistance,
            totalCost,
            totalDeliveries,
            routes.isEmpty() ? 0 : totalDistance / routes.size(),
            routes.isEmpty() ? 0 : totalCost / routes.size()
        );
    }
    
    /**
     * RouteMetrics inner class
     */
    public static class RouteMetrics {
        public int totalRoutes;
        public double totalDistance;
        public double totalCost;
        public int totalDeliveries;
        public double averageDistancePerRoute;
        public double averageCostPerRoute;
        
        public RouteMetrics(int totalRoutes, double totalDistance, double totalCost,
                           int totalDeliveries, double avgDistance, double avgCost) {
            this.totalRoutes = totalRoutes;
            this.totalDistance = totalDistance;
            this.totalCost = totalCost;
            this.totalDeliveries = totalDeliveries;
            this.averageDistancePerRoute = avgDistance;
            this.averageCostPerRoute = avgCost;
        }
    }
}
