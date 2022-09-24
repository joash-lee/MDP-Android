package com.example.mdp_android.ui.main;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mdp_android.R;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private LayoutInflater eLayoutInflater;
    private ArrayList<BluetoothDevice> myDevices;
    private int eViewResourceId;

    public DeviceListAdapter(Context context, int tvResourceId, ArrayList<BluetoothDevice> devices) {
        super(context, tvResourceId, devices);
        this.myDevices = devices;
        eLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        eViewResourceId = tvResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("DeviceListAdapter", "Getting View");
        convertView = eLayoutInflater.inflate(eViewResourceId, null);

        BluetoothDevice device = myDevices.get(position);

        if (device != null) {
            TextView deviceName = (TextView) convertView.findViewById(R.id.deviceName);
            TextView deviceAdress = (TextView) convertView.findViewById(R.id.deviceAddress);

            if (deviceName != null) {
                deviceName.setText(device.getName());
            }
            if (deviceAdress != null) {
                deviceAdress.setText(device.getAddress());
            }
        }

        return convertView;
    }
}