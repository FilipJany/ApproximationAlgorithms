package com.fjps.main.graph;

import com.fjps.main.graph.exceptions.NoDirectEdgeException;

import java.util.HashMap;

/**
 * Class representing single node.
 *
 * Created by Patryk Stopyra on 17/03/16.
 */
public class Vertex {

    private final String id;
    private final HashMap<Vertex, Integer> neighbourhood;

    public Vertex(String id) {
        this.id = id;
        this.neighbourhood = new HashMap<>();
    }

    public Vertex(int orderNumber) {
        this.id = "" + orderNumber;
        this.neighbourhood = new HashMap<>();
    }

    public boolean isConnectedWith(Vertex v) {
        return false;
    }

    public int getEdgeCost(Vertex v) throws NoDirectEdgeException {
        return 0;
    }

    public void addConnectionTo(Vertex v) {

    }

    public void removeConnectionTo(Vertex v) {

    }
}
