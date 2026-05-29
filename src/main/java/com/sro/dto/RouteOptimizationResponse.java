package com.sro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for route optimization response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteOptimizationResponse {
    private boolean success;
    private String message;
    private List<RouteDTO> routes;
    private double totalDistance;
    private double totalCost;
    private double capacityUtilization;
    private long executionTimeMs;
}
