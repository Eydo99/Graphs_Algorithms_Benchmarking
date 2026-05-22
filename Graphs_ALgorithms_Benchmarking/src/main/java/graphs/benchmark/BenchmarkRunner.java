package graphs.benchmark;

import graphs.model.Graph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class BenchmarkRunner {
    private static int RUNS;
    private static final int DEFAULT_RUNS = 20;
    private static final int DEFAULT_SIZE=5000;
    private static final int WARM_UPS=10;
    private static final String CSV_FILE = "results.csv";

    public static void main(String[] args) throws IOException {

        initCSV();
        Scanner scanner=new Scanner(System.in);
        System.out.print("What is the size of input do you want to benchmark (Enter 0 for default size): ");
        int size=scanner.nextInt();
        while (size<0)
        {
            System.out.println("size of input must be at least one");
            System.out.print("What is the size of input do you want to benchmark: ");
            size=scanner.nextInt();
        }
        size= (size==0) ? DEFAULT_SIZE : size;
        InputGenerator.setV(size);

        System.out.print("How many runs do you want for the benchmark (Enter 0 for default runs): ");
        RUNS=scanner.nextInt();
        while(RUNS<0)
        {
            System.out.println("the benchmark need at least one run");
            System.out.print("How many runs do you want for the benchmark: ");
            RUNS=scanner.nextInt();
        }
        RUNS= (RUNS==0) ? DEFAULT_RUNS : RUNS;

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
        printTableRow("Dijkstra on DAG",   dijkstraMean,  dijkstraMedian,  Stats.standardDeviation(dijkstraTimes), "—");
        printTableRow("DAG Shortest Path", dagMean,       dagMedian,       Stats.standardDeviation(dagTimes),      "—");
        printSpeedupRow(speedupMean, speedupMedian);

        writeToCSV("DAG_SSSP", "DAG", "Dijkstra",        dijkstraMean,  dijkstraMedian,  Stats.standardDeviation(dijkstraTimes));
        writeToCSV("DAG_SSSP", "DAG", "DAG Shortest Path", dagMean,     dagMedian,       Stats.standardDeviation(dagTimes));

        System.out.println("\nResults written to " + CSV_FILE);
    }

    // ── Measurement helpers (milliseconds) ───────────────────────────────────────

    private static long[] measurePrim(Graph g) {
        for (int i=0;i<WARM_UPS;i++)
            g.primMST();

        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.primMST();
            times[i] = (System.nanoTime() - start) / 1_000_000;
        }
        return times;
    }

    private static long[] measureKruskal(Graph g) {
        for (int i=0;i<WARM_UPS;i++)
            g.kruskalMST();
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.kruskalMST();
            times[i] = (System.nanoTime() - start) / 1_000_000;
        }
        return times;
    }

    private static long[] measureDijkstra(Graph g) {
        for (int i=0;i<WARM_UPS;i++)
            g.dijkstra(0);
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.dijkstra(0);
            times[i] = (System.nanoTime() - start) / 1_000_000;
        }
        return times;
    }

    // ── Measurement helpers (microseconds) ───────────────────────────────────────

    private static long[] measureDijkstraMicro(Graph g) {
        for (int i=0;i<WARM_UPS;i++)
            g.dijkstra(0);
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.dijkstra(0);
            times[i] = (System.nanoTime() - start) / 1_000;
        }
        return times;
    }

    private static long[] measureDAGMicro(Graph g) {
        for (int i=0;i<WARM_UPS;i++)
            g.dagShortestPath(0);
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            g.dagShortestPath(0);
            times[i] = (System.nanoTime() - start) / 1_000;
        }
        return times;
    }

    // ── Print + CSV helpers ───────────────────────────────────────────────────────

    private static void printMSTRow(String label, Graph g) throws IOException {
        long[] primTimes    = measurePrim(g);
        long[] kruskalTimes = measureKruskal(g);

        System.out.println("\n  Graph: " + label);
        printTableHeader("Algorithm", "Mean (ms)", "Median (ms)", "Std Dev (ms)", "");
        printTableRow("Prim",    Stats.mean(primTimes),    Stats.median(primTimes),    Stats.standardDeviation(primTimes),    "");
        printTableRow("Kruskal", Stats.mean(kruskalTimes), Stats.median(kruskalTimes), Stats.standardDeviation(kruskalTimes), "");

        writeToCSV("MST", label, "Prim",    Stats.mean(primTimes),    Stats.median(primTimes),    Stats.standardDeviation(primTimes));
        writeToCSV("MST", label, "Kruskal", Stats.mean(kruskalTimes), Stats.median(kruskalTimes), Stats.standardDeviation(kruskalTimes));
    }

    private static void printDijkstraRow(String label, Graph g) throws IOException {
        long[] times = measureDijkstra(g);

        System.out.println("\n  Graph: " + label);
        printTableHeader("Algorithm", "Mean (ms)", "Median (ms)", "Std Dev (ms)", "");
        printTableRow("Dijkstra", Stats.mean(times), Stats.median(times), Stats.standardDeviation(times), "");

        writeToCSV("SSSP", label, "Dijkstra", Stats.mean(times), Stats.median(times), Stats.standardDeviation(times));
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

    private static void initCSV() throws IOException {
        FileWriter fw = new FileWriter(CSV_FILE, false);
        PrintWriter pw = new PrintWriter(fw);
        pw.println("Section,Graph,Algorithm,Mean,Median,StdDev");
        pw.close();
    }

    private static void writeToCSV(String section, String graph, String algorithm,
                                   double mean, double median, double stdDev) throws IOException {
        FileWriter fw = new FileWriter(CSV_FILE, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("%s,%s,%s,%.2f,%.2f,%.2f%n", section, graph, algorithm, mean, median, stdDev);
        pw.close();
    }
}