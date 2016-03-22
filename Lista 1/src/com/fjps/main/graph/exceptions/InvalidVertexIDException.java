package com.fjps.main.graph.exceptions;

/**
 * Created by MagnaT on 22/03/16.
 */
public class InvalidVertexIDException extends RuntimeException {

    public InvalidVertexIDException(String id) {
        super("ID: " + id + " is invalid");
    }
}
