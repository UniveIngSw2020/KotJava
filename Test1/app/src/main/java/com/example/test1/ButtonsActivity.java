package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class ButtonsActivity extends AppCompatActivity {
    //Questa activity dovrebbe fare da base per i fragment di history e favourites
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int val = getIntent().getIntExtra("val", 0 );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        Toolbar toolbar = findViewById(R.id.bt_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Per mettere il back button

        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
            switch(val){
//Ho ancora dei dubbi su come fare 2 e 3 quindi metto così
                case 2:
                    getSupportActionBar().setTitle("Informazioni sull'affluenza");
                    //fragmentTransaction.replace(R.id.buttons_frag, StatsFragment.newInstance()).commit();
                                       //.add(R.id.buttons_frag, StatsFragment.newInstance(), null)
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ButtonsActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}