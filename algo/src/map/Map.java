package map;

// import java.util.Random;
import config.constant;

public class Map {
	// Grid labels: 0 - unexplored, 1 - explored, 2 - obstacle, 3 - obstacle, 4 - start point, 5 - end point
	// Note that grid is defined by x,y coordinate, and this is opposite of the array position.
	private boolean changed = true;
	private String[][] grid = new String[constant.BOARDWIDTH][constant.BOARDHEIGHT];
	private int[][] dist = new int[constant.BOARDWIDTH][constant.BOARDHEIGHT];
	public int[][] obstacle = new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
	public int[][] waypoint = new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
	public int[] direction = new int[] {-1, -1, -1, -1, -1};
	// 	private String[] MDPString = new String[3];

	// 	// Only used for simulation
	// 	public static Random r = new Random();

	public Map(String[][] grid) {
		initMap(grid);
	}
	public Map(int[][] grid) {
		for (int x = 0; x < constant.BOARDWIDTH; x++) {
			for (int y = 0; y < constant.BOARDHEIGHT; y++) {
				setGrid(x, y, constant.GRIDLABEL[(grid[x][y] < constant.GRIDLABEL.length) ? grid[x][y] : 0]);
			}
		}
	}

	public Map() {
		resetMap();
	}

	// Get all the grid
	protected String[][] getGridMap() {
		return grid;
	}
	
	// Set grid type
	public void setGrid(int x, int y, String command) {
		if (x < 0 || x >= constant.BOARDWIDTH || y < 0 || y >= constant.BOARDHEIGHT) {
			return;
		}

		for (int z = 0; z < constant.GRIDLABEL.length; z++) {
			if (command.toUpperCase().compareTo(constant.GRIDLABEL[z].toUpperCase()) == 0) {
				changed = true;
				if (z == 3) {
					setObstacle(x, y, 0);
				} else {
					grid[x][y] = command;
				}
				return;
			}
		}
		System.out.println("Grid labelling encounter some error when setting the grid.");
	}

	// Get the grid type
	public String getGrid(int x, int y) {
		// If the coordinate is outside the board, it returns an obstacle.
		if (x < 0 || x >= constant.BOARDWIDTH || y < 0 || y >= constant.BOARDHEIGHT) {
			return constant.GRIDLABEL[2];
		}
		return grid[x][y];
	}

	// Set distance of which the grid label is set
	public void setDist(int x, int y, int value) {
		if (x < 0 || x >= constant.BOARDWIDTH || y < 0 || y >= constant.BOARDHEIGHT) {
			return;
		} else {
			dist[x][y] = value;
		}
	}

	// Get the distance from the grid
	public int getDist(int x, int y) {
		// If the x,y is outside the board, it returns an obstacle.
		if (x < 0 || x >= constant.BOARDWIDTH || y < 0 || y >= constant.BOARDHEIGHT) {
			return 1;
		}
		return dist[x][y];
	}

	// Initialise the map
	public void initMap(String[][] grid) {
		for (int y = 0; y < constant.BOARDHEIGHT; y++) {
			for (int x = 0; x < constant.BOARDWIDTH; x++) {
				setGrid(x, y, grid[x][y]);
			}
		}
	}

	// Creates an unexplored map
	public void resetMap() {
		// default start point is always at the bottom left corner.
		for (int x = 0; x < constant.BOARDWIDTH; x++) {
			for (int y = 0; y < constant.BOARDHEIGHT; y++) {
				// The larger the distance value, the higher the chances for it to be overridden.
				// Set the start point grid
				if (x < constant.ROBOTWIDTH && y < constant.ROBOTHEIGHT) {
					setGrid(x, y, constant.GRIDLABEL[4]);
					setDist(x, y, 0);
				} else {
					setGrid(x, y, constant.GRIDLABEL[0]);
					setDist(x, y, 999999);
				}
			}
		}
	}

	// Copy the map and return the copy of it
	public Map copy() {
		Map m = new Map(this.grid);
		for (int k = 0; k < 5; k++) {
			m.setObstacle(this.obstacle[k][0], this.obstacle[k][1], k);
			m.setWaypoint(this.waypoint[k][0], this.waypoint[k][1], k);
		}
		return m;
	}

	// Set the obstacle of a given index
	public void setObstacle(int x, int y, int z) {
		boolean verbose = new Exception().getStackTrace()[1].getClassName().equals("robot.Robot");
		if (x >= constant.BOARDWIDTH - 1 || x <= 0 || y >= constant.BOARDHEIGHT - 1 || y <= 0
				|| (getGrid(x, y) != null && getGrid(x, y).compareTo(constant.GRIDLABEL[0]) != 0
						&& getGrid(x, y).compareTo(constant.GRIDLABEL[1]) != 0)) {
			if (!(obstacle[z][0] == -1 && obstacle[z][1] == -1)) {
				this.obstacle[z][0] = -1;
				this.obstacle[z][1] = -1;
				if (verbose) {
					System.out.println("The current obstacle is set as: " + "-1" + "," + "-1");
				}
			}
			return;
		}
		this.obstacle[z][0] = x;
		this.obstacle[z][1] = y;
		if (verbose) {
			System.out.println("Successfully set the obstacle: " + x + "," + y);
		}
	}

	// Return the obstacle of a given index.
	public int[] getObstacle(int z) {
		return obstacle[z];
	}

	public int[] getObstacle() {
		return getObstacle(0);
	}

	// Set the obstacle direction of a given index.
	public void setObsDirection(int dir, int z) {
		// if (direction[z] != -1) { ==> I think this is redudant as the next statement overwrite everything.
		// 	this.direction[z] = -1;
		// }
		this.direction[z] = dir;
	}

	// Return obstacle direction of a given index.
	public int getObsDirection(int z) {
		return direction[z];
	}
	public int getObsDirection() {
		return getObsDirection(0);
	}

	// Set the obstacle of a given index
	public void setWaypoint(int x, int y, int z) {
		boolean verbose = new Exception().getStackTrace()[1].getClassName().equals("robot.Robot");
		if (x >= constant.BOARDWIDTH - 1 || x <= 0 || y >= constant.BOARDHEIGHT - 1 || y <= 0
				|| (getGrid(x, y) != null && getGrid(x, y).compareTo(constant.GRIDLABEL[0]) != 0
						&& getGrid(x, y).compareTo(constant.GRIDLABEL[1]) != 0)) {
			if (!(waypoint[z][0] == -1 && waypoint[z][1] == -1)) {
				this.waypoint[z][0] = -1;
				this.waypoint[z][1] = -1;
				if (verbose) {
					System.out.println("The current waypoint is set as: " + "-1" + "," + "-1");
				}
			}
			return;
		}
		this.waypoint[z][0] = x;
		this.waypoint[z][1] = y;
		if (verbose) {
			System.out.println("Successfully set the waypoint: " + x + "," + y);
		}
	}

	// Return the obstacle of a given index.
	public int[] getWaypoint(int z) {
		return waypoint[z];
	}


	// 	public String print() {
	// 		String s = "";
	// 		s += "The current map is: \n\n";
	// 		System.out.println("The current map is: \n");

	// 		for (int j = 0; j < Constant.BOARDHEIGHT; j++) {
	// 			for (int i = 0; i < Constant.BOARDWIDTH; i++) {
	// 				if (i != Constant.BOARDWIDTH - 1) {
	// 					if (grid[i][j] == Constant.POSSIBLEGRIDLABELS[1] || grid[i][j] == Constant.POSSIBLEGRIDLABELS[2] ||
	// 							grid[i][j] == Constant.POSSIBLEGRIDLABELS[3] || grid[i][j] == Constant.POSSIBLEGRIDLABELS[5]) {
	// //						s+=grid[i][j] + "  , ";
	// 						String temp = " ";
	// 						if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[1]) {
	// 							temp = String.format("%3s|", " ");
	// 						} else if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[2]) {
	// 							temp = String.format("%3s|", "X");
	// 						} else if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[3]) {
	// 							temp = String.format("%3s|", "W");
	// 						} else if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[5]) {
	// 							temp = String.format("%3s|", "E");
	// 						}

	// 						s += temp;

	// //						System.out.print(grid[i][j] + "  , " );
	// 						System.out.printf("%3s", temp);
	// 					}
	// 					else {
	// //						s+=grid[i][j] + ", ";
	// //						System.out.print(grid[i][j] + ", " );
	// 						String temp = " ";
	// 						if(grid[i][j]== Constant.POSSIBLEGRIDLABELS[0]) {
	// 							temp = String.format("%3s|", "O");
	// 						} else if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[4]) {
	// 							temp = String.format("%3s|", "S");
	// 						}
	// 						s += temp;
	// 						System.out.printf("%3s", temp);
	// 					}
	// 				}
	// 				else {
	// 					String temp = " ";
	// 					if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[1]) {
	// 						temp = String.format("%3s|", " ");
	// 					} else if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[2]) {
	// 						temp = String.format("%3s|", "X");
	// 					} else if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[3]) {
	// 						temp = String.format("%3s|", "W");
	// 					} else if(grid[i][j] == Constant.POSSIBLEGRIDLABELS[5]) {
	// 						temp = String.format("%3s|", "E");
	// 					} else if(grid[i][j]== Constant.POSSIBLEGRIDLABELS[0]) {
	// 						temp = String.format("%3s|", "O");
	// 					} else if (grid[i][j] == Constant.POSSIBLEGRIDLABELS[4]) {
	// 						temp = String.format("%3s|", "S");
	// 					}

	// 					s += temp;
	// 					System.out.printf("%3s", temp);
	// 				}
	// 			}
	// 			s+="\n";
	// 			System.out.println();
	// 		}
	// 		s+="\n";
	// 		System.out.println("");
	// 		return s;
	// 	}

	// 	// Generate Random Map or Empty Map. Note that this does not guarantee a maneuverable map
	// 	public void generateMap(boolean rand) {
	// 		int k = 0;

	// 		for (int i = 0; i< Constant.BOARDWIDTH; i++) {
	// 			for (int j = 0; j < Constant.BOARDHEIGHT; j++) {
	// 				// Set the start point grids
	// 				if (i < Constant.STARTPOINTWIDTH && j < Constant.STARTPOINTHEIGHT) {
	// 					setGrid(i, j, Constant.POSSIBLEGRIDLABELS[4]);
	// 				}
	// 				// Set the end point grids
	// 				else if (i >= Constant.BOARDWIDTH - Constant.ENDPOINTWIDTH && j >= Constant.BOARDHEIGHT - Constant.ENDPOINTHEIGHT) {
	// 					setGrid(i, j, Constant.POSSIBLEGRIDLABELS[5]);
	// 				}
	// 				// Set the remaining grids explored
	// 				else {
	// 					setGrid(i, j, Constant.POSSIBLEGRIDLABELS[1]);
	// 				}
	// 			}
	// 		}

	// 		if (rand) {
	// 			while (k <= Constant.MAXOBSTACLECOUNT) {
	// 				int x = r.nextInt(Constant.BOARDWIDTH);
	// 				int y = r.nextInt(Constant.BOARDHEIGHT);
	// 				if (getGrid(x, y).compareTo(Constant.POSSIBLEGRIDLABELS[1]) == 0) {
	// 					setGrid(x, y, Constant.POSSIBLEGRIDLABELS[2]);
	// 					k++;
	// 				}

	// 			}
	// 		}

	// 	}


	// 	// Only for simulator purposes
	// 	public static boolean compare(Map a, Map b) {
	// 		String [][]a_grid = a.getGridMap();
	// 		String [][]b_grid = b.getGridMap();
	// 		for (int j = 0; j < Constant.BOARDHEIGHT; j++) {
	// 			for (int i = 0; i < Constant.BOARDWIDTH; i++) {
	// 				if (a_grid[i][j].compareTo(b_grid[i][j])!=0) {
	// 					return false;
	// 				}
	// 			}
	// 		}
	// 		return true;
	// 	}

	// 	// Check if the MDF string stored has changed and make the mdf string if it did change
	// 	public String[] getMDFString() {
	// 		if (changed == false) {
	// 			return this.MDPString;
	// 		}

	// 		changed = false;

	// 		StringBuilder MDFBitStringPart1 = new StringBuilder();
	// 		StringBuilder MDFBitStringPart2 = new StringBuilder();

	// 		MDFBitStringPart1.append("11");
	// 		String[] MDFHexString = new String[] {"","",""};

	// 		for (int j = 0; j < Constant.BOARDWIDTH; j++) {
	// 			for (int i = 0; i < Constant.BOARDHEIGHT; i++) {

	// 				if (grid[j][i].compareTo(Constant.POSSIBLEGRIDLABELS[2])==0) { // Obstacle
	// 					MDFBitStringPart1.append("1");
	// 					MDFBitStringPart2.append("1");

	// 				}
	// 				else if (grid[j][i].compareTo(Constant.POSSIBLEGRIDLABELS[0]) == 0) { // Unexplored
	// 					MDFBitStringPart1.append("0");
	// 				}
	// 				else {
	// 					MDFBitStringPart1.append("1");
	// 					MDFBitStringPart2.append("0");
	// 				}

	// 			}
	// 		}
	// 		MDFBitStringPart1.append("11");

	// 		for (int i = 0; i < MDFBitStringPart1.length(); i += 4) {
	// 			MDFHexString[0] += Integer.toString(Integer.parseInt(MDFBitStringPart1.substring(i, i + 4), 2), 16);
	// 		}

	// 		if ((MDFBitStringPart2.length() % 4) != 0){ // Only pad if the MDF Bit string is not a multiple of 4
	// 			MDFBitStringPart2.insert(0, "0".repeat(4 - (MDFBitStringPart2.length() % 4)));
	// 		}

	// 		for (int i = 0; i < MDFBitStringPart2.length(); i += 4) {
	// 			MDFHexString[2] += Integer.toString(Integer.parseInt(MDFBitStringPart2.substring(i, i + 4), 2), 16);
	// 		}

	// 		int length = 0;
	// 		for (int j = 0; j < Constant.BOARDHEIGHT; j++) {
	// 			for (int i = 0; i < Constant.BOARDWIDTH; i++) {
	// 				if (grid[i][j] != Constant.POSSIBLEGRIDLABELS[0]) {
	// 					length++;
	// 				}
	// 			}
	// 		}

	// 		MDFHexString[1] = Integer.toString(length);

	// 		this.MDPString = MDFHexString;
	// 		return MDFHexString;

	// 	}
}