package graphs.algorithms.MST;


import graphs.model.Edge;
import graphs.model.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Prim implements MSTStrategy {

    @Override
    public List<Edge> computeMST(Graph graph) {
        int V=graph.getV();
        boolean[] visited=new boolean[graph.getV()];
        int[]     minWeight = new int[graph.getV()];
        int[]     parent    = new int[graph.getV()];
        for (int i = 0; i < V; i++) {
            minWeight[i] = Integer.MAX_VALUE;
            parent[i]    = -1;
        }
        minWeight[0]=0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{0, 0});
        List<Edge> res=new ArrayList<>();

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int v      = curr[0];
            int weight = curr[1];
            if (visited[v]) continue;
            visited[v] = true;
            if (parent[v] != -1)
                res.add(new Edge(parent[v], v, weight));
            for (Edge e : graph.getAdjList().get(v)) {
                if (!visited[e.v] && e.weight < minWeight[e.v]) {
                    minWeight[e.v] = e.weight;
                    parent[e.v]    = v;
                    pq.add(new int[]{e.v, e.weight});
                }
            }
        }
        return res;
    }
}
