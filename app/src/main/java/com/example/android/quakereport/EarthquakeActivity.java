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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        final ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        EarthquakeAdapter adapter = new EarthquakeAdapter(earthquakes);
        adapter.setListener(new EarthquakeAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(earthquakes.get(position).getUrl()));
                String title = "View";
                Intent choose = Intent.createChooser(intent,title);
                startActivity(choose);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }
}
