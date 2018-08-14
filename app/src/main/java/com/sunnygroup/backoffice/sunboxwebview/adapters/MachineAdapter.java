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
import com.sunnygroup.backoffice.sunboxwebview.MachineDetailsActivity;
import com.sunnygroup.backoffice.sunboxwebview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.ViewHolder> {
    private JSONObject machineAssetNumbers;
    private JSONArray machines;
    private Context ctx;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView machineID;
        public final TextView asset_number;
        public final TextView meter_IN;
        public final TextView meter_OUT;
        public final TextView gross;
        public final TextView online;
        public final TextView offline;
        public JSONObject machine;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            machineID = (TextView) view.findViewById(R.id.machine_id);
            asset_number = (TextView) view.findViewById(R.id.asset_number);
            meter_IN = (TextView) view.findViewById(R.id.meter_IN);
            meter_OUT = (TextView) view.findViewById(R.id.meter_OUT);
            gross = (TextView) view.findViewById(R.id.gross);
            online = (TextView) view.findViewById(R.id.machine_online);
            offline = (TextView) view.findViewById(R.id.machine_offline);
        }
    }

    public MachineAdapter (JSONObject machines) {
        try {
            this.machines = machines.getJSONObject("body").getJSONArray("slotMachines");
            this.machineAssetNumbers = getMachineAssetNumbers(this.machines);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_slot_machine, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        try {
            holder.machine = machines.getJSONObject(position);
            holder.machineID.setText(holder.machine.getString("id"));
            holder.asset_number.setText(holder.machine.getString("companyAssetNumber"));

            JSONObject collectionType = holder.machine.getJSONObject("collectionType");
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

            holder.meter_IN.setText(in);
            holder.meter_OUT.setText(out);

            holder.online.setVisibility(View.VISIBLE);
            holder.offline.setVisibility(View.GONE);

            int current_gross = (Integer.parseInt(in) - Integer.parseInt(prev_in)) - (Integer.parseInt(out) - Integer.parseInt(prev_out));
            holder.gross.setText("$ " + current_gross);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                Context ctx = v.getContext();

                TextView tv =  (TextView) v.findViewById(R.id.machine_id);
                TextView tv2 = (TextView) v.findViewById(R.id.asset_number);

                String asset_number = tv2.getText().toString();
                String id = tv.getText().toString();

                Intent i = new Intent(ctx, MachineDetailsActivity.class);
                i.putExtra("asset_number", asset_number);
                i.putExtra("id", id);
                ctx.startActivity(i);
                }
            });

        } catch (JSONException e) {
            System.out.println(e);
        }
    }

    @Override
    public int getItemCount () { return this.machines.length(); }

    public JSONObject getMachineAssetNumbers(JSONArray machines) {
        JSONObject object = new JSONObject();
        JSONArray assetNumber = new JSONArray();

        try {
            for (int i = 0; i < machines.length(); i++) {
                assetNumber.put(machines.getJSONObject(i).getString("companyAssetNumber"));
            }
            object.put("set", assetNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
