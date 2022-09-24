package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Robot {
	public static int BLOCKWIDTH = 20, BLOCKHEIGHT = 20;
	public double x0, y0, x1, x2, x3, y1, y2, y3;
	public double _x0, _y0, _x1, _x2, _x3, _y1, _y2, _y3;
	public Direction direction;
	public double velocityX = 0, velocityY = 0;
	public List<Double> directionVec;
	AffineTransform transform = new AffineTransform();
	
	public Robot(double startPosX, double startPosY, Direction direction) {
		
		x0 = startPosX;
		y0 = startPosY;
		x1 = x0 + BLOCKWIDTH;
		y1 = y0;
		x2 = x0;
		y2 = y0 + BLOCKHEIGHT;
		x3 = x1;
		y3 = y2;
		
		this.direction = direction;
		
		_x0 = x0;
		_y0 = y0;
		_x1 = x1;
		_y1 = y1;
		_y2 = y2;
		_y2 = y2;
		_y3 = y3;
		_y3 = y3;
	}
	
	public void draw(Graphics2D g2d) {
		
		double _x = 0, _y = 0;
		
		switch(direction) {
		case North:
			_x = x2 * Panel.SCALE;
			_y = (Panel.WIN_HEIGHT - y2) * Panel.SCALE;
			break;
		case South:
			_x = x1 * Panel.SCALE;
			_y = (Panel.WIN_HEIGHT - y1) * Panel.SCALE;
			break;
		case East:
			_x = x0 * Panel.SCALE;
			_y = (Panel.WIN_HEIGHT - y0) * Panel.SCALE;
			break;
		case West:
			_x = x3 * Panel.SCALE;
			_y = (Panel.WIN_HEIGHT - y3) * Panel.SCALE;
			break;
		}
		
		Rectangle2D box = new Rectangle2D.Double( _x, _y , BLOCKWIDTH * Panel.SCALE, BLOCKHEIGHT * Panel.SCALE);
		g2d.setColor(Color.orange);
		g2d.transform(transform);
		g2d.fill(box);
	}
	
	public List<Double> calculateDirectionVec(double destX, double destY) {
		List<Double> directionVec = new ArrayList<Double>();
		directionVec.add((destX - x0) / Math.sqrt(Math.pow((destX - x0), 2) + Math.pow((destY - y0), 2)));
		directionVec.add((destY - y0) / Math.sqrt(Math.pow((destX - x0), 2) + Math.pow((destY - y0), 2)));
		return directionVec;
	}
	
	public double calculateDistance(double destX, double destY) {
		return Math.sqrt(Math.pow((destX - x0), 2) + Math.pow((destY - y0), 2));
	}
	
	public void update() {
		x0 = x0 + velocityX;
		y0 = y0 + velocityY;
		x1 = x0 + BLOCKWIDTH;
		y1 = y0;
		x2 = x0;
		y2 = y0 + BLOCKHEIGHT;
		x3 = x1;
		y3 = y2;
	}
	
	public boolean moveStraight(double destX, double destY) {
		
//		System.out.println(x0);
//		System.out.println(y0);
		
		if(velocityX == 0 && velocityY == 0)
		{
			velocityX = (destX - x0) / calculateDistance(destX, destY);
			velocityY = (destY - y0) / calculateDistance(destX, destY);
			
//			System.out.println(velocityX);
//			System.out.println(velocityY);
			
			
		}
		
		if(calculateDistance(destX, destY) <= 1) {
			
//			System.out.println((destX - x0) / calculateDistance(destX, destY));
//			System.out.println((destY - y0) / calculateDistance(destX, destY));
			
			x0 = destX;
			y0 = destY;
			
			velocityX = 0;
			velocityY = 0;
			
			x1 = x0 + BLOCKWIDTH;
			y1 = y0;
			x2 = x0;
			y2 = y0 + BLOCKHEIGHT;
			x3 = x1;
			y3 = y2;
			
			_x0 = x0;
			_y0 = y0;
			_x1 = x1;
			_y1 = y1;
			_y2 = y2;
			_y2 = y2;
			_y3 = y3;
			_y3 = y3;
			
//			System.out.println(x0);
//			System.out.println(y0);
			
			return true;
		}
		
		else {
			
			x0 = x0 + velocityX;
			y0 = y0 + velocityY;
			x1 = x1 + velocityX;
			y1 = y1 + velocityY;
			x2 = x2 + velocityX;
			y2 = y2 + velocityY;
			x3 = x3 + velocityX;
			y3 = y3 + velocityY;
			
			return false;
		}		
	}
	
	public int turnLeft(boolean left, int step) {		
		
		if(left == true) {
			switch(direction) {
			case North:
				transform.rotate(Math.toRadians(-9), (x2 - 15) * Panel.SCALE, (Panel.WIN_HEIGHT - (y2 - (BLOCKHEIGHT/2))) * Panel.SCALE);
				break;
			case South:
				transform.rotate(Math.toRadians(-9), (x2 + 15) * Panel.SCALE, (Panel.WIN_HEIGHT - (y2 + (BLOCKHEIGHT/2))) * Panel.SCALE);
				break;
			case East:
				transform.rotate(Math.toRadians(-9), (x2 - (BLOCKHEIGHT/2)) * Panel.SCALE, (Panel.WIN_HEIGHT - (y2 + 15)) * Panel.SCALE);
				break;
			case West:
				transform.rotate(Math.toRadians(-9), (x2 + (BLOCKHEIGHT/2)) * Panel.SCALE, (Panel.WIN_HEIGHT - (y2 - 15)) * Panel.SCALE);
				break;
			}
			
		} else {
			switch(direction) {
			case North:
				transform.rotate(Math.toRadians(9), (x3 + 15) * Panel.SCALE, (Panel.WIN_HEIGHT - (y3 - (BLOCKHEIGHT/2))) * Panel.SCALE);
				break;
			case South:
				transform.rotate(Math.toRadians(9), (x3 - 15) * Panel.SCALE, (Panel.WIN_HEIGHT - (y3 + (BLOCKHEIGHT/2))) * Panel.SCALE);
				break;
			case East:
				transform.rotate(Math.toRadians(9), (x3 - (BLOCKHEIGHT/2)) * Panel.SCALE, (Panel.WIN_HEIGHT - (y3 - 15)) * Panel.SCALE);
				break;
			case West:
				transform.rotate(Math.toRadians(9), (x3 + (BLOCKHEIGHT/2)) * Panel.SCALE, (Panel.WIN_HEIGHT - (y3 + 15)) * Panel.SCALE);
				break;
			}
		}
		
		step++;
		
		if(step > 10)
		{
			
//			System.out.println(x0);
//			System.out.println(y0);
			
			if(left == true) {
				
				switch(direction) {
				case North:
					direction = Direction.West;
					x0 = x0 - 5;
					y0 = y0 + 25;
					x1 = x1 - 25;
					y1 = y1 + 45;
					x2 = x2 - 25;
					y2 = y2 + 5;
					x3 = x3 - 45;
					y3 = y3 + 25;
					break;
				case South:
					direction = Direction.East;
					x0 = x0 + 5;
					y0 = y0 - 25;
					x1 = x1 + 25;
					y1 = y1 - 45;
					x2 = x2 + 25;
					y2 = y2 - 5;
					x3 = x3 + 45;
					y3 = y3 - 25;
					break;
				case East:
					direction = Direction.North;
					x0 = x0 + 25;
					y0 = y0 + 5;
					x1 = x1 + 45;
					y1 = y1 + 25;
					x2 = x2 + 5;
					y2 = y2 + 25;
					x3 = x3 + 25;
					y3 = y3 + 45;
					break;
				case West:
					direction = Direction.South;
					x0 = x0 - 25;
					y0 = y0 - 5;
					x1 = x1 - 45;
					y1 = y1 - 25;
					x2 = x2 - 5;
					y2 = y2 - 25;
					x3 = x3 - 25;
					y3 = y3 - 45;
					break;
				}
				
			} else {
				switch(direction) {
				case North:
					direction = Direction.East;
					x0 = x0 + 25;
					y0 = y0 + 45;
					x1 = x1 + 5;
					y1 = y1 + 25;
					x2 = x2 + 45;
					y2 = y2 + 25;
					x3 = x3 + 25;
					y3 = y3 + 5;
					break;
				case South:
					direction = Direction.West;
					x0 = x0 - 25;
					y0 = y0 - 45;
					x1 = x1 - 5;
					y1 = y1 - 15;
					x2 = x2 - 45;
					y2 = y2 - 25;
					x3 = x3 - 25;
					y3 = y3 - 5;
					break;
				case East:
					direction = Direction.South;
					x0 = x0 + 45;
					y0 = y0 - 25;
					x1 = x1 + 25;
					y1 = y1 - 5;
					x2 = x2 + 25;
					y2 = y2 - 45;
					x3 = x3 + 5;
					y3 = y3 - 25;
					break;
				case West:
					direction = Direction.North;
					x0 = x0 - 45;
					y0 = y0 + 25;
					x1 = x1 - 25;
					y1 = y1 + 5;
					x2 = x2 - 25;
					y2 = y2 + 45;
					x3 = x3 - 5;
					y3 = y3 + 25;
					break;
				}
			}
			
			transform.setToIdentity();
		}
		
		return step;
		
	}			
	
}
