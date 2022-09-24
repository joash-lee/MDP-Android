package component;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class imgComponent extends JLabel {
	// private static final long serialVersionUID = -3071360136132074019L;
	private ImageIcon imageIcon;
	private int height, width;
	public abstract void moveN (int pixel);
	public abstract void moveS (int pixel);
	public abstract void moveE (int pixel);
	public abstract void moveW (int pixel);
	public abstract void moveNE (int pixel);
	public abstract void moveNW (int pixel);
	public abstract void moveSE (int pixel);
	public abstract void moveSW (int pixel);

	// Take the path of the image, width and height to be displayed
	public imgComponent(String path, int w, int h) {
		this.height = h;
		this.width = w;
		setImage(path);
	}

	// Get the image from the path and scale it based on the dimension specified.
	public void setImage(String path) {
		setVisible(false);
		imageIcon = new ImageIcon(path);
		setSize(width, height);
		setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
		setVisible(true);
	}

	// Define a general movement function to allow the Image to move
	public void moveTo(int pixel, int heading) {
		if (heading == 0 || heading == 12) {
			moveE(pixel);
		} else if (heading == 1 || heading == 13) {
			moveN(pixel);
		} else if (heading == 2 || heading == 10) {
			moveW(pixel);
		} else if (heading == 3 || heading == 11) {
			moveS(pixel);
		} else if (heading == 4 || heading == 14) {
			moveNE(pixel);
		} else if (heading == 5 || heading == 15) {
			moveNW(pixel);
		} else if (heading == 6 || heading == 16) {
			moveSW(pixel);
		} else if (heading == 7 || heading == 17) {
			moveSE(pixel);
		}
	}

	// public int getWidth() {
	// 	return this.width;
	// }

	// public int getHeight() {
	// 	return this.height;
	// }
}