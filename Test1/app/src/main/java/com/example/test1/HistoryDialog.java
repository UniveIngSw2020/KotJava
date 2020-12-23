package com.example.test1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Map;

public class HistoryDialog extends AppCompatDialogFragment {
    public static final String SHARED_PREF = "sharedpref";

    @Override
   public Dialog onCreateDialog(Bundle savedInstancedState){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        //Map<String, String > map =
        ArrayAdapter<String>array = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);

        dialog.setTitle("Recent place visited")
                .setAdapter(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("sku", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        return dialog.create();
    }

    // save recenti
    public void saveData(Location location, String namelocation){
        SharedPreferences sharedpref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();

        if(!sharedpref.contains(namelocation))
            editor.putString("namelocation", location.toString());
    }

    //prende tutti i recenti salvati
    public Map<String, ?> get_all_shared(){
        SharedPreferences sharedpref = getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        return  sharedpref.getAll();
    }



}
