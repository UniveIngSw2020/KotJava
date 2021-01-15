package com.example.test1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncSend extends AsyncTask<String,Void,String> {
    private static final int INPUT_DIM = 4;
    private final Context context;


    public AsyncSend(Context context) {
        this.context = context;

    }

    @Override
    protected String doInBackground(String... voids) {

        if(voids.length == INPUT_DIM) {
            System.out.println("dentro if");
            final String id = voids[0];
            final String bMac = voids[1];
            final String loc = voids[2];
            final String bluefoundd = String.valueOf(voids[3]);
            Log.e("quanti blue","ok -- -- - - --- - - - - - - - -- - - " + bluefoundd + " ------------");

            System.out.println(id + bMac + loc + bluefoundd);
            //NON SO PERCHE MA I MARKER VANO CON QUESTO:
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            //String data = "id="+getId()+"&bmac="+getMac()+"&loc="+loc+"&blueFound="+bluefound+"&timeStamp=1";
            String data = "id=" + id + "&bmac=" + bMac + "&loc=" + loc + "&blueFound=" + bluefoundd + "&timeStamp=1"; //ricordare timestamp e`su server messo non qui

            String text = "";

            BufferedReader reader = null;

            // Send data
            //Log.e("location", "this is your location" + loc);

            Log.e("location", "ASYNSEND LOG " + loc);

            URL url ;
            HttpURLConnection conn = null;
            try {

                // Defined URL  where to send data
                url = new URL("https://circumflex-hub.000webhostapp.com/posti.php");

                // Send POST data request

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

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

            return "";// true se fa tutto giusto
        }
        return ""; // else se errore
    }


    @Override
    protected void onPostExecute(String result) {

        Log.i("SEND", "send is finished");
    }






}

