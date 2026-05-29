package com.sro.controller;

import com.sro.model.Location;
import com.sro.service.LocationService;
import com.sro.dto.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Location endpoints
 */
@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = "*")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    /**
     * GET /locations - Get all locations
     */
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }
    
    /**
     * GET /locations/{id} - Get location by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Optional<Location> location = locationService.getLocationById(id);
        return location.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * POST /locations - Create new location
     */
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location created = locationService.createLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /locations/{id} - Update location
     */
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        Optional<Location> existing = locationService.getLocationById(id);
        if (existing.isPresent()) {
            location.setId(id);
            Location updated = locationService.updateLocation(location);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * DELETE /locations/{id} - Delete location
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * GET /locations/distance?from={id1}&to={id2} - Calculate distance
     */
    @GetMapping("/distance")
    public ResponseEntity<Double> calculateDistance(
            @RequestParam String from,
            @RequestParam String to) {
        double distance = locationService.calculateDistance(from, to);
        return distance >= 0 ? ResponseEntity.ok(distance) : ResponseEntity.badRequest().build();
    }
}
