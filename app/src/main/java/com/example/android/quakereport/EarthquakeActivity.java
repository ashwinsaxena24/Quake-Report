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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private EarthquakeAdapter adapter;
    private String url = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button retry = findViewById(R.id.retry_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkConnection()) {
                    return;
                }
                else {
                    findViewById(R.id.retry_button).setVisibility(View.GONE);
                    findViewById(R.id.progress).setVisibility(View.VISIBLE);
                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                    getSupportLoaderManager().initLoader(1,null,EarthquakeActivity.this);
                }
            }
        });
        if(!checkConnection()) {
            TextView emptyView = findViewById(R.id.empty_view);
            emptyView.setText(getResources().getString(R.string.no_connection));
            emptyView.setVisibility(View.VISIBLE);
            (findViewById(R.id.progress)).setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
            return;
        }

        getSupportLoaderManager().initLoader(1,null,this);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i,Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Uri baseUri = Uri.parse(url);
        String minMag = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit","10");
        builder.appendQueryParameter("minmag",minMag);
        builder.appendQueryParameter("orderby",orderBy);
        return new EarthquakeLoader(this,builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader,final List<Earthquake> earthquakesList) {
        RecyclerView recyclerView = findViewById(R.id.list);
        TextView emptyView = findViewById(R.id.empty_view);
        ProgressBar bar = findViewById(R.id.progress);
        bar.setVisibility(View.GONE);
        if (earthquakesList != null && !    earthquakesList.isEmpty()) {
            adapter = new EarthquakeAdapter((ArrayList) earthquakesList);
            adapter.setListener(new EarthquakeAdapter.Listener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(earthquakesList.get(position).getUrl()));
                    String title = "View in browser";
                    Intent choose = Intent.createChooser(intent, title);
                    startActivity(choose);
                }
            });

            recyclerView.setAdapter(adapter);

            LinearLayoutManager manager = new LinearLayoutManager(EarthquakeActivity.this);
            recyclerView.setLayoutManager(manager);
        }
        else {
            emptyView.setText(getResources().getString(R.string.no_data));
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(1,null,this);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getSupportLoaderManager().restartLoader(1,null,this);
    }

}
