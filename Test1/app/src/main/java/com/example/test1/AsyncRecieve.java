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
/*
classe che permette di ricevere dati  dal server in background
 */

public class AsyncRecieve extends AsyncTask<String,String,String> {
        Context context;
    private OnTaskCompleted listener;



public AsyncRecieve(Context context, OnTaskCompleted listener) { //costruttore classe
        this.context = context;
        this.listener = listener;
        }

@Override
protected String doInBackground(String... voids) {

    
    String text = ""; //stringa  che contera` i dati ricevuti dal server sottoforma di stringa

    BufferedReader reader=null; //contiene le stringhe dal server e si aggiorna in riga per riga

    // Send data
    Log.e("location","getting INFOs");

    URL url = null;
    HttpURLConnection conn = null;

    try
    {
        // definisce url del server
        url = new URL("https://circumflex-hub.000webhostapp.com/posti.php");
        conn = (HttpURLConnection) url.openConnection();


        // prede dal server

        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;


        // legge dal server
        while((line = reader.readLine()) != null)
        {
            // aggiunge alla stringa sb le line dal buffer
            sb.append(line + "\n");
        }

        //da sb a string
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
protected void onPostExecute(String s) {  //metodo che viene chiamato una volta completata la doinBackground
    
    List<ReciveItem> getFromServer = new ArrayList<>();
    if (s != null && !s.equals("")) {
        try {

            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject obj = jsonArray.getJSONObject(i);


                final Double lat = Double.parseDouble(obj.optString("loc").split(":")[0]);
                final Double lon = Double.parseDouble(obj.optString("loc").split(":")[1]);
                final Integer found = Integer.parseInt(obj.optString("blueFound").split("=")[0]);
                final String name = (obj.optString("id").split("=")[0]);
                ReciveItem reciveItem = new ReciveItem(lat, lon, found, name);
                getFromServer.add(reciveItem);





            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onTaskCompleted(getFromServer);
    }
    listener.onTaskCompleted(null);
    }

}
