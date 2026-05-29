package com.sro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Route Model - represents an optimized delivery route for a vehicle
 */
@Entity
@Table(name = "routes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String routeId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "route_deliveries",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "delivery_id")
    )
    private List<Delivery> deliveries = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "route_waypoints",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<Location> waypoints = new ArrayList<>();
    
    @Column(nullable = false)
    private double totalDistance = 0; // km
    
    @Column(nullable = false)
    private double totalTime = 0; // minutes
    
    @Column(nullable = false)
    private double totalCost = 0; // rupees
    
    @Enumerated(EnumType.STRING)
    private RouteStatus status = RouteStatus.PLANNED;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "estimated_end_time")
    private LocalDateTime estimatedEndTime;
    
    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;
    
    @Column(nullable = false)
    private int sequenceNumber = 0;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    public Route(String routeId, Vehicle vehicle) {
        this.routeId = routeId;
        this.vehicle = vehicle;
        this.status = RouteStatus.PLANNED;
    }
    
    /**
     * Add delivery to route
     */
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        // Add both pickup and delivery locations to waypoints
        if (!waypoints.contains(delivery.getPickupLocation())) {
            waypoints.add(delivery.getPickupLocation());
        }
        if (!waypoints.contains(delivery.getDeliveryLocation())) {
            waypoints.add(delivery.getDeliveryLocation());
        }
    }
    
    /**
     * Add location to route waypoints
     */
    public void addWaypoint(Location location) {
        if (!waypoints.contains(location)) {
            waypoints.add(location);
        }
    }
    
    /**
     * Calculate total weight of all deliveries
     */
    public double getTotalWeight() {
        return deliveries.stream().mapToDouble(Delivery::getWeight).sum();
    }
    
    /**
     * Calculate total volume of all deliveries
     */
    public double getTotalVolume() {
        return deliveries.stream().mapToDouble(Delivery::getVolume).sum();
    }
    
    /**
     * Start the route
     */
    public void startRoute() {
        this.status = RouteStatus.IN_PROGRESS;
        this.startTime = LocalDateTime.now();
    }
    
    /**
     * Complete the route
     */
    public void completeRoute() {
        this.status = RouteStatus.COMPLETED;
        this.actualEndTime = LocalDateTime.now();
    }
    
    /**
     * Get average cost per delivery
     */
    public double getAverageCostPerDelivery() {
        if (deliveries.isEmpty()) return 0;
        return totalCost / deliveries.size();
    }
    
    /**
     * Get average distance per delivery
     */
    public double getAverageDistancePerDelivery() {
        if (deliveries.isEmpty()) return 0;
        return totalDistance / deliveries.size();
    }
    
    @Override
    public String toString() {
        return "Route{" +
                "routeId='" + routeId + '\'' +
                ", vehicle='" + vehicle.getVehicleId() + '\'' +
                ", deliveries=" + deliveries.size() +
                ", distance=" + totalDistance + "km" +
                ", cost=₹" + totalCost +
                ", status=" + status +
                '}';
    }
}
