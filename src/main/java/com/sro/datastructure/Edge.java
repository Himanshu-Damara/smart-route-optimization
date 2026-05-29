package com.sro.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an edge (connection) between two vertices
 * Weighted edge with distance, time, and cost
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Edge {
    private Vertex source;
    private Vertex destination;
    private double distance;  // kilometers
    private double time;      // minutes
    private double cost;      // rupees
    private boolean isBlocked; // traffic/obstacles

    public Edge(Vertex source, Vertex destination, double distance, double time, double cost) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.time = time;
        this.cost = cost;
        this.isBlocked = false;
    }

    @Override
    public String toString() {
        return source.getName() + " -> " + destination.getName() +
                " (" + distance + "km, " + time + "min, ₹" + cost + ")";
    }
}
