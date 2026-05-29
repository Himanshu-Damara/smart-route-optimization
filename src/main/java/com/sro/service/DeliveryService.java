package com.sro.service;

import com.sro.model.Delivery;
import com.sro.model.DeliveryStatus;
import com.sro.model.Vehicle;
import com.sro.repository.DeliveryRepository;
import com.sro.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for Delivery operations
 */
@Service
public class DeliveryService {
    
    @Autowired
    private DeliveryRepository deliveryRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    /**
     * Get all deliveries
     */
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
    
    /**
     * Get delivery by ID
     */
    public Optional<Delivery> getDeliveryById(Long id) {
        return deliveryRepository.findById(id);
    }
    
    /**
     * Get delivery by deliveryId
     */
    public Optional<Delivery> getDeliveryByDeliveryId(String deliveryId) {
        return deliveryRepository.findByDeliveryId(deliveryId);
    }
    
    /**
     * Get all pending deliveries
     */
    public List<Delivery> getPendingDeliveries() {
        return deliveryRepository.findByStatus(DeliveryStatus.PENDING);
    }
    
    /**
     * Get deliveries by priority
     */
    public List<Delivery> getDeliveriesByPriority(int priority) {
        return deliveryRepository.findByPriorityGreaterThanEqual(priority);
    }
    
    /**
     * Create new delivery
     */
    public Delivery createDelivery(Delivery delivery) {
        if (delivery.getDeliveryId() == null || delivery.getDeliveryId().isBlank()) {
            delivery.setDeliveryId("D" + String.format("%03d", deliveryRepository.count() + 1));
        }
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setCreatedAt(LocalDateTime.now());
        return deliveryRepository.save(delivery);
    }
    
    /**
     * Update delivery
     */
    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }
    
    /**
     * Assign delivery to vehicle
     */
    public Delivery assignToVehicle(Long deliveryId, Long vehicleId) {
        Optional<Delivery> delivery = deliveryRepository.findById(deliveryId);
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        
        if (delivery.isPresent() && vehicle.isPresent()) {
            Delivery d = delivery.get();
            Vehicle v = vehicle.get();
            d.assignToVehicle(v);
            return deliveryRepository.save(d);
        }
        return null;
    }
    
    /**
     * Mark delivery as delivered
     */
    public Delivery markAsDelivered(Long deliveryId) {
        Optional<Delivery> delivery = deliveryRepository.findById(deliveryId);
        if (delivery.isPresent()) {
            Delivery d = delivery.get();
            d.markAsDelivered();
            return deliveryRepository.save(d);
        }
        return null;
    }
    
    /**
     * Delete delivery
     */
    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }
    
    /**
     * Get count of deliveries by status
     */
    public long getDeliveryCountByStatus(DeliveryStatus status) {
        return deliveryRepository.findByStatus(status).size();
    }
}
