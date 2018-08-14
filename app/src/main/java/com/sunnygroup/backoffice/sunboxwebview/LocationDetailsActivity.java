package com.sunnygroup.backoffice.sunboxwebview;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class LocationDetailsActivity extends AppCompatActivity {
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        this.ctx = this;

        LineChart chart = (LineChart) findViewById(R.id.machine_gross_chart);
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(10, 81));
        entries.add(new Entry(12, 111));
        entries.add(new Entry(14, 92));
        entries.add(new Entry(15, 77));
        entries.add(new Entry(20, 89));
        entries.add(new Entry(21, 83));
        entries.add(new Entry(27, 99));
        entries.add(new Entry(35, 95));
        entries.add(new Entry(43, 98));
        entries.add(new Entry(57, 84));

        LineDataSet dataset = new LineDataSet(entries, "2018");
        LineData lineData = new LineData(dataset);
        chart.setData(lineData);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.animateY(3000);
        chart.invalidate();
    }

}
