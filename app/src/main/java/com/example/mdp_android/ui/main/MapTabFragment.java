package com.example.mdp_android.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mdp_android.MainActivity;
import com.example.mdp_android.R;

import org.json.JSONObject;

public class MapTabFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "MapFragment";

    private PageViewModel pageViewModel;

    Button resetMapbtn, updatebtn;
    ImageButton directionChangeImageBtn, exploredImageBtn, obstacleImageBtn, clearImageBtn,
    northFaceObsBtn, eastFaceObsBtn, westFaceObsBtn,southFaceObsBtn;
    ToggleButton setStartPointToggleBtn, setWaypointToggleBtn;
    GridMap gridMap;
    private static boolean autoUpdate = false;
    public static boolean manualUpdateRequest = false;

    public static MapTabFragment newInstance(int index) {
        MapTabFragment fragment = new MapTabFragment();
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
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_map, container, false);

        gridMap = MainActivity.getGridMap();
        final DirectionFragment directionFragment = new DirectionFragment();

        resetMapbtn = root.findViewById(R.id.resetMapBtn);
        setStartPointToggleBtn = root.findViewById(R.id.setStartPointToggleBtn);
        setWaypointToggleBtn = root.findViewById(R.id.setWaypointToggleBtn);

        exploredImageBtn = root.findViewById(R.id.exploredImageBtn);
        obstacleImageBtn = root.findViewById(R.id.obstacleImageBtn);
        northFaceObsBtn = root.findViewById(R.id.northFaceObs);
        eastFaceObsBtn = root.findViewById(R.id.eastFaceObs);
        westFaceObsBtn = root.findViewById(R.id.westFaceObs);
        southFaceObsBtn = root.findViewById(R.id.southFaceObs);
        clearImageBtn = root.findViewById(R.id.clearImageBtn);
        updatebtn = root.findViewById(R.id.updateButton);

        resetMapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked resetMapBtn");
                showToast("Reseting map...");
                northFaceObsBtn.setVisibility(View.GONE);
                eastFaceObsBtn.setVisibility(View.GONE);
                westFaceObsBtn.setVisibility(View.GONE);
                southFaceObsBtn.setVisibility(View.GONE);
                gridMap.resetMap();
            }
        });

        setStartPointToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked setStartPointToggleBtn");
                if (setStartPointToggleBtn.getText().equals("STARTING POINT"))
                    showToast("Cancelled selecting starting point");
                else if (setStartPointToggleBtn.getText().equals("CANCEL") && !gridMap.getAutoUpdate()) {
                    showToast("Please select starting point");
                    gridMap.setStartCoordStatus(true);
                    gridMap.toggleCheckedBtn("setStartPointToggleBtn");
                } else
                    showToast("Please select manual mode");
                showLog("Exiting setStartPointToggleBtn");
            }
        });

        setWaypointToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked setWaypointToggleBtn");
                if (setWaypointToggleBtn.getText().equals("WAYPOINT"))
                    showToast("Cancelled selecting waypoint");
                else if (setWaypointToggleBtn.getText().equals("CANCEL")) {
                    showToast("Please select waypoint");
                    gridMap.setWaypointStatus(true);
                    gridMap.toggleCheckedBtn("setWaypointToggleBtn");
                }
                else
                    showToast("Please select manual mode");
                showLog("Exiting setWaypointToggleBtn");
            }
        });

        exploredImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked exploredImageBtn");
                if (!gridMap.getExploredStatus()) {
                    showToast("Please check cell");
                    gridMap.setExploredStatus(true);
                    gridMap.toggleCheckedBtn("exploredImageBtn");
                }
                else if (gridMap.getExploredStatus())
                    gridMap.setSetObstacleStatus(false);
                showLog("Exiting exploredImageBtn");
            }
        });

        obstacleImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked obstacleImageBtn");
                if (!gridMap.getSetObstacleStatus()) {
                    showToast("Please plot obstacles");
                    gridMap.setSetObstacleStatus(true);
                    gridMap.toggleCheckedBtn("obstacleImageBtn");
                    northFaceObsBtn.setVisibility(View.VISIBLE);
                    eastFaceObsBtn.setVisibility(View.VISIBLE);
                    westFaceObsBtn.setVisibility(View.VISIBLE);
                    southFaceObsBtn.setVisibility(View.VISIBLE);

                }
                else if (gridMap.getSetObstacleStatus())
                {gridMap.setSetObstacleStatus(false);
                    northFaceObsBtn.setVisibility(View.GONE);
                    eastFaceObsBtn.setVisibility(View.GONE);
                    westFaceObsBtn.setVisibility(View.GONE);
                    southFaceObsBtn.setVisibility(View.GONE);
                }

                showLog("Exiting obstacleImageBtn");
            }
        });

        northFaceObsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked setnorthobstacledirectionbtn");
                GridMap.obstacleDirectionCoordinateStatus = true;
                GridMap.obstacleDirection = "0";
                showLog("Exiting setnorthobstacledirectionbtn");
            }
        });

        southFaceObsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked setsouthobstacledirectionbtn");
                GridMap.obstacleDirectionCoordinateStatus = true;
                GridMap.obstacleDirection = "2";
                showLog("Exiting setsouthobstacledirectionbtn");
            }
        });

        westFaceObsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked setwestobstacledirectionbtn");
                GridMap.obstacleDirectionCoordinateStatus = true;
                GridMap.obstacleDirection = "3";
                showLog("Exiting setwestobstacledirectionbtn");
            }
        });

        eastFaceObsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked seteastobstacledirectionbtn");
                GridMap.obstacleDirectionCoordinateStatus = true;
                GridMap.obstacleDirection = "1";
                showLog("Exiting seteastobstacledirectionbtn");
            }
        });
        clearImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked clearImageBtn");
                if (!gridMap.getUnSetCellStatus()) {
                    showToast("Please remove cells");
                    gridMap.setUnSetCellStatus(true);
                    gridMap.toggleCheckedBtn("clearImageBtn");
                }
                else if (gridMap.getUnSetCellStatus())
                    gridMap.setUnSetCellStatus(false);
                showLog("Exiting clearImageBtn");
            }
        });



        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog("Clicked updateButton");
                MainActivity.printMessage("sendArena");
                manualUpdateRequest = true;
                showLog("Exiting updateButton");
                try {
                    String message = "{\"map\":[{\"explored\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\"length\":300,\"obstacle\":\"00000000000000000706180400080010001e000400000000200044438f840000000000000080\"}]}";

                    gridMap.setReceivedJsonObject(new JSONObject(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return root;
    }

    private void showLog(String message) {
        Log.d(TAG, message);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}