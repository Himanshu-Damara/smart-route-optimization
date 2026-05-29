package com.sro.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a vertex (node) in the graph
 * Used for graph-based route optimization
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vertex implements Comparable<Vertex> {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private double distance; // Used in Dijkstra's algorithm
    private Vertex previous; // Previous vertex in shortest path
    private boolean visited;

    public Vertex(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = Double.MAX_VALUE;
        this.visited = false;
    }

    @Override
    public int compareTo(Vertex other) {
        return Double.compare(this.distance, other.distance);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + latitude +
                ", lon=" + longitude +
                ", distance=" + distance +
                '}';
    }
}
