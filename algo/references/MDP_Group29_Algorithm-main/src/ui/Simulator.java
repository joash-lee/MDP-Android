package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import algo.*;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class Simulator extends Application {
	
	double width = 200;
	double height = 200;
	double scaling = 4;
	
	public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
    	
    	Pane pane = new Pane();
    	
    	Scene scene = new Scene(pane, width * scaling, height * scaling);   	
        
        Map testMap = new Map();
        // Set test obstacles.
        testMap.setObstacle(2, 16, 'E');
        testMap.setObstacle(5, 9, 'S');
        testMap.setObstacle(9, 12, 'W');
        testMap.setObstacle(11, 13, 'E');
        testMap.setObstacle(17, 2, 'N');
        testMap.assignNodeNumbers();
        testMap.printMap();
        double[][] distances = testMap.computeDistances();
        
        List<Obstacle> obstacleList = new ArrayList<Obstacle>();  //Match with the obstacles in map, order matters, it is the order of the obstacles that the robot visits
        
        obstacleList.add(new Obstacle(5, 9, 'S'));
        obstacleList.add(new Obstacle(9, 12, 'W'));
        obstacleList.add(new Obstacle(2, 16, 'E'));
        obstacleList.add(new Obstacle(11, 13, 'E'));
        obstacleList.add(new Obstacle(17, 2, 'N'));
		
		placeObstacles(obstacleList, pane);		//Adds obstacles to the GUI

        // Adding edges to the nodes.
        List<Edge> edges = generateEdges(testMap.getNodeNumbers(), distances);

        // Initialize total number of nodes in the graph.
        int noOfNodes = testMap.getNodeNumbers().size();

        // Build a graph from the given edges.
        Graph g = new Graph(edges, noOfNodes);

        // Starting node
        int start = 0;

        // Add starting node to the path.
        List<Integer> path = new ArrayList<>();
        path.add(start);

        // Mark the start node as visited.
        boolean[] visited = new boolean[noOfNodes];
        visited[start] = true;

        // Hamiltonian Algorithm
        Pathfinder p = new Pathfinder();
        System.out.println("All Hamiltonian paths and their total distance: ");
        p.printAllHamiltonianPaths(g, start, visited, path, noOfNodes, distances);
        // Finding the shortest Hamiltonian path.
        List<Integer> sequence = p.findShortestPath(distances);
        System.out.println("\nShortest path: " + sequence);

        // Getting the source node and destination node to pass into A* algorithm
        HashMap<Integer, Cell> nodeNumbers = testMap.getNodeNumbers();
        System.out.println("Sequence of nodes to stop at: ");
        for (Integer k : sequence) {
            System.out.println(nodeNumbers.get(k));
        }

        // Instantiate Robot
        Robot robot = new Robot(1, 1, 'N');
        
        robot.centerX = 15;               //My pathing function requires the center point of the robot which I set here as the starting point
        robot.centerY = 15;
        robot.setDirection('N');
        
		

        // A* Algorithm
        AStar ass = new AStar(testMap.getGrid(), robot);
        for (int i = 0; i < sequence.size() - 1; i++) {
            ass.menu(nodeNumbers.get(sequence.get(i)).getX(), nodeNumbers.get(sequence.get(i)).getY(),
                    nodeNumbers.get(sequence.get(i + 1)).getX(), nodeNumbers.get(sequence.get(i + 1)).getY());
        }
        
        List<List<Cell>> aPath = ass.getListOfPaths();        
		
        showPath(aPath, pane);      
               
        UIRobot uiRobot = new UIRobot(pane, height, scaling, 'N', 15, 15);      //Creates a robot version to be placed into the GUI
		uiRobot.executeCommands(aPath, robot, testMap, obstacleList);      //Plays the animated path
		
        primaryStage.setTitle("Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        scene.setFill(createGridPattern());
    }
    
    public ImagePattern createGridPattern() {

        double w = 40;
        double h = 40;

        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.valueOf("#bee1ed"));
        gc.fillRect(0, 0, w, h);
        gc.strokeRect(0.5, 0.5, w, h);
        
        Image image = canvas.snapshot(new SnapshotParameters(), null);
        ImagePattern pattern = new ImagePattern(image, 0, 0, w, h, false);

        return pattern;

    }
    
    public void showPath(List<List<Cell>> aPath, Pane pane)
    {
    	for(List<Cell> path : aPath)
    	{
    		for(int i = 0; i < path.size(); i++)
        	{
            	Rectangle pathRect = new Rectangle();
            	pathRect.setWidth(40);
            	pathRect.setHeight(40);
            	pathRect.setX(path.get(i).getX() * 40);
            	pathRect.setY((20 - path.get(i).getY() - 1) * 40);
            	if(i == path.size() - 1 || i == 0)
            	{
            		pathRect.setFill(Color.BLUE);
            	}
            	else
            	{
            		pathRect.setFill(Color.ORANGE);
            	}
            	
            	pane.getChildren().add(pathRect);
        	}
    	}	
    }
    
    public void placeObstacles(List<Obstacle> obstacleList, Pane pane)  
    {  	  	
    	for(Obstacle obstacle : obstacleList)
    	{
    		int x = obstacle.getX() * 10;
    		int y = obstacle.getY() * 10;
    		Rectangle obstacleRectangle = new Rectangle();
    		obstacleRectangle.setX(x * scaling);
    		obstacleRectangle.setY((height - 10 - y) * scaling);
    		obstacleRectangle.setWidth(10 * scaling);
    		obstacleRectangle.setHeight(10 * scaling);
    		pane.getChildren().add(obstacleRectangle);
    		
    		char direction = obstacle.getDirection();
    		
    		Line line = new Line();
    		
    		switch(direction)
    		{
	    		case 'N':
	    			line.setStartX((x + 1) * scaling);
	    			line.setEndX((x + 9) * scaling);
	    			line.setStartY((height - 10 - y) * scaling);
	    			line.setEndY((height - 10 - y) * scaling);
	    			line.setStroke(Color.RED);
	    			line.setStrokeWidth(scaling + 1);
	    			break;
	    		case 'S':
	    			line.setStartX((x + 1) * scaling);
	    			line.setEndX((x + 9) * scaling);
	    			line.setStartY((height - y) * scaling);
	    			line.setEndY((height - y) * scaling);
	    			line.setStroke(Color.RED);
	    			line.setStrokeWidth(scaling + 1);
	    			break;
	    		case 'E':
	    			line.setStartX((x + 10) * scaling);
	    			line.setEndX((x + 10) * scaling);
	    			line.setStartY((height - 1 - y) * scaling);
	    			line.setEndY((height - 9 - y) * scaling);
	    			line.setStroke(Color.RED);
	    			line.setStrokeWidth(scaling + 1);
	    			break;
	    		case 'W':
	    			line.setStartX((x) * scaling);
	    			line.setEndX((x) * scaling);
	    			line.setStartY((height - 1 - y) * scaling);
	    			line.setEndY((height - 9 - y) * scaling);
	    			line.setStroke(Color.RED);
	    			line.setStrokeWidth(scaling + 1);
	    			break;
    		}
    		
    		pane.getChildren().add(line);
    	}
    }
    
    private static List<Edge> generateEdges(HashMap<Integer, Cell> nodeNumbers, double[][] distances) {
        int n = nodeNumbers.size();
        Edge[] edges = new Edge[n * (n-1)/2];
        int count = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                edges[count++] = new Edge(i, j, distances[i][j]);
            }
        }
        return Arrays.asList(edges);
    }
}