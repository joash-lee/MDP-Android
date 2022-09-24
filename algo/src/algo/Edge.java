package algo;

public class Edge {
  private int weight;
  private Node startNode;
  private Node endNode;

  public Edge(int weight, Node startNode, Node endNode) {
    this.weight = weight;
    this.startNode = startNode;
    this.endNode = endNode;
  }

  //Set the weight of the edge
  public void setWeight(int weight) {
    this.weight = weight;
  }

  //Get the weight of the edge
  public int getWeight() {
    return weight;
  }

  //Get the start node
  public Node getNodeStart() {
    return startNode;
  }

  //Get the end node
  public Node getNodeEnd() {
    return endNode;
  }
}