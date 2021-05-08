package com.example.android.quakereport.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.example.android.quakereport.entities.Earthquake;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String TAG = "EarthquakeLoader";
    private String mUrl;

    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
        Log.v(TAG, "This is the onStartLoading Method");
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<Earthquake> earthquakes = QueryUtils.fetchData(mUrl);
        Log.d(TAG, "EarthQuakes Object: " + earthquakes.toString());

        return earthquakes;
    }
}
