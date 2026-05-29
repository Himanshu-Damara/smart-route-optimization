package com.sro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Location
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private String locationId;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String type;
    private String phoneNumber;
}
