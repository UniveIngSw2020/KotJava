package com.example.test1;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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


/*v0:
//aggiunta metodo per fare send e get (get viene fatta come lettura dopo risposta del send per ora poi vediamo)
//send messa sull click per la posizione per ora
v1:
nuovo metodo solo per fare il get senza send creato
v2:
messo thread e controllo per permessi prima di fare invio location (location da a (google in california?)

<uses-permission android:name="android.permission.READ_PHONE_STATE" />  per https://stackoverflow.com/questions/29753117/waitinginmainsignalcatcherloop-error-in-android-application

 */


/*
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap map;
    private SearchView searchView;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private Location location = null; // Location
    double latitude; // Latitude
    double longitude; // Longitude



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Toolbar superiore con l'overflow menu
        Toolbar myToolbar1 = findViewById(R.id.toolbar);
        setActionBar(myToolbar1);
        getActionBar().setDisplayShowTitleEnabled(false);

        ImageButton bfav = findViewById(R.id.imageButtonFavourites);
        bfav.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //cose
            }
        });

        ImageButton bstats = findViewById(R.id.imageButtonStats);
        bstats.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //cose
            }
        });

        ImageButton bloc = findViewById(R.id.imageButtonLocation);
        bloc.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //cose
            }
        });

        ImageButton bhist = findViewById(R.id.imageButtonHistory);
        bhist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //cose
            }
        });


        final SearchView searchView = findViewById(R.id.srclocation); //da cambiare con nome del search]

        //Button scan = findViewById(R.id.bscan); //per lo scan per ora no

        //searchView = findViewById(R.id.srclocation);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                MapsActivity.this.map = googleMap;

                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                if(FirstAccessActivity.checkPermission(getApplicationContext())) {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( location.getLatitude(),location.getLongitude()), 20));

                }



            }

        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String location) {

                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);



                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        Toast.makeText(MapsActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        mapFragment.getMapAsync(this);
        displayDiscovry(); //scan BLUETOOTH
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

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //SendLoc(String.format(String.valueOf(location)));
        controllPermissionAndSend(latitude + ":" + longitude);
        Log.e("loc","ok=");
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
        return super.onCreateOptionsMenu(menu);
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
            case R.id.scansioni:
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

/////////////////////////////////////////////////

    void controllPermissionAndSend(String loc) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ArrayList<String> arrayList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding

            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            arrayList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            arrayList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            Toast.makeText(this, "location fine", Toast.LENGTH_SHORT).show();
        }else {
            Log.e("permessi","ok");
            SendLoc(loc);
        }
        if (!arrayList.isEmpty()) {
            String[] permi = new String[arrayList.size()];
            permi = arrayList.toArray(permi);
            ActivityCompat.requestPermissions(this, permi, 0);
        }
    }




    //

    public  void  SendLoc(String loc){ //need location



        String data = "id="+getId()+"&bmac="+getMac()+"&loc="+loc+"&blueFound=0&timeStamp=1";


        String text = "";
        BufferedReader reader=null;

        // Send data
        Log.e("location","this is your location"+loc);
        try
        {

            // Defined URL  where to send data
            URL url = new URL("http://192.168.0.104/posti.php");

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

            Log.e("loc","ok2 Sent"); //vedi cosa invia
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
            getParsing(text);
        }

    }

    void getParsing(String k){
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

    //get separato da send
    void getLastData(String k){

        String text = "";
        BufferedReader reader=null;

        try {
            // Defined URL  where to send data
            URL url = new URL("http://192.168.1.4/posti.php");

            URLConnection conn = url.openConnection();

            // get from server and add markers
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
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
            getParsing(text);
        }




    }

    public void  displayDiscovry(){

        final ArrayList<String> list = new ArrayList<>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MapsActivity.this,android.R.layout.simple_list_item_1,list);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        LinearLayout linearLayout = new LinearLayout(MapsActivity.this);

        ListView listView = new ListView(MapsActivity.this);
        linearLayout.addView(listView);

        listView.setAdapter(arrayAdapter);

        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast t = new Toast(this);
            t.setText("Sorry your phone do not support Bluetooth");
            t.show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,1);}
            //   bluetoothDev=new BluetoothDev();

            //added
            // Create a BroadcastReceiver for ACTION_FOUND
            final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        // Add the name and address to an array adapter to show in a ListView

                        Log.e("list",device.getAddress());
                        list.add(device.getName());
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            };
// Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
            //end added

            bluetoothAdapter.startDiscovery();

            alertDialog.setTitle("Bluetooth Scan");
            alertDialog.setView(linearLayout);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    unregisterReceiver(mReceiver);
                    bluetoothAdapter.cancelDiscovery();
                    dialog.dismiss();
                }
            });
            alertDialog.show();

        }
    }

}