package com.example.test1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//inizialize variabile button for request permission
private Button btrequest,btcheck;
static final int REQUEST_CODE = 123;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// asign button request
		btrequest = findViewById(R.id.bt_request);
		btcheck = findViewById(R.id.bt_check);


		btrequest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					// when permission are not granted
					if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setTitle("Grant those permission");
						builder.setMessage("read location");
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i) {
								ActivityCompat.requestPermissions(MainActivity.this,
										new String[]{
												Manifest.permission.ACCESS_FINE_LOCATION
										},
										REQUEST_CODE);

							}
						});
						builder.setNegativeButton("cancel", null);
						AlertDialog alertDialog = builder.create();
						alertDialog.show();
					}
					else{
						ActivityCompat.requestPermissions(MainActivity.this,
							new String[]{
									Manifest.permission.ACCESS_FINE_LOCATION
							},
							REQUEST_CODE);

					}
				}
				else{
					// when permission are already granted
					Toast.makeText(getApplicationContext(), "permission already granted", Toast.LENGTH_SHORT).show();

				}
			}
		});

		btcheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.fromParts("package", getPackageName(),null);
				intent.setData(uri);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == REQUEST_CODE){
			if( grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
				//permission are granted
				Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
			}
			else{
				// permission are denied
				Toast.makeText(getApplicationContext(),"Permission are denied",Toast.LENGTH_SHORT).show();
			}
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////fine permessi

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
