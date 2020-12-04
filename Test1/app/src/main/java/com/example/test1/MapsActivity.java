package com.example.test1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;



//aggiunta metodo per fare send e get (get viene fatta come lettura dopo risposta del send per ora poi vediamo)
//send messa sull click per la posizione per ora

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap map;
    SearchView searchView;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Toolbar superiore con l'overflow menu
        Toolbar myToolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(myToolbar1);

/*Così come'è non funziona
        //Toolbarinferiore con il menu a icone/bottoni
        Toolbar myToolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setActionBar( myToolbar2 );
*/

        searchView = findViewById(R.id.srclocation);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

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
        //fa la send and il get per ora
        SendLoc(String.format(String.valueOf(location)));

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, menu);
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
                //Rimandare alla pagina con la guida/mostrare popup della guida
                //Intent intent = new Intent(this, "guida".class);
                //startActivity(intent);
                return true;
            case R.id.faq:
                //Rimandare alla pagina con le F.A.Q.
                //Intent intent = new Intent(this, "FAQ".class);
                //startActivity(intent);
                return true;
            case R.id.contatti:
                //Rimandare alla pagina dei contatti/mostrare popup dei contatti
                //Intent intent = new Intent(this, "contatti".class);
                //startActivity(intent);
                return true;
            case R.id.credits:
                //Rimandare alla pagina dei credits/mostrare popup dei credits
                //Intent intent = new Intent(this, "credits".class);
                //startActivity(intent);
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

    //get info of smartminchia da inviare
    String getId() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    String getMac() {

        BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return m_BluetoothAdapter.getAddress();
    }
    //send and from server php version:

    public  void  SendLoc(String loc){ //need location


        String data = "id="+getId()+"&bmac="+getMac()+"&loc="+loc+"&blueFound=0&timeStamp=1";


        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL("http://192.168.1.4/posti.php");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }

            //server response da usare per i marker nella get
            text = sb.toString();
        }
        catch(Exception ex)
        {

            ex.printStackTrace();

        }
        finally
        {
            try
            {
                reader.close();
            }

            catch(Exception ex) {ex.printStackTrace();}
            // fa la get
            get(text);
        }

    }

    void get(String k){
        // get from server and add markers

        try {
            JSONObject jsonObject = new JSONObject(k);

            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i=0;i<jsonArray.length();i++){
                final JSONObject obj = jsonArray.getJSONObject(i);

                // Log.e("json",obj.optString("id"));

                final Double lat= Double.parseDouble(obj.optString("loc").split(":")[0]);

                final Double lon= Double.parseDouble(obj.optString("loc").split(":")[1]);

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title(obj.optString("id")));

                        // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 10));

                    }
                });


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }





    }

}