package ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tester {
	
	public static Block[] blocks = new Block[5];
	
	
	public Tester() {	
		
		blocks[0] = new Block(20, 60, Direction.North);
		blocks[1] = new Block(150, 100, Direction.East);
		blocks[2] = new Block(80, 40, Direction.East);
		blocks[3] = new Block(120, 80, Direction.West);
		blocks[4] = new Block(190, 190, Direction.South);
		
		JFrame frame = new JFrame("Algorithm Tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Panel panel = new Panel();
		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		

	}
	
	public static void main(String[] args) {
		new Tester();
		
		List<Grid> path = new ArrayList<Grid>();
		path.add(new Grid(10, 10, 0, false));
		path.add(new Grid(20, 10, 0, false));
		path.add(new Grid(30, 10, 0, false));
		path.add(new Grid(40, 10, 0, false));
		path.add(new Grid(40, 20, 0, false));
		path.add(new Grid(40, 30, 0, false));
		path.add(new Grid(30, 30, 0, false));
		path.add(new Grid(20, 30, 0, false));
		
		List<List<Grid>> simplifiedPath = new ArrayList<List<Grid>>();
		simplifiedPath = Pathing.Simplify_Path(path);
		
		for(List<Grid> subPath : simplifiedPath)
		{
			
//			System.out.println(simplifiedPath.size());
			System.out.println("----------");
			
			for(Grid grid : subPath)
			{
				System.out.println(grid.x + " " + grid.y);
			}
			
		}
	}
}
