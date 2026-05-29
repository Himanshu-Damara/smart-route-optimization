package com.sro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

/**
 * DTO for Vehicle
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private String vehicleId;
    private String name;
    private double maxWeightCapacity;
    private double maxVolumeCapacity;
    private double fuelCostPerKm;
    private String currentLocationId;
    private LocalTime availableFrom;
    private LocalTime availableUntil;
    private String driverName;
    private String driverPhone;
    private String plateNumber;
}
