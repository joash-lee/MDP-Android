package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Block {
	
	public static int BLOCKWIDTH = 10, BLOCKHEIGHT = 10;
	public double x0, y0;
	public double x1, x2, x3, y1, y2, y3;
	public Direction direction;
	
	public Block(double x, double y, Direction direction) {
		this.x0 = x;
		this.y0 = y;
		this.x1 = x0 + BLOCKWIDTH;
		this.y1 = y0;
		this.x2 = x0;
		this.y2 = y0 + BLOCKHEIGHT;
		this.x3 = x1;
		this.y3 = y2;
		this.direction = direction;
	}
	
	public void draw(Graphics2D g2d) {
//		Rectangle box = new Rectangle(x2 * Panel.SCALE, (Panel.WIN_HEIGHT - y2) * Panel.SCALE , BLOCKWIDTH * Panel.SCALE, BLOCKHEIGHT * Panel.SCALE);
		Rectangle2D box = new Rectangle2D.Double(x2 * Panel.SCALE, (Panel.WIN_HEIGHT - y2) * Panel.SCALE , BLOCKWIDTH * Panel.SCALE, BLOCKHEIGHT * Panel.SCALE);
		g2d.setColor(Color.black);
		g2d.fill(box);
		
		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(5));
        
        if(this.direction == Direction.North)
		{
        	g2d.drawLine((int)x2 * Panel.SCALE , (Panel.WIN_HEIGHT - (int)y2) * Panel.SCALE, (int)x3 * Panel.SCALE, (Panel.WIN_HEIGHT - (int)y3) * Panel.SCALE);
		}
		
		if(this.direction == Direction.South)
		{
			g2d.drawLine((int)x0 * Panel.SCALE , (Panel.WIN_HEIGHT - (int)y0) * Panel.SCALE, (int)x1 * Panel.SCALE, (Panel.WIN_HEIGHT - (int)y1) * Panel.SCALE);
		}
		
		if(this.direction == Direction.East)
		{
			g2d.drawLine((int)x3 * Panel.SCALE, (Panel.WIN_HEIGHT - (int)y3) * Panel.SCALE, (int)x1 * Panel.SCALE, (Panel.WIN_HEIGHT - (int)y1) * Panel.SCALE);
		}
		
		if(this.direction == Direction.West)
		{
			g2d.drawLine((int)x2 * Panel.SCALE, (Panel.WIN_HEIGHT - (int)y2) * Panel.SCALE, (int)x0 * Panel.SCALE, (Panel.WIN_HEIGHT - (int)y0) * Panel.SCALE);
		}
	}
}
