package com.fjps.main.graph;

import com.fjps.main.graph.exceptions.NoVerticesException;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by fifi on 26.03.2016.
 */
public class Generator
{
    private static int iterator = 0;

    private Graph<Double> g;
    private List<Vertex<Double>> verticles;
    private final int graphSize;

    public Generator(int graphSize)
    {
        g = new Graph();
        verticles = new LinkedList<>();
        this.graphSize = graphSize;
    }

    public void generateVertices()
    {
        for (int i = 0; i < graphSize; ++i)
        {
            Vertex<Double> vertex = g.addVertex();
            verticles.add(vertex);
        }
    }

    public void generateEdges()  throws NoVerticesException
    {
        if(g.getAllVertexes().size() == 0)
            throw new NoVerticesException();
        else
        {
            Random rand = new Random(System.currentTimeMillis() * (iterator % 10 + 1));
            for (int i = 0; i < graphSize-1; ++i)
                for (int j = i+1; j < graphSize; ++j)
                {
                    g.connect(verticles.get(i), verticles.get(j), (rand.nextDouble()*rand.nextInt(50)) + rand.nextDouble());
                    iterator++;
                }
        }
    }

    public Graph<Double> getG() {
        return g;
    }
}
