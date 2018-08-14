package com.sunnygroup.backoffice.sunboxwebview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity {
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.ctx = this;

        final String machines = getIntent().getStringExtra("machines");
        final String name = getIntent().getStringExtra("name");
        final String id = getIntent().getStringExtra("id");

        String date_milliseconds = getIntent().getStringExtra("lastReadingDate");
        Long milliseconds = Long.parseLong(date_milliseconds);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Date last_reading = new Date(milliseconds);

        final String last_reading_date = formatter.format(last_reading);
        final String gross = getIntent().getStringExtra("gross");

        TextView vTitle = (TextView) findViewById(R.id.title);
        vTitle.setText(name);

        TextView vMachineTotal = (TextView) findViewById(R.id.machine_total);
        vMachineTotal.setText(machines);

        TextView vLastCollection = (TextView) findViewById(R.id.last_collection);
        //TextView vLastReading = (TextView) findViewById(R.id.last_reading_date);
        vLastCollection.setText(last_reading_date);
        //vLastReading.setText(last_reading_date);

        TextView vPrimaryGross = (TextView) findViewById(R.id.primary_gross);
        //TextView vSecondaryGross = (TextView) findViewById(R.id.secondary_gross);

        vPrimaryGross.setText("$ " + gross);
        //vSecondaryGross.setText("$ " + gross);

        final Button machine_status_btn = findViewById(R.id.view_machine_status);
        //final Button location_details_btn = findViewById(R.id.view_location_details);

        machine_status_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                Intent i = new Intent(ctx, SlotMachineActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

//        location_details_btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick (View v) {
//                Intent i = new Intent(ctx, LocationDetailsActivity.class);
//                i.putExtra("id", id);
//                startActivity(i);
//            }
//        });
    }

}
