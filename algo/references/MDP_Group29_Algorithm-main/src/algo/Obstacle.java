package algo;

public class Obstacle extends Cell {
    // To indicate North, South, East, or West.
    private final char direction;

    public Obstacle(int x, int y, char direction) {
        super(x, y);
        isObstacle = true;
        isTraversable = false;
        this.direction = direction;
    }

    public char getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d), %c", x0, y0, direction);
    }
}