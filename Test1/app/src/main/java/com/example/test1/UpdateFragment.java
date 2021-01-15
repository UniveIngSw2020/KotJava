package com.example.test1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

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

        final View view = inflater.inflate(R.layout.fragment_update, container, false);

        //Per la prima stringa cambiamo il testo in base alla versione attuale
        TextView tv1 = view.findViewById( R.id.tv_update1 );
        tv1.setText( getString(R.string.update_version, BuildConfig.VERSION_NAME) );

        //Per la seconda verifichiamo la presenza di aggiornamenti e modifichiamo di conseguenza
        if( getContext() != null ) {
            // Creates instance of the manager.
            AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getContext() );
            // Oggetto che controlla gli aggiornamenti
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            // Verifica che la piattaforma permetta l'aggiornamento
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                        TextView tv2 = view.findViewById( R.id.tv_update2 );
                        tv2.setText( getText(R.string.update_available) );
                    }
                }
            });
        }
        return view;
    }
}