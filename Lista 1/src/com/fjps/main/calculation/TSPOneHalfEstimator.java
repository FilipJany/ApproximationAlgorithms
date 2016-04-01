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
 * Implementation of Christofies algorithm. This is 1.5-approximation algorithm for TSP problem.
 * <p>
 * Created by Patryk Stopyra on 22/03/16.
 */
public class TSPOneHalfEstimator<T extends Number> implements TravellingSalesmanProblemSolver<T> {

    private List<Edge<T>> lastOptimalPath;

    @Override
    public T calculateOptimum(Graph<T> graph) {
        if (graph.getAllVertexes().size() <= 1)
            throw new IllegalArgumentException("Cannot calculate TSP path for single node. I am not dumb.");

        Graph<T> mst = minimumSpanningTree(graph);
        Graph<T> mpm = minimumPerfectMatching(indicedOddNeighbourhood(mst, graph));

        lastOptimalPath = optimalPath(mst, mpm, graph);

        return pathLength(lastOptimalPath);
    }

    @Override
    public String getPathAsString() {
        StringBuilder builder = new StringBuilder("path:");

        List<Vertex<T>> vertices = getVerticesFromRoute(lastOptimalPath);

        vertices.stream()
                .forEach(v -> builder.append(" ").append(v.getID()));

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
                    .filter(odd::hasVertex)
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

        for (Vertex<T> currentVertex : graph.getAllVertexes()) {
            if (matching.getVertex(currentVertex.getID()).getNeighbourhood().size() == 0) {
                Edge<T> shortestEdge = null;

                for (Edge<T> e : currentVertex.getNeighbourhood().values())
                    if (matching.getVertex(e.getOtherVertex(currentVertex).getID()).getNeighbourhood().size() == 0
                            && (shortestEdge == null || shortestEdge.compareTo(e) > 0))
                        shortestEdge = e;

                if (shortestEdge != null)
                    matching.connect(
                            currentVertex,
                            shortestEdge.getOtherVertex(currentVertex),
                            shortestEdge.getWeight());
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
        List<Edge<T>> edgesOrder = searchFullPath(
                mst.getAllVertexes().get(0),
                new LinkedList<>(), mst, mpm);
        List<Vertex<T>> verticesOrder = getVerticesFromRoute(edgesOrder);

        LinkedList<Vertex<T>> uniqueVertexOrder = new LinkedList<>();

        verticesOrder.stream()
                .filter(v -> !uniqueVertexOrder.contains(v))
                .forEach(uniqueVertexOrder::add);

        return getRouteBetweenVertexes(uniqueVertexOrder, original);
    }

    @SuppressWarnings("unchecked")
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

    private List<Edge<T>> getRouteBetweenVertexes(LinkedList<Vertex<T>> vertices, Graph<T> graph) {
        LinkedList<Edge<T>> route = new LinkedList<>();

        Iterator<Vertex<T>> iter = vertices.iterator();

        Vertex<T> currentVertex = iter.next();
        while (iter.hasNext()) {
            Vertex<T> nextVertex = iter.next();
            route.add(graph.getVertex(currentVertex.getID()).getEdge(nextVertex));
            currentVertex = nextVertex;
        }
        route.add(graph
                .getVertex(currentVertex.getID())
                .getEdge(vertices.getFirst()));

        return route;
    }

    private List<Vertex<T>> getVerticesFromRoute(List<Edge<T>> route) {
        LinkedList<Vertex<T>> vertices = new LinkedList<>();
        Vertex<T> currentVertex;

        Edge<T> first = route.get(0);
        Edge<T> second = route.get(1);
        if (first.getV2().equals(second.getV1())
                || first.getV2().equals(second.getV2()))
            currentVertex = first.getV1();
        else
            currentVertex = first.getV2();

        for (Edge<T> currentEdge : route) {
            vertices.add(currentVertex);
            currentVertex = currentEdge.getOtherVertex(currentVertex);
        }

        return vertices;
    }

    private List<Edge<T>> searchFullPath(Vertex<T> currentVertex, List<Edge<T>> alreadyVisited,
                                         Graph<T> mst, Graph<T> mpm) {
        Collection<Edge<T>> mstNeighbourhood = mst.getVertex(currentVertex.getID()).getNeighbourhood().values();
        Collection<Edge<T>> mpmNeighbourhood = mpm.hasVertex(currentVertex) ?
                mpm.getVertex(currentVertex.getID()).getNeighbourhood().values()
                : new LinkedList<>();
        List<Edge<T>> prospective = unusedEdges(mstNeighbourhood, mpmNeighbourhood, alreadyVisited);

        for (Edge<T> e : prospective) {
            alreadyVisited.add(e);

            List<Edge<T>> resultEdges = searchFullPath(e.getOtherVertex(currentVertex), alreadyVisited, mst, mpm);

            if (resultEdges.size() == mst.getAllEdges().size() + mpm.getAllEdges().size())
                return resultEdges;

            alreadyVisited.remove(alreadyVisited.size() - 1);
        }

        return alreadyVisited;
    }

//    private List<Edge<T>> searchFullPath(Vertex<T> currentVertex1, Vertex<T> currentVertex2, List<Edge<T>> alreadyVisited,
//                                         Graph<T> mst, Graph<T> mpm) {
//        Collection<Edge<T>> mstNeighbourhood = mst.getVertex(currentVertex1.getID()).getNeighbourhood().values();
//        Collection<Edge<T>> mpmNeighbourhood = mpm.hasVertex(currentVertex1) ?
//                mpm.getVertex(currentVertex1.getID()).getNeighbourhood().values()
//                : new LinkedList<>();
//        List<Edge<T>> prospectiveV1 = unusedEdges(mstNeighbourhood, mpmNeighbourhood, alreadyVisited);
//
//        mstNeighbourhood = mst.getVertex(currentVertex2.getID()).getNeighbourhood().values();
//        mpmNeighbourhood = mpm.hasVertex(currentVertex2) ?
//                mpm.getVertex(currentVertex2.getID()).getNeighbourhood().values()
//                : new LinkedList<>();
//        List<Edge<T>> prospectiveV2 = unusedEdges(mstNeighbourhood, mpmNeighbourhood, alreadyVisited);
//
//        if (prospectiveV1.size() == 0 || prospectiveV2.size() == 0)
//            return alreadyVisited;
//
//        Edge<T> decisionV1 = prospectiveV1.get(0);
//        Edge<T> decisionV2 = prospectiveV2.get(0);
//
//        if (decisionV2.getOtherVertex(currentVertex2).equals(decisionV1.getOtherVertex(currentVertex1)))
//            if (prospectiveV2.size() > 1)
//                decisionV2 = prospectiveV2.get(1);
//            else if (prospectiveV1.size() > 1)
//                decisionV1 = prospectiveV1.get(1);
//
//        alreadyVisited.add(0, decisionV1);
//        alreadyVisited.add(decisionV2);
//
//        return searchFullPath(
//                decisionV1.getOtherVertex(currentVertex1),
//                decisionV2.getOtherVertex(currentVertex2),
//                alreadyVisited, mst, mpm);
//    }

    private List<Edge<T>> unusedEdges(Collection<Edge<T>> edges1, Collection<Edge<T>> edges2, List<Edge<T>> visited) {
        LinkedList<Edge<T>> unvisited = new LinkedList<>();

        edges1.stream()
                .filter(e -> !visited.contains(e))
                .forEach(unvisited::add);

        edges2.stream()
                .filter(e -> !visited.contains(e) || (edges1.contains(e) && visited.indexOf(e) == visited.lastIndexOf(e)))
                .forEach(unvisited::add);

        return unvisited;
    }
}
