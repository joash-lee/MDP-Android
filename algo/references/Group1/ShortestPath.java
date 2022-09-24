import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class ShortestPath {
  public static ArrayList<ArrayList<int[]>> finalOrder = new ArrayList<ArrayList<int[]>>();

  public static ArrayList<int[]> GetUserInput() {
    Scanner sc = new Scanner(System.in); //System.in is a standard input stream  
    ArrayList<int[]> userNode = new ArrayList<int[]>();

    System.out.print("Enter number of obstacles: ");
    int a = sc.nextInt();
    for (int i = 0; i < a; i++) {
      System.out.println("Obstacle number " + (i + 1) + ":");
      System.out.print("Enter X: ");
      int b = sc.nextInt();
      System.out.print("Enter Y: ");
      int c = sc.nextInt();
      System.out.print("Enter Direction (1: bottom, 2: right, 3: left, 4: top): ");
      int d = sc.nextInt();
      int[] temp = { b, c, d };
      userNode.add(temp);
    }
    sc.close();
    return userNode;
  }

  public static void main() {
    ArrayList<int[]> myNode = new ArrayList<int[]>();
    int[] start = { 0, 39 };
    myNode = GetUserInput();
    GetPath(start, myNode);
  }

  public static ArrayList<int[]> GetPath(int[] start, ArrayList<int[]> myNode) {
    double shortestDist = -1;
    ArrayList<int[]> shortestPath = new ArrayList<int[]>();
    permuteRecursive(myNode.size(), myNode);

    for (int i = 0; i < finalOrder.size(); i++) {
      finalOrder.get(i).add(0, start);
      double dist = getPathDistance(finalOrder.get(i));

      /* DEBUGGING
       * System.out.println(finalOrder.size());
       * ArrayList<int[]> tempArr = finalOrder.get(i);
       * System.out.print(Arrays.toString(tempArr.get(0)) + " ");
       * System.out.print(Arrays.toString(tempArr.get(1)) + " ");
       * System.out.print(Arrays.toString(tempArr.get(2)) + " ");
       * System.out.print(Arrays.toString(tempArr.get(3)) + " ");
       * System.out.print(Arrays.toString(tempArr.get(4)) + " ");
       * System.out.println(dist);
       */

      if (shortestDist == -1) {
        shortestDist = dist;
        shortestPath = finalOrder.get(i);
      } else {
        if (shortestDist > dist) {
          shortestDist = dist;
          shortestPath = finalOrder.get(i);
        }
      }
    }
    System.out.print("Path: ");
    for (int i = 0; i < shortestPath.size(); i++) {
      System.out.print(Arrays.toString(shortestPath.get(i)) + " ");
    }
    System.out.println("Distance: " + shortestDist);
    return shortestPath;
  };

  public static void permuteRecursive(int n, ArrayList<int[]> arr) {
    if (n == 1) {
      finalOrder.add(new ArrayList<int[]>(arr));
      /*
       * System.out.print(Arrays.toString(arr.get(0)) + " ");
       * System.out.print(Arrays.toString(arr.get(1)) + " ");
       * System.out.print(Arrays.toString(arr.get(2)) + " ");
       * System.out.print(Arrays.toString(arr.get(3)) + " ");
       * System.out.print(Arrays.toString(arr.get(4)) + " ");
       * System.out.println(getPathDistance(tempArr));
       */
    } else {
      for (int i = 0; i < n - 1; i++) {
        permuteRecursive(n - 1, arr);
        if (n % 2 == 0) {
          swap(arr, i, n - 1);
        } else {
          swap(arr, 0, n - 1);
        }
      }
      permuteRecursive(n - 1, arr);
    }
  }

  public static void addToList(ArrayList<int[]> arr, ArrayList<ArrayList<int[]>> arrAll) {
    arrAll.add(arr);
  }

  private static void swap(ArrayList<int[]> arr, int i, int j) {
    int[] t = arr.get(i);
    arr.set(i, arr.get(j));
    arr.set(j, t);
  }

  public static double getDist(int[] a, int[] b) { //CALCULATES THE EUCLIDIAN
    int a_x = a[0];
    int a_y = a[1];
    int b_x = b[0];
    int b_y = b[1];
    int x_dist = Math.abs(a_x - b_x);
    int y_dist = Math.abs(a_y - b_y);
    return Math.sqrt((x_dist * x_dist) + (y_dist * y_dist));
  }

  public static double getPathDistance(ArrayList<int[]> myPath) {
    double total = 0;
    for (int i = 0; i < myPath.size() - 1; i++) {
      total = total + getDist(myPath.get(i), myPath.get(i + 1));
    }
    ;
    return total;
  };
}