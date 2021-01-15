package com.example.test1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
Classe che gestisce lo start della scansione bluetooth in background e ritorna un log alla fine

 */
public class AsyncBluetooth  extends AsyncTask<String,Void,List<BluetoothDevice>> {
    private Context context;
    final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    List<BluetoothDevice> devices  = new ArrayList<>();

    onTaskComplatedBluetooth liestener;


    public AsyncBluetooth(Context context, onTaskComplatedBluetooth liestener) {
        //costruttore classe
        this.context = context;
        this.liestener = liestener;
    }

    @Override
    protected List<BluetoothDevice> doInBackground(String... voids) {
        if (!bluetoothAdapter.isEnabled()) {
            //controllo se il bluetooth e`attivo
            //altrimenti notifica tramite dialog
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableBtIntent, 1);
        } else {


            if (!bluetoothAdapter.isDiscovering()) {
                Log.e("DISCOVERING", "start discovery");

                try {
                    bluetoothAdapter.startDiscovery();

                    Thread.sleep(5000); //sleep che da tempo al BroadcastReceiver del bluetooth  di scansionare i bluetooth vicini attivi

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
    protected void onPostExecute(List<BluetoothDevice> s) { //metodo che viene chiamato una volta completata la doinBackground
        liestener.onTaskComplatedBluetooth(s);

    }



}
