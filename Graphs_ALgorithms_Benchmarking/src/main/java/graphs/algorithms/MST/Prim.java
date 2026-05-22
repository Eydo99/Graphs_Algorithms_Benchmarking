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
        boolean[] visited=new boolean[graph.getV()];
        PriorityQueue<Edge> pq=new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        List<Edge> res=new ArrayList<>();
        pq.add(new Edge(-1,0,0));

        while (!pq.isEmpty()) {
            Edge e=pq.poll();
            if(!visited[e.v]){
                visited[e.v]=true;
                if(e.u!=-1) res.add(e);
                for( Edge edge : graph.getAdjList().get(e.v) ) {
                    if(!visited[edge.v]){pq.add(edge);}
                }
            }

        }
        return res;
    }
}
