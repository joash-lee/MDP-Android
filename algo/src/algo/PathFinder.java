package algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import robot.robot;
import map.Map;

public class PathFinder {
	private robot r;
	private Map m;
	
	public static void ShortestP(Node sourceNode){
		sourceNode.setDist(0); // set source node cost as 0
		sourceNode.setDir(1); // set source direction as North
		PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
		priorityQueue.add(sourceNode);
		sourceNode.setVisited(true);
        
		while( !priorityQueue.isEmpty() ){
			Node actualNode = priorityQueue.poll();

			for(Edge edge : actualNode.getList()){
				Node n = edge.getNodeEnd();
				int nodeDir = n.getDir();

				if (!n.Visited()) {
					double newDistance = actualNode.getDist() + edge.getWeight();
					if ( newDistance < n.getDist()) {
						priorityQueue.remove(n);
						n.setDist(newDistance);
						n.setPr(actualNode);
						priorityQueue.add(n);
					}
				}
			}
			actualNode.setVisited(true);
		}
	}
 
	public static List<Node> getShortestP(Node targetNode){
		List<Node> path = new ArrayList<>();
		for(Node node=targetNode; node!=null; node=node.getPr()){
			path.add(node);
			System.out.println(node);
		}
		Collections.reverse(path);
		return path;
	}

	// movement of robot for ENWS directions when robot's facing is set as forward
	private void robotPathMovement(robot robot, List<Node> path) {
        
        // Move the robot based on the path
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).getDir() == 0) {
                 robot.forward(3);
				 robot.backwardRight();
				 robot.forward(3);
            } else if (path.get(i).getDir() == 1) {
                robot.forward(1);
            } else if (path.get(i).getDir() == 2) {
                robot.forward(3);
				 robot.backwardLeft();
				 robot.forward(3);
            } else if (path.get(i).getDir() == 3){
                robot.backward(1);
            } else {
                return;
            }
        }
    }
}
