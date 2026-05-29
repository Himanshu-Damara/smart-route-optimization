package com.sro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Location Model - represents a physical location
 * Used for delivery points, warehouses, and waypoints
 */
@Entity
@Table(name = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String locationId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private double latitude;
    
    @Column(nullable = false)
    private double longitude;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    @Enumerated(EnumType.STRING)
    private LocationType type; // WAREHOUSE, CUSTOMER, WAYPOINT
    
    @Column(length = 50)
    private String phoneNumber;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    public Location(String locationId, String name, double latitude, double longitude) {
        this.locationId = locationId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = LocationType.CUSTOMER;
    }
    
    /**
     * Calculate distance to another location using Haversine formula
     * Returns distance in kilometers
     */
    public double distanceTo(Location other) {
        final int EARTH_RADIUS = 6371; // Radius in km
        
        double latDiff = Math.toRadians(other.latitude - this.latitude);
        double lonDiff = Math.toRadians(other.longitude - this.longitude);
        
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                   Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) *
                   Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
    
    @Override
    public String toString() {
        return "Location{" +
                "locationId='" + locationId + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + latitude +
                ", lon=" + longitude +
                ", type=" + type +
                '}';
    }
}
