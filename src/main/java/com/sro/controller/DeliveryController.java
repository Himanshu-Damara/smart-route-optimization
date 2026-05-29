package com.sro.controller;

import com.sro.model.Delivery;
import com.sro.model.DeliveryStatus;
import com.sro.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Delivery endpoints
 */
@RestController
@RequestMapping("/deliveries")
@CrossOrigin(origins = "*")
public class DeliveryController {
    
    @Autowired
    private DeliveryService deliveryService;
    
    /**
     * GET /deliveries - Get all deliveries
     */
    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * GET /deliveries/{id} - Get delivery by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);
        return delivery.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * GET /deliveries/pending - Get all pending deliveries
     */
    @GetMapping("/status/pending")
    public ResponseEntity<List<Delivery>> getPendingDeliveries() {
        List<Delivery> deliveries = deliveryService.getPendingDeliveries();
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * POST /deliveries - Create new delivery
     */
    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        Delivery created = deliveryService.createDelivery(delivery);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /deliveries/{id} - Update delivery
     */
    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable Long id, @RequestBody Delivery delivery) {
        Optional<Delivery> existing = deliveryService.getDeliveryById(id);
        if (existing.isPresent()) {
            delivery.setId(id);
            Delivery updated = deliveryService.updateDelivery(delivery);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * PUT /deliveries/{id}/mark-delivered - Mark delivery as delivered
     */
    @PutMapping("/{id}/mark-delivered")
    public ResponseEntity<Delivery> markAsDelivered(@PathVariable Long id) {
        Delivery delivery = deliveryService.markAsDelivered(id);
        return delivery != null ? ResponseEntity.ok(delivery) : ResponseEntity.notFound().build();
    }
    
    /**
     * DELETE /deliveries/{id} - Delete delivery
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
}
