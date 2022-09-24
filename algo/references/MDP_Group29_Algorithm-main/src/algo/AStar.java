package algo;

import java.util.*;
public class AStar {

    private final ArrayList<Cell> path = new ArrayList<>();
    private final List<List<Cell>> listOfPaths = new ArrayList<>();
    private final ArrayList<Cell> closedList = new ArrayList<>();
    private final Cell[][] grid;
    private final Robot robot;

    public AStar(Cell[][] grid, Robot robot) {
        this.grid = grid;
        this.robot = robot;
    }

    /**
     * Populates the grid with new Cell objects and calculates the H-cost for each Cell.
     * @param xs x-coordinate of source.
     * @param ys y-coordinate of source.
     * @param xd x-coordinate of destination.
     * @param yd y-coordinate of destination.
     * @param additionalPath indicates whether diagonal cells should be explored.
     * @param h indicates whether to use Euclidean (h = 2) or Manhattan (h = 3) computation.
     */
    private void generateHValue(int xs, int ys, int xd, int yd, boolean additionalPath, int h) {
        Cell source = grid[MapConstants.MAP_ROWS - ys - 1][xs];
        Cell dest = grid[MapConstants.MAP_ROWS - yd - 1][xd];
        for (int row = 0; row < MapConstants.MAP_ROWS; row++) {
            for (int col = 0; col < MapConstants.MAP_COLS; col++) {
                if (h == 2) {
                    // Assign Euclidean H-cost
                    grid[MapConstants.MAP_ROWS - col - 1][row].updateEuclidHCost(dest);
                } else if (h == 3) {
                    // Assign Manhattan H-cost
                    grid[MapConstants.MAP_ROWS - col - 1][row].updateManHCost(dest);
                }
            }
        }
        try {
            generatePath(source, dest, additionalPath);
        } catch (OutOfMemoryError o) {
            System.out.println("Out of memory!");
            System.out.println("Max JVM memory: " + Runtime.getRuntime().maxMemory());
        }

    }

    /**
     * Asks for input for source and destination nodes. Then, finds Euclidean and Manhattan paths.
     */
    public void menu(int xs, int ys, int xd, int yd) {
        int gCost = 0;
        int fCost = 0;
        //Loop to find all 3 pathways and their relative Final Cost values
        for (int j = 0; j < 3; j++) {
            /*if (j == 1) {
                generateHValue(xs, ys, xd, yd, true, 2);
                if (pathList.contains(grid[MapConstants.MAP_ROWS - yd - 1][xd])) {
                    System.out.println("Backtracked path: ");
                    for (Cell node : pathList) {
                        System.out.println(node.getX() + " " + node.getY());
                        gCost += node.getGCost();
                        fCost += node.getFCost();
                    }
                    System.out.println("Euclidean Path Found");
                    System.out.println("Total Cost: " + gCost);
                    System.out.println("Total fCost: " + fCost);
                    gCost = 0;
                    fCost = 0;
                } else {
                    System.out.println("Euclidean Path Not found");
                }
                pathList.clear();
                closedList.clear();
            }*/

            if (j == 2) {
                generateHValue(xs, ys, xd, yd, false, 3);
                if (path.contains(grid[MapConstants.MAP_ROWS - yd - 1][xd])) {
                    System.out.println("Path: ");
                    for (Cell node : path) {
                        // System.out.println(node.getX() + " " + node.getY());
                        gCost += node.getGCost();
                        fCost += node.getFCost();
                    }
                    Collections.reverse(path);
                    System.out.println(path);
                    System.out.println("Manhattan Path Found");
                    System.out.println("Total Cost: " + gCost);
                    System.out.println("Total fCost: " + fCost);
                    gCost = 0;
                    fCost = 0;
                } else {
                    System.out.println("Manhattan Path Not found");
                }
                // Store the path in the list of paths.
                listOfPaths.add(new ArrayList<>(path));
                path.clear();
                closedList.clear();
            }
        }

    }

    /**
     * Runs the main A* algorithm.
     * @param source source Cell.
     * @param dest destination Cell.
     * @param additionalPath indicates whether diagonal neighbours should be explored.
     */
    private void generatePath(Cell source, Cell dest, boolean additionalPath) {

        source.setParent(null);
        // Creation of a PriorityQueue and the declaration of the Comparator
        //Compares 2 Cell objects stored in the PriorityQueue and Reorders the Queue according to the object which has the lowest fValue
        PriorityQueue<Cell> openList = new PriorityQueue<>(11, (Comparator) Comparator.comparingDouble(cell -> ((Cell) cell).getFCost()));

        //Adds the Starting cell inside the openList
        openList.add(source);

        //Executes the rest if there are objects left inside the PriorityQueue
        while (true) {

            //Gets and removes the object stored on the top of the openList and saves it inside current
            Cell current = openList.poll();

            //Checks if whether node is empty and f it is then breaks the while loop
            if (current == null) {
                break;
            }

            //Checks if whether the node returned is having the same node object values of the ending point
            //If it des then stores that inside the closedList and breaks the while loop
            if (current == dest) {
                closedList.add(current);
                break;
            }

            closedList.add(current);

            //Left Cell
            try {
                Cell left = grid[MapConstants.MAP_ROWS - current.getY() - 1][current.getX() - 1];
                // If neighbour cell is obstacle AND destination,
                // OR not an obstacle, not in openList AND not in closedList AND is traversable.
                if ((left.getIsObstacle() && left == dest)
                        || (!left.getIsObstacle()
                            && !openList.contains(left)
                            && !closedList.contains(left)
                            && left.getTraversable())) {
                    switch (robot.getDirection()) {
                    case 'W':
                        left.setDCost(0);
                        break;
                    case 'N':
                    case 'S':
                        left.setDCost(1);
                        break;
                    case 'E':
                        left.setDCost(5);
                        break;
                    default:
                        break;
                    }
                    double gNew = current.getGCost() + 1;
                    double fNew = gNew + left.getHCost() + left.getDCost();
                    if (left.getFCost() > fNew || !openList.contains(left))
                        left.setFCost(fNew);

                    openList.add(left);
                    left.parent = current;
                }
            } catch (IndexOutOfBoundsException ignored) {
            }

            //Right Cell
            try {
                Cell right = grid[MapConstants.MAP_ROWS - current.getY() - 1][current.getX() + 1];
                // If neighbour cell is obstacle AND destination,
                // OR not an obstacle, not in openList AND not in closedList AND is traversable.
                if ((right.getIsObstacle() && right == dest)
                        || (!right.getIsObstacle()
                            && !openList.contains(right)
                            && !closedList.contains(right)
                            && right.getTraversable())) {
                    switch (robot.getDirection()) {
                    case 'W':
                        right.setDCost(5);
                        break;
                    case 'N':
                    case 'S':
                        right.setDCost(1);
                        break;
                    case 'E':
                        right.setDCost(0);
                        break;
                    default:
                        break;
                    }
                    double gNew = current.getGCost() + 1;
                    double fNew = gNew + right.getHCost() + right.getDCost();
                    if (right.getFCost() > fNew || !openList.contains(right))
                        right.setFCost(fNew);

                    openList.add(right);
                    right.parent = current;
                }
            } catch (IndexOutOfBoundsException ignored) {
            }

            //Bottom Cell
            try {
                Cell bottom = grid[MapConstants.MAP_ROWS - current.getY()][current.getX()];
                // If neighbour cell is obstacle AND destination,
                // OR not an obstacle, not in openList AND not in closedList AND is traversable.
                if ((bottom.getIsObstacle() && bottom == dest)
                        || (!bottom.getIsObstacle()
                            && !openList.contains(bottom)
                            && !closedList.contains(bottom)
                            && bottom.getTraversable())) {
                    switch (robot.getDirection()) {
                    case 'S':
                        bottom.setDCost(0);
                        break;
                    case 'W':
                    case 'E':
                        bottom.setDCost(1);
                        break;
                    case 'N':
                        bottom.setDCost(5);
                        break;
                    default:
                        break;
                    }
                    double gNew = current.getGCost() + 1;
                    double fNew = gNew + bottom.getHCost() + bottom.getDCost();
                    if (bottom.getFCost() > fNew || !openList.contains(bottom))
                        bottom.setFCost(fNew);

                    openList.add(bottom);
                    bottom.parent = current;
                }
            } catch (IndexOutOfBoundsException ignored) {
            }

            //Top Cell
            try {
                Cell top = grid[MapConstants.MAP_ROWS - current.getY() - 2][current.getX()];
                // If neighbour cell is obstacle AND destination,
                // OR not an obstacle, not in openList AND not in closedList AND is traversable.
                if ((top.getIsObstacle() && top == dest)
                        || (!top.getIsObstacle()
                            && !openList.contains(top)
                            && !closedList.contains(top)
                            && top.getTraversable())) {
                    switch (robot.getDirection()) {
                    case 'N':
                        top.setDCost(0);
                        break;
                    case 'W':
                    case 'E':
                        top.setDCost(1);
                        break;
                    case 'S':
                        top.setDCost(5);
                        break;
                    default:
                        break;
                    }
                    double gNew = current.getGCost() + 1;
                    double fNew = gNew + top.getHCost() + top.getDCost();
                    if (top.getFCost() > fNew || !openList.contains(top))
                        top.setFCost(fNew);

                    openList.add(top);
                    top.parent = current;
                }
            } catch (IndexOutOfBoundsException ignored) {
            }

            if (additionalPath) {

                //TopLeft Cell
                try {
                    Cell topLeft = grid[MapConstants.MAP_ROWS - current.getY() - 2][current.getX() - 1];
                    // If neighbour cell is obstacle AND destination,
                    // OR not an obstacle, not in openList AND not in closedList AND is traversable.
                    if ((topLeft.getIsObstacle() && topLeft == dest)
                            || (!topLeft.getIsObstacle()
                                && !openList.contains(topLeft)
                                && !closedList.contains(topLeft)
                                && topLeft.getTraversable())) {
                        double tCost = current.getFCost() + MapConstants.DIAGONAL;
                        topLeft.setGCost(MapConstants.DIAGONAL);
                        double cost = topLeft.getHCost() + tCost;
                        if (topLeft.getFCost() > cost || !openList.contains(topLeft))
                            topLeft.setFCost(cost);

                        openList.add(topLeft);
                        topLeft.parent = current;
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }

                //TopRight Cell
                try {
                    Cell topRight = grid[MapConstants.MAP_ROWS - current.getY() - 2][current.getX() + 1];
                    // If neighbour cell is obstacle AND destination,
                    // OR not an obstacle, not in openList AND not in closedList AND is traversable.
                    if ((topRight.getIsObstacle() && topRight == dest)
                            || (!topRight.getIsObstacle()
                                && !openList.contains(topRight)
                                && !closedList.contains(topRight)
                                && topRight.getTraversable())) {
                        double tCost = current.getFCost() + MapConstants.DIAGONAL;
                        topRight.setGCost(MapConstants.DIAGONAL);
                        double cost = topRight.getHCost() + tCost;
                        if (topRight.getFCost() > cost || !openList.contains(topRight))
                            topRight.setFCost(cost);

                        openList.add(topRight);
                        topRight.parent = current;
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }

                //BottomLeft Cell
                try {
                    Cell bottomLeft = grid[MapConstants.MAP_ROWS - current.getY()][current.getX() - 1];
                    // If neighbour cell is obstacle AND destination,
                    // OR not an obstacle, not in openList AND not in closedList AND is traversable.
                    if ((bottomLeft.getIsObstacle() && bottomLeft == dest)
                            || (!bottomLeft.getIsObstacle()
                                && !openList.contains(bottomLeft)
                                && !closedList.contains(bottomLeft)
                                && bottomLeft.getTraversable())) {
                        double tCost = current.getFCost() + MapConstants.DIAGONAL;
                        bottomLeft.setGCost(MapConstants.DIAGONAL);
                        double cost = bottomLeft.getHCost() + tCost;
                        if (bottomLeft.getFCost() > cost || !openList.contains(bottomLeft))
                            bottomLeft.setFCost(cost);

                        openList.add(bottomLeft);
                        bottomLeft.parent = current;
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }

                //BottomRight Cell
                try {
                    Cell bottomRight = grid[MapConstants.MAP_ROWS - current.getY()][current.getX() + 1];
                    // If neighbour cell is obstacle AND destination,
                    // OR not an obstacle, not in openList AND not in closedList AND is traversable.
                    if ((bottomRight.getIsObstacle() && bottomRight == dest)
                            || (!bottomRight.getIsObstacle()
                                && !openList.contains(bottomRight)
                                && !closedList.contains(bottomRight)
                                && bottomRight.getTraversable())) {
                        double tCost = current.getFCost() + MapConstants.DIAGONAL;
                        bottomRight.setGCost(MapConstants.DIAGONAL);
                        double cost = bottomRight.getHCost() + tCost;
                        if (bottomRight.getFCost() > cost || !openList.contains(bottomRight))
                            bottomRight.setFCost(cost);

                        openList.add(bottomRight);
                        bottomRight.parent = current;
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }

        /*for (int i = 0; i < MapConstants.MAP_ROWS; i++) {
            for (int j = 0; j < MapConstants.MAP_COLS; j++) {
                System.out.print(grid[i][j].getFCost() + "    ");
            }
            System.out.println();
        }*/

        //Assigns the last Object in the closedList to the endCell variable
        Cell endCell = closedList.get(closedList.size() - 1);

        //Checks if whether the endCell variable currently has a parent Cell. if it doesn't then stops moving forward.
        //Stores each parent Cell to the PathList so it is easier to trace back the final path
        while (endCell.parent != null) {
            Cell currentCell = endCell;
            path.add(currentCell);
            endCell = endCell.parent;
        }

        path.add(source);

        openList.clear();

        System.out.println();
    }

    /**
     * Get the list of paths. The first path is that from the starting position to the first obstacle.
     * Subsequent paths are the paths from one obstacle to the next.
     * @return the list of paths.
     */
    public List<List<Cell>> getListOfPaths() {
        return listOfPaths;
    }

}