package algo;

import java.util.ArrayList;
import java.util.List;
import ui.Command;

public class Pathing {
	
	public static List<List<Cell>> Simplify_Path(List<Cell> path)
	{
		List<List<Cell>> simplifiedPath = new ArrayList<List<Cell>>();
		List<Cell> tempPath = new ArrayList<Cell>();
		
		int directionX = 0;
		int directionY = 0;
		
		for(Cell cell : path)
		{
			if(directionX == 0 && directionY == 0)
			{
				tempPath.add(cell);
				
				if(tempPath.size() == 2)
				{
					directionX = tempPath.get(tempPath.size() - 1).getX() - tempPath.get(tempPath.size() - 2).getX();
					directionY = tempPath.get(tempPath.size() - 1).getY() - tempPath.get(tempPath.size() - 2).getY();
				}

			}
			else if(cell.getX() - tempPath.get(tempPath.size() - 1).getX() != directionX && cell.getY() - tempPath.get(tempPath.size() - 1).getY() != directionY)
			{
				directionX = cell.getX() - tempPath.get(tempPath.size() - 1).getX();
				directionY = cell.getY() - tempPath.get(tempPath.size() - 1).getY();
				
				List<Cell> subPath = new ArrayList<Cell>();
				subPath.addAll(tempPath);
				simplifiedPath.add(subPath);
				tempPath.clear();
				
				tempPath.add(cell);
			}
			else
			{
				tempPath.add(cell);
			}
		}
		
		List<Cell> subPath = new ArrayList<Cell>();
		subPath.addAll(tempPath);
		simplifiedPath.add(subPath);
		
		return simplifiedPath;
	}	
	
	public static boolean oppositeDirection(int directionX, int directionY, int _directionX, int _directionY)
	{
		if(directionX == 0 && _directionX == 0)
		{
			if(Math.abs(directionY - _directionY) == 2)
			{
				return true;
			}
		}
		
		if(directionY == 0 && _directionY == 0)
		{
			if(Math.abs(directionX - _directionX) == 2)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static double crossProduct(int directionX, int directionY, int _directionX, int _directionY)
	{	
		return directionX * _directionY - directionY * _directionX;
	}
	
	public static int whereToTurn(int directionX, int directionY, int _directionX, int _directionY)
	{
		if(Pathing.crossProduct(directionX, directionY, _directionX, _directionY) > 0) //Turn left
		{
			return 1;
		}
		else if(Pathing.crossProduct(directionX, directionY, _directionX, _directionY) < 0) //Turn right
		{
			return -1;
		}
		
		return 0;
	}
	
	public static boolean checkEmptySpace(Cell[][] grid, Cell turningPoint, int directionX, int directionY, int offset)
	{
		int x = turningPoint.getX();
		int y = turningPoint.getY();
		
		if(offset == 0)
		{
			return grid[MapConstants.MAP_ROWS - y - 1 - directionY][x + directionX].isTraversable;
		}
		
		if(offset < 0)
		{
			for(int i = 1; i <= Math.abs(offset); i++)
			{	
				if(grid[MapConstants.MAP_ROWS - y - 1 - (directionY * -i)][x + (directionX * -i)].isTraversable == false)
				{
					return false;
				}
			}
		}
		
		for(int i = 1; i <= offset; i++)
		{	
			if(grid[MapConstants.MAP_ROWS - y - 1 - (directionY * i)][x + (directionX * i)].isTraversable == false)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean checkEmptySpace2(Cell[][] grid, int[] turningPoint, int directionX, int directionY, int offset)
	{
		int x = turningPoint[0];
		int y = turningPoint[1];
		
		//Convert from point to cell
		
		x = x/10;
		y = y/10;
	
		if(offset == 0)
		{
			return grid[MapConstants.MAP_ROWS - y - 1 - directionY][x + directionX].isTraversable;
		}
		
		if(offset < 0)
		{
			for(int i = 1; i <= Math.abs(offset); i++)
			{		
				if(grid[MapConstants.MAP_ROWS - y - 1 - (directionY * -i)][x + (directionX * -i)].isTraversable == false)
				{
					return false;
				}
			}
		}
		
		for(int i = 1; i <= offset; i++)
		{	
			if(grid[MapConstants.MAP_ROWS - y - 1 - (directionY * i)][x + (directionX * i)].isTraversable == false)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean checkEmptySpace3(Cell[][] grid, int[] turningPoint, int directionX, int directionY, int offset)
	{
		int x = turningPoint[0];
		int y = turningPoint[1];
		
		//Convert from point to cell
		
		x = x/10;
		y = y/10;		
		
		System.out.println("x : " + x);
		System.out.println("y : " + y);
		
		if(offset < 0)
		{
			offset = (-offset + 10)/10;
			
			for(int i = 1; i <= Math.abs(offset); i++)
			{			
				if(grid[MapConstants.MAP_ROWS - y - 1 - (directionY * -i)][x + (directionX * -i)].isTraversable == false)
				{
					return false;
				}
			}
		}
		
		for(int i = 1; i <= offset; i++)
		{	
			offset = (offset + 10)/10;
			
			if(grid[MapConstants.MAP_ROWS - y - 1 - (directionY * i)][x + (directionX * i)].isTraversable == false)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean checkPointInCell(Cell cell, int[] point)
	{
		int x = point[0];
		int y = point[1];
		
		int cellX = cell.getX() * 10;
		int cellY = cell.getY() * 10;
		
		if(x >= cellX && x <= (cellX + 10) && y >= cellY && y <= (cellY + 10))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static Command moveForwards(Robot robot, int distance)
	{
		robot.centerX += robot.getDirectionX() * distance;
		robot.centerY += robot.getDirectionY() * distance;
		
		System.out.println("Moving forward " + distance);
		System.out.println("Robot Position: " + robot.centerX + " , " + robot.centerY); 
		
		return new Command(1, 0, distance);
	}
	
	public static Command moveBackwards(Robot robot, int distance)
	{
		robot.centerX -= robot.getDirectionX() * distance;
		robot.centerY -= robot.getDirectionY() * distance;
		
		System.out.println("Moving backwards " + distance);
		System.out.println("Robot Position: " + robot.centerX + " , " + robot.centerY);
		
		return new Command(-1, 0, distance);
	}
			
	public static Command turnLeft(Robot robot, int turningRadius)
	{
		int directionX = robot.getDirectionX();
		int directionY = robot.getDirectionY();
		int newDirectionX = robot.getDirectionX();
		int newDirectionY = robot.getDirectionY();
		
		if(directionX == 0 && directionY == 1)
		{
			newDirectionX = -1;
			newDirectionY = 0;
		}
		else if(directionX == 0 && directionY == -1)
		{
			newDirectionX = 1;
			newDirectionY = 0;
		}
		else if(directionX == 1 && directionY == 0)
		{
			newDirectionX = 0;
			newDirectionY = 1;
		}
		else if(directionX == -1 && directionY == 0)
		{
			newDirectionX = 0;
			newDirectionY = -1;
		}
		
		robot.setDirectionX(newDirectionX);
		robot.setDirectionY(newDirectionY);
		
		robot.centerX = robot.centerX + (directionX * turningRadius);
		robot.centerY = robot.centerY + (directionY * turningRadius);
		robot.centerX = robot.centerX + (newDirectionX * turningRadius);
		robot.centerY = robot.centerY + (newDirectionY * turningRadius);
		
		System.out.println("Turning left");
		System.out.println("Robot Position: " + robot.centerX + " , " + robot.centerY);
		
		return new Command(1, -1, turningRadius);
	}
	
	public static Command turnRight(Robot robot, int turningRadius)
	{
		int directionX = robot.getDirectionX();
		int directionY = robot.getDirectionY();
		int newDirectionX = robot.getDirectionX();
		int newDirectionY = robot.getDirectionY();
		
		if(directionX == 0 && directionY == 1)
		{
			newDirectionX = 1;
			newDirectionY = 0;
		}
		else if(directionX == 0 && directionY == -1)
		{
			newDirectionX = -1;
			newDirectionY = 0;
		}
		else if(directionX == 1 && directionY == 0)
		{
			newDirectionX = 0;
			newDirectionY = -1;
		}
		else if(directionX == -1 && directionY == 0)
		{
			newDirectionX = 0;
			newDirectionY = 1;
		}
		
		robot.setDirectionX(newDirectionX);
		robot.setDirectionY(newDirectionY);
		
		robot.centerX = robot.centerX + (directionX * turningRadius);
		robot.centerY = robot.centerY + (directionY * turningRadius);
		robot.centerX = robot.centerX + (newDirectionX * turningRadius);
		robot.centerY = robot.centerY + (newDirectionY * turningRadius);
		
		System.out.println("Turning right");
		System.out.println("Robot Position: " + robot.centerX + " , " + robot.centerY);
		
		return new Command(1, 1, turningRadius);
	}
	
	public static Command turnLeftBackwards(Robot robot, int turningRadius)
	{
		int directionX = robot.getDirectionX();
		int directionY = robot.getDirectionY();
		int newDirectionX = robot.getDirectionX();
		int newDirectionY = robot.getDirectionY();		
		
		if(directionX == 0 && directionY == 1)
		{
			newDirectionX = 1;
			newDirectionY = 0;
		}
		else if(directionX == 0 && directionY == -1)
		{
			newDirectionX = -1;
			newDirectionY = 0;
		}
		else if(directionX == 1 && directionY == 0)
		{
			newDirectionX = 0;
			newDirectionY = -1;
		}
		else if(directionX == -1 && directionY == 0)
		{
			newDirectionX = 0;
			newDirectionY = 1;
		}
		
		robot.setDirectionX(newDirectionX);
		robot.setDirectionY(newDirectionY);
		
		robot.centerX = robot.centerX + (-directionX * turningRadius);
		robot.centerY = robot.centerY + (-directionY * turningRadius);
		robot.centerX = robot.centerX + (-newDirectionX * turningRadius);
		robot.centerY = robot.centerY + (-newDirectionY * turningRadius);
		
		System.out.println("Turning left backwards");
		System.out.println("Robot Position: " + robot.centerX + " , " + robot.centerY); 
		
		return new Command(-1, -1, turningRadius);
	}
	
	public static Command turnRightBackwards(Robot robot, int turningRadius)
	{
		int directionX = robot.getDirectionX();
		int directionY = robot.getDirectionY();
		int newDirectionX = robot.getDirectionX();
		int newDirectionY = robot.getDirectionY();		
		
		if(directionX == 0 && directionY == 1)
		{
			newDirectionX = -1;
			newDirectionY = 0;
		}
		else if(directionX == 0 && directionY == -1)
		{
			newDirectionX = 1;
			newDirectionY = 0;
		}
		else if(directionX == 1 && directionY == 0)
		{
			newDirectionX = 0;
			newDirectionY = 1;
		}
		else if(directionX == -1 && directionY == 0)
		{
			newDirectionX = 0;
			newDirectionY = -1;
		}
		
		robot.setDirectionX(newDirectionX);
		robot.setDirectionY(newDirectionY);
		
		robot.centerX = robot.centerX + (-directionX * turningRadius);
		robot.centerY = robot.centerY + (-directionY * turningRadius);
		robot.centerX = robot.centerX + (-newDirectionX * turningRadius);
		robot.centerY = robot.centerY + (-newDirectionY * turningRadius);
		
		System.out.println("Turning right backwards");
		System.out.println("Robot Position: " + robot.centerX + " , " + robot.centerY);
		
		return new Command(-1, 1, turningRadius);
	}	
	
	public static List<int[]> getPoints(List<List<Cell>> simplifiedPath)
	{
		List<int[]> pointList = new ArrayList<int[]>();		
		
		for(int i = 0; i < simplifiedPath.size(); i++)
		{
			List<Cell> path = simplifiedPath.get(i);
			
			int[] point = new int[2];
			
			if(i == 0)
			{
				int[] startPoint = new int[2];
				
				startPoint[0] = path.get(0).getX() * 10 + 5;
				startPoint[1] = path.get(0).getY() * 10 + 5;				
				pointList.add(startPoint);
			}
				
			point[0] = path.get(path.size() - 1).getX() * 10 + 5;
			point[1] = path.get(path.size() - 1).getY() * 10 + 5;		
			
			pointList.add(point);		
		}
		
		return pointList;
	}
	
	public static List<Command> createPath(List<List<Cell>> simplifiedPath, Robot robot, Map map, Obstacle obstacleDestination)
	{
		int directionX = 0;
		int directionY = 0;
		int offset = 3; //Offset to be used when turning
		int turningRadius = 25;
		
		Cell[][] grid = map.getGrid();
		
		
		List<int[]> directionList = new ArrayList<int[]>();
		List<Command> commandList = new ArrayList<Command>();
		int[] robotPos = new int[2];			
		
		List<Cell> pathToStart = new ArrayList<Cell>();		
		
		if(robot.centerX != (simplifiedPath.get(0).get(0).getX() * 10) + 5 || robot.centerY != (simplifiedPath.get(0).get(0).getY() * 10) + 5) //If robot is not at start position, get a path to the start position
		{
			pathToStart = createPathToStart(grid, robot, simplifiedPath);
			simplifiedPath.add(0, pathToStart);			
		}

		//Add a final path to the obstacle itself
		List<Cell> finalSubPath = simplifiedPath.get(simplifiedPath.size() - 1);
		Cell finalCell = finalSubPath.get(finalSubPath.size() - 1);
		
		List<Cell> pathToDestination = createPathToDestination(grid, finalCell, obstacleDestination, 3);	//Warning setting distance from obstacle to be < 2 will cause it to break			
		
		simplifiedPath.add(pathToDestination);					
		
		for(int i = 0; i < simplifiedPath.size(); i++)
		{
			List<Cell> subPath = simplifiedPath.get(i);			
			
			if(subPath.size() > 1)
			{
				directionX = subPath.get(subPath.size() - 1).getX() - subPath.get(0).getX();
				directionY = subPath.get(subPath.size() - 1).getY() - subPath.get(0).getY();
			}
			
			else
			{
				directionX = subPath.get(0).getX() - simplifiedPath.get(i - 1).get(simplifiedPath.get(i - 1).size() - 1).getX();
				directionY = subPath.get(0).getY() - simplifiedPath.get(i - 1).get(simplifiedPath.get(i - 1).size() - 1).getY();
			}
			
			if(directionX > 0 && directionY == 0)
			{
				directionList.add(new int[] {1, 0});
			}
			
			if(directionX < 0 && directionY == 0)
			{
				directionList.add(new int[] {-1, 0});
			}
			
			if(directionX == 0 && directionY > 0)
			{
				directionList.add(new int[] {0, 1});
			}
			
			if(directionX == 0 && directionY < 0)
			{
				directionList.add(new int[] {0, -1});
			}		
			
			System.out.println("Directions: " + directionList.get(i)[0] + " , " + directionList.get(i)[1]);
		}
		
		for(int i = 0; i < simplifiedPath.size(); i++)
		{
			System.out.println("Sub path : " + i);
			for(Cell cell : simplifiedPath.get(i))
			{
				System.out.println(cell.getX() + " : " + cell.getY());
			}
		}	
		
		if(pathToStart.size() > 0)
		{
			//Fuse first 2 subpath if they are the same direction
			if(directionList.get(0)[0] == directionList.get(1)[0] && directionList.get(0)[1] == directionList.get(1)[1])
			{
				directionList.remove(0);
				simplifiedPath.remove(0);
				
				for(int i = pathToStart.size() - 1; i >= 0; i--)
				{
					simplifiedPath.get(0).add(0, pathToStart.get(i));
				}		
			}
			else if(directionList.get(0)[0] == -directionList.get(1)[0] && directionList.get(0)[1] == -directionList.get(1)[1]) //First 2 subpath opposite directions
			{

			}
			else
			{
				simplifiedPath.get(0).add(simplifiedPath.get(1).get(0));
				simplifiedPath.get(1).remove(0);
			}
		}
		
		//Fuse the last 2 subpath if they are the same direction
		if(directionList.get(directionList.size() - 1)[0] == directionList.get(directionList.size() - 2)[0] && directionList.get(directionList.size() - 1)[1] == directionList.get(directionList.size() - 2)[1])
		{
			directionList.remove(directionList.size() - 1);
			simplifiedPath.remove(simplifiedPath.size() - 1);
			
			for(Cell cell : pathToDestination)
			{
				simplifiedPath.get(simplifiedPath.size() - 1).add(cell);
			}
		}				
		
		List<int[]> pointList = getPoints(simplifiedPath);			
		
//		if(robot.centerX != pointList.get(0)[0] && robot.centerY != pointList.get(0)[1]) //If robot is not at start position, get a path to the start position
//		{
//			pathToStart = createPathToStart(grid, robot, simplifiedPath);
//			directionX = (int)robot.centerX - pointList.get(0)[0];
//			directionY = (int)robot.centerY - pointList.get(0)[1];
//			
//			if(directionY == 0)
//			{
//				directionX = directionX / Math.abs(directionX);
//			}
//			else
//			{
//				directionY = directionY / Math.abs(directionY);
//			}
//			
//			if(directionX == directionList.get(0)[0] && directionY == directionList.get(0)[1]) //Both paths same direction
//			{
//				for(int i = pathToStart.size() - 1; i > 0; i--)
//				{
//					simplifiedPath.get(0).add(0, pathToStart.get(pathToStart.size() - 1));
//				}
//			}
//			else
//			{
//				simplifiedPath.add(0, pathToStart);
//			}
//		}

		robot.centerX = pointList.get(0)[0];
		robot.centerY = pointList.get(0)[1];
		System.out.println("Robot Position: " + robot.centerX + " , " + robot.centerY);
		
		if(directionList.get(0)[0] == -directionList.get(1)[0] && directionList.get(0)[1] == -directionList.get(1)[1]) //First 2 subpath opposite directions
		{
			simplifiedPath.remove(0);
			directionList.remove(0);
			
			pointList = getPoints(simplifiedPath);
			
			int distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(0), true);
			commandList.add(moveBackwards(robot, distanceToMove));
		}
		
		for(int i = 0; i < pointList.size(); i++)
		{
			System.out.println(pointList.get(i)[0] + " point list " + pointList.get(i)[1]);
		}
		
		for(int i = 0; i < simplifiedPath.size() - 1; i++)
		{
			List<Cell> subPath = simplifiedPath.get(i);
			Cell turningPoint = subPath.get(subPath.size() - 1); //Last cell in the subpath
			
			robotPos[0] = (int)robot.centerX;
			robotPos[1] = (int)robot.centerY;
			
			System.out.println("------------------------");
			System.out.println(i);
			System.out.println("Robot direction: " + robot.getDirectionX() + " , " + robot.getDirectionY());//Debugging
			System.out.println("Path direction: " + directionList.get(i)[0] + " , " + directionList.get(i)[1]);
			System.out.println("Turning point: " + turningPoint.getX() + " , " + turningPoint.getY()); 						
			
			if(oppositeDirection(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i)[0], directionList.get(i)[1])) //Robot direction opposite as path direction
			{
				System.out.println("Robot facing wrong way");
				
//				if(checkEmptySpace(grid, turningPoint, directionList.get(i)[0], directionList.get(i)[1], offset) == true) //Check along current path, if there is space
				if(checkEmptySpace3(grid, pointList.get(i + 1), directionList.get(i)[0], directionList.get(i)[1], turningRadius) == true && !oppositeDirection(directionList.get(directionList.size() - 1)[0], directionList.get(directionList.size() - 1)[1], directionList.get(directionList.size() - 2)[0], directionList.get(directionList.size() - 2)[1])) //Check along current path, if there is space
				{	
					System.out.println("There is space along the current path to perform a move back");
					
					if(checkLength(pointList.get(i + 1), pointList.get(i + 2), true) >= turningRadius) //Next path has space
					{
						System.out.println("There is space along the next path to turn");
						
						int distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
						commandList.add(moveBackwards(robot, distanceToMove + turningRadius));
						
						if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) > 0) //Move beyond the path backwards and then turn left
						{
							System.out.println("Move back turn left");
							commandList.add(turnLeft(robot, turningRadius));						
						}
						else //Move beyond the path backwards and then turn right
						{
							System.out.println("Move back turn right");
							commandList.add(turnRight(robot, turningRadius));
						}
					}
					else if(checkLength(pointList.get(i + 1), pointList.get(i + 2), true) < turningRadius) //Next path has no space
					{
						System.out.println("There is no space along the next path to turn");
						
						int distanceToMove = turningRadius - checkLength(pointList.get(i + 1), pointList.get(i + 2), true); //Check the distance beyond the end of 2nd subpath robot needs to move
						
						if(checkEmptySpace3(grid, pointList.get(i + 2), directionList.get(i + 1)[0], directionList.get(i + 1)[1], distanceToMove)) //No obstacle blocking end of 2nd subpath, can overshoot
						{
							System.out.println("There is space to overshoot");
							
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
							commandList.add(moveBackwards(robot, distanceToMove + turningRadius));
							
							if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) > 0) //Move beyond the path backwards and then turn left
							{
								System.out.println("Move back turn left");								
								commandList.add(turnRightBackwards(robot, turningRadius));							
							}
							else //Move beyond the path backwards and then turn right
							{
								System.out.println("Move back turn right");								
								commandList.add(turnLeftBackwards(robot, turningRadius));	
							}
							
							System.out.println("Reversing the overshoot");							
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 2), true);
							commandList.add(moveBackwards(robot, distanceToMove));
						}
					}			
				}
//				else if(checkEmptySpace(grid, turningPoint, directionList.get(i + 1)[0], directionList.get(i + 1)[1], -offset) == true) //Check in the opposite direction of the next path, if there is space
				else if(checkEmptySpace3(grid, pointList.get(i + 1), directionList.get(i + 1)[0], directionList.get(i + 1)[1], turningRadius) == true && !oppositeDirection(directionList.get(directionList.size() - 1)[0], directionList.get(directionList.size() - 1)[1], directionList.get(directionList.size() - 2)[0], directionList.get(directionList.size() - 2)[1])) //Check in the opposite direction of the next path, if there is space
				{	
					System.out.println("There is space along the next path to perform a backwards turn");
					
					if(checkLength(pointList.get(i), pointList.get(i + 1), true) >= turningRadius) //There is space in current path to turn
					{
						System.out.println("There is space along the current path to perform a backwards turn");
						
						int distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
						if(distanceToMove >= turningRadius) //Needs to move to reach the turning radius
						{
							commandList.add(moveBackwards(robot, distanceToMove - turningRadius));
						}
						else //Overshot the turning radius, needs to move forward
						{
							commandList.add(moveForwards(robot, turningRadius - distanceToMove));
						}
						
						if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) > 0) //Turn Right Backwards
						{	
							commandList.add(turnRightBackwards(robot, turningRadius));
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
							commandList.add(moveForwards(robot, distanceToMove));
						}
						else //Turn Left Backwards
						{
							commandList.add(turnLeftBackwards(robot, turningRadius));
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
							commandList.add(moveForwards(robot, distanceToMove));
						}			
					}
					else //There is no space in current path to turn
					{
						//Check if there is space in front of the path to reverse to
						System.out.println("First subpath no space");
						
						int distanceToMove = turningRadius - checkLength(pointList.get(i), pointList.get(i + 1), true); //Check the distance beyond the start of first subpath robot needs to move
						
						if(checkEmptySpace3(grid, pointList.get(i), directionList.get(i)[0], directionList.get(i)[1], -distanceToMove)) //No obstacle blocking start of first subpath, can move forward to make space
						{
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
							commandList.add(moveForwards(robot, turningRadius - distanceToMove));		
							
							if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) > 0) //Turn Right Backwards
							{																
								commandList.add(turnRightBackwards(robot, turningRadius));
								distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
								commandList.add(moveForwards(robot, distanceToMove));
							}
							else //Turn Left Backwards
							{								
								commandList.add(turnLeftBackwards(robot, turningRadius));
								distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
								commandList.add(moveForwards(robot, distanceToMove));
							}	
						}
						else
						{
							//cry
							System.out.println("No possible way to do this turn (Maybe three point turn?)");
						}
					}						
				}
				else if(i == (simplifiedPath.size() - 2) && oppositeDirection(directionList.get(directionList.size() - 1)[0], directionList.get(directionList.size() - 1)[1], directionList.get(directionList.size() - 2)[0], directionList.get(directionList.size() - 2)[1]))
				{
					int distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(pointList.size() - 1), true);
					commandList.add(moveBackwards(robot, distanceToMove));
				}
				else //No space at all, keep moving backwards and try again when there is another turn
				{
					System.out.println("No space at all, check next turn");
					
					if(checkLength(pointList.get(i), pointList.get(i + 1), true) >= turningRadius && checkLength(pointList.get(i + 1), pointList.get(i + 2), true) >= turningRadius) //Enough space in both subpaths
					{
						System.out.println("Enough space");
						int distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
						
						if(distanceToMove >= turningRadius) //Needs to move to reach the turning radius
						{
							commandList.add(moveBackwards(robot, distanceToMove - turningRadius));	
						}
						else //Overshot the turning radius, needs to move forward
						{
							commandList.add(moveForwards(robot, turningRadius - distanceToMove));	
						}
						
						if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) < 0)
						{
							commandList.add(turnRightBackwards(robot, turningRadius));	
						}
						else
						{
							commandList.add(turnLeftBackwards(robot, turningRadius));	
						}
					}
					else if(checkLength(pointList.get(i), pointList.get(i + 1), true) < turningRadius && checkLength(pointList.get(i + 1), pointList.get(i + 2), true) >= turningRadius) //No space in first subpath, try and reverse to get space
					{
						System.out.println("First subpath no space");
						
						int distanceToMove = turningRadius - checkLength(pointList.get(i), pointList.get(i + 1), true); //Check the distance beyond the start of first subpath robot needs to move
						
						if(checkEmptySpace3(grid, pointList.get(i), directionList.get(i)[0], directionList.get(i)[1], -distanceToMove)) //No obstacle blocking start of first subpath, can move forward to make space
						{
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
							commandList.add(moveForwards(robot, turningRadius - distanceToMove));	
							
							if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) < 0)
							{
								commandList.add(turnRightBackwards(robot, turningRadius));	
							}
							else
							{
								commandList.add(turnLeftBackwards(robot, turningRadius));	
							}
						}
						else
						{
							//cry
							System.out.println("No possible way to do this turn (Maybe three point turn?)");
						}
					}
					else if(checkLength(pointList.get(i), pointList.get(i + 1), true) >= turningRadius && checkLength(pointList.get(i + 1), pointList.get(i + 2), true) < turningRadius) //No space in second subpath, check if there is obstacle in 2nd subpath, if not can overshoot and reverse
					{
						System.out.println("2nd subpath no space");
						
						int distanceToMove = turningRadius - checkLength(pointList.get(i + 1), pointList.get(i + 2), true); //Check the distance beyond the end of 2nd subpath robot needs to move
						
						if(checkEmptySpace3(grid, pointList.get(i + 2), directionList.get(i + 1)[0], directionList.get(i + 1)[1], distanceToMove)) //No obstacle blocking end of 2nd subpath, can overshoot
						{
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
							
							if(distanceToMove >= turningRadius) //Needs to move to reach the turning radius
							{
								commandList.add(moveBackwards(robot, distanceToMove - turningRadius));
							}
							else //Overshot the turning radius, needs to reverse
							{
								commandList.add(moveForwards(robot, turningRadius - distanceToMove));
							}	
							
							if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) < 0)
							{
								commandList.add(turnRightBackwards(robot, turningRadius));	
							}
							else
							{
								commandList.add(turnLeftBackwards(robot, turningRadius));	
							}
							
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 2), true);
							commandList.add(moveForwards(robot, distanceToMove));
						}
						else
						{
							//cry
							System.out.println("No possible way to do this turn (Maybe three point turn?)");
						}
					}
					else //No space in both subpaths
					{
						System.out.println("Both paths no space");
						if(checkEmptySpace2(grid, pointList.get(i), directionList.get(i)[0], directionList.get(i)[1], -offset)) //No obstacle blocking start of first subpath, can reverse to make space
						{								
	//							if(checkEmptySpace2(grid, pointList.get(i + 2), directionList.get(i + 1)[0], directionList.get(i + 1)[1], offset)) //No obstacle blocking end of 2nd subpath, can overshoot
	//							{						
	//								int distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
	//								commandList.add(moveForwards(robot, turningRadius - distanceToMove));		
	//								commandList.add(turnRightBackwards(robot, turningRadius));
	//								distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 2), true);									
	//								commandList.add(moveForwards(robot, distanceToMove));
	//							}
							
							int distanceToMove = turningRadius - checkLength(pointList.get(i), pointList.get(i + 1), true); //Check the distance beyond the start of first subpath robot needs to move
							
							if(checkEmptySpace3(grid, pointList.get(i), directionList.get(i)[0], directionList.get(i)[1], -distanceToMove)) //No obstacle blocking start of first subpath, can move forward to make space
							{	
								distanceToMove = turningRadius - checkLength(pointList.get(i + 1), pointList.get(i + 2), true);
								
								if(checkEmptySpace3(grid, pointList.get(i + 2), directionList.get(i + 1)[0], directionList.get(i + 1)[1], distanceToMove)) //No obstacle blocking end of 2nd subpath, can overshoot
								{						
									distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
									commandList.add(moveForwards(robot, turningRadius - distanceToMove));	
	
									if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) < 0)
									{
										commandList.add(turnRightBackwards(robot, turningRadius));	
									}
									else
									{
										commandList.add(turnLeftBackwards(robot, turningRadius));	
									}							
									
									distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 2), true);									
									commandList.add(moveForwards(robot, distanceToMove));
								}
							}
							else
							{
								//cry
								System.out.println("No possible way to do this turn (Maybe three point turn?)");
							}
						}
					}						
				}
			}
			else	//Correct orientation
			{				
				if(i != simplifiedPath.size() - 1)
				{						
					if(checkLength(pointList.get(i), pointList.get(i + 1), true) >= turningRadius && checkLength(pointList.get(i + 1), pointList.get(i + 2), true) >= turningRadius) //Enough space in both subpaths
					{
						System.out.println("Enough space in both subpaths");
						
						int distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true); //Check distance between robot and turning point
						
						if(distanceToMove >= turningRadius) //Needs to move to reach the turning radius
						{
							commandList.add(moveForwards(robot, distanceToMove - turningRadius));
						}
						else //Overshot the turning radius, needs to reverse
						{
							commandList.add(moveBackwards(robot, turningRadius - distanceToMove));															
						}
						
						if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) > 0) //Turn Left
						{
							commandList.add(turnLeft(robot, turningRadius));
						}
						else //Turn right
						{
							commandList.add(turnRight(robot, turningRadius));
						}					
					}
					else if(checkLength(pointList.get(i), pointList.get(i + 1), true) < turningRadius && checkLength(pointList.get(i + 1), pointList.get(i + 2), true) >= turningRadius) //No space in first subpath, try and reverse to get space
					{
						System.out.println("First subpath no space");
						
						int distanceToMove = turningRadius - checkLength(pointList.get(i), pointList.get(i + 1), true); //Check the distance beyond the start of first subpath robot needs to move
						
						if(checkEmptySpace3(grid, pointList.get(i), directionList.get(i)[0], directionList.get(i)[1], -distanceToMove)) //No obstacle blocking start of first subpath, can reverse to make space
						{
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
							commandList.add(moveBackwards(robot, turningRadius - distanceToMove));		
							
							if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) > 0) //Turn Left
							{
								commandList.add(turnLeft(robot, turningRadius));
							}
							else //Turn right
							{
								commandList.add(turnRight(robot, turningRadius));
							}		
						}
						else
						{
							//cry
							System.out.println("No possible way to do this turn (Maybe three point turn?)");
						}
					}
					else if(checkLength(pointList.get(i), pointList.get(i + 1), true) >= turningRadius && checkLength(pointList.get(i + 1), pointList.get(i + 2), true) <= turningRadius) //No space in second subpath, check if there is obstacle in 2nd subpath, if not can overshoot and reverse
					{
						System.out.println("2nd subpath no space");
						
						int distanceToMove = turningRadius - checkLength(pointList.get(i + 1), pointList.get(i + 2), true); //Check the distance beyond the end of 2nd subpath robot needs to move
						
						System.out.println(distanceToMove);
						System.out.println(pointList.get(i + 2)[0] + " " + pointList.get(i + 2)[1]);
						
						if(checkEmptySpace3(grid, pointList.get(i + 2), directionList.get(i + 1)[0], directionList.get(i + 1)[1], distanceToMove)) //No obstacle blocking end of 2nd subpath, can overshoot
						{
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
							
							if(distanceToMove >= turningRadius) //Needs to move to reach the turning radius
							{
								commandList.add(moveForwards(robot, distanceToMove - turningRadius));
							}
							else //Overshot the turning radius, needs to reverse
							{
								commandList.add(moveBackwards(robot, turningRadius - distanceToMove));
							}
							
							if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) > 0) //Turn Left
							{
								commandList.add(turnLeft(robot, turningRadius));
							}
							else //Turn right
							{
								commandList.add(turnRight(robot, turningRadius));
							}		
							
							distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 2), true);
							commandList.add(moveBackwards(robot, distanceToMove));
						}
						else
						{
							//cry
							System.out.println("No possible way to do this turn (Maybe three point turn?)");
						}
					}
					else //No space in both subpaths
					{
						System.out.println("Both paths no space");
						
						int distanceToMove = turningRadius - checkLength(pointList.get(i), pointList.get(i + 1), true); //Check the distance beyond the start of first subpath robot needs to move
						
						if(checkEmptySpace3(grid, pointList.get(i), directionList.get(i)[0], directionList.get(i)[1], -distanceToMove)) //No obstacle blocking start of first subpath, can reverse to make space
						{	
							distanceToMove = turningRadius - checkLength(pointList.get(i + 1), pointList.get(i + 2), true);
							
							if(checkEmptySpace3(grid, pointList.get(i + 2), directionList.get(i + 1)[0], directionList.get(i + 1)[1], distanceToMove)) //No obstacle blocking end of 2nd subpath, can overshoot
							{						
								distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 1), true);
								commandList.add(moveBackwards(robot, turningRadius - distanceToMove));	

								if(crossProduct(robot.getDirectionX(), robot.getDirectionY(), directionList.get(i + 1)[0], directionList.get(i + 1)[1]) > 0) //Turn Left
								{
									commandList.add(turnLeft(robot, turningRadius));
								}
								else //Turn right
								{
									commandList.add(turnRight(robot, turningRadius));
								}									
								
								distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(i + 2), true);									
								commandList.add(moveBackwards(robot, distanceToMove));
							}
						}
						else
						{
							//cry
							System.out.println("No possible way to do this turn (Maybe three point turn?)");
						}
					}					
				}				
			}
		}
		
		if(robot.getDirectionX() != directionList.get(directionList.size() - 1)[0] && robot.getDirectionY() != directionList.get(directionList.size() - 1)[1])  //No more turns left but robot is still not in the correct direction
		{
			//Do emergency turning
		}
		else
		{
			int distanceToMove = checkLength(new int[] {(int)robot.centerX, (int)robot.centerY}, pointList.get(pointList.size() - 1), true);
			commandList.add(moveForwards(robot, distanceToMove));
		}		
		
		if(robot.centerX != pointList.get(pointList.size() - 1)[0] && robot.centerY != pointList.get(pointList.size() - 1)[1])
		{
			System.out.println("Could not reach destination");
		}
		
		System.out.println("----------------------------------------------------------------");
		return commandList;
	}
	
	public static int checkLength(int[] p1, int[] p2, boolean abs)
	{
		if(abs == true)
		{
			if(p1[0] - p2[0] == 0)
			{	
				return Math.abs(p1[1] - p2[1]);
			}
			else
			{				
				return Math.abs(p1[0] - p2[0]);
			}
		}
		else
		{
			if(p1[0] - p2[0] == 0)
			{
				return Math.abs(p1[1] - p2[1]);
			}
			else
			{
				return Math.abs(p1[0] - p2[0]);
			}
		}
	}
	public static List<Cell> createPathToDestination(Cell[][] grid, Cell finalCell, Obstacle obstacleDestination, int distanceFromObstacle)
	{
		int k;
		List<Cell> pathToDestination = new ArrayList<Cell>();		
		
		switch(obstacleDestination.getDirection())
		{
		case('N'):
			
			k = Math.abs(finalCell.getY() - obstacleDestination.getY());			
		
			while(k > distanceFromObstacle)
			{
				pathToDestination.add(grid[MapConstants.MAP_ROWS - 1 - obstacleDestination.getY() - k + 1][obstacleDestination.getX()]);
				k--;
			}
			
			break;
		case('S'):
			
			k = Math.abs(finalCell.getY() - obstacleDestination.getY());		
			
			while(k > distanceFromObstacle)
			{
				pathToDestination.add(grid[MapConstants.MAP_ROWS - 1 - obstacleDestination.getY() + k - 1][obstacleDestination.getX()]);
				k--;
			}
			break;
		case('E'):
			
			k = Math.abs(finalCell.getX() - obstacleDestination.getX());
				
			while(k > distanceFromObstacle)
			{
				pathToDestination.add(grid[MapConstants.MAP_ROWS - 1 - obstacleDestination.getY()][obstacleDestination.getX() + k - 1]);
				k--;
			}
			
			break;
		case('W'):
			
			k = Math.abs(finalCell.getX() - obstacleDestination.getX());		
		
			while(k > distanceFromObstacle)
			{
				pathToDestination.add(grid[MapConstants.MAP_ROWS - 1 - obstacleDestination.getY()][obstacleDestination.getX() - k + 1]);
				k--;
			}
			
			break;
		}
		
		return pathToDestination;
	}
	
	public static List<Cell> createPathToStart(Cell[][] grid, Robot robot, List<List<Cell>> simplifiedPath) 
	{
		List<Cell> pathToStart = new ArrayList<Cell>();
		
		int _startX = (int)robot.centerX / 10;
		int _startY = (int)robot.centerY / 10;
		
		int startX = simplifiedPath.get(0).get(0).getX();
		int startY = simplifiedPath.get(0).get(0).getY();	
		
		int directionX = startX - _startX;
		int directionY = startY - _startY;
		
		int numberOfCells;
		
		if(directionY == 0)
		{
			numberOfCells = Math.abs(directionX);
			directionX = directionX / Math.abs(directionX);
		}
		else
		{
			numberOfCells = Math.abs(directionY);
			directionY = directionY / Math.abs(directionY);
		}	
		
		for(int i = 0; i < numberOfCells; i++)
		{
			pathToStart.add(grid[MapConstants.MAP_ROWS - 1 - _startY - (i * directionY)][_startX + (i * directionX)]);
		}
		
		return pathToStart;
	}
}
