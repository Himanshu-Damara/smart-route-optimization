package com.sro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

/**
 * TimeWindow Model - represents time constraints for delivery
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeWindow {
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "end_time")
    private LocalTime endTime;
    
    /**
     * Check if a given time is within the time window
     */
    public boolean isWithinWindow(LocalTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }
    
    /**
     * Get duration of time window in minutes
     */
    public long getDurationMinutes() {
        return java.time.temporal.ChronoUnit.MINUTES.between(startTime, endTime);
    }
    
    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }
}
