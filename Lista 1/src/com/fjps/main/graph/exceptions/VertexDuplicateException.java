package com.fjps.main.graph.exceptions;

import com.fjps.main.graph.Vertex;

/**
 * Thrown when such vertex already exists in graph.
 *
 * Created by MagnaT on 22/03/16.
 */
public class VertexDuplicateException extends RuntimeException {

    public VertexDuplicateException(String id) {
        super("Vertex of ID: " + id + " already exists in this graph. It should be removed first.");
    }
}
