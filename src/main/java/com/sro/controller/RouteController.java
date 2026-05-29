package com.sro.controller;

import com.sro.model.Route;
import com.sro.algorithm.VehicleRoutingProblem;
import com.sro.service.RouteOptimizationService;
import com.sro.dto.RouteOptimizationRequest;
import com.sro.dto.RouteOptimizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Route Optimization endpoints
 */
@RestController
@RequestMapping("/routes")
@CrossOrigin(origins = "*")
public class RouteController {
    
    @Autowired
    private RouteOptimizationService routeOptimizationService;
    
    /**
     * POST /routes/optimize - Optimize routes for pending deliveries
     */
    @PostMapping("/optimize")
    public ResponseEntity<RouteOptimizationResponse> optimizeRoutes(
            @RequestParam String warehouseLocationId) {
        try {
            VehicleRoutingProblem.VRPSolution solution = 
                routeOptimizationService.optimizeRoutes(warehouseLocationId);
            
            RouteOptimizationResponse response = new RouteOptimizationResponse();
            response.setSuccess(true);
            response.setMessage("Routes optimized successfully");
            response.setTotalDistance(solution.totalDistance);
            response.setTotalCost(solution.totalCost);
            response.setCapacityUtilization(solution.capacityUtilization);
            response.setExecutionTimeMs(solution.executionTimeMs);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            RouteOptimizationResponse response = new RouteOptimizationResponse();
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * GET /routes - Get all routes
     */
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeOptimizationService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }
    
    /**
     * GET /routes/{id} - Get route by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        Optional<Route> route = routeOptimizationService.getRouteById(id);
        return route.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * GET /routes/active - Get all active routes
     */
    @GetMapping("/status/active")
    public ResponseEntity<List<Route>> getActiveRoutes() {
        List<Route> routes = routeOptimizationService.getActiveRoutes();
        return ResponseEntity.ok(routes);
    }
    
    /**
     * PUT /routes/{id}/start - Start route
     */
    @PutMapping("/{id}/start")
    public ResponseEntity<Route> startRoute(@PathVariable Long id) {
        Route route = routeOptimizationService.startRoute(id);
        return route != null ? ResponseEntity.ok(route) : ResponseEntity.notFound().build();
    }
    
    /**
     * PUT /routes/{id}/complete - Complete route
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Route> completeRoute(@PathVariable Long id) {
        Route route = routeOptimizationService.completeRoute(id);
        return route != null ? ResponseEntity.ok(route) : ResponseEntity.notFound().build();
    }
    
    /**
     * GET /routes/metrics - Get route metrics
     */
    @GetMapping("/metrics/all")
    public ResponseEntity<RouteOptimizationService.RouteMetrics> getMetrics() {
        RouteOptimizationService.RouteMetrics metrics = routeOptimizationService.calculateTotalMetrics();
        return ResponseEntity.ok(metrics);
    }
}
