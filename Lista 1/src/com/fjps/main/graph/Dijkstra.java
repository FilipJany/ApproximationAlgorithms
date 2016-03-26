package com.fjps.main.graph;

import java.util.*;

/**
 * Created by fifi on 26.03.2016.
 */
public class Dijkstra
{
    public static void computePaths(Vertex source)
    {
        source.setMinDistance(0.0);
        PriorityQueue<Vertex<Double>> vertexQueue = new PriorityQueue<Vertex<Double>>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex<Double> u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge<Double> e : u.getNeighbourhood().values())
            {
                Vertex<Double> v = e.getV2();
                if(v == u)
                    v = e.getV1();

                double weight = e.getWeight();
                double distanceThroughU = u.getMinDistance() + weight;
                if (distanceThroughU < v.getMinDistance()) {
                    vertexQueue.remove(u);
                    v.setMinDistance(distanceThroughU);
                    v.setPrevious(u);
                    vertexQueue.add(v);
                }

            }
        }
    }

    public static List<Vertex<Double>> getShortestPathTo(Vertex<Double> target)
    {
        List<Vertex<Double>> path = new ArrayList<Vertex<Double>>();
        for (Vertex<Double> vertex = target; vertex != null; vertex = vertex.getPrevious())
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }
}
