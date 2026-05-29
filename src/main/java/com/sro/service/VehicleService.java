package com.sro.service;

import com.sro.model.Vehicle;
import com.sro.model.VehicleStatus;
import com.sro.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service for Vehicle operations
 */
@Service
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    /**
     * Get all vehicles
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    /**
     * Get vehicle by ID
     */
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }
    
    /**
     * Get vehicle by vehicleId
     */
    public Optional<Vehicle> getVehicleByVehicleId(String vehicleId) {
        return vehicleRepository.findByVehicleId(vehicleId);
    }
    
    /**
     * Get all available vehicles
     */
    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus(VehicleStatus.IDLE);
    }
    
    /**
     * Create new vehicle
     */
    public Vehicle createVehicle(Vehicle vehicle) {
        vehicle.setStatus(VehicleStatus.IDLE);
        vehicle.setCurrentWeight(0);
        vehicle.setCurrentVolume(0);
        vehicle.setTotalDistance(0);
        vehicle.setTotalCost(0);
        return vehicleRepository.save(vehicle);
    }
    
    /**
     * Update vehicle
     */
    public Vehicle updateVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
    
    /**
     * Update vehicle status
     */
    public Vehicle updateVehicleStatus(Long vehicleId, VehicleStatus status) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isPresent()) {
            Vehicle v = vehicle.get();
            v.setStatus(status);
            return vehicleRepository.save(v);
        }
        return null;
    }
    
    /**
     * Delete vehicle
     */
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
    
    /**
     * Get vehicle utilization percentage
     */
    public double getVehicleUtilization(Long vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        return vehicle.map(Vehicle::getWeightUtilization).orElse(0.0);
    }
    
    /**
     * Get total deliveries assigned to vehicle
     */
    public int getAssignedDeliveryCount(Long vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        return vehicle.map(v -> v.getAssignedDeliveries().size()).orElse(0);
    }
}
