package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.dynamic.SupportFragmentWrapper;

public class FavouritesHistoryActivity extends AppCompatActivity {
//Questa activity dovrebbe fare da base per i fragment di history e favourites
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int val = getIntent().getIntExtra("val", 0 );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_history);

        Toolbar toolbar = findViewById(R.id.fh_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Per mettere il back button

        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
            switch(val){
                case 1:
                    //Nel caso l'activity venga aperta con "val = 1", cioè premendo sul bottone dei
                      //preferiti, viene inizializzato il fragment dei preferiti
                    fragmentTransaction.add(R.id.fav_hist_frag, FavouritesFragment.newInstance(), null).commit();
                    break;
//Ho ancora dei dubbi su come fare 2 e 3 quindi metto così
                case 2:
                    //fragmentTransaction.add(R.id.fav_hist_frag, StatsFragment.newInstance(), null).commit();
                    break;
                case 3:
                    //fragmentTransaction.add(R.id.fav_hist_frag, LocationFragment.newInstance(), null).commit();
                    break;
                case 4:
                    fragmentTransaction.add(R.id.fav_hist_frag, HistoryFragment.newInstance(), null).commit();
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(FavouritesHistoryActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}