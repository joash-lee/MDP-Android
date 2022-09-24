package algo;

public class Robot {
    protected Map map;
    //protected int x, y; // coordinates of lower left corner of robot
    public static int BLOCKWIDTH = 20, BLOCKHEIGHT = 20;
    public double x0, y0; // centre of robot
    public double centerX, centerY;
	private char direction;
	private int directionX, directionY;

    public Robot(double startPosX, double startPosY, char direction) {
    	x0 = startPosX;
		y0 = startPosY;
        this.direction = direction;
        
        switch(direction)
        {
        case('N'):
        	directionX = 0;
        	directionY = 1;
        	break;
        case('S'):
        	directionX = 0;
    		directionY = -1;
        	break;
        case('E'):
        	directionX = 1;
    		directionY = 0;
        	break;
        case('W'):
        	directionX = -1;
    		directionY = 0;
        	break;
        }
    }

    public void setDirection(char direction) {
    	this.direction = direction;
    	
    	switch(direction)
        {
        case('N'):
        	directionX = 0;
        	directionY = 1;
        	break;
        case('S'):
        	directionX = 0;
    		directionY = -1;
        	break;
        case('E'):
        	directionX = 1;
    		directionY = 0;
        	break;
        case('W'):
        	directionX = -1;
    		directionY = 0;
        	break;
        }
	}

	public char getDirection() {
    	return direction;
	}
	
	public int getDirectionX() {
    	return directionX;
	}
	
	public int getDirectionY() {
    	return directionY;
	}
	
	public void setDirectionX(int directionX) {
		this.directionX = directionX;
	}
	
	public void setDirectionY(int directionY) {
		this.directionY = directionY;
	}

    // TODO: add more stuff!
}
