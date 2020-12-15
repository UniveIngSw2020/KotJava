package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

//Ho messo come estensione Appcompact invece che FragmentActivity perchè mi permette di fare cose e estende a sua volta fragment
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap map;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //Toolbar superiore con l'overflow menu
        Toolbar myToolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(myToolbar1);
        getActionBar().setDisplayShowTitleEnabled(false);


        searchView = findViewById(R.id.srclocation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                System.out.println(location);
                if(location != null  && !location.equals("")){
                    System.out.println("dentro if");
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try{

                        addressList = geocoder.getFromLocationName(location, 1);
                        System.out.println(addressList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    System.out.println(address);
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                     */
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);

        //Bottomview per la navigazione
//(!)Forse è getSupportFragmentManager il problema perchè non è inizializzato o qualcosa del genere
        BottomNavigationView navView = findViewById(R.id.bottomView);
        AppBarConfiguration appBarConf = new AppBarConfiguration.Builder( R.id.destination1, R.id.destination2, R.id.destination3, R.id.destination4 ).build();
        NavHostFragment nhf = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        //NavController navCont = nhf.getNavController();
        //NavController navCont = nhf.findNavController(this, R.id.nav_host_fragment );
        NavController navCont = NavHostFragment.findNavController(nhf);
        NavigationUI.setupActionBarWithNavController(this, navCont, appBarConf);
        NavigationUI.setupWithNavController( navView, navCont );
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
    public void onMapReady(GoogleMap map) {
        if (FirstAccessActivity.checkPermission(getApplicationContext())) {
            System.out.println("entrato");

            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();

        return false;
    }

    //Creazione del menu della maps activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    //Gestione del click sulle varie voci del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.help:
                //Aprire sottomenu di help (Ho messo di seguito le voci, forse è uguale)
                return true;
            case R.id.guida:
                /*
                Rimandare alla pagina con la guida/mostrare popup della guida
                Intent intent = new Intent(this, "guida".class);
                startActivity(intent);
                 */
                return true;
            case R.id.faq:
                /*
                Rimandare alla pagina con le F.A.Q.
                Intent intent = new Intent(this, "FAQ".class);
                startActivity(intent);
                 */
                return true;
            case R.id.contatti:
                /*
                Rimandare alla pagina dei contatti/mostrare popup dei contatti
                Intent intent = new Intent(this, "contatti".class);
                startActivity(intent);
                 */
                return true;
            case R.id.credits:
                /*
                Rimandare alla pagina dei credits/mostrare popup dei credits
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
}