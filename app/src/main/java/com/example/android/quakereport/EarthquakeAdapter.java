package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by agent47 on 10/2/18.
 */

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {

    private ArrayList<Earthquake> earthquakes;
    private Listener listener;

    interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout rl;

        public ViewHolder(LinearLayout rl) {
            super(rl);
            this.rl = rl;
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public EarthquakeAdapter(ArrayList<Earthquake> earthquakes) {
        this.earthquakes = earthquakes;
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    @Override
    public EarthquakeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        LinearLayout rl = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(rl);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LinearLayout rl = holder.rl;

        TextView magnitudeView = rl.findViewById(R.id.magnitude);
        magnitudeView.setText(String.valueOf(new DecimalFormat("0.0").format(earthquakes.get(position).getMagnitude())));
        GradientDrawable magnitudeCircle = (GradientDrawable)magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(holder.rl.getContext(),earthquakes.get(position).getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        String location = earthquakes.get(position).getPlace();
        String offset,primaryLocation;

        if(location.contains(" of ")) {
            String a[] = location.split(" of ");
            offset = a[0] + " of ";
            primaryLocation = a[1];
        }
        else {
            offset = "Near The";
            primaryLocation = location;
        }

        TextView offsetView = rl.findViewById(R.id.offset);
        offsetView.setText(offset);

        TextView placeView = rl.findViewById(R.id.place);
        placeView.setText(primaryLocation);

        Date stamp = new Date(earthquakes.get(position).getDate());

        TextView dateView = rl.findViewById(R.id.date);
        String date = new SimpleDateFormat("MMM dd, yyyy").format(stamp);
        dateView.setText(date);

        TextView timeView = rl.findViewById(R.id.time);
        String time = new SimpleDateFormat("h:mm a").format(stamp);
        timeView.setText(time);

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    private int getMagnitudeColor(Context context, double mag) {
        int magnitude = (int)Math.floor(mag);
        int color;
        switch (magnitude) {
            case 0:
            case 1:
                color = R.color.magnitude1;
                break;
            case 2:
                color = R.color.magnitude2;
                break;
            case 3:
                color = R.color.magnitude3;
                break;
            case 4:
                color = R.color.magnitude4;
                break;
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            default:
                color = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(context,color);
    }

}
 
