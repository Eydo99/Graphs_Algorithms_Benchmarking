package graphs.benchmark;

import graphs.model.Graph;

import java.util.Random;

public  class InputGenerator {
    private static  int V;
    private static final int seed=42;

    public static Graph generateSparseGraph() {
        Graph sparse=new Graph(V);
        generateConnectedGraph(sparse);
        int remainingEdges=5*V-(V-1);
        generateRemainingGraph(sparse,remainingEdges);
        return sparse;
    }

    public static Graph generateDenseGraph() {
        Graph dense=new Graph(V);
       generateConnectedGraph(dense);
        int targetedEdges=(int) (0.25*V*(V-1)/2);
        int remainingEdges=targetedEdges-(V-1);
       generateRemainingGraph(dense,remainingEdges);
        return dense;
    }

    public static Graph generateCompleteGraph() {
        Graph complete=new Graph(V);
        Random rand=new Random(seed);
        for(int i=0;i<V;i++)
            for(int j=i+1;j<V;j++)
            {
                int weight=rand.nextInt(1,1001);
                complete.addEdge(i,j,weight);
            }
        return complete;
    }

    public static Graph generateDAG() {
        Graph DAG=new Graph(V);
        Random rand=new Random(seed);

        for(int i=1;i<V;i++)
        {
            int parent=rand.nextInt(i);
            int weight=rand.nextInt(1,1001);
            DAG.addDirectedEdge(parent,i,weight);

        }
        int remainingEdges = 5 * V - (V - 1);
        while(remainingEdges>0)
        {
            int u=rand.nextInt(V);
            int v=rand.nextInt(V);
            if(u>=v)continue;
            int weight=rand.nextInt(1,1001);
            DAG.addDirectedEdge(u,v,weight);
            remainingEdges--;
        }
        return DAG;
    }

    private static void generateConnectedGraph(Graph graph) {
        Random rand=new Random(seed);
        for(int i=1;i<V;i++)
        {
            int parent=rand.nextInt(i);
            int weight=rand.nextInt(1,1001);
            graph.addEdge(parent,i,weight);
        }
    }

    private static void generateRemainingGraph(Graph graph,int remainingEdges) {
        Random rand=new Random(seed);
        while(remainingEdges>0)
        {
            int u=rand.nextInt(V);
            int v=rand.nextInt(V);
            if(u==v)continue;
            int weight=rand.nextInt(1,1001);
            graph.addEdge(u,v,weight);
            remainingEdges--;
        }
    }

    public static void setV(int v) {
        V = v;
    }
}
