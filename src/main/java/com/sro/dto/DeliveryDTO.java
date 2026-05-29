package com.sro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

/**
 * DTO for Delivery
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {
    private String deliveryId;
    private String pickupLocationId;
    private String deliveryLocationId;
    private double weight;
    private double volume;
    private int priority;
    private LocalTime startTime;
    private LocalTime endTime;
    private String customerPhone;
    private String notes;
}
