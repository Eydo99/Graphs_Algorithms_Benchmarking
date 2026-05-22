package graphs.algorithms.SSSP;

import graphs.model.Edge;
import graphs.model.Graph;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra implements SSSPStrategy{

    @Override
    public int[] computeSSSP(Graph graph,int source) {
        int[] dist=new int[graph.getV()];
        for(int i=0;i<graph.getV();i++){
            dist[i]=Integer.MAX_VALUE;
        }
        dist[source]=0;

        PriorityQueue<int[]> pq=new PriorityQueue<>(Comparator.comparingInt(a->a[1]));
        pq.add(new int[]{source,0});
        while(!pq.isEmpty()){
            int[] curr=pq.poll();
            int v=curr[0];
            int distance=curr[1];
            for (Edge e: graph.getAdjList().get(v)) {
                if(dist[e.v]>distance+e.weight){
                    dist[e.v]=distance+e.weight;
                    pq.add(new int[]{e.v,dist[e.v]});
                }
            }
        }
        return dist;
    }
}
