package com.example.mdp_android.ui.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mdp_android.R;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothPopUp extends AppCompatActivity {
    private static final String TAG = "BluetoothPopUp";
    private String conStatus;
    BluetoothAdapter eBluetoothAdapter;
    public ArrayList<BluetoothDevice> NewBTDevice;
    public ArrayList<BluetoothDevice> PairedBTDevice;
    public DeviceListAdapter NewDeviceListAdapter;
    public DeviceListAdapter PairedDeviceListAdapter;
    TextView conStatusTV;
    ListView otherDevicesListView;
    ListView pairedDevicesListView;
    Button connectBtn;
    ProgressDialog PDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BluetoothConnectionService eBluetoothConnection;
    private static final UUID myUUID = UUID.fromString("c47e45bc-355f-11ed-b176-b543f0f9f7b6");
    public static BluetoothDevice ebtDevice;

    boolean retryConnection = false;
    Handler reconnectionHandler = new Handler();

    Runnable reconnectionRunnable = new Runnable() {
        @Override
        public void run() {
            // Magic here
            try {
                if (BluetoothConnectionService.BluetoothConnectionStatus == false) {
                    startBTConnection(ebtDevice, myUUID);
                    Toast.makeText(BluetoothPopUp.this, "Reconnection Success", Toast.LENGTH_SHORT).show();

                }
                reconnectionHandler.removeCallbacks(reconnectionRunnable);
                retryConnection = false;
            } catch (Exception e) {
                Toast.makeText(BluetoothPopUp.this, "Failed to reconnect, reconnecting in 5 second", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_pop_up_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        eBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Switch bluetoothSwitch = (Switch) findViewById(R.id.bluetoothSwitch);
        if(eBluetoothAdapter.isEnabled()){
            bluetoothSwitch.setChecked(true);
            bluetoothSwitch.setText("ON");
        }

        otherDevicesListView = (ListView) findViewById(R.id.otherDevicesListView);
        pairedDevicesListView = (ListView) findViewById(R.id.pairedDevicesListView);
        NewBTDevice = new ArrayList<>();
        PairedBTDevice = new ArrayList<>();

        connectBtn = (Button) findViewById(R.id.connectBtn);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(eBroadcastReceiver4, filter);

        IntentFilter filter2 = new IntentFilter("ConnectionStatus");
        LocalBroadcastManager.getInstance(this).registerReceiver(eBroadcastReceiver5, filter2);

        otherDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                eBluetoothAdapter.cancelDiscovery();
                pairedDevicesListView.setAdapter(PairedDeviceListAdapter);

                String deviceName = NewBTDevice.get(i).getName();
                String deviceAddress = NewBTDevice.get(i).getAddress();
                Log.d(TAG, "onItemClick: A device is selected.");
                Log.d(TAG, "onItemClick: DEVICE NAME: " + deviceName);
                Log.d(TAG, "onItemClick: DEVICE ADDRESS: " + deviceAddress);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Log.d(TAG, "onItemClick: Initiating pairing with " + deviceName);
                    NewBTDevice.get(i).createBond();

                    eBluetoothConnection = new BluetoothConnectionService(BluetoothPopUp.this);
                    ebtDevice = NewBTDevice.get(i);
                }
            }
        });

        pairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                eBluetoothAdapter.cancelDiscovery();
                otherDevicesListView.setAdapter(NewDeviceListAdapter);

                String deviceName =PairedBTDevice.get(i).getName();
                String deviceAddress =PairedBTDevice.get(i).getAddress();
                Log.d(TAG, "onItemClick: A device is selected.");
                Log.d(TAG, "onItemClick: DEVICE NAME: " + deviceName);
                Log.d(TAG, "onItemClick: DEVICE ADDRESS: " + deviceAddress);

                eBluetoothConnection = new BluetoothConnectionService(BluetoothPopUp.this);
                ebtDevice =PairedBTDevice.get(i);
            }
        });

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.d(TAG, "onChecked: Switch button toggled. Enabling/Disabling Bluetooth");
                if(isChecked){
                    compoundButton.setText("ON");
                }else
                {
                    compoundButton.setText("OFF");
                }

                if(eBluetoothAdapter ==null){
                    Log.d(TAG, "enableDisableBT: Bluetooth is not supported");
                    Toast.makeText(BluetoothPopUp.this, "Bluetooth is not supported", Toast.LENGTH_LONG).show();
                    compoundButton.setChecked(false);
                }
                else {
                    if (!eBluetoothAdapter.isEnabled()) {
                        Log.d(TAG, "enableDisableBT: Bluetooth is enable");
                        Log.d(TAG, "enableDisableBT: device is discoverable for 600 seconds.");

                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
                        startActivity(discoverableIntent);

                        compoundButton.setChecked(true);

                        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        registerReceiver(eBroadcastReceiver1, BTIntent);

                        IntentFilter discoverIntent = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                        registerReceiver(eBroadcastReceiver2, discoverIntent);
                    }
                    if (eBluetoothAdapter.isEnabled()) {
                        Log.d(TAG, "enableDisableBT: Bluetooth is diable");
                        eBluetoothAdapter.disable();

                        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        registerReceiver(eBroadcastReceiver1, BTIntent);
                    }
                }
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ebtDevice ==null)
                {
                    Toast.makeText(BluetoothPopUp.this, "Please Select a Device before starting connection.", Toast.LENGTH_LONG).show();
                }
                else {
                    startConnection();
                }
            }
        });


        ImageButton backBtn = findViewById(R.id.backBtn);

        conStatusTV = (TextView) findViewById(R.id.connStatusTextView);
        conStatus ="Disconnected";
        sharedPreferences = getApplicationContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("conStatus"))
            conStatus = sharedPreferences.getString("conStatus", "");

        conStatusTV.setText(conStatus);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.putString("conStatus", conStatusTV.getText().toString());
                editor.commit();
                finish();
            }
        });

        PDialog = new ProgressDialog(BluetoothPopUp.this);
        PDialog.setMessage("Waiting for device to reconnect...");
        PDialog.setCancelable(false);
        PDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void toggleButtonScan(View view){
        Log.d(TAG, "toggleButton: Scanning for unpaired devices.");
        NewBTDevice.clear();
        if(eBluetoothAdapter != null) {
            if (!eBluetoothAdapter.isEnabled()) {
                Toast.makeText(BluetoothPopUp.this, "Bluetooth is not turn on", Toast.LENGTH_SHORT).show();
            }
            if (eBluetoothAdapter.isDiscovering()) {
                eBluetoothAdapter.cancelDiscovery();
                Log.d(TAG, "toggleButton: Cancelling Scan.");

                checkBTPermissions();

                eBluetoothAdapter.startDiscovery();
                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(eBroadcastReceiver3, discoverDevicesIntent);
            } else if (!eBluetoothAdapter.isDiscovering()) {
                checkBTPermissions();

                eBluetoothAdapter.startDiscovery();
                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(eBroadcastReceiver3, discoverDevicesIntent);
            }
            PairedBTDevice.clear();
            Set<BluetoothDevice> pairedDevices = eBluetoothAdapter.getBondedDevices();
            Log.d(TAG, "toggleButton: Number of paired devices found: "+ pairedDevices.size());
            for(BluetoothDevice d : pairedDevices){
                Log.d(TAG, "Paired Devices: "+ d.getName() +" : " + d.getAddress());
                PairedBTDevice.add(d);
                PairedDeviceListAdapter = new DeviceListAdapter(this, R.layout.device_adapter_view,PairedBTDevice);
                pairedDevicesListView.setAdapter(PairedDeviceListAdapter);
            }
        }
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }
    }
    private final BroadcastReceiver eBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(eBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "eBroadcastReceiver1: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "eBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "eBroadcastReceiver1: STATE ON");

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "eBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver eBroadcastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(eBluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "eBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "eBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "eBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "eBroadcastReceiver2: Connecting...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "eBroadcastReceiver2: Connected.");
                        break;
                }
            }
        }
    };

    private BroadcastReceiver eBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if(action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                NewBTDevice.add(device);
                Log.d(TAG, "onReceive: "+ device.getName() +" : " + device.getAddress());
                NewDeviceListAdapter= new DeviceListAdapter(context, R.layout.device_adapter_view, NewBTDevice);
                otherDevicesListView.setAdapter(NewDeviceListAdapter);

            }
        }
    };

    private BroadcastReceiver eBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BOND_BONDED.");
                    Toast.makeText(BluetoothPopUp.this, "Successfully paired with " + mDevice.getName(), Toast.LENGTH_SHORT).show();
                    ebtDevice = mDevice;
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "BOND_BONDING.");
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "BOND_NONE.");
                }
            }
        }
    };

    private BroadcastReceiver eBroadcastReceiver5 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice mDevice = intent.getParcelableExtra("Device");
            String status = intent.getStringExtra("Status");
            sharedPreferences = getApplicationContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            if(status.equals("connected")){
                try {
                    PDialog.dismiss();
                } catch(NullPointerException e){
                    e.printStackTrace();
                }

                Log.d(TAG, "eBroadcastReceiver5: Device now connected to "+mDevice.getName());
                Toast.makeText(BluetoothPopUp.this, "Device now connected to "+mDevice.getName(), Toast.LENGTH_LONG).show();
                editor.putString("conStatus", "Connected to " + mDevice.getName());
                conStatusTV.setText("Connected to " + mDevice.getName());
            }
            else if(status.equals("disconnected") && retryConnection == false){
                Log.d(TAG, "eBroadcastReceiver5: Disconnected from "+mDevice.getName());
                Toast.makeText(BluetoothPopUp.this, "Disconnected from "+mDevice.getName(), Toast.LENGTH_LONG).show();
                eBluetoothConnection = new BluetoothConnectionService(BluetoothPopUp.this);
//                eBluetoothConnection.startAcceptThread();


                sharedPreferences = getApplicationContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("conStatus", "Disconnected");
                TextView conStatusTV= findViewById(R.id.connStatusTextView);
                conStatusTV.setText("Disconnected");
                editor.commit();

                try {
                    PDialog.show();
                }catch (Exception e){
                    Log.d(TAG, "BluetoothPopUp: eBroadcastReceiver5 Dialog show failure");
                }
                retryConnection = true;
                reconnectionHandler.postDelayed(reconnectionRunnable, 5000);

            }
            editor.commit();
        }
    };

    public void startConnection(){
        startBTConnection(ebtDevice,myUUID);
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection");

        eBluetoothConnection.startClientThread(device, uuid);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
        try {
            unregisterReceiver(eBroadcastReceiver1);
            unregisterReceiver(eBroadcastReceiver2);
            unregisterReceiver(eBroadcastReceiver3);
            unregisterReceiver(eBroadcastReceiver4);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(eBroadcastReceiver5);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: called");
        super.onPause();
        try {
            unregisterReceiver(eBroadcastReceiver1);
            unregisterReceiver(eBroadcastReceiver2);
            unregisterReceiver(eBroadcastReceiver3);
            unregisterReceiver(eBroadcastReceiver4);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(eBroadcastReceiver5);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("ebtDevice", ebtDevice);
        data.putExtra("myUUID",myUUID);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
