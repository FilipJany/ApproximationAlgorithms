package com.fjps.main.calculation;

import com.fjps.main.graph.Graph;

/**
 * Created by MagnaT on 22/03/16.
 */
public interface TravellingSalesmanProblemSolver<T extends Number> {

    T calculateOptimum(Graph<T> graph);
}
