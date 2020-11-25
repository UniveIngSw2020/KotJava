package com.example.test1;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class  MyPositionLayer extends AppCompatActivity
implements GoogleMap.OnMyLocationButtonClickListener,GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
