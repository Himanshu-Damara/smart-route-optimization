package com.sro.datastructure;

import java.util.*;

/**
 * Weighted, directed graph implementation for representing city map
 * Supports Dijkstra's algorithm for shortest path finding
 */
public class Graph {
    private Map<String, Vertex> vertices;
    private List<Edge> edges;
    private boolean isDirected;

    public Graph(boolean isDirected) {
        this.vertices = new HashMap<>();
        this.edges = new ArrayList<>();
        this.isDirected = isDirected;
    }

    public Graph() {
        this(true); // Default: directed graph
    }

    /**
     * Add a vertex to the graph
     */
    public void addVertex(Vertex vertex) {
        if (!vertices.containsKey(vertex.getId())) {
            vertices.put(vertex.getId(), vertex);
        }
    }

    /**
     * Add an edge between two vertices
     */
    public void addEdge(String sourceId, String destId, double distance, double time, double cost) {
        Vertex source = vertices.get(sourceId);
        Vertex destination = vertices.get(destId);

        if (source == null || destination == null) {
            throw new IllegalArgumentException("Vertex not found");
        }

        Edge edge = new Edge(source, destination, distance, time, cost);
        edges.add(edge);

        // If undirected, add reverse edge
        if (!isDirected) {
            edges.add(new Edge(destination, source, distance, time, cost));
        }
    }

    /**
     * Get all adjacent vertices for a given vertex
     */
    public List<Edge> getAdjacentEdges(String vertexId) {
        List<Edge> adjacent = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSource().getId().equals(vertexId) && !edge.isBlocked()) {
                adjacent.add(edge);
            }
        }
        return adjacent;
    }

    /**
     * Dijkstra's shortest path algorithm
     * Time Complexity: O((V + E) log V)
     */
    public List<Vertex> dijkstra(String sourceId) {
        // Reset all vertices
        for (Vertex v : vertices.values()) {
            v.setDistance(Double.MAX_VALUE);
            v.setPrevious(null);
            v.setVisited(false);
        }

        Vertex source = vertices.get(sourceId);
        if (source == null) {
            throw new IllegalArgumentException("Source vertex not found");
        }

        source.setDistance(0);

        // Priority queue for getting minimum distance vertex
        PriorityQueue<Vertex> pq = new PriorityQueue<>();
        pq.add(source);

        while (!pq.isEmpty()) {
            Vertex current = pq.poll();

            if (current.isVisited()) {
                continue;
            }

            current.setVisited(true);

            // Check all adjacent vertices
            for (Edge edge : getAdjacentEdges(current.getId())) {
                Vertex neighbor = edge.getDestination();
                double newDistance = current.getDistance() + edge.getDistance();

                // Relaxation
                if (newDistance < neighbor.getDistance()) {
                    neighbor.setDistance(newDistance);
                    neighbor.setPrevious(current);
                    pq.add(neighbor);
                }
            }
        }

        return new ArrayList<>(vertices.values());
    }

    /**
     * Get shortest path between source and destination
     */
    public List<Vertex> getShortestPath(String sourceId, String destId) {
        dijkstra(sourceId);
        List<Vertex> path = new ArrayList<>();
        Vertex current = vertices.get(destId);

        if (current == null) {
            throw new IllegalArgumentException("Destination vertex not found");
        }

        while (current != null) {
            path.add(0, current);
            current = current.getPrevious();
        }

        return path;
    }

    /**
     * Calculate total distance of a path
     */
    public double calculatePathDistance(List<Vertex> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex current = path.get(i);
            Vertex next = path.get(i + 1);

            for (Edge edge : getAdjacentEdges(current.getId())) {
                if (edge.getDestination().getId().equals(next.getId())) {
                    total += edge.getDistance();
                    break;
                }
            }
        }
        return total;
    }

    /**
     * BFS (Breadth-First Search) traversal
     */
    public List<Vertex> bfs(String startId) {
        List<Vertex> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<Vertex> queue = new java.util.ArrayDeque<>();

        Vertex start = vertices.get(startId);
        if (start == null) {
            throw new IllegalArgumentException("Start vertex not found");
        }

        queue.add(start);
        visited.add(startId);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            result.add(current);

            for (Edge edge : getAdjacentEdges(current.getId())) {
                String neighborId = edge.getDestination().getId();
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    queue.add(edge.getDestination());
                }
            }
        }

        return result;
    }

    /**
     * DFS (Depth-First Search) traversal
     */
    public List<Vertex> dfs(String startId) {
        List<Vertex> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfsHelper(startId, visited, result);
        return result;
    }

    private void dfsHelper(String vertexId, Set<String> visited, List<Vertex> result) {
        visited.add(vertexId);
        Vertex vertex = vertices.get(vertexId);
        result.add(vertex);

        for (Edge edge : getAdjacentEdges(vertexId)) {
            String neighborId = edge.getDestination().getId();
            if (!visited.contains(neighborId)) {
                dfsHelper(neighborId, visited, result);
            }
        }
    }

    // Getters
    public Vertex getVertex(String id) {
        return vertices.get(id);
    }

    public Collection<Vertex> getAllVertices() {
        return vertices.values();
    }

    public List<Edge> getAllEdges() {
        return edges;
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }
}
