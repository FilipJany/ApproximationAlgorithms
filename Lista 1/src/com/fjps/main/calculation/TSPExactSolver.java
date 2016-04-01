package com.fjps.main.calculation;

import com.fjps.main.graph.Edge;
import com.fjps.main.graph.Graph;
import com.fjps.main.graph.Vertex;
import com.fjps.main.graph.exceptions.InvalidVertexIDException;

import java.util.*;


/**
 * Created by Patryk Stopyra on 22/03/16.
 */

public class TSPExactSolver<T extends Number> implements TravellingSalesmanProblemSolver<T>
{
    private Graph<T> graph;
    private int[] path;
    private Random r;

    private int[] getStartingSolution()
    {
        int[] newList = new int[path.length];
        for (int i = 0; i < newList.length; ++i)
            newList[i] = i;
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < newList.length; ++i)
        {
            int i1 = r.nextInt(newList.length);
            int i2 = r.nextInt(newList.length);
            int temp = newList[i1];
            newList[i1] = newList[i2];
            newList[i2] = temp;
        }
        return newList;
    }

    private Double P(Double x0, Double x, Double temp)
    {
        return Math.min(1.0, Math.exp((-1)*(x - x0) / temp));
    }

    private int[] swapIndexes(int[] master, int x, int y)
    {
        int i1 = x;
        int i2 = y;
        if(i2 < i1)
        {
            int temp = i1;
            i2 = i1;
            i1 = temp;
        }
        List<Integer> newList = new ArrayList<>();
        for (int l = 0; l < i1; ++l)
            newList.add(master[l]);
        for (int l = i2; l >= i1; --l)
            newList.add(master[l]);
        for (int l = i2+1; l < master.length; ++l)
            newList.add(master[l]);
        int[] listToReturn = new int[newList.size()];
        for (int i = 0; i < listToReturn.length; ++i)
            listToReturn[i] = newList.get(i);
        return listToReturn;
    }

    private int[] getNeighbor(int[] master)
    {
        int i1 = r.nextInt(master.length);
        int i2 = r.nextInt(master.length);
        while ( i1 == i2)
        {
            i2 = r.nextInt(master.length);
        }
        return swapIndexes(master, i1, i2);
    }

    private int getIdFromString(Vertex<T> v)
    {
        return Integer.parseInt(v.getID().substring(1));
    }

    private Double getLength(int[] array)
    {
        Double sum = 0.0;
        List<Vertex<T>> verticles = graph.getAllVertexes();
        List<Edge<T>> edges = new LinkedList<>(graph.getAllEdges());
        for (int i = 0; i < array.length-1; ++i)
        {
            for (Edge<T> e : edges)
            {
                int v1 = getIdFromString(e.getV1());
                int v2 = getIdFromString(e.getV2());
                if(v1 == array[i] && v2 == array[i+1])
                    sum += (Double) e.getWeight();
                if(v1 == array[i+1] && v2 == array[i])
                    sum += (Double)e.getWeight();
            }
        }
        for (Edge<T> e : edges)
        {
            int v1 = getIdFromString(e.getV1());
            int v2 = getIdFromString(e.getV2());
            if(v1 == array[0] && v2 == array[array.length-1])
                sum += (Double)e.getWeight();
            if(v1 == array[array.length-1] && v2 == array[0])
                sum += (Double)e.getWeight();
        }
        return sum;
    }

    private void solve(Double startTemp, Double delta, int iterations)
    {
        int[] x0 = getStartingSolution();
        int[] xopt = x0.clone();
        int[] x = x0.clone();
        double temp = startTemp;
        int iter = 0;

        while(iter < iterations)
        {
            x = getNeighbor(x0);
            Double x0Len = getLength(x0);
            Double xLen = getLength(x);
            if((r.nextDouble() % 1.0) < P(x0Len, xLen, temp))
            {
                x0 = x.clone();
                if(getLength(x0) < getLength(xopt))
                {
                    xopt = x0.clone();
                }
            }
            temp *= delta;
            ++iter;
        }
        path = xopt.clone();
    }

    @Override
    public T calculateOptimum(Graph<T> graph)
    {
        r = new Random(System.currentTimeMillis());
        this.graph = graph;
        path = new int[graph.getNumberVertexes()];
        solve(10000.0, 0.999, 100000);
        return  (T)getLength(path);
    }

    @Override
    public String getPathAsString()
    {
        StringBuilder builder = new StringBuilder("path: ");
        for (int i = 0; i < path.length; ++i)
            builder.append("V").append(path[i]).append(" ");
        return  builder.toString();
    }

    @Override
    public List<Edge<T>> getLastOptimalPath()
    {
        return  null;
    }
}