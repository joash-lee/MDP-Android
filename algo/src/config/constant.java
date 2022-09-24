package config;

public class constant {
	public static final int GRIDSIZE = 10; //Value in cm of a single grid, default: 5
	public static final int ARENAWIDTH = 200; //Value in cm of the arena width and multiple of gridsize, default: 200
	public static final int ARENAHEIGHT = 200; //Value in cm of the arena height and multiple of gridsize, default: 200
	public static final int BOARDWIDTH = Math.round(ARENAWIDTH / GRIDSIZE);
	public static final int BOARDHEIGHT = Math.round(ARENAHEIGHT / GRIDSIZE);
	public static final int ROBOTWIDTH = Math.round(30 / GRIDSIZE);
	public static final int ROBOTHEIGHT = Math.round(30 / GRIDSIZE);

	// For UI Simulator display
	public static final int GRIDWIDTH = 40;
	public static final int GRIDHEIGHT = 40; //Make sure this is the same as gridwidth
	public static final int MARGINX = 50;
	public static final int MARGINY = 25;
	public static final int DELAY = 10;

	//Image path
	public static final String ICONPATH = ".\\images\\simulate.png";
	public static final String FAVICONPATH = ".\\images\\simulate.ico";
	public static final String UNEXPLOREDPATH = ".\\images\\grid_unknown.png";
	public static final String EXPLOREDPATH = ".\\images\\grid_explore.png";
	public static final String OBSTACLEPATH = ".\\images\\grid_block.png";
	public static final String WAYPOINTPATH = ".\\images\\grid_way.png";
	public static final String STARTPOINTPATH = ".\\images\\grid_start.png";
	public static final String ENDPOINTPATH = ".\\images\\grid_end.png";
	public static final String[] GRIDIMGPATH = new String[] { UNEXPLOREDPATH, EXPLOREDPATH, OBSTACLEPATH, WAYPOINTPATH,
			STARTPOINTPATH, ENDPOINTPATH };

	public static final String ROBOTIMGPATH = ".\\images\\Pikachu.gif";
	public static final String ROBOTIMAGEPATHN = ".\\images\\robotN.png";
	public static final String ROBOTIMAGEPATHS = ".\\images\\robotS.png";
	public static final String ROBOTIMAGEPATHE = ".\\images\\robotE.png";
	public static final String ROBOTIMAGEPATHw = ".\\images\\robotW.png";

	public static final String[] ROBOTIMAGEDIRECTION = new String[] { ROBOTIMAGEPATHN, ROBOTIMAGEPATHS, ROBOTIMAGEPATHE,
			ROBOTIMAGEPATHw };

	//String
	public static final String SIMTITLE = "AY21/22 GROUP 8 SIMULATOR";
	public static final String[] GRIDLABEL = new String[] { "Unexplored", "Explored", "Obstacle", "Waypoint",
			"Startpoint", "Endpoint" };
	public static final String WAYPOINT = GRIDLABEL[3];
	public static final String OBSTACLE = GRIDLABEL[2];
	public static final String UNEXPLORED = GRIDLABEL[0];

	// Map directions
	public static final int EAST = 0;
	public static final int NORTH = 1;
	public static final int WEST = 2;
	public static final int SOUTH = 3;

	// Movement constant
	public static final int TURNINGRADIUS = 30 / GRIDSIZE;
	public static final int[][] MOVEVALUE = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 },
			{ TURNINGRADIUS, TURNINGRADIUS }, { -TURNINGRADIUS, TURNINGRADIUS }, { -TURNINGRADIUS, -TURNINGRADIUS },
			{ TURNINGRADIUS, -TURNINGRADIUS } }; //E N W S NE NW SW SE 

}