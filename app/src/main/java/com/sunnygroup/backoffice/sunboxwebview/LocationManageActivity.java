package com.sunnygroup.backoffice.sunboxwebview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sunnygroup.backoffice.sunboxwebview.adapters.LocationAdapter;

import org.json.JSONObject;

public class LocationManageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_manage);
        this.ctx = this;

        getUserLocations();
        showProgress(true);
    }

    protected void getUserLocations () {
        String id = getIntent().getStringExtra("id");
        String accountType = getIntent().getStringExtra("accountType");

        switch (accountType) {
            case "licensee":
                getLicenseeLocations(id);
                break;
        }
    }

    protected void getLicenseeLocations (String id) {
        int socketTimeout = 30000;

        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );

        recyclerView = (RecyclerView) findViewById(R.id.location_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MyVolleyRequest mvr = MyVolleyRequest.getInstance(this);
        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, "http://www.backoffice.ltd/location/gm/" + id, null, new Response.Listener<JSONObject>() {
        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, "http://192.168.1.104:8081/location/gm/" + id, null, new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, "http://www.backoffice.ltd/location/all/", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                showProgress(false);
                System.out.println(">>>>>>>>>>>>>> LOCATIONS" + response);
                adapter = new LocationAdapter(response);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", "Line 66 | LocationManageActivity.java: " + error);

            }
        });

        jsonObjectRequest.setRetryPolicy(policy);

        mvr.addToRequestQueue(jsonObjectRequest);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        final View location_list = findViewById(R.id.location_list);
        final View loader = findViewById(R.id.login_progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            location_list.setVisibility(show ? View.GONE : View.VISIBLE);
            location_list.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    location_list.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loader.setVisibility(show ? View.VISIBLE : View.GONE);
            loader.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loader.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loader.setVisibility(show ? View.VISIBLE : View.GONE);
            location_list.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}