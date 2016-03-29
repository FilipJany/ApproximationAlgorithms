package com.fjps.main.calculation;

import com.fjps.main.calculation.helpers.Node;
import com.fjps.main.graph.Edge;
import com.fjps.main.graph.Graph;
import com.fjps.main.graph.Vertex;

import java.util.*;


/**
 * Created by Patryk Stopyra on 22/03/16.
 */
public class TSPExactSolver<T extends Number> implements TravellingSalesmanProblemSolver<T>
{
    private Graph<Double> g;
    private double[][] distances;
    double best_cost;
    int[] best_path;

    public TSPExactSolver(Graph<Double> g)
    {
        this.g = g;
        distances = new double[g.getAllVertexes().size()][g.getAllVertexes().size()];
        for (int i = 0; i < distances.length; ++i)
            for (int j = 0; j < distances.length; ++j)
                distances[i][j] = Double.POSITIVE_INFINITY;
        populateMatrix();
    }

    @Override
    public T calculateOptimum(Graph<T> graph) {
        Double cost = 0.0;
        int[] path = calculate();
        for (int i = 0; i < path.length-1; i++)
            cost += distances[path[i]][path[i+1]];
        cost += distances[path[path.length-1]][path[0]];
        return (T) Double.valueOf(cost);
    }

    @Override
    public List<Edge<T>> getLastOptimalPath()
    {
//        List<Edge<T>> edges = new LinkedList<>();
//        TreeSet<Edge<T>> graphedges = (TreeSet<Edge<T>>)g.getAllEdges(); //TODO nie umiem tego zrobic i chuj
//        for (int i = 0; i < best_path.length-1; i++)
//        {
//            for (Edge<T> e : graphedges)
//            {
//                if(Integer.parseInt(e.getV1().getID().substring(1)) == best_path[i] || Integer.parseInt(e.getV2().getID().substring(1)) == best_path[i+1])
//                {
//                    edges.add(e);
//                }
//                else if(Integer.parseInt(e.getV2().getID().substring(1)) == best_path[i] || Integer.parseInt(e.getV1().getID().substring(1)) == best_path[i+1])
//                {
//                    edges.add(e);
//                }
//            }
//        }
//        for (Edge<T> e : graphedges)
//        {
//            if(Integer.parseInt(e.getV1().getID().substring(1)) == best_path[best_path.length-1] || Integer.parseInt(e.getV2().getID().substring(1)) == best_path[0])
//            {
//                edges.add(e);
//            }
//            else if(Integer.parseInt(e.getV2().getID().substring(1)) == best_path[0] || Integer.parseInt(e.getV1().getID().substring(1)) == best_path[best_path.length-1])
//            {
//                edges.add(e);
//            }
//        }
        return  null;
    }

    public int[] getBestPath()
    {
        return best_path;
    }

    private int[] calculate() {
        HashSet<Integer> location_set = new HashSet<Integer>(distances.length);
        for(int i = 0; i < distances.length; i++)
            location_set.add(i);

        best_cost = findGreedyCost(0, location_set, distances);

        int[] active_set = new int[distances.length];
        for(int i = 0; i < active_set.length; i++)
            active_set[i] = i;

        Node root = new Node(null, 0, distances, active_set, 0);
        traverse(root);

        return best_path;
    }

    public double getCost() {
        return best_cost;
    }

    /**
     * Method founds cost by greedy algorithm - to check in B&B is correct
     * @param i
     * @param location_set
     * @param distances
     * @return
     */
    private double findGreedyCost(int i, HashSet<Integer> location_set, double[][] distances) {
        if(location_set.isEmpty())
            return distances[0][i];

        location_set.remove(i);

        double lowest = Double.MAX_VALUE;
        int closest = 0;
        for(int location : location_set) {
            double cost = distances[i][location];
            if(cost < lowest) {
                lowest = cost;
                closest = location;
            }
        }

        return lowest + findGreedyCost(closest, location_set, distances);
    }

    /**Traverses nodes in order to find proper (best) solution
     *
     * @param parent - node's parent
     */
    private void traverse(Node parent) {
        Node[] children = parent.generateChildren();

        for(Node child : children) {
            if(child.isTerminal()) {
                double cost = child.getPathCost();
                if(cost < best_cost) {
                    best_cost = cost;
                    best_path = child.getPath();
                }
            }
            else if(child.getLowerBound() <= best_cost) {
                traverse(child);
            }
        }
    }

    /**Populates distances matrix with given costs
     *
     */
    private void populateMatrix()
    {
        if(distances.length != 0)
        {
            for (int i = 0; i < g.getAllVertexes().size(); ++i)
            {
                Vertex<Double> vertex = g.getAllVertexes().get(i);
                for (Edge<Double> edge: vertex.getNeighbourhood().values())
                {
                    int index1 = Integer.parseInt(edge.getV1().getID().substring(1));
                    int index2 = Integer.parseInt(edge.getV2().getID().substring(1));
                    distances[index1][index2] = edge.getWeight();
                    distances[index2][index1] = edge.getWeight();
                }
            }
        }
    }
}