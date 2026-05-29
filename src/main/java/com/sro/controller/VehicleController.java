package com.sro.controller;

import com.sro.model.Vehicle;
import com.sro.model.VehicleStatus;
import com.sro.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Vehicle endpoints
 */
@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    /**
     * GET /vehicles - Get all vehicles
     */
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * GET /vehicles/{id} - Get vehicle by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        return vehicle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * GET /vehicles/available - Get all available vehicles
     */
    @GetMapping("/status/available")
    public ResponseEntity<List<Vehicle>> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * POST /vehicles - Create new vehicle
     */
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle created = vehicleService.createVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /vehicles/{id} - Update vehicle
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        Optional<Vehicle> existing = vehicleService.getVehicleById(id);
        if (existing.isPresent()) {
            vehicle.setId(id);
            Vehicle updated = vehicleService.updateVehicle(vehicle);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * DELETE /vehicles/{id} - Delete vehicle
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * GET /vehicles/{id}/utilization - Get vehicle utilization
     */
    @GetMapping("/{id}/utilization")
    public ResponseEntity<Double> getVehicleUtilization(@PathVariable Long id) {
        double utilization = vehicleService.getVehicleUtilization(id);
        return ResponseEntity.ok(utilization);
    }
}
