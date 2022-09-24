package algo;

import java.util.ArrayList;
import java.util.List;

public class Graph
{
    // A list of lists to represent an adjacency list.
    private List<List<Integer>> adjList = null;

    public Graph(List<Edge> edges, int N) {
        adjList = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            adjList.add(new ArrayList<>());
        }

        // Add edges to the undirected graph.
        for (Edge edge: edges) {
            int src = edge.getSource();
            int dest = edge.getDest();

            adjList.get(src).add(dest);
            adjList.get(dest).add(src);
        }
    }

    public List<List<Integer>> getAdjList() {
        return adjList;
    }
}