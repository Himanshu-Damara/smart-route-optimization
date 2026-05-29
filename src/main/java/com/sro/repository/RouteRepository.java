package com.sro.repository;

import com.sro.model.Route;
import com.sro.model.RouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Route entity
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByRouteId(String routeId);
    List<Route> findByStatus(RouteStatus status);
    List<Route> findByVehicleId(Long vehicleId);
}
