package com.fjps.test;

import com.fjps.main.calculation.TSPExactSolver;
import com.fjps.main.calculation.TSPOneHalfEstimator;
import com.fjps.main.graph.Graph;
import org.junit.Assert;
import org.junit.Test;

/**
 * Optimizer tests.
 *
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

        System.out.println(estimator.getLastOptimalPath());

        System.out.println("Exact     " + solver.getPathAsString());
        System.out.println("Estimated " +estimator.getPathAsString());
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

        System.out.println("Exact     " + solver.getPathAsString());
        System.out.println("Estimated " +estimator.getPathAsString());
    }
}
