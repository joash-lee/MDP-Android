package map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Arrays;
import java.util.HashMap;

import config.constant;
import robot.simRobot;
import component.*;

public class simMap {
	private JFrame frame;
	private Map map;
	private simRobot r;
	private HashMap<String, String> gridImgPath = new HashMap<String, String>();
	private imgComponent[][] gridCells = new imgComponent[constant.BOARDWIDTH][constant.BOARDHEIGHT];
	private JLabel[] axis = new JLabel[constant.BOARDWIDTH + constant.BOARDHEIGHT];
	private static simMap simMap = null;

	private simMap(JFrame frame, Map map) {
		this.frame = frame;
		this.map = map;

		// Create hashmap for grid label to map to the grid image path
		for (int z = 0; z < constant.GRIDLABEL.length; z++) {
			gridImgPath.put(constant.GRIDLABEL[z], constant.GRIDIMGPATH[z]);
		}
		initMapUI(frame);

		// Create the number for the axis
		for (int k = 0; k < axis.length; k++) {
			if (k < constant.BOARDWIDTH) {
				axis[k] = new JLabel("" + k);
				axis[k].setBounds((int) (constant.MARGINX + (k + 1 / 4.0) * constant.GRIDWIDTH), constant.MARGINY - 10, 20, 20);
				axis[k].setLocation((int) (constant.MARGINX + (k + 1 / 4.0) * constant.GRIDWIDTH), constant.MARGINY - 10);
			} else {
				axis[k] = new JLabel("" + (constant.BOARDHEIGHT - k + constant.BOARDWIDTH - 1));
				axis[k].setBounds(constant.MARGINX - 25, constant.MARGINY + (k + 1 - constant.BOARDWIDTH) * constant.GRIDHEIGHT,
						20, 20);
				axis[k].setLocation(constant.MARGINX - 25,
						constant.MARGINY + (k + 1 - constant.BOARDWIDTH) * constant.GRIDHEIGHT);
			}
			frame.add(axis[k]);
			axis[k].setVisible(true);
		}
	}

	// Create the map onto the frame (UI).
	public void initMapUI(JFrame frame) {
		for (int x = 0; x < constant.BOARDWIDTH; x++) {
			for (int y = 0; y < constant.BOARDHEIGHT; y++) {
				String gridValue = map.getGrid(x, y);
				if (gridImgPath.containsKey(gridValue)) {
					createUIGrid(x, y, gridImgPath.get(gridValue));
				}
			}
		}
	}

	public static simMap getInstance(JFrame frame, Map map) {
		if (simMap == null) {
			simMap = new simMap(frame, map);
		}
		return simMap;
	}

	// // Allow the caller to set and update the UI with the map object
	// public void setMap(Map map) {
	// 	updateMapOnUI(this.map, map);
	// 	this.map = map.copy();
	// }
	
	// Set obstacle in UI
	public void setMapForObstacle(Map map, int z) {
		updateObstacleOnUI(this.map, map, z);
		this.map = map.copy();
	}

	// Create the grid image component for UI display.
	private void createUIGrid(int x, int y, String path) {
		gridCells[x][y] = new gridImgComponent(path, constant.GRIDWIDTH, constant.GRIDHEIGHT);
		gridCells[x][y].setLocation(constant.MARGINX + x * constant.GRIDWIDTH,
				constant.MARGINY + (constant.BOARDHEIGHT - y) * constant.GRIDHEIGHT);
		frame.add(gridCells[x][y]);
	}


	// Update obstacle on UI
	private void updateObstacleOnUI(Map oldMap, Map newMap, int z) {
		String[][] oldGridValue = oldMap.getGridMap();
		String[][] newGridValue = newMap.getGridMap();

		int[] oldObs = oldMap.getObstacle(z);
		int[] oldWaypoint = oldMap.getWaypoint(z);
		int[] newObs = newMap.getObstacle(z);
		int[] newWaypoint = newMap.getWaypoint(z);

		if (!Arrays.equals(oldObs, newObs)) {
			for (int x = 0; x < (10 / constant.GRIDSIZE); x++) {
				for (int y = 0; y < (10 / constant.GRIDSIZE); y++) {
					gridCells[newObs[0] + x][newObs[1] + y].setImage(gridImgPath.get(constant.OBSTACLE));
				}
			}
		}
		if (!Arrays.equals(newObs, new int[] { -1, -1 }) && !Arrays.equals(newWaypoint, new int[] { -1, -1 })) {
			gridCells[newWaypoint[0]][newWaypoint[1]].setImage(gridImgPath.get(constant.WAYPOINT));
		}

		for (int x = 0; x < constant.BOARDWIDTH; x++) {
			for (int y = 0; y < constant.BOARDHEIGHT; y++) {
				Boolean diffGridValue = (oldGridValue[x][y].compareTo(newGridValue[x][y]) != 0);
				Boolean oldObsCoord = false;
				Boolean newObsCoord = false;
				for (int i = 0; i < (10 / constant.GRIDSIZE); i++) {
					for (int j = 0; j < (10 / constant.GRIDSIZE); j++) {
						if (Arrays.equals(new int[] { oldObs[0] + i, oldObs[1] + j }, new int[] { x, y })) {
							oldObsCoord = true;
						}
						if (Arrays.equals(new int[] { newObs[0] + i, newObs[1] + j }, new int[] { x, y })) {
							newObsCoord = true;
						}
					}
				}

				if (diffGridValue || oldObsCoord || Arrays.equals(oldWaypoint, new int[] { x, y })) {
					if (gridImgPath.containsKey(newGridValue[x][y]) && !newObsCoord) {
						gridCells[x][y].setImage(gridImgPath.get(newGridValue[x][y]));
					}
				}
			}
		}
	}

	// Get the map
	public Map getMap() {
		return map;
	}
}