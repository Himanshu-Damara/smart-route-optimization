package com.sro.service;

import com.sro.model.Location;
import com.sro.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service for Location operations
 */
@Service
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    /**
     * Get all locations
     */
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    /**
     * Get location by ID
     */
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }
    
    /**
     * Get location by locationId
     */
    public Optional<Location> getLocationByLocationId(String locationId) {
        return locationRepository.findByLocationId(locationId);
    }
    
    /**
     * Create new location
     */
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }
    
    /**
     * Update location
     */
    public Location updateLocation(Location location) {
        return locationRepository.save(location);
    }
    
    /**
     * Delete location
     */
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
    
    /**
     * Calculate distance between two locations
     */
    public double calculateDistance(String locationId1, String locationId2) {
        Optional<Location> loc1 = getLocationByLocationId(locationId1);
        Optional<Location> loc2 = getLocationByLocationId(locationId2);
        
        if (loc1.isPresent() && loc2.isPresent()) {
            return loc1.get().distanceTo(loc2.get());
        }
        return -1;
    }
}
