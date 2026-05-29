package com.sro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for Route
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDTO {
    private String routeId;
    private String vehicleId;
    private List<String> deliveryIds;
    private List<LocationDTO> waypoints;
    private double totalDistance;
    private double totalTime;
    private double totalCost;
    private String status;
    private int sequenceNumber;
}
