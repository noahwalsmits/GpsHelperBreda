package com.b.gpshelperbreda.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.data.Waypoint;

import java.util.ArrayList;

public class WaypointActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PointsAdapter pointsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint);

        recyclerView = findViewById(R.id.waypoint_recyclerview_waypoints);

        pointsAdapter = new PointsAdapter((ArrayList<Waypoint>) getIntent().getExtras().getSerializable("WAYPOINTS"));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(pointsAdapter);
    }
}
