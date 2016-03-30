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

            Generator gen = new Generator(i);
            gen.generateVertices();
            gen.generateEdges();

            Graph<Double> graph = gen.getG();
            graph.assureMetric();

            TSPExactSolver<Double> solver = new TSPExactSolver<>();
            TSPOneHalfEstimator<Double> estimator = new TSPOneHalfEstimator<>();

            Double exactCost = solver.calculateOptimum(graph);
            Double estimatedCost = estimator.calculateOptimum(graph);

            System.out.println();
            System.out.println("Exact solver: " + exactCost + "(x1.5: " + 1.5 * exactCost + ")");
            System.out.println("Estimator: " + estimatedCost);

            Assert.assertTrue(exactCost * 1.5 >= estimatedCost);
            Assert.assertTrue(exactCost <= estimatedCost); //just to check if exact solver works properly

            System.out.println();
            System.out.println("Exact     " + solver.getPathAsString());
            System.out.println("Estimated " + estimator.getPathAsString());
        }
    }
}
