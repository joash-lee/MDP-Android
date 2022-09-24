package simulator;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
// import java.util.Map;
import java.util.List;

import config.constant;
// import connection.ConnectionSocket;
// import exploration.ExplorationThread;
import robot.simRobot;
import timertask.*;
import map.Map;
import algo.*;
// import astarpathfinder.AStarPathFinder;
// import astarpathfinder.FastestPathThread;

public class AddJButtonActionListener implements ActionListener {
	private JFrame frame;
	private int x = 1000;
	private int y = 200;
	private simRobot r;
	private Map m;
	private ArrayList<JComponent> Buttons = new ArrayList<JComponent>();
	private HashMap<String, JLabel> Labels = new HashMap<String, JLabel>();
	private HashMap<String, String> filePath = new HashMap<String, String>();
	private Timer t = new Timer();
	// private int step = 1;
	private int speed_chosen = 1;
	private JTextArea message = null;
	private JScrollPane scrollPane = null;
	private int[] obs1_chosen = { -1, -1 };
	private int[] obs2_chosen = { -1, -1 };
	private int[] obs3_chosen = { -1, -1 };
	private int[] obs4_chosen = { -1, -1 };
	private int[] obs5_chosen = { -1, -1 };
	private int direction1_chosen = -1;
	private int direction2_chosen = -1;
	private int direction3_chosen = -1;
	private int direction4_chosen = -1;
	private int direction5_chosen = -1;

	private static final AtomicBoolean running = new AtomicBoolean(false);
	private static final AtomicBoolean completed = new AtomicBoolean(false);

	// This constructor initialises all the buttons
	public AddJButtonActionListener(JFrame frame, simRobot r) {
		this.frame = frame;
		this.r = r;
		// String[] arr = getArenaMapFileNames();
		String[] obs1_x_pos = create_seq_array(0, constant.BOARDWIDTH);
		String[] obs1_y_pos = create_seq_array(0, constant.BOARDHEIGHT);

		String[] obs2_x_pos = create_seq_array(0, constant.BOARDWIDTH);
		String[] obs2_y_pos = create_seq_array(0, constant.BOARDHEIGHT);

		String[] obs3_x_pos = create_seq_array(0, constant.BOARDWIDTH);
		String[] obs3_y_pos = create_seq_array(0, constant.BOARDHEIGHT);

		String[] obs4_x_pos = create_seq_array(0, constant.BOARDWIDTH);
		String[] obs4_y_pos = create_seq_array(0, constant.BOARDHEIGHT);

		String[] obs5_x_pos = create_seq_array(0, constant.BOARDWIDTH);
		String[] obs5_y_pos = create_seq_array(0, constant.BOARDHEIGHT);

		String[] speed_arr = create_seq_array(1, 6);
		String[] direction_arr = new String[] { "North", "South", "East", "West" };

		// Create the UI Component
		JLabel mcLabel = new JLabel("Manual Control:");
		JButton front_right = new JButton();
		JButton back_right = new JButton();
		JButton front_left = new JButton();
		JButton back_left = new JButton();
		JButton front = new JButton();
		JButton back = new JButton();

		JComboBox<String> obs1_x = new JComboBox<String>(obs1_x_pos);
		JComboBox<String> obs1_y = new JComboBox<String>(obs1_y_pos);

		JComboBox<String> obs2_x = new JComboBox<String>(obs2_x_pos);
		JComboBox<String> obs2_y = new JComboBox<String>(obs2_y_pos);

		JComboBox<String> obs3_x = new JComboBox<String>(obs3_x_pos);
		JComboBox<String> obs3_y = new JComboBox<String>(obs3_y_pos);

		JComboBox<String> obs4_x = new JComboBox<String>(obs4_x_pos);
		JComboBox<String> obs4_y = new JComboBox<String>(obs4_y_pos);

		JComboBox<String> obs5_x = new JComboBox<String>(obs5_x_pos);
		JComboBox<String> obs5_y = new JComboBox<String>(obs5_y_pos);

		JButton resetRobot = new JButton();
		JLabel speed_label = new JLabel("Set speed (secs/step): ");
		JComboBox<String> speed = new JComboBox<>(speed_arr);

		JLabel direction_label = new JLabel("Set direction: ");

		JLabel obstacle1_label = new JLabel("Coordinates of obstacle 1: ");
		JComboBox<String> direction1 = new JComboBox<>(direction_arr);

		JLabel obstacle2_label = new JLabel("Coordinates of obstacle 2: ");
		JComboBox<String> direction2 = new JComboBox<>(direction_arr);

		JLabel obstacle3_label = new JLabel("Coordinates of obstacle 3: ");
		JComboBox<String> direction3 = new JComboBox<>(direction_arr);

		JLabel obstacle4_label = new JLabel("Coordinates of obstacle 4: ");
		JComboBox<String> direction4 = new JComboBox<>(direction_arr);

		JLabel obstacle5_label = new JLabel("Coordinates of obstacle 5: ");
		JComboBox<String> direction5 = new JComboBox<>(direction_arr);

		JButton algo = new JButton();
		JLabel status = new JLabel("Status");
		message = new JTextArea("\n".repeat(7) + "Initialising the User Interface...");
		scrollPane = new JScrollPane(message);

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		front_right.setIcon(new ImageIcon(new ImageIcon(".\\images\\front_right.png").getImage()
				.getScaledInstance(constant.GRIDWIDTH, constant.GRIDHEIGHT, Image.SCALE_DEFAULT)));
		back_right.setIcon(new ImageIcon(new ImageIcon(".\\images\\back_right.png").getImage()
				.getScaledInstance(constant.GRIDWIDTH, constant.GRIDHEIGHT, Image.SCALE_DEFAULT)));
		front_left.setIcon(new ImageIcon(new ImageIcon(".\\images\\front_left.png").getImage()
				.getScaledInstance(constant.GRIDWIDTH, constant.GRIDHEIGHT, Image.SCALE_DEFAULT)));
		back_left.setIcon(new ImageIcon(new ImageIcon(".\\images\\back_left.png").getImage()
				.getScaledInstance(constant.GRIDWIDTH, constant.GRIDHEIGHT, Image.SCALE_DEFAULT)));
		front.setIcon(new ImageIcon(new ImageIcon(".\\images\\front.png").getImage().getScaledInstance(constant.GRIDWIDTH,
				constant.GRIDHEIGHT, Image.SCALE_DEFAULT)));
		back.setIcon(new ImageIcon(new ImageIcon(".\\images\\back.png").getImage().getScaledInstance(constant.GRIDWIDTH,
				constant.GRIDHEIGHT, Image.SCALE_DEFAULT)));
		resetRobot.setText("Restart");
		algo.setText("Run Algorithm");

		// For the Button to do something, you need to add the button to this Action Listener and set the command for the ActionListener to receive
		front_right.addActionListener(this);
		front_right.setActionCommand("Front right");
		front_left.addActionListener(this);
		front_left.setActionCommand("Front left");
		back_left.addActionListener(this);
		back_left.setActionCommand("Back left");
		back_right.addActionListener(this);
		back_right.setActionCommand("Back right");
		front.addActionListener(this);
		front.setActionCommand("Front");
		back.addActionListener(this);
		back.setActionCommand("Back");
		resetRobot.setActionCommand("Restart");
		algo.addActionListener(this);
		algo.setActionCommand("Run Algo");
		speed.addActionListener(this);
		speed.setActionCommand("Set speed");
		speed.setSelectedIndex(0);
		direction1.addActionListener(this);
		direction1.setActionCommand("Set direction1");
		direction1.setSelectedIndex(-1);
		direction2.addActionListener(this);
		direction2.setActionCommand("Set direction2");
		direction2.setSelectedIndex(-1);
		direction3.addActionListener(this);
		direction3.setActionCommand("Set direction3");
		direction3.setSelectedIndex(-1);
		direction4.addActionListener(this);
		direction4.setActionCommand("Set direction4");
		direction4.setSelectedIndex(-1);
		direction5.addActionListener(this);
		direction5.setActionCommand("Set direction5");
		direction5.setSelectedIndex(-1);

		obs1_x.addActionListener(this);
		obs1_x.setActionCommand("Set x coordinate of obstacle 1");
		obs1_x.setSelectedIndex(-1);
		obs1_y.addActionListener(this);
		obs1_y.setActionCommand("Set y coordinate of obstacle 1");
		obs1_y.setSelectedIndex(-1);

		obs2_x.addActionListener(this);
		obs2_x.setActionCommand("Set x coordinate of obstacle 2");
		obs2_x.setSelectedIndex(-1);
		obs2_y.addActionListener(this);
		obs2_y.setActionCommand("Set y coordinate of obstacle 2");
		obs2_y.setSelectedIndex(-1);

		obs3_x.addActionListener(this);
		obs3_x.setActionCommand("Set x coordinate of obstacle 3");
		obs3_x.setSelectedIndex(-1);
		obs3_y.addActionListener(this);
		obs3_y.setActionCommand("Set y coordinate of obstacle 3");
		obs3_y.setSelectedIndex(-1);

		obs4_x.addActionListener(this);
		obs4_x.setActionCommand("Set x coordinate of obstacle 4");
		obs4_x.setSelectedIndex(-1);
		obs4_y.addActionListener(this);
		obs4_y.setActionCommand("Set y coordinate of obstacle 4");
		obs4_y.setSelectedIndex(-1);

		obs5_x.addActionListener(this);
		obs5_x.setActionCommand("Set x coordinate of obstacle 5");
		obs5_x.setSelectedIndex(-1);
		obs5_y.addActionListener(this);
		obs5_y.setActionCommand("Set y coordinate of obstacle 5");
		obs5_y.setSelectedIndex(-1);

		// Set the size (x, y, width, height) of the UI label
		mcLabel.setBounds(x, y - 100, 100, 50);
		front_left.setBounds(x + 125, y - 150, 50, 50);
		back_left.setBounds(x + 125, y - 100, 50, 50);
		front_right.setBounds(x + 225, y - 150, 50, 50);
		back_right.setBounds(x + 225, y - 100, 50, 50);
		front.setBounds(x + 175, y - 150, 50, 50);
		back.setBounds(x + 175, y - 100, 50, 50);
		resetRobot.setBounds(x + 300, y - 100, 100, 50);

		obstacle1_label.setBounds(x, y + 20, 300, 30);
		obs1_x.setBounds(x + 175, y + 20, 50, 30);
		obs1_y.setBounds(x + 225, y + 20, 50, 30);
		direction1.setBounds(x + 285, y + 20, 100, 30);

		obstacle2_label.setBounds(x, y + 50, 300, 30);
		obs2_x.setBounds(x + 175, y + 50, 50, 30);
		obs2_y.setBounds(x + 225, y + 50, 50, 30);
		direction2.setBounds(x + 285, y + 50, 100, 30);

		obstacle3_label.setBounds(x, y + 80, 300, 30);
		obs3_x.setBounds(x + 175, y + 80, 50, 30);
		obs3_y.setBounds(x + 225, y + 80, 50, 30);
		direction3.setBounds(x + 285, y + 80, 100, 30);

		obstacle4_label.setBounds(x, y + 110, 300, 30);
		obs4_x.setBounds(x + 175, y + 110, 50, 30);
		obs4_y.setBounds(x + 225, y + 110, 50, 30);
		direction4.setBounds(x + 285, y + 110, 100, 30);

		obstacle5_label.setBounds(x, y + 140, 300, 30);
		obs5_x.setBounds(x + 175, y + 140, 50, 30);
		obs5_y.setBounds(x + 225, y + 140, 50, 30);
		direction5.setBounds(x + 285, y + 140, 100, 30);

		algo.setBounds(x + 50, y + 185, 300, 50);
		speed.setBounds(x + 150, y - 15, 50, 30);
		speed_label.setBounds(x, y - 15, 200, 30);
		direction_label.setBounds(x + 290, y - 15, 200, 30);
		status.setBounds(x, y - 50, 50, 30);
		scrollPane.setBounds(x, y - 50, 500, 163);

		// Set fonts for the labels
		mcLabel.setFont(new Font(mcLabel.getFont().getName(), Font.ITALIC, 13));

		// Set background colour of components
		message.setBackground(Color.WHITE);

		// Set edittable of components
		message.setEditable(false);

		// Set max row of message
		message.setLineWrap(true);
		message.setWrapStyleWord(true);

		// Set location of the UI component
		mcLabel.setLocation(x, y - 100);
		front_left.setLocation(x + 125, y - 150);
		back_left.setLocation(x + 125, y - 100);
		front_right.setLocation(x + 225, y - 150);
		back_right.setLocation(x + 225, y - 100);
		front.setLocation(x + 175, y - 150);
		back.setLocation(x + 175, y - 100);
		resetRobot.setLocation(x + 300, y - 100);

		obstacle1_label.setLocation(x, y + 20);
		obs1_x.setLocation(x + 175, y + 20);
		obs1_y.setLocation(x + 225, y + 20);
		direction1.setLocation(x + 285, y + 20);

		obstacle2_label.setLocation(x, y + 50);
		obs2_x.setLocation(x + 175, y + 50);
		obs2_y.setLocation(x + 225, y + 50);
		direction2.setLocation(x + 285, y + 50);

		obstacle3_label.setLocation(x, y + 80);
		obs3_x.setLocation(x + 175, y + 80);
		obs3_y.setLocation(x + 225, y + 80);
		direction3.setLocation(x + 285, y + 80);

		obstacle4_label.setLocation(x, y + 110);
		obs4_x.setLocation(x + 175, y + 110);
		obs4_y.setLocation(x + 225, y + 110);
		direction4.setLocation(x + 285, y + 110);

		obstacle5_label.setLocation(x, y + 140);
		obs5_x.setLocation(x + 175, y + 140);
		obs5_y.setLocation(x + 225, y + 140);
		direction5.setLocation(x + 285, y + 140);

		algo.setLocation(x + 50, y + 185);
		speed.setLocation(x + 150, y - 15);
		speed_label.setLocation(x, y - 15);
		direction_label.setLocation(x + 290, y - 15);
		status.setLocation(x, y + 360);
		scrollPane.setLocation(x, y + 390);

		// Add the UI component to the frame 
		frame.add(mcLabel);
		frame.add(front_left);
		frame.add(front_right);
		frame.add(back_left);
		frame.add(back_right);
		frame.add(front);
		frame.add(back);
		frame.add(resetRobot);

		frame.add(obstacle1_label);
		frame.add(obs1_x);
		frame.add(obs1_y);
		frame.add(direction1);

		frame.add(obstacle2_label);
		frame.add(obs2_x);
		frame.add(obs2_y);
		frame.add(direction2);

		frame.add(obstacle3_label);
		frame.add(obs3_x);
		frame.add(obs3_y);
		frame.add(direction3);

		frame.add(obstacle4_label);
		frame.add(obs4_x);
		frame.add(obs4_y);
		frame.add(direction4);

		frame.add(obstacle5_label);
		frame.add(obs5_x);
		frame.add(obs5_y);
		frame.add(direction5);

		frame.add(algo);
		frame.add(speed);
		frame.add(speed_label);
		frame.add(direction_label);
		frame.add(status);
		frame.add(scrollPane);

		// Set Visibility of UI Component
		mcLabel.setVisible(true);
		front_right.setVisible(true);
		back_right.setVisible(true);
		front_left.setVisible(true);
		back_left.setVisible(true);
		front.setVisible(true);
		back.setVisible(true);
		resetRobot.setVisible(true);

		obstacle1_label.setVisible(true);
		obs1_x.setVisible(true);
		obs1_y.setVisible(true);
		direction1.setVisible(true);

		obstacle2_label.setVisible(true);
		obs2_x.setVisible(true);
		obs2_y.setVisible(true);
		direction2.setVisible(true);

		obstacle3_label.setVisible(true);
		obs3_x.setVisible(true);
		obs3_y.setVisible(true);
		direction3.setVisible(true);

		obstacle4_label.setVisible(true);
		obs4_x.setVisible(true);
		obs4_y.setVisible(true);
		direction4.setVisible(true);

		obstacle5_label.setVisible(true);
		obs5_x.setVisible(true);
		obs5_y.setVisible(true);
		direction5.setVisible(true);

		algo.setVisible(true);
		speed.setVisible(true);
		speed_label.setVisible(true);
		direction_label.setVisible(true);
		status.setVisible(true);
		message.setVisible(true);
		scrollPane.setVisible(true);

		// Add button to the list of buttons
		Buttons.add(front_right);
		Buttons.add(front_left);
		Buttons.add(back_right);
		Buttons.add(back_left);
		Buttons.add(front);
		Buttons.add(back);
		Buttons.add(resetRobot);
		Buttons.add(algo);
		Buttons.add(speed);
		Buttons.add(direction1);
		Buttons.add(direction2);
		Buttons.add(direction3);
		Buttons.add(direction4);
		Buttons.add(direction5);
		Buttons.add(obs1_x);
		Buttons.add(obs1_y);
		Buttons.add(obs2_x);
		Buttons.add(obs2_y);
		Buttons.add(obs3_x);
		Buttons.add(obs3_y);
		Buttons.add(obs4_x);
		Buttons.add(obs4_y);
		Buttons.add(obs5_x);
		Buttons.add(obs5_y);

		// Add label to the hashmap
		Labels.put("mcLabel", mcLabel);
		Labels.put("speed_label", speed_label);
		Labels.put("obstacle1_label", obstacle1_label);
		Labels.put("obstacle2_label", obstacle2_label);
		Labels.put("obstacle3_label", obstacle3_label);
		Labels.put("obstacle4_label", obstacle4_label);
		Labels.put("obstacle5_label", obstacle5_label);
		Labels.put("direction_label", direction_label);

		// Disable all button during the real runs as they are not meant to work for the Real Run
		/* if (ConnectionSocket.checkConnection()) {
			this.disableButtons();
		} */
	}

	public void displayMessage(String s, int mode) {
		// Mode 0 only print the message, Mode 1 display onto the console, Mode 2 does both
		JScrollBar vbar = scrollPane.getVerticalScrollBar();
		switch (mode) {
			case 0:
				System.out.println(s);
				break;
			case 1:
				message.append("\n" + s);
				try {
					String messageText = message.getDocument().getText(0, message.getDocument().getLength());
					if (messageText.charAt(0) == '\n') {
						message.setText(message.getDocument().getText(0, message.getDocument().getLength()).replaceFirst("\n", ""));
					}
					String[] arr = messageText.split("\n");
					if (arr.length > 100) {
						message.setText(
								message.getDocument().getText(0, message.getDocument().getLength()).replaceFirst(arr[0] + "\n", ""));
					}
				} catch (Exception e) {
					System.out.println("Error in displayMessage");
				}
				vbar.addAdjustmentListener(new AdjustmentListener() {
					@Override
					public void adjustmentValueChanged(AdjustmentEvent e) {
						Adjustable adjustable = e.getAdjustable();
						adjustable.setValue(adjustable.getMaximum());
						// This is so that the user can scroll down afterwards
						vbar.removeAdjustmentListener(this);
					}
				});
				break;
			case 2:
				System.out.println(s);
				message.append("\n" + s);
				try {
					String messageText = message.getDocument().getText(0, message.getDocument().getLength());
					if (messageText.charAt(0) == '\n') {
						message.setText(message.getDocument().getText(0, message.getDocument().getLength()).replaceFirst("\n", ""));
					}
					String[] arr = messageText.split("\n");
					if (arr.length > 100) {
						message.setText(
								message.getDocument().getText(0, message.getDocument().getLength()).replaceFirst(arr[0] + "\n", ""));
					}
				} catch (Exception e) {
					System.out.println("Error in displayMessage");
				}
				vbar.addAdjustmentListener(new AdjustmentListener() {
					@Override
					public void adjustmentValueChanged(AdjustmentEvent e) {
						Adjustable adjustable = e.getAdjustable();
						adjustable.setValue(adjustable.getMaximum());
						// This is so that the user can scroll down afterwards
						vbar.removeAdjustmentListener(this);
					}
				});
				break;
		}
	}

	private String[] create_seq_array(int min, int max) {
		String[] arr = new String[max - min + 1];
		int count = 1;
		for (int i = min; i < max; i++) {
			arr[count] = Integer.toString(i);
			count++;
		}
		return arr;
	}

	// private String[] create_even_num_array(int min, int max) {
	// 	String[] even_arr = new String[max/2 + 1];
	// 	int count = 1;
	// 	for (int i = min; i < max; i++) {
	// 		if (i % 2 == 0) {
	// 		 even_arr[count] = Integer.toString(i);
	// 		 count++;
	// 		}
	// 	 }
	// 	return even_arr;
	// }

	private void disableButtons() {
		for (int i = 0; i < Buttons.size(); i++) {
			Buttons.get(i).setEnabled(false);
		}
	}

	public void enableButtons() {
		for (int i = 0; i < Buttons.size(); i++) {
			Buttons.get(i).setEnabled(true);
		}
	}

	public void disableLabel(String label) {
		Labels.get(label).setVisible(false);
	}

	public void enableLabel(String label) {
		Labels.get(label).setVisible(true);
	}

	// Define all the action of all the button
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("Front left")) {
			displayMessage("Front left button clicked", 2);
			disableButtons();
			// if (!Labels.get("robotView").isVisible()) {	enableLabel("robotView");	disableLabel("simulatedMap");	}
			r.forwardLeft();
			t.schedule(new EnableButtonTask(this), constant.DELAY * (constant.TURNINGRADIUS * constant.GRIDWIDTH + 1));
			System.out.println("DEBUG: x: " + r.getPositionX() + " y: " + r.getPositionY() + " dir: " + r.getDirection());
		}

		if (action.equals("Front")) {
			displayMessage("Front button clicked", 2);
			disableButtons();
			// if (!Labels.get("robotView").isVisible()) {	enableLabel("robotView");	disableLabel("simulatedMap");	}
			r.forward();
			t.schedule(new EnableButtonTask(this), constant.DELAY * (1 * constant.GRIDWIDTH + 1));
			System.out.println("DEBUG: x: " + r.getPositionX() + " y: " + r.getPositionY() + " dir: " + r.getDirection());
		}

		if (action.equals("Front right")) {
			displayMessage("Front right button clicked", 2);
			disableButtons();
			// if (!Labels.get("robotView").isVisible()) {	enableLabel("robotView");	disableLabel("simulatedMap");	}
			r.forwardRight();
			t.schedule(new EnableButtonTask(this), constant.DELAY * (constant.TURNINGRADIUS * constant.GRIDWIDTH + 1));
			System.out.println("DEBUG: x: " + r.getPositionX() + " y: " + r.getPositionY() + " dir: " + r.getDirection());
		}

		if (action.equals("Back left")) {
			displayMessage("Back left button clicked", 2);
			disableButtons();
			// if (!Labels.get("robotView").isVisible()) {	enableLabel("robotView");	disableLabel("simulatedMap");	}
			r.backwardLeft();
			t.schedule(new EnableButtonTask(this), constant.DELAY * (constant.TURNINGRADIUS * constant.GRIDWIDTH + 1));
			System.out.println("DEBUG: x: " + r.getPositionX() + " y: " + r.getPositionY() + " dir: " + r.getDirection());
		}

		if (action.equals("Back")) {
			displayMessage("Back button clicked", 2);
			disableButtons();
			// if (!Labels.get("robotView").isVisible()) {	enableLabel("robotView");	disableLabel("simulatedMap");	}
			r.backward();
			t.schedule(new EnableButtonTask(this), constant.DELAY * (1 * constant.GRIDWIDTH + 1));
			System.out.println("DEBUG: x: " + r.getPositionX() + " y: " + r.getPositionY() + " dir: " + r.getDirection());
		}

		if (action.equals("Back right")) {
			displayMessage("Back right button clicked", 2);
			disableButtons();
			// if (!Labels.get("robotView").isVisible()) {	enableLabel("robotView");	disableLabel("simulatedMap");	}
			r.backwardRight();
			t.schedule(new EnableButtonTask(this), constant.DELAY * (constant.TURNINGRADIUS * constant.GRIDWIDTH + 1));
			System.out.println("DEBUG: x: " + r.getPositionX() + " y: " + r.getPositionY() + " dir: " + r.getDirection());
		}

		if (action.contentEquals("Update")) {
			disableButtons();
			if (!Labels.get("robotView").isVisible()) {
				enableLabel("robotView");
				disableLabel("simulatedMap");
			}
			/* int[] isObstacle = r.updateMap();
			for (int i = 0; i < isObstacle.length; i++) {
				System.out.print(isObstacle[i] + " ");
			}
			System.out.println();
			t.schedule(new EnableButtonTask(this), Constant.DELAY * (step * Constant.GRIDWIDTH + 1)); */
		}

		if (action.contentEquals("Check Map")) {
			disableButtons();
			// JOptionPane.showMessageDialog(null, r.checkMap(), "Result of checking map", JOptionPane.INFORMATION_MESSAGE);
			// t.schedule(new EnableButtonTask(this), Constant.DELAY * (step * Constant.GRIDWIDTH + 1));
		}

		if (action.contentEquals("Toggle Map")) {
			disableButtons();
			/* if (r.toggleMap().compareTo("robot") == 0) {
				if (r.checkMap().equals("The maps are the same!") && Labels.get("robotView").isVisible()){
					disableLabel("robotView");
					enableLabel("simulatedMap");
				}
				else {
					enableLabel("robotView");
					disableLabel("simulatedMap");
				}
			}
			else {
				disableLabel("robotView");
				enableLabel("simulatedMap");
			}
			t.schedule(new EnableButtonTask(this), Constant.DELAY * (step * Constant.GRIDWIDTH + 1)); */
		}

		if (action.contentEquals("Restart")) {
			disableButtons();
			enableLabel("robotView");
			disableLabel("simulatedMap");
			// ExplorationThread.stopThread();
			// FastestPathThread.stopThread();
			// r.restartRobotUI();
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (Exception z) {

			}
			// r.restartRobot();
			// t.schedule(new EnableButtonTask(this), Constant.DELAY * (step * Constant.GRIDWIDTH + 1));
			displayMessage("Restarted the Robot", 2);
		}

		/* if (action.contentEquals("Load Map")) {
			JComboBox <String> arenaMap = (JComboBox <String>)e.getSource();
			String selectedFile = (String) arenaMap.getSelectedItem();
			if (selectedFile == null || selectedFile.compareTo("Choose a map to load") == 0) {
				return;
			}
			// String[][] grid = new String[Constant.BOARDWIDTH][Constant.BOARDHEIGHT];
			disableButtons();
			try {
				// Map map = getGridfromFile(filePath.get(selectedFile), selectedFile, grid);
				// r.setTrueMap(map);
				if (Labels.get("simulatedMap").isVisible()) {
					enableLabel("robotView");
					disableLabel("simulatedMap");
					// r.toggleMap();
					// r.toggleMap();
				}
			}
			catch (FileNotFoundException f){
				System.out.println("File not found");
			}
			catch (IOException IO) {
				System.out.println("IOException when reading" + selectedFile);
			}
			catch (Exception eX) {
				System.out.println(eX.getMessage());
			}
		
			// t.schedule(new EnableButtonTask(this), Constant.DELAY * (step * Constant.GRIDWIDTH + 1));
			
		}*/

		if (action.contentEquals("Run Algo")) {
			// TESTING
			// algo.getHamiltonPath(r.getMap());
			// algo a = new algo(r.getMap());

			Map robotMap = r.getMap();
				// algo.algo(robotMap);
				// LINK/INTEGRATE ALGORITHM
				List<Integer> hamiltonPath = algo.getHamiltonPath(robotMap);

				int currentX = r.getPositionX();
				int currentY = r.getPositionY();

				int oldX = r.getPositionX();
				int oldY = r.getPositionY();

				int robotDirection = r.getDirection();

				System.out.println("TEST");
				System.out.println(currentX);
				System.out.println(currentY);
				System.out.println(robotDirection);

				System.out.println(hamiltonPath);

				int stepcompleted = 0;
				for (int i = 0; i < hamiltonPath.size(); i++) {
					algo a = new algo(robotMap);
					int waypoint = hamiltonPath.get(i);

					PathFinder.ShortestP(a.getAllNodes()[currentX][currentY][robotDirection]);
					List<Node> path = PathFinder.getShortestP(a.getAllNodes()[robotMap.getWaypoint(waypoint)[0]][robotMap.getWaypoint(waypoint)[1]][(robotMap.getObsDirection(waypoint) + 2) % 4]);	
					//List<Node> path = PathFinder.getShortestP(a.getAllNodes()[5][5][1]);	

					int currentDirection = path.get(0).getNode()[2];

					currentX = robotMap.getWaypoint(waypoint)[0];
					currentY = robotMap.getWaypoint(waypoint)[1];
					robotDirection = (robotMap.getObsDirection(waypoint) + 2) % 4;

					System.out.println("targetX: " + currentX);
					System.out.println("targetY: " + currentY);

					System.out.println(path);

					for (int j = 1; j < path.size(); j++) {
						int nextDirection = path.get(j).getNode()[2];
						int nextX = path.get(j).getNode()[0];
						int nextY = path.get(j).getNode()[1];

						System.out.println("Robot (" + r.getPositionX() + ", " + r.getPositionY() + ")");
						System.out.println("Robot direction" + r.getDirection());

						if (nextDirection == currentDirection) {
							if (currentDirection == 0) {
								if (nextX < oldX) {
									System.out.println("backward");
									t.schedule(new taskRobotAlgo(r, 4), 2000 * (stepcompleted+j + 1));
								} 
								else {
									System.out.println("forward");
									t.schedule(new taskRobotAlgo(r, 1), 2000 * (stepcompleted+j + 1));
								}
							}
							else if (currentDirection == 1) {
								if (nextY < oldY) {
									System.out.println("backward");
									t.schedule(new taskRobotAlgo(r, 4), 2000 * (stepcompleted+j + 1));
								}	
								else {
									System.out.println("forward");
									t.schedule(new taskRobotAlgo(r, 1), 2000 * (stepcompleted+j + 1));
								}
							}
							else if (currentDirection == 2) {
								if (nextX > oldX) {
									System.out.println("backward");
									t.schedule(new taskRobotAlgo(r, 4), 2000 * (stepcompleted+j + 1));
								}
								else {
									System.out.println("forward");
									t.schedule(new taskRobotAlgo(r, 1), 2000 * (stepcompleted+j + 1));
								}
							} 
							else if (currentDirection == 3) {
								if (nextY > oldY) {
									System.out.println("backward");
									t.schedule(new taskRobotAlgo(r, 4), 2000 * (stepcompleted+j + 1));
								}
								else {
									System.out.println("forward");
									t.schedule(new taskRobotAlgo(r, 1), 2000 * (stepcompleted+j + 1));
								}
							}	
						} else if (((nextDirection - currentDirection) % 4 + 4) % 4 == 1) {
							if (currentDirection == 0) {
								if (nextY < oldY) {
									System.out.println("backwardRight");
									t.schedule(new taskRobotAlgo(r, 6), 2000 * (stepcompleted+j + 1));
								} 
								else {
									System.out.println("forwardLeft");
									t.schedule(new taskRobotAlgo(r, 2), 2000 * (stepcompleted+j + 1));
								}
							}
							else if (currentDirection == 1) {
								if (nextX < oldX) {
									System.out.println("forwardLeft");
									t.schedule(new taskRobotAlgo(r, 2), 2000 * (stepcompleted+j + 1));
								}	
								else {
									System.out.println("backwardRight");
									t.schedule(new taskRobotAlgo(r, 6), 2000 * (stepcompleted+j + 1));
								}
							}
							else if (currentDirection == 2) {
								if (nextY > oldY) {
									System.out.println("backwardRight");
									t.schedule(new taskRobotAlgo(r, 6), 2000 * (stepcompleted+j + 1));
								}
								else {
									System.out.println("forwardLeft");
									t.schedule(new taskRobotAlgo(r, 2), 2000 * (stepcompleted+j + 1));
								}
							} 
							else if (currentDirection == 3) {
								if (nextX > oldX) {
									System.out.println("forwardLeft");
									t.schedule(new taskRobotAlgo(r, 2), 2000 * (stepcompleted+j + 1));
								}
								else {
									System.out.println("backwardRight");
									t.schedule(new taskRobotAlgo(r, 6), 2000 * (stepcompleted+j + 1));
								}
							}	
						} else if (((nextDirection - currentDirection) % 4 + 4) % 4 == 3) {
							if (currentDirection == 0) {
								if (nextY < oldY) {
									System.out.println("forwardRight");
									t.schedule(new taskRobotAlgo(r, 3), 2000 * (stepcompleted+j + 1));
								} 
								else {
									System.out.println("backwardLeft");
									t.schedule(new taskRobotAlgo(r, 5), 2000 * (stepcompleted+j + 1));
								}
							}
							else if (currentDirection == 1) {
								if (nextX < oldX) {
									System.out.println("backwardLeft");
									t.schedule(new taskRobotAlgo(r, 5), 2000 * (stepcompleted+j + 1));
								}	
								else {
									System.out.println("forwardRight");
									t.schedule(new taskRobotAlgo(r, 3), 2000 * (stepcompleted+j + 1));
								}
							}
							else if (currentDirection == 2) {
								if (nextY > oldY) {
									System.out.println("forwardRight");
									t.schedule(new taskRobotAlgo(r, 3), 2000 * (stepcompleted+j + 1));
								}
								else {
									System.out.println("backwardLeft");
									t.schedule(new taskRobotAlgo(r, 5), 2000 * (stepcompleted+j + 1));
								}
							} 
							else if (currentDirection == 3) {
								if (nextX > oldX) {
									System.out.println("backwardLeft");
									t.schedule(new taskRobotAlgo(r, 5), 2000 * (stepcompleted+j + 1));
								}
								else {
									System.out.println("forwardRight");
									t.schedule(new taskRobotAlgo(r, 3), 2000 * (stepcompleted+j + 1));
								}
							}	
						}
						
						currentDirection = nextDirection;
						oldX = nextX;
						oldY = nextY;
					}
					stepcompleted += path.size();


				}

				

			Boolean unsetWaypoint = false;
			for (int z = 0; z < 5; z++) {
				if (unsetWaypoint) {
					break;
				} else if (Arrays.equals(r.getWaypoint(z), new int[] { -1, -1 })) {
					unsetWaypoint = true;
				}
			}
			if (unsetWaypoint) {
				displayMessage("All the 5 obstacles are not set correctly. Please update the obstacles first!", 1);
			} else {
				displayMessage("Algorithm is now starting...", 1);
				// Map robotMap = r.getMap();
				// // algo.algo(robotMap);
				// // LINK/INTEGRATE ALGORITHM
				// algo a = new algo(robotMap);
				// PathFinder.ShortestP(a.getAllNodes()[1][1][1]);
				// List<Node> path = PathFinder.getShortestP(a.getAllNodes()[16][16][3]);
				// for (Node node : path) {
				// 	System.out.println(node.getDir());
				// }
			}
		}

		if (action.contentEquals("Fastest Path")) {

			/* if (Arrays.equals(waypoint_chosen, new int[] {-1, -1})) {
				// r.setWaypoint(waypoint_chosen[0], waypoint_chosen[1]);
			} */
			// int [] waypoint = r.getWaypoint();
			/* if (!FastestPathThread.getRunning() && ExplorationThread.getCompleted()) {
				displayMessage("Fastest Path Started", 1);
				FastestPathThread.getInstance(r, waypoint, speed_chosen);
				disableButtons();
				t.schedule(new EnableButtonTask(this), 1);
			}
			else if (!ExplorationThread.getCompleted()) {
				displayMessage("You need to run exploration first.", 2);
			} */
		}

		if (action.contentEquals("Exploration")) {
			/* if (!ExplorationThread.getRunning()) {
				displayMessage("Exploration Started", 1);
				ExplorationThread.getInstance(r, time_chosen, percentage_chosen, speed_chosen, image_recognition_chosen);
				disableButtons();
				t.schedule(new EnableButtonTask(this), 1);
			}*/
		}

		if (action.contentEquals("Set x coordinate of obstacle 1")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs1_x = (JComboBox<String>) e.getSource();
			String selected_obs1_x = (String) obs1_x.getSelectedItem();
			obs1_chosen[0] = (selected_obs1_x == null) ? -1 : Integer.parseInt(selected_obs1_x);
			// int[] old_waypoint = r.getObstacle(0);
			r.setObstacle(obs1_chosen, direction1_chosen, 0);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set y coordinate of obstacle 1")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs1_y = (JComboBox<String>) e.getSource();
			String selected_obs1_y = (String) obs1_y.getSelectedItem();
			obs1_chosen[1] = (selected_obs1_y == null) ? -1 : Integer.parseInt(selected_obs1_y);
			// int[] old_waypoint = r.getObstacle(0);
			r.setObstacle(obs1_chosen, direction1_chosen, 0);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set direction1")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> direction1 = (JComboBox<String>) e.getSource();
			String selected_direction1 = (String) direction1.getSelectedItem();
			direction1_chosen = (selected_direction1 == null) ? -1
					: indexOf(new String[] { "East", "North", "West", "South" }, selected_direction1);
			r.setObstacle(obs1_chosen, direction1_chosen, 0);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set x coordinate of obstacle 2")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs2_x = (JComboBox<String>) e.getSource();
			String selected_obs2_x = (String) obs2_x.getSelectedItem();
			obs2_chosen[0] = (selected_obs2_x == null) ? -1 : Integer.parseInt(selected_obs2_x);
			// int[] old_waypoint = r.getObstacle(1);
			r.setObstacle(obs2_chosen, direction2_chosen, 1);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set y coordinate of obstacle 2")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs2_y = (JComboBox<String>) e.getSource();
			String selected_obs2_y = (String) obs2_y.getSelectedItem();
			obs2_chosen[1] = (selected_obs2_y == null) ? -1 : Integer.parseInt(selected_obs2_y);
			// int[] old_waypoint = r.getObstacle(1);
			r.setObstacle(obs2_chosen, direction2_chosen, 1);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set direction2")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> direction2 = (JComboBox<String>) e.getSource();
			String selected_direction2 = (String) direction2.getSelectedItem();
			direction2_chosen = (selected_direction2 == null) ? -1
					: indexOf(new String[] { "East", "North", "West", "South" }, selected_direction2);
			r.setObstacle(obs2_chosen, direction2_chosen, 1);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set x coordinate of obstacle 3")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs3_x = (JComboBox<String>) e.getSource();
			String selected_obs3_x = (String) obs3_x.getSelectedItem();
			obs3_chosen[0] = (selected_obs3_x == null) ? -1 : Integer.parseInt(selected_obs3_x);
			// int[] old_waypoint = r.getObstacle(1);
			r.setObstacle(obs3_chosen, direction3_chosen, 2);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set y coordinate of obstacle 3")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs3_y = (JComboBox<String>) e.getSource();
			String selected_obs3_y = (String) obs3_y.getSelectedItem();
			obs3_chosen[1] = (selected_obs3_y == null) ? -1 : Integer.parseInt(selected_obs3_y);
			// int[] old_waypoint = r.getObstacle(1);
			r.setObstacle(obs3_chosen, direction3_chosen, 2);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set direction3")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> direction3 = (JComboBox<String>) e.getSource();
			String selected_direction3 = (String) direction3.getSelectedItem();
			direction3_chosen = (selected_direction3 == null) ? -1
					: indexOf(new String[] { "East", "North", "West", "South" }, selected_direction3);
			r.setObstacle(obs3_chosen, direction3_chosen, 2);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set x coordinate of obstacle 4")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs4_x = (JComboBox<String>) e.getSource();
			String selected_obs4_x = (String) obs4_x.getSelectedItem();
			obs4_chosen[0] = (selected_obs4_x == null) ? -1 : Integer.parseInt(selected_obs4_x);
			// int[] old_waypoint = r.getObstacle(1);
			r.setObstacle(obs4_chosen, direction4_chosen, 3);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set y coordinate of obstacle 4")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs4_y = (JComboBox<String>) e.getSource();
			String selected_obs4_y = (String) obs4_y.getSelectedItem();
			obs4_chosen[1] = (selected_obs4_y == null) ? -1 : Integer.parseInt(selected_obs4_y);
			// int[] old_waypoint = r.getObstacle(1);
			r.setObstacle(obs4_chosen, direction4_chosen, 3);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set direction4")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> direction4 = (JComboBox<String>) e.getSource();
			String selected_direction4 = (String) direction4.getSelectedItem();
			direction4_chosen = (selected_direction4 == null) ? -1
					: indexOf(new String[] { "East", "North", "West", "South" }, selected_direction4);
			r.setObstacle(obs4_chosen, direction4_chosen, 3);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set x coordinate of obstacle 5")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs5_x = (JComboBox<String>) e.getSource();
			String selected_obs5_x = (String) obs5_x.getSelectedItem();
			obs5_chosen[0] = (selected_obs5_x == null) ? -1 : Integer.parseInt(selected_obs5_x);
			// int[] old_waypoint = r.getObstacle(1);
			r.setObstacle(obs5_chosen, direction5_chosen, 4);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set y coordinate of obstacle 5")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> obs5_y = (JComboBox<String>) e.getSource();
			String selected_obs5_y = (String) obs5_y.getSelectedItem();
			obs5_chosen[1] = (selected_obs5_y == null) ? -1 : Integer.parseInt(selected_obs5_y);
			// int[] old_waypoint = r.getObstacle(1);
			r.setObstacle(obs5_chosen, direction5_chosen, 4);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set direction5")) {
			// AStarPathFinder astar = new AStarPathFinder();
			JComboBox<String> direction5 = (JComboBox<String>) e.getSource();
			String selected_direction5 = (String) direction5.getSelectedItem();
			direction5_chosen = (selected_direction5 == null) ? -1
					: indexOf(new String[] { "East", "North", "West", "South" }, selected_direction5);
			r.setObstacle(obs5_chosen, direction5_chosen, 4);
			// if (!Arrays.equals(old_waypoint, r.getObstacle(0))) { enableLabel("robotView"); disableLabel("simulatedMap"); }
		}

		if (action.contentEquals("Set speed")) {
			JComboBox<String> s = (JComboBox<String>) e.getSource();
			String sp = (String) s.getSelectedItem();
			if (sp != null) {
				speed_chosen = Integer.parseInt(sp);
			}
		}

	}

	private static int indexOf(Object[] stack, Object word) {
		return Arrays.asList(stack).indexOf(word);
	}
}
