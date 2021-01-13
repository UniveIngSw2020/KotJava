package com.example.test1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncRecieve extends AsyncTask<String,String,String> {
        Context context;
    private OnTaskCompleted listener;



public AsyncRecieve(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.listener = listener;
        }

@Override
protected String doInBackground(String... voids) {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
    //
    String text = "";

    BufferedReader reader=null;

    // Send data
    Log.e("location","getting INFOs");

    URL url = null;
    HttpURLConnection conn = null;

    try
    {
        // Defined URL  where to send data
        url = new URL("https://circumflex-hub.000webhostapp.com/posti.php");
        conn = (HttpURLConnection) url.openConnection();


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
        conn.disconnect();
    }


return text;
}
@Override
protected void onPostExecute(String s) {
    List<ReciveItem> getFromServer = new ArrayList<>();
    if (s != null && !s.equals("")) {
        try {

            JSONObject jsonObject = new JSONObject(s);

            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject obj = jsonArray.getJSONObject(i);

                // Log.e("json",obj.optString("id"));
                final Double lat = Double.parseDouble(obj.optString("loc").split(":")[0]);
                final Double lon = Double.parseDouble(obj.optString("loc").split(":")[1]);
                final Integer found = Integer.parseInt(obj.optString("blueFound").split("=")[0]);
                final String name = (obj.optString("id").split("=")[0]);
                ReciveItem reciveItem = new ReciveItem(lat, lon, found, name);
                getFromServer.add(reciveItem);


                // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 10));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onTaskCompleted(getFromServer);
    }
    listener.onTaskCompleted(null);
    }

}
