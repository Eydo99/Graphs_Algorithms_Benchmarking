package graphs.util;

public class DisjointSet {

    int[] parent;
    int[] rank;
    int[] size;

    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }
    }

    public int find_ultimate_parent(int u) {
        if (u == parent[u]) {
            return u;
        }
        int ultimate_parent = find_ultimate_parent(parent[u]);
        parent[u] = ultimate_parent;
        return ultimate_parent;
    }

    public void union_by_rank(int u, int v) {
        int parent_u = find_ultimate_parent(u);
        int parent_v = find_ultimate_parent(v);
        if(parent_u == parent_v) return;
        if (rank[parent_u] < rank[parent_v]) {
            parent[parent_u] = parent_v;
        }
        else if (rank[parent_u] > rank[parent_v]) {
            parent[parent_v] = parent_u;
        }
        else {
            parent[parent_v] = parent_u;
            rank[parent_u]++;
        }
    }

    public void union_by_size(int u, int v) {
        int
                parent_u = find_ultimate_parent(u);
        int parent_v = find_ultimate_parent(v);
        if(parent_u == parent_v) return;
        if (size[parent_u] < size[parent_v]) {
            parent[parent_u] = parent_v;
            size[parent_v] += size[parent_u];
        }
        else {
            parent[parent_v] = parent_u;
            size[parent_u] += size[parent_v];
        }
    }
}
