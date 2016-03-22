package com.fjps.main.graph.exceptions;

import com.fjps.main.graph.Vertex;

/**
 * Thrown when such vertex could not been found in graph.
 *
 * Created by Patryk Stopyra on 22/03/16.
 */
public class NoSuchVertexException extends Exception {

    public NoSuchVertexException(String id) {
        super("Vertex of ID: " + id + " does not exist in this graph.");
    }

    public NoSuchVertexException(Vertex v) {
        this(v.getID());
    }

}
