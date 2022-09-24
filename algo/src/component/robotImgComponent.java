package component;

import config.constant;

public class robotImgComponent extends imgComponent {
	// private static final long serialVersionUID = 5944902400245422120L;

	// Create the image component of the robot
	public robotImgComponent(String path, int w, int h) {
		super(path, w, h);
	}

	// Move in the direction by how many pixel, the smaller pixel will result in smoother movement
	public void moveN(int pixel) {
		int x = (int) super.getLocation().getX();
		int y = (int) super.getLocation().getY();
		if (y - pixel > constant.MARGINY + constant.GRIDHEIGHT) {
			setLocation(x, y - pixel);
		}
	}

	public void moveS(int pixel) {
		int x = (int) super.getLocation().getX();
		int y = (int) super.getLocation().getY();
		if (y + pixel < constant.MARGINY + (constant.BOARDHEIGHT - constant.ROBOTHEIGHT + 1) * constant.GRIDHEIGHT) {
			setLocation(x, y + pixel);
		}
	}

	public void moveE(int pixel) {
		int x = (int) super.getLocation().getX();
		int y = (int) super.getLocation().getY();
		if (x + pixel < constant.MARGINX + (constant.BOARDWIDTH - constant.ROBOTWIDTH) * constant.GRIDWIDTH) {
			setLocation(x + pixel, y);
		}
	}

	public void moveW(int pixel) {
		int x = (int) super.getLocation().getX();
		int y = (int) super.getLocation().getY();
		if (x - pixel > constant.MARGINX) {
			setLocation(x - pixel, y);
		}
	}

	public void moveNE(int pixel) {
		int x = (int) super.getLocation().getX();
		int y = (int) super.getLocation().getY();
		if ((x + pixel < constant.MARGINX + (constant.BOARDWIDTH - constant.ROBOTWIDTH) * constant.GRIDWIDTH)
				&& (y - pixel > constant.MARGINY + constant.GRIDHEIGHT)) {
		
			setLocation(x + pixel, y - pixel);
		}
	}

	public void moveNW(int pixel) {
		int x = (int) super.getLocation().getX();
		int y = (int) super.getLocation().getY();
		if ((x - pixel > constant.MARGINX) && (y - pixel > constant.MARGINY + constant.GRIDHEIGHT)) {
			setLocation(x - pixel, y - pixel);
		}
	}

	public void moveSE(int pixel) {
		int x = (int) super.getLocation().getX();
		int y = (int) super.getLocation().getY();
		if ((x + pixel < constant.MARGINX + (constant.BOARDWIDTH - constant.ROBOTWIDTH) * constant.GRIDWIDTH)
				&& (y + pixel < constant.MARGINY + (constant.BOARDHEIGHT - constant.ROBOTHEIGHT + 1) * constant.GRIDHEIGHT)) {
			setLocation(x + pixel, y + pixel);
		}
	}

	public void moveSW(int pixel) {
		int x = (int) super.getLocation().getX();
		int y = (int) super.getLocation().getY();
		if ((x - pixel > constant.MARGINX)
				&& (y + pixel < constant.MARGINY + (constant.BOARDHEIGHT - constant.ROBOTHEIGHT + 1) * constant.GRIDHEIGHT)) {
			setLocation(x - pixel, y + pixel);
		}
	}
}