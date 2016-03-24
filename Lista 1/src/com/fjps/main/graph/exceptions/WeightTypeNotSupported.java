package com.fjps.main.graph.exceptions;

/**
 * Thrown when generic type of Edge/Graph/Vertex cannot by evaluated or compared.
 *
 * Created by MagnaT on 24/03/16.
 */
public class WeightTypeNotSupported extends RuntimeException {
    public WeightTypeNotSupported(String message) {
        super(message);
    }
}
