package graphs.model;

import graphs.algorithms.MST.Kruskal;
import graphs.algorithms.MST.MSTStrategy;
import graphs.algorithms.MST.Prim;
import graphs.algorithms.SSSP.DAG;
import graphs.algorithms.SSSP.Dijkstra;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    int V;
    List<Edge> edges;
    List<List<Edge>> adjList;


    public Graph(int V) {
        this.V = V;
        this.edges = new ArrayList<>();
        this.adjList = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }
    }



    public void addEdge(int u, int v, int weight) {
        Edge e1 = new Edge(u, v, weight);
        edges.add(e1);
        adjList.get(u).add(e1);
        Edge e2 = new Edge(v, u, weight);
        adjList.get(v).add(e2);
    }
    public void addDirectedEdge(int u, int v, int weight) {
        Edge e = new Edge(u, v, weight);
        edges.add(e);
        adjList.get(u).add(e);
    }

    public List<Edge> primMST() {
        Prim prim = new Prim();
        return prim.computeMST(this);
    }
    public List<Edge> kruskalMST() {
        Kruskal kruskal = new Kruskal();
        return kruskal.computeMST(this);
    }

    public int[] dijkstra(int source) {
        Dijkstra dijkstra = new Dijkstra();
        return dijkstra.computeSSSP(this,source);
    }

    public int[] dagShortestPath(int source) {
        DAG dag = new DAG();
        return dag.computeSSSP(this,source);
    }

    public int getV() {
        return V;
    }


    public List<Edge> getEdges() {
        return edges;
    }

    public List<List<Edge>> getAdjList() {
        return adjList;
    }
}
