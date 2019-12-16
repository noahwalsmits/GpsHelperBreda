package com.b.gpshelperbreda.activity;

import android.content.Context;
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
    private Context context;
    private LayoutInflater layoutInflater;
    private View pointView;
    private ViewHolder viewHolder;

    private Waypoint waypoint;

    public PointsAdapter(List<Waypoint> waypoints){
        this.waypoints = waypoints;
    }

    //Deze klasse geeft aan dat de recyclerview gevult moet worden met de mural
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        this.pointView = layoutInflater.inflate(R.layout.activity_waypoint_mural, parent, false);
        this.viewHolder = new ViewHolder(pointView);

        return viewHolder;
    }

    //Deze methode vult de mural met informatie
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.waypoint = waypoints.get(position);
        TextView textTitle = viewHolder.muralTitle;
        TextView textInfo = viewHolder.muralInfo;
        ImageView imageMural = viewHolder.muralImage;

        textTitle.setText(waypoint.getName());
        textInfo.setText(waypoint.getDescription());
        imageMural = null;
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

        public TextView muralTitle;
        public TextView muralInfo;
        public ImageView muralImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            muralTitle = itemView.findViewById(R.id.waypointmural_text_mural_title);
            muralInfo = itemView.findViewById(R.id.waypointmural_text_muralinfo);
            muralImage = itemView.findViewById(R.id.waypointmural_imageview_muralimage);

        }
    }
}
