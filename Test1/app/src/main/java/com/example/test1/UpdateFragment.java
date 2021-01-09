package com.example.test1;

import android.os.Bundle;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UpdateFragment extends Fragment {

    public UpdateFragment() {
        super(R.layout.fragment_update);
    }

    public static UpdateFragment newInstance() {
        return new UpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//Sta roba la metto qui perchè altrimenti ho errore sul findViewById()
        //view è final perchè altrimenti non posso usarla nelle anonymous class
        final View view = inflater.inflate(R.layout.fragment_update, container, false);

        //Per la prima stringa cambiamo il testo in base alla versione attuale
        TextView tv1 = view.findViewById( R.id.tv_update1 );
        tv1.setText( getString(R.string.update_version, BuildConfig.VERSION_NAME) );

        //Per la seconda verifichiamo la presenza di aggiornamenti e modifichiamo di conseguenza
        if( getContext() != null ) {
            // Creates instance of the manager.
            AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getContext() );
            // Returns an intent object that you use to check for an update.
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        /*&& appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)*/) {
                        TextView tv2 = view.findViewById( R.id.tv_update2 );
                        tv2.setText( getText(R.string.update_available) );
                    }
                }
            });
        }

        // Inflate the layout for this fragment
        return view;
    }
}