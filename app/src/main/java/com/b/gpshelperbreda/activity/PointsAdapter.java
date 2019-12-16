package com.b.gpshelperbreda.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.data.Waypoint;

import java.util.List;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder>
{

    private List<Waypoint> dataset;

    public PointsAdapter(List<Waypoint> waypoints)
    {
        this.dataset = waypoints;
    }

    //Deze klasse geeft aan dat de recyclerview gevult moet worden met de mural
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_waypoints, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, dataset.get(i));

        return viewHolder;
    }

    //Deze methode vult de mural met informatie
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Waypoint waypoint = dataset.get(position);

        TextView textTitle = holder.waypointTitle;
        TextView textVisited = holder.waypointVisited;
        ImageView imageView = holder.waypointImage;

        textTitle.setText(waypoint.getName());
        textVisited.setText(waypoint.isSeen() ? R.string.waypoint_check : R.string.waypoint_todo);

        if (waypoint.getPhotoIDs()[0] == 0)
        {
            imageView.setImageResource(R.drawable.photo_no_picture);
        } else
        {
            String name = "photo_" + waypoint.getPhotoIDs()[0];
            int resid = holder.itemView.getResources().getIdentifier(name, "drawable", holder.itemView.getContext().getPackageName());
            imageView.setImageResource(resid);
            //TODO Fotos implementeren
        }
        //TODO: vullen met de info uit de waypoint klassen zodra die bestaat.

        // zoals textTitle.setText(waypoint.getTitle);
    }

    //Deze methode stuurt terug hoeveel waypoinst er zijn
    @Override
    public int getItemCount()
    {
        return this.dataset.size();
    }

    //Deze klasse benoemd de objecten uit de mural xml
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView waypointTitle;
        TextView waypointVisited;
        ImageView waypointImage;
        CardView cardView;

        private Waypoint waypoint;

        public ViewHolder(@NonNull View itemView, final Waypoint waypoint)
        {
            super(itemView);
            this.waypoint = waypoint;

            cardView = itemView.findViewById(R.id.cardview);
            waypointTitle = itemView.findViewById(R.id.waypoint_title);
            waypointVisited = itemView.findViewById(R.id.waypoint_visited);
            waypointImage = itemView.findViewById(R.id.waypoint_image);

            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(view.getContext(), WaypointDetailedActivity.class);
                    intent.putExtra("WAYPOINT", waypoint);
                    view.getContext().startActivity(intent);
                }
            });

        }

    }
}
