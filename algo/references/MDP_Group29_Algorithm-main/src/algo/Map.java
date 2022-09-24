package algo;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    private final Cell[][] grid = new Cell[MapConstants.MAP_ROWS][MapConstants.MAP_COLS];
    private final String[][] gridDisplay = new String[MapConstants.MAP_ROWS][MapConstants.MAP_COLS];
    private final HashMap<Integer, Cell> nodeNumbers = new HashMap<>(10);
    private final ArrayList<Obstacle> obstacles = new ArrayList<>(3);

    /**
     * Initialises the map.
     */
    public Map() {
        initialise();
    }

    private void initialise() {
        // Populate grid with cells.
        for (int row = 0; row < MapConstants.MAP_ROWS; row++) {
            for (int col = 0; col < MapConstants.MAP_COLS; col++) {
                grid[MapConstants.MAP_ROWS - col - 1][row] = new Cell(row, col);
                gridDisplay[MapConstants.MAP_COLS - col - 1][row] = "\u2b1c";

                // Set the cell as a virtual wall if it lies at the edge of the map.
                if (row == 0 || col == 0 || row == MapConstants.MAP_ROWS - 1 || col == MapConstants.MAP_COLS - 1) {
                    grid[MapConstants.MAP_COLS - col - 1][row].setVirtualWall(true);
                    grid[MapConstants.MAP_COLS - col - 1][row].setTraversable(false);
                }
            }
        }
    }

    /**
     * Assigns a node number to each obstacle and the start point.
     */
    public void assignNodeNumbers() {
        // Set start point (1,1) as node number 0.
        nodeNumbers.put(0, grid[MapConstants.MAP_ROWS - 2][1]);
        // Set obstacles on the grid and set them as nodes for path finding.
        int i = 1;
        Cell stopHere;
        // Robot needs to leave BUFFER cells spacing from obstacle.
        for (Obstacle obstacle : obstacles) {
            switch (obstacle.getDirection()) {
            case 'N':
                stopHere = grid[MapConstants.MAP_ROWS - obstacle.getY() - 1 - MapConstants.BUFFER][obstacle.getX()];
                break;
            case 'S':
                stopHere = grid[MapConstants.MAP_ROWS - obstacle.getY() - 1 + MapConstants.BUFFER][obstacle.getX()];
                break;
            case 'E':
                stopHere = grid[MapConstants.MAP_ROWS - obstacle.getY() - 1][obstacle.getX() + MapConstants.BUFFER];
                break;
            case 'W':
                stopHere = grid[MapConstants.MAP_ROWS - obstacle.getY() - 1][obstacle.getX() - MapConstants.BUFFER];
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + obstacle.getDirection());
            }
            nodeNumbers.put(i++, stopHere);
            setBufferZone(obstacle);
        }
    }

    private void setBufferZone(Obstacle obstacle) {
        // Set left neighbour as non-traversable.
        try {
            Cell left = grid[MapConstants.MAP_ROWS - obstacle.getY() - 1][obstacle.getX() - 1];
            left.setTraversable(false);
        } catch (IndexOutOfBoundsException ignored) {
        }
        // Set right neighbour as non-traversable.
        try {
            Cell right = grid[MapConstants.MAP_ROWS - obstacle.getY() - 1][obstacle.getX() + 1];
            right.setTraversable(false);
        } catch (IndexOutOfBoundsException ignored) {
        }
        // Set bottom neighbour as non-traversable.
        try {
            Cell bottom = grid[MapConstants.MAP_ROWS - obstacle.getY()][obstacle.getX()];
            bottom.setTraversable(false);
        } catch (IndexOutOfBoundsException ignored) {
        }
        // Set top neighbour as non-traversable.
        try {
            Cell top = grid[MapConstants.MAP_ROWS - obstacle.getY() - 2][obstacle.getX()];
            top.setTraversable(false);
        } catch (IndexOutOfBoundsException ignored) {
        }
        // Set top-left neighbour as non-traversable.
        try {
            Cell topLeft = grid[MapConstants.MAP_ROWS - obstacle.getY() - 2][obstacle.getX() - 1];
            topLeft.setTraversable(false);
        } catch (IndexOutOfBoundsException ignored) {
        }
        // Set top-right neighbour as non-traversable.
        try {
            Cell topRight = grid[MapConstants.MAP_ROWS - obstacle.getY() - 2][obstacle.getX() + 1];
            topRight.setTraversable(false);
        } catch (IndexOutOfBoundsException ignored) {
        }
        // Set bottom-left neighbour as non-traversable.
        try {
            Cell bottomLeft = grid[MapConstants.MAP_ROWS - obstacle.getY()][obstacle.getX() - 1];
            bottomLeft.setTraversable(false);
        } catch (IndexOutOfBoundsException ignored) {
        }
        // Set bottom-right neighbour as non-traversable.
        try {
            Cell bottomRight = grid[MapConstants.MAP_ROWS - obstacle.getY()][obstacle.getX() + 1];
            bottomRight.setTraversable(false);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    public void setObstacle(int x, int y, char c) {
        Obstacle newObstacle = new Obstacle(x, y, c);
        // Change Cells to Obstacles.
        grid[MapConstants.MAP_ROWS - y - 1][x] = newObstacle;
        gridDisplay[MapConstants.MAP_ROWS - y - 1][x] = "\u2612";

        obstacles.add(newObstacle);

        // Set the obstacle as a virtual wall if it is at the edge of the map.
        if (x == 0 || y == 0 || x == MapConstants.MAP_COLS - 1 || y == MapConstants.MAP_ROWS - 1) {
            grid[MapConstants.MAP_COLS - y - 1][x].setVirtualWall(true);
        }       
    }

    public void printMap() {
        System.out.println("Movement area: ");
        for (int row = 0; row < MapConstants.MAP_ROWS; row++) {
            for (int col = 0; col < MapConstants.MAP_COLS; col++) {
                System.out.print(gridDisplay[row][col]);
            }
            System.out.println("\n");
        }
        System.out.println("Details of obstacles are as follows: ");
        int nodeNumber = 1;
        for (Obstacle obstacle : obstacles) {
            System.out.printf("%d. (%d, %d), %c%n", nodeNumber++, obstacle.getX(), obstacle.getY(), obstacle.getDirection());
        }
        System.out.println();
        obstacles.clear();
    }

    // Use a 2D array to store the distances between nodes.
    public double[][] computeDistances() {
        double[][] distances = new double[nodeNumbers.size()][nodeNumbers.size()];
        double x0, y0, x1, y1;
        for (int source = 0; source < nodeNumbers.size(); source++) {
            for (int dest = source; dest < nodeNumbers.size(); dest++) {
                if (source != dest) {
                	// Getting the coordinates for both nodes.
                    x0 = nodeNumbers.get(source).getX();
                    y0 = nodeNumbers.get(source).getY();
                    x1 = nodeNumbers.get(dest).getX();
                    y1 = nodeNumbers.get(dest).getY();
                    // Computing distance between the 2 nodes.
                    distances[source][dest] = computeDistance(x0, y0, x1, y1);
                    distances[dest][source] = computeDistance(x0, y0, x1, y1);
                } else {
                	// If source node is the destination node, distance will be 0.
                    distances[source][dest] = 0;
                }
            }
        }
        return distances;
    }

    // Calculating the distance between 2 nodes.
    private double computeDistance(double x0, double y0, double x1, double y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCell(int row, int col) {
        return grid[MapConstants.MAP_COLS - col - 1][row];
    }

    public boolean checkValidCoordinates(int row, int col) {
        return row >= 0 && col >= 0 && row < MapConstants.MAP_ROWS && col < MapConstants.MAP_COLS;
    }

    private boolean inStartZone(int row, int col) {
        return row >= 0 && row <= 2 && col >= 0 && col <= 2;
    }

    public HashMap<Integer, Cell> getNodeNumbers() {
        return nodeNumbers;
    }
}
