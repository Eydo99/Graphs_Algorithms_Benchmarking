package graphs.algorithms.MST;


import graphs.model.Edge;
import graphs.model.Graph;
import graphs.util.DisjointSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal implements MSTStrategy {

    @Override
    public List<Edge>computeMST(Graph graph) {
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        sortedEdges.sort(Comparator.comparingInt(e -> e.weight));
        List<Edge> res = new ArrayList<>();
        DisjointSet disjointSet=new DisjointSet(graph.getV());

        for( Edge e : sortedEdges ) {
            if(disjointSet.find_ultimate_parent(e.u)!=disjointSet.find_ultimate_parent(e.v)) {
                res.add(e);
                disjointSet.union_by_rank(e.u, e.v);
            }
        }
        return res;
    }
}
