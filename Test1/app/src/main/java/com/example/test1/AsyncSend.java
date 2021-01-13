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
    private final Context context;
    private OnTaskCompleted listener;

    public AsyncSend(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... voids) {
        System.out.println("dentro if");

            System.out.println("dentro if");
            final String id = voids[0];
            final String bMac = voids[1];
            final String loc = voids[2];
            final String bluefound = String.valueOf(voids[3]);
            System.out.println(id+bMac+loc+bluefound);
            //NON SO PERCHE MA I MARKER VANO CON QUESTO:
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //

            //String data = "id="+getId()+"&bmac="+getMac()+"&loc="+loc+"&blueFound="+bluefound+"&timeStamp=1";
            String data = "id=" + id + "&bmac=" + bMac + "&loc=" + loc + "&blueFound=" + bluefound + "&timeStamp=1"; //ricordare timestamp e`su server messo non qui

            String text = "";

            BufferedReader reader = null;

            // Send data
            Log.e("location", "this is your location" + loc);

            URL url = null;
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

                Log.e("loc", "Sent Loc e blue"); //vedi cosa invia

                // Get the server response
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

            } catch (Exception ex) {

                ex.printStackTrace();

            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                conn.disconnect();

            }

        return text;
    }


    @Override
    protected void onPostExecute(String result) {
        System.out.println("Onpost\n");
        listener.onTaskCompleted(result);
    }






}

