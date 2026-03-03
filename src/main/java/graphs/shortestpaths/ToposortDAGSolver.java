package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.util.*;

/**
 * Topological sorting implementation of the {@link ShortestPathSolver} interface for <b>directed acyclic graphs</b>.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    /**
     * Constructs a new instance by executing the toposort-DAG-shortest-paths algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public ToposortDAGSolver(Graph<V> graph, V start) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        
       distTo.put(start, 0.0);

       List<V> postorder = new ArrayList<>();
       Set<V> visited = new HashSet<>();
       dfsPostOrder(graph, start, visited, postorder);

       Collections.reverse(postorder);

       for ( V v : postorder) {
        double distV = distTo.getOrDefault(v, Double.POSITIVE_INFINITY);

        if(distV == Double.POSITIVE_INFINITY) {
            continue;
        }

        for (Edge<V> e : graph.neighbors(v)) {
            V w = e.to;
            double newDist = distV + e.weight;

            if(newDist < distTo.getOrDefault(w, Double.POSITIVE_INFINITY)) {
                distTo.put(w, newDist);
                edgeTo.put(w, e);
            }
        }
    }
}

    /**
     * Recursively adds nodes from the graph to the result in DFS postorder from the start vertex.
     *
     * @param graph   the input graph.
     * @param start   the start vertex.
     * @param visited the set of visited vertices.
     * @param result  the destination for adding nodes.
     */
    private void dfsPostOrder(Graph<V> graph, V start, Set<V> visited, List<V> result) {
        if(visited.contains(start)) return;
        visited.add(start);

        for(Edge<V> e : graph.neighbors(start)) {
            dfsPostOrder(graph, e.to, visited, result);
        }
        result.add(start);
    }

    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}
