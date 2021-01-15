package com.example.test1;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

public class Networkreciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            if(isOnline(context)){
                // ONLINE
            }
            else{
                Toast.makeText(context, "YOU ARE OFFLINE", Toast.LENGTH_SHORT).show();
                buildAlertMessageInternet(context);
            }
        }
    }
    // attivazione da parte dell' utente dei dati(wifi o mobili)--> alertdialog
    private void buildAlertMessageInternet(Context context) {
        final Context cont = context;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You are offline, active data ")
                .setCancelable(false)
                .setPositiveButton("enable mobile data", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        cont.startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));

                    }
                })
                .setNegativeButton("enable wifi ", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        cont.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        System.exit(0);
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }
    //restituisce se il dispositivo è online
    public static  boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}

