package com.sro.repository;

import com.sro.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for Location entity
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLocationId(String locationId);
    Optional<Location> findByName(String name);
}
