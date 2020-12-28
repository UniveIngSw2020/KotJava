package com.example.test1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatDialogFragment;

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




}
