package com.example.test1;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BluetoothReceiver extends BroadcastReceiver {


    List<BluetoothDevice> devices  = new ArrayList<>();

    public int a = 1;



    public List<BluetoothDevice>  getDevices(){
        return devices;
    }

    public void  getDevic(){
        devices = new ArrayList<>();
    }


    public void onReceive(Context context, Intent intent) {
        //ArrayList<String> list = new ArrayList<>();
        Log.e("quanti blue","ok");
        String action = intent.getAction();
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // Add the name and address to an array adapter to show in a ListView
            //Log.e("list",device.getAddress());
            //bluefound += 1;
            //bluefound = 1;
            //
            //list.add(device.getName());
            //bmac = device.getAddress();

            Log.e("quanti blue","ok--------------------");



            devices.add(device);

            //bMac = bMac + String.valueOf(bmac);
            //Log.e("listA","blue: " + bluefound);
            //Log.e("listA",bMac);

            //z = 1;
            //bluefound = list.size();
            //arrayAdapter.notifyDataSetChanged();
            // Toast.makeText(MapsActivity.this, "trovato almeno un dispositivo", Toast.LENGTH_SHORT).show();

        }

    }


}
