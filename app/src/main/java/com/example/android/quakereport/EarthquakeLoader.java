package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by agent47 on 11/2/18.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String url = null;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(url != null) {
            return QueryUtils.fetchEarthquakeData(url);
        }
        return null;
    }

}
