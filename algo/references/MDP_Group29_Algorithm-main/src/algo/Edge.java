package algo;

public class Edge {
	private int source, dest;
    private double weight;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Edge(int source, int dest, double weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }
}
