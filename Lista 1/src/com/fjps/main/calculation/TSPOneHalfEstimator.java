package com.fjps.main.calculation;

import com.fjps.main.graph.Edge;
import com.fjps.main.graph.Graph;
import com.fjps.main.graph.Vertex;
import com.fjps.main.graph.exceptions.WeightTypeNotSupported;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of Christofies algorithm.
 * <p>
 * Created by Patryk Stopyra on 22/03/16.
 */
public class TSPOneHalfEstimator<T extends Number> implements TravellingSalesmanProblemSolver<T> {

    private List<Edge<T>> lastOptimalPath;

    @Override
    public T calculateOptimum(Graph<T> graph) {
        Graph<T> mst = minimumSpanningTree(graph);
        Graph<T> mpm = minimumPerfectMatching(indicedOddNeighbourhood(mst, graph));

        lastOptimalPath = optimalPath(mst, mpm, graph);

        return pathLength(lastOptimalPath);
    }

    public List<Edge<T>> getLastOptimalPath() {
        return lastOptimalPath;
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
            original.getVertex(v.getID()).getNeighbourhood().keySet().stream()
                    .filter(neighbour -> odd.hasVertex(neighbour))
                    .map(neighbour -> v.getEdge(neighbour))
                    .forEach(edge -> odd.connect(edge.getV1(), edge.getV2(), edge.getWeight()));

        return odd;
    }

    private Graph<T> minimumPerfectMatching(Graph<T> graph) {
        Graph<T> matching = new Graph<>();

        return matching;
    }

    /**
     * Algoritm to find Hamiltonian Path on given <code>multigraph</code> (consisting of minimum spanning tree over the original graph
     * & minimum perfect matching over odd nodes of mst). Algoritm consists of two steps:<br>
     * 1. Construct a circuit over the <code>multigraph</code>.<br>
     * 2. Transformation of circuit to Hamilton cycle, by skipping twice visited node.
     *
     * @param mst      minimum sppaning tree - part of <code>multigraph</code>
     * @param mpm      minimum perfect matching - part of <code>multigraph</code>
     * @param original original graph - used to find lenghts of direct edges between nodes
     * @return Estimated optimal path (1.5-estimation) for TSP.
     */
    private List<Edge<T>> optimalPath(Graph<T> mst, Graph<T> mpm, Graph<T> original) {
        LinkedList<Edge<T>> edgesOrder = new LinkedList<>();
        LinkedList<Vertex<T>> vertexOrder = new LinkedList<>();

        Vertex<T> lastVertex = mst.getAllVertexes().get(0);
        Edge<T> lastEdge = unvisitedEdge(
                lastVertex.getNeighbourhood().values(),
                mpm.getVertex(lastVertex.getID()).getNeighbourhood().values(),
                edgesOrder);

        while (lastEdge != null) {
            edgesOrder.add(lastEdge);
            vertexOrder.add(lastVertex);

            if (lastEdge.getV1().equals(lastVertex))
                lastVertex = lastEdge.getV2();
            else
                lastVertex = lastEdge.getV1();

            lastEdge = unvisitedEdge(
                    mst.getVertex(lastVertex.getID()).getNeighbourhood().values(),
                    mpm.getVertex(lastVertex.getID()).getNeighbourhood().values(),
                    edgesOrder);
        }

        LinkedList<Vertex<T>> uniqueVertexOrder = new LinkedList<>();

        vertexOrder.stream()
                .filter(v -> !uniqueVertexOrder.contains(v))
                .forEach(uniqueVertexOrder::add);

        return getRouteBetweenVertexes(uniqueVertexOrder, original);
    }

    private Edge<T> unvisitedEdge(Collection<Edge<T>> edges1, Collection<Edge<T>> edges2, List<Edge<T>> visited) {
        Edge<T> unvisitedEdge = edges1.stream()
                .filter(e -> !visited.contains(e))
                .findFirst()
                .orElse(null);

        if (unvisitedEdge != null)
            return unvisitedEdge;

        unvisitedEdge = edges2.stream()
                .filter(e -> !visited.contains(e))
                .findFirst()
                .orElse(null);

        return unvisitedEdge;
    }

    private T pathLength(List<Edge<T>> path) throws WeightTypeNotSupported {
        if (path.size() == 0)
            return (T) Integer.valueOf(0);

        T someWeight = path.get(0).getWeight();

        if (someWeight instanceof Integer)
            return (T) Integer.valueOf(path.stream()
                    .mapToInt(e -> e.getWeight().intValue())
                    .sum());

        if (someWeight instanceof Long)
            return (T) Long.valueOf(path.stream()
                    .mapToLong(e -> e.getWeight().intValue())
                    .sum());

        if (someWeight instanceof Double)
            return (T) Double.valueOf(path.stream()
                    .mapToDouble(e -> e.getWeight().intValue())
                    .sum());

        throw new WeightTypeNotSupported("Type is not supported: " + someWeight.getClass() + ".");
    }

    private List<Edge<T>> getRouteBetweenVertexes(LinkedList<Vertex<T>> vertexes, Graph<T> graph) {
        LinkedList<Edge<T>> route = new LinkedList<>();

        Iterator<Vertex<T>> iter = vertexes.iterator();

        Vertex<T> currentVertex = iter.next();
        while (iter.hasNext()) {
            Vertex<T> nextVertex = iter.next();
            route.add(graph.getVertex(currentVertex.getID()).getEdge(nextVertex));
            currentVertex = nextVertex;
        }
        route.add(graph
                .getVertex(currentVertex.getID())
                .getEdge(vertexes.getFirst()));

        return route;
    }
}
