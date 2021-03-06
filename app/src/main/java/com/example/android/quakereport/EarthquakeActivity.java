/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.quakereport.adapters.EarthQuakeAdapter;
import com.example.android.quakereport.entities.Earthquake;
import com.example.android.quakereport.utils.EarthquakeLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String TAG = "EarthquakeActivity";
    private static final String JSON_RESPONSE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final int ID = 0;
    private EarthQuakeAdapter mAdapter;
    private RecyclerView mEarthquakeListView;
    private TextView mView;
    private ProgressBar mProgressBar;
    private List<Earthquake> mData;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);



        // Find a reference to the {@link ListView} in the layout
        mEarthquakeListView = (RecyclerView) findViewById(R.id.recycler_list);

        mView = findViewById(R.id.text_earthquake);
        mProgressBar = findViewById(R.id.loading_spinner);

       checkConnectivity();
        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthQuakeAdapter(
                this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mEarthquakeListView.setAdapter(mAdapter);
        mEarthquakeListView.setLayoutManager(new LinearLayoutManager(this));



//        mEarthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                // Find the current earthquake that was clicked on
//                Earthquake currentEarthquake = mAdapter.getItem(position);
//
//                Uri geoUri = Uri.parse(currentEarthquake.getUrl());
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
//                startActivity(intent);
//            }
//        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        checkConnectivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.isDefaultNetworkActive()) {
            mProgressBar.setVisibility(View.VISIBLE);

            Log.v(TAG, "This is the Activity's onCreate Method");


            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            LoaderManager.getInstance(this).initLoader(ID, null, this);
        }
        else{
            mProgressBar.setVisibility(View.GONE);
            mView.setText(R.string.no_internet);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void displayData(List<Earthquake> earthquakeData) {
        mAdapter.notifyItemRemoved(earthquakeData.size());

        if (earthquakeData != null && !earthquakeData.isEmpty()) {

//            mData.addAll(earthquakeData);
            Log.d(TAG, "EarthQuake Data: " + earthquakeData);
            mAdapter.updateData((ArrayList<Earthquake>) earthquakeData);
            //mAdapter.addItem(earthquakeData.size(), earthquakeData.get(0));
            //mAdapter.notifyDataSetChanged();


        }
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v(TAG, "This is the onCreateLoader Method");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));


        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(JSON_RESPONSE_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {
        Log.v(TAG, "This is the onLoadFinished Method");
        displayData(data);
        mProgressBar.setVisibility(View.GONE);
        if(data == null || data.isEmpty()) {

            mView.setText(getString(R.string.no_earthquake_data_found));
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        Log.v(TAG, "This is the onLoaderReset Method");
        mAdapter.notifyItemRemoved(loader.getId());
    }

}
