package com.fjps.main.calculation;

import com.fjps.main.graph.Graph;
import com.fjps.main.graph.Vertex;

/**
 * Implementation of Christofies algorithm.
 * <p>
 * Created by Patryk Stopyra on 22/03/16.
 */
public class TSPOneHalfEstimator<T extends Number> implements TravellingSalesmanProblemSolver<T> {

    @Override
    public T calculateOptimum(Graph<T> graph) {
        return null;
    }

    /**
     * Implementation of Kraskal's Algorithm for obtaing Minimum Spanning Tree.
     * Result is kept as new <code>Graph</code> object containing the same nodes like the source graph and some of
     * original edges (those defining MST).
     *
     * @param graph graph on which we are looking for a MST.
     * @return New <code>Graph</code> object containing exactly the same vertex objects as <code>graph</code> and some
     * subset of it's edges that defines minimum spanning tree.
     */
    private Graph<T> minimumSpanningTree(Graph<T> graph) {
        Graph<T> mst = new Graph<>();

        graph.getAllVertexes().stream()
                .map(Vertex::getID)
                .forEach(mst::addVertex);

        graph.getAllEdges().stream()
                .filter(e -> !mst.isConnected(e.getV1(), e.getV2()))
                .peek(e -> mst.connect(e.getV1(), e.getV2(), e.getWeight()))
                .anyMatch(e -> mst.getNumberEdges() == mst.getNumberVertexes() - 1);

        return mst;
    }
}
