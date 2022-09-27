package com.example.mdp_android.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;

import com.example.mdp_android.MainActivity;
import com.example.mdp_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class GridMap extends View {

    private static Userinput userinput;
    private static String Direction;
    private static int rid;
    private static int Directioncount;
    public static String message = "";




    public GridMap(Context c) {
        super(c);
        initMap();
    }


    SharedPreferences sharedPreferences;

    private Paint blackPaint = new Paint();
    private Paint obstacleColor = new Paint();
    private Paint robotColor = new Paint();
    private Paint endColor = new Paint();
    private Paint startColor = new Paint();
    private Paint waypointColor = new Paint();
    private Paint unexploredColor = new Paint();
    private Paint exploredColor = new Paint();
    private Paint arrowColor = new Paint();
    private Paint fastestPathColor = new Paint();

    private static JSONObject receivedJsonObject = new JSONObject();
    private static JSONObject mapInformation;
    private static JSONObject backupMapInformation;
    private static String robotDirection = "None";
    private static int[] startCoord = new int[]{-1, -1};
    private static int[] curCoord = new int[]{-1, -1};
    private static int[] oldCoord = new int[]{-1, -1};
    private static int[] waypointCoord = new int[]{-1, -1};
    private static ArrayList<String[]> arrowCoord = new ArrayList<>();
    private static ArrayList<int[]> obstacleCoord = new ArrayList<>();
    private static boolean autoUpdate = false;
    private static boolean canDrawRobot = false;
    private static boolean setWaypointStatus = false;
    private static boolean startCoordStatus = false;
    private static boolean setObstacleStatus = false;
    private static boolean unSetCellStatus = false;
    private static boolean setExploredStatus = false;
    private static boolean validPosition = false;
    private static boolean printObstacleAdded= false;

    private Bitmap arrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_error);
    private static final String TAG = "GridMap";
    private static final int COL = 20;
    private static final int ROW = 20;
    private static float cellSize;
    private static Cell[][] cells;
    private boolean isStartcoord = false;
    private static final int maxObstacles = 8;
    private int obstacleCount = 0;
    private boolean moveObstaclesStatus = false;
    private boolean rotationstatus  = false;
    private int GX = 0;
    private int GY = 0;




    //will be updated from controlfragment's seekbar value' count sbDistanceCount. when algo uses, can directly update via a updateSbDistance method.
    //sbDistance is used to calculate and map the canvas accordigly to the 10cm grid squares. See moveRobot() method
    private int sbDistance = 0;
    private int rotationid = 0;

    // rotationValue will be updated from controlfragment' radio button checked value rotationCount, placed here in case for further need to use this value.
    // rotationValue and leftRotateElseRight will be used to update android's robot direction on the grid accordingly to the rotation.
    private boolean leftRotateElseRight = false;
    private int rotationValue = 0;

    private boolean mapDrawn = false;
    public static String publicMDFExploration;
    public static String publicMDFObstacle;
    private static Bitmap obstacleDirectionBitmap;
    public static String obstacleDirection = "";
    public static ArrayList<String[]> obstacleDirectionCoord = new ArrayList<>();
    public static boolean obstacleDirectionCoordinateStatus = false;
    public GridMap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initMap();
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        obstacleColor.setColor(Color.BLUE);
        robotColor.setColor(Color.GREEN);
        endColor.setColor(Color.RED);
        startColor.setColor(Color.CYAN);
        waypointColor.setColor(Color.YELLOW);
        unexploredColor.setColor(Color.LTGRAY);
        exploredColor.setColor(Color.WHITE);
        arrowColor.setColor(Color.BLACK);
        fastestPathColor.setColor(Color.MAGENTA);

        // get shared preferences
        sharedPreferences = getContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
    }

    private void initMap() {
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        showLog("Entering onDraw");
        super.onDraw(canvas);
        showLog("Redrawing map");

        //CREATE CELL COORDINATES
        Log.d(TAG,"Creating Cell");

        if (!mapDrawn) {
            String[] dummyArrowCoord = new String[3];
            dummyArrowCoord[0] = "1";
            dummyArrowCoord[1] = "1";
            dummyArrowCoord[2] = "dummy";
            arrowCoord.add(dummyArrowCoord);
            this.createCell();
            this.setEndCoord(19, 19);
            mapDrawn = true;
        }

        drawIndividualCell(canvas);
        drawHorizontalLines(canvas);
        drawVerticalLines(canvas);
        drawGridNumber(canvas);
        if (getCanDrawRobot())
            drawRobot(canvas, curCoord);
        drawArrow(canvas, arrowCoord);
        drawObsAndDirections(canvas,obstacleDirectionCoord);

        showLog("Exiting onDraw");
    }

    private void drawIndividualCell(Canvas canvas) {
        showLog("Entering drawIndividualCell");
        for (int x = 1; x <= COL; x++)
            for (int y = 0; y < ROW; y++)
                for (int i = 0; i < this.getArrowCoord().size(); i++)
                    if (!cells[x][y].type.equals("image") && cells[x][y].getId() == -1) {
                        canvas.drawRect(cells[x][y].startX, cells[x][y].startY, cells[x][y].endX, cells[x][y].endY, cells[x][y].paint);
                    } else {
                        Paint textPaint = new Paint();
                        textPaint.setTextSize(20);
                        textPaint.setColor(Color.WHITE);
                        textPaint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawRect(cells[x][y].startX, cells[x][y].startY, cells[x][y].endX, cells[x][y].endY, cells[x][y].paint);
                        canvas.drawText(String.valueOf(cells[x][y].getId()),(cells[x][y].startX+cells[x][y].endX)/2, cells[x][y].endY + (cells[x][y].startY-cells[x][y].endY)/4, textPaint);
                    }

        showLog("Exiting drawIndividualCell");
    }

    public void drawImageNumberCell(int x, int y, int id) {
        cells[x+1][19-y].setType("image");
        cells[x+1][19-y].setId(id);
        this.invalidate();
    }

    private void drawHorizontalLines(Canvas canvas) {
        for (int y = 0; y <= ROW; y++)
            canvas.drawLine(cells[1][y].startX, cells[1][y].startY - (cellSize / 30), cells[20][y].endX, cells[20][y].startY - (cellSize / 30), blackPaint);
    }

    private void drawVerticalLines(Canvas canvas) {
        for (int x = 0; x <= COL; x++)
            canvas.drawLine(cells[x][0].startX - (cellSize / 30) + cellSize, cells[x][0].startY - (cellSize / 30), cells[x][0].startX - (cellSize / 30) + cellSize, cells[x][19].endY + (cellSize / 30), blackPaint);
    }

    private void drawGridNumber(Canvas canvas) {
        showLog("Entering drawGridNumber");
        for (int x = 1; x <= COL; x++) {
            if (x > 9)
                canvas.drawText(Integer.toString(x-1), cells[x][20].startX + (cellSize / 5), cells[x][20].startY + (cellSize / 3), blackPaint);
            else
                canvas.drawText(Integer.toString(x-1), cells[x][20].startX + (cellSize / 3), cells[x][20].startY + (cellSize / 3), blackPaint);
        }
        for (int y = 0; y < ROW; y++) {
            if ((20 - y) > 9)
                canvas.drawText(Integer.toString(19 - y), cells[0][y].startX + (cellSize / 2), cells[0][y].startY + (cellSize / 1.5f), blackPaint);
            else
                canvas.drawText(Integer.toString(19 - y), cells[0][y].startX + (cellSize / 1.5f), cells[0][y].startY + (cellSize / 1.5f), blackPaint);
        }
        showLog("Exiting drawGridNumber");
    }

    private void drawRobot(Canvas canvas, int[] curCoord) {
        showLog("Entering drawRobot");
        int androidRowCoord = this.convertRow(curCoord[1]);
        for (int y = androidRowCoord; y <= androidRowCoord + 1; y++)
            canvas.drawLine(cells[curCoord[0] - 1][y].startX, cells[curCoord[0] - 1][y].startY - (cellSize / 30), cells[curCoord[0] + 1][y].endX, cells[curCoord[0] + 1][y].startY - (cellSize / 30), robotColor);
        for (int x = curCoord[0] - 1; x < curCoord[0] + 1; x++)
            canvas.drawLine(cells[x][androidRowCoord - 1].startX - (cellSize / 30) + cellSize, cells[x][androidRowCoord - 1].startY, cells[x][androidRowCoord + 1].startX - (cellSize / 30) + cellSize, cells[x][androidRowCoord + 1].endY, robotColor);

        switch (this.getRobotDirection()) {
            case "N":
                canvas.drawLine(cells[curCoord[0] - 1][androidRowCoord + 1].startX, cells[curCoord[0] - 1][androidRowCoord + 1].endY, (cells[curCoord[0]][androidRowCoord - 1].startX + cells[curCoord[0]][androidRowCoord - 1].endX) / 2, cells[curCoord[0]][androidRowCoord - 1].startY, blackPaint);
                canvas.drawLine((cells[curCoord[0]][androidRowCoord - 1].startX + cells[curCoord[0]][androidRowCoord - 1].endX) / 2, cells[curCoord[0]][androidRowCoord - 1].startY, cells[curCoord[0] + 1][androidRowCoord + 1].endX, cells[curCoord[0] + 1][androidRowCoord + 1].endY, blackPaint);
                break;
            case "S":
                canvas.drawLine(cells[curCoord[0] - 1][androidRowCoord - 1].startX, cells[curCoord[0] - 1][androidRowCoord - 1].startY, (cells[curCoord[0]][androidRowCoord + 1].startX + cells[curCoord[0]][androidRowCoord + 1].endX) / 2, cells[curCoord[0]][androidRowCoord + 1].endY, blackPaint);
                canvas.drawLine((cells[curCoord[0]][androidRowCoord + 1].startX + cells[curCoord[0]][androidRowCoord + 1].endX) / 2, cells[curCoord[0]][androidRowCoord + 1].endY, cells[curCoord[0] + 1][androidRowCoord - 1].endX, cells[curCoord[0] + 1][androidRowCoord - 1].startY, blackPaint);
                break;
            case "E":
                canvas.drawLine(cells[curCoord[0] - 1][androidRowCoord - 1].startX, cells[curCoord[0] - 1][androidRowCoord - 1].startY, cells[curCoord[0] + 1][androidRowCoord].endX, cells[curCoord[0] + 1][androidRowCoord - 1].endY + (cells[curCoord[0] + 1][androidRowCoord].endY - cells[curCoord[0] + 1][androidRowCoord - 1].endY) / 2, blackPaint);
                canvas.drawLine(cells[curCoord[0] + 1][androidRowCoord].endX, cells[curCoord[0] + 1][androidRowCoord - 1].endY + (cells[curCoord[0] + 1][androidRowCoord].endY - cells[curCoord[0] + 1][androidRowCoord - 1].endY) / 2, cells[curCoord[0] - 1][androidRowCoord + 1].startX, cells[curCoord[0] - 1][androidRowCoord + 1].endY, blackPaint);
                break;
            case "W":
                canvas.drawLine(cells[curCoord[0] + 1][androidRowCoord - 1].endX, cells[curCoord[0] + 1][androidRowCoord - 1].startY, cells[curCoord[0] - 1][androidRowCoord].startX, cells[curCoord[0] - 1][androidRowCoord - 1].endY + (cells[curCoord[0] - 1][androidRowCoord].endY - cells[curCoord[0] - 1][androidRowCoord - 1].endY) / 2, blackPaint);
                canvas.drawLine(cells[curCoord[0] - 1][androidRowCoord].startX, cells[curCoord[0] - 1][androidRowCoord - 1].endY + (cells[curCoord[0] - 1][androidRowCoord].endY - cells[curCoord[0] - 1][androidRowCoord - 1].endY) / 2, cells[curCoord[0] + 1][androidRowCoord + 1].endX, cells[curCoord[0] + 1][androidRowCoord + 1].endY, blackPaint);
                break;
            default:
                Toast.makeText(this.getContext(), "Error with drawing robot (unknown direction)", Toast.LENGTH_LONG).show();
                break;
        }
        showLog("Exiting drawRobot");
    }

    private ArrayList<String[]> getArrowCoord() {
        return arrowCoord;
    }

    public String getRobotDirection() {
        return robotDirection;
    }

    public void setAutoUpdate(boolean autoUpdate) throws JSONException {
        showLog(String.valueOf(backupMapInformation));
        if (!autoUpdate)
            backupMapInformation = this.getReceivedJsonObject();
        else {
            setReceivedJsonObject(backupMapInformation);
            backupMapInformation = null;
        }
        GridMap.autoUpdate = autoUpdate;
    }

    public JSONObject getReceivedJsonObject() {
        return receivedJsonObject;
    }

    public void setReceivedJsonObject(JSONObject receivedJsonObject) {
        showLog("Entered setReceivedJsonObject");
        GridMap.receivedJsonObject = receivedJsonObject;
        backupMapInformation = receivedJsonObject;
    }

    public boolean getAutoUpdate() {
        return autoUpdate;
    }

    public boolean getMapDrawn() {
        return mapDrawn;
    }

    private void setValidPosition(boolean status) {
        validPosition = status;
    }

    public boolean getValidPosition() {
        return validPosition;
    }

    public void setUnSetCellStatus(boolean status) {
        unSetCellStatus = status;
    }

    public boolean getUnSetCellStatus() {
        return unSetCellStatus;
    }

    public void setSetObstacleStatus(boolean status) {
        setObstacleStatus = status;
    }

    public boolean getSetObstacleStatus() {
        return setObstacleStatus;
    }

    public void setExploredStatus(boolean status) {
        setExploredStatus = status;
    }

    public boolean getExploredStatus() {
        return setExploredStatus;
    }

    public void setStartCoordStatus(boolean status) {
        startCoordStatus = status;
    }

    private boolean getStartCoordStatus() {
        return startCoordStatus;
    }

    public void setWaypointStatus(boolean status) {
        setWaypointStatus = status;
    }

    public boolean getCanDrawRobot() {
        return canDrawRobot;
    }

    private void createCell() {
        showLog("Entering cellCreate");
        cells = new Cell[COL + 1][ROW + 1];
        this.calculateDimension();
        cellSize = this.getCellSize();

        for (int x = 0; x <= COL; x++)
            for (int y = 0; y <= ROW; y++)
                cells[x][y] = new Cell(x * cellSize + (cellSize / 30), y * cellSize + (cellSize / 30), (x + 1) * cellSize, (y + 1) * cellSize, unexploredColor, "unexplored");
        showLog("Exiting createCell");
    }

    public void setEndCoord(int col, int row) {
        showLog("Entering setEndCoord");
        row = this.convertRow(row);
        for (int x = col - 1; x <= col + 1; x++)
            for (int y = row - 1; y <= row + 1; y++)
                cells[x][y].setType("end");
        showLog("Exiting setEndCoord");
    }

    public void setStartCoord(int col, int row) {
        showLog("Entering setStartCoord");
        startCoord[0] = col;
        startCoord[1] = row;
        String direction = getRobotDirection();
        if(direction.equals("None")) {
            direction = "N";
        }
        if (this.getStartCoordStatus())
            this.setOldRobotCoord(col,row);
            this.setCurCoord(col, row, direction);
        showLog("Exiting setStartCoord");
    }

    private int[] getStartCoord() {
        return startCoord;
    }

    public void setCurCoord(int col, int row, String direction) {
        showLog("Entering setCurCoord");
        curCoord[0] = col;
        curCoord[1] = row;
        this.setRobotDirection(direction);
        this.updateRobotAxis(col, row, direction);

        row = this.convertRow(row);
        for (int x = col - 1; x <= col + 1; x++)
            for (int y = row - 1; y <= row + 1; y++)
                cells[x][y].setType("robot");
        showLog("Exiting setCurCoord");
    }

    public int[] getCurCoord() {
        return curCoord;
    }

    private void calculateDimension() {
        this.setCellSize(getWidth()/(COL+1));
    }

    private int convertRow(int row) {
        return (20 - row);
    }

    private void setCellSize(float cellSize) {
        GridMap.cellSize = cellSize;
    }

    private float getCellSize() {
        return cellSize;
    }

    //controls where robot sets the grid to be explored
    public void setOldRobotCoord(int oldCol, int oldRow) {
        showLog("Entering setOldRobotCoord");
        oldCoord[0] = oldCol;
        oldCoord[1] = oldRow;
        oldRow = this.convertRow(oldRow);
        //how to cater to > 1 grid movement?, we take the vertical range between newCoordinates and old.

        for (int x = oldCol - 1; x <= oldCol + 1; x++)
            for (int y = oldRow - 1; y <= oldRow + 1; y++)
                cells[x][y].setType("explored");
        showLog("Exiting setOldRobotCoord");
    }
    private int[] getOldRobotCoord() {
        return oldCoord;
    }

    private void setArrowCoordinate(int col, int row, String arrowDirection) {
        showLog("Entering setArrowCoordinate");
        int[] obstacleCoord = new int[]{col, row};
        this.getObstacleCoord().add(obstacleCoord);
        String[] arrowCoord = new String[3];
        arrowCoord[0] = String.valueOf(col);
        arrowCoord[1] = String.valueOf(row);
        arrowCoord[2] = arrowDirection;
        this.getArrowCoord().add(arrowCoord);

        row = convertRow(row);
        cells[col][row].setType("arrow");
        showLog("Exiting setArrowCoordinate");
    }

    public void setRobotDirection(String direction) {
        sharedPreferences = getContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        robotDirection = direction;
        editor.putString("direction", direction);
        editor.commit();
        this.invalidate();;
    }

    private void updateRobotAxis(int col, int row, String direction) {
        TextView xAxisTextView =  ((Activity)this.getContext()).findViewById(R.id.xAxisTextView);
        TextView yAxisTextView =  ((Activity)this.getContext()).findViewById(R.id.yAxisTextView);
        TextView directionAxisTextView =  ((Activity)this.getContext()).findViewById(R.id.directionAxisTextView);

        xAxisTextView.setText(String.valueOf(col-1));
        yAxisTextView.setText(String.valueOf(row-1));
        directionAxisTextView.setText(direction);
    }

    private void setWaypointCoord(int col, int row) throws JSONException {
        showLog("Entering setWaypointCoord");
        waypointCoord[0] = col;
        waypointCoord[1] = row;

        row = this.convertRow(row);
        cells[col][row].setType("waypoint");

        MainActivity.printMessage("waypoint", waypointCoord[0]-1, waypointCoord[1]-1);
        showLog("Exiting setWaypointCoord");
    }

    private int[] getWaypointCoord() {
        return waypointCoord;
    }

    private void setObstacleCoord(int col, int row) { //c5 - c7 set obstacle with face. Require adding of face indicator
        showLog("Entering setObstacleCoord");
        int[] obstacleCoord = new int[]{col, row};
        GridMap.obstacleCoord.add(obstacleCoord);
        row = this.convertRow(row);
        cells[col][row].setType("obstacle");
        showLog("Exiting setObstacleCoord");
    }

    private ArrayList<int[]> getObstacleCoord() {
        return obstacleCoord;
    }

    private void showLog(String message) {
        Log.d(TAG, message);
    }

    private void drawArrow(Canvas canvas, ArrayList<String[]> arrowCoord) {
        showLog("Entering drawArrow");
        RectF rect;

        for (int i = 0; i < arrowCoord.size(); i++) {
            if (!arrowCoord.get(i)[2].equals("dummy")) {
                int col = Integer.parseInt(arrowCoord.get(i)[0]);
                int row = convertRow(Integer.parseInt(arrowCoord.get(i)[1]));
                rect = new RectF(col * cellSize, row * cellSize, (col + 1) * cellSize, (row + 1) * cellSize);
                switch (arrowCoord.get(i)[2]) {
                    case "N":
                        arrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_up);
                        break;
                    case "E":
                        arrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_right);
                        break;
                    case "S":
                        arrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_down);
                        break;
                    case "W":
                        arrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_left);
                        break;
                    default:
                        break;
                }
                canvas.drawBitmap(arrowBitmap, null, rect, null);
            }
            showLog("Exiting drawArrow");
        }
    }


    private class Cell {
        float startX, startY, endX, endY;
        Paint paint;
        String type;
        int id = -1;

        private Cell(float startX, float startY, float endX, float endY, Paint paint, String type) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.paint = paint;
            this.type = type;
        }

        public void setType(String type) {
            this.type = type;
            switch (type) {
                case "obstacle":
                    this.paint = obstacleColor;
                    break;
                case "robot":
                    this.paint = robotColor;
                    break;
                case "end":
                    this.paint = endColor;
                    break;
                case "start":
                    this.paint = startColor;
                    break;
                case "waypoint":
                    this.paint = waypointColor;
                    break;
                case "unexplored":
                    this.paint = unexploredColor;
                    break;
                case "explored":
                    this.paint = exploredColor;
                    break;
                case "arrow":
                    this.paint = arrowColor;
                    break;
                case "fastestPath":
                    this.paint = fastestPathColor;
                    break;
                case "image":
                    this.paint = obstacleColor;
                default:
                    showLog("setTtype default: " + type);
                    break;
            }
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        showLog("Entering onTouchEvent");
        boolean obsMoved = false;
        int obstacleInArray = -1;
        if (event.getAction() == MotionEvent.ACTION_DOWN && this.getAutoUpdate() == false) {
            int column = (int) (event.getX() / cellSize);
            int row = this.convertRow((int) (event.getY() / cellSize));
            ToggleButton setStartPointToggleBtn = ((Activity)this.getContext()).findViewById(R.id.setStartPointToggleBtn);
            ToggleButton setWaypointToggleBtn = ((Activity)this. getContext()).findViewById(R.id.setWaypointToggleBtn);

            if (startCoordStatus) {
                if (canDrawRobot) {
                    int[] startCoord = this.getStartCoord();
                    if (startCoord[0] >= 2 && startCoord[1] >= 2) {
                        startCoord[1] = this.convertRow(startCoord[1]);
                        for (int x = startCoord[0] - 1; x <= startCoord[0] + 1; x++)
                            for (int y = startCoord[1] - 1; y <= startCoord[1] + 1; y++)
                                cells[x][y].setType("unexplored");
                    }
                }
                else
                    canDrawRobot = true;
                this.setStartCoord(column, row);
                startCoordStatus = false;
                String direction = getRobotDirection();
                if(direction.equals("None")) {
                    direction = "N";
                }
                try {
                    int directionInt = 0;
                    if(direction.equals("N")){
                        directionInt = 0;
                    } else if(direction.equals("W")) {
                        directionInt = 3;
                    } else if(direction.equals("E")) {
                        directionInt = 1;
                    } else if(direction.equals("S")) {
                        directionInt = 2;
                    }
                    MainActivity.printMessage("starting " + "(" + String.valueOf(column-1) + "," + String.valueOf(row-1) + "," + String.valueOf(directionInt) + ")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateRobotAxis(column, row, direction);
                if (setStartPointToggleBtn.isChecked())
                    setStartPointToggleBtn.toggle();
                this.invalidate();
                return true;
            }
            if (obstacleDirectionCoordinateStatus) {
                obstacleInArray = isValidObstacleCoord(column,row);
                if(obstacleCount < maxObstacles){
                    //have a obstacle count ++ max to 5

                    /*set obstacle to move upon tap down and is obstacle
                    then upon action up set the obstacle*/
                    //if current x,y is obstacle then set ismove flag to true

                    if( obstacleInArray == -1) //-1 means valid obs coord
                    {
                        //if is valid then we add
                        //have a obstacle direction
                        this.printObstacleAdded=true;
                        //sets obstacle upon tap.-----Start-----
                        this.setObstacleCoord(column, row);
                        obstacleCount++;
                        setObstacleDirectionCoordinate(column,row,obstacleDirection);

                        Log.d(TAG, "Added obstacle: " + obstacleCount);
                        Toast.makeText(this.getContext(), "Added obstacle: "+ obstacleCount, Toast.LENGTH_SHORT).show();
                        this.invalidate();

                        return true;
                        //sets obstacle upon tap.-------End-----
                    }
                }
                else{//max obstacles
                    this.printObstacleAdded=false;
                    Log.d(TAG, "Unable to add obstacle, Max Obstacles on field");
                    Toast.makeText(this.getContext(), "Unable to add obstacle, Max Obstacles on field", Toast.LENGTH_SHORT).show();
                }
                this.obstacleDirectionCoordinateStatus = false;
                this.printObstacleAdded=false;
                this.invalidate();
                return true;
            }
            if (setWaypointStatus) {
                int[] waypointCoord = this.getWaypointCoord();
                if (waypointCoord[0] >= 1 && waypointCoord[1] >= 1)
                    cells[waypointCoord[0]][this.convertRow(waypointCoord[1])].setType("unexplored");
                setWaypointStatus = false;
                try {
                    this.setWaypointCoord(column, row);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (setWaypointToggleBtn.isChecked())
                    setWaypointToggleBtn.toggle();
                this.invalidate();
                return true;
            }
            if (setObstacleStatus) {//set obstacle on btton action down
                this.obstacleDirectionCoordinateStatus = false;
                this.invalidate();
                return true;
            }
            if (setExploredStatus) {
                cells[column][20-row].setType("explored");
                this.invalidate();
                return true;
            }
            if (unSetCellStatus) {
                ArrayList<int[]> obstacleCoord = this.getObstacleCoord();
                cells[column][20-row].setType("unexplored");
                for (int i=0; i<obstacleCoord.size(); i++)
                    if (obstacleCoord.get(i)[0] == column && obstacleCoord.get(i)[1] == row)
                    {
                        MainActivity.printMessage("obstacle "+ obstacleCount + " removed " + "(" +
                                (Integer.parseInt(obstacleDirectionCoord.get(obstacleCount-1)[0]) - 1) +
                                "," + (Integer.parseInt(obstacleDirectionCoord.get(obstacleCount-1)[1]) - 1) +
                                "," + Integer.parseInt(obstacleDirectionCoord.get(obstacleCount-1)[2]) + ")");
                        obstacleCoord.remove(i);
                    obstacleDirectionCoord.remove(i);
                    obstacleCount--;
                        Log.d(TAG, "Success remove obstacle, new obstacle count"+obstacleCount);
                        Toast.makeText(this.getContext(), "Removed obstacle, new obs count:"+obstacleCount, Toast.LENGTH_SHORT).show();
                    }
                this.invalidate();
                return true;
            }
        }

        showLog("Exiting onTouchEvent");
        return false;
    }
    public int isValidObstacleCoord(int col, int row)
    {
        if(obstacleCount == 0) {
            return -1;
        }
        else{
            for(int i=0;i<obstacleCoord.size();i++)
            {
                if (obstacleCoord.get(i)[0] == col && obstacleCoord.get(i)[1] == row)
                    return i;//returns the obstacle position in the array
            }
            return -1;//means obstacle is valid coord but we don't check if maxObstacle
            //max obstacle will be handled in the ontouch.
        }

    }

    public void toggleCheckedBtn(String buttonName) {
        ToggleButton setStartPointToggleBtn = ((Activity)this.getContext()).findViewById(R.id.setStartPointToggleBtn);
        ToggleButton setWaypointToggleBtn = ((Activity)this.getContext()).findViewById(R.id.setWaypointToggleBtn);
        ImageButton obstacleImageBtn = ((Activity)this.getContext()).findViewById(R.id.obstacleImageBtn);
        ImageButton exploredImageBtn = ((Activity)this.getContext()).findViewById(R.id.exploredImageBtn);
        ImageButton clearImageBtn = ((Activity)this.getContext()).findViewById(R.id.clearImageBtn);

        if (!buttonName.equals("setStartPointToggleBtn"))
            if (setStartPointToggleBtn.isChecked()) {
                this.setStartCoordStatus(false);
                setStartPointToggleBtn.toggle();
            }
        if (!buttonName.equals("setWaypointToggleBtn"))
            if (setWaypointToggleBtn.isChecked()) {
                this.setWaypointStatus(false);
                setWaypointToggleBtn.toggle();
            }
        if (!buttonName.equals("exploredImageBtn"))
            if (exploredImageBtn.isEnabled())
                this.setExploredStatus(false);
        if (!buttonName.equals("obstacleImageBtn"))
            if (obstacleImageBtn.isEnabled())
                this.setSetObstacleStatus(false);
        if (!buttonName.equals("clearImageBtn"))
            if (clearImageBtn.isEnabled())
                this.setUnSetCellStatus(false);
    }


    public void resetMap() {
        showLog("Entering resetMap");
        TextView robotStatusTextView =  ((Activity)this.getContext()).findViewById(R.id.robotStatusTextView);
        Switch manualAutoToggleBtn = ((Activity)this.getContext()).findViewById(R.id.manualAutoToggleBtn);
        obstacleCount =0;
        //Switch phoneTiltSwitch = ((Activity)this.getContext()).findViewById(R.id.phoneTiltSwitch);
        updateRobotAxis(1, 1, "None");
        robotStatusTextView.setText("Not Available");
        SharedPreferences.Editor editor = sharedPreferences.edit();


        if (manualAutoToggleBtn.isChecked()) {
            manualAutoToggleBtn.toggle();
            manualAutoToggleBtn.setText("MANUAL");
        }
        this.toggleCheckedBtn("None");

        /*if (phoneTiltSwitch.isChecked()) {
            phoneTiltSwitch.toggle();
            phoneTiltSwitch.setText("TILT OFF");
        }*/

        receivedJsonObject = null;
        backupMapInformation = null;
        startCoord = new int[]{-1, -1};
        curCoord = new int[]{-1, -1};
        oldCoord = new int[]{-1, -1};
        robotDirection = "None";
        autoUpdate = false;
        arrowCoord = new ArrayList<>();
        obstacleCoord = new ArrayList<>();
        waypointCoord = new int[]{-1, -1};
        mapDrawn = false;
        canDrawRobot = false;
        validPosition = false;
        obstacleDirectionCoord = new ArrayList<>();
        Bitmap arrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_error);

        showLog("Exiting resetMap");
        this.invalidate();
    }

    public void moveRobot(String direction) {
        showLog("Entering moveRobot");
        setValidPosition(false);
        int[] curCoord = this.getCurCoord();
        ArrayList<int[]> obstacleCoord = this.getObstacleCoord();
        this.setOldRobotCoord(curCoord[0], curCoord[1]);
        int[] oldCoord = this.getOldRobotCoord();
        String robotDirection = getRobotDirection();
        String backupDirection = robotDirection;
        int tempCoordY = curCoord[1];//holds current coordinates for Robot's Y
        int tempCoordX = curCoord[0];//holds current coordinates for Robot's X

        if(rotationstatus){
            switch(rotationid)
            {
                case 0: break;
                case 1: GX=30; GY=30;break;
                case 2: GX=60; GY=60;break;
                case 3: GX=90; GY=90;break;
                case 4: GX=120; GY=120;break;
                default:
                    GX+=30; GY+=30;break;
            }
        }

        switch (robotDirection) {
            case "N":
                switch (direction) {
                    case "forward":
                        if (curCoord[1] != 19) {
                            //need new movement validator method to check if sbdistance will exceed grid.
                            if (rotationstatus) {
                                tempCoordY = tempCoordY + GY / 10;
                            } else {
                                tempCoordY = tempCoordY + sbDistance;
                            }
                            if (tempCoordY <= 19) {
                                validPosition = true;

                            }
                        }
                        break;
                    case "right":
                        robotDirection = "E";
                        break;
                    case "back":
                        if (curCoord[1] != 2) {

                            if(rotationstatus)
                            {
                                tempCoordY = tempCoordY - GY/10;
                            }
                            else
                            tempCoordY = tempCoordY - sbDistance;
                                if(tempCoordY >=2) {

                                    validPosition = true;
                                }
                            }


                        break;
                    case "left":
                        robotDirection = "W";
                        break;
                    default:
                        robotDirection = "error N";
                        break;
                }
                break;
            case "E":
                switch (direction) {
                    case "forward":
                        if (curCoord[0] != 19) { //Working

                            if(rotationstatus)
                            {
                                tempCoordX = tempCoordX + GX/10;
                            }// }
                            else
                            tempCoordX = tempCoordX + sbDistance;
                            if(tempCoordX <= 19)
                            {//

                            validPosition = true;}
                        }
                        break;
                    case "right":
                        robotDirection = "S";
                        break;
                        case "back":
                        if (curCoord[0] != 2) {//Working
                            if(rotationstatus){

                                tempCoordX = tempCoordX - GX/10;
                            }
                            else
                            tempCoordX = tempCoordX - sbDistance;
                            if(tempCoordX >=2) {
                            //curCoord[0] -= 1;

                            validPosition = true;}
                        }
                        break;
                    case "left":
                        robotDirection = "N";
                        break;
                    default:
                        robotDirection = "error E";
                }
                break;
            case "S":
                switch (direction) {
                    case "forward":
                        if (curCoord[1] != 2) {

                            if(rotationstatus) {
                                tempCoordY = tempCoordY - GY / 10;
                            }
                            else
                            tempCoordY = tempCoordY - sbDistance;
                            if(tempCoordY >=2) {

                                    validPosition = true;

                            }
                        }
                        break;
                    case "right":
                        robotDirection = "W";
                        break;
                    case "back":
                        if (curCoord[1] != 19) {
                            if(rotationstatus){

                                tempCoordY = tempCoordY + GY/10;
                            }
                            else
                            tempCoordY = tempCoordY + sbDistance;
                            if(tempCoordY <= 19) {
                                //curCoord[1] += 1;


                                validPosition = true;
                            }
                        }
                        break;
                    case "left":
                        robotDirection = "E";
                        break;
                    default:
                        robotDirection = "error S";
                }
                break;
            case "W":
                switch (direction) {
                    case "forward":
                        if (curCoord[0] != 2) {
                            if(rotationstatus) {
                                tempCoordX = tempCoordX - GX / 10;

                            }
                            else
                                tempCoordX = tempCoordX - sbDistance;
                            if(tempCoordX >=2) {


                                validPosition = true;}


                        }
                        break;
                    case "right":
                        robotDirection = "N";
                        break;
                    case "back":
                        if (curCoord[0] != 19) {
                            if(rotationstatus)
                            {tempCoordX = tempCoordX +GX/10;}
                            else
                            tempCoordX = tempCoordX + sbDistance;
                            if(tempCoordX <= 19)
                            {//curCoord[0] += 1
                                                            // ;}
                                validPosition = true;}
                        }
                        break;
                    case "left":
                        robotDirection = "S";
                        break;
                    default:
                        robotDirection = "error W";
                }
                break;
            default:
                robotDirection = "error moveCurCoord";
                break;
        }
        if (getValidPosition()) {//this method here is to check if there are obstacles blocking the path if not, it will move robot.
            int startX;//this works for forward and backword of direction N. error for backward of N because startpoint highest is 18 not 19
            int startY; //need to fix the setting of Y coordinates to proper 19 not 18, while the row ' X coordinates are proper @ 2 meaning index 1 of array. e.g. [2,18] shld be [2,19]
            int endX;
            int endY;
            if(tempCoordX-oldCoord[0]==0) {//no changes only Y axis movement
                startX = tempCoordX;
                endX = tempCoordX;
            }
            if(tempCoordX-oldCoord[0]>0){//+ve changes in X axis
                //then we do start x = old, end x = tempCoordX
                startX=oldCoord[0];
                endX=tempCoordX;
            }
            else//-ve changes in X axis, so since we using ++ for loops, we change start x to the loweer number, aka tempCoordX.
            {
                startX=tempCoordX;
                endX=oldCoord[0];
            }

            if(tempCoordY-oldCoord[1]==0) {//no changes only, x axis movement
                startY = tempCoordY;
                endY = tempCoordY;
            }
            if(tempCoordY-oldCoord[1]>0){//+ve changes in y axis
                //then we do start y = old, end y = tempCoordy
                startY=oldCoord[1];
                endY=tempCoordY;
            }
            else//-ve changes in Y Axis.
            {
                startY=tempCoordY;
                endY=oldCoord[1];
            }


            for (int x = startX - 1; x <= endX + 1; x++) {//working for Direction N forward and backward to detect obstacle
                for (int y = startY - 1; y <= endY + 1; y++) {
                    for (int i = 0; i < obstacleCoord.size(); i++) {
                        if (obstacleCoord.get(i)[0] != x || obstacleCoord.get(i)[1] != y)
                            setValidPosition(true);
                        else {
                            setValidPosition(false);
                            MainActivity.printMessage("Collision at Obstacle " +i+ "  detected, will  not proceed with movement");
                            break;
                        }
                    }
                    if (!getValidPosition())
                        break;
                }
                if (!getValidPosition())
                    break;
            }
        }
        if (getValidPosition()){ //+ve y
            int movementY = sbDistance;
            if(rotationstatus)
                movementY =GY/10;

            int movementX = sbDistance;
            if(rotationstatus)
                movementX =GX/10;
            if (tempCoordY - oldCoord[1] > 0) {
            for(int i = 0;i<movementY;i++)//currently this method works to set explored cells, but it will override obstacle cells.
                {//thus the at the end of moveRobot there is a valid thingy that check obstacle, maybe I can shift it forward to check b4 setting exploration.
                    curCoord[1] += 1;//movement of grid by 10cm
                    this.setOldRobotCoord(curCoord[0],curCoord[1]);
                }
            }
            else if(tempCoordY - oldCoord[1] < 0)
            {
                for(int i = 0;i<movementY;i++)
                {
                    curCoord[1] -= 1;//movement of grid by 10cm
                    this.setOldRobotCoord(curCoord[0],curCoord[1]);
                }
            }
            if (tempCoordX - oldCoord[0] > 0) {//+ve x
            for(int i = 0;i< movementX;i++)//currently this method works to set explored cells, but it will override obstacle cells.
                {//thus the at the end of moveRobot there is a valid thingy that check obstacle, maybe I can shift it forward to check b4 setting exploration.
                    curCoord[0] += 1;//movement of grid by 10cm
                    this.setOldRobotCoord(curCoord[0],curCoord[1]);
                }

            }
            else if (tempCoordX - oldCoord[0]<0)
            {
                for(int i = 0;i<movementX;i++)
                {
                    curCoord[0] -= 1;//movement of grid by 10cm
                    this.setOldRobotCoord(curCoord[0],curCoord[1]);
                }
            }
            rotationstatus=false;

            this.setCurCoord(curCoord[0], curCoord[1], robotDirection);
        }
        else {
            if (direction.equals("forward") || direction.equals("back"))
                robotDirection = backupDirection;
            this.setCurCoord(oldCoord[0], oldCoord[1], robotDirection);
        }
        this.invalidate();
        showLog("Exiting moveRobot");
    }

    public void printRobotStatus(String message) {
        TextView robotStatusTextView = ((Activity)this.getContext()).findViewById(R.id.robotStatusTextView);
        robotStatusTextView.setText(message);
    }

    public static void setPublicMDFExploration(String msg) {
        publicMDFExploration = msg;
    }

    public static void setPublicMDFObstacle(String msg) {
        publicMDFObstacle = msg;
    }

    public static String getPublicMDFExploration() {
        return publicMDFExploration;
    }

    public static String getPublicMDFObstacle() {
        return publicMDFObstacle;
    }

    //--will come from control fragment.
    public void updateSbDistance(int distance)//during seekbar change
    {this.sbDistance = distance;}
    public void updaterotationid(int rid)//during seekbar change
    {this.rotationid = rid ;}
    public void updateRotation(int rotation)//during radio button change
    {this.rotationValue = rotation; }
    public void updateRotationDirection(boolean leftElseRight)
    {this.leftRotateElseRight = leftElseRight;}
    //--
    public ArrayList<String[]> getObstacleDirectionCoord(){
        return obstacleDirectionCoord;
    }

    public void updateObstacleDirectionCoord(ArrayList<String[]>updatedObsDirectionCoord){
        this.obstacleDirectionCoord = updatedObsDirectionCoord;
    }
    private void setObstacleDirectionCoordinate(int col, int row,String obstacleDirection) {
        showLog("Entering setDirectionCoord");
        /*int[] obstacleCoord = new int[]{col, row};
        this.getObstacleCoord().add(obstacleCoord);*/
        String[] directionCoord = new String[4];
        directionCoord[0] = String.valueOf(col);
        directionCoord[1] = String.valueOf(row);
        directionCoord[2] = obstacleDirection;
        directionCoord[3] = "0";//0 means not yet found for obstacle
        this.getObstacleDirectionCoord().add(directionCoord);

        row = this.convertRow(row);
        cells[col][row].setType("obstacleDirection");
        showLog("Exiting setDirectionCoord");
    }
    public void setRotationstatus(boolean setRotationStatus)
    {
        this.rotationstatus = setRotationStatus;
    }
    private void drawObsAndDirections(Canvas canvas, ArrayList<String[]> obstacleDirectionCoord){
        showLog("Entering drawObstacleWithDirection");
        RectF rect;
        int arrayIndex=0;

        for(int i =0; i<obstacleDirectionCoord.size(); i++){
            arrayIndex=i;
            int col= Integer.parseInt(obstacleDirectionCoord.get(i)[0]);
            int row= convertRow(Integer.parseInt(obstacleDirectionCoord.get(i)[1]));
            rect = new RectF(col * cellSize, row * cellSize, (col+1) * cellSize, (row+1) * cellSize);

            switch(obstacleDirectionCoord.get(i)[2]){
                case "0":
                    obstacleDirectionBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.north_obstacle);
                    break;
                case "1":
                    obstacleDirectionBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.east_obstacle);

                    break;
                case "2":
                    obstacleDirectionBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.south_obstacle);

                    break;
                case "3":
                    obstacleDirectionBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.west_obstacle);
                    break;
                default:
                    break;

            }

            canvas.drawBitmap(obstacleDirectionBitmap,null,rect,null);
            showLog("Exiting drawObstacleWithDirection");
        }
        if(!obstacleDirectionCoord.isEmpty() && printObstacleAdded) {
            String foundbit = "Obs ID not yet Found";
            if(!(obstacleDirectionCoord.get(arrayIndex)[3].equals("0")))
            {
                foundbit = "Found";
            }

            message = message + "(" + (Integer.parseInt(obstacleDirectionCoord.get(arrayIndex)[0].trim()) - 1) + "," + (Integer.parseInt(obstacleDirectionCoord.get(arrayIndex)[1].trim()) - 1) + "," + Integer.parseInt(obstacleDirectionCoord.get(arrayIndex)[2].trim()) +")";
            Toast.makeText(getContext().getApplicationContext(),message,Toast.LENGTH_LONG);
            //MainActivity.printMessage("obstacle" + "(" + (Integer.parseInt(obstacleDirectionCoord.get(arrayIndex)[0]) - 1) + "," + (Integer.parseInt(obstacleDirectionCoord.get(arrayIndex)[1]) - 1) + "," + Integer.parseInt(obstacleDirectionCoord.get(arrayIndex)[2]) + " "+foundbit+")");
            this.printObstacleAdded=false;
        }
    }
}
