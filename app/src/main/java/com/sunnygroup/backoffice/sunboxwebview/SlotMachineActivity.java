package com.sunnygroup.backoffice.sunboxwebview;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sunnygroup.backoffice.sunboxwebview.adapters.MachineAdapter;

import org.json.JSONObject;

public class SlotMachineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_machine);
        this.ctx = this;

        final String id = getIntent().getStringExtra("id");
        getLocationMachines(id);
    }

    protected void getLocationMachines (String id) {
        recyclerView = (RecyclerView) findViewById(R.id.machine_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MyVolleyRequest mvr = MyVolleyRequest.getInstance(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest

                 (Request.Method.GET, "http://www.backoffice.ltd/location/" + id, null, new Response.Listener<JSONObject>() {
                //(Request.Method.GET, "http://192.168.1.104:8081/location/" + id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        adapter = new MachineAdapter(response);
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

        mvr.addToRequestQueue(jsonObjectRequest);
    }

}
