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


        String action = intent.getAction();
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


            Log.i("blue trovato","trovato");

            devices.add(device);



        }

    }


}
