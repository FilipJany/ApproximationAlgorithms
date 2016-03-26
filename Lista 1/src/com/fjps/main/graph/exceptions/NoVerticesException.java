package com.fjps.main.graph.exceptions;

import com.fjps.main.graph.Vertex;

/**
 * Created by fifi on 26.03.2016.
 */
public class NoVerticesException extends Exception
{
    public NoVerticesException() {
        super("There are no vertices in graph - add some first!");
    }
}
