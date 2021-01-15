package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class StatsActivity extends AppCompatActivity {

    //Questa activity dovrebbe servire per le statistiche sull'affluenza
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = findViewById(R.id.stats_toolbar);
        setSupportActionBar(toolbar);
        if( getSupportActionBar() != null ){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Per mettere il back button
            getSupportActionBar().setTitle("Informazioni sull'affluenza");
        }


        //Lista degli id dei dispositivi trovati e del numero di dispositivi trovati da essi
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        StatsAdapter statsAdapter = new StatsAdapter(AsyncReceive.jsonArray); //kjo merr 2 array 1 me array string njera me integer duhet te kaloj po list
        //duhet te kaloj listem mga maps activity me put extra
        recyclerView.setAdapter(statsAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(StatsActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}