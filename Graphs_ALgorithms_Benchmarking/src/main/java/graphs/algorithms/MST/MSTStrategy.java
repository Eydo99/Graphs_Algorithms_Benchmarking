package graphs.algorithms.MST;

import graphs.model.Edge;
import graphs.model.Graph;

import java.util.List;

public interface MSTStrategy {
    List<Edge> computeMST(Graph graph);
}
