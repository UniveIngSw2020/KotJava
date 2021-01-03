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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.BaseColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/*

public  void  SendLoc(String loc)
prende una stringa di longitine + lat ed invia al server, la risposta dal server e` quello che vediamo per aggiornare

private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
gestiste la scansione fatta da scandiscovery
se ce un dispositivo va su OnReceive()

private Runnable runnableCode = new Runnable() {
questo gestisce cio che viene fatto ogni tot sencondi, viene chimato da dentro onCrreate poi si chiama ricorsivamente

nella OnDestroy Distrugge il broadCast che tiene le connesioni con i bluetooth
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
    int bluefound;
    SimpleCursorAdapter mAdapter;

    boolean autoScan;

    Handler handler = new Handler();

    final BluetoothAdapter bluetoothAdapterr = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //button favourite and recent
        final SharedPreferences sharedPreferences = getSharedPreferences("recentloc",MODE_PRIVATE);
        final SharedPreferences sharedPreferencesfav = getSharedPreferences("favloc",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final SharedPreferences.Editor editorfav = sharedPreferencesfav.edit();
        final Gson gson =new Gson();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Toolbar superiore con l'overflow menu
        Toolbar myToolbar = findViewById(R.id.maps_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Dovrebbe forzare la presenza dell'overflow menu anche su dispositivi con il tasto dedicato
        try{
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if( menuKeyField != null ){
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            //Log.d(TAG, e.getLocalizedMessage());
        }

        handler.postDelayed(runnableCode ,5000);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

//Bottoni della toolbar inferiore
        ImageButton bfav = findViewById(R.id.imageButtonFavourites);
        bfav.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String json = sharedPreferencesfav.getString("favloc", "");
                Type type = new TypeToken<List<String>>() {
                }.getType();
                final ArrayList<String> arrayListm = gson.fromJson(json, type);
                final List<String> namelocs = getNameOfLocation(arrayListm);


                final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                final ListView listView = new ListView(alert.getContext());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(arrayListm.get(i).split(":")[0]), Double.parseDouble(arrayListm.get(i).split(":")[1]))));
                    }
                });
                //se il record viene tenuto premuto si può eliminare
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        final AlertDialog.Builder alertcancel =new AlertDialog.Builder(MapsActivity.this);

                        alertcancel.setTitle("Delete this position?");
                        alertcancel.setCancelable(true);
                        alertcancel.setPositiveButton("delete this position", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                arrayListm.remove(position);
                                namelocs.remove(position);
                                editorfav.remove("favloc");
                                String json = gson.toJson(arrayListm);
                                editorfav.putString("favloc", json);
                                editorfav.apply();
                                listView.setAdapter(new ArrayAdapter<>(alert.getContext(), android.R.layout.simple_list_item_1, namelocs));
                                Toast.makeText(MapsActivity.this, "Location deleted", Toast.LENGTH_SHORT).show();
                            }


                        });
                        alertcancel.setNegativeButton("Delete all", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAll("favloc");
                                namelocs.clear();
                                arrayListm.clear();
                                listView.setAdapter(new ArrayAdapter<>(alert.getContext(),android.R.layout.simple_list_item_1,namelocs));

                            }
                        });
                        alertcancel.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertcancel.show();
                        return true;
                    }
                });

                listView.setAdapter(new ArrayAdapter<>(alert.getContext(), android.R.layout.simple_list_item_1, namelocs));
                alert.setTitle("Favourite places");
                alert.setCancelable(true);
                alert.setView(listView);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                alert.show();

            }
        });



        ImageButton bstats = findViewById(R.id.imageButtonStats);
        bstats.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent( MapsActivity.this, ButtonsActivity.class );
                intent.putExtra("val", 2 );// 2 = fragment delle statistiche
                startActivity(intent);
                finish();
            }
        });

        ImageButton bhist = findViewById(R.id.imageButtonHistory);
        bhist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //cose
                String json = sharedPreferences.getString("recentloc","");
                Type type =  new TypeToken<List<String>>(){
                }.getType();
                final ArrayList<String> arrayListm = gson.fromJson(json,type);
                final List<String> namelocs = getNameOfLocation(arrayListm);


                final AlertDialog.Builder alert =new AlertDialog.Builder(MapsActivity.this);
                final ListView listView = new ListView(alert.getContext());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng( Double.parseDouble(arrayListm.get(i).split(":")[0]) ,Double.parseDouble(arrayListm.get(i).split(":")[1]))));
                    }



                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                        final AlertDialog.Builder alertcancel =new AlertDialog.Builder(MapsActivity.this);

                        alertcancel.setTitle("Delete this position?");
                        alertcancel.setCancelable(true);
                        alertcancel.setPositiveButton("delete this position", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayListm.remove(position);
                                namelocs.remove(position);
                                editor.remove("recentloc");
                                String json = gson.toJson(arrayListm);
                                editor.putString("recentloc", json);
                                editor.apply();
                                listView.setAdapter(new ArrayAdapter<>(alert.getContext(),android.R.layout.simple_list_item_1,namelocs));
                                Toast.makeText(MapsActivity.this, "Location deleted", Toast.LENGTH_SHORT).show();
                            }


                        });
                        alertcancel.setNegativeButton("Delete all", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAll("recentloc");
                               namelocs.clear();
                               arrayListm.clear();
                                listView.setAdapter(new ArrayAdapter<>(alert.getContext(),android.R.layout.simple_list_item_1,namelocs));

                            }
                        });
                        alertcancel.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertcancel.show();
                        return true;
                    }
                });


                listView.setAdapter(new ArrayAdapter<>(alert.getContext(),android.R.layout.simple_list_item_1,namelocs));
                alert.setTitle("Recent location visited");
                alert.setCancelable(true);
                alert.setView(listView);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                alert.show();


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

                // inserisco in memoria i preferiti
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        String json = sharedPreferencesfav.getString("favloc", "");
                        Type type = new TypeToken<List<String>>() {
                        }.getType();
                        ArrayList<String> arrayList = gson.fromJson(json, type);
                        if(arrayList == null)
                            arrayList = new ArrayList<>();

                        arrayList.add(latLng.latitude +":"+latLng.longitude);
                        json = gson.toJson(arrayList);
                        editorfav.putString("favloc",json);
                        editorfav.apply();

                        Toast.makeText(MapsActivity.this, "Favourite location added", Toast.LENGTH_SHORT).show();

                    }
                });

            }



        });
    
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
    
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(false);
    
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
            
                List<Address> addressList = null;
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                String location = cursor.getString(cursor.getColumnIndex("cityName"));
            
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                    
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    
                    
                    
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        Toast.makeText(MapsActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


//                searchView.setQuery(txt, true);
            
            
                return true;
            }
        
            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
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
                        addressList = geocoder.getFromLocationName(location, 5);
                    
                    
                    
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        Toast.makeText(MapsActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            
                return false;
            }
            int m = 0;
            @Override
            public boolean onQueryTextChange(String location) {
            
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
                        for (int i=0; i<addressList.size(); i++) {
                        
                            c.addRow(new Object[]{i, addressList.get(i).getAddressLine(0)});
                            m++;
                            //mAdapter.changeCursor(c);
                        
                            Log.e("list",addressList.get(i).getAddressLine(0));
                        }
                    
                        mAdapter.changeCursor(c);
                    
                    
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        Toast.makeText(MapsActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
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

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //SendLoc(String.format(String.valueOf(location)));
        controllPermissionAndSend(latitude + ":" + longitude);
        Log.e("loc","ok=");

        // aggiungo a recenti la posizione
        putlocrecent(location);
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
    //Creazione condividi app
    private void showMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Condividi la nostra app");
        sendIntent.setType("text/plain");
        
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    //Gestione del click sulle varie voci del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent( MapsActivity.this, MenuItemsActivity.class );
        switch (item.getItemId()) {
            case R.id.help:
                //Apre sottomenu di help
                return true;
            case R.id.guida:
                //Rimandare alla pagina con la guida/mostrare popup della guida
                intent.putExtra("val", 1 );// 1 = fragment della guida
                startActivity(intent);
                finish();
                return true;
            case R.id.faq:
                intent.putExtra("val", 2 );// 2 = fragment delle FAQ
                startActivity(intent);
                finish();
                return true;
            case R.id.contatti:
                //Rimandare alla pagina dei contatti/mostrare popup dei contatti
                intent.putExtra("val", 3 );// 3 = fragment dei contatti
                startActivity(intent);
                finish();
                return true;
            case R.id.credits:
                //Rimandare alla pagina dei credits/mostrare popup dei credits
                intent.putExtra("val", 4 );// 4 = fragment dei credits
                startActivity(intent);
                finish();
                return true;
            case R.id.aggiornamento:
                //Rimandare alla pagina di aggiornamento
                intent.putExtra("val", 5 );// 5 = fragment di aggiornamento
                startActivity(intent);
                finish();
                return true;
            case R.id.condividi:
                showMsg("Condivisione app..");
                //Copiare il link per la condivisione
                return true;
            case R.id.valuta:
                //Aprire la pagina del playStore(?)
                return true;
            case R.id.scansioni:
                
                return true;
            case R.id.autoscan:
                final SharedPreferences sharedautoscan = getSharedPreferences("autoscan",MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedautoscan.edit();
                Switch sw = new Switch(MapsActivity.this);
                boolean checked;
                sw.setTextOn("on");
                sw.setTextOff("off");
                sw.setGravity(Gravity.CENTER);

                final AlertDialog.Builder myDialog = new AlertDialog.Builder(MapsActivity.this);
                myDialog.setTitle("Auto Scan");
                myDialog.setMessage("TURN ON the autoscan to find other devices automatically ");
                myDialog.setView(sw);
                sw.setChecked(sharedautoscan.getBoolean("autoscan",false));
                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            //scanBlue();

                            editor.putBoolean("autoscan", true);
                            editor.apply();
                            Toast.makeText(myDialog.getContext(), "Autoscan mode ON", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            editor.putBoolean("autoscan", false);
                            editor.apply();
                            Toast.makeText(myDialog.getContext(), "Autoscan mode OFF", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                myDialog.show();


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


        String data = "id="+getId()+"&bmac="+getMac()+"&loc="+loc+"&blueFound="+bluefound+"&timeStamp=1";


        String text = "";
        BufferedReader reader=null;

        // Send data
        Log.e("location","this is your location"+loc);
        try
        {

            // Defined URL  where to send data
            URL url = new URL("https://circumflex-hub.000webhostapp.com/posti.php");

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





    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        final ArrayList<String> list = new ArrayList<>();
        public void onReceive(Context context, Intent intent) {
            Log.e("quanti blue","ok");

            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                Log.e("list",device.getAddress());
                list.add(device.getName());
                bluefound = list.size();
                //arrayAdapter.notifyDataSetChanged();

                Toast.makeText(MapsActivity.this, "trovato almeno un dispositivo", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MapsActivity.this, "quanti trovati" +String.format(String.valueOf(bluefound)), Toast.LENGTH_SHORT).show();

                bluetoothAdapterr.cancelDiscovery();
            }
        }
    };

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            SharedPreferences share = getSharedPreferences("autoscan",MODE_PRIVATE);
            autoScan = share.getBoolean("autoscan", false);
            System.out.println("entra in 1 thread");


            if (autoScan) {

                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //thread che serve solo per fare la Thread.sleep(1000);

                        //non funzianava con bluescan perche non dava tempo di fare lo startDiscovery
                        try {
                            System.out.println("entra in 2 thread");
                            bluetoothAdapterr.startDiscovery();
                            Toast.makeText(MapsActivity.this, "scanning", Toast.LENGTH_SHORT).show();

                            Thread.sleep(1000);
                            bluetoothAdapterr.cancelDiscovery(); //serve il thread per fare la cancelDiscovery()
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });

                closeActivity.start();
            }

            SendLoc(String.format(location.getLatitude() + ":" +location.getLongitude()));
            ////
            // SERVE GET SENZA SEND QUI
            /////
            handler.postDelayed(this, 5000);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }
//////////////////////////////

    public void putlocrecent(Location loc){
        final SharedPreferences sharedPreferences = getSharedPreferences("recentloc",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final ArrayList<String> arrayListrecent = new ArrayList<>();
        final Gson gson =new Gson();

        // get Array shared preferences
        String json = sharedPreferences.getString("recentloc","");
        Type type =  new TypeToken<List<String>>() {
        }.getType();
        ArrayList<String> arrayListm = gson.fromJson(json,type);
        String thisloc = String.format(loc.getLatitude() + ":" + loc.getLongitude());
        if(arrayListm == null) {
            arrayListm = new ArrayList<>();
            arrayListm.add(thisloc);
            json = gson.toJson(arrayListm);
            editor.putString("recentloc", json);
            editor.apply();
            Toast.makeText(MapsActivity.this, "Recent location added", Toast.LENGTH_SHORT).show();
        }
        else{
            if (!arrayListm.contains(thisloc)) {
                arrayListm.add(thisloc);
                json = gson.toJson(arrayListm);
                editor.putString("recentloc", json);
                editor.apply();
                Toast.makeText(MapsActivity.this, "Recent location added", Toast.LENGTH_SHORT).show();

            }
        }
    }
    public List<String> getNameOfLocation(List<String> locations){
        Geocoder g = new Geocoder(MapsActivity.this);
        ArrayList<String> namelocs = new ArrayList<>();
        if (locations != null){
            for (String s : locations) {

                try {
                    List<Address> names = g.getFromLocation(Double.parseDouble(s.split(":")[0]), Double.parseDouble(s.split(":")[1]), 1);
                    namelocs.add(names.get(0).getAddressLine(0));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        return namelocs;
    }

    //metodo per cancellare tutti i campi in contemporaena
    public void deleteAll(String key){
        final SharedPreferences sharedPreferences = getSharedPreferences(key,MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


}