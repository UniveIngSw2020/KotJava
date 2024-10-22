package com.example.test1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class FirstAccessActivity extends AppCompatActivity  {

    public  static final int REQUEST_CODE = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent activitymaps = new Intent(FirstAccessActivity.this,MapsActivity.class);

        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstaccess);
        Button btaccess = findViewById(R.id.button);

        Toolbar myToolbar = findViewById(R.id.fa_toolbar);
        setSupportActionBar(myToolbar);
        if( getSupportActionBar() != null ){
            getSupportActionBar().setTitle("CountOnYou");
        }

        TextView textView = findViewById(R.id.tv_guide_fa);
        textView.setMovementMethod(new ScrollingMovementMethod());


        /*se c' è il permesso per l' utilizzo del gps, passo alla MapsActivity, mentre se manca eseguo questa classe*/
        if (checkPermission(getApplicationContext())){
            startActivity(activitymaps);
            finish();
        }
        else{
           // Permessi pericolosi gestiti in runtime
            btaccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkRequestpermission();
                }
            });
        }


    }


    /*metodo che gestisce il controllo e l' accettazione dei permessi*/
    public void checkRequestpermission() {
        Intent activitymaps = new Intent(FirstAccessActivity.this,MapsActivity.class);
           if(!checkPermission(FirstAccessActivity.this)) {
                // when permission are not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(FirstAccessActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(FirstAccessActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstAccessActivity.this);
                    builder.setTitle("Grant those permission");
                    builder.setMessage("Read gps location data");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            ActivityCompat.requestPermissions(FirstAccessActivity.this,
                                    new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
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
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                            REQUEST_CODE);
                }
                } else {
                    // permessi sono già garantiti
                    Toast.makeText(getApplicationContext(), "permission already granted", Toast.LENGTH_SHORT).show();
                    // passa alla Mapsactivity
                startActivity(activitymaps);
            }
    }



    /* metodo per controllare i permessi*/
    public static boolean checkPermission(Context context){
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    + ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    /**Dangerous permission**/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Intent activitymaps = new Intent(FirstAccessActivity.this,MapsActivity.class);
        if(requestCode == REQUEST_CODE){
            if( grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                //permessi accettati
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();


                startActivity(activitymaps);
            }
            else{
                // permessi rifiutati
                Toast.makeText(getApplicationContext(),"Permission are denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

