package algo;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import config.constant;

import map.*;

public class algo {
	private Map m;
	private String[][] grid = new String[constant.BOARDWIDTH][constant.BOARDHEIGHT];
	private int[][] obstacle = new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
	private int[][] waypoint = new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
	private Node[][][] allNodes = new Node[constant.BOARDWIDTH][constant.BOARDHEIGHT][4];

	// We need to pass robot, obstacle, waypoint and obstacle direction into this.
	public algo(Map m) {
		this.m = m;

		for (int x = 0; x < 5; x++) {
			obstacle[x] = m.getObstacle(x);
			waypoint[x] = m.getWaypoint(x);
		}

		for (int x = 0; x < constant.BOARDWIDTH; x++) {
			for (int y = 0; y < constant.BOARDHEIGHT; y++) {
				this.grid[x][y] = m.getGrid(x, y);
				for (int z = 0; z < 5; z++) {
					if (Arrays.equals(this.obstacle[z], new int[] { x, y })) {
						this.grid[x][y] = constant.OBSTACLE;
					}
				}
			}
		}

		for (int x = 0; x < constant.BOARDWIDTH; x++) {
			for (int y = 0; y < constant.BOARDHEIGHT; y++) {
				for (int z = 0; z < 4; z++) {
					allNodes[x][y][z] = new Node(new int[] { x, y, z });
				}
				//System.out.println(x + "," + y + " ::: " + grid[x][y]);
			}
		}

		int sx = (int) ((constant.ROBOTWIDTH - 1) / 2.0);
		int ex = (constant.BOARDWIDTH - (int) ((constant.ROBOTWIDTH - 1) / 2.0));
		int sy = (int) ((constant.ROBOTHEIGHT - 1) / 2.0);
		int ey = (constant.BOARDHEIGHT - (int) ((constant.ROBOTHEIGHT - 1) / 2.0));

		for (int x = sx; x < ex; x++) {
			for (int y = sy; y < ey; y++) {
				// Connect the north-south edges
				if (y < (ey - 1)) {
					Boolean safeEdge = true;
					for (int i = (x - 1); i <= (x + 1); i++) {
						for (int j = (y - 1); j <= (y + 3); j++) {
							if (i >= 0 && i < constant.BOARDWIDTH && j >= 0 && j < constant.BOARDHEIGHT) {
								if (this.grid[i][j] == constant.OBSTACLE) {
									safeEdge = false;
								}
							}
						}
					}
					if (safeEdge) {
						allNodes[x][y][1].addNeighbour(new Edge(1, allNodes[x][y][1], allNodes[x][y + 1][1]));
						allNodes[x][y][3].addNeighbour(new Edge(1, allNodes[x][y][3], allNodes[x][y + 1][3]));
						allNodes[x][y + 1][1].addNeighbour(new Edge(1, allNodes[x][y + 1][1], allNodes[x][y][1]));
						allNodes[x][y + 1][3].addNeighbour(new Edge(1, allNodes[x][y + 1][3], allNodes[x][y][3]));
					}
				}

				// Connect the east-west edges
				if (x < (ex - 1)) {
					Boolean safeEdge = true;
					for (int i = (x - 1); i <= (x + 3); i++) {
						for (int j = (y - 1); j <= (y + 1); j++) {
							if (i >= 0 && i < constant.BOARDWIDTH && j >= 0 && j < constant.BOARDHEIGHT) {
								if (this.grid[i][j] == constant.OBSTACLE) {
									safeEdge = false;
								}
							}
						}
					}
					if (safeEdge) {
						allNodes[x][y][0].addNeighbour(new Edge(1, allNodes[x][y][0], allNodes[x + 1][y][0]));
						allNodes[x][y][2].addNeighbour(new Edge(1, allNodes[x][y][2], allNodes[x + 1][y][2]));
						allNodes[x + 1][y][0].addNeighbour(new Edge(1, allNodes[x + 1][y][0], allNodes[x][y][0]));
						allNodes[x + 1][y][2].addNeighbour(new Edge(1, allNodes[x + 1][y][2], allNodes[x][y][2]));
					}
				}

				// Connect the northeast-southwest edges
				if (x < (ex - 3) && y < (ey - 3)) {
					Boolean safeEdge = true;
					for (int i = (x - 1); i <= (x + 2); i++) {
						for (int j = (y - 1); j <= (y + 2); j++) {
							if (i >= 0 && i < constant.BOARDWIDTH && j >= 0 && j < constant.BOARDHEIGHT) {
								if (this.grid[i][j] == constant.OBSTACLE) {
									safeEdge = false;
								}
							}
						}
					}
					for (int i = x; i <= (x + 3); i++) {
						for (int j = y; j <= (y + 3); j++) {
							if (i >= 0 && i < constant.BOARDWIDTH && j >= 0 && j < constant.BOARDHEIGHT) {
								if (this.grid[i][j] == constant.OBSTACLE) {
									safeEdge = false;
								}
							}
						}
					}
					for (int i = (x + 1); i <= (x + 4); i++) {
						for (int j = (y + 1); j <= (y + 4); j++) {
							if (i >= 0 && i < constant.BOARDWIDTH && j >= 0 && j < constant.BOARDHEIGHT) {
								if (this.grid[i][j] == constant.OBSTACLE) {
									safeEdge = false;
								}
							}
						}
					}
					if (safeEdge) {
						allNodes[x][y][0].addNeighbour(new Edge(5, allNodes[x][y][0], allNodes[x + 3][y + 3][1]));
						allNodes[x][y][1].addNeighbour(new Edge(5, allNodes[x][y][1], allNodes[x + 3][y + 3][0]));
						allNodes[x][y][2].addNeighbour(new Edge(5, allNodes[x][y][2], allNodes[x + 3][y + 3][3]));
						allNodes[x][y][3].addNeighbour(new Edge(5, allNodes[x][y][3], allNodes[x + 3][y + 3][2]));
						allNodes[x + 3][y + 3][0].addNeighbour(new Edge(5, allNodes[x + 3][y + 3][0], allNodes[x][y][1]));
						allNodes[x + 3][y + 3][1].addNeighbour(new Edge(5, allNodes[x + 3][y + 3][1], allNodes[x][y][0]));
						allNodes[x + 3][y + 3][2].addNeighbour(new Edge(5, allNodes[x + 3][y + 3][2], allNodes[x][y][3]));
						allNodes[x + 3][y + 3][3].addNeighbour(new Edge(5, allNodes[x + 3][y + 3][3], allNodes[x][y][2]));
					}
				}

				// Connect the northwest-southeast edges
				if (x >= (sx + 3) && y < (ey - 3)) {
					Boolean safeEdge = true;
					for (int i = (x - 2); i <= (x + 1); i++) {
						for (int j = (y - 1); j <= (y + 2); j++) {
							if (i >= 0 && i < constant.BOARDWIDTH && j >= 0 && j < constant.BOARDHEIGHT) {
								if (this.grid[i][j] == constant.OBSTACLE) {
									safeEdge = false;
								}
							}
						}
					}
					for (int i = (x - 3); i <= x; i++) {
						for (int j = y; j <= (y + 3); j++) {
							if (i >= 0 && i < constant.BOARDWIDTH && j >= 0 && j < constant.BOARDHEIGHT) {
								if (this.grid[i][j] == constant.OBSTACLE) {
									safeEdge = false;
								}
							}
						}
					}
					for (int i = (x - 4); i <= (x - 1); i++) {
						for (int j = (y + 1); j <= (y + 4); j++) {
							if (i >= 0 && i < constant.BOARDWIDTH && j >= 0 && j < constant.BOARDHEIGHT) {
								if (this.grid[i][j] == constant.OBSTACLE) {
									safeEdge = false;
								}
							}
						}
					}
					if (safeEdge) {
						allNodes[x][y][0].addNeighbour(new Edge(5, allNodes[x][y][0], allNodes[x - 3][y + 3][3]));
						allNodes[x][y][1].addNeighbour(new Edge(5, allNodes[x][y][1], allNodes[x - 3][y + 3][2]));
						allNodes[x][y][2].addNeighbour(new Edge(5, allNodes[x][y][2], allNodes[x - 3][y + 3][1]));
						allNodes[x][y][3].addNeighbour(new Edge(5, allNodes[x][y][3], allNodes[x - 3][y + 3][0]));
						allNodes[x - 3][y + 3][0].addNeighbour(new Edge(5, allNodes[x - 3][y + 3][0], allNodes[x][y][3]));
						allNodes[x - 3][y + 3][1].addNeighbour(new Edge(5, allNodes[x - 3][y + 3][1], allNodes[x][y][2]));
						allNodes[x - 3][y + 3][2].addNeighbour(new Edge(5, allNodes[x - 3][y + 3][2], allNodes[x][y][1]));
						allNodes[x - 3][y + 3][3].addNeighbour(new Edge(5, allNodes[x - 3][y + 3][3], allNodes[x][y][0]));
					}
				}

			}
		}

		// ArrayList<ArrayList<Node>> map = new ArrayList<>();

		// for (int i = 0; i < 20; i++) {
		// 	ArrayList<Node> temp = new ArrayList<>();
		// 	for (int j = 0; j < 20; j++) {
		// 		temp.add(new Node(new int[] {i, j}));
		// 	}
		// 	map.add(temp);
		// }

		// for (int i = 0; i < 20; i++) {
		// 	for (int j = 0; j < 20; j++) {
		// 		if (j + 1 < 20) {
		// 			map.get(i).get(j).addNeighbour(new Edge(1, map.get(i).get(j), map.get(i).get(j + 1)));
		// 		}
		// 		if (j - 1 > 0) {
		// 			map.get(i).get(j).addNeighbour(new Edge(1, map.get(i).get(j), map.get(i).get(j - 1)));
		// 		}
		// 		if (i + 1 < 20) {
		// 			map.get(i).get(j).addNeighbour(new Edge(1, map.get(i).get(j), map.get(i + 1).get(j)));
		// 		}
		// 		if (i - 1 > 0) {
		// 			map.get(i).get(j).addNeighbour(new Edge(1, map.get(i).get(j), map.get(i - 1).get(j)));
		// 		}
		// 	}
		// }


		// PathFinder.ShortestP(map.get(0).get(0), 6, 6, 0);
		// List<Node> path = PathFinder.getShortestP(map.get(15).get(15));
		// for (Edge edge : allNodes[1][1][1].getList()) {
		// 	System.out.println(edge.getNodeEnd());
		// }
		// PathFinder.ShortestP(allNodes[1][1][1], 0, 0, 0);
		// List<Node> path = PathFinder.getShortestP(allNodes[15][15][0]);
		// System.out.println(path);
		// PathFinder.ShortestP(sourceNode, waypointX, waypointY, waypointDirection);
	}

	// Calculate the max cost of 2 points.
	// Assuming that the robot can go horizontal or vertically, in order to reach any 2 points, the robot needs to make at most 2 turn, with the first turn can be reversed. Since the cost of a turn is 1 less than doing a rotation, and the need for offset if doing a reverse turn, then the maximum cost if the sum of change in x, change in y and 1.
	public static double distBtwnTwoPoints(int wp1x, int wp1y, int wp2x, int wp2y) {
		return (Math.abs(wp2y - wp1y) + Math.abs(wp2x - wp1x) + 1.0);
	}

	public Node[][][] getAllNodes() {
		return this.allNodes;
	}
	// This is hamiliton path computation.

	// This is calling the brain for algo path finder. We call 5 times. From start -> a -> b -> c -> d -> e

	// to find the next nearest waypoint to the current waypoint and return index of next waypoint to go to
	// public int nextNearestWaypoint(int z) {
	// 	List<Double> sortedList = new ArrayList<>();

	// 	double dist1 = distBtwnTwoPoints(m.getWaypoint(z)[0], m.getWaypoint(z)[1], m.getWaypoint(z + 1)[0],
	// 			m.getWaypoint(z + 1)[1]);
	// 	sortedList.add(dist1);
	// 	double dist2 = distBtwnTwoPoints(m.getWaypoint(z)[0], m.getWaypoint(z)[1], m.getWaypoint(z + 2)[0],
	// 			m.getWaypoint(z + 2)[1]);
	// 	sortedList.add(dist2);
	// 	double dist3 = distBtwnTwoPoints(m.getWaypoint(z)[0], m.getWaypoint(z)[1], m.getWaypoint(z + 3)[0],
	// 			m.getWaypoint(z + 3)[1]);
	// 	sortedList.add(dist3);
	// 	double dist4 = distBtwnTwoPoints(m.getWaypoint(z)[0], m.getWaypoint(z)[1], m.getWaypoint(z + 4)[0],
	// 			m.getWaypoint(z + 4)[1]);
	// 	sortedList.add(dist4);

	// 	Collections.sort(sortedList);
	// 	if (sortedList.get(0) == dist1) {
	// 		return z + 1;
	// 	} else if (sortedList.get(0) == dist2) {
	// 		return z + 2;
	// 	} else if (sortedList.get(0) == dist3) {
	// 		return z + 3;
	// 	} else {
	// 		return z + 4;
	// 	}
	// }

	public static ArrayList<Integer> getHamiltonPath(Map map) {
		ArrayList<Integer> path = new ArrayList<>();

		HashSet<Integer> remainingDestinations = new HashSet<>();
		for (Integer i = 0; i < 5; i++) {
			remainingDestinations.add(i);
		}

		Queue<Integer> queue = new LinkedList<>();
		
		queue.add(-1);

		while (!queue.isEmpty()) {
			Integer current = queue.poll();
			double minDistance = Double.MAX_VALUE;
			int minNext = 0;
			
			if (current == -1) {
				for (Integer remainingDestination : remainingDestinations) {
					Integer waypoint = remainingDestination;
					double distance = distBtwnTwoPoints(map.getWaypoint(waypoint)[0], map.getWaypoint(waypoint)[1], 1, 1);
					if (distance < minDistance) {
						minDistance = distance;
						minNext = waypoint;
					}
				}
				path.add(minNext);
				remainingDestinations.remove(minNext);
			} else {
				for (Integer remainingDestination : remainingDestinations) {
					Integer waypoint = remainingDestination;
					double distance = distBtwnTwoPoints(map.getWaypoint(waypoint)[0], map.getWaypoint(waypoint)[1], map.getWaypoint(current)[0], map.getWaypoint(current)[1]);
					if (distance < minDistance) {
						minDistance = distance;
						minNext = waypoint;
					}
				}
				path.add(minNext);
				remainingDestinations.remove(minNext);
			}

			queue.add(minNext);

			if (remainingDestinations.isEmpty()) {
				break;
			}
		}
		System.out.println(path);

		return path;
	}
}