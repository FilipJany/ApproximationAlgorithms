package com.fjps.main.graph;

import com.fjps.main.graph.exceptions.WeightTypeNotSupported;

/**
 * Class representing edge connecting two vertexes.
 * <p>
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
        int weightComparison = compareWeights(e);

        if (weightComparison != 0)
            return weightComparison;
        else
            return compareVertexes(e);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "weight=" + weight +
                ", v1=" + v1 +
                ", v2=" + v2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge<?> edge = (Edge<?>) o;

        if (weight != null ? !weight.equals(edge.weight) : edge.weight != null) return false;
        if (v1 != null ? !v1.equals(edge.v1) : edge.v1 != null) return false;
        return v2 != null ? v2.equals(edge.v2) : edge.v2 == null;

    }

    @Override
    public int hashCode() {
        int result = weight != null ? weight.hashCode() : 0;
        result = 31 * result + (v1 != null ? v1.hashCode() : 0);
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        return result;
    }

    private int compareWeights(Edge e) throws WeightTypeNotSupported {
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

    private int compareVertexes(Edge e) {
        if (v1.equals(e.getV2()) && v2.equals(e.getV1()))
            return 0;

        // ids have to be unique; Graph object is responsible for this
        int v1Comparison = v1.getID().compareTo(e.getV1().getID());
        if (v1Comparison != 0)
            return v1Comparison;

        return v2.getID().compareTo(e.getV2().getID());
    }


}
