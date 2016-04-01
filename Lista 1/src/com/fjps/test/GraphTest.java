package com.fjps.test;

import com.fjps.main.graph.Graph;
import com.fjps.main.graph.Vertex;
import com.fjps.main.graph.exceptions.NoSuchVertexException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by MagnaT on 30/03/16.
 */
public class GraphTest {

    @Test
    public void assureMetricTest1() {
        System.out.println("\n-----------------\nGraphTest: Test 1\n-----------------\n");

        Graph<Double> initial = new Graph<>();

        Vertex<Double> v0 = initial.addVertex();
        Vertex<Double> v1 = initial.addVertex();
        Vertex<Double> v2 = initial.addVertex();
        Vertex<Double> v3 = initial.addVertex();

        initial.connect(v0, v1, 5.0);
        initial.connect(v0, v2, 1.2);
        initial.connect(v0, v3, 2.5);
        initial.connect(v1, v2, 4.0);
        initial.connect(v1, v3, 2.0);
        initial.connect(v2, v3, 1.1);

        System.out.println("Initial: " + initial.toString());

        Graph<Double> expected = new Graph<>();

        v0 = expected.addVertex();
        v1 = expected.addVertex();
        v2 = expected.addVertex();
        v3 = expected.addVertex();

        expected.connect(v0, v1, 4.3);
        expected.connect(v0, v2, 1.2);
        expected.connect(v0, v3, 2.3);
        expected.connect(v1, v2, 3.1);
        expected.connect(v1, v3, 2.0);
        expected.connect(v2, v3, 1.1);

        System.out.println("Expected: " + expected.toString());

        Graph<Double> obtained = initial;
        obtained.assureMetric();

        System.out.println("Obtained: " + obtained.toString());

        Assert.assertEquals(expected.getAllVertexes(), obtained.getAllVertexes());
        Assert.assertEquals(expected.getAllEdges(), obtained.getAllEdges());
    }

    @Test
    public void assureMetricTest2() {
        System.out.println("\n-----------------\nGraphTest: Test 2\n-----------------\n");

        Graph<Double> initial = new Graph<>();

        Vertex<Double> v0 = initial.addVertex();
        Vertex<Double> v1 = initial.addVertex();
        Vertex<Double> v2 = initial.addVertex();
        Vertex<Double> v3 = initial.addVertex();
        Vertex<Double> v4 = initial.addVertex();

        initial.connect(v0, v1, 6.2);
        initial.connect(v0, v2, 7.0);
        initial.connect(v0, v3, 9.8);
        initial.connect(v0, v4, 2.0);
        initial.connect(v1, v2, 0.5);
        initial.connect(v1, v3, 0.6);
        initial.connect(v1, v4, 3.1);
        initial.connect(v2, v3, 1.2);
        initial.connect(v2, v4, 1.2);
        initial.connect(v3, v4, 2.7);

        System.out.println("Initial: " + initial.toString());

        Graph<Double> expected = new Graph<>();

        v0 = expected.addVertex();
        v1 = expected.addVertex();
        v2 = expected.addVertex();
        v3 = expected.addVertex();
        v4 = expected.addVertex();

        expected.connect(v0, v1, 3.7);
        expected.connect(v0, v2, 3.2);
        expected.connect(v0, v3, 4.3);
        expected.connect(v0, v4, 2.0);
        expected.connect(v1, v2, 0.5);
        expected.connect(v1, v3, 0.6);
        expected.connect(v1, v4, 1.7);
        expected.connect(v2, v3, 1.1);
        expected.connect(v2, v4, 1.2);
        expected.connect(v3, v4, 2.3);

        System.out.println("Expected: " + expected.toString());

        Graph<Double> obtained = initial;
        obtained.assureMetric();

        System.out.println("Obtained: " + obtained.toString());

        Assert.assertEquals(expected.getAllVertexes(), obtained.getAllVertexes());
        Assert.assertEquals(expected.getAllEdges(), obtained.getAllEdges());
    }

    @Test
    public void graphOperationsTest1() {
        System.out.println("\n-----------------\nGraphTest: Test 3\n-----------------\n");

        Graph<Double> graph = new Graph<>();

        Vertex<Double> v0 = graph.addVertex();
        Vertex<Double> v1 = graph.addVertex();
        Vertex<Double> v2 = graph.addVertex();
        Vertex<Double> v3 = graph.addVertex();

        Assert.assertEquals(4, graph.getNumberVertexes());

        try {
            graph.connect(v0, v1, 3.0);
            graph.connect(v0, v2, 1.8);
            graph.connect(v0, v3, 2.2);
            graph.connect(v2, v1, 4.7);
            graph.connect(v2, v3, 5.2);
            graph.connect(v3, v1, 0.5);
        } catch (NoSuchVertexException e) {
            System.out.println("This exception should not occur.\n\t" + e.getMessage());
        }

        Assert.assertEquals(6, graph.getNumberEdges());

        System.out.println("Result:\n" + graph);
        graph.assureMetric();
        try {
            graph.removeVertex(v1);
        } catch (NoSuchVertexException e) {
            System.out.println("This exception should not occur.\n\t" + e.getMessage());
        }

        Assert.assertEquals(3, graph.getNumberVertexes());
        Assert.assertEquals(3, graph.getNumberEdges());
        Assert.assertEquals(null, graph.getVertex(v1.getID()));

        System.out.println("Removing vertex V1");
        System.out.println("Result:\n" + graph);

    }
}
