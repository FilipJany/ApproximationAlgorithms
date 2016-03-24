package com.fjps.main.graph;

import com.fjps.main.graph.exceptions.WeightTypeNotSupported;

/**
 * Class representing edge connecting two vertexes.
 *
 * Created by MagnaT on 24/03/16.
 */
public class Edge<T extends Number> implements Comparable<Edge> {

    private final T weight;
    private final Vertex<T> v1;
    private final Vertex<T> v2;

    public Edge(Vertex<T> v1, Vertex<T> v2, T weight) {
        this.weight = weight;
        this.v1 = v1;
        this.v2 = v2;
    }

    public T getWeight() {
        return weight;
    }

    public Vertex<T> getV1() {
        return v1;
    }

    public Vertex<T> getV2() {
        return v2;
    }

    @Override
    public int compareTo(Edge e) throws WeightTypeNotSupported {
        if (weight instanceof Integer)
            return weight.intValue() - e.getWeight().intValue();
        if (weight instanceof Long) {
            long thisWeight = weight.longValue();
            long otherWeight = e.getWeight().longValue();
            return thisWeight > otherWeight ? 1 : (thisWeight < otherWeight ? -1 : 0);
        }
        if (weight instanceof Double)
            return (int) (weight.doubleValue() - e.getWeight().doubleValue());

        throw new WeightTypeNotSupported("Type is not supported: " + e.getWeight().getClass() + ".");
    }

    @Override
    public String toString() {
        return "Edge{" +
                "weight=" + weight +
                ", v1=" + v1 +
                ", v2=" + v2 +
                '}';
    }
}
