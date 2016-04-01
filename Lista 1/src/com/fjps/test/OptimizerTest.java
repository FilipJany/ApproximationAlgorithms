package com.fjps.test;

import com.fjps.main.calculation.TSPExactSolver;
import com.fjps.main.calculation.TSPOneHalfEstimator;
import com.fjps.main.graph.Generator;
import com.fjps.main.graph.Graph;
import com.fjps.main.graph.exceptions.NoVerticesException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Optimizer tests.
 * <p>
 * Created by MagnaT on 30/03/16.
 */
public class OptimizerTest {

    @Test
    public void optimizerTest1() {
        System.out.println("\n---------------------\nOptimizerTest: Test 1\n---------------------\n");

        Graph<Double> graph = TestUtils.getKnownGraph1();

        TSPExactSolver<Double> solver = new TSPExactSolver<>();
        TSPOneHalfEstimator<Double> estimator = new TSPOneHalfEstimator<>();

        Double exactCost = solver.calculateOptimum(graph);
        Double estimatedCost = estimator.calculateOptimum(graph);

        System.out.println("Exact solver: " + exactCost + "(x1.5: " + 1.5 * exactCost + ")");
        System.out.println("Estimator: " + estimatedCost);

        Assert.assertTrue(exactCost * 1.5 >= estimatedCost);

        System.out.println();
        System.out.println("Exact     " + solver.getPathAsString());
        System.out.println("Estimated " + estimator.getPathAsString());
    }

    @Test
    public void optimizerTest2() {
        System.out.println("\n---------------------\nOptimizerTest: Test 2\n---------------------\n");

        Graph<Double> graph = TestUtils.getKnownGraph2();

        TSPExactSolver<Double> solver = new TSPExactSolver<>();
        TSPOneHalfEstimator<Double> estimator = new TSPOneHalfEstimator<>();

        Double exactCost = solver.calculateOptimum(graph);
        Double estimatedCost = estimator.calculateOptimum(graph);

        System.out.println("Exact solver: " + exactCost + "(x1.5: " + 1.5 * exactCost + ")");
        System.out.println("Estimator: " + estimatedCost);

        Assert.assertTrue(exactCost * 1.5 >= estimatedCost);

        System.out.println();
        System.out.println("Exact     " + solver.getPathAsString());
        System.out.println("Estimated " + estimator.getPathAsString());
    }

    @Test
    public void optimizerTest3() throws NoVerticesException {
        for (int i = 3; i < 20; i++) {
            System.out.println("\n------------------------\nOptimizerTest: Test 3."
                    + (i - 2) + "\n------------------------\n");

            long start;
            long end;

            Generator gen = new Generator(i);
            gen.generateVertices();
            gen.generateEdges();

            Graph<Double> graph = gen.getG();
            graph.assureMetric();

            TSPExactSolver<Double> solver = new TSPExactSolver<>();
            TSPOneHalfEstimator<Double> estimator = new TSPOneHalfEstimator<>();

            start = System.nanoTime();
            Double exactCost = solver.calculateOptimum(graph);
            end = System.nanoTime();

            System.out.println();
            System.out.println("Exact solver time: " + TestUtils.format(end-start));

            start = System.nanoTime();
            Double estimatedCost = estimator.calculateOptimum(graph);
            end = System.nanoTime();

            System.out.println("Estimator time:    " + TestUtils.format(end-start));

            System.out.println();
            System.out.println("Exact solver: " + exactCost + " (x1.5: " + 1.5 * exactCost + ")");
            System.out.println("Estimator:    " + estimatedCost);

            System.out.println();
            System.out.println("Exact     " + solver.getPathAsString());
            System.out.println("Estimated " + estimator.getPathAsString());

            Assert.assertTrue(exactCost * 1.5 >= estimatedCost);
            Assert.assertTrue(exactCost <= estimatedCost); //just to check if exact solver works properly
        }
    }

    /**
     * Exact solver performance.
     *
     * @throws NoVerticesException
     */
    @Test
    public void optimizerTest4() throws NoVerticesException {
        System.out.println("\n---------------------\nOptimizerTest: Test 4\n---------------------\n");

        System.out.println("Exact solver performance:\nSize\tMilliseconds");
        for (int i = 3; i < 10; i++) {

            long avg = 0;
            int reps = 10;

            for (int repeat = 0; repeat < reps; repeat++) {
                long start;
                long end;

                Generator gen = new Generator(i);
                gen.generateVertices();
                gen.generateEdges();

                Graph<Double> graph = gen.getG();
                graph.assureMetric();

                TSPExactSolver<Double> solver = new TSPExactSolver<>();

                start = System.nanoTime();
                solver.calculateOptimum(graph);
                end = System.nanoTime();

                avg += end - start;
            }

            System.out.println(i + "\t\t" + avg/reps/1000000);
        }
    }

    /**
     * 1.5-Approximation solver performance.
     *
     * @throws NoVerticesException
     */
    @Test
    public void optimizerTest5() throws NoVerticesException {
        System.out.println("\n---------------------\nOptimizerTest: Test 5\n---------------------\n");

        System.out.println("Approximation solver performance:\nSize\tMilliseconds");
        for (int i = 3; i < 140; i++) {

            long avg = 0;
            int reps = 2;

            for (int repeat = 0; repeat < reps; repeat++) {
                long start;
                long end;

                Generator gen = new Generator(i);
                gen.generateVertices();
                gen.generateEdges();

                Graph<Double> graph = gen.getG();
                graph.assureMetric();

                TSPOneHalfEstimator<Double> solver = new TSPOneHalfEstimator<>();

                start = System.nanoTime();
                solver.calculateOptimum(graph);
                end = System.nanoTime();

                avg += end - start;
            }

            System.out.println(i + "\t" + avg/reps/1000000);
        }
    }
}
