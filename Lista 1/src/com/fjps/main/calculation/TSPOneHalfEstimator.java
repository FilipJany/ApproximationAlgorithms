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
        Graph<T> mst = minimumSpanningTree(graph);


        return null;
    }

    /**
     * Implementation of Kraskal's Algorithm for obtaing Minimum Spanning Tree.
     * Result is kept as new <code>Graph</code> object containing the same nodes like the source graph and some of
     * original edges (those defining MST).
     *
     * @param original graph on which we are looking for a MST.
     * @return New <code>Graph</code> object containing exactly the same vertex objects as <code>original</code> and some
     * subset of it's edges that defines minimum spanning tree.
     */
    private Graph<T> minimumSpanningTree(Graph<T> original) {
        Graph<T> mst = new Graph<>();

        original.getAllVertexes().stream()
                .map(Vertex::getID)
                .forEach(mst::addVertex);

        original.getAllEdges().stream()
                .filter(e -> !mst.isConnected(e.getV1(), e.getV2()))
                .peek(e -> mst.connect(e.getV1(), e.getV2(), e.getWeight()))
                .anyMatch(e -> mst.getNumberEdges() == mst.getNumberVertexes() - 1);

        return mst;
    }

    /**
     * Returns an indiced graph containing nodes with odd number of neighbours from <code>mst</code>.
     * These nodes are connected with edges of weight (distance) took from the <code>original</code> graph,
     * since there is no information about connection in <code>mst</code>.
     *
     * @param mst      graph containing minimum spanning tree to find odd-degree vertexes
     * @param original original graph containing all edges information
     * @return Graph made of odd-degree vertexes from <code>mst</code>,
     * connected with edges from <code>original</code> graph.
     */
    private Graph<T> indicedOddNeighbourhood(Graph<T> mst, Graph<T> original) {
        Graph<T> odd = new Graph<>();

        mst.getAllVertexes().stream()
                .filter(v -> v.getNeighbourhood().size() % 2 != 0)
                .map(Vertex::getID)
                .forEach(odd::addVertex);

        for (Vertex<T> v : odd.getAllVertexes())
            v.getNeighbourhood().keySet().stream()
                    .filter(neighbour -> odd.hasVertex(v))
                    .map(neighbour -> v.getEdge(neighbour))
                    .forEach(edge -> odd.connect(edge.getV1(), edge.getV2(), edge.getWeight()));

        return odd;
    }
}
