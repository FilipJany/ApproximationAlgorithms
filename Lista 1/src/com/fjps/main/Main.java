package com.fjps.main;

import com.fjps.main.calculation.TSPExactSolver;
import com.fjps.main.calculation.TSPOneHalfEstimator;
import com.fjps.main.graph.Generator;
import com.fjps.main.graph.Graph;

/**
 * Main class of the List 1 program for Approximation Algorithms.
 *
 * Patryk Stopyra & Filip Jany
 * Faculty of Fundamental Problems of Technology,
 * Wroclaw Univerity of Technology
 * Wrocław, March 2016
 */
public class Main {

    private static final long NANO = 1000000000;

    public static void main(String[] args) {
        Generator gen = new Generator(50);
        gen.generateVertices();
        try
        {
            gen.generateEdges();
            gen.getG().assureMetric();
            System.out.println("Successfully Generated graph with "
                    + gen.getG().getNumberVertexes() + " vertexes and "
                    + gen.getG().getNumberEdges() + " edges.");
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }

        System.out.println();
        //long exactResult = calculateBySolver(gen.getG());
        long estimatedResult = calculateByEstimator(gen.getG());

        System.out.println();
        System.out.println("Exact result calculation time:     " + "NOT CALCULATED");//format(exactResult));
        System.out.println("Estimated result calculation time: " + format(estimatedResult));

    }

    private static long calculateBySolver(Graph<Double> graph) {
        TSPExactSolver<Double> solver = new TSPExactSolver<>();

        long start = System.nanoTime();
        double result = solver.calculateOptimum(graph);
        long end = System.nanoTime();

        System.out.println("Exact optimal:     " + result + "\n" + solver.getPathAsString());

        return end - start;
    }

    private static long calculateByEstimator(Graph<Double> graph) {
        TSPOneHalfEstimator<Double> estimator = new TSPOneHalfEstimator<>();

        long start = System.nanoTime();
        double result = estimator.calculateOptimum(graph);
        long end = System.nanoTime();

        System.out.println("Estimated optimal: " + result + "\n" + estimator.getPathAsString());

        return end - start;
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
