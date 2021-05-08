package com.example.android.quakereport.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.quakereport.R;
import com.example.android.quakereport.entities.Earthquake;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EarthQuakeAdapter extends RecyclerView.Adapter<EarthQuakeAdapter.ViewHolder> {

    private static final String TAG = "EarthQuakeAdapter";
    ArrayList<Earthquake> mQuakes;
    Context context;

    public EarthQuakeAdapter(@NonNull Context context, ArrayList<Earthquake> arrayListOfQuakes) {
        super();
        this.mQuakes = arrayListOfQuakes;
        this.context = context;
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
//        }
//
//        Earthquake currentQuake = getItem(position);
//
//        TextView magnitude = convertView.findViewById(R.id.textViewMagnitude);
//
//        GradientDrawable drawable = (GradientDrawable) magnitude.getBackground();
//
//        int magnitudeColor = getMagnitudeColor(currentQuake.getMagnitude());
//
//        // Set the color on the magnitude circle
//        drawable.setColor(magnitudeColor);
//
//        DecimalFormat decimalFormat = new DecimalFormat("0.0");
//        String mag = decimalFormat.format(currentQuake.getMagnitude());
//
//        magnitude.setText(mag);
//
//        TextView offset = convertView.findViewById(R.id.textViewOffset);
//        offset.setText(getOffsetLocation(currentQuake.getLocation()));
//
//        TextView location = convertView.findViewById(R.id.textViewLocation);
//        location.setText(getPrimaryLocation(currentQuake.getLocation()));
//
//        TextView date = convertView.findViewById(R.id.textViewDate);
//        date.setText(getStringDate(currentQuake.getDate()));
//
//        TextView time = convertView.findViewById(R.id.textViewTime);
//        time.setText(getStringTime(currentQuake.getDate()));
//
//        return convertView;
//    }

    private int getMagnitudeColor(double magnitude) {
        int resColorId = 0;
        int magnitudeNumber = (int) Math.floor(magnitude);

        switch (magnitudeNumber){
            case 1:
                resColorId = R.color.magnitude1;
                break;
            case 2:
                resColorId = R.color.magnitude2;
                break;
            case 3:
                resColorId = R.color.magnitude3;
                break;
            case 4:
                resColorId = R.color.magnitude4;
                break;
                case 5:
                resColorId = R.color.magnitude5;
                break;
            case 6:
                resColorId = R.color.magnitude6;
                break;
                case 7:
                resColorId = R.color.magnitude7;
                break;
            case 8:
                resColorId = R.color.magnitude8;
                break;
            case 9:
                resColorId = R.color.magnitude9;
                break;
            default:
                resColorId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context, resColorId);
    }

    public String getOffsetLocation(String location) {
        String locationOffset = "";
        if (location.contains("of")) {
            locationOffset = location.substring(0, location.indexOf(" of")) + " of";
        } else {
            locationOffset = context.getString(R.string.near_the);
        }
        return locationOffset;
    }

    public String getPrimaryLocation(String location) {
        String[] pryLocation = {"", ""};
        if (location.contains("of")) {
            pryLocation = location.split(" of ");
        } else {
            pryLocation = new String[]{location};
            return pryLocation[0];
        }
        return pryLocation[1];
    }

    public String getStringTime(Long timeLong) {

        //Convert epoch time to date string.
        Date time = new Date(timeLong);
        DateFormat format = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedTime = format.format(time);

        return formattedTime;
    }

    public String getStringDate(Long dateLong) {

        //Convert epoch time to date string.
        Date date = new Date(dateLong);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = format.format(date);

        return formattedDate;
    }

    @NonNull
    @Override
    public EarthQuakeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EarthQuakeAdapter.ViewHolder holder, int position) {
        final Earthquake currentQuake = mQuakes.get(position);

        TextView magnitude = holder.magnitude;

        GradientDrawable drawable = (GradientDrawable) magnitude.getBackground();

        int magnitudeColor = getMagnitudeColor(currentQuake.getMagnitude());

        // Set the color on the magnitude circle
        drawable.setColor(magnitudeColor);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String mag = decimalFormat.format(currentQuake.getMagnitude());

        magnitude.setText(mag);

        TextView offset = holder.offset;
        offset.setText(getOffsetLocation(currentQuake.getLocation()));

        TextView location = holder.location;
        location.setText(getPrimaryLocation(currentQuake.getLocation()));

        TextView date = holder.date;
        date.setText(getStringDate(currentQuake.getDate()));

        TextView time = holder.time;
        time.setText(getStringTime(currentQuake.getDate()));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Find the current earthquake that was clicked on

                Uri geoUri = Uri.parse(currentQuake.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.mQuakes.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView magnitude;
        TextView offset, location, date, time;
        View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.rootView = itemView.findViewById(R.id.parent_view);
            this.magnitude = itemView.findViewById(R.id.textViewMagnitude);
            this.offset = itemView.findViewById(R.id.textViewOffset);
            this.location = itemView.findViewById(R.id.textViewLocation);
            this.date = itemView.findViewById(R.id.textViewDate);
            this.time = itemView.findViewById(R.id.textViewTime);
        }
    }

    public void updateData(ArrayList<Earthquake> arrayListQuakes) {
        mQuakes.clear();
        mQuakes.addAll(arrayListQuakes);
        notifyDataSetChanged();
    }
    public void addItem(int position, Earthquake earthquake) {
        mQuakes.add(position, earthquake);
        notifyItemInserted(position);
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(mQuakes.size() == 0){
//
//        }
//        return super.getItemViewType(position);
//    }

    public void removeItem(int position) {
        mQuakes.remove(position);
        notifyItemRemoved(position);
    }
}
