package com.sro.repository;

import com.sro.model.Vehicle;
import com.sro.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Vehicle entity
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleId(String vehicleId);
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findByCurrentLocationId(Long locationId);
}
