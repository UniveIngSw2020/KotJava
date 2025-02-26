package com.example.test1;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/*

Maps activity class: classe principale che viene eseguita sempre per prima tranner al primo avvio dell'applicazione in cui
 viene eseguita per seconda dopo la first access Activity

Funzionamento metodi principali:

onCreate (r. 105): vengono iniziallizate le varie variabili e i bottoni prensenti nella classe

onMapReady (r. 481): gestisce metodi e variabili che servono per la gestione della mappa nel momento in qui la mappa diventa "ready"

onOptionsItemSelected (r. 618): Gestione del click sulle varie voci del menu



 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    //variabili globali della classe
    private GoogleMap map;

    //private SearchView searchView;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private Location location ; // Location
    double latitude; // Latitude
    double longitude; // Longitude
    public AlertDialog gpslost;


    ClusterManager<MyItem> clusterManager;
    public BluetoothReceiver mReceiver = new BluetoothReceiver();
    SimpleCursorAdapter mAdapter;
    Handler handler = new Handler();

    //List<ReciveItem> listItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //button favourite and recent
        final SharedPreferences sharedPreferences = getSharedPreferences("recentloc",MODE_PRIVATE);
        final SharedPreferences sharedPreferencesfav = getSharedPreferences("favloc",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final SharedPreferences.Editor editorfav = sharedPreferencesfav.edit();
        final Gson gson =new Gson();
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );


/////////////////////////////////
        // Broadcastreciever per il cambio di stato della connessione
        Networkreciever nw = new Networkreciever();
        registerReceiver(nw, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));



        // controllo gps primo accesso
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps(); // metodo che chiede all' utente di attivare gps
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Toolbar superiore con l'overflow menu
        Toolbar myToolbar = findViewById(R.id.maps_toolbar);
        setSupportActionBar(myToolbar);
        if( getSupportActionBar() != null ){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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
                Intent intent = new Intent( MapsActivity.this, StatsActivity.class );
                //intent.putExtra("vaal", listaID );// 2 = fragment delle statistiche
                //intent.putExtra("vaal", listaNumeriBluetooth );
                startActivity(intent);

            }
        });

        //bottone recenti
        ImageButton bhist = findViewById(R.id.imageButtonHistory);
        bhist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
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

        //
        // Implementazione searchbar ..........................................................................................................................................
        //
        final SearchView searchView = findViewById(R.id.srclocation);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                MapsActivity.this.map = googleMap;

                // imposta l' ultima posizone rilevata all' avvio del gps
                if(FirstAccessActivity.checkPermission(getApplicationContext())) {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( location.getLatitude(),location.getLongitude()), 15));
                }

                // inserisco in memoria i preferiti (long click)
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


        // listener sull' elemento suggerito
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

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {

                return true;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        //
        // ricerca del posto scritto in input
        //
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String location) {

                List<Address> addressList = null;

                // trasformo stringa in una location tramite geocoder + cambio posizione camera
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
            //  viene riscritta la scritta in input (cosa fare)
            @Override
            public boolean onQueryTextChange(String location) {

                List<Address> addressList = null;
                if (location != null && !location.equals("")) {
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



    @Override
    public void onMapReady(GoogleMap map) {
        // creazione alertdialog per perdita gps....................................................
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        final AlertDialog.Builder alertgps = new AlertDialog.Builder(MapsActivity.this);
        alertgps.setTitle("Wait gps connection...");
        alertgps.setCancelable(false);
        alertgps.setPositiveButton("Close app", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //interrompe l' app
                System.exit(0);
            }
        });
        gpslost = alertgps.create();

        // necessarie per mylocationbutton
            if (FirstAccessActivity.checkPermission(getApplicationContext())) {
                System.out.println("entrato");

                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
                map.setOnMyLocationClickListener(this);
            }


        // listener chje controlla se il gps è attivo
        if (FirstAccessActivity.checkPermission(getApplicationContext())) {
            manager.addGpsStatusListener(new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    switch(event){
                        case GpsStatus.GPS_EVENT_STARTED:
                            // quando il servizio gps parte, si avvia il broadcastreciever con la funzione per aggiornare i dati della mappa in base a quello che c è sul server
                            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                            registerReceiver(mReceiver, filter);

                            handler.postDelayed(runnableCode, 5000);

                            gpslost.cancel();
                            break;
                        case GpsStatus.GPS_EVENT_STOPPED:
                            // se il gps non va più, -> intent
                            Toast.makeText(MapsActivity.this, " Gps has STOPPED", Toast.LENGTH_LONG);
                            if(( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )) {
                                // gps stato disattivato dall'utente
                                buildAlertMessageNoGps();
                            }
                            else {
                                // gps interrotto
                                if (!isFinishing()) {
                                    gpslost = alertgps.show();
                                }
                                break;
                            }
                    }
                }
            });
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // quando viene premuto lo schermo sulla propria posizione viene visualizzato indirizzo linea della posizione
        Geocoder g = new Geocoder(MapsActivity.this);
        List<Address> s;
        String addr = "";
            try {
                 s = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                 addr = s.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        Toast.makeText(this, "Current location:\n" + addr , Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onMyLocationButtonClick() {
        // bottone che mi riporta alla location

        Geocoder g = new Geocoder(MapsActivity.this);
        List<Address> s;
        String addr = "";
        try {
            s = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            addr = s.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Current location:\n"  , Toast.LENGTH_SHORT)
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

    //permette di rimandare alla pagine del playstore
    void GoToURL(String url){
        Uri uri = Uri.parse(url);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
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
                return true;
            case R.id.faq:
                intent.putExtra("val", 2 );// 2 = fragment delle FAQ
                startActivity(intent);

                return true;
            case R.id.contatti:
                //Rimandare alla pagina dei contatti/mostrare popup dei contatti
                intent.putExtra("val", 3 );// 3 = fragment dei contatti
                startActivity(intent);
                return true;
            case R.id.credits:
                //Rimandare alla pagina dei credits/mostrare popup dei credits
                intent.putExtra("val", 4 );// 4 = fragment dei credits
                startActivity(intent);
                return true;
            case R.id.aggiornamento:
                //Rimandare alla pagina di aggiornamento
                intent.putExtra("val", 5 );// 5 = fragment di aggiornamento
                startActivity(intent);
                return true;
            case R.id.condividi:
                showMsg("Condivisione app..");
                //Copiare il link per la condivisione
                return true;
            case R.id.valuta:
                //Dialog per rimandare al playstore per la valutazione dell'app
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                GoToURL("https://play.google.com/store/apps/details?id=countinyou");
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage("VORRESTI AIUTARCI ? VALUTA LA NOSTRA APP").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                //Aprire la pagina del playStore(?)
                return true;
            case R.id.autoscan:
                //abilita autoscan del bluetooth e l'invio della location
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

    //prende info id e mac  del dispositivo
    String getId() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    String getMac() {
        BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return m_BluetoothAdapter.getAddress();
    }


/////////////////////////////////////////////////



    //Runnable che permette di eseguire la scansione del bluetooth, ricezione e invio dei dati dal server
    public  Runnable runnableCode = (new Runnable() { //manca start join ecc. bo
        @Override
        public   void  run() {

            if(FirstAccessActivity.checkPermission(getApplicationContext())) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            SharedPreferences share = getSharedPreferences("autoscan", MODE_PRIVATE);
            boolean autoScan = share.getBoolean("autoscan", false);


            if (location != null) {
                AsyncReceive asyncReceive = new AsyncReceive(MapsActivity.this, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(List<ReceiveItem> fromserver) {
                        ArrayList<ReceiveItem> array;

                        if (fromserver != null){
                            array = new ArrayList<>(fromserver);
                            if (clusterManager != null){
                                clusterManager.clearItems();
                                clusterManager.cluster();
                            }
                            for (ReceiveItem s : array) {
                                final  ReceiveItem r = s;
                                // add marker
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        MyItem item = new MyItem(r.getLat(), r.getLng(), String.format(Integer.toString(r.getDevices())), r.getName());
                                        addItems(item);

                                        Log.i("markerPosition", String.valueOf(r.getLat()) + ":" +  String.valueOf(r.getLng()) );
                                    }
                                });
                            }
                            setUpClusterer();
                        }
                    }
                });
                asyncReceive.execute();


                if (autoScan){
                    AsyncBluetooth asyncBluetooth = new AsyncBluetooth(MapsActivity.this);
                    asyncBluetooth.execute();

                    final AsyncSend asyncsend = new AsyncSend(MapsActivity.this);

                    List<BluetoothDevice>  b = mReceiver.getDevices();
                    int bluefound = b.size();
                    String  s = "";
                    String bmac = getMac();


                    for (int z = 0; z < bluefound; z++) {
                        if (b.get(z) != null){
                            s =  s +"," +   String.valueOf(b.get(z).getName());
                        }
                    }
                    for (int z = 0; z < bluefound; z++) {
                        if (b.get(z) != null){
                            bmac =  bmac +":" + String.valueOf(b.get(z).getAddress());
                        }
                    }
                    asyncsend.execute(inputSend(getId(), bmac, String.format(location.getLatitude() + ":" + location.getLongitude()), bluefound));

                    if (s != "")
                        Toast.makeText(MapsActivity.this, s, Toast.LENGTH_SHORT).show(); //toast che mosta bluetooth scannerizati

                    mReceiver.resetDevices();
                }

            }

            putlocrecent(location);
            Log.i("hadler", "hadler");
            handler.postDelayed(this, 30000);
        }

    });
    //restituisce input per la asyncSend
    public String[] inputSend(String id, String bmac, String loc, int devices){
        String [] input = {id,bmac,loc,Integer.toString(devices)};
        return input;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
//////////////////////////////
    //
    // metodo che mette aggiunge la locazione recente all array
    //
    public void putlocrecent(Location loc){
        final SharedPreferences sharedPreferences = getSharedPreferences("recentloc",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final Gson gson =new Gson();
        if (loc != null) {
            // get Array shared preferences
            String json = sharedPreferences.getString("recentloc", "");
            Type type = new TypeToken<List<String>>() {
            }.getType();
            ArrayList<String> arrayListm = gson.fromJson(json, type);
            String thisloc = String.format(loc.getLatitude() + ":" + loc.getLongitude());
            if (arrayListm == null) {
                arrayListm = new ArrayList<>();
                arrayListm.add(thisloc);
                json = gson.toJson(arrayListm);
                editor.putString("recentloc", json);
                editor.apply();
                Toast.makeText(MapsActivity.this, "Recent location added", Toast.LENGTH_SHORT).show();
            } else {
                if (!arrayListm.contains(thisloc)) {
                    arrayListm.add(thisloc);
                    json = gson.toJson(arrayListm);
                    editor.putString("recentloc", json);
                    editor.apply();
                    Toast.makeText(MapsActivity.this, "Recent location added", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
    //metodo che ritorna una lista di AddressLine da lista di location
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

    //metodo per cancellare tutti i campi in contemporaena salvati in locale (preferiti o recenti)
    public void deleteAll(String key){
        final SharedPreferences sharedPreferences = getSharedPreferences(key,MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    //metodo che notifica quando il gps e`spento e chiedi di attivarlo
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //metodo che aggiorna il cluster clusterManager
    private void setUpClusterer() {


        clusterManager = new ClusterManager<MyItem>(MapsActivity.this, map);

        MarkerClusterRender render = new MarkerClusterRender(MapsActivity.this, map, clusterManager);
        clusterManager.setRenderer(render); //impostazione del render custom

        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);

    }
    //aggiunge item a clusterManger
    private void addItems(MyItem item) {
            clusterManager.addItem(item);
            clusterManager.cluster();
    }



}




