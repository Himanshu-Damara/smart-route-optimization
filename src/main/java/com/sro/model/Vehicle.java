package com.sro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Vehicle Model - represents a delivery vehicle
 */
@Entity
@Table(name = "vehicles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String vehicleId;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_location_id")
    private Location currentLocation;
    
    @Column(nullable = false)
    private double maxWeightCapacity; // kg
    
    @Column(nullable = false)
    private double maxVolumeCapacity; // cubic meters
    
    @Column(nullable = false)
    private double currentWeight = 0;
    
    @Column(nullable = false)
    private double currentVolume = 0;
    
    @Column(nullable = false)
    private double fuelCostPerKm; // rupees
    
    @Enumerated(EnumType.STRING)
    private VehicleStatus status = VehicleStatus.IDLE;
    
    @Column(name = "available_from")
    private LocalTime availableFrom = LocalTime.of(6, 0);
    
    @Column(name = "available_until")
    private LocalTime availableUntil = LocalTime.of(22, 0);
    
    @OneToMany(mappedBy = "assignedVehicle", fetch = FetchType.LAZY)
    private List<Delivery> assignedDeliveries = new ArrayList<>();
    
    @Column(nullable = false)
    private double totalDistance = 0; // km
    
    @Column(nullable = false)
    private double totalCost = 0; // rupees
    
    @Column(length = 50)
    private String driverName;
    
    @Column(length = 15)
    private String driverPhone;
    
    @Column(length = 50)
    private String plateNumber;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    public Vehicle(String vehicleId, String name, double maxWeightCapacity, 
                   double maxVolumeCapacity, double fuelCostPerKm, Location currentLocation) {
        this.vehicleId = vehicleId;
        this.name = name;
        this.maxWeightCapacity = maxWeightCapacity;
        this.maxVolumeCapacity = maxVolumeCapacity;
        this.fuelCostPerKm = fuelCostPerKm;
        this.currentLocation = currentLocation;
        this.status = VehicleStatus.IDLE;
        this.currentWeight = 0;
        this.currentVolume = 0;
    }
    
    /**
     * Add delivery to vehicle
     */
    public void addDelivery(Delivery delivery) {
        if (!delivery.fitsInVehicle(this)) {
            throw new IllegalArgumentException("Delivery does not fit in vehicle");
        }
        assignedDeliveries.add(delivery);
        delivery.assignToVehicle(this);
        currentWeight += delivery.getWeight();
        currentVolume += delivery.getVolume();
        
        // Update status
        if (currentWeight >= maxWeightCapacity * 0.9) {
            status = VehicleStatus.FULL;
        }
    }
    
    /**
     * Remove delivery from vehicle
     */
    public void removeDelivery(Delivery delivery) {
        assignedDeliveries.remove(delivery);
        currentWeight -= delivery.getWeight();
        currentVolume -= delivery.getVolume();
        
        // Update status
        if (status == VehicleStatus.FULL) {
            status = VehicleStatus.IDLE;
        }
    }
    
    /**
     * Get available weight capacity
     */
    public double getAvailableCapacity() {
        return maxWeightCapacity - currentWeight;
    }
    
    /**
     * Get available volume capacity
     */
    public double getAvailableVolume() {
        return maxVolumeCapacity - currentVolume;
    }
    
    /**
     * Get weight utilization percentage
     */
    public double getWeightUtilization() {
        return (currentWeight / maxWeightCapacity) * 100;
    }
    
    /**
     * Update distance and cost
     */
    public void updateTravelMetrics(double distance) {
        this.totalDistance += distance;
        this.totalCost += distance * fuelCostPerKm;
    }
    
    /**
     * Check if vehicle is available at given time
     */
    public boolean isAvailableAt(LocalTime time) {
        return !time.isBefore(availableFrom) && !time.isAfter(availableUntil);
    }
    
    /**
     * Clear all deliveries
     */
    public void clearDeliveries() {
        assignedDeliveries.clear();
        currentWeight = 0;
        currentVolume = 0;
        status = VehicleStatus.IDLE;
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + maxWeightCapacity + "kg" +
                ", currentLoad=" + currentWeight + "kg" +
                ", status=" + status +
                ", deliveries=" + assignedDeliveries.size() +
                '}';
    }
}
