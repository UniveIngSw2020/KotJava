package com.example.test1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class FirstAccessActivity extends AppCompatActivity  {
    public  static final int REQUEST_CODE = 123;
    private Button btaccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent activitymaps = new Intent(FirstAccessActivity.this,MapsActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstaccess);
        Button btaccess = findViewById(R.id.button);

            /*guardo se ho il permesso per il gps, se ce l ho salto direttamente alla maps activity*/
            if (ContextCompat.checkSelfPermission(FirstAccessActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startActivity(activitymaps);
            }

            /** Dangerous permission**/
            btaccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkRequestpermission();
                }
            });
    }


    /*metodo che gestisce il controllo e l' accettazione dei permessi*/
    public void checkRequestpermission() {
        Intent activitymaps = new Intent(FirstAccessActivity.this,MapsActivity.class);
        /*da togliere il primo if, rindondante ----> faccio gia il controllo nella ONcreate*/


            if (ContextCompat.checkSelfPermission(FirstAccessActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // when permission are not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(FirstAccessActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstAccessActivity.this);
                    builder.setTitle("Grant those permission");
                    builder.setMessage("Read gps location data");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            ActivityCompat.requestPermissions(FirstAccessActivity.this,
                                    new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                    },
                                    REQUEST_CODE);

                        }
                    });

                    builder.setNegativeButton("cancel", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {

                    ActivityCompat.requestPermissions(FirstAccessActivity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            },
                            REQUEST_CODE);
                }
                } else {
                    // when permission are already granted
                    Toast.makeText(getApplicationContext(), "permission already granted", Toast.LENGTH_SHORT).show();


                startActivity(activitymaps);
            }
    }







    /**Dangerous permission**/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Intent activitymaps = new Intent(FirstAccessActivity.this,MapsActivity.class);
        if(requestCode == REQUEST_CODE){
            if( grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                //permission are granted
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();


                startActivity(activitymaps);
            }
            else{
                // permission are denied
                Toast.makeText(getApplicationContext(),"Permission are denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

