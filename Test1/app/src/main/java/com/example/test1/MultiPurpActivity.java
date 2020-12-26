package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

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
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
            switch (val) {
                case 1:
                    //Nel caso l'activity venga aperta con "val = 1", cioè selezionando la voce guida dal sottomenu
                    //help, viene inizializzato il fragment della guida
                    fragmentTransaction.replace(R.id.menu_frag, GuideFragment.newInstance()).commit();
                                     //.add(R.id.menu_frag, GuideFragment.newInstance(), null)
                    break;
                case 2:
                    //val = 2 -> fragment delle FAQ
                    fragmentTransaction.replace(R.id.menu_frag, FAQFragment.newInstance()).commit();
                                     //.add(R.id.menu_frag, FAQFragment.newInstance(), null)
                    break;
                case 3:
                    //val = 3 -> fragment dei contatti
                    fragmentTransaction.replace(R.id.menu_frag, ContactsFragment.newInstance()).commit();
                                     //.add(R.id.menu_frag, ContactsFragment.newInstance(), null)
                    break;
                case 4:
                    //val = 4 -> fragment dei credits
                    fragmentTransaction.replace(R.id.menu_frag, CreditsFragment.newInstance()).commit();
                                     //.add(R.id.menu_frag, CredistsFragment.newInstance(), null)
                    break;
/*Questi fragment devono ancora essere creati e implementati
                case 5:
                    //val = 5 -> fragment di aggiornamento
                    fragmentTransaction.replace(R.id.menu_frag, UpdateFragment.newInstance()).commit();
                                     //.add(R.id.menu_frag, UpdateFragment.newInstance(), null)
                    break;
                case 6:
                    //val = 6 -> fragment dei dispositivi scansionati
                    fragmentTransaction.replace(R.id.menu_frag, ScanHistoryFragment.newInstance()).commit();
                                     //.add(R.id.menu_frag, ScanHistoryFragment.newInstance(), null)
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