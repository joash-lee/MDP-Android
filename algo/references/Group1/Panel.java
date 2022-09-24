import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel {
  static int originX = 0;
  static int originY = 0;
  static int i = 0;
  int vert = 780;
  int hori = 0;
  static boolean path_done = false;
  static List<Spot> obstacles = new ArrayList<Spot>();
  static List<Spot> boundary = new ArrayList<Spot>();
  static List<Integer> x_path = new ArrayList<Integer>();
  static List<Integer> y_path = new ArrayList<Integer>();
  static List<Spot> endNodes = new ArrayList<Spot>();
  static boolean update = false;

  //	private Grid grid;
  public Panel(Grid grid, List<Spot> endNodes, List<Spot> obstacles, List<Spot> boundary) {
    Panel.endNodes = endNodes;
    Panel.obstacles = obstacles;
    Panel.boundary = boundary;
  }

  public Panel(Grid grid, List<Integer> x_path, List<Integer> y_path, List<Spot> endNodes, List<Spot> obstacles,
      List<Spot> boundary, boolean update) {
    //		this.grid = grid;
    Panel.x_path = x_path;
    Panel.y_path = y_path;
    Panel.endNodes = endNodes;
    Panel.obstacles = obstacles;
    Panel.boundary = boundary;
    Panel.update = update;
  }

  private static final long serialVersionUID = 1L;

  //	public Panel(Spot spot) {
  //		this. = board;
  //	}

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    drawGrid(g2);
    drawBoundary(g2);
    drawObstacles(g2);
    drawTarget(g2);
    if (update) {

      //drawCar(g2);
      System.out.println(y_path.get(i) + "before");
      drawCar(g2);

      g2.fillRoundRect(x_path.get(i), y_path.get(i), Grid.cellSide * 2, Grid.cellSide * 2, 20, 20);
      g2.setColor(Color.black);

      if (y_path.get(i) == (vert) - 20 && x_path.get(i) == hori) {
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 10, x_path.get(i) + 20, y_path.get(i) + 30);
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 10, x_path.get(i) + 10, y_path.get(i) + 20);
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 10, x_path.get(i) + 30, y_path.get(i) + 20);
      } //Up

      if (y_path.get(i) == (vert) + 20 && x_path.get(i) == hori) {
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 10, x_path.get(i) + 20, y_path.get(i) + 30);
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 30, x_path.get(i) + 10, y_path.get(i) + 20);
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 30, x_path.get(i) + 30, y_path.get(i) + 20);
      } //Down

      if (y_path.get(i) == (vert) && x_path.get(i) == hori + 20) {
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 20, x_path.get(i) + 30, y_path.get(i) + 20);
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 10, x_path.get(i) + 30, y_path.get(i) + 20);
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 30, x_path.get(i) + 30, y_path.get(i) + 20);
      } //Right

      if (y_path.get(i) == (vert) && x_path.get(i) == hori - 20) {
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 20, x_path.get(i) + 30, y_path.get(i) + 20);
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 20, x_path.get(i) + 20, y_path.get(i) + 10);
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 20, x_path.get(i) + 20, y_path.get(i) + 30);
      } //Left

      if (y_path.get(i) == (vert) - 20 && x_path.get(i) == hori + 20) {
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 30, x_path.get(i) + 30, y_path.get(i) + 10);
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 10, x_path.get(i) + 30, y_path.get(i) + 10);
        g2.drawLine(x_path.get(i) + 30, y_path.get(i) + 10, x_path.get(i) + 30, y_path.get(i) + 20);
      } //Up Right

      if (y_path.get(i) == (vert) + 20 && x_path.get(i) == hori + 20) {
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 10, x_path.get(i) + 30, y_path.get(i) + 30);
        g2.drawLine(x_path.get(i) + 20, y_path.get(i) + 30, x_path.get(i) + 30, y_path.get(i) + 30);
        g2.drawLine(x_path.get(i) + 30, y_path.get(i) + 20, x_path.get(i) + 30, y_path.get(i) + 30);
      } // Down Right

      if (y_path.get(i) == (vert) - 20 && x_path.get(i) == hori - 20) {
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 10, x_path.get(i) + 30, y_path.get(i) + 30);
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 10, x_path.get(i) + 10, y_path.get(i) + 20);
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 10, x_path.get(i) + 20, y_path.get(i) + 10);
      } // Up Left

      if (y_path.get(i) == (vert) + 20 && x_path.get(i) == hori - 20) {
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 30, x_path.get(i) + 30, y_path.get(i) + 10);
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 20, x_path.get(i) + 10, y_path.get(i) + 30);
        g2.drawLine(x_path.get(i) + 10, y_path.get(i) + 30, x_path.get(i) + 20, y_path.get(i) + 30);
      } // Down Left

      hori = x_path.get(i);
      if (path_done) {
        drawPath(g2);
        //path_done=false;
      }
      //	        drawPath(g2);

    }

  }

  //	private void drawCar(Graphics2D g2) {
  //		g2.setColor(Color.black);
  //		//for (CarCell cell : board.car) {
  //		int x = originX + Grid.car.col * Grid.cellSide;
  //		int y = originY + Grid.car.row * Grid.cellSide;
  //		g2.fillRoundRect(x, y, Grid.cellSide, Grid.cellSide, 20, 20 );
  //		//}
  //		
  //	}

  private void drawCar(Graphics2D g2) {
    g2.setColor(Color.cyan);

    Timer timer = new Timer(200, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //System.out.println(x_path.size());
        //               System.out.println(x_path.get(i));
        //               System.out.println(y_path.get(i));
        //System.out.println(i);

        if (i >= x_path.size() - 1) {
          //System.out.println(i >= x_path.size()-1);
          i = x_path.size() - 1;
          System.out.println("test");

          Timer timer = (Timer) e.getSource();
          timer.stop();
          path_done = true;
        }
        System.out.println(path_done);
        if (i != x_path.size() - 1) {
          //System.out.println(i != x_path.size()-1);
          vert = y_path.get(i);
          hori = x_path.get(i);
          i += 1;

        }
        //                repaint();
        //                repaint();
        //                repaint();
        //                repaint();
        repaint();
      }
    });
    if (i == 0) {
      timer.setInitialDelay(2000);
      timer.start();
    }

    //		for(int i=0 ; i<x_path.size(); i++){
    //			g2.fillRoundRect(x_path.get(i), y_path.get(i), Grid.cellSide, Grid.cellSide, 20, 20 );
  }
  //g2.fillRoundRect(x, y, Grid.cellSide, Grid.cellSide, 20, 20 );
  //}

  private void drawPath(Graphics2D g2) {
    g2.setColor(Color.black);
    for (int k = 0; k < x_path.size(); k++) {
      //        	System.out.println("test");
      //g2.fillRoundRect(x_path.get(k), y_path.get(k), Grid.cellSide*2, Grid.cellSide*2, 20, 20 );
      //g2.fillOval(x_path.get(k), y_path.get(k), Grid.cellSide*2, Grid.cellSide*2 );
      if (k + 1 < x_path.size()) {
        g2.drawLine(x_path.get(k) + Grid.cellSide, y_path.get(k) + Grid.cellSide, x_path.get(k + 1) + Grid.cellSide,
            y_path.get(k + 1) + Grid.cellSide);
      }

    }
    //repaint();
  }

  private void drawGrid(Graphics2D g2) {
    g2.setColor(Color.lightGray);
    for (int i = 0; i < Grid.rows + 1; i += 2) {
      g2.drawLine(originX, originY + i * Grid.cellSide, originX + Grid.cols * Grid.cellSide,
          originY + i * Grid.cellSide);
      //			System.out.println(originY + i*cellSide);
    }

    for (int j = 0; j < Grid.cols + 1; j += 2) {
      g2.drawLine(originX + j * Grid.cellSide, originY, originX + j * Grid.cellSide,
          originY + Grid.rows * Grid.cellSide);
      //System.out.println(originX + j*Grid.cellSide);
    }
  }

  //	endNodes.add(grid[10][0]);
  //	endNodes.add(grid[10][10]);
  //	endNodes.add(grid[19][10]);
  //	endNodes.add(grid[19][19]);
  //	endNodes.add(grid[0][19]);
  private void drawTarget(Graphics2D g2) {
    g2.setColor(Color.green);
    for (int k = 0; k < endNodes.size(); k++) {
      g2.fillRoundRect(endNodes.get(k).i, endNodes.get(k).j, Grid.cellSide * 2, Grid.cellSide * 2, 20, 20);
    }
  }

  private void drawObstacles(Graphics2D g2) {
    g2.setColor(Color.black);
    for (int k = 0; k < obstacles.size(); k++) {
      g2.fillRoundRect(obstacles.get(k).i, obstacles.get(k).j, Grid.cellSide * 2, Grid.cellSide * 2, 0, 0);
    }
  }

  private void drawBoundary(Graphics2D g2) {
    g2.setColor(Color.lightGray);
    for (int k = 0; k < boundary.size(); k++) {
      g2.fillRoundRect(boundary.get(k).i, boundary.get(k).j, Grid.cellSide, Grid.cellSide, 0, 0);
    }
  }

}