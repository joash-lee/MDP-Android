package com.example.mdp_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.mdp_android.ui.main.BluetoothConnectionService;
import com.example.mdp_android.ui.main.BluetoothPopUp;
import com.example.mdp_android.ui.main.CommsFragment;
import com.example.mdp_android.ui.main.GridMap;
import com.example.mdp_android.ui.main.MapTabFragment;
import com.example.mdp_android.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // Declaration Variables
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Context context;

    private static GridMap gridMap;
    static TextView xAxisTextView, yAxisTextView, directionAxisTextView;
    static TextView robotStatusTextView;

    BluetoothConnectionService mBluetoothConnection;
    BluetoothDevice mBTDevice;
    private static UUID myUUID;
    ProgressDialog myDialog;

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(9999);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("incomingMessage"));

        // Set up sharedPreferences
        MainActivity.context = getApplicationContext();
        this.sharedPreferences();
        editor.putString("message", "");
        editor.putString("direction","None");
        editor.putString("connStatus", "Disconnected");
        editor.commit();

        Button printMDFStringButton = (Button) findViewById(R.id.printMDFString);
        printMDFStringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Explored : " + GridMap.getPublicMDFExploration();
                editor = sharedPreferences.edit();
                editor.putString("message", CommsFragment.getMessageReceivedTextView().getText() + "\n" + message);
                editor.commit();
                refreshMessageReceived();
                message = "Obstacle : " + GridMap.getPublicMDFObstacle() + "0";
                editor.putString("message", CommsFragment.getMessageReceivedTextView().getText() + "\n" + message);
                editor.commit();
                refreshMessageReceived();
            }
        });

        // Toolbar
        Button bluetoothButton = (Button) findViewById(R.id.bluetoothButton);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popup = new Intent(MainActivity.this, BluetoothPopUp.class);
                startActivity(popup);
            }
        });

        // Map
        gridMap = new GridMap(this);
        gridMap = findViewById(R.id.mapView);
        xAxisTextView = findViewById(R.id.xAxisTextView);
        yAxisTextView = findViewById(R.id.yAxisTextView);
        directionAxisTextView = findViewById(R.id.directionAxisTextView);
        robotStatusTextView = findViewById(R.id.robotStatusTextView);

        // Robot Status
        myDialog = new ProgressDialog(MainActivity.this);
        myDialog.setMessage("Waiting for other device to reconnect...");
        myDialog.setCancelable(false);
        myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }


    public static GridMap getGridMap() {
        return gridMap;
    }

    public static TextView getRobotStatusTextView() {  return robotStatusTextView; }

    private static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public static void sharedPreferences() {
        sharedPreferences = MainActivity.getSharedPreferences(MainActivity.context);
        editor = sharedPreferences.edit();
    }

    // Send message to bluetooth
    public static void printMessage(String message) {
        showLog("Entering printMessage");
        editor = sharedPreferences.edit();

        if (BluetoothConnectionService.BluetoothConnectionStatus == true) {
            byte[] bytes = message.getBytes(Charset.defaultCharset());
            BluetoothConnectionService.write(bytes);
        }
        showLog(CommsFragment.getMessageReceivedTextView().getText() + "\n" + message);
        showLog(message);
        editor.putString("message", CommsFragment.getMessageReceivedTextView().getText() + "\n" + message);
        editor.commit();
        refreshMessageReceived();
        showLog("Exiting printMessage");
    }

    public static void printMessage(String name, int x, int y) throws JSONException {
        showLog("Entering printMessage");
        sharedPreferences();

        JSONObject jsonObject = new JSONObject();
        String message;

        switch(name) {
//            case "starting":
            case "waypoint":
                jsonObject.put(name, name);
                jsonObject.put("x", x);
                jsonObject.put("y", y);
                message = name + " (" + x + "," + y + ")";
                break;
            default:
                message = "Unexpected default for printMessage: " + name;
                break;
        }
        editor.putString("message", CommsFragment.getMessageReceivedTextView().getText() + "\n" + message);
        editor.commit();
        showLog(CommsFragment.getMessageReceivedTextView().getText() + "\n" + message);
        if (BluetoothConnectionService.BluetoothConnectionStatus == true) {
            byte[] bytes = message.getBytes(Charset.defaultCharset());
            BluetoothConnectionService.write(bytes);
        }
        showLog("Exiting printMessage");
    }

    public static void refreshMessageReceived() {
        CommsFragment.getMessageReceivedTextView().setText(sharedPreferences.getString("message", ""));
    }


    public void refreshDirection(String direction) {
        gridMap.setRobotDirection(direction);
        directionAxisTextView.setText(sharedPreferences.getString("direction",""));
        printMessage("Direction is set to " + direction);
    }

    public static void refreshLabel() {
        xAxisTextView.setText(String.valueOf(gridMap.getCurCoord()[0]-1));
        yAxisTextView.setText(String.valueOf(gridMap.getCurCoord()[1]-1));
        directionAxisTextView.setText(sharedPreferences.getString("direction",""));
    }

    public static void receiveMessage(String message) {
        showLog("Entering receiveMessage");
        sharedPreferences();
        editor.putString("message", sharedPreferences.getString("message", "") + "\n" + message);
        editor.commit();
        showLog("Exiting receiveMessage");
    }
 /*   public static void changeTextView(String message) {
        robotStatusTextView.setText(message);
    }*/

    private static void showLog(String message) {
        Log.d(TAG, message);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
    }

    private BroadcastReceiver mBroadcastReceiver5 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice mDevice = intent.getParcelableExtra("Device");
            String status = intent.getStringExtra("Status");
            sharedPreferences();

            if(status.equals("connected")){
                try {
                    myDialog.dismiss();
                } catch(NullPointerException e){
                    e.printStackTrace();
                }

                Log.d(TAG, "mBroadcastReceiver5: Device now connected to "+mDevice.getName());
                Toast.makeText(MainActivity.this, "Device now connected to "+mDevice.getName(), Toast.LENGTH_LONG).show();
                editor.putString("connStatus", "Connected to " + mDevice.getName());
//                TextView connStatusTextView = findViewById(R.id.connStatusTextView);
//                connStatusTextView.setText("Connected to " + mDevice.getName());
            }
            else if(status.equals("disconnected")){
                Log.d(TAG, "mBroadcastReceiver5: Disconnected from "+mDevice.getName());
                Toast.makeText(MainActivity.this, "Disconnected from "+mDevice.getName(), Toast.LENGTH_LONG).show();
//                mBluetoothConnection = new BluetoothConnectionService(MainActivity.this);
//                mBluetoothConnection.startAcceptThread();

                editor.putString("connStatus", "Disconnected");
//                TextView connStatusTextView = findViewById(R.id.connStatusTextView);
//                connStatusTextView.setText("Disconnected");

                myDialog.show();
            }
            editor.commit();
        }
    };

    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("receivedMessage");
            showLog("receivedMessage: message --- " + message);
            try{
                if (message.substring(0,6).equals("STATUS")){
                    String[]statusMessage= message.split(":");
                    robotStatusTextView.setText(statusMessage[1]);
                    wait(5000);
                }
            } catch (Exception e) {
                showLog("Adding Image Failed");
            }
            try {
                if (message.length() > 7 && message.substring(2,6).equals("grid")) {
                    String resultString = "";
                    String amdString = message.substring(11,message.length()-2);
                    showLog("amdString: " + amdString);
                    BigInteger hexBigIntegerExplored = new BigInteger(amdString, 16);
                    String exploredString = hexBigIntegerExplored.toString(2);

                    while (exploredString.length() < 300)
                        exploredString = "0" + exploredString;

                    for (int i=0; i<exploredString.length(); i=i+15) {
                        int j=0;
                        String subString = "";
                        while (j<15) {
                            subString = subString + exploredString.charAt(j+i);
                            j++;
                        }
                        resultString = subString + resultString;
                    }
                    hexBigIntegerExplored = new BigInteger(resultString, 2);
                    resultString = hexBigIntegerExplored.toString(16);

                    JSONObject amdObject = new JSONObject();
                    amdObject.put("explored", "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
                    amdObject.put("length", amdString.length()*4);
                    amdObject.put("obstacle", resultString);
                    JSONArray amdArray = new JSONArray();
                    amdArray.put(amdObject);
                    JSONObject amdMessage = new JSONObject();
                    amdMessage.put("map", amdArray);
                    message = String.valueOf(amdMessage);
                    showLog("Executed for AMD message, message: " + message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (message.length() > 8 && message.substring(2,7).equals("image")) {
                    JSONObject jsonObject = new JSONObject(message);
                    JSONArray jsonArray = jsonObject.getJSONArray("image");
                    gridMap.drawImageNumberCell(jsonArray.getInt(0),jsonArray.getInt(1),jsonArray.getInt(2));
                    showLog("Image Added for index: " + jsonArray.getInt(0) + "," +jsonArray.getInt(1));
                }
            } catch (JSONException e) {
                showLog("Adding Image Failed");
            }
            //Function to read capture image from rpi (Updated)
            try {
                if (message.substring(0,3).equals("CAM")) {//IMAGE X,Y,ObstacleID.
                    String[]coorDist = message.split(" ");
                    String[]xyd = coorDist[1].split("_");
                    /*changeTextView("Ready");*/
                    Log.d(TAG, "onReceive: x: " + xyd[0] + " y: " + xyd[1] +" img Id: " +xyd[2]);
                    gridMap.drawImageNumberCell(Integer.parseInt(xyd[0]),Integer.parseInt(xyd[1]),Integer.parseInt(xyd[2]));
                    Log.d(TAG, "onReceive: image added to x: " + xyd[0] + " y: " + xyd[1] +" img Id: " +xyd[2]);
                    /*TODO check if on image,x,y,31 that it will stay as not found,
                       else found then upon algo get segment it will be less by 1 when a obstacle is found.
                       Then check if obstacleDirectionCoord is updated accordingly to its found bit [3] when
                       an obstacle is found.*/

                    //even tho say IMAGE 19,0,08 shld be the btm right hand side of grid,
                    // obsDirectionCoordDetails(x,y,direction,foundbit) as 20,1,0,0. x = 20 y = 1. Need to -1 when comparing
                    ArrayList<String[]> tempObsDirectionCoord = gridMap.getObstacleDirectionCoord();
                    for (String[] obsDirectionCoordDetails :tempObsDirectionCoord) {
                        Log.d(TAG, "onReceive: image added to x: " + obsDirectionCoordDetails[0] + " y: " + obsDirectionCoordDetails[1] + "Direction" +obsDirectionCoordDetails[2] + "found bit" + obsDirectionCoordDetails[3]);
                        if ((Integer.parseInt(obsDirectionCoordDetails[0])-1)==Integer.parseInt(xyd[0]) &&
                                (Integer.parseInt(obsDirectionCoordDetails[1])-1)==Integer.parseInt(xyd[1]) &&
                                Integer.parseInt(xyd[2])<31)//y,x
                        {//IMAGE x,y,direction if else method here will be mainly be used for manual since no IR sensor to detect.

                            obsDirectionCoordDetails[3]="1";
                            gridMap.updateObstacleDirectionCoord(tempObsDirectionCoord);
                            Toast.makeText(MainActivity.this, "FOUND Obs Image!", Toast.LENGTH_LONG).show();
                            robotStatusTextView.setText("Obstacle Found");
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Bulleye detected!", Toast.LENGTH_LONG).show();
                        }
                        Log.d(TAG, "onReceive: image added to x: " + obsDirectionCoordDetails[1] + " y: " + obsDirectionCoordDetails[0] +" Direction: " +obsDirectionCoordDetails[2] + "found bit" + obsDirectionCoordDetails[3]);
                        //MainActivity.printMessage("onReceive: image added to x: " + obsDirectionCoordDetails[1] + " y: " + obsDirectionCoordDetails[0] +" img Id: " +obsDirectionCoordDetails[2] + "found bit" + obsDirectionCoordDetails[3]);
                    }
                }
            } catch (Exception e) {
                showLog("Adding Image Failed");
            }

            if (gridMap.getAutoUpdate() || MapTabFragment.manualUpdateRequest) {
                try {
                    gridMap.setReceivedJsonObject(new JSONObject(message));
                    MapTabFragment.manualUpdateRequest = false;
                    showLog("messageReceiver: try decode successful");
                } catch (JSONException e) {
                    showLog("messageReceiver: try decode unsuccessful");
                }
            }
            //String chkRecievedText = sharedPreferences.getString("message", "") + message;
            try{
            if(message.substring(0,5).equals("ROBOT"))
            {
                //TODO add checking validator for valid or invalid placement?
                //Log.d(TAG, "onReceive: " + message.substring(7,message.length()));//substring 6 to message.length will give 08,08,N rather than 7 gives 8,08,N
                String[]coorDist = message.split(" ");
                String[]xyd = coorDist[1].split(",");
                Log.d(TAG, "onReceive: x: " + xyd[0] + " y: " + xyd[1] +" d: " +xyd[2]);
                int[] currCood = gridMap.getCurCoord();
                gridMap.setOldRobotCoord(currCood[0],currCood[1]);
                gridMap.setCurCoord(Integer.parseInt(xyd[0]),Integer.parseInt(xyd[1]),xyd[2]);
                wait(1500);
            }
            }catch (Exception e) {
                showLog("messageReceiver: try decode unsuccessful");
            }
            sharedPreferences();

            String receivedText = sharedPreferences.getString("message", "") + "\n" + message;

            editor.putString("message", receivedText);
            editor.commit();
            refreshMessageReceived();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    mBTDevice = (BluetoothDevice) data.getExtras().getParcelable("mBTDevice");
                    myUUID = (UUID) data.getSerializableExtra("myUUID");
                }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver5);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver5);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        try{
            IntentFilter filter2 = new IntentFilter("ConnectionStatus");
            LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver5, filter2);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        showLog("Entering onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putString(TAG, "onSaveInstanceState");
        showLog("Exiting onSaveInstanceState");
    }
}