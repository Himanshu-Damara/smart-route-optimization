package com.sro.repository;

import com.sro.model.Delivery;
import com.sro.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Delivery entity
 */
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByDeliveryId(String deliveryId);
    List<Delivery> findByStatus(DeliveryStatus status);
    List<Delivery> findByPriorityGreaterThanEqual(int priority);
    List<Delivery> findByAssignedVehicleId(Long vehicleId);
}
