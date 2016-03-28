package com.fjps.main.graph;

import com.fjps.main.graph.exceptions.InvalidVertexIDException;
import com.fjps.main.graph.exceptions.NoSuchVertexException;
import com.fjps.main.graph.exceptions.NoVerticesException;
import com.fjps.main.graph.exceptions.VertexDuplicateException;

import java.util.*;

/**
 * Class of whole graph. I assume that graph is not directed.
 * Since it should be metric too it contains methood <code>assureMetric()</code>.
 * <p>
 * Every vertex has its own, unique id.
 * It can be managed from the <code>Graph</code> object by id or by <code>Vertex</code> object itself.
 * <p>
 * Created by Patryk Stopyra on 17/03/16.
 */
public class Graph<T extends Number> {

    private final HashMap<String, Vertex<T>> vertexes;
    private final TreeSet<Edge<T>> edges; //sth about edge.equals may occur problematic, but should not!

    public Graph() {
        vertexes = new HashMap<>();
        edges = new TreeSet<>();

    }

    public Graph(int vertexAmount)
    {
        vertexes = new HashMap<>();
        edges = new TreeSet<>();
        for (int i = 0; i < vertexAmount; ++i)
            addVertex();
    }

    public Vertex<T> addVertex() {
        Vertex<T> v;
        int diff = 0;

        do {
            v = new Vertex<>(vertexes.size() + diff++);
        } while (vertexes.containsKey(v.getID()));

        vertexes.put(v.getID(), v);
        return v;
    }

    public Vertex<T> addVertex(String id) throws InvalidVertexIDException, VertexDuplicateException {
        if (id.trim().equals(""))
            throw new InvalidVertexIDException("<some white characters>");
        if (vertexes.containsKey(id))
            throw new VertexDuplicateException(id);

        Vertex<T> v = new Vertex<>(id);

        vertexes.put(id, v);
        return v;
    }

    public void removeVertex(Vertex<T> v) throws InvalidVertexIDException, NoSuchVertexException {
        if (!hasVertex(v))
            throw new NoSuchVertexException(v);

        v = vertexes.get(v.getID()); // to ensure that v is a vertex from a graph, not an alien clone

        for (Vertex<T> neighbour : new HashSet<>(v.getNeighbourhood().keySet()))
            v.removeConnectionTo(neighbour);
        vertexes.remove(v.getID());
    }

    public void removeVertex(String id) throws NoSuchVertexException {
        if (!vertexes.containsKey(id))
            throw new NoSuchVertexException(id);

        removeVertex(vertexes.get(id));
    }

    public boolean hasVertex(Vertex<T> v) throws InvalidVertexIDException {
        if (v == null)
            throw new InvalidVertexIDException(null);

        return vertexes.containsKey(v.getID());
    }

    public boolean hasVertex(String id) {
        return vertexes.containsKey(id);
    }

    public Vertex<T> getVertex(String id) {
        return vertexes.get(id);
    }

    public List<Vertex<T>> getAllVertexes() {
        return new LinkedList<>(vertexes.values());
    }

    public int getNumberVertexes() {
        return vertexes.size();
    }

    public TreeSet<Edge<T>> getAllEdges() {
        return new TreeSet<>(edges);
    }

    public int getNumberEdges() {
        return edges.size();
    }

    public boolean isConnected(Vertex<T> v1, Vertex<T> v2) {
        if (!hasVertex(v1))
            throw new NoSuchVertexException(v1);
        if (!hasVertex(v2))
            throw new NoSuchVertexException(v2);

        v1 = vertexes.get(v1.getID());
        v2 = vertexes.get(v2.getID());

        return v1.isReachable(v2);
    }

    public void connect(Vertex<T> v1, Vertex<T> v2, T distance) throws NoSuchVertexException {
        if (!hasVertex(v1))
            throw new NoSuchVertexException(v1);
        if (!hasVertex(v2))
            throw new NoSuchVertexException(v2);

        v1 = vertexes.get(v1.getID());
        v2 = vertexes.get(v2.getID());

        Edge<T> newEdge = v1.addConnectionTo(v2, distance);
        edges.add(newEdge);
    }

    public void disconnect(Vertex<T> v1, Vertex<T> v2) throws NoSuchVertexException {
        if (!hasVertex(v1))
            throw new NoSuchVertexException(v1);
        if (!hasVertex(v2))
            throw new NoSuchVertexException(v2);

        v1 = vertexes.get(v1.getID());
        v2 = vertexes.get(v2.getID());

        Edge<T> removedEdge = v1.removeConnectionTo(v2);
        edges.remove(removedEdge);
    }

    public void assureMetric() {
        //TODO 1. Graf MUSI być spójny - patrz pkt. 2.
        //TODO 2. upewnić się, że graf jest pełny - generator generuje tylko takie
        // (uzupełnić brakujące krawędzie najkrótszymi ścieżkami pomiędzy parami wierzchołków)
        //TODO 3. dla każdej pary wierzchołków krawędź pomiędzy nimi powinna być równa najkrótszej ścieżce. - done
        // (Jeżeli bezpośrednia krawędź jest dłuższa od najkrótszej ścieżki - należy zmienić wartość krawędzi na wartość
        // najkrótszej ścieżki)

        // Metoda powinna być wywoływana po dodaniu wszystkich wierzchołków.
        System.out.println("Updating edge values...");
        for (Vertex v: getAllVertexes())
        {
            Dijkstra.computePaths(v);
            for (Object e : v.getNeighbourhood().values())
            {
                if(((Edge)(e)).getV2() != v)
                    ((Edge)(e)).updateWeight(((Edge)(e)).getV2().getMinDistance());
                if(((Edge)(e)).getV1() != v)
                    ((Edge)(e)).updateWeight(((Edge)(e)).getV1().getMinDistance());
            }
            resetRound();
        }
        System.out.println("Successfully updated edges");
        System.out.println(this);
    }

    private void resetRound()
    {
        for (Vertex v : getAllVertexes())
            v.setMinDistance(Double.POSITIVE_INFINITY);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Graph object containing:\n");

        if (vertexes.size() <= 16)
            vertexes.values().stream()
                    .forEach(v -> builder.append(v).append("\n"));
        else
            vertexes.keySet().stream()
                    .forEach(id -> builder.append(", ").append(id));


        return builder.toString();
    }
}
