package graphs;

import graphs.model.Edge;
import graphs.model.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphAlgorithmsTest {

    // ─── MST Tests ───────────────────────────────────────────────────────────────

    Graph undirected;

    @BeforeEach
    void setup() {
        // Simple undirected weighted graph:
        //   0 -1- 1
        //   |     |
        //   4     2
        //   |     |
        //   3 -3- 2
        //   (0-3 weight 4, 0-1 weight 1, 1-2 weight 2, 2-3 weight 3)
        undirected = new Graph(4);
        undirected.addEdge(0, 1, 1);
        undirected.addEdge(1, 2, 2);
        undirected.addEdge(2, 3, 3);
        undirected.addEdge(0, 3, 4);
    }

    // ── Prim ─────────────────────────────────────────────────────────────────────

    @Test
    void primMST_edgeCount() {
        List<Edge> mst = undirected.primMST();
        // MST of V vertices always has V-1 edges
        assertEquals(3, mst.size());
    }

    @Test
    void primMST_totalWeight() {
        List<Edge> mst = undirected.primMST();
        int total = mst.stream().mapToInt(e -> e.weight).sum();
        // Optimal MST: edges (0,1,1) + (1,2,2) + (2,3,3) = 6
        assertEquals(6, total);
    }

    @Test
    void primMST_singleVertex() {
        Graph g = new Graph(1);
        List<Edge> mst = g.primMST();
        assertTrue(mst.isEmpty());
    }

    @Test
    void primMST_twoVertices() {
        Graph g = new Graph(2);
        g.addEdge(0, 1, 5);
        List<Edge> mst = g.primMST();
        assertEquals(1, mst.size());
        assertEquals(5, mst.get(0).weight);
    }

    // ── Kruskal ───────────────────────────────────────────────────────────────────

    @Test
    void kruskalMST_edgeCount() {
        List<Edge> mst = undirected.kruskalMST();
        assertEquals(3, mst.size());
    }

    @Test
    void kruskalMST_totalWeight() {
        List<Edge> mst = undirected.kruskalMST();
        int total = mst.stream().mapToInt(e -> e.weight).sum();
        assertEquals(6, total);
    }

    @Test
    void kruskalMST_singleVertex() {
        Graph g = new Graph(1);
        List<Edge> mst = g.kruskalMST();
        assertTrue(mst.isEmpty());
    }

    @Test
    void kruskalMST_twoVertices() {
        Graph g = new Graph(2);
        g.addEdge(0, 1, 7);
        List<Edge> mst = g.kruskalMST();
        assertEquals(1, mst.size());
        assertEquals(7, mst.get(0).weight);
    }

    @Test
    void primAndKruskalAgree() {
        // Both algorithms must produce the same total MST weight
        int primWeight   = undirected.primMST().stream().mapToInt(e -> e.weight).sum();
        int kruskalWeight = undirected.kruskalMST().stream().mapToInt(e -> e.weight).sum();
        assertEquals(primWeight, kruskalWeight);
    }

    // ─── Dijkstra Tests ───────────────────────────────────────────────────────────

    @Test
    void dijkstra_sourceIsZero() {
        int[] dist = undirected.dijkstra(0);
        assertEquals(0, dist[0]);
        assertEquals(1, dist[1]);
        assertEquals(3, dist[2]);
        assertEquals(4, dist[3]); // 0->1->2->3 = 6, but 0->3 = 4
    }

    @Test
    void dijkstra_sourceIsOne() {
        int[] dist = undirected.dijkstra(1);
        assertEquals(1, dist[0]);
        assertEquals(0, dist[1]);
        assertEquals(2, dist[2]);
        assertEquals(5, dist[3]);
    }

    @Test
    void dijkstra_singleVertex() {
        Graph g = new Graph(1);
        int[] dist = g.dijkstra(0);
        assertEquals(0, dist[0]);
    }

    @Test
    void dijkstra_linearGraph() {
        // 0 -1- 1 -1- 2 -1- 3
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);
        int[] dist = g.dijkstra(0);
        assertArrayEquals(new int[]{0, 1, 2, 3}, dist);
    }

    // ─── DAG Shortest Path Tests ──────────────────────────────────────────────────

    @Test
    void dag_simpleDAG() {
        // 0 -> 1 (weight 1)
        // 0 -> 2 (weight 4)
        // 1 -> 2 (weight 2)
        // 1 -> 3 (weight 5)
        // 2 -> 3 (weight 1)
        Graph dag = new Graph(4);
        dag.addDirectedEdge(0, 1, 1);
        dag.addDirectedEdge(0, 2, 4);
        dag.addDirectedEdge(1, 2, 2);
        dag.addDirectedEdge(1, 3, 5);
        dag.addDirectedEdge(2, 3, 1);

        int[] dist = dag.dagShortestPath(0);
        assertEquals(0, dist[0]);
        assertEquals(1, dist[1]);
        assertEquals(3, dist[2]); // 0->1->2 = 3
        assertEquals(4, dist[3]); // 0->1->2->3 = 4
    }

    @Test
    void dag_sourceNotZero() {
        Graph dag = new Graph(3);
        dag.addDirectedEdge(0, 1, 10);
        dag.addDirectedEdge(1, 2, 5);

        int[] dist = dag.dagShortestPath(1);
        // node 0 is unreachable from source 1
        assertEquals(Integer.MAX_VALUE, dist[0]);
        assertEquals(0, dist[1]);
        assertEquals(5, dist[2]);
    }

    @Test
    void dag_throwsOnCycle() {
        Graph cyclic = new Graph(3);
        cyclic.addDirectedEdge(0, 1, 1);
        cyclic.addDirectedEdge(1, 2, 1);
        cyclic.addDirectedEdge(2, 0, 1); // cycle!

        assertThrows(IllegalArgumentException.class, () -> cyclic.dagShortestPath(0));
    }

    @Test
    void dag_singleVertex() {
        Graph g = new Graph(1);
        int[] dist = g.dagShortestPath(0);
        assertEquals(0, dist[0]);
    }

    @Test
    void dag_andDijkstraAgreeOnDAG() {
        // On a DAG with positive weights both algorithms should give the same distances
        Graph dag1 = new Graph(4);
        dag1.addDirectedEdge(0, 1, 2);
        dag1.addDirectedEdge(0, 2, 6);
        dag1.addDirectedEdge(1, 2, 3);
        dag1.addDirectedEdge(1, 3, 8);
        dag1.addDirectedEdge(2, 3, 1);

        Graph dag2 = new Graph(4);
        dag2.addDirectedEdge(0, 1, 2);
        dag2.addDirectedEdge(0, 2, 6);
        dag2.addDirectedEdge(1, 2, 3);
        dag2.addDirectedEdge(1, 3, 8);
        dag2.addDirectedEdge(2, 3, 1);

        int[] dagDist      = dag1.dagShortestPath(0);
        int[] dijkstraDist = dag2.dijkstra(0);

        assertArrayEquals(dijkstraDist, dagDist);
    }
}