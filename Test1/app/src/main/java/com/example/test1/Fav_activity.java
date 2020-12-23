package com.example.test1;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Fav_activity extends AppCompatActivity {
    private LocationManager locationManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_activity);
        final ListView listview = findViewById(R.id.listview);
        Button button = findViewById(R.id.button);
        final ArrayList<Location> array = new ArrayList();

        final ArrayAdapter arrayadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        listview.setAdapter(arrayadapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirstAccessActivity.checkPermission(getApplicationContext())) {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null)

                        arrayadapter.add(location);
                    listview.setAdapter(arrayadapter);

                }

            }
        });
    }
}