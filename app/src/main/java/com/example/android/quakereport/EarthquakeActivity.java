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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private EarthquakeAdapter adapter;
    private String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        getSupportLoaderManager().initLoader(1,null,this);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i,Bundle bundle) {
        return new EarthquakeLoader(this,url);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader,final List<Earthquake> earthquakesList) {
        if (earthquakesList != null) {
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

            RecyclerView recyclerView = findViewById(R.id.list);
            recyclerView.setAdapter(adapter);

            LinearLayoutManager manager = new LinearLayoutManager(EarthquakeActivity.this);
            recyclerView.setLayoutManager(manager);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

    }

}
