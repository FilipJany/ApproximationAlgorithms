package com.fjps.main.graph;

import com.fjps.main.graph.exceptions.NoDirectEdgeException;

import java.util.HashMap;

/**
 * Class representing single node.
 *
 * Created by Patryk Stopyra on 17/03/16.
 */
public class Vertex<T> {

    private final String id;
    private final HashMap<Vertex, T> neighbourhood;

    public Vertex(String id) {
        this.id = id;
        this.neighbourhood = new HashMap<>();
    }

    public Vertex(int orderNumber) {
        this.id = "V" + orderNumber;
        this.neighbourhood = new HashMap<>();
    }

    public boolean isConnectedWith(Vertex v) {
        return false;
    }

    public int getEdgeCost(Vertex v) throws NoDirectEdgeException {
        return 0;
    }

    public void addConnectionTo(Vertex v, T distance) {

    }

    public void removeConnectionTo(Vertex v) {

    }

    public String getID() {
        return id;
    }

    public HashMap<Vertex, T> getNeighbourhood() {
        return neighbourhood;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
