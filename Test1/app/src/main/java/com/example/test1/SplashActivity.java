package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bisogna fare il check se è il primo lancio o meno e nel caso far partire l'altra
         //activity
        Intent intent = new Intent(this, FirstAccessActivity.class);
        startActivity(intent);
        finish();
    }
}