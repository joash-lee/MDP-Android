package ui;

import java.util.ArrayList;
import java.util.List;

public class Pathing {
	
	public void Process_Path(Grid[] path, Robot robot)
	{
		
		int[] direction = new int[2];
		direction[0] = 0;
		direction[1] = 0;
		int tempX, tempY, continuous = 0;
		
		for(int i = 0; i < path.length; i++) //Analyze initial direction
		{
		
			if(i + 1 < path.length)//Check if it is the last grid
			{
				tempX = (path[i + 1].x - path[i].x) / 10;
				tempY = (path[i + 1].y - path[i].y) / 10;
				
				if(tempX != direction[0] && tempY != direction[1]) //Change in direction
				{
					
					continuous = 0;
					
					if(robot.direction == Direction.North)//Check orientation of robot to determine the direction its going to be moving
					{
						if(tempX == 0 && tempY == 1) //Path going north
						{
							
						}
						else if(tempX == 0 && tempY == -1) //Path going south
						{
							
						}
						else if(tempX == 1 && tempY == 0) //Path going east
						{
							
						}
						else if(tempX == -1 && tempY == 0) //Path going west
						{
							
						}
					}
					else if(robot.direction == Direction.South)
					{
						if(tempX == 0 && tempY == 1) //Path going north
						{
							
						}
						else if(tempX == 0 && tempY == -1) //Path going south
						{
							
						}
						else if(tempX == 1 && tempY == 0) //Path going east
						{
							
						}
						else if(tempX == -1 && tempY == 0) //Path going west
						{
							
						}
					}
					else if(robot.direction == Direction.East)
					{
						if(tempX == 0 && tempY == 1) //Path going north
						{
							
						}
						else if(tempX == 0 && tempY == -1) //Path going south
						{
							
						}
						else if(tempX == 1 && tempY == 0) //Path going east
						{
							
						}
						else if(tempX == -1 && tempY == 0) //Path going west
						{
							
						}
					}
					else if(robot.direction == Direction.West)
					{
						if(tempX == 0 && tempY == 1) //Path going north
						{
							
						}
						else if(tempX == 0 && tempY == -1) //Path going south
						{
							
						}
						else if(tempX == 1 && tempY == 0) //Path going east
						{
							
						}
						else if(tempX == -1 && tempY == 0) //Path going west
						{
							
						}
					}
				}
				else //No change in direction
				{
					continuous++;
				}
			}
		}
	}

	public void reverseTurn()
	{
		
	}
	
	public static List<List<Grid>> Simplify_Path(List<Grid> path)
	{
		List<List<Grid>> simpliedPath = new ArrayList<List<Grid>>();
		List<Grid> tempPath = new ArrayList<Grid>();
		
		int directionX = 0;
		int directionY = 0;
		
		for(Grid grid : path)
		{
			if(directionX == 0 && directionY == 0)
			{
				tempPath.add(grid);
				
				if(tempPath.size() == 2)
				{
					directionX = tempPath.get(tempPath.size() - 1).x - tempPath.get(tempPath.size() - 2).x;
					directionY = tempPath.get(tempPath.size() - 1).y - tempPath.get(tempPath.size() - 2).y;
				}

			}
			else if(grid.x - tempPath.get(tempPath.size() - 1).x != directionX && grid.y - tempPath.get(tempPath.size() - 1).y != directionY)
			{
				
				directionX = grid.x - tempPath.get(tempPath.size() - 1).x;
				directionY = grid.y - tempPath.get(tempPath.size() - 1).y;
				
				List<Grid> subPath = new ArrayList<Grid>();
				subPath.addAll(tempPath);
				simpliedPath.add(subPath);
				tempPath.clear();
				
				tempPath.add(grid);
			}
			else
			{
				tempPath.add(grid);
			}
		}
		
		List<Grid> subPath = new ArrayList<Grid>();
		subPath.addAll(tempPath);
		simpliedPath.add(subPath);
		
		return simpliedPath;
	}
}
