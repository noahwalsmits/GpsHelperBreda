package com.b.gpshelperbreda.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.data.Waypoint;

import java.util.List;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    private List<Waypoint> waypoints;

    public PointsAdapter(List<Waypoint> waypoints){
        this.waypoints = waypoints;
    }

    //Deze klasse geeft aan dat de recyclerview gevult moet worden met de mural
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_waypoints, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    //Deze methode vult de mural met informatie
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Waypoint waypoint = waypoints.get(position);

        TextView textTitle = holder.waypointTitle;
        TextView textInfo = holder.waypointDescription;
        ImageView imageView = holder.waypointImage;

        textTitle.setText(waypoint.getName());
        textInfo.setText(waypoint.getDescription());
        if (waypoint.getPhotoIDs()[0] == 0) {
            imageView.setImageResource(R.drawable.photo_no_picture);
        } else {
            String name = "photo_" + waypoint.getPhotoIDs()[0];
            int resid = holder.itemView.getResources().getIdentifier(name, "drawable",holder.itemView.getContext().getPackageName());
            imageView.setImageResource(resid);
            //TODO Fotos implementeren
        }
        //TODO: vullen met de info uit de waypoint klassen zodra die bestaat.

        // zoals textTitle.setText(waypoint.getTitle);
    }

    //Deze methode stuurt terug hoeveel waypoinst er zijn
    @Override
    public int getItemCount() {
        return this.waypoints.size();
    }

    //Deze klasse benoemd de objecten uit de mural xml
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView waypointTitle;
        public TextView waypointDescription;
        public ImageView waypointImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            waypointTitle = itemView.findViewById(R.id.waypoint_title);
            waypointDescription = itemView.findViewById(R.id.waypoint_description);
            waypointImage = itemView.findViewById(R.id.waypoint_image);
        }
    }
}
