package algo;

public class Cell {
	public static int BLOCKWIDTH = 10, BLOCKHEIGHT = 10; // 10x10 cell
	public int x0, y0, x1, y1, x2, y2, x3, y3;
	private double fCost, gCost, hCost, dCost;
    protected Cell parent;
    protected boolean isTraversable;
    protected boolean isObstacle;
    protected boolean isVirtualWall;
    
    // Creating cell based on 4 points
    public Cell(int x, int y) {
        this.x0 = x;
        this.y0 = y;
        this.x1 = x0 + BLOCKWIDTH;
        this.y1 = y0;
        this.x2 = x0;
        this.y2 = y0 + BLOCKHEIGHT;
        this.x3 = x1;
        this.y3 = y2;
        isTraversable = true;
        isObstacle = false;
        isVirtualWall = false;
    }
    
    public int getX() {
        return x0;
    }

    public int getY() {
        return y0;
    }
    
    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public void setObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }
    
    public boolean getIsObstacle() {
        return isObstacle;
    }

    public boolean getTraversable() {
        return isTraversable;
    }

    public void setTraversable(boolean isOccupied) {
        this.isTraversable = isOccupied;
    }

    public void setVirtualWall(boolean isVirtualWall) {
        if (isVirtualWall) {
            this.isVirtualWall = true;
        } else {
            if (x1 != 0 && x1 != MapConstants.MAP_COLS - 1 && y1 != 0 && y1 != MapConstants.MAP_ROWS - 1) {
                this.isVirtualWall = false;
            }
        }
    }
    
    /**
     * Updates the G Cost of a Node without diagonal distances.
     */
    public void updateManGCost(Cell source) {
        gCost = Math.abs((x0 - source.getX())) + Math.abs((y0 - source.getY()));
    }

    public void updateManHCost(Cell destination) {
        hCost = Math.abs(x0 - destination.getX()) + Math.abs(y0 - destination.getY());
    }

    public void updateEuclidGCost(Cell source) {
        gCost = Math.sqrt(Math.pow(x0 - source.getX(), 2) + Math.pow(y0 - source.getY(), 2));
    }

    public void updateEuclidHCost(Cell destination) {
        hCost = Math.sqrt(Math.pow(x0 - destination.getX(), 2) + Math.pow(y0 - destination.getY(), 2));
    }

    public void updateFCost() {
        fCost = gCost + hCost;
    }
    
    public double getGCost() {
        return gCost;
    }

    public void setGCost(double gCost) {
        this.gCost = gCost;
    }

    public double getHCost() {
        return hCost;
    }

    public double getFCost() {
        return fCost;
    }

    public void setFCost(double fCost) {
        this.fCost = fCost;
    }

    public double getDCost() {
        return dCost;
    }

    public void setDCost(double dCost) {
        this.dCost = dCost;
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %d)", x0, y0);
    }
}
