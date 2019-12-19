package com.b.gpshelperbreda.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
//        waypointDescription.setText(waypoint.getDescription());
        String descriptionTitle = "description_" + waypoint.getSequenceID();
        int resid = getResources().getIdentifier(descriptionTitle, "string", getPackageName());
        waypointDescription.setText(resid);

        String descriptionTitle = "description_" + waypoint.getSequenceID();
        int resid = getResources().getIdentifier(descriptionTitle, "string", getPackageName());
        waypointDescription.setText(resid);

        ImageView imageView = findViewById(R.id.picture);
        if (waypoint.getPhotoIDs()[0] == 0) {
            //imageView.setImageResource(R.drawable.photo_no_picture);
            imageView.setVisibility(View.GONE);
        } else {
            String name = "photo_" + waypoint.getPhotoIDs()[0];
            int resid2 = getResources().getIdentifier(name, "drawable", getPackageName());
            imageView.setImageResource(resid2);

        }
    }
}
