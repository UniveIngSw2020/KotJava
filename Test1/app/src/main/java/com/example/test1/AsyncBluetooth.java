package com.example.test1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AsyncBluetooth  extends AsyncTask<String,Void,List<BluetoothDevice>> {
    private Context context;
    final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    List<BluetoothDevice> devices  = new ArrayList<>();
    public BroadcastReceiver mReceiver   = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            //ArrayList<String> list = new ArrayList<>();
            Log.e("DEVICES DETECTED","ok");
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //Log.e("list",device.getAddress());
                //bluefound += 1;
               devices.add(device);
                //list.add(device.getName());

                //z = 1;
                //bluefound = list.size();
                //arrayAdapter.notifyDataSetChanged();
                // Toast.makeText(MapsActivity.this, "trovato almeno un dispositivo", Toast.LENGTH_SHORT).show();

            }
        }
    };;
    OnTaskDetected liestener;


    public AsyncBluetooth(Context context, OnTaskDetected liestener) {
        this.context = context;
        this.liestener = liestener;
    }

    @Override
    protected List<BluetoothDevice> doInBackground(String... voids) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mReceiver,filter);
            ((Activity) context).startActivityForResult(enableBtIntent, 1);
        } else {

            if (!bluetoothAdapter.isDiscovering()) {
                Log.e("DISCOVERING", "start discovery");

                try {
                    bluetoothAdapter.startDiscovery();

                    Thread.sleep(5000);

                    bluetoothAdapter.cancelDiscovery();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
       // context.unregisterReceiver(mReceiver);
        return devices ;
    }
    @Override
    protected void onPostExecute(List<BluetoothDevice> s) {

        liestener.onTaskDetected(s);

    }



}
