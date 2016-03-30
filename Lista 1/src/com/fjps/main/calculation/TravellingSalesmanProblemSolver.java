package com.fjps.main.calculation;

import com.fjps.main.graph.Edge;
import com.fjps.main.graph.Graph;

import java.util.List;

/**
 * Simple interface for solving/estimating TSP problem.
 *
 * Created by Patryk Stopyra on 22/03/16.
 */
public interface TravellingSalesmanProblemSolver<T extends Number> {

    T calculateOptimum(Graph<T> graph);

    List<Edge<T>> getLastOptimalPath();

    String getPathAsString();
}
