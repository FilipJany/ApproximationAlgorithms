package com.fjps.main.calculation;

import com.fjps.main.calculation.helpers.Node;
import com.fjps.main.graph.Edge;
import com.fjps.main.graph.Graph;
import com.fjps.main.graph.Vertex;
import java.util.HashSet;


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
        return null;
    }

    public int[] calculate() {
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