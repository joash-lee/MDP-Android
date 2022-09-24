package robot;

import java.util.Arrays;
import java.util.Timer;
// import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

import component.*;
import config.constant;
import map.*;
// import sensor.SimulatorSensor;
import simulator.AddJButtonActionListener;
import timertask.taskMoveImg;

public class simRobot extends robot {
	private JFrame frame;
	private imgComponent robotImg;
	private simMap smap;
	private AddJButtonActionListener buttonListener;
	private Timer t = new Timer();
	private int delay = constant.DELAY;

	public simRobot(JFrame frame, int x, int y, int dir) {
		super();
		initialise(x, y, dir);
		this.frame = frame;
		// 		this.sensor = new SimulatorSensor();

		// The order of adding the robot matters (parent-child rendering)
		// Hence, RobotImage must run before Map create the UI grid.

		// Place the robot based on its coordinate.
		initRobotImg(this.x, this.y);

		// Create the UI map for display with the robot.
		map = new Map();
		smap = simMap.getInstance(frame, map.copy());

		// Add the buttons onto the UI
		buttonListener = new AddJButtonActionListener(frame, this);
	}

	// Take the centre of the robot is located
	private void initRobotImg(int x, int y) {
		robotImg = new robotImgComponent(constant.ROBOTIMGPATH, constant.ROBOTWIDTH * constant.GRIDWIDTH,
				constant.ROBOTHEIGHT * constant.GRIDHEIGHT); // params 1: Constant.ROBOTIMAGEPATHS[this.getDirection()]
		robotImg.setLocation((int) (constant.MARGINX + (x - (constant.ROBOTWIDTH - 1) / 2.0) * constant.GRIDWIDTH),
				(int) (constant.MARGINY + (constant.BOARDHEIGHT - y - (constant.ROBOTHEIGHT - 1) / 2.0) * constant.GRIDHEIGHT));
		frame.add(robotImg);
	}

	// Reset the robot position;
	public void resetRobotPosOnUI() {
		this.x = checkValid(1);
		this.y = checkValid(1, true);
		// toggleValid;
		robotImg.setLocation((int) (constant.MARGINX + (x - (constant.ROBOTWIDTH - 1) / 2.0) * constant.GRIDWIDTH),
				(int) (constant.MARGINY + (constant.BOARDHEIGHT - y - (constant.ROBOTHEIGHT - 1) / 2.0) * constant.GRIDHEIGHT));
	}

	// 	// Get the sensor values from the simulated environment
	// 	protected String[] getSensorValues() {
	// 		// FL, FM, FR, RB, RF, LF
	// 		String[] sensorValues = sensor.getAllSensorsValue(this.x, this.y, getDirection());
	// 		return sensorValues;
	// 	}

	// Set the robot position and ubdate the UI
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
	}

	// Set the robot direction and update the UI
	public void setDirection(int direction) {
		super.setDirection(direction);
		// robotImg.setImage(constant.ROBOTIMAGEDIRECTION[direction]);
	}

	// // Update the map and display on the screen
	// public int[] updateMap() {
	// 	int[] isObstacle = super.updateMap();
	// 	smap.setMap(map);
	// 	return isObstacle;
	// }

	public void setObstacle(int[] obs, int dir, int z) {
		int[] oldObs = this.getObstacle(z).clone();
		int[] oldWaypoint = this.getWaypoint(z).clone();

		super.setObstacle(obs[0], obs[1], z);
		super.setObsDirection(dir, z);

		switch (dir) {
			case 0:
				super.setWaypoint(obs[0] + 20 / constant.GRIDSIZE + (int) ((constant.ROBOTWIDTH - 1) / 2.0) + 1, obs[1], z);
				break;
			case 1:
				super.setWaypoint(obs[0], obs[1] + 20 / constant.GRIDSIZE + (int) ((constant.ROBOTHEIGHT - 1) / 2.0) + 1, z);
				break;
			case 2:
				super.setWaypoint(obs[0] - (20 / constant.GRIDSIZE + (int) ((constant.ROBOTWIDTH - 1) / 2.0) + 1), obs[1], z);
				break;
			case 3:
				super.setWaypoint(obs[0], obs[1] - (20 / constant.GRIDSIZE + (int) ((constant.ROBOTHEIGHT - 1) / 2.0) + 1), z);
				break;
			default:
		}

		if (!Arrays.equals(oldObs, this.getObstacle(z)) || !Arrays.equals(oldWaypoint, this.getWaypoint(z))) {
			smap.setMapForObstacle(map, z);
			if (Arrays.equals(this.getObstacle(z), new int[] { -1, -1 })) {
				buttonListener.displayMessage("Removed obstacle " + (z + 1) + ".", 1);
				buttonListener.displayMessage("Removed waypoint " + (z + 1) + ".", 1);
			} else {
				int x1 = this.getObstacle(z)[0];
				int y1 = this.getObstacle(z)[1];
				int x2 = this.getWaypoint(z)[0];
				int y2 = this.getWaypoint(z)[1];

				if (Arrays.equals(oldObs, new int[] { -1, -1 })) {
					buttonListener.displayMessage("Succesfully set the obstacle " + (z + 1) + ": " + x1 + ", " + y1, 1);
				} else {
					buttonListener.displayMessage("Succesfully update the obstacle " + (z + 1) + ": " + x1 + ", " + y1, 1);
				}
				if (Arrays.equals(oldWaypoint, new int[] { -1, -1 })) {
					buttonListener.displayMessage("Succesfully set the waypoint " + (z + 1) + ": " + x2 + ", " + y2, 1);
				} else {
					buttonListener.displayMessage("Succesfully update the waypoint " + (z + 1) + ": " + x2 + ", " + y2, 1);
				}
			}
		}
	}

	// 	// Simulator purposes
	// 	public String checkMap() {
	// 		Map temp = sensor.getTrueMap();
	// 		if (Map.compare(temp, map)) {
	// 			return "The maps are the same!";
	// 		}
	// 		else {
	// 			return "The maps are different!";
	// 		}
	// 	}

	// 	// Just for displaying the true map in the simulator
	// 	public String toggleMap() {
	// 		Map temp = sensor.getTrueMap();
	// 		if (Map.compare(temp, smap.getMap())) {
	// 			smap.setMap(map.copy());
	// 			return "robot";
	// 		}
	// 		else {
	// 			smap.setMap(temp.copy());
	// 			return "simulated";
	// 		}
	// 	}

	// 	// Reset the robot
	// 	public void restartRobot() {
	// 		this.x = checkValidX(0);
	// 		this.y = checkValidY(0);
	// 		robotImage.setLocation(Constant.MARGINLEFT + (Constant.GRIDWIDTH * 3 - Constant.ROBOTWIDTH)/2 + (x-1) * Constant.GRIDWIDTH, Constant.MARGINTOP + (Constant.GRIDHEIGHT * 3 - Constant.ROBOTHEIGHT)/2 + (y-1) * Constant.GRIDHEIGHT);
	// 		setDirection(Constant.SOUTH);
	// 		this.sensor = new SimulatorSensor();
	// 		this.map = new Map();
	// 		smap.setMap(map);
	// 	}

	// Move the robot to move forward
	@Override
	public void forward(int step) {
		int dirNo = this.getDirection();
		if (inBound(this.x + constant.MOVEVALUE[dirNo][0], this.y + constant.MOVEVALUE[dirNo][1])) {
			this.x = checkValid(this.x + constant.MOVEVALUE[dirNo][0]);
			this.y = checkValid(this.y + constant.MOVEVALUE[dirNo][1], false);
			for (int i = 0; i < step * constant.GRIDWIDTH; i++) {
				t.schedule(new taskMoveImg(robotImg, dirNo, 1), delay * (i + 1));
			}
			this.setPosition(this.x, this.y);
		}
	}

	@Override
	public void forward() {
		forward(1);
	}

	// Move the robot to turn left in forward 
	@Override
	public void forwardLeft(int step) {
		int dirNo = this.getDirection() + 4;
		if (inBound(this.x + constant.MOVEVALUE[dirNo][0], this.y + constant.MOVEVALUE[dirNo][1])) {
			this.x = checkValid(this.x + constant.MOVEVALUE[dirNo][0]);
			this.y = checkValid(this.y + constant.MOVEVALUE[dirNo][1], false);
			for (int i = 0; i < step * constant.GRIDWIDTH; i++) {
				t.schedule(new taskMoveImg(robotImg, dirNo, 1), delay * (i + 1));
			}
			this.setDirection((this.getDirection() + 1) % 4);
			this.setPosition(this.x, this.y);
		}
	}

	@Override
	public void forwardLeft() {
		forwardLeft(constant.TURNINGRADIUS);
	}

	// Move the robot to turn right in forward 
	@Override
	public void forwardRight(int step) {
		int dirNo = ((this.getDirection() == 0) ? 4 : 0) + this.getDirection() + 3;
		if (inBound(this.x + constant.MOVEVALUE[dirNo][0], this.y + constant.MOVEVALUE[dirNo][1])) {
			this.x = checkValid(this.x + constant.MOVEVALUE[dirNo][0]);
			this.y = checkValid(this.y + constant.MOVEVALUE[dirNo][1], false);
			for (int i = 0; i < step * constant.GRIDWIDTH; i++) {
				t.schedule(new taskMoveImg(robotImg, dirNo, 1), delay * (i + 1));
			}
			this.setDirection((this.getDirection() + 3) % 4);
			this.setPosition(this.x, this.y);
		}
	}

	@Override
	public void forwardRight() {
		forwardRight(constant.TURNINGRADIUS);
	}

	// Move the robot to move backward
	@Override
	public void backward(int step) {
		int dirNo = this.getDirection();
		if (inBound(this.x - constant.MOVEVALUE[dirNo][0], this.y - constant.MOVEVALUE[dirNo][1])) {
			this.x = checkValid(this.x - constant.MOVEVALUE[dirNo][0]);
			this.y = checkValid(this.y - constant.MOVEVALUE[dirNo][1], false);
			for (int i = 0; i < step * constant.GRIDWIDTH; i++) {
				t.schedule(new taskMoveImg(robotImg, 10 + dirNo, 1), delay * (i + 1));
			}
			this.setPosition(this.x, this.y);
		}
	}

	@Override
	public void backward() {
		backward(1);
	}

	// Move the robot to turn left in backward
	@Override
	public void backwardLeft(int step) {
		int dirNo = this.getDirection() + 5 + ((this.getDirection() == 3) ? -4 : 0);
		if (inBound(this.x + constant.MOVEVALUE[dirNo][0], this.y + constant.MOVEVALUE[dirNo][1])) {
			this.x = checkValid(this.x + constant.MOVEVALUE[dirNo][0]);
			this.y = checkValid(this.y + constant.MOVEVALUE[dirNo][1], false);
			for (int i = 0; i < step * constant.GRIDWIDTH; i++) {
				t.schedule(new taskMoveImg(robotImg, 10 + dirNo, 1), delay * (i + 1));
			}
			this.setDirection((this.getDirection() + 3) % 4);
			this.setPosition(this.x, this.y);
		}
	}

	@Override
	public void backwardLeft() {
		backwardLeft(constant.TURNINGRADIUS);
	}

	// Move the robot to turn right in backward
	@Override
	public void backwardRight(int step) {
		int dirNo = this.getDirection() + 2 + ((this.getDirection() < 2) ? 4 : 0);
		if (inBound(this.x + constant.MOVEVALUE[dirNo][0], this.y + constant.MOVEVALUE[dirNo][1])) {
			this.x = checkValid(this.x + constant.MOVEVALUE[dirNo][0]);
			this.y = checkValid(this.y + constant.MOVEVALUE[dirNo][1], false);
			for (int i = 0; i < step * constant.GRIDWIDTH; i++) {
				t.schedule(new taskMoveImg(robotImg, 10 + dirNo, 1), delay * (i + 1));
			}
			this.setDirection((this.getDirection() + 1) % 4);
			this.setPosition(this.x, this.y);
		}
	}

	@Override
	public void backwardRight() {
		backwardRight(constant.TURNINGRADIUS);
	}

	// 	// Set the map of the robot
	// 	public void setMap(Map map) {
	// 		this.map = map;
	// 		smap.setMap(map);
	// 	}

	// 	// Restart the timer so it does not make the image move
	// 	public void restartRobotUI() {
	// 		t.cancel();
	// 		t.purge();
	// 		t = new Timer();
	// 	}

	// 	// Simulate the delay in capturing image
	// 	public boolean captureImage(int[][] image_pos) {
	// 		buttonListener.displayMessage("Capturing image at " + Arrays.toString(getPosition()) + " now", 1);
	// 		System.out.println("Capturing Image");
	// 		try {
	// 			TimeUnit.SECONDS.sleep(2);
	// 		}
	// 		catch (Exception e){
	// 			System.out.println(e.getMessage());
	// 		}
	// 		return false;
	// 	}

	// 	// Simulate the delay in calibration
	// 	public void calibrate() {
	// 		buttonListener.displayMessage("Calibrating", 1);
	// 		try {
	// 			TimeUnit.SECONDS.sleep(2);
	// 		}
	// 		catch (Exception e){
	// 			System.out.println(e.getMessage());
	// 		}
	// 	}

	// 	// Simulate the delay in right_align
	// 	public void right_align() {
	// 		buttonListener.displayMessage("Calibrating", 1);
	// 		try {
	// 			TimeUnit.SECONDS.sleep(1);
	// 		}
	// 		catch (Exception e){
	// 			System.out.println(e.getMessage());
	// 		}
	// 	}

	// 	public void displayMessage(String s, int mode) {
	// 		buttonListener.displayMessage(s, mode);
	// 	}
}