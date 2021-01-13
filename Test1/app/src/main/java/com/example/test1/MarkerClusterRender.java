package com.example.test1;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MarkerClusterRender extends DefaultClusterRenderer<MyItem> {

    //modificato il render di default perchè mostri il numero esatto di dispositivi
    MarkerClusterRender(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        // cambia le opzioni del marker prima che venga stampato
       // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pins));

    }

    @Override
    protected void onClusterItemRendered(MyItem item, Marker marker) {
        //metodo che imposta le opzioni del marker

    }

    @Override
    protected String getClusterText(int bucket) {
        return String.valueOf(bucket);
    }

    @Override
    protected int getBucket(Cluster<MyItem> cluster) {
        return cluster.getSize();
    }
}