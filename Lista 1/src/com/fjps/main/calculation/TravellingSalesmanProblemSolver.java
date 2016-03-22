package com.fjps.main.calculation;

import com.fjps.main.graph.Graph;

/**
 * Simple interface for solving/estimating TSP problem.
 *
 * Created by Patryk Stopyra on 22/03/16.
 */
public interface TravellingSalesmanProblemSolver<T extends Number> {

    T calculateOptimum(Graph<T> graph);
}
