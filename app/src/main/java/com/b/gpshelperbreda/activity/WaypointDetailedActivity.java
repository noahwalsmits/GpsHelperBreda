package com.b.gpshelperbreda.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.data.Waypoint;

public class WaypointDetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint_detail);

        Waypoint waypoint = (Waypoint) getIntent().getSerializableExtra("WAYPOINT");

        TextView waypointText = findViewById(R.id.logoSubtext);
        waypointText.setText(waypoint.getName());

        TextView waypointDescription = findViewById(R.id.waypoint_description);
        waypointDescription.setText(waypoint.getDescription());

        ImageView imageView = findViewById(R.id.picture);
        if (waypoint.getPhotoIDs()[0] == 0)
        {
            imageView.setImageResource(R.drawable.photo_no_picture);
        } else{
            String name = "photo_" + waypoint.getPhotoIDs()[0];
            int resid = getResources().getIdentifier(name, "drawable", getPackageName());
            imageView.setImageResource(resid);

        }
    }
}
