package com.example.test1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncSend extends AsyncTask<String,Void,String> {
    private static final int INPUT_DIM = 4;
    private final Context context;

    // costruttore
    public AsyncSend(Context context) {
        this.context = context;

    }
// invia : ID, BMAC , POSIZIONE  del device dove è eseguita l' app e il NUMERO DI DEVICE  scannerizzati dal bluetooth
    @Override
    protected String doInBackground(String... voids) {
        // Al var args passo un array di dim 4 con le informazioni da inviare al server
        if(voids.length == INPUT_DIM) {

            System.out.println("dentro if");
            final String id = voids[0];
            final String bMac = voids[1];
            final String loc = voids[2];
            final String bluefoundd = String.valueOf(voids[3]);
            Log.e("NUMERO BLUETOOTH", bluefoundd );




            // STRINGA costruita che viene passata al server
            String data = "id=" + id + "&bmac=" + bMac + "&loc=" + loc + "&blueFound=" + bluefoundd + "&timeStamp=1"; //ricordare timestamp e`su server messo non qui

            String text = "";
            BufferedReader reader = null;


            // Log di controllo
            Log.e("ASYNC SEND", "LOCAZIONE " + loc);

            URL url ;
            HttpURLConnection conn = null;
            try {

                // Definisce l' URL dove inviare i dati
                url = new URL("https://circumflex-hub.000webhostapp.com/posti.php");

                // INVIO richiesta connessione

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
                // Disconnessione
                conn.disconnect();
            }

            return "";// true se fa tutto giusto
        }
        return ""; // else se errore
    }


    @Override
    protected void onPostExecute(String result) {//metodo che viene chiamato una volta completata la doinBackground

        Log.i("SEND", "send is finished");
    }






}

