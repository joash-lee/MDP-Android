package algo;

import java.util.ArrayList;
import java.util.List;

public class Pathfinder {
    // To store all possible Hamiltonian paths.
    private final List<List<Integer>> hamPaths;

    public Pathfinder() {
        hamPaths = new ArrayList<>();
    }

    public void printAllHamiltonianPaths(Graph g, int v, boolean[] visited, List<Integer> path, int noOfNodes, double[][] distances) {
        // If all the vertices are visited, then the Hamiltonian path exists.
        if (path.size() == noOfNodes) {
            System.out.print(path + ", ");
            System.out.printf("%.2f%n", computePathDistance(path, distances));
            hamPaths.add(new ArrayList<>(path));
            return;
        }

        // Check if every edge starting from vertex 'v' lead to a solution or not.
        for (int w: g.getAdjList().get(v)) {
            // Process only unvisited vertices as the Hamiltonian path visit each vertex exactly once.
            if (!visited[w]) {
                visited[w] = true;
                path.add(w);

                // Check if adding vertex 'w' to the path leads to the solution or not
                printAllHamiltonianPaths(g, w, visited, path, noOfNodes, distances);

                // Backtrack
                visited[w] = false;
                path.remove(path.size() - 1);
            }
        }
    }

    public List<Integer> findShortestPath(double[][] distances) {
        int shortestPathIndex = 0;
        double shortestTotalDistance = 0;
        double tempTotalDistance;
        // Assume first path is the shortest.
        shortestTotalDistance = computePathDistance(hamPaths.get(0), distances);
        // Check all other paths to find the shortest one.
        for (int i = 1; i < hamPaths.size(); i++) {
            tempTotalDistance = computePathDistance(hamPaths.get(i), distances);
            if (tempTotalDistance < shortestTotalDistance) {
                shortestTotalDistance = tempTotalDistance;
                shortestPathIndex = i;
            }
        }
        return hamPaths.get(shortestPathIndex);
    }

    private double computePathDistance(List<Integer> path, double[][] distances) {
        double pathTotalDistance = 0;
        // Compute total path distance by adding the distance between the nodes.
        for (int j = 0; j < path.size() - 1; j++) {
            pathTotalDistance += distances[path.get(j)][path.get(j + 1)];
        }
        return pathTotalDistance;
    }
}