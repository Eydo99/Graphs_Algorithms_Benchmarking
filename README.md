# Graphs_Algorithms_Benchmarking# Graph Algorithms Benchmarking

A Java benchmarking suite for classic graph algorithms — Prim's MST, Kruskal's MST, Dijkstra's SSSP, and DAG Shortest Path — tested across multiple graph topologies with a Python script for visualizing the results.

---

## Algorithms

### Minimum Spanning Tree (MST)
| Algorithm | Approach | Complexity |
|---|---|---|
| Prim's | Priority queue (min-heap) greedy expansion | O((V + E) log V) |
| Kruskal's | Sort edges + Union-Find (union by rank) | O(E log E) |

### Single-Source Shortest Path (SSSP)
| Algorithm | Approach | Complexity |
|---|---|---|
| Dijkstra's | Priority queue, non-negative weights | O((V + E) log V) |
| DAG Shortest Path | Topological sort (DFS) + relaxation | O(V + E) |

---

## Graph Topologies

| Type | Description |
|---|---|
| **Sparse** | Connected spanning tree + ~5V edges total |
| **Dense** | ~25% of all possible edges |
| **Complete** | All V(V−1)/2 edges present |
| **DAG** | Directed Acyclic Graph — random spanning tree with only forward edges |

---

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java 17 |
| Build | Maven |
| Testing | JUnit 5 |
| Logging | SLF4J + Logback |
| Plotting | Python 3, matplotlib, numpy |

---

## Project Structure

```
Graphs_ALgorithms_Benchmarking/
├── pom.xml
├── plot.py                          # Python visualization dashboard
├── results.csv                      # Sample benchmark output
└── src/
    ├── main/java/graphs/
    │   ├── algorithms/
    │   │   ├── MST/
    │   │   │   ├── MSTStrategy.java      # Interface
    │   │   │   ├── Prim.java
    │   │   │   └── Kruskal.java
    │   │   └── SSSP/
    │   │       ├── SSSPStrategy.java     # Interface
    │   │       ├── Dijkstra.java
    │   │       └── DAG.java              # Topo-sort + relaxation
    │   ├── benchmark/
    │   │   ├── BenchmarkRunner.java      # CLI entry point
    │   │   ├── InputGenerator.java       # Graph factory (sparse/dense/complete/DAG)
    │   │   └── Stats.java                # Mean, median, std dev
    │   ├── model/
    │   │   ├── Graph.java                # Adjacency list + algorithm dispatch
    │   │   └── Edge.java
    │   ├── util/
    │   │   └── DisjointSet.java          # Union-Find (union by rank + path compression)
    │   └── resources/
    │       └── logback.xml
    └── test/java/graphs/
        └── GraphAlgorithmsTest.java      # JUnit 5 correctness tests
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Python 3.9+ with `matplotlib` and `numpy` (for plotting only)

### Build

```bash
cd Graphs_ALgorithms_Benchmarking
mvn package
```

This produces a fat JAR at `target/Graph_Algorithms_Benchmarking-1.0-SNAPSHOT-jar-with-dependencies.jar`.

### Run the Benchmark

```bash
java -jar target/Graph_Algorithms_Benchmarking-1.0-SNAPSHOT-jar-with-dependencies.jar
```

The CLI will prompt you for:
- **Graph size** (number of vertices) — enter `0` for the default of 5,000
- **Number of runs** — enter `0` for the default of 20

Each algorithm is warmed up for 10 runs before timing begins. Results are printed to the console and written to `results.csv`.

### Run Tests

```bash
mvn test
```

### Plot Results

```bash
pip install matplotlib numpy
python plot.py
```

Saves `benchmark_plots.png` in the current directory.

---

## Benchmark Methodology

- **Warm-up:** 10 runs before any timing to allow JIT compilation
- **Timing unit:** milliseconds (ms) for MST and general SSSP; microseconds (µs) for the Dijkstra vs DAG head-to-head comparison
- **Statistics:** mean, median, and standard deviation computed over all runs
- **Reproducibility:** graph generation uses a fixed random seed (`42`)

---

## Sample Results

From `results.csv` (default 5,000-node graphs, 20 runs):

### MST — Prim vs Kruskal (mean ms)

| Graph | Prim | Kruskal |
|---|---|---|
| Sparse | 1.48 | 2.58 |
| Dense | 28.92 | 316.96 |
| Complete | 39.48 | 1,141.30 |

Prim's priority-queue approach scales much better than Kruskal's edge-sort on dense graphs.

### SSSP — Dijkstra vs DAG Shortest Path (µs, DAG topology)

| Algorithm | Mean | Median | Std Dev |
|---|---|---|---|
| Dijkstra | 760.52 | 749.50 | 31.10 |
| DAG Shortest Path | 537.96 | 540.00 | 66.13 |

DAG Shortest Path achieves a **~1.41× mean speedup** over Dijkstra on the same DAG, consistent with its O(V + E) vs O((V + E) log V) complexity advantage.

---

## Plots

`plot.py` generates a 3-panel dashboard saved as `benchmark_plots.png`:

1. **Prim vs Kruskal** — grouped bar chart with error bars (log scale)
2. **Dijkstra across topologies** — bar chart by graph type (log scale)
3. **Dijkstra vs DAG Shortest Path** — mean/median comparison with speedup annotation

---

## Test Coverage

The JUnit 5 suite (`GraphAlgorithmsTest`) covers:

- MST edge count and total weight correctness for Prim and Kruskal
- Edge cases: single vertex, two vertices, parallel edges, disconnected graphs
- Agreement between Prim and Kruskal on the same graph
- Dijkstra correctness from multiple source nodes
- DAG Shortest Path correctness including unreachable vertices
- Cycle detection — `DAG.computeSSSP` throws `IllegalArgumentException` on cyclic input
- Agreement between Dijkstra and DAG Shortest Path on positive-weight DAGs

---

## License

MIT
