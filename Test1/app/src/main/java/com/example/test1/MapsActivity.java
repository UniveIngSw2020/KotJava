package com.example.test1;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    //Creazione del menu della maps activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, menu);
        return true;
    }

    //Gestione del click sulle varie voci del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch(item.getItemId()) {
            case R.id.help:
                //Aprire sottomenu di help (Ho messo di seguito le voci, forse è uguale)
                return true;
            case R.id.guida:
                /*Rimandare alla pagina con la guida/mostrare popup della guida
                Intent intent = new Intent(this, "guida".class);
                startActivity(intent);
                */
                return true;
            case R.id.faq:
                /*Rimandare alla pagina con le F.A.Q.
                Intent intent = new Intent(this, "FAQ".class);
                startActivity(intent);
                */
                return true;
            case R.id.contatti:
                /*Rimandare alla pagina dei contatti/mostrare popup dei contatti
                Intent intent = new Intent(this, "contatti".class);
                startActivity(intent);
                */
                return true;
            case R.id.credits:
                /*Rimandare alla pagina dei credits/mostrare popup dei credits
                Intent intent = new Intent(this, "credits".class);
                startActivity(intent);
                */
                return true;
            case R.id.aggiornamento:
                /*Rimandare alla pagina di aggiornamento
                Intent intent = new Intent(this, "aggiornamento".class);
                startActivity(intent);
                */
                return true;
            case R.id.condividi:
                //Copiare il link per la condivisione
                return true;
            case R.id.valuta:
                //Something
                return true;
            case R.id.storico:
                /*Rimandare alla pagina dello storico scansioni/mostrare popup dello storico
                Intent intent = new Intent(this, "storico".class);
                startActivity(intent);
                */
                return true;
            case R.id.autoscan:
                //Attivare/disattivare autoscan
                return true;
            case R.id.nascosti:
                /*Rimandare alla pagina dei dispositivi nascosti/mostrare popup dei dispositivi
                Intent intent = new Intent(this, "nascosti".class);
                startActivity(intent);
                */
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}