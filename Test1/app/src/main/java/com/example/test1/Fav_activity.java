package com.example.test1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Fav_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_activity);
        ListView listview = findViewById(R.id.listview);

        ArrayList<String> array = new ArrayList();

        array.add("Belluno");
        array.add("Belluno");
        array.add("Belluno");
        array.add("Belluno");
        ArrayAdapter arrayadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        listview.setAdapter(arrayadapter);
    }
}