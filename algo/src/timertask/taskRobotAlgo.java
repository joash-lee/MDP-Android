package timertask;

import java.util.TimerTask;
import robot.simRobot;

public class taskRobotAlgo extends TimerTask {
  private simRobot robot;
  private int command;

  public taskRobotAlgo(simRobot r, int a) {
    robot = r;
    command = a;
  }

  @Override
  public void run() {
    if (command == 1) {
      robot.forward();
    } else if (command == 2) {
      robot.forwardLeft();
    } else if (command == 3) {
      robot.forwardRight();
    } else if (command == 4) {
      robot.backward();
    } else if (command == 5) {
      robot.backwardLeft();
    } else if (command == 6) {
      robot.backwardRight();
    }
  }
}