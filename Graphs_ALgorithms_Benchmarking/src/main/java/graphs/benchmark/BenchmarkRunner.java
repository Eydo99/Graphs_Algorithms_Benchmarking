package graphs.benchmark;

import graphs.model.Graph;

public class BenchmarkRunner {

    private static final int RUNS = 10;

    public static void main(String[] args) {

        System.out.println("Generating graphs...");
        Graph sparse   = InputGenerator.generateSparseGraph();
        Graph dense    = InputGenerator.generateDenseGraph();
        Graph complete = InputGenerator.generateCompleteGraph();
        Graph dag      = InputGenerator.generateDAG();
        System.out.println("Graphs ready.\n");

        // ── MST: Prim vs Kruskal ─────────────────────────────────────────────────
        printSectionHeader("MST BENCHMARKS — Prim vs Kruskal");
        printMSTRow("Sparse",   sparse);
        printMSTRow("Dense",    dense);
        printMSTRow("Complete", complete);

        // ── SSSP: Dijkstra on all topologies ─────────────────────────────────────
        printSectionHeader("SSSP BENCHMARKS — Dijkstra (source = 0)");
        printDijkstraRow("Sparse",   sparse);
        printDijkstraRow("Dense",    dense);
        printDijkstraRow("Complete", complete);
        printDijkstraRow("DAG",      dag);

        // ── SSSP: Dijkstra vs DAG on DAG topology ────────────────────────────────
        printSectionHeader("SSSP BENCHMARKS — Dijkstra vs DAG Shortest Path (µs)");

        long[] dijkstraTimes = measureDijkstraMicro(dag);
        long[] dagTimes      = measureDAGMicro(dag);

        double dijkstraMean   = Stats.mean(dijkstraTimes);
        double dagMean        = Stats.mean(dagTimes);
        double speedupMean    = dijkstraMean / dagMean;

        double dijkstraMedian = Stats.median(dijkstraTimes);
        double dagMedian      = Stats.median(dagTimes);
        double speedupMedian  = dijkstraMedian / dagMedian;

        printTableHeader("Algorithm", "Mean (µs)", "Median (µs)", "Std Dev (µs)", "Speedup");
        printTableRow("Dijkstra on DAG",  dijkstraMean,  dijkstraMedian,  Stats.standardDeviation(dijkstraTimes), "—");
        printTableRow("DAG Shortest Path", dagMean,      dagMedian,       Stats.standardDeviation(dagTimes),      "—");
        printSpeedupRow(speedupMean, speedupMedian);
    }

    // ── Measurement helpers (milliseconds) ───────────────────────────────────────

    private static long[] measurePrim(Graph g) {
        g.primMST(); g.primMST(); // warmup
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.primMST();
            times[i] = (System.nanoTime() - start) / 1_000_000;
        }
        return times;
    }

    private static long[] measureKruskal(Graph g) {
        g.kruskalMST(); g.kruskalMST(); // warmup
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.kruskalMST();
            times[i] = (System.nanoTime() - start) / 1_000_000;
        }
        return times;
    }

    private static long[] measureDijkstra(Graph g) {
        g.dijkstra(0); g.dijkstra(0); // warmup
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.dijkstra(0);
            times[i] = (System.nanoTime() - start) / 1_000_000;
        }
        return times;
    }

    // ── Measurement helpers (microseconds) — used for DAG comparison ─────────────

    private static long[] measureDijkstraMicro(Graph g) {
        g.dijkstra(0); g.dijkstra(0); // warmup
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.dijkstra(0);
            times[i] = (System.nanoTime() - start) / 1_000;
        }
        return times;
    }

    private static long[] measureDAGMicro(Graph g) {
        g.dagShortestPath(0); g.dagShortestPath(0); // warmup
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.dagShortestPath(0);
            times[i] = (System.nanoTime() - start) / 1_000;
        }
        return times;
    }

    // ── Print helpers ─────────────────────────────────────────────────────────────

    private static void printMSTRow(String label, Graph g) {
        long[] primTimes    = measurePrim(g);
        long[] kruskalTimes = measureKruskal(g);

        System.out.println("\n  Graph: " + label);
        printTableHeader("Algorithm", "Mean (ms)", "Median (ms)", "Std Dev (ms)", "");
        printTableRow("Prim",    Stats.mean(primTimes),    Stats.median(primTimes),    Stats.standardDeviation(primTimes),    "");
        printTableRow("Kruskal", Stats.mean(kruskalTimes), Stats.median(kruskalTimes), Stats.standardDeviation(kruskalTimes), "");
    }

    private static void printDijkstraRow(String label, Graph g) {
        long[] times = measureDijkstra(g);
        System.out.println("\n  Graph: " + label);
        printTableHeader("Algorithm", "Mean (ms)", "Median (ms)", "Std Dev (ms)", "");
        printTableRow("Dijkstra", Stats.mean(times), Stats.median(times), Stats.standardDeviation(times), "");
    }

    private static void printSectionHeader(String title) {
        System.out.println("\n" + "=".repeat(75));
        System.out.println("  " + title);
        System.out.println("=".repeat(75));
    }

    private static void printTableHeader(String c1, String c2, String c3, String c4, String c5) {
        System.out.printf("  %-25s %12s %14s %14s %10s%n", c1, c2, c3, c4, c5);
        System.out.println("  " + "-".repeat(73));
    }

    private static void printTableRow(String algo, double mean, double median, double std, String extra) {
        System.out.printf("  %-25s %12.2f %14.2f %14.2f %10s%n", algo, mean, median, std, extra);
    }

    private static void printSpeedupRow(double speedupMean, double speedupMedian) {
        System.out.println("  " + "-".repeat(73));
        System.out.printf("  %-25s %12.2fx %13.2fx%n", "Speedup (Dijkstra/DAG)", speedupMean, speedupMedian);
    }
}