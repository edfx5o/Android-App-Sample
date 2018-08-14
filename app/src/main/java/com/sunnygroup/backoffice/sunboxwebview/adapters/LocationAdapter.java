package com.sunnygroup.backoffice.sunboxwebview.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunnygroup.backoffice.sunboxwebview.DashboardActivity;
import com.sunnygroup.backoffice.sunboxwebview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private JSONArray locations;
    private Context ctx;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView locationID;
        public final TextView locationTitle;
        public final TextView machineCount;
        public final TextView currentGross;
        public JSONObject location;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            locationID = (TextView) view.findViewById(R.id.location_id);
            locationTitle = (TextView) view.findViewById(R.id.location_name);
            machineCount = (TextView) view.findViewById(R.id.machine_count);
            currentGross = (TextView) view.findViewById(R.id.current_gross);
        }
    }

    public LocationAdapter (JSONObject locations) {
        try {
            this.locations = locations.getJSONArray("body");
        } catch (JSONException e) {
            System.out.println(e);
        }
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_location, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        try {
            holder.location = locations.getJSONObject(position);
            holder.locationID.setText(holder.location.getString("id"));
            holder.locationTitle.setText(holder.location.getString("name"));

            final String date = Long.toString(holder.location.getLong("lastReadingDate"));
            final int numMachines = holder.location.getJSONArray("slotMachines").length();
            int grossInt = getCurrentGross(holder.location.getJSONArray("slotMachines"));

            DecimalFormat df = new DecimalFormat("#,###");
            String grossStr = Integer.toString(grossInt);
            double g = Double.parseDouble(grossStr);
            final String gross = df.format(g);

            holder.machineCount.setText("" + numMachines);
            holder.currentGross.setText("$ " + gross);


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                Context ctx = v.getContext();

                TextView tv =  (TextView) v.findViewById(R.id.location_id);
                TextView tv2 = (TextView) v.findViewById(R.id.location_name);
                String name = tv2.getText().toString();
                String id = tv.getText().toString();

                Intent i = new Intent(ctx, DashboardActivity.class);

                i.putExtra("machines", "" + numMachines);
                i.putExtra("lastReadingDate", date);
                i.putExtra("gross", gross);
                i.putExtra("name", name);
                i.putExtra("id", id);
                ctx.startActivity(i);
                }
            });

        } catch (JSONException e) {
            System.out.println(">>>>>>>>>>>>>>>> LOCATION ADAPTER" + e);
        }
    }

    @Override
    public int getItemCount () { return this.locations.length(); }

    protected int getCurrentGross (JSONArray machines) {
        int current_gross = 0;

        try {

            for (int i = 0; i < machines.length(); i++) {

                JSONObject collectionType = machines.getJSONObject(i).getJSONObject("collectionType");
                JSONArray setFields = collectionType.getJSONArray("setFields");
                JSONObject meter1 = setFields.getJSONObject(0);
                JSONObject meter2 = setFields.getJSONObject(1);

                String in;
                String out;
                String prev_in;
                String prev_out;

                if (meter1.getString("alias").equalsIgnoreCase("IN") && meter1.getString("hardMeter").equalsIgnoreCase("true")) {
                    in = meter1.getJSONObject("lastReading").getString("currentHardMeter");
                    prev_in = meter1.getJSONObject("lastReading").getString("previousHardMeter");
                    out = meter2.getJSONObject("lastReading").getString("currentHardMeter");
                    prev_out = meter2.getJSONObject("lastReading").getString("previousHardMeter");
                } else if (meter2.getString("alias").equalsIgnoreCase("IN") && meter2.getString("hardMeter").equalsIgnoreCase("true")) {
                    in = meter2.getJSONObject("lastReading").getString("currentHardMeter");
                    prev_in = meter2.getJSONObject("lastReading").getString("previousHardMeter");
                    out = meter1.getJSONObject("lastReading").getString("currentHardMeter");
                    prev_out = meter1.getJSONObject("lastReading").getString("previousHardMeter");
                } else if (meter1.getString("alias").equalsIgnoreCase("IN")) {
                    in = meter1.getJSONObject("lastReading").getString("currentSoftMeter");
                    prev_in = meter1.getJSONObject("lastReading").getString("previousHardMeter");
                    out = meter2.getJSONObject("lastReading").getString("currentSoftMeter");
                    prev_out = meter2.getJSONObject("lastReading").getString("previousHardMeter");
                } else {
                    in = meter2.getJSONObject("lastReading").getString("currentSoftMeter");
                    prev_in = meter2.getJSONObject("lastReading").getString("previousHardMeter");
                    out = meter1.getJSONObject("lastReading").getString("currentSoftMeter");
                    prev_out = meter1.getJSONObject("lastReading").getString("previousHardMeter");
                }

                current_gross += (Integer.parseInt(in) - Integer.parseInt(prev_in)) - (Integer.parseInt(out) - Integer.parseInt(prev_out));

            }
        } catch (JSONException e) {
            System.err.println(e);
        }

        return current_gross;
    }


}
