package com.fjps.main.graph.exceptions;

import com.fjps.main.graph.Vertex;

/**
 * Exception representing case of direct edge lack between some nodes.
 *
 * Created by MagnaT on 17/03/16.
 */
public class NoDirectEdgeException extends RuntimeException {

    public NoDirectEdgeException(Vertex v1, Vertex v2) {
        super("There is no edge between " + v1.getID() + " and " + v2.getID());
    }
}
