// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.util.ArrayList;
// import java.util.Arrays;
import java.util.List;

// import javax.swing.JButton;
import javax.swing.JFrame;

public class Grid {
  static final int cols = 40;
	static final int rows = 40;
	static final int cellSide = 20;
	static final int targetCount = 5;
	static int count = 0;
	static int pathCount = 0;
	static Spot start;
	static Spot end;
	static CarCell car;
	static Spot[][] grid = new Spot[cols][rows];
	static List<Spot> obstacles = new ArrayList<Spot>();
	static List<Spot> boundary = new ArrayList<Spot>();
	static List<Spot> startNodes = new ArrayList<Spot>();
	static List<Spot> endNodes = new ArrayList<Spot>();
	static List<Spot> openSet = new ArrayList<Spot>();
	static List<Spot> closeSet = new ArrayList<Spot>();
	static List<Spot> path = new ArrayList<Spot>();
	static List<List<Spot>> paths = new ArrayList<List<Spot>>();
	static List<Integer> x_path = new ArrayList<Integer>();
	static List<Integer> y_path = new ArrayList<Integer>();
	static boolean noSolution = false;
	
	
	public static void main(String args[]) {
		for(int i=0; i<cols; i++) {
			for(int j=0; j<rows; j++) {
				grid[i][j] = new Spot(i*cellSide,j*cellSide);
				count++;
				//System.out.println(grid[i][j].f);
				//System.out.println(j*cellSide);
			}
		}
		for(int i=0; i<cols; i++) {
			for(int j=0; j<rows; j++) {
				addNeighbours(grid[i][j]);
			}
		}
		for (int i=0; i<40; i++) {
			grid[39][i].wall = true;
		}
		for (int i=0; i<40; i++) {
			grid[i][39].wall = true;
		}
		
		ArrayList<int[]> myNodes = new ArrayList<int[]>();
		ArrayList<int[]> pathOrder = new ArrayList<int[]>();
		//insert user 
		int[] startNode = {0,39};
		String[] presetDirection = {"bottom", "right", "left", "top"};
		myNodes = ShortestPath.GetUserInput();
		pathOrder = ShortestPath.GetPath(startNode, myNodes);
//		targetCount = myNodes.size();
//		System.out.println(targetCount);
//		startNodes.add(grid[0][39]);
		for (int i=0; i<pathOrder.size(); i++) {
			if (i == 0) {
				startNodes.add(grid[pathOrder.get(i)[0]][pathOrder.get(i)[1]]);
			} else {
				addObstacles(grid[pathOrder.get(i)[0]][pathOrder.get(i)[1]], presetDirection[pathOrder.get(i)[2]-1]); //the obstacles
			}
		}
		
//		addObstacles(grid[0][0], "bottom"); //the obstacles
//		addObstacles(grid[20][20], "right");
//		addObstacles(grid[10][10], "bottom");
//		addObstacles(grid[35][0], "left");
//		addObstacles(grid[35][35], "top");
		/*
		 * 
0
0
1
20
20
2
10
10
1
35
0
3
35
35
4
		 */
	
		
//		System.out.println(grid[0][0].neighbours.get(2).i);
//		System.out.println(grid[0][0].neighbours.get(2).j);
//		startNodes.add(grid[0][0]);
//		startNodes.add(grid[10][0]);
//		startNodes.add(grid[10][10]);
//		startNodes.add(grid[14][14]);
//		startNodes.add(grid[19][19]);
//		endNodes.add(grid[10][0]);
//		endNodes.add(grid[10][10]);
//		endNodes.add(grid[14][14]);
//		endNodes.add(grid[19][19]);
//		endNodes.add(grid[38][38]);
		
		for(int node = 0; node<targetCount; node++) {
		start = startNodes.get(node);
		//car = new CarCell(start.i/cellSide,start.j/cellSide);
		end = endNodes.get(node);
		
		openSet.add(start);
		
		
		
		while(openSet.size() > 0) { 
			//keep going 
			int lowestIndex = 0;
			for(int i=0; i<openSet.size(); i++) {
				if(openSet.get(i).f < openSet.get(lowestIndex).f) {
					lowestIndex = i;
					
				}
			}
			Spot current = openSet.get(lowestIndex);
			//car = new CarCell(current.i/cellSide,current.j/cellSide);
			
			if(openSet.get(lowestIndex) == end) {
				Spot temp = current;
				path.add(temp);
				while(temp.previous != null) {
					System.out.println(temp.previous.i);
					path.add(temp.previous);
					temp = temp.previous;
				}
				System.out.println("DONE");
				break;
			}
			
			
			/* openSet.remove(current); */
			for(int k=openSet.size()-1; k>=0; k--) {
				if(openSet.get(k)==current)
					openSet.remove(k);
			}
			closeSet.add(current);
			
			List<Spot> neighbours = current.neighbours;
			for(int i=0; i< neighbours.size(); i++) {
				Spot neighbour = neighbours.get(i);
				
				if(!closeSet.contains(neighbour) && !neighbour.wall) {
					double tempG = current.g + 1;
					
					boolean newPath = false;
					if(openSet.contains(neighbour)) {
						if(tempG<neighbour.g) {
							neighbour.g = tempG;
							newPath = true;
						}
					}
					else {
						neighbour.g = tempG;
						newPath = true;
						openSet.add(neighbour);
					}
					
					if(newPath) {
						neighbour.h = heuristic(neighbour,end);
						neighbour.f = neighbour.g + neighbour.h;
						neighbour.previous = current;
					}
				}
				
			}
			
		} 
		openSet.removeAll(openSet);
		System.out.println(pathCount + "test");
		if(pathCount!=targetCount-1) {
			path.clear();
		}
		else {
			if(path.isEmpty()) {
				noSolution = true;
			}
		}
		pathCount++;
//		paths.add(path);
//		System.out.println(paths.size());
//		path.removeAll(path);
		
	}
//		System.out.println(paths.get(1).size()-1);
		if(noSolution == true) {
			System.out.println("no solution");
		}
		else {
			for(int k=path.size()-1 ; k>=0; k--) {
				x_path.add(path.get(k).i);
				y_path.add(path.get(k).j);
			}
			
			//}
			JFrame gui = new JFrame("Test");
			gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			gui.setVisible(true);
			gui.setSize(897,920);
			gui.setLayout(null);
			
			
			
			/*
			 * JButton right = new JButton("right"); right.setBounds(20, 700, 20, 20);
			 * gui.add(right);
			 */
			Panel panel = new Panel(new Grid(),x_path,y_path,endNodes,obstacles,boundary, true);
			panel.setBounds(40, 40, 801, 801);
			gui.add(panel);
			
			
			
		}
		
		
//		JButton right = new JButton("right");
//		right.setBounds(20, 700, 20, 20);
//		gui.add(right);
		
//		right.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		});
	}
	
	private static double heuristic(Spot neighbour, Spot end) {
		double dist =  Math. hypot(neighbour.i-end.i, neighbour.j-end.j);
		return dist;
	}


	private static void addNeighbours(Spot spot) {
		int i = spot.i/cellSide;
		//System.out.println(i);
		int j = spot.j/cellSide;
		//System.out.println(j);
		if(i<cols-1) {
			spot.neighbours.add(grid[i+1][j]);
		}
		if(i>0) {
			spot.neighbours.add(grid[i-1][j]);
		}
		if(j<rows-1) {
			spot.neighbours.add(grid[i][j+1]);
		}
		if(j>0) {
			spot.neighbours.add(grid[i][j-1]);
		}
//		if(i>0 && j >0) {
//			spot.neighbours.add(grid[i-1][j-1]);
//		}
//		if(i<cols-1 && j >0) {
//			spot.neighbours.add(grid[i+1][j-1]);
//		}
//		if(i>0 && j <rows-1) {
//			spot.neighbours.add(grid[i-1][j+1]);
//		}
//		if(i<cols-1 && j <rows-1) {
//			spot.neighbours.add(grid[i+1][j+1]);
//		}
	}
	private static void addObstacles(Spot spot, String side) {
		int i = spot.i/cellSide;
		//System.out.println(i);
		int j = spot.j/cellSide;
		//System.out.println(j);
		obstacles.add(spot);
		for(int n=0 ; n<9; n++) {
			for(int m=0 ; m<9; m++) {
				//System.out.println(j-3+m);
				if(i-4+n<40 && i-4+n>=0 && j-4+m<40 && j-4+m>=0) {
					grid[i-4+n][j-4+m].wall = true;
				}
				else
					continue;
				if(n==0||m==0)
					continue;
				boundary.add(grid[i-4+n][j-4+m]);
			}
		}
		if(side.equals("top")&& j-5>0) {
			endNodes.add(grid[i][j-5]);
			startNodes.add(grid[i][j-5]);
		}
		else if(side.equals("left")&& i-5>0) {
			endNodes.add(grid[i-5][j]);
			startNodes.add(grid[i-5][j]);
		}
		else if(side.equals("right")&& i+5<cols) {
			endNodes.add(grid[i+5][j]);
			startNodes.add(grid[i+5][j]);
		}
		else if(side.equals("bottom")&& j+5<rows) {
			endNodes.add(grid[i][j+5]);
			startNodes.add(grid[i][j+5]);
		}
		
	}
//	private static void addTargets(int side) {
//		
//	}
}