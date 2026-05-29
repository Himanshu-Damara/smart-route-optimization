package com.sro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for route optimization request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteOptimizationRequest {
    private List<DeliveryDTO> deliveries;
    private List<VehicleDTO> vehicles;
    private String warehouseLocationId;
}
