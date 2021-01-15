package com.example.test1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {
    private  JSONArray ids=null;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_id, tv_number;

        //Assegna ai campi il valore dell'id delle rispettive TextView
        public ViewHolder( View view ) {
            super(view);
            tv_id = view.findViewById(R.id.tv_id);
            tv_number = view.findViewById(R.id.tv_number);
        }

        public TextView getDeviceIdTV() {
            return tv_id;
        }

        public TextView getScanNumberTV() {
            return tv_number;
        }
    }

    public StatsAdapter(JSONArray dataSetId) {
        ids = dataSetId;

    }

    @NonNull
    @Override
    public StatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.devices_list, parent, false);
        return new StatsAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull com.example.test1.StatsAdapter.ViewHolder viewHolder,  int position ) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        try {
            viewHolder.getDeviceIdTV().setText(ids.getJSONObject(position).optString("id"));
            viewHolder.getScanNumberTV().setText(ids.getJSONObject(position).optString("blueFound"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ids.length();
    }
}