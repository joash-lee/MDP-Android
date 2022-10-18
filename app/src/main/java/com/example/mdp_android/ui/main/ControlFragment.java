package com.example.mdp_android.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mdp_android.MainActivity;
import com.example.mdp_android.R;

import static android.content.Context.SENSOR_SERVICE;

public class ControlFragment extends Fragment implements SensorEventListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "ControlFragment";
    private PageViewModel pageViewModel;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // Control Button
    ImageButton moveForwardbtn, turnRightbtn, moveBackbtn, turnLeftbtn,
            forwardRightbtn, forwardLeftbtn, backwardRightbtn, backwardLeftbtn, stopbtn,
            resetexplorebtn, resetfastestbtn;
    private static long exploreTimer, fastestTimer;
    ToggleButton explorebtn, fastestbtn;
    TextView robotStatustv, tvSbDistance;
    private static TextView exploreTimetv, fastestTimetv;
    Switch phoneTiltSwitch;
    SeekBar sbDistance;
    private boolean isStraightMovement;

    SeekBar sbRotation;

    private static GridMap gridMap;

    private Sensor eSensor;
    private SensorManager eSensorManager;
    private Button takePicBtn;
    private TextView tvSbRotation;

    private static int sbDistanceCount,rotationCount,rotationid;
    private static String subStrngMessage = "",finalUpdateStatusMsg="";
    // Timer
    //static Runnable timerRunnableExplore;
    static Handler timerHandler = new Handler();



         static Runnable timerRunnableExplore = new Runnable() {
            @Override
            public void run() {
                long millisExplore = System.currentTimeMillis() - exploreTimer;
                int secondsExplore = (int) (millisExplore / 1000);
                int minutesExplore = secondsExplore / 60;
                secondsExplore = secondsExplore % 60;

                exploreTimetv.setText(String.format("%02d:%02d", minutesExplore, secondsExplore));

                timerHandler.postDelayed(this, 500);
            }
        };


    Runnable timerRunnableFastest = new Runnable() {
        @Override
        public void run() {
            long millisFastest = System.currentTimeMillis() - fastestTimer;
            int secondsFastest = (int) (millisFastest / 1000);
            int minutesFastest = secondsFastest / 60;
            secondsFastest = secondsFastest % 60;

            fastestTimetv.setText(String.format("%02d:%02d", minutesFastest, secondsFastest));

            timerHandler.postDelayed(this, 500);
        }
    };



    // Fragment Constructor
    public static ControlFragment newInstance(int index) {
        ControlFragment fragment = new ControlFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        isStraightMovement =false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // inflate
        View root = inflater.inflate(R.layout.activity_control, container, false);

        // get shared preferences
        sharedPreferences = getActivity().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);


        // variable initialization
        moveForwardbtn = root.findViewById(R.id.forwardImageBtn);
        turnRightbtn = root.findViewById(R.id.rightImageBtn);
        moveBackbtn = root.findViewById(R.id.backImageBtn);
        turnLeftbtn = root.findViewById(R.id.leftImageBtn);
        forwardRightbtn = root.findViewById(R.id.forwardRightImageBtn);
        forwardLeftbtn = root.findViewById(R.id.forwardLeftImageBtn);
        backwardRightbtn = root.findViewById(R.id.backwardRightImageBtn);
        backwardLeftbtn =root.findViewById(R.id.backwardLeftImageBtn);
        stopbtn =root.findViewById(R.id.stopImageBtn);
        exploreTimetv = root.findViewById(R.id.exploreTimeTextView);
        fastestTimetv = root.findViewById(R.id.fastestTimeTextView);
        explorebtn = root.findViewById(R.id.exploreToggleBtn);
        fastestbtn = root.findViewById(R.id.fastestToggleBtn);
        resetexplorebtn = root.findViewById(R.id.exploreResetImageBtn);
        resetfastestbtn = root.findViewById(R.id.fastestResetImageBtn);
        phoneTiltSwitch = root.findViewById(R.id.phoneTiltSwitch);
        tvSbDistance = root.findViewById(R.id.txtViewSbDistance);
        sbDistance = root.findViewById(R.id.sbDistance);//seekbar is the slider
        takePicBtn = root.findViewById(R.id.takePic);
        sbRotation = root.findViewById(R.id.sbRotation);
        tvSbRotation = root.findViewById(R.id.tvRotation);
        robotStatustv = MainActivity.getRobotStatusTextView();
        fastestTimer = 0;
        exploreTimer = 0;

        eSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        eSensor = eSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        gridMap = MainActivity.getGridMap();

        //Button
        takePicBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                MainActivity.printMessage("TAKEPIC|");
            }
        });

        // Button Listener
        moveForwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUseDistanceElseRotate(true);
                watchTVDistance();
                gridMap.setRotationstatus(false);
                isStraightMovement = true;
                showLog("Clicked moveForwardImageBtn");
                if (gridMap.getAutoUpdate())
                    updateStatus("Please press 'MANUAL'");
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    gridMap.moveRobot("forward");
                    MainActivity.refreshLabel();
                    if (gridMap.getValidPosition())
                        updateStatus("moving forward "+ finalUpdateStatusMsg); //(sbDistanceCount*10)+"cm");
                    else
                        updateStatus("Unable to move forward by "+ finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");
                    //MainActivity.printMessage("FORWARD|"+subStrngMessage);
                    MainActivity.printMessage("f");
                    isStraightMovement = false;
                }
                else
                    updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting moveForwardImageBtn");
            }

        });
        moveBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUseDistanceElseRotate(true);
                watchTVDistance();
                gridMap.setRotationstatus(false);
                isStraightMovement = true;
                showLog("Clicked moveBackwardImageBtn");
                if (gridMap.getAutoUpdate())
                    updateStatus("Please press 'MANUAL'");
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    gridMap.moveRobot("back");
                    MainActivity.refreshLabel();
                    if (gridMap.getValidPosition())
                        updateStatus("moving backward "+finalUpdateStatusMsg);
                    else
                        updateStatus("Unable to move backward by "+finalUpdateStatusMsg);
                    MainActivity.printMessage("BACK|"+subStrngMessage);
                    isStraightMovement = false;
                }
                else
                    updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting moveBackwardImageBtn");
            }
        });
        turnRightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridMap.setRotationstatus(false);
                showLog("Clicked turnRightImageBtn");
                if (gridMap.getAutoUpdate())
                    updateStatus("Please press 'MANUAL'");
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    gridMap.moveRobot("right");
                    MainActivity.refreshLabel();
                    //MainActivity.printMessage("RIGHT|");
                }
                else
                    updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting turnRightImageBtn");
            }
        });
          turnLeftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridMap.setRotationstatus(false);
                showLog("Clicked turnLeftImageBtn");
                if (gridMap.getAutoUpdate())
                    updateStatus("Please press 'MANUAL'");
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    gridMap.moveRobot("left");
                    MainActivity.refreshLabel();
                    updateStatus("turning left");
                    //MainActivity.printMessage("LEFT|");
                }
                else
                    updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting turnLeftImageBtn");
            }
        });
        forwardLeftbtn.setOnClickListener(new View.OnClickListener() {//left then forward
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showLog("Clicked forwardLeftImageBtn");
                isUseDistanceElseRotate(false);
                watchTVRotation();
                gridMap.setRotationstatus(true);
                if(rotationCount!=0)
                {
                if (gridMap.getAutoUpdate())
                    updateStatus("Please press 'MANUAL'");
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    gridMap.setRotationstatus(true);
                    gridMap.moveRobot("forward");
                    MainActivity.refreshLabel();
                    if (gridMap.getValidPosition())
                        updateStatus("moving Lforward before turn L "+finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");
                    else
                        //updateStatus("Unable to move forward");//requires new method to detect if can move forward with certain rotation...
                        updateStatus("Unable to move Lforward by before turn L "+finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");

                    gridMap.moveRobot("left");
                    MainActivity.refreshLabel();
                    updateStatus("turning left");//turn left
                    gridMap.setRotationstatus(true);
                    gridMap.moveRobot("forward");
                    MainActivity.refreshLabel();
                    gridMap.updateRotationDirection(true);//false means Right turn

                    if (gridMap.getValidPosition())
                        updateStatus("moving Lforward "+finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");
                    else
                        //updateStatus("Unable to move forward");//requires new method to detect if can move forward with certain rotation...
                        updateStatus("Unable to move Lforward by "+finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");
                    MainActivity.printMessage("LFORWARD|"+subStrngMessage);//+(sbDistanceCount*10)+"cm");
                }
                else
                    updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting forwardLeftImageBtn");
                }
            }
        });
        forwardRightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUseDistanceElseRotate(false);
                watchTVRotation();
                showLog("Clicked forwardRightImageBtn");
                if(rotationCount!=0)
                {
                if (gridMap.getAutoUpdate())
                    updateStatus("Please press 'MANUAL'");
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    gridMap.setRotationstatus(true);
                    gridMap.moveRobot("forward");
                    MainActivity.refreshLabel();
                    if (gridMap.getValidPosition())
                        updateStatus("moving Rforward before turn R "+finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");
                    else
                        updateStatus("Unable to move Rforward by before turn R "+finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");//(sbDistanceCount*10)+"cm");
                    gridMap.moveRobot("right");
                    MainActivity.refreshLabel();
                    updateStatus("turning right");//turn right
                    gridMap.setRotationstatus(true);
                    gridMap.moveRobot("forward");
                    MainActivity.refreshLabel();
                    gridMap.updateRotationDirection(false);//false means Right turn

                    if (gridMap.getValidPosition())
                        updateStatus("moving Rforward "+finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");
                    else
                        updateStatus("Unable to move Rforward by "+finalUpdateStatusMsg);//(sbDistanceCount*10)+"cm");
                    MainActivity.printMessage("RFORWARD|"+subStrngMessage);//(sbDistanceCount*10)+"cm");
                }
                else
                    updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting forwardRightImageBtn");
                }
            }
        });
        backwardLeftbtn.setOnClickListener(new View.OnClickListener() {//left then backward
            @Override
            public void onClick(View view) {
                isUseDistanceElseRotate(false);
                watchTVRotation();
                gridMap.setRotationstatus(true);
                showLog("Clicked backwardLeftImageBtn");
                if(rotationCount!=0)
                {
                if (gridMap.getAutoUpdate())
                    updateStatus("Please press 'MANUAL'");
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    gridMap.setRotationstatus(true);
                    gridMap.moveRobot("back");
                    MainActivity.refreshLabel();
                    if (gridMap.getValidPosition())
                        updateStatus("moving Lbackward before turn L"+finalUpdateStatusMsg);
                    else
                        updateStatus("Unable to move Lbackward before turn L by "+finalUpdateStatusMsg);
                    gridMap.moveRobot("right");
                    MainActivity.refreshLabel();
                    updateStatus("turning left");//turn left
                    gridMap.setRotationstatus(true);
                    gridMap.moveRobot("back");
                    MainActivity.refreshLabel();
                    gridMap.updateRotationDirection(true);//false means Right turn
                    if (gridMap.getValidPosition())
                        updateStatus("moving Lbackward"+finalUpdateStatusMsg);
                    else
                        updateStatus("Unable to move Lbackward by "+finalUpdateStatusMsg);
                    MainActivity.printMessage("LBACK|"+subStrngMessage);
                }
                else
                    updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting backwardLeftImageBtn");
                }
            }
        });
        backwardRightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUseDistanceElseRotate(false);
                watchTVRotation();
                gridMap.setRotationstatus(true);
                showLog("Clicked backwardRightImageBtn");
                if(rotationCount!=0)
                {
                if (gridMap.getAutoUpdate())
                    updateStatus("Please press 'MANUAL'");
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    gridMap.setRotationstatus(true);
                    gridMap.moveRobot("back");
                    MainActivity.refreshLabel();
                    if (gridMap.getValidPosition())
                        updateStatus("moving Lbackward before turn R "+finalUpdateStatusMsg);
                    else
                        updateStatus("Unable to move Lbackward before turn R by "+finalUpdateStatusMsg);
                    gridMap.moveRobot("left");
                    MainActivity.refreshLabel();
                    updateStatus("turning right");//turn right
                    gridMap.setRotationstatus(true);
                    gridMap.moveRobot("back");
                    MainActivity.refreshLabel();
                    gridMap.updateRotationDirection(false);//false means Right turn
                    if (gridMap.getValidPosition())
                        updateStatus("moving Rbackward"+finalUpdateStatusMsg);
                    else
                        updateStatus("Unable to move Rbackward by "+finalUpdateStatusMsg);
                    MainActivity.printMessage("RBACK|"+subStrngMessage);
                }
                else
                    updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting backwardRightImageBtn");
                }
            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View view) {
                watchTVRotation();
                watchTVDistance();
                gridMap.setRotationstatus(false);
                showLog("Clicked stopImageBtn");
                //if (gridMap.getAutoUpdate())
                 //   updateStatus("Please press 'MANUAL'");
                //else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {

                    updateStatus("movement stop");//turn right

                    MainActivity.printMessage("STOP|");
              // }
                //else
                    //updateStatus("Please press 'STARTING POINT'");
                showLog("Exiting stopImageBtn");
            }
        });

        //Method for dragging slider for distance
        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override //make it so that theres a global string for the integer distance to be added to the movement, e.g FORWARD|8
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sbDistanceCount = i;
                tvSbDistance.setText(String.valueOf(sbDistanceCount));
                watchTVDistance();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                tvSbDistance.setText(String.valueOf(sbDistanceCount));
                gridMap.updateSbDistance(sbDistanceCount);//goes into gridmap
            }
        });

        sbRotation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // updated continuously as the user slides the thumb
                tvSbRotation.setText(String.valueOf(progress));
                rotationCount = progress;
                //method to check against txtView for accurate rotation
                watchTVRotation();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
                rotationid =1;
                gridMap.updateRotation(rotationCount);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
                tvSbRotation.setText(String.valueOf(rotationCount));
            }
        });



        explorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog("Clicked exploreToggleBtn");
                ToggleButton exploreToggleBtn = (ToggleButton) v;
                if (exploreToggleBtn.getText().equals("EXPLORE")) {
                    showToast("Exploration timer stop!");
                    robotStatustv.setText("Exploration Stopped");
                    timerHandler.removeCallbacks(timerRunnableExplore);
                }
                else if (exploreToggleBtn.getText().equals("STOP")) {
                    showToast("Exploration timer start!");
                    MainActivity.printMessage("ES|");
                    robotStatustv.setText("Exploration Started");
                    exploreTimer = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnableExplore, 0);
                }
                else {
                    showToast("Else statement: " + exploreToggleBtn.getText());
                }
                showLog("Exiting exploreToggleBtn");
            }
        });

        fastestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog("Clicked fastestToggleBtn");
                ToggleButton fastestToggleBtn = (ToggleButton) v;
                if (fastestToggleBtn.getText().equals("FASTEST")) {
                    showToast("Fastest timer stop!");
                    robotStatustv.setText("Fastest Path Stopped");
                    timerHandler.removeCallbacks(timerRunnableFastest);
                }
                else if (fastestToggleBtn.getText().equals("STOP")) {
                    showToast("Fastest timer start!");
                    MainActivity.printMessage("FS|");
                    robotStatustv.setText("Fastest Path Started");
                    fastestTimer = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnableFastest, 0);
                }
                else
                    showToast(fastestToggleBtn.getText().toString());
                showLog("Exiting fastestToggleBtn");            }
        });

        resetexplorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog("Clicked exploreResetImageBtn");
                showToast("Reseting exploration time...");
                exploreTimetv.setText("00:00");
                robotStatustv.setText("Not Available");
                if(explorebtn.isChecked())
                    explorebtn.toggle();
                //timerHandler.removeCallbacks(timerRunnableExplore);
                showLog("Exiting exploreResetImageBtn");            }
        });


        resetfastestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog("Clicked fastestResetImageBtn");
                showToast("Reseting fastest time...");
                fastestTimetv.setText("00:00");
                robotStatustv.setText("Not Available");
                if (fastestbtn.isChecked())
                    fastestbtn.toggle();
                timerHandler.removeCallbacks(timerRunnableFastest);
                showLog("Exiting fastestResetImageBtn");            }
        });

        phoneTiltSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (gridMap.getAutoUpdate()) {
                    updateStatus("Please press 'MANUAL'");
                    phoneTiltSwitch.setChecked(false);
                }
                else if (gridMap.getCanDrawRobot() && !gridMap.getAutoUpdate()) {
                    if(phoneTiltSwitch.isChecked()){
                        showToast("Tilt motion control: ON");
                        phoneTiltSwitch.setPressed(true);

                        eSensorManager.registerListener(ControlFragment.this, eSensor, eSensorManager.SENSOR_DELAY_NORMAL);
                        sensorHandler.post(sensorDelay);
                    }else{
                        showToast("Tilt motion control: OFF");
                        showLog("unregistering Sensor Listener");
                        try {
                            eSensorManager.unregisterListener(ControlFragment.this);
                        }catch(IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        sensorHandler.removeCallbacks(sensorDelay);
                    }
                } else {
                    updateStatus("Please press 'STARTING POINT'");
                    phoneTiltSwitch.setChecked(false);
                }
            }
        });


        return root;
    }

    private static void isUseDistanceElseRotate(boolean isStraightMovement)
    { //0 means default as movement 0cm 0 rotate but using 0cm message. -1 means movement only , 1 means rotation
        if(isStraightMovement)
        {//as long as rotation is 0, distance will be used.
            finalUpdateStatusMsg = (sbDistanceCount*10)+"cm";
            //subStrngMessage = (sbDistanceCount*10)+"cm";
            subStrngMessage = "f";
        }
        else{//rotation != 0
                finalUpdateStatusMsg = String.valueOf(rotationCount) + "degrees";
                //subStrngMessage = String.valueOf(rotationCount);
            subStrngMessage = "f";
            }
    }

    private void watchTVDistance()
    {
        sbDistanceCount = Integer.valueOf(tvSbDistance.getText().toString());
        gridMap.updateSbDistance(sbDistanceCount);//goes into gridmap
    }

    private void watchTVRotation()
    {
        rotationCount = Integer.valueOf(tvSbRotation.getText().toString());
        switch(rotationCount) {
            case 0: rotationid=0;
                    rotationCount=0;
                    sbRotation.setProgress(0);
            case 90:
                rotationid = 1;
                sbRotation.setProgress(90);
                break;
            case 180:
                rotationid = 2;
                break;
            case 270:
                rotationid = 3;
                break;
            case 360:
                rotationid = 4;
                break;
            default:
                rotationid = 1;
                break;
        }
        gridMap.updateRotation(rotationCount);
        gridMap.updaterotationid(rotationid);
    }
    private static void showLog(String message) {
        Log.d(TAG, message);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    Handler sensorHandler = new Handler();
    boolean sensorFlag= false;

    private final Runnable sensorDelay = new Runnable() {
        @Override
        public void run() {
            sensorFlag = true;
            sensorHandler.postDelayed(this,1000);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        showLog("SensorChanged X: "+x);
        showLog("SensorChanged Y: "+y);
        showLog("SensorChanged Z: "+z);

        if(sensorFlag) {//requires editing based on sensor input. need test with sensor first
            if (y < -2) {
                showLog("Sensor Move Forward Detected");
                gridMap.moveRobot("forward");
                MainActivity.refreshLabel();
                MainActivity.printMessage("N1|");//Sensor change this to
            } else if (y > 2) {
                showLog("Sensor Move Backward Detected");
                gridMap.moveRobot("back");
                MainActivity.refreshLabel();
                MainActivity.printMessage("S1|");
            } else if (x > 2) {
                showLog("Sensor Move Left Detected");
                gridMap.moveRobot("left");
                MainActivity.refreshLabel();
                MainActivity.printMessage("W|");
            } else if (x < -2) {
                showLog("Sensor Move Right Detected");
                gridMap.moveRobot("right");
                MainActivity.refreshLabel();
                MainActivity.printMessage("E|");
            }
        }
        sensorFlag = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            eSensorManager.unregisterListener(ControlFragment.this);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void updateStatus(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0, 0);
        toast.show();
    }
    private static void updateStatus(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0, 0);
        toast.show();
    }

}
