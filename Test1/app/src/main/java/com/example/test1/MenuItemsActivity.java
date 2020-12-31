package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class MenuItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int val = getIntent().getIntExtra("val", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);


        Toolbar toolbar = findViewById(R.id.mi_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Per mettere il back button


        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
            switch (val) {
                case 1:
                    //Nel caso l'activity venga aperta con "val = 1", cioè selezionando la voce guida dal sottomenu
                    //help, viene inizializzato il fragment della guida
                    getSupportActionBar().setTitle("Guida");
                    fragmentTransaction.replace(R.id.menu_frag, GuideFragment.newInstance()).commit();
                    //.add(R.id.menu_frag, GuideFragment.newInstance(), null)
                    break;
                case 2:
                    //val = 2 -> fragment delle FAQ
                    getSupportActionBar().setTitle("FAQ");
                    fragmentTransaction.replace(R.id.menu_frag, FAQFragment.newInstance()).commit();
                    //.add(R.id.menu_frag, FAQFragment.newInstance(), null)
                    break;
                case 3:
                    //val = 3 -> fragment dei contatti
                    getSupportActionBar().setTitle("Contatti");
                    fragmentTransaction.replace(R.id.menu_frag, ContactsFragment.newInstance()).commit();
                    //.add(R.id.menu_frag, ContactsFragment.newInstance(), null)
                    break;
                case 4:
                    //val = 4 -> fragment dei credits
                    getSupportActionBar().setTitle("Credits");
                    fragmentTransaction.replace(R.id.menu_frag, CreditsFragment.newInstance()).commit();
                    //.add(R.id.menu_frag, CredistsFragment.newInstance(), null)
                    break;
//Questi fragment devono ancora essere creati e implementati
                case 5:
                    //val = 5 -> fragment di aggiornamento
                    getSupportActionBar().setTitle("Aggiornamento");
                    //fragmentTransaction.replace(R.id.menu_frag, UpdateFragment.newInstance()).commit();
                    //.add(R.id.menu_frag, UpdateFragment.newInstance(), null)
                    break;
                case 6:
                    //val = 6 -> fragment dei scansione automatica
                    getSupportActionBar().setTitle("Attivazione scansione automatica");
                    //fragmentTransaction.replace(R.id.menu_frag, ScanHistoryFragment.newInstance()).commit();
                    //.add(R.id.menu_frag, ScanHistoryFragment.newInstance(), null)
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MenuItemsActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}