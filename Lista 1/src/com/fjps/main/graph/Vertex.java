package com.fjps.main.graph;

import com.fjps.main.graph.exceptions.NoDirectEdgeException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing single graph node.
 * <p>
 * I assume that vertex belongs to not directed graph, what is expressed by <code>addConnectionTo()</code>
 * & <code>removeConnectionTo()</code> methods.
 * <p>
 * Created by Patryk Stopyra on 17/03/16.
 */
public class Vertex<T extends Number> implements Comparable<Vertex>{

    private final String id;
    private double minDistance = Double.POSITIVE_INFINITY;
    private Vertex<T> previous;
    private final HashMap<Vertex, Edge<T>> neighbourhood;

    public Vertex(String id) {
        this.id = id;
        this.neighbourhood = new HashMap<>();
    }

    public Vertex(int orderNumber) {
        this.id = "V" + orderNumber;
        this.neighbourhood = new HashMap<>();
    }

    public boolean hasDirectConnection(Vertex v) {
        return neighbourhood.containsKey(v);
    }

    public boolean isReachable(Vertex<T> v) {
        return depthFirstSearch(new LinkedList<>(), v);
    }

    public Edge<T> getEdge(Vertex v) throws NoDirectEdgeException {
        if (!hasDirectConnection(v))
            throw new NoDirectEdgeException(this, v);

        return neighbourhood.get(v);
    }

    public T getEdgeCost(Vertex v) throws NoDirectEdgeException {
        if (!hasDirectConnection(v))
            throw new NoDirectEdgeException(this, v);

        return neighbourhood.get(v).getWeight();
    }

    public Edge<T> addConnectionTo(Vertex<T> v, T distance) {
        Edge<T> newEdge = new Edge<>(this, v, distance);

        this.neighbourhood.put(v, newEdge); // we override existing path, ignoring ratio of existing/previous distance
        v.neighbourhood.put(this, newEdge);

        return newEdge;
    }

    public Edge<T> removeConnectionTo(Vertex v) throws NoDirectEdgeException {
        if (!this.hasDirectConnection(v))
            throw new NoDirectEdgeException(this, v);
        if (!v.hasDirectConnection(this))
            throw new NoDirectEdgeException(v, this);

        Edge<T> removedEdge = neighbourhood.get(v);

        this.neighbourhood.remove(v);
        v.neighbourhood.remove(this);

        return removedEdge;
    }

    public String getID() {
        return id;
    }

    public HashMap<Vertex, Edge<T>> getNeighbourhood() {
        return neighbourhood;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        try {
            if (((Vertex<?>) other).id.equals(this.id))
                return true;
        } catch (ClassCastException unused) {
            // we fail as this exception occurs, thats the only wished behaviour
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder
                .append("Vertex{ID:")
                .append(id)
                .append(", ")
                .append(neighbourhood.size())
                .append(" connections:");

        neighbourhood.entrySet().stream()
                .forEach(neighbourEntry -> builder
                        .append("\t")
                        .append(neighbourEntry.getKey().getID())
                        .append(" (")
                        .append(neighbourEntry.getValue().getWeight())
                        .append(")"));

        return builder.append("}").toString();
    }

    private boolean depthFirstSearch(List<Vertex<T>> visited, Vertex<T> dest) {
        if (hasDirectConnection(dest))
            return true;

        visited.add(this);

        for (Vertex v : neighbourhood.keySet()) {
            if (!visited.contains(v))
                if (v.depthFirstSearch(visited, dest))
                    return true;
        }

        return false;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    public Vertex getPrevious() {
        return this.previous;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(minDistance, o.minDistance);
    }
}
