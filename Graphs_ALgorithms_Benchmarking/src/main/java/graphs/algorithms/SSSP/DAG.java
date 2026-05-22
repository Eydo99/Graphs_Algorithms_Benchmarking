package graphs.algorithms.SSSP;

import graphs.model.Edge;
import graphs.model.Graph;

import java.util.List;
import java.util.Stack;

public class DAG implements  SSSPStrategy{

    @Override
    public int[] computeSSSP(Graph graph,int source) {
        int[] colours = new int[graph.getV()];
        Stack<Integer> stack=new Stack<>();
        if (!topo_sort(graph,colours,stack))
            throw new IllegalArgumentException("Cycle detected");
        int[] dist = new int[graph.getV()];
        for(int i=0;i<graph.getV();i++)
            dist[i]=Integer.MAX_VALUE;
        dist[source]=0;
        while(!stack.isEmpty()){
            int v=stack.pop();

            if(dist[v]==Integer.MAX_VALUE)
                continue;

            for( Edge e: graph.getAdjList().get(v)){
                if(dist[e.v]>dist[v]+e.weight)
                    dist[e.v]=dist[v]+e.weight;
            }
        }
        return dist;
    }

    private boolean topo_sort(Graph graph,int[] colours,Stack<Integer> stack)
    {
        for(int i=0;i< graph.getV();i++)
        {
            if(colours[i]==0)
                if(DFS_Visit(i, graph.getAdjList(),colours,stack)) return false;
        }
        return true;
    }
    private boolean DFS_Visit(int node,List<List<Edge>> adjList,int[] colours, Stack<Integer> stack)
    {
        colours[node]=1;
        for(Edge e:adjList.get(node))
        {
            if(colours[e.v]==1) return true;
            else if(colours[e.v]==0)
                if(DFS_Visit(e.v, adjList,colours,stack)) return true;

        }
        colours[node]=2;
        stack.push(node);
        return false;
    }
}
