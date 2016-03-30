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

    @Override
    public String getPathAsString() {
        StringBuilder builder = new StringBuilder("path: ");

        Iterator<Edge<T>> iter = lastOptimalPath.iterator();

        Edge<T> currentEdge = iter.next();
        while (iter.hasNext()) {
            Edge<T> nextEdge = iter.next();

            if (currentEdge.getV1().equals(nextEdge.getV1()) || currentEdge.getV1().equals(nextEdge.getV2()))
                builder.append(currentEdge.getV1().getID()).append(" ");
            else
                builder.append(currentEdge.getV2().getID()).append(" ");

            currentEdge = nextEdge;
        }
        if (currentEdge.getV1().equals(lastOptimalPath.get(0).getV1())
                || currentEdge.getV1().equals(lastOptimalPath.get(0).getV2()))
            builder.append(currentEdge.getV1().getID()).append(" ");
        else
            builder.append(currentEdge.getV2().getID()).append(" ");

        return builder.toString();
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
                    .map(neighbour -> original.getVertex(v.getID()).getEdge((Vertex) neighbour))
                    .forEach(edge -> odd.connect(((Edge<T>) edge).getV1(), ((Edge<T>) edge).getV2(), ((Edge<T>) edge).getWeight()));

        return odd;
    }

    /**
     * Algorithm simplified to find perfect matching and use some basic heuristic to minimize it.
     * <p>
     * Full, polynomial-time exact algorithm is described at:
     * <a href="https://courses.engr.illinois.edu/cs598csc/sp2010/Lectures/Lecture11.pdf">Illinois University Site</a>.
     *
     * @param graph graph to be used in perfect matching seek
     * @return New graph containing all <code>graph</code>'s vertices and only these edges that are used in matching.
     */
    private Graph<T> minimumPerfectMatching(Graph<T> graph) {
        Graph<T> matching = new Graph<>();

        graph.getAllVertexes().stream()
                .map(Vertex::getID)
                .forEach(matching::addVertex);

        Iterator<Vertex<T>> iter = graph.getAllVertexes().iterator();

        while (iter.hasNext()) {
            Vertex<T> currentVertex = iter.next();

            if (matching.getVertex(currentVertex.getID()).getNeighbourhood().size() == 0) {
                Edge<T> shortestEdge = null;

               for (Edge<T> e : currentVertex.getNeighbourhood().values())
                   if (shortestEdge == null || shortestEdge.compareTo(e) > 0)
                       shortestEdge = e;

                Vertex<T> nearestVertex = shortestEdge.getOtherVertex(currentVertex);
                matching.connect(currentVertex, nearestVertex, shortestEdge.getWeight());
            }
        }

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
        Collection<Edge<T>> mpmNeighbourhood = mpm.hasVertex(lastVertex) ?
                mpm.getVertex(lastVertex.getID()).getNeighbourhood().values()
                : new LinkedList<>();
        Edge<T> lastEdge = unvisitedEdge(
                lastVertex.getNeighbourhood().values(),
                mpmNeighbourhood,
                edgesOrder);

        while (lastEdge != null) {
            edgesOrder.add(lastEdge);
            vertexOrder.add(lastVertex);

            if (lastEdge.getV1().equals(lastVertex))
                lastVertex = lastEdge.getV2();
            else
                lastVertex = lastEdge.getV1();

            mpmNeighbourhood = mpm.hasVertex(lastVertex) ?
                    mpm.getVertex(lastVertex.getID()).getNeighbourhood().values()
                    : new LinkedList<>();
            lastEdge = unvisitedEdge(
                    mst.getVertex(lastVertex.getID()).getNeighbourhood().values(),
                    mpmNeighbourhood,
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

        if (someWeight instanceof Double)
            return (T) Double.valueOf(path.stream()
                    .mapToDouble(e -> e.getWeight().doubleValue())
                    .sum());

        if (someWeight instanceof Long)
            return (T) Long.valueOf(path.stream()
                    .mapToLong(e -> e.getWeight().longValue())
                    .sum());

        if (someWeight instanceof Integer)
            return (T) Integer.valueOf(path.stream()
                    .mapToInt(e -> e.getWeight().intValue())
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
