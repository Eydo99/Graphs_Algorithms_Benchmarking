package graphs.algorithms.SSSP;

import graphs.model.Graph;

public interface SSSPStrategy {
    int[] computeSSSP(Graph graph,int source);
}
