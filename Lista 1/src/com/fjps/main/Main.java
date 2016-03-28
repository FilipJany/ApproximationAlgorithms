package com.fjps.main;

import com.fjps.main.calculation.TSPExactSolver;
import com.fjps.main.graph.Generator;
import com.fjps.main.graph.Graph;
import com.fjps.main.graph.Vertex;
import com.fjps.main.graph.exceptions.NoSuchVertexException;

/**
 * Main class of the List 1 program for Approximation Algorithms.
 *
 * Patryk Stopyra & Filip Jany
 * Faculty of Fundamental Problems of Technology,
 * Wroclaw Univerity of Technology
 * Wrocław, March 2016
 */
public class Main {

    public static void main(String[] args) {
        Graph<Double> exampleGraph = new Graph<>();
//
//        Vertex<Double> v0 = exampleGraph.addVertex();
//        Vertex<Double> v1 = exampleGraph.addVertex();
//        Vertex<Double> v2 = exampleGraph.addVertex();
//        Vertex<Double> v3 = exampleGraph.addVertex();
//
//        try {
//            exampleGraph.connect(v0, v1, 3.0);
//            exampleGraph.connect(v0, v2, 1.8);
//            exampleGraph.connect(v0, v3, 2.2);
//            exampleGraph.connect(v2, v1, 4.7);
//            exampleGraph.connect(v2, v3, 5.2);
//            exampleGraph.connect(v3, v1, 0.5);
//        } catch (NoSuchVertexException e) {
//            System.out.println("This exception should not occur.\n\t" + e.getMessage());
//        }
//
//        System.out.println("Result:\n" + exampleGraph);
//        exampleGraph.assureMetric();
//        try {
//            exampleGraph.removeVertex(v1);
//        } catch (NoSuchVertexException e) {
//            System.out.println("This exception should not occur.\n\t" + e.getMessage());
//        }
//
//        System.out.println("Removing vertex V1");
//        System.out.println("Result:\n" + exampleGraph);
        Generator gen = new Generator(5);
        gen.generateVertices();
        try
        {
            gen.generateEdges();
            gen.getG().assureMetric();
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        TSPExactSolver t = new TSPExactSolver(gen.getG());
        int[] path = t.calculate();
    }
}
