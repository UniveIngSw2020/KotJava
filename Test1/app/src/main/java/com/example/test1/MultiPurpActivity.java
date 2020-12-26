package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class MultiPurpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int val = getIntent().getIntExtra("val", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_purp);


        Toolbar toolbar = findViewById(R.id.mp_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Per mettere il back button


        if (savedInstanceState == null) {
            switch (val) {
                case 1:
                    //Nel caso l'activity venga aperta con "val = 1", cioè selezionando la voce guida dal sottomenu
                    //help, viene inizializzato il fragment della guida
                    System.out.println("Accesso a MultiPurpActivity con fragment Guida con valore " + val );
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
//                            .add(R.id.menu_frag, GuideFragment.newInstance(), null)
                            .replace(R.id.menu_frag, GuideFragment.newInstance())
                            .commit();
                    break;
                /*Questi fragment devono ancora essere creati e implementati
                case 2:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.menu_frag, FAQFragment.newInstance(), null)
                            .commit();
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.menu_frag, ContactsFragment.newInstance(), null)
                            .commit();
                    break;
                case 4:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.menu_frag, CredistsFragment.newInstance(), null)
                            .commit();
                    break;
                case 5:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.menu_frag, UpdateFragment.newInstance(), null)
                            .commit();
                    break;
                case 6:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.menu_frag, ScanHistoryFragment.newInstance(), null)
                            .commit();
                    break;
                */
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Intent intent = new Intent(MultiPurpActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
                return true;
    }
}