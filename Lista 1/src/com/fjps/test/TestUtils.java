package com.fjps.test;

import com.fjps.main.graph.Graph;
import com.fjps.main.graph.Vertex;

/**
 * Utilities for program testing.
 *
 * Created by MagnaT on 30/03/16.
 */
public class TestUtils {

    private static final long NANO = 1000000000;

    public static Graph<Double> getKnownGraph1() {
        Graph<Double> graph = new Graph<>();

        Vertex<Double> v0 = graph.addVertex();
        Vertex<Double> v1 = graph.addVertex();
        Vertex<Double> v2 = graph.addVertex();
        Vertex<Double> v3 = graph.addVertex();

        graph.connect(v0, v1, 4.3);
        graph.connect(v0, v2, 1.2);
        graph.connect(v0, v3, 2.3);
        graph.connect(v1, v2, 3.1);
        graph.connect(v1, v3, 2.0);
        graph.connect(v2, v3, 1.1);

        graph.assureMetric();

        return graph;
    }

    public static Graph<Double> getKnownGraph2() {
        Graph<Double> graph = new Graph<>();

        Vertex<Double> v0 = graph.addVertex();
        Vertex<Double> v1 = graph.addVertex();
        Vertex<Double> v2 = graph.addVertex();
        Vertex<Double> v3 = graph.addVertex();
        Vertex<Double> v4 = graph.addVertex();

        graph.connect(v0, v1, 3.7);
        graph.connect(v0, v2, 3.2);
        graph.connect(v0, v3, 4.3);
        graph.connect(v0, v4, 2.0);
        graph.connect(v1, v2, 0.5);
        graph.connect(v1, v3, 0.6);
        graph.connect(v1, v4, 1.7);
        graph.connect(v2, v3, 1.1);
        graph.connect(v2, v4, 1.2);
        graph.connect(v3, v4, 2.3);

        graph.assureMetric();

        return graph;
    }

    public static String format(long nanos) {
        long sec = Math.abs(nanos / NANO);
        return String.format(
                "%d:%02d:%02d.%03d",
                sec / 3600,
                (sec % 3600) / 60,
                sec % 60,
                (nanos / 1000000) % 1000);
    }
}
