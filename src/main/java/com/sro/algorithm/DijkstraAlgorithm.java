package com.sro.algorithm;

import com.sro.datastructure.Graph;
import com.sro.datastructure.Vertex;
import java.util.*;

/**
 * Dijkstra's Shortest Path Algorithm
 * Time Complexity: O((V + E) log V)
 * Used to find shortest path between two locations
 */
public class DijkstraAlgorithm {
    private Graph graph;

    public DijkstraAlgorithm(Graph graph) {
        this.graph = graph;
    }

    /**
     * Find shortest path from source to destination
     * Returns list of vertices representing the path
     */
    public List<Vertex> findShortestPath(String sourceId, String destId) {
        return graph.getShortestPath(sourceId, destId);
    }

    /**
     * Calculate distance of a path
     */
    public double calculatePathDistance(List<Vertex> path) {
        return graph.calculatePathDistance(path);
    }

    /**
     * Get shortest distances from source to all vertices
     */
    public Map<String, Double> getShortestDistances(String sourceId) {
        graph.dijkstra(sourceId);
        Map<String, Double> distances = new HashMap<>();
        
        for (Vertex vertex : graph.getAllVertices()) {
            if (vertex.getDistance() != Double.MAX_VALUE) {
                distances.put(vertex.getId(), vertex.getDistance());
            }
        }
        
        return distances;
    }

    /**
     * Find all unvisited vertices
     */
    public List<Vertex> getUnvisitedVertices() {
        List<Vertex> unvisited = new ArrayList<>();
        for (Vertex v : graph.getAllVertices()) {
            if (!v.isVisited()) {
                unvisited.add(v);
            }
        }
        return unvisited;
    }
}
