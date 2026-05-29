package com.sro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Delivery Model - represents a delivery order
 */
@Entity
@Table(name = "deliveries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String deliveryId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pickup_location_id", nullable = false)
    private Location pickupLocation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_location_id", nullable = false)
    private Location deliveryLocation;
    
    @Column(nullable = false)
    private double weight; // kg
    
    @Column(nullable = false)
    private double volume; // cubic meters
    
    @Column(nullable = false)
    private int priority = 3; // 1-5 (5 = highest)
    
    @Embedded
    private TimeWindow timeWindow;
    
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.PENDING;
    
    @Column(length = 50)
    private String customerPhone;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle assignedVehicle;
    
    public Delivery(String deliveryId, Location pickupLocation, Location deliveryLocation, 
                   double weight, double volume, int priority) {
        this.deliveryId = deliveryId;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.weight = weight;
        this.volume = volume;
        this.priority = priority;
        this.status = DeliveryStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Mark delivery as assigned
     */
    public void assignToVehicle(Vehicle vehicle) {
        this.assignedVehicle = vehicle;
        this.status = DeliveryStatus.ASSIGNED;
        this.assignedAt = LocalDateTime.now();
    }
    
    /**
     * Mark delivery as completed
     */
    public void markAsDelivered() {
        this.status = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }
    
    /**
     * Check if delivery fits in a vehicle
     */
    public boolean fitsInVehicle(Vehicle vehicle) {
        return weight <= vehicle.getAvailableCapacity() && 
               volume <= vehicle.getAvailableVolume();
    }
    
    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryId='" + deliveryId + '\'' +
                ", from='" + pickupLocation.getName() + '\'' +
                ", to='" + deliveryLocation.getName() + '\'' +
                ", weight=" + weight + "kg" +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}
