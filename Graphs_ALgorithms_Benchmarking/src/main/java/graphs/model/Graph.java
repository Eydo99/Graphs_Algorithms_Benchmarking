package graphs.model;

import graphs.algorithms.MST.MSTStrategy;
import graphs.algorithms.MST.Prim;

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
        Edge e = new Edge(u, v, weight);
        edges.add(e);
        adjList.get(u).add(e);
        adjList.get(v).add(e);
    }
    public void addDirectedEdge(int u, int v, int weight) {
        Edge e = new Edge(u, v, weight);
        edges.add(e);
        adjList.get(u).add(e);
    }

    public List<Edge> PrimMST() {
        Prim prim = new Prim();
        List<Edge> res=prim.computeMST()
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
