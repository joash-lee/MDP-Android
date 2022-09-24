package timertask;

import java.util.TimerTask;
import component.imgComponent;

public class taskMoveImg extends TimerTask {
	private imgComponent item;
	private int dirCommand;
	private int pixel;

	public taskMoveImg(imgComponent i, int d, int p) {
		item = i;
		dirCommand = d;
		pixel = p;
	}

	@Override
	public void run() {
		item.moveTo(pixel, dirCommand);
	}
}