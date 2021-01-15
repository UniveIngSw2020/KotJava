package com.example.test1;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BluetoothReceiver extends BroadcastReceiver {

    // Estende il broadcast reciever per visualizzare i dispositivi Bluetooth
    List<BluetoothDevice> devices  = new ArrayList<>();

    public int a = 1;


    // Ritorna la lista di dispositivi
    public List<BluetoothDevice>  getDevices(){
        return devices;
    }
    // Resetta la lista dei dispositivi
    public void  resetDevices(){
        devices = new ArrayList<>();
    }

    // metodo overraidato della superclasse
    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();
        // Quando trova dispositivi Bluetooth
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Prende il Bluetooth device dall' intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.i("BLUERECEIVER","trovato");
            devices.add(device);
        }

    }


}
