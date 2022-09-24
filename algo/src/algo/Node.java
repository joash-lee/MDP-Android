package algo;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    private boolean visited;
	private int[] node;
	private List<Edge> List;
	private double dist = Double.MAX_VALUE;
	private Node pr;
    private int dir;
	
 
	public Node(int[] node) {
		this.node = node;
		this.List = new ArrayList<>();
	}
	
	public List<Edge> getList() {
		return List;
	}
 
	// public int[] getNode(Boolean dir) {
	// 	return (dir ? node : new int[] { node[0], node[1] });
	// }
	// public int[] getNode() {
	// 	return getNode(false);
	// }

	public int[] getNode() {
		return this.node;
	}
 
	public void setList(List<Edge> List) {
		this.List = List;
	}
	
	public void addNeighbour(Edge edge) {
		this.List.add(edge);
	}
	
	public boolean Visited() {
		return visited;
	}
 
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
 
	public Node getPr() {
		return pr;
	}
 
	public void setPr(Node pr) {
		this.pr = pr;
	}

    public double getDist() {
		return dist;
	}

    public void setDist(double dist) {
		this.dist = dist;
	}

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

	@Override
	public String toString() {
		return this.node[0] + ", " + this.node[1] + ", " + this.node[2];
	}

	@Override
	public int compareTo(Node otherNode) {
		return Double.compare(this.dist, otherNode.getDist());
	}
}
