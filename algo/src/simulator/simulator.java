package simulator;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import config.constant;
// import map.Map;
import robot.simRobot;

public class simulator {
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	//Creating the window frame
	public simulator() {
		JFrame frame = new JFrame(constant.SIMTITLE);

		frame.setIconImage(
				new ImageIcon(constant.FAVICONPATH).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		frame.setLayout(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(constant.GRIDWIDTH * constant.BOARDWIDTH, constant.GRIDHEIGHT * constant.BOARDHEIGHT);

		simRobot sr = new simRobot(frame, 0, 0, 1);
		frame.setVisible(true);
	}
}