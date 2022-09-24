import java.util.ArrayList;
import java.util.List;

public class Spot {
  int i;
  int j;
  double f;
  double h;
  double g;
  List<Spot> neighbours;
  Spot previous;
  boolean wall;

  Spot(int i, int j) {
    this.i = i;
    this.j = j;
    this.f = 0;
    this.h = 0;
    this.g = 0;
    this.neighbours = new ArrayList<Spot>();
    this.wall = false;

  }

  //	Spot getPrevious() {	
  //		return previous;		
  //	}
  //	void setPrevious(Spot prev) {	
  //		this.previous = prev;		
  //	}
}