package ui;

import java.util.ArrayList;
import java.util.List;

import algo.Cell;
import algo.Obstacle;
import algo.Pathing;
import algo.Robot;
import algo.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class UIRobot {
	
	private Rectangle robot = new Rectangle();
	private double x, y, panelHeight, scaling, _x, _y;
	private char direction = 'W';
	private double duration = 2000;
	private double arc = 100;
	public boolean finished = false;
	
	public UIRobot (Pane pane, double panelHeight, double scaling, char direction, double startX, double startY) {
		
		this.panelHeight = panelHeight;
		this.scaling = scaling;
		    	
		this.direction = direction;
		
    	this.setX(startX * scaling);
    	this.setY((panelHeight - startY) * scaling);
    	
    	robot.setX(startX * scaling);
    	robot.setY((panelHeight - startY) * scaling);
    	robot.setWidth(20 * scaling);
    	robot.setHeight(20 * scaling);   	
    	
    	Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
        LinearGradient lg1 = new LinearGradient(1, 0, 0, 0, true, CycleMethod.NO_CYCLE, stops);
    	
        robot.setFill(lg1);
    	
		pane.getChildren().add(robot);
	}
	
	public Rectangle getRobot() {
		return robot;
	}
	
	public SequentialTransition executeCommand(List<Command> commandList)
	{
		List<PathTransition> pathTransitionList = new ArrayList<PathTransition>();		
		
		for(int i = 0; i < commandList.size(); i++)
		{
			Command command = commandList.get(i);
			Path path = new Path();
			
			if(command.getParameter() == 0)
			{
				continue;
			}			

			_x = x;
			_y = y;
			
			if(command.getForwardBack() == 1)
			{							
				if(command.getLeftRight() == 0)
				{						
					
					path.getElements().add(new MoveTo(_x, _y));
					
					switch(direction)
					{
					case 'N':
						y -= command.getParameter() * scaling;
						break;
					case 'S':
						y += command.getParameter() * scaling;
						break;
					case 'E':
						x += command.getParameter() * scaling;
						break;
					case 'W':
						x -= command.getParameter() * scaling;
						break;	
					}

					path.getElements().add(new LineTo(x, y));
					
					PathTransition pt = new PathTransition();
					pt.setNode(robot);
					pt.setDuration(Duration.millis(duration));
					pt.setPath(path);
					pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
					pathTransitionList.add(pt);
				}
				else if(command.getLeftRight() == -1)
				{	
					ArcTo arcTo = new ArcTo();
					arcTo.setRadiusX(command.getParameter()  * scaling);
					arcTo.setRadiusY(command.getParameter()  * scaling);
					
					switch(this.direction)
					{
					case('N'):
						x -= command.getParameter() * scaling;
						y -= command.getParameter() * scaling;
						direction = 'W';
						break;
					case('S'):
						x += command.getParameter() * scaling;
						y += command.getParameter() * scaling;
						direction = 'E';
						break;
					case('E'):
						x += command.getParameter() * scaling;
						y -= command.getParameter() * scaling;
						direction = 'N';
						break;
					case('W'):
						x -= command.getParameter() * scaling;
						y += command.getParameter() * scaling;
						direction = 'S';
						break;
					}					
					
					arcTo.setX(x);
					arcTo.setY(y);
					
					path.getElements().add(new MoveTo(_x, _y));
					path.getElements().add(arcTo);	
					
					PathTransition pt = new PathTransition();
					pt.setNode(robot);
					pt.setDuration(Duration.millis(duration));
					pt.setPath(path);
					pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
					pathTransitionList.add(pt);
				}
				else
				{
					ArcTo arcTo = new ArcTo();
					arcTo.setRadiusX(command.getParameter()  * scaling);
					arcTo.setRadiusY(command.getParameter()  * scaling);
					arcTo.setSweepFlag(true);
					
					switch(this.direction)
					{
					case('N'):
						x += command.getParameter() * scaling;
						y -= command.getParameter() * scaling;
						direction = 'E';
						break;
					case('S'):
						x -= command.getParameter() * scaling;
						y += command.getParameter() * scaling;
						direction = 'W';
						break;
					case('E'):
						x += command.getParameter() * scaling;
						y += command.getParameter() * scaling;
						direction = 'S';
						break;
					case('W'):
						x -= command.getParameter() * scaling;
						y -= command.getParameter() * scaling;
						direction = 'N';
						break;
					}					
					
					arcTo.setX(x);
					arcTo.setY(y);
					
					path.getElements().add(new MoveTo(_x, _y));
					path.getElements().add(arcTo);	
					
					PathTransition pt = new PathTransition();
					pt.setNode(robot);
					pt.setDuration(Duration.millis(duration));
					pt.setPath(path);
					pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
					pathTransitionList.add(pt);
				}
			}
			else //Backwards
			{
				if(command.getLeftRight() == 0)
				{				
					switch(direction)
					{
					case 'N':
						y += command.getParameter() * scaling;
						break;
					case 'S':
						y -= command.getParameter() * scaling;
						break;
					case 'E':
						x -= command.getParameter() * scaling;
						break;
					case 'W':
						x += command.getParameter() * scaling;
						break;	
					}
				
					path.getElements().add(new MoveTo(x, y));								
					
					path.getElements().add(new LineTo(_x, _y));
										
					PathTransition pt = new PathTransition();
					pt.setNode(robot);
					pt.setDuration(Duration.millis(duration));
					pt.setPath(path);
					pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
					pt.setInterpolator(ReverseInterpolator.reverse(pt.getInterpolator()));
					pathTransitionList.add(pt);
				}
				else if(command.getLeftRight() == -1)
				{	
					ArcTo arcTo = new ArcTo();
					arcTo.setRadiusX(arc);
					arcTo.setRadiusY(arc);
					
					switch(this.direction)
					{
					case('N'):
						x -= command.getParameter() * scaling;
						y += command.getParameter() * scaling;
						direction = 'E';
						break;
					case('S'):
						x += command.getParameter() * scaling;
						y -= command.getParameter() * scaling;
						direction = 'W';
						break;
					case('E'):
						x -= command.getParameter() * scaling;
						y -= command.getParameter() * scaling;
						direction = 'S';
						break;
					case('W'):
						x += command.getParameter() * scaling;
						y += command.getParameter() * scaling;					
						direction = 'N';
						break;
					}		
					
					arcTo.setX(_x);
					arcTo.setY(_y);
					
					path.getElements().add(new MoveTo(x, y));
					path.getElements().add(arcTo);
					
					PathTransition pt = new PathTransition();
					pt.setNode(robot);
					pt.setDuration(Duration.millis(duration));
					pt.setPath(path);
					pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
					pt.setInterpolator(ReverseInterpolator.reverse(pt.getInterpolator()));
					pathTransitionList.add(pt);
				}
				else
				{
					ArcTo arcTo = new ArcTo();
					arcTo.setRadiusX(arc);
					arcTo.setRadiusY(arc);
					arcTo.setSweepFlag(true);
					
					switch(this.direction)
					{
					case('N'):
						x += command.getParameter() * scaling;
						y += command.getParameter() * scaling;
						direction = 'W';
						break;
					case('S'):
						x -= command.getParameter() * scaling;
						y -= command.getParameter() * scaling;
						direction = 'E';
						break;
					case('E'):
						x -= command.getParameter() * scaling;
						y += command.getParameter() * scaling;
						direction = 'N';
						break;
					case('W'):
						x += command.getParameter() * scaling;
						y -= command.getParameter() * scaling;
						direction = 'S';
						break;
					}					
					
					arcTo.setX(_x);
					arcTo.setY(_y);
					
					path.getElements().add(new MoveTo(x, y));	
					path.getElements().add(arcTo);
					
					PathTransition pt = new PathTransition();
					pt.setNode(robot);
					pt.setDuration(Duration.millis(duration));
					pt.setPath(path);
					pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
					pt.setInterpolator(ReverseInterpolator.reverse(pt.getInterpolator()));
					pathTransitionList.add(pt);
				}
			}
		}
		
		SequentialTransition st = new SequentialTransition();

		for(PathTransition pathT : pathTransitionList)
		{
			st.getChildren().add(pathT);
		}
		st.setCycleCount(1);
		
//		st.play();
		
		return st;
	}
	
	public void executeCommands(List<List<Cell>> aPath, Robot robot, Map map, List<Obstacle> obstacleList)
	{		
		SequentialTransition finalAnimation = new SequentialTransition();		
		
		for(int i = 0; i < aPath.size(); i++)
		{
			try {
				finalAnimation.getChildren().add(executeCommand(Pathing.createPath(Pathing.Simplify_Path(aPath.get(i)), robot, map, obstacleList.get(i))));
			} catch (Exception e) {
				System.out.println("Issues with path");
				break;
			}
		}
		
		finalAnimation.play();
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
}
