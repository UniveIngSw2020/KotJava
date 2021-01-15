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

    onTaskComplatedBluetooth liestener;


    public AsyncBluetooth(Context context, onTaskComplatedBluetooth liestener) {
        this.context = context;
        this.liestener = liestener;
    }

    @Override
    protected List<BluetoothDevice> doInBackground(String... voids) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            //context.registerReceiver(mReceiver,filter);
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

        liestener.onTaskComplatedBluetooth(s);

    }



}
